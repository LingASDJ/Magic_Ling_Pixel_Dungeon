package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Android 多进程查种：用 ProcessBuilder 拉起 app_process（Android 的 Java/Dalvik/ART 进程入口），
 * 子进程与父进程共享同一 APK（classpath 指向 base.apk），因此世界生成依赖的类与资源完全一致。
 *
 * 平台差异由反射处理（core 模块不依赖 Android SDK）：
 *   - APK 路径：ApplicationInfo.sourceDir
 *   - files 目录：Context.getFilesDir()
 *   - native lib 目录：ApplicationInfo.nativeLibraryDir（供子进程 System.load .so）
 *
 * 若反射获取上下文失败（如非 Android 环境），create() 返回 null，UI 自动回退到单进程查找。
 */
public class AndroidSeedFinderLauncher implements SeedFinderLauncher {

    private static final int MAX_WORKERS = 8;

    private final String apkPath;
    private final String filesDir;
    private final String nativeLibDir;
    private final String runRoot;
    private final ConcurrentHashMap<Integer, Process> processes = new ConcurrentHashMap<>();

    private AndroidSeedFinderLauncher(String apkPath, String filesDir, String nativeLibDir, String runRoot) {
        this.apkPath = apkPath;
        this.filesDir = filesDir;
        this.nativeLibDir = nativeLibDir;
        this.runRoot = runRoot;
    }

    /** 通过反射从 ActivityThread.currentApplication() 获取平台参数；非 Android 环境返回 null */
    public static SeedFinderLauncher create() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentApplication = activityThreadClass.getMethod("currentApplication");
            Object app = currentApplication.invoke(null);
            if (app == null) return null;

            Method getApplicationInfo = app.getClass().getMethod("getApplicationInfo");
            Object appInfo = getApplicationInfo.invoke(app);
            if (appInfo == null) return null;

            String apkPath = readField(appInfo, "sourceDir");
            String nativeLibDir = readField(appInfo, "nativeLibraryDir");

            Method getFilesDir = app.getClass().getMethod("getFilesDir");
            Object filesDirFile = getFilesDir.invoke(app);
            String filesDir = (String) filesDirFile.getClass().getMethod("getAbsolutePath").invoke(filesDirFile);

            if (apkPath == null || filesDir == null) return null;

            File runRoot = new File(filesDir, "seedfinder");
            return new AndroidSeedFinderLauncher(apkPath, filesDir, nativeLibDir, runRoot.getAbsolutePath());
        } catch (Throwable t) {
            return null;
        }
    }

    private static String readField(Object obj, String name) {
        Class<?> c = obj.getClass();
        while (c != null) {
            try {
                Field f = c.getDeclaredField(name);
                f.setAccessible(true);
                Object v = f.get(obj);
                return v == null ? null : v.toString();
            } catch (NoSuchFieldException ignored) {
                c = c.getSuperclass();
            } catch (Throwable t) {
                return null;
            }
        }
        return null;
    }

    @Override
    public int maxWorkers() {
        return MAX_WORKERS;
    }

    @Override
    public Map<String, String> platformParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("apkPath", apkPath);
        params.put("filesDir", filesDir);
        if (nativeLibDir != null) params.put("nativeLibDir", nativeLibDir);
        params.put("runRoot", runRoot);
        // 把父进程的查种设置透传给子进程（子进程无 SharedPreferences 访问权限）
        params.put("checkBranches", String.valueOf(SPDSettings.logBranch()));
        return params;
    }

    @Override
    public boolean launch(int index, File jobFile) {
        kill(index);
        try {
            File logFile = new File(jobFile.getParentFile(), "worker-" + index + ".log");
            ArrayList<String> cmd = new ArrayList<>();
            cmd.add(findAppProcess());
            // classpath 指向 APK：app_process 据此加载 DEX 中的应用类
            cmd.add("-Djava.class.path=" + apkPath);
            cmd.add("/");
            cmd.add(AndroidSeedFinderWorker.class.getName());
            cmd.add(jobFile.getAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder(cmd);
            // native lib 路径：让子进程能找到 libgdx.so 等
            if (nativeLibDir != null) {
                Map<String, String> env = pb.environment();
                String ld = env.get("LD_LIBRARY_PATH");
                env.put("LD_LIBRARY_PATH", ld == null ? nativeLibDir : nativeLibDir + ":" + ld);
            }
            pb.redirectErrorStream(true);
            Process p = pb.start();
            // ProcessBuilder.redirectOutput(File) 是 API 26+，这里用守护线程把合并输出写到日志
            drainToLog(p, logFile);
            processes.put(index, p);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 守护线程把子进程 stdout/stderr 写入日志文件，避免管道缓冲区写满后阻塞子进程 */
    private static void drainToLog(final Process p, final File logFile) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream in = p.getInputStream();
                byte[] buf = new byte[4096];
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(logFile);
                    int n;
                    while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
                } catch (IOException ignored) {
                } finally {
                    if (out != null) {
                        try { out.close(); } catch (IOException ignored) {}
                    }
                    try { in.close(); } catch (IOException ignored) {}
                }
            }
        }, "seedfinder-worker-log");
        t.setDaemon(true);
        t.start();
    }

    /**
     * Process.isAlive() 是 API 24+，用 exitValue() 兼容到 API 1：
     * 进程未结束时 exitValue() 抛 IllegalThreadStateException。
     */
    private static boolean processAlive(Process p) {
        try {
            p.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

    /** Android 上 app_process 的可能路径（按 64/32/通用顺序探测） */
    private static String findAppProcess() {
        String[] candidates = {
                "/system/bin/app_process64",
                "/system/bin/app_process32",
                "/system/bin/app_process"
        };
        for (String c : candidates) {
            if (new File(c).exists()) return c;
        }
        return "app_process";
    }

    @Override
    public void kill(int index) {
        Process p = processes.remove(index);
        // destroyForcibly() 是 API 26+；Android 上 Process.destroy() 本身即发 SIGKILL，效果一致
        if (p != null && processAlive(p)) p.destroy();
    }

    @Override
    public boolean isAlive(int index) {
        Process p = processes.get(index);
        return p != null && processAlive(p);
    }
}
