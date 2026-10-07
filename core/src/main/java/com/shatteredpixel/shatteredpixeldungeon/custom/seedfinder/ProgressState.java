package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

/**
 * 多进程查种的共享进度视图：父进程按子进程号读取当前种子与已扫描数，
 * 并在重启子进程前清空其格位。实现类决定底层存储方式
 * （桌面端 = 内存映射共享文件，web 端不提供实现）。
 */
public interface ProgressState {

    /** 子进程 i 正在处理的种子；未上报为 -1 */
    long seed(int index);

    /** 子进程 i 已扫描的种子数 */
    long count(int index);

    /** 启动/重启子进程 i 之前清掉它那一格，避免读到上一轮的残留 */
    void reset(int index);
}
