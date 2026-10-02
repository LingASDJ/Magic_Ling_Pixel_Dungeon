package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 多进程查种的共享状态文件：运行目录下一个固定大小的小文件，每个子进程一格，
 * 子进程往自己那格里写当前种子与已扫描数，父进程读同一份内存映射，双方都不做文件系统操作。
 * 逐种子写文件（新建 → 写入 → 删除 → 改名）在本机实测约 8-13ms/次，而映射写入只是一次内存拷贝，
 * 这是多进程查种与多线程查种差距的主要来源。
 */
public final class SeedFinderState {

    /** 运行目录内的状态文件名 */
    public static final String FILE_NAME = "state.bin";
    /** 每个子进程一格的字节数（留白以便日后加字段） */
    public static final int SLOT_BYTES = 64;
    /** 格内偏移：当前种子 / 已扫描数（含当前这颗） */
    private static final int OFF_SEED = 0;
    private static final int OFF_COUNT = 8;

    private final MappedByteBuffer map;
    /** 本实例 publish 写入哪一格（子进程 = 自己的进程号，父进程不使用） */
    private final int slot;

    private SeedFinderState(MappedByteBuffer map, int slot) {
        this.map = map;
        this.slot = slot;
    }

    /** 父进程：新建状态文件并映射整表，全部格子先置为“未上报” */
    public static SeedFinderState forCoordinator(File file, int workers) throws IOException {
        SeedFinderState state = map(file, workers, true, true, 0);
        for (int i = 0; i < workers; i++) state.reset(i);
        return state;
    }

    /** 子进程：映射父进程建好的整表，只写自己那一格 */
    public static SeedFinderState forWorker(File file, int workers, int index) throws IOException {
        return map(file, workers, true, false, index);
    }

    /** 只读映射（测试与排查用） */
    public static SeedFinderState forReader(File file, int workers) throws IOException {
        return map(file, workers, false, false, 0);
    }

    /**
     * @param writable 是否需要写入
     * @param create   true = 新建并截断为整表大小；false = 要求文件已存在且长度足够
     * @param slot     本实例 publish 写入的格位
     */
    private static SeedFinderState map(File file, int workers, boolean writable, boolean create, int slot) throws IOException {
        long bytes = (long) workers * SLOT_BYTES;
        if (!create && (!file.isFile() || file.length() < bytes))
            throw new IOException("状态文件不完整：" + file);
        RandomAccessFile raf = new RandomAccessFile(file, writable ? "rw" : "r");
        try {
            if (create) raf.setLength(bytes);
            //映射建立后独立于创建它的通道，通道关闭不影响读写
            MappedByteBuffer map = raf.getChannel().map(
                    writable ? FileChannel.MapMode.READ_WRITE : FileChannel.MapMode.READ_ONLY, 0, bytes);
            return new SeedFinderState(map, slot);
        } finally {
            raf.close();
        }
    }

    /** 子进程：汇报当前种子与已扫描数；写的是共享内存，父进程下一次读取即可见 */
    public void publish(long seed, long count) {
        int base = slot * SLOT_BYTES;
        map.putLong(base + OFF_SEED, seed);
        map.putLong(base + OFF_COUNT, count);
    }

    /** 子进程 i 正在处理的种子；未上报为 -1 */
    public long seed(int i) {
        return map.getLong(i * SLOT_BYTES + OFF_SEED);
    }

    /** 子进程 i 已扫描的种子数 */
    public long count(int i) {
        return map.getLong(i * SLOT_BYTES + OFF_COUNT);
    }

    /** 启动/重启子进程 i 之前清掉它那一格，避免读到上一轮的残留 */
    public void reset(int i) {
        int base = i * SLOT_BYTES;
        map.putLong(base + OFF_SEED, -1L);
        map.putLong(base + OFF_COUNT, 0L);
    }
}