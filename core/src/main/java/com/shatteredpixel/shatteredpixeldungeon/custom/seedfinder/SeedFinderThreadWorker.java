package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.DefaultAndroidFiles;
import com.badlogic.gdx.backends.android.AndroidPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.watabou.noosa.Game;

import java.io.File;

/**
 * 安卓原生多线程查种的工作线程入口。
 *
 * <p>本类由 {@link SeedFinderThreadLauncher} 放进每个 worker 自己的子加载器里加载并反射调用，
 * 因此这里触发的 Dungeon / Random / 任务 Quest / Generator …… 全部是
 * 该 worker 私有的类副本 —— 全局静态状态按线程隔离，这是多线程真正并发的前提。</p>
 *
 * <p>安卓环境由主进程直接传入（不反射）：</p>
 * <ul>
 *   <li>{@link AssetManager} —— 包成该加载器自己的 {@link DefaultAndroidFiles}，assets 可读；</li>
 *   <li>{@link SharedPreferences} —— 包成 {@link AndroidPreferences}，读到与主进程一致的设置。</li>
 * </ul>
 *
 * <p>流程与多进程版子进程完全一致：读任务文件 → 加载 libgdx natives 副本 →
 * 搭无头 Gdx 环境（{@link SeedFinderHeadless}）→ 交给 {@link SeedFinderWorker}
 * 跑种子扫描循环（mmap 进度 + hit/done 文件汇报）。</p>
 */
public final class SeedFinderThreadWorker {

    private SeedFinderThreadWorker() {}

    /** 反射入口：args[0]=任务文件，args[1]=AssetManager，args[2]=SharedPreferences，args[3]=应用 Context，args[4]=libgdx.so 副本，args[5]=独立工作目录，args[6]=主进程 Game.version */
    public static void main(Object[] args) throws Exception {
        if (args == null || args.length < 7) {
            throw new IllegalArgumentException("bad worker args: need job file, assets, prefs, context, natives path, work dir, game version");
        }
        SeedFinderJob job = SeedFinderJob.read(new File((String) args[0]));
        AssetManager assets = (AssetManager) args[1];
        SharedPreferences sp = (SharedPreferences) args[2];
        ContextWrapper context = (ContextWrapper) args[3];
        String nativesPath = (String) args[4];
        String workDir = (String) args[5];
        String gameVersion = (String) args[6];

        Files files = (assets != null) ? new WorkerFiles(assets, context, new File(workDir)) : new FallbackFiles();
        Preferences prefs = (sp != null) ? new AndroidPreferences(sp) : null;

        // 必须先加载 libgdx native 库（Gdx2DPixmap 解码 PNG 是硬依赖）。
        // 安卓按 ClassLoader 隔离 native 库：主进程已打开的那份 .so 在子加载器里打不开，
        // 所以这里加载的是主进程预复制到私有目录的独立副本（不同路径=ART 视为不同库）。
        loadNatives(nativesPath);

        // 再装好无头环境，才触发任何游戏类静态初始化（install 幂等，可安全重入）。
        // external 目录必须传 worker 私有目录：install 依赖它设置 FileUtils 默认文件类型
        //（externalPath==null 时不设置，defaultFileType 保持 null，
        //   Badges.loadGlobal → FileUtils.getFileHandle 的 switch(type) 会直接 NPE）。
        SeedFinderHeadless.install(files, new File(workDir, "external").getAbsolutePath(), prefs);

        // 同步主进程的版本号：worker 副本默认 null，而 Document.<clinit> → DeviceCompat.isDebug
        // 会 Game.version.contains(...) 直接 NPE。
        if (gameVersion != null) {
            Game.version = gameVersion;
        }

        // 预初始化核心物品生成器：把类初始化失败提前暴露成带完整 cause 链的异常
        //（否则会在 testSeed 深处炸成难读的 ExceptionInInitializerError + Rejecting re-init）
        try {
            Class.forName(Generator.class.getName(), true, Generator.class.getClassLoader());
        } catch (Throwable t) {
            throw new RuntimeException("游戏核心类预初始化失败（Generator），完整原因见 cause 链：", t);
        }

        String cb = job.params.get("checkBranches");
        if (cb != null) {
            SeedFinder.Options.checkBranches = Boolean.parseBoolean(cb);
        }

        SeedFinderWorker.run(job);
    }

    /** 加载 libgdx natives 副本；失败直接抛出（错误可见，不再静默吞掉） */
    private static void loadNatives(String nativesPath) {
        if (nativesPath == null) {
            throw new RuntimeException("缺少 libgdx native 库副本路径，无法解码纹理（Gdx2DPixmap）");
        }
        try {
            System.load(nativesPath);
        } catch (Throwable t) {
            throw new RuntimeException("加载 libgdx native 库副本失败: " + t, t);
        }
    }

    /** 每 worker 独立的 Files 实现：internal 走 APK assets（只读），
     * local/external 指向本 worker 的私有目录——既隔离存档（Badges/Dungeon 不会
     * 读到或写坏主进程的真实存档），也让 Badges.loadGlobal 走"无存档"分支。 */
    private static final class WorkerFiles implements Files {

        private final DefaultAndroidFiles assetsFiles;
        private final File localRoot;
        private final File externalRoot;

        WorkerFiles(AssetManager assets, ContextWrapper context, File workDir) {
            this.assetsFiles = new DefaultAndroidFiles(assets, context, false);
            this.localRoot = new File(workDir, "local");
            this.externalRoot = new File(workDir, "external");
        }

        @Override
        public FileHandle getFileHandle(String path, FileType type) {
            switch (type) {
                case Internal:
                    return assetsFiles.internal(path);
                case Local:
                    return new FileHandle(new File(localRoot, path));
                case External:
                    return new FileHandle(new File(externalRoot, path));
                case Classpath:
                    return new ClasspathHandle(path);
                case Absolute:
                default:
                    return new FileHandle(new File(path));
            }
        }

        @Override
        public FileHandle local(String path) {
            return getFileHandle(path, FileType.Local);
        }

        @Override
        public FileHandle internal(String path) {
            return getFileHandle(path, FileType.Internal);
        }

        @Override
        public FileHandle external(String path) {
            return getFileHandle(path, FileType.External);
        }

        @Override
        public FileHandle absolute(String path) {
            return getFileHandle(path, FileType.Absolute);
        }

        @Override
        public FileHandle classpath(String path) {
            return getFileHandle(path, FileType.Classpath);
        }

        @Override
        public String getExternalStoragePath() {
            return externalRoot.getAbsolutePath();
        }

        @Override
        public boolean isExternalStorageAvailable() {
            return true;
        }

        @Override
        public String getLocalStoragePath() {
            return localRoot.getAbsolutePath();
        }

        @Override
        public boolean isLocalStorageAvailable() {
            return true;
        }
    }

    /** 非安卓兜底 Files：internal 优先 classpath 资源，其余按绝对路径（仅开发/调试用） */
    private static final class FallbackFiles implements Files {

        @Override
        public FileHandle getFileHandle(String path, FileType type) {
            if (type == FileType.Classpath) {
                return new ClasspathHandle(path);
            }
            if (type == FileType.Internal) {
                java.net.URL url = getClass().getClassLoader().getResource(path);
                if (url != null) {
                    try {
                        return new FileHandle(new File(url.toURI()));
                    } catch (Exception ignored) {
                    }
                }
                return new FileHandle(new File(path));
            }
            return new FileHandle(new File(path));
        }

        @Override
        public FileHandle local(String path) {
            return getFileHandle(path, FileType.Local);
        }

        @Override
        public FileHandle internal(String path) {
            return getFileHandle(path, FileType.Internal);
        }

        @Override
        public FileHandle external(String path) {
            return getFileHandle(path, FileType.External);
        }

        @Override
        public FileHandle absolute(String path) {
            return getFileHandle(path, FileType.Absolute);
        }

        @Override
        public FileHandle classpath(String path) {
            return getFileHandle(path, FileType.Classpath);
        }

        @Override
        public String getExternalStoragePath() {
            return "";
        }

        @Override
        public boolean isExternalStorageAvailable() {
            return false;
        }

        @Override
        public String getLocalStoragePath() {
            return "";
        }

        @Override
        public boolean isLocalStorageAvailable() {
            return false;
        }
    }

    /** gdx 1.14 起 FileHandle 的 FileType 构造器为 protected，用子类暴露 Classpath 句柄 */
    private static final class ClasspathHandle extends FileHandle {
        ClasspathHandle(String path) {
            super(path, Files.FileType.Classpath);
        }
    }
}
