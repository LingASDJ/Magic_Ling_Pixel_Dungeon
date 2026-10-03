package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;

import java.io.File;
import java.util.ArrayList;

/**
 * 子进程侧的无头扫描主体：读入任务后沿种子环逐个种子做生成 + 复查（与单进程 findSeed 完全同源）。
 * 进度（当前种子与已扫描数）经共享状态文件实时汇报；命中、扫完这类一次性事件仍用文件告知父进程，
 * 异常由各平台的 worker main 捕获后写入 error 文件。
 */
public final class SeedFinderWorker {

    private SeedFinderWorker() {}

    public static void run(SeedFinderJob job) throws Exception {
        GamesInProgress.selectedClass = HeroClass.valueOf(job.heroClass);
        SeedFinder.resetTest();

        SeedFinder finder = new SeedFinder(job.buildTargets(), job.floor, GamesInProgress.selectedClass);

        File dir = new File(job.outDir);
        SeedFinderState state = SeedFinderState.forWorker(new File(dir, SeedFinderState.FILE_NAME), job.workers, job.index);
        File hitFile = new File(dir, "worker-" + job.index + ".hit");
        File doneFile = new File(dir, "worker-" + job.index + ".done");

        int stride = job.stride > 0 ? job.stride : 1;
        int reps = job.reps > 0 ? job.reps : SeedFinder.CONFIRM_REPS;
        long checked = 0;
        
        long seed = Math.floorMod(job.startSeed, DungeonSeed.TOTAL_SEEDS);

        while (job.count <= 0 || checked < job.count) {
            // 线程版查种：被 kill/stop 打断后尽快退出（多进程版由进程销毁承担）
            if (Thread.currentThread().isInterrupted()) return;
            //先发布进度再处理：父进程据此判定该种子是否卡死
            state.publish(seed, checked + 1);

            boolean confirmed = true;
            for (int r = 0; r < reps; r++) {
                if (!finder.testSeed(seed)) {
                    confirmed = false;
                    break;
                }
            }
            if (confirmed) {
                // 命中：在 worker 环境生成完整物品日志 + 匹配楼层区块，写 worker-N.log 供父进程显示。
                // 不能在父进程重新生成：安卓主进程的全局 RNG 正被渲染线程（UI 粒子等）消耗，
                // 父进程 reportHit 重新生成的楼层与 worker 不一致，导致“匹配物品层数”收集为空。
                ArrayList<String> matched = new ArrayList<>();
                String log = finder.logSeedItemsWithMatches(seed, matched);
                StringBuilder sb = new StringBuilder(log);
                if (!matched.isEmpty()) {
                    sb.append("\n").append(Messages.get(SeedFinder.class, "matched_floors")).append("\n");
                    for (String m : matched) sb.append(m).append("\n");
                }
                SeedFinderJob.writeAtomic(new File(dir, "worker-" + job.index + ".log"), sb.toString());
                SeedFinderJob.writeAtomic(hitFile, Long.toString(seed));
                return;
            }

            seed += stride;
            if (seed >= DungeonSeed.TOTAL_SEEDS) seed -= DungeonSeed.TOTAL_SEEDS;//环形回绕
            checked++;
        }
        SeedFinderJob.writeAtomic(doneFile, Long.toString(seed));
    }
}