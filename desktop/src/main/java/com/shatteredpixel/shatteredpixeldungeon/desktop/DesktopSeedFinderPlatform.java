package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.ProgressState;
import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.SeedFinderPlatform;
import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.SeedFinderState;

import java.io.File;
import java.io.IOException;

/**
 * 桌面端多进程查种平台：把原 SeedFinderState 的内存映射实现挂在平台桥下，
 * 供 SeedFinderCoordinator 通过 {@link SeedFinderPlatform#instance} 使用。
 * 该 mmap 逻辑因此只参与桌面构建，web 端可达图不再包含 NIO 映射类型。
 */
public class DesktopSeedFinderPlatform extends SeedFinderPlatform {

    @Override
    public ProgressState openCoordinatorState(File file, int workers) throws IOException {
        return SeedFinderState.forCoordinator(file, workers);
    }
}
