package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;

import java.io.File;

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
        //种子空间是一个环 [0, TOTAL_SEEDS)：本进程从自己的环段起点开始连续推进，越过环尾回绕到 0
        //Math.floorMod 是 API 24+，这里手写欧几里得取模以兼容 minSdk 21
        long seed = job.startSeed % DungeonSeed.TOTAL_SEEDS;
        if (seed < 0) seed += DungeonSeed.TOTAL_SEEDS;

        while (job.count <= 0 || checked < job.count) {
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