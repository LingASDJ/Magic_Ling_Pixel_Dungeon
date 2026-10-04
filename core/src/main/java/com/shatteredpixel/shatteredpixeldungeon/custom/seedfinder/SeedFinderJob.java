package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 多进程查种的任务描述：父进程写出任务文件，子进程读入后独立扫描一段种子。
 * 全部使用纯 java.io —— 子进程不初始化 Gdx，不能用 FileUtils。
 * 文件格式为逐行 key=value（param.&lt;k&gt; 为平台参数，target 为目标，可出现多次）。
 */
public class SeedFinderJob {

    public int floor;
    public String heroClass;
    public long startSeed;
    /** 本进程要检查的种子个数；<=0 表示沿环一直扫（不设上限） */
    public long count;
    /** 子进程下标，决定状态文件的格位与 hit/done/error 文件名 */
    public int index;
    /** 本次运行的子进程总数（决定共享状态文件的格数） */
    public int workers;
    /** 前进的步长：环形切片下为 1（从段首连续推进，越过环尾回绕到 0） */
    public int stride = 1;
    /** 命中复查次数 */
    public int reps = SeedFinder.CONFIRM_REPS;
    /** 运行目录（绝对路径）：子进程在此写汇报文件 */
    public String outDir;
    public String gameVersion;
    public int gameVersionCode;
    public boolean gameIsDebug;
    /** 平台参数（由 SeedFinderLauncher.platformParams() 提供），原样透传给子进程 */
    public final HashMap<String, String> params = new HashMap<>();
    /** 目标序列化：clsName|minLevel|augName */
    public final ArrayList<String> targetSpecs = new ArrayList<>();

    public static String targetSpec(WantedTarget t) {
        return t.clsName() + "|" + t.minLevel + "|" + t.augName();
    }

    /** 子进程按类名重建查询目标（类名空串 = 通配任意物品，仅按等级/附魔） */
    public ArrayList<WantedTarget> buildTargets() throws ClassNotFoundException {
        ArrayList<WantedTarget> targets = new ArrayList<>();
        for (String spec : targetSpecs) {
            String[] parts = spec.split("\\|", -1);
            Class<? extends Item> cls = parts[0].isEmpty() ? null : Class.forName(parts[0]).asSubclass(Item.class);
            int minLevel = parts.length > 1 && !parts[1].isEmpty() ? Integer.parseInt(parts[1]) : 0;
            Class<?> aug = parts.length > 2 && !parts[2].isEmpty() ? Class.forName(parts[2]) : null;
            targets.add(new WantedTarget(cls, minLevel, aug));
        }
        return targets;
    }

    public SeedFinderJob copyForWorker(int index, long startSeed, long count) {
        SeedFinderJob job = new SeedFinderJob();
        job.floor = floor;
        job.heroClass = heroClass;
        job.startSeed = startSeed;
        job.count = count;
        job.index = index;
        job.workers = workers;
        job.stride = stride;
        job.reps = reps;
        job.outDir = outDir;
        job.gameVersion = gameVersion;
        job.gameVersionCode = gameVersionCode;
        job.gameIsDebug = gameIsDebug;
        job.params.putAll(params);
        job.targetSpecs.addAll(targetSpecs);
        return job;
    }

    public void write(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("floor=").append(floor).append('\n');
        sb.append("heroClass=").append(heroClass).append('\n');
        sb.append("startSeed=").append(startSeed).append('\n');
        sb.append("count=").append(count).append('\n');
        sb.append("index=").append(index).append('\n');
        sb.append("workers=").append(workers).append('\n');
        sb.append("stride=").append(stride).append('\n');
        sb.append("reps=").append(reps).append('\n');
        sb.append("outDir=").append(outDir).append('\n');
        sb.append("gameVersion=").append(gameVersion).append('\n');
        sb.append("gameVersionCode=").append(gameVersionCode).append('\n');
        sb.append("gameIsDebug=").append(gameIsDebug).append('\n');
        for (Map.Entry<String, String> e : params.entrySet())
            sb.append("param.").append(e.getKey()).append('=').append(e.getValue()).append('\n');
        for (String spec : targetSpecs)
            sb.append("target=").append(spec).append('\n');
        writeAtomic(file, sb.toString());
    }

    public static SeedFinderJob read(File file) throws IOException {
        SeedFinderJob job = new SeedFinderJob();
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                int eq = line.indexOf('=');
                if (eq <= 0) continue;
                String key = line.substring(0, eq);
                String value = line.substring(eq + 1);
                if ("floor".equals(key)) job.floor = Integer.parseInt(value);
                else if ("heroClass".equals(key)) job.heroClass = value;
                else if ("startSeed".equals(key)) job.startSeed = Long.parseLong(value);
                else if ("count".equals(key)) job.count = Long.parseLong(value);
                else if ("index".equals(key)) job.index = Integer.parseInt(value);
                else if ("workers".equals(key)) job.workers = Integer.parseInt(value);
                else if ("stride".equals(key)) job.stride = Integer.parseInt(value);
                else if ("reps".equals(key)) job.reps = Integer.parseInt(value);
                else if ("outDir".equals(key)) job.outDir = value;
                else if ("gameVersion".equals(key)) job.gameVersion = value;
                else if ("gameVersionCode".equals(key)) job.gameVersionCode = Integer.parseInt(value);
                else if ("gameIsDebug".equals(key)) job.gameIsDebug = Boolean.parseBoolean(value);
                else if (key.startsWith("param.")) job.params.put(key.substring(6), value);
                else if ("target".equals(key)) job.targetSpecs.add(value);
            }
        } finally {
            in.close();
        }
        if (job.heroClass == null) throw new IOException("job file missing heroClass: " + file);
        return job;
    }

    /** 原子写小文件（先写 .tmp 再替换），避免父进程读到半截内容；Windows 上文件可能被瞬时占用，做有限次重试 */
    public static void writeAtomic(File target, String content) throws IOException {
        File tmp = new File(target.getPath() + ".tmp");
        FileOutputStream out = new FileOutputStream(tmp);
        try {
            out.write(content.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } finally {
            out.close();
        }
        //Windows 下 rename 不能覆盖已存在的文件；杀毒/索引/父进程读取都可能瞬时占用，删了再改名，失败则短暂退避重试
        for (int attempt = 0; attempt < 20; attempt++) {
            if (target.exists() && !target.delete()) {
                sleepQuietly();
                continue;
            }
            if (tmp.renameTo(target)) return;
            sleepQuietly();
        }
        throw new IOException("cannot write " + target);
    }

    private static void sleepQuietly() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}