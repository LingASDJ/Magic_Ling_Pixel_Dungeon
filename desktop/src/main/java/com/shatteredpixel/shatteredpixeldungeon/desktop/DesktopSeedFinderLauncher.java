package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.SeedFinderLauncher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Desktop 多进程查种：用 ProcessBuilder 拉起独立 JVM（同 classpath）运行 DesktopSeedFinderWorker。
 * 子进程与父进程共享同一外部文件目录（basePath），因此挑战等设置完全一致。
 */
public class DesktopSeedFinderLauncher implements SeedFinderLauncher {

    /** 最多使用的子进程数 */
    private static final int MAX_WORKERS = 8;

    /**
     * 【临时·性能测试】子 JVM 参数：固定小堆 + SerialGC。
     * 消除"每进程默认堆=物理内存1/4"的内存压力与 G1 每 JVM ~8 条 GC 线程的超订开销（对照实验时改这里）。
     */
    private static final String[] WORKER_JVM_ARGS = { "-Xms128m", "-Xmx512m", "-XX:+UseSerialGC" };

    private final String basePath;
    private final String javaBin;
    private final String classPath;
    private final String runRoot;
    private final ConcurrentHashMap<Integer, Process> processes = new ConcurrentHashMap<>();

    private DesktopSeedFinderLauncher(String basePath, String javaBin, String classPath, String runRoot) {
        this.basePath = basePath;
        this.javaBin = javaBin;
        this.classPath = classPath;
        this.runRoot = runRoot;
    }

    /** classpath/运行环境不可用时返回 null，此时 UI 回退到单进程查找 */
    public static SeedFinderLauncher create(String basePath) {
        String classPath = System.getProperty("java.class.path");
        String javaHome = System.getProperty("java.home");
        String userHome = System.getProperty("user.home");
        if (basePath == null || classPath == null || classPath.isEmpty() || javaHome == null || userHome == null)
            return null;

        String exeName = System.getProperty("os.name", "").toLowerCase().contains("win") ? "java.exe" : "java";
        File javaBin = new File(new File(javaHome, "bin"), exeName);
        if (!javaBin.isFile()) return null;

        //与 libGDX 的 External 目录一致：user.home + basePath
        File runRoot = new File(new File(userHome, basePath), "seedfinder");
        return new DesktopSeedFinderLauncher(basePath, javaBin.getAbsolutePath(), classPath, runRoot.getAbsolutePath());
    }

    @Override
    public int maxWorkers() {
        //【临时·性能测试】固定放开到 8，使 1-8 进程滑块全部可选（允许超订，后续移除）
        return MAX_WORKERS;
    }

    @Override
    public Map<String, String> platformParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("basePath", basePath);
        params.put("runRoot", runRoot);
        return params;
    }

    @Override
    public boolean launch(int index, File jobFile) {
        kill(index);
        try {
            File logFile = new File(jobFile.getParentFile(), "worker-" + index + ".log");
            ArrayList<String> cmd = new ArrayList<>();
            cmd.add(javaBin);
            for (String arg : WORKER_JVM_ARGS) cmd.add(arg);
            cmd.add("-cp");
            cmd.add(classPath);
            cmd.add(DesktopSeedFinderWorker.class.getName());
            cmd.add(jobFile.getAbsolutePath());
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            pb.redirectOutput(logFile);
            Process p = pb.start();
            processes.put(index, p);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void kill(int index) {
        Process p = processes.remove(index);
        if (p != null && p.isAlive()) p.destroyForcibly();
    }

    @Override
    public boolean isAlive(int index) {
        Process p = processes.get(index);
        return p != null && p.isAlive();
    }
}