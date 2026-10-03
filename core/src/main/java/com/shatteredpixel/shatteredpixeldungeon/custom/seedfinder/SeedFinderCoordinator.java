package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

/**
 * 父进程侧的多进程协调器：把种子环 [0, TOTAL_SEEDS) 均分成 N 段，
 * 第 i 个子进程从第 i 段的段首起步、连续推进并在越过环尾时回绕到 0，各进程起点均匀分布在整个种子环上。
 * 轮询汇总各子进程的结果：进度经共享状态文件（mmap）读取，命中/完成/错误用一次性文件汇报。
 * 看门狗负责处理死循环种子：卡死 30 秒后换新进程重试同一颗种子，重试 2 次仍卡则跳过并记入 skipped.txt。
 */
public class SeedFinderCoordinator implements Runnable {

    /** 平台多进程支持；null 表示当前平台不支持（UI 回退到单进程线程查找） */
    public static volatile SeedFinderLauncher launcher;

    /** 正在运行的子进程数（UI 显示用） */
    public static volatile int activeWorkers = 0;

    /** 各子进程最近报告的种子快照（索引 = 进程号，未上报为 -1）；UI 并行进度显示用 */
    public static volatile long[] workerSeeds = null;

    /** 无进度推进超过该时长即判定该种子卡死 */
    private static final long HANG_TIMEOUT = 30000L;
    /** 同一颗卡死种子的最大重试次数（超过则跳过） */
    private static final int STUCK_RETRIES = 2;
    /** 单个子进程异常退出的重启上限 */
    private static final int MAX_RESTARTS = 8;
    private static final long POLL_INTERVAL = 200L;

    private final ArrayList<WantedTarget> targets;
    private final int floor;
    private final HeroClass heroClass;
    private final int workers;

    private volatile boolean stopped;

    private File runDir;
    private SeedFinderJob template;
    /** 与子进程共享的进度状态（内存映射） */
    private SeedFinderState state;
    private boolean anyFailed;
    private String failureInfo = "";
    /** 最终结果：种子码 / 物品清单 / "NONE" / 错误文案 */
    volatile String lastResult = "";
    /** 供测试覆盖起步种子；<0 表示按 findSeed() 的随机逻辑 */
    long startSeedOverride = -1;
    /** 【临时·性能测试】本轮扫描起点，用于计时，后续移除 */
    private long benchStartMs;

    public SeedFinderCoordinator(ArrayList<WantedTarget> targets, int floor, HeroClass heroClass, int workers) {
        this.targets = targets;
        this.floor = floor;
        this.heroClass = heroClass;
        this.workers = Math.max(1, workers);
    }

    /** 平台支持的最大子进程数（1 = 不支持多进程） */
    public static int maxWorkers() {
        ensureLauncher();
        SeedFinderLauncher l = launcher;
        return l == null ? 1 : Math.max(1, l.maxWorkers());
    }

    /**
     * 平台自动注册：desktop 在 DesktopLauncher 中注入 launcher，
     * Android 在此反射创建 AndroidSeedFinderLauncher（拿不到 Application 上下文时返回 null）。
     * 非 Android 环境下 AndroidSeedFinderLauncher.create() 直接返回 null，不影响 desktop。
     */
    private static void ensureLauncher() {
        if (launcher != null) return;
        synchronized (SeedFinderCoordinator.class) {
            if (launcher != null) return;
            SeedFinderLauncher l = AndroidSeedFinderLauncher.create();
            if (l != null) launcher = l;
        }
    }

    /** 自动档：核数 - 1，上限 4 */
    public static int defaultWorkers() {
        int n = Runtime.getRuntime().availableProcessors() - 1;
        if (n < 1) n = 1;
        return Math.min(n, Math.min(4, maxWorkers()));
    }

    /** 把配置值解析为实际进程数：<=0 为自动，其余取平台上限内的值 */
    public static int resolveWorkers(int setting) {
        int max = maxWorkers();
        if (setting <= 0) return defaultWorkers();
        return Math.max(1, Math.min(setting, max));
    }

    public void stop() {
        stopped = true;
        killAll();
    }

    @Override
    public void run() {
        String result = "";
        try {
            result = coordinate();
        } catch (Throwable t) {
            result = Messages.get(SeedFinder.class, "find_failed", t);
        } finally {
            activeWorkers = 0;
            workerSeeds = null;
        }
        lastResult = result;
        //被用户中止（打断 sleep 会抛 InterruptedException）时不展示任何结果
        if (stopped) return;
        if (SeedFindScene.INSTANCE != null && !result.isEmpty()) {
            SeedFindScene.INSTANCE.text = result;
            SeedFindScene.INSTANCE.needUpdate = true;
        }
    }

    private String coordinate() throws IOException, InterruptedException {
        ensureLauncher();
        if (launcher == null) return Messages.get(SeedFinder.class, "platform_missing");
        long firstSeed = startSeedOverride >= 0 ? startSeedOverride : deriveFirstSeed();
        benchStartMs = System.currentTimeMillis();//【临时·性能测试】

        Map<String, String> params = launcher.platformParams();
        String runRootPath = params.get("runRoot");
        if (runRootPath == null) return Messages.get(SeedFinder.class, "platform_missing");
        File runRoot = new File(runRootPath);
        cleanOldRuns(runRoot);
        runDir = new File(runRoot, "run-" + System.currentTimeMillis());
        if (!runDir.mkdirs() && !runDir.isDirectory())
            return Messages.get(SeedFinder.class, "run_dir_failed", runDir);
        state = SeedFinderState.forCoordinator(new File(runDir, SeedFinderState.FILE_NAME), workers);

        template = new SeedFinderJob();
        template.floor = floor;
        template.heroClass = heroClass.name();
        template.workers = workers;
        template.stride = 1;//环形切片：各进程在自己的段内连续推进
        template.reps = SeedFinder.CONFIRM_REPS;
        template.outDir = runDir.getAbsolutePath();
        template.gameVersion = Game.version;
        template.gameVersionCode = Game.versionCode;
        template.gameIsDebug = DeviceCompat.isDebug();
        template.params.putAll(params);
        for (WantedTarget t : targets)
            template.targetSpecs.add(SeedFinderJob.targetSpec(t));

        //把种子环 [0, TOTAL_SEEDS) 均分成 workers 段（余数分给靠前的进程），第 i 个进程从第 i 段的段首起步
        long slice = DungeonSeed.TOTAL_SEEDS / workers;
        long rem = DungeonSeed.TOTAL_SEEDS % workers;
        long offset = 0;
        long[] segStart = new long[workers];
        for (int i = 0; i < workers; i++) {
            segStart[i] = (firstSeed + offset) % DungeonSeed.TOTAL_SEEDS;
            offset += slice + (i < rem ? 1 : 0);
        }
        Worker[] ws = new Worker[workers];
        for (int i = 0; i < workers; i++) {
            ws[i] = new Worker(i, segStart[i]);
            launchWorker(ws[i]);
        }

        long hitSeed = -1;
        while (!stopped && hitSeed < 0) {
            int activeCount = 0;
            long frontier = Long.MAX_VALUE;

            for (Worker w : ws) {
                if (stopped || hitSeed >= 0) break;
                if (w.finished || w.failed) continue;

                long hit = readLong(fileOf(w, "hit"));
                if (hit >= 0) {
                    hitSeed = hit;
                    break;
                }

                if (fileOf(w, "done").exists()) {
                    w.finished = true;
                    launcher.kill(w.index);
                    continue;
                }

                File errFile = fileOf(w, "error");
                if (errFile.exists() && errFile.length() > 0) {
                    crashWorker(w, readText(errFile));
                    continue;
                }

                if (!launcher.isAlive(w.index)) {
                    crashWorker(w, Messages.get(SeedFinder.class, "process_exit"));
                    continue;
                }

                long p = state.seed(w.index);
                if (p >= 0) {
                    if (p != w.lastSeed) {
                        w.lastSeed = p;
                        w.lastChange = System.currentTimeMillis();
                        w.stuckRetries = 0;
                    } else if (System.currentTimeMillis() - w.lastChange > HANG_TIMEOUT) {
                        stuckWorker(w, p);
                        continue;
                    }
                }

                activeCount++;
                if (w.lastSeed >= 0 && w.lastSeed < frontier) frontier = w.lastSeed;
            }

            //快照各子进程当前种子供 UI 展示（整体换新数组，避免渲染线程读到半更新状态）
            long[] snapshot = new long[workers];
            for (int i = 0; i < workers; i++) snapshot[i] = ws[i].lastSeed;
            workerSeeds = snapshot;

            //【临时·性能测试】汇总各进程已扫描的种子数并刷新到界面
            long scanned = scanTotal(ws);
            if (SeedFindScene.INSTANCE != null) SeedFindScene.INSTANCE.scannedSeeds = scanned;

            if (stopped) break;
            if (hitSeed >= 0) {
                killAll();
                //【临时·性能测试】命中结果附带耗时与已扫描种子数
                return reportHit(hitSeed) + SeedFinder.scanStats(scanned, benchStartMs);
            }

            activeWorkers = activeCount;
            if (activeCount == 0) {
                if (anyFailed)
                    return Messages.get(SeedFinder.class, "interrupted", failureInfo, runDir);
                return "NONE" + SeedFinder.scanStats(scanned, benchStartMs);//【临时·性能测试】
            }
            //【临时·性能测试】满上限仍未命中则中断，如实报告已扫描的种子数
            if (System.currentTimeMillis() - benchStartMs >= SeedFinder.SEARCH_LIMIT_MS) {
                killAll();
                return Messages.get(SeedFinder.class, "not_found")
                        + SeedFinder.scanStats(scanned, benchStartMs)
                        + Messages.get(SeedFinder.class, "time_limit_multi", workers);
            }
            if (SeedFindScene.INSTANCE != null && frontier != Long.MAX_VALUE)
                SeedFindScene.INSTANCE.updateCurrentSeed(frontier);

            Thread.sleep(POLL_INTERVAL);
        }

        killAll();
        return "";
    }

    /** 把种子归一到环 [0, TOTAL_SEEDS) 内；手写取模，兼容 minSdk 21（Math.floorMod 为 API 24+） */
    private static long ring(long seed) {
        long m = seed % DungeonSeed.TOTAL_SEEDS;
        return m < 0 ? m + DungeonSeed.TOTAL_SEEDS : m;
    }

    /** 完全镜像 SeedFinder.findSeed() 的起步种子算法 */
    private static long deriveFirstSeed() {
        long seedDigits = DungeonSeed.randomSeed();
        if (seedDigits > 200000) seedDigits -= 100000;
        return seedDigits + Random.Int(99999);
    }

    private void launchWorker(Worker w) throws IOException {
        SeedFinderJob job = template.copyForWorker(w.index, w.nextSeed, -1);
        File jobFile = new File(runDir, "worker-" + w.index + ".job");
        job.write(jobFile);
        //清掉上一轮的终态文件与状态格，避免误读
        deleteQuietly(fileOf(w, "hit"));
        deleteQuietly(fileOf(w, "done"));
        deleteQuietly(fileOf(w, "error"));
        state.reset(w.index);
        w.lastSeed = -1;
        w.lastChange = System.currentTimeMillis();
        if (!launcher.launch(w.index, jobFile)) {
            w.failed = true;
            anyFailed = true;
            failureInfo = Messages.get(SeedFinder.class, "launch_failed", w.index);
        }
    }

    /** 子进程异常退出：未超限则从最近报告的种子重启 */
    private void crashWorker(Worker w, String message) throws IOException {
        launcher.kill(w.index);
        if (w.restarts >= MAX_RESTARTS) {
            w.failed = true;
            anyFailed = true;
            failureInfo = Messages.get(SeedFinder.class, "crash_failed", w.index, message);
            return;
        }
        w.restarts++;
        if (w.lastSeed >= 0) w.nextSeed = w.lastSeed;
        launchWorker(w);
    }

    /** 卡死处理：换新进程重试同一颗种子，超过重试上限则跳过 */
    private void stuckWorker(Worker w, long stuckSeed) throws IOException {
        launcher.kill(w.index);
        if (w.stuckRetries < STUCK_RETRIES) {
            w.stuckRetries++;
            w.nextSeed = stuckSeed;
        } else {
            w.stuckRetries = 0;
            appendSkipped(stuckSeed);
            w.nextSeed = ring(stuckSeed + 1);
        }
        launchWorker(w);
    }

    private String reportHit(long seed) {
        // 父进程有 Gdx，可以渲染完整物品清单（不依赖 SeedFindScene 场景，供老查种入口复用）
        SeedFinder.resetTest();
        ArrayList<String> matched = new ArrayList<>();
        SeedFinder finder = new SeedFinder(targets, floor, heroClass);
        String log = finder.logSeedItemsWithMatches(seed, matched);
        if (matched.isEmpty()) return log;
        StringBuilder sb = new StringBuilder(log);
        sb.append("\n").append(Messages.get(SeedFinder.class, "matched_floors")).append("\n");
        for (String m : matched) sb.append(m).append("\n");
        return sb.toString();
    }

    private void killAll() {
        SeedFinderLauncher l = launcher;
        if (l == null || runDir == null) return;
        for (int i = 0; i < workers; i++) l.kill(i);
        activeWorkers = 0;
    }

    private File fileOf(Worker w, String suffix) {
        return new File(runDir, "worker-" + w.index + "." + suffix);
    }

    /** 清掉上一次的运行目录，避免磁盘堆积 */
    private static void cleanOldRuns(File runRoot) {
        File[] old = runRoot.listFiles();
        if (old == null) return;
        for (File f : old)
            if (f.isDirectory() && f.getName().startsWith("run-"))
                deleteRecursively(f);
    }

    private static void deleteRecursively(File f) {
        File[] children = f.listFiles();
        if (children != null)
            for (File c : children) deleteRecursively(c);
        f.delete();
    }

    private static void deleteQuietly(File f) {
        if (f.exists() && !f.delete()) f.deleteOnExit();
    }

    private void appendSkipped(long seed) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File(runDir, "skipped.txt"), true), StandardCharsets.UTF_8);
        try {
            out.write(seed + " " + DungeonSeed.convertToCode(seed) + "\n");
        } finally {
            out.close();
        }
    }

    //【临时·性能测试】汇总各子进程已扫描的种子数：子进程重启后计数会回落到 0，这里按"只增不减"累计（后续移除）
    private long scanTotal(Worker[] ws) {
        long total = 0;
        for (Worker w : ws) {
            long c = state.count(w.index);
            if (c > w.count) w.count = c;
            total += w.count;
        }
        return total;
    }

    private static long readLong(File f) {
        String s = readText(f);
        if (s == null || s.isEmpty()) return -1;
        try {
            return Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String readText(File f) {
        if (!f.exists() || f.length() <= 0) return null;
        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8);
            try {
                char[] buf = new char[4096];
                StringBuilder sb = new StringBuilder();
                int n;
                while ((n = in.read(buf)) > 0) sb.append(buf, 0, n);
                return sb.toString();
            } finally {
                in.close();
            }
        } catch (IOException e) {
            return null;
        }
    }

    /** 单个子进程的协调状态 */
    private static final class Worker {
        final int index;
        /** 下次启动的起始种子 */
        long nextSeed;
        /** 最近一次报告的种子 */
        long lastSeed = -1;
        /** lastSeed 最后一次变化的时间 */
        long lastChange;
        /** 【临时·性能测试】已累计的扫描数（只增不减） */
        long count;
        int restarts;
        int stuckRetries;
        boolean finished;
        boolean failed;

        Worker(int index, long nextSeed) {
            this.index = index;
            this.nextSeed = nextSeed;
        }
    }
}