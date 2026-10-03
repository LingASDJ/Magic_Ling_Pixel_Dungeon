package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.android.AndroidLauncher;
import com.shatteredpixel.shatteredpixeldungeon.scenes.BackupSaveScene;
import com.watabou.noosa.Game;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 安卓原生多线程查种的平台实现（SeedFinderLauncher 的线程版）。
 *
 * <p>安卓运行时无法再拉起独立的 ART 虚拟机，因此在应用进程内为每个工作线程创建一个
 * 独立的 DexClassLoader（parent 只挂框架 BootClassLoader），让工作线程加载一套完全私有的
 * 游戏类副本。世界生成依赖的全部全局静态状态（Dungeon / Random / 任务 Quest / Generator ……）
 * 随之按线程天然隔离，多个工作线程可以真正并发地各自跑完整的种子模拟，互不污染。</p>
 *
 * <p>协调逻辑完全复用 {@link SeedFinderCoordinator}（mmap 进度表、卡死看门狗、
 * hit/done/error 一次性文件），本类只实现"拉起 / 结束 / 存活检测"三个动作。</p>
 *
 * <p>安卓环境（APK 路径、native 目录、AssetManager、SharedPreferences）直接取自
 * {@link AndroidLauncher#instance}，不经过任何反射；拿不到（非安卓或未初始化）时
 * create() 返回 null，UI 自动回退单线程查找。</p>
 */
public class SeedFinderThreadLauncher implements SeedFinderLauncher {

    /** 并发上限：与旧安卓实现一致，兼顾内存与发热 */
    private static final int MAX_WORKERS = 8;

    /** 工作线程入口类名（由子加载器加载并反射调用） */
    private static final String WORKER_ENTRY =
            "com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.SeedFinderThreadWorker";

    private final AndroidEnv android;
    private final String runRoot;
    private final String checkBranches;

    /** 每个 worker 一个独立加载器与运行线程；按 index 缓存，重启复用加载器（避免重复类加载） */
    private final ClassLoader[] loaders;
    private final Thread[] threads;

    private SeedFinderThreadLauncher(AndroidEnv android, String runRoot, String checkBranches) {
        this.android = android;
        this.runRoot = runRoot;
        this.checkBranches = checkBranches;
        this.loaders = new ClassLoader[MAX_WORKERS];
        this.threads = new Thread[MAX_WORKERS];
    }

    /** 创建线程版启动器；非安卓环境或拿不到应用上下文/native 库时返回 null（调用方回退单线程查找） */
    public static SeedFinderLauncher create() {
        try {
            if (AndroidLauncher.instance == null) return null;
            ApplicationInfo info = AndroidLauncher.instance.getApplicationInfo();
            File filesDir = AndroidLauncher.instance.getFilesDir();
            if (info == null || info.sourceDir == null || filesDir == null) return null;
            // 安卓的 native 库按 ClassLoader 隔离：同一份 libgdx.so 被某个 ClassLoader 打开后，
            // 其他 ClassLoader 无法再打开它（Gdx2DPixmap 拿不到 native 实现）。
            // 因此每个 worker 都必须有一份独立副本（launch 时按 index 复制，路径不同=ART 视为不同库）。
            if (info.nativeLibraryDir == null || !new File(info.nativeLibraryDir, "libgdx.so").isFile()) {
                return null; // 设备上没有 libgdx native 库，回退单线程（主进程上下文 natives 可用）
            }
            AndroidEnv env = new AndroidEnv(
                    info.sourceDir,
                    info.nativeLibraryDir,
                    filesDir,
                    AndroidLauncher.instance.getAssets(),
                    AndroidLauncher.instance.getSharedPreferences(
                            BackupSaveScene.ANDROID_PREFS_NAME, Context.MODE_PRIVATE),
                    AndroidLauncher.instance);
            return new SeedFinderThreadLauncher(
                    env,
                    new File(filesDir, "seedfinder").getAbsolutePath(),
                    String.valueOf(SPDSettings.logBranch()));
        } catch (Throwable t) {
            return null;
        }
    }

    /** 为第 index 个 worker 复制独立 libgdx.so 副本（幂等，临时文件+改名保证原子性） */
    private static File ensureNativesCopy(int index, String nativeLibDir, File filesDir) throws Exception {
        if (nativeLibDir == null) return null;
        File src = new File(nativeLibDir, "libgdx.so");
        if (!src.isFile()) return null;
        File dir = new File(filesDir, "seedfinder/natives");
        if (!dir.isDirectory() && !dir.mkdirs()) return null;
        File dst = new File(dir, "libgdx-" + index + ".so");
        if (!dst.isFile() || dst.length() != src.length()) {
            File tmp = new File(dir, "libgdx-" + index + ".so.tmp");
            try (java.io.FileInputStream in = new java.io.FileInputStream(src);
                 java.io.FileOutputStream out = new java.io.FileOutputStream(tmp)) {
                byte[] buf = new byte[64 * 1024];
                int n;
                while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            }
            if (!tmp.renameTo(dst)) {
                if (!dst.delete() || !tmp.renameTo(dst)) return null;
            }
        }
        return dst;
    }

    @Override
    public int maxWorkers() {
        int cores = Runtime.getRuntime().availableProcessors();
        if (cores < 2) return 1;
        return Math.min(MAX_WORKERS, cores);
    }

    @Override
    public Map<String, String> platformParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("runRoot", runRoot);
        params.put("checkBranches", checkBranches);
        return params;
    }

    @Override
    public boolean launch(int index, File jobFile) {
        if (index < 0 || index >= MAX_WORKERS) return false;
        // 上一轮同槽位的线程必须真正退出，避免两个线程共享同一套静态状态
        Thread prev = threads[index];
        if (prev != null && prev.isAlive()) {
            prev.interrupt();
            try {
                prev.join(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        try {
            ClassLoader loader = loaderFor(index);
            if (loader == null) return false;
            // 每个 worker 一份独立 native 副本（per-ClassLoader 隔离，一份只够一个加载器用）
            File nativesCopy = ensureNativesCopy(index, android.nativeLibDir, android.filesDir);
            if (nativesCopy == null) return false;
            // 每个 worker 独立工作目录：local/external 文件与主进程存档隔离
            // （否则 Badges.loadGlobal 会读到主进程 badges.dat，且多 worker 并发写同一存档会损坏数据）
            File workDir = new File(android.filesDir, "seedfinder/work-" + index);
            if (!workDir.isDirectory() && !workDir.mkdirs()) return false;
            Thread t = new Thread(new WorkerRunnable(index, jobFile, loader,
                    android.assets, android.prefs, android.context,
                    nativesCopy.getAbsolutePath(), workDir.getAbsolutePath()),
                    "seed-finder-worker-" + index);
            t.setDaemon(true);
            threads[index] = t;
            t.start();
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public void kill(int index) {
        if (index < 0 || index >= MAX_WORKERS) return;
        Thread t = threads[index];
        if (t != null) t.interrupt();
    }

    @Override
    public boolean isAlive(int index) {
        if (index < 0 || index >= MAX_WORKERS) return false;
        Thread t = threads[index];
        return t != null && t.isAlive();
    }

    /** 取（或首次创建）第 index 个 worker 的独立加载器 */
    private ClassLoader loaderFor(int index) throws Exception {
        ClassLoader loader = loaders[index];
        if (loader != null) return loader;
        loader = createAndroidLoader(android);
        if (loader == null) return null;
        // 隔离校验：子加载器必须解析到它自己的 SeedFinder 副本。
        // 若得到主加载器的那份（说明委托链没隔离开），宁可失败回退单线程，也不冒险共享静态状态。
        Class<?> probe = Class.forName(SeedFinder.class.getName(), false, loader);
        if (probe == SeedFinder.class) return null;
        loaders[index] = loader;
        return loader;
    }

    /** parent 只含框架类的 DexClassLoader：应用类全部落到子加载器，静态状态按线程隔离 */
    private static ClassLoader createAndroidLoader(AndroidEnv env) throws Exception {
        ClassLoader bootParent = ClassLoader.getSystemClassLoader().getParent();
        if (bootParent == null) return null; // 拿不到仅含框架类的父加载器，无法保证隔离
        File odexDir = new File(env.filesDir, "seedfinder/odex");
        if (!odexDir.isDirectory() && !odexDir.mkdirs()) return null;
        Class<?> dcl = Class.forName("dalvik.system.DexClassLoader");
        Constructor<?> ctor = dcl.getConstructor(String.class, String.class, String.class, ClassLoader.class);
        return (ClassLoader) ctor.newInstance(
                env.apkPath, odexDir.getAbsolutePath(), env.nativeLibDir, bootParent);
    }

    /** 反射调用子加载器里的工作线程入口；异常写入 error 文件，协调器据此重启或放弃该 worker */
    private static final class WorkerRunnable implements Runnable {
        private final int index;
        private final File jobFile;
        private final ClassLoader loader;
        private final AssetManager assets;
        private final SharedPreferences prefs;
        private final ContextWrapper context;
        private final String nativesPath;
        private final String workDir;

        WorkerRunnable(int index, File jobFile, ClassLoader loader,
                       AssetManager assets, SharedPreferences prefs, ContextWrapper context,
                       String nativesPath, String workDir) {
            this.index = index;
            this.jobFile = jobFile;
            this.loader = loader;
            this.assets = assets;
            this.prefs = prefs;
            this.context = context;
            this.nativesPath = nativesPath;
            this.workDir = workDir;
        }

        @Override
        public void run() {
            try {
                Class<?> entry = Class.forName(WORKER_ENTRY, true, loader);
                Method main = entry.getMethod("main", Object[].class);
                // 把主进程已设置的 Game.version 传给 worker（worker 副本默认 null，
                // 而 Document.<clinit> → DeviceCompat.isDebug 会 String.contains 直接 NPE）
                main.invoke(null, (Object) new Object[]{jobFile.getAbsolutePath(), assets, prefs, context,
                        nativesPath, workDir, Game.version});
            } catch (Throwable t) {
                t.printStackTrace(); // 完整栈进 logcat，便于真机抓取（Rejecting re-init 不打印 cause）
                try {
                    File err = new File(jobFile.getParentFile(), "worker-" + index + ".error");
                    // 完整栈写入 error 文件（顶层消息可能为空，如 ExceptionInInitializerError）
                    java.io.StringWriter sw = new java.io.StringWriter();
                    t.printStackTrace(new java.io.PrintWriter(sw));
                    SeedFinderJob.writeAtomic(err, sw.toString());
                } catch (Throwable ignored) {
                }
            }
        }
    }

    /** 安卓平台信息：全部取自 AndroidLauncher.instance（不反射） */
    private static final class AndroidEnv {
        final String apkPath;
        final String nativeLibDir;
        final File filesDir;
        final AssetManager assets;
        final SharedPreferences prefs;
        final ContextWrapper context;

        AndroidEnv(String apkPath, String nativeLibDir, File filesDir,
                   AssetManager assets, SharedPreferences prefs, ContextWrapper context) {
            this.apkPath = apkPath;
            this.nativeLibDir = nativeLibDir;
            this.filesDir = filesDir;
            this.assets = assets;
            this.prefs = prefs;
            this.context = context;
        }
    }
}
