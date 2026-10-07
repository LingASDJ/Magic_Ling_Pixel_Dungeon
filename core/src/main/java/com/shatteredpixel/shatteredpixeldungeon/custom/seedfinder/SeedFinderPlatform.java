package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import java.io.File;
import java.io.IOException;

/**
 * 多进程查种的平台桥。桌面端在启动时注入 {@link #instance}（DesktopSeedFinderPlatform，
 * 基于内存映射共享状态文件）；web 端不注入，UI 自动回退到单进程线程查找。
 * 把 mmap 实现隔离在桌面侧，避免 web 编译期引入 Java NIO 映射类型。
 */
public abstract class SeedFinderPlatform {

    /** 桌面端注入的实例；null 表示当前平台不支持多进程查种 */
    public static volatile SeedFinderPlatform instance;

    /** 运行目录内的状态文件名（与 SeedFinderState.FILE_NAME 保持一致） */
    public static final String STATE_FILE_NAME = "state.bin";

    /** 父进程：在运行目录下新建共享状态文件并返回整表视图 */
    public ProgressState openCoordinatorState(File file, int workers) throws IOException {
        throw new IOException("multi-process seed finder not supported on this platform");
    }
}
