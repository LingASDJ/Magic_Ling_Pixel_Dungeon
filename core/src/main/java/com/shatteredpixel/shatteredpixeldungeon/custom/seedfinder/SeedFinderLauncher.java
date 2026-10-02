package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import java.io.File;
import java.util.Map;

/**
 * 多进程查种的平台抽象：core 只依赖本接口，由各平台提供实现（拉起/杀死/存活检测子进程）。
 * 平台不支持多进程时 SeedFinderCoordinator.launcher 保持为 null，UI 自动回退到单进程查找。
 */
public interface SeedFinderLauncher {

    /** 平台允许的最大子进程数（1 表示不支持多进程） */
    int maxWorkers();

    /** 平台相关的透传参数（如 desktop 的 basePath/runRoot），会写入任务文件原样交给子进程 */
    Map<String, String> platformParams();

    /** 启动第 index 个子进程执行 jobFile；返回 false 表示启动失败 */
    boolean launch(int index, File jobFile);

    /** 结束第 index 个子进程（不存在或已退出时静默忽略） */
    void kill(int index);

    /** 第 index 个子进程是否仍在运行 */
    boolean isAlive(int index);
}