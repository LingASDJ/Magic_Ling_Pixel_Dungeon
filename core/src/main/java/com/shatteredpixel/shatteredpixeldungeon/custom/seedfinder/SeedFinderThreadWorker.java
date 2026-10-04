package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.DefaultAndroidFiles;
import com.badlogic.gdx.backends.android.AndroidPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.custom.CollectRankings;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Gregorian;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

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

        // 先把主进程全局存档复刻到本 worker 的 external 根目录（纯文件 IO，不依赖 Gdx/FileUtils）。
        // 必须在 install 之前完成：install 里 new Game(PixelScene.class, null) 会触发
        // Badges/Generator 等类的静态初始化，而 Generator.<clinit> 直接 Badges.loadGlobal()
        // 读磁盘决定武器/法杖的徽章解锁概率——存档若在 install 之后才复制，概率表会按
        // “空存档”定型且静态块只执行一次，生成结果与主进程不一致（地面掉落缺失/解锁武器为 0）。
        copyMainSave(context, new File(workDir, "external"));

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

        // 复刻主进程全局存档到本 worker 的 external 根目录：
        // worker 的 FileUtils 默认 External 指向私有目录，Badges.loadGlobal / Rankings.load /
        // Bones / Journal / keybindings 等全部读到"无存档"，而 Generator 静态块里
        // Badges.isUnlocked(...) 门控的生成概率会恒为 0，bones 掉落也受影响——
        // 依赖进度/徽章解锁的物品永远查不出来，查种结果与真实进度不适配。
        // 必须在 Generator 预初始化之前复制（Badges.loadGlobal 由 Generator.<clinit> 触发）。
        copyMainSave(context, new File(workDir, "external"));
        // install 里 new Game(PixelScene.class, null) 可能提前触发 Badges/Rankings/Bones 等
        // 类的静态初始化（缓存空存档），因此复制完磁盘文件后必须强制作废并重载这些全局缓存，
        // 与 BackupSaveScene.resetGlobalCache 的行为对齐；若 prefs 非本 worker 私有副本，
        // 这里 Rankings.load 的 lastDaily 回写会污染主进程设置（launcher 已保证私有）。
        reloadGlobalSaves();

        // 预初始化核心物品生成器：把类初始化失败提前暴露成带完整 cause 链的异常
        //（否则会在 testSeed 深处炸成难读的 ExceptionInInitializerError + Rejecting re-init）
        try {
            Class.forName(Generator.class.getName(), true, Generator.class.getClassLoader());
        } catch (Throwable t) {
            throw new RuntimeException("游戏核心类预初始化失败（Generator），完整原因见 cause 链：", t);
        }

        // 复刻主进程日期相关状态（worker 不跑 TitleScene，这些状态全留在默认值，生成会与主进程不一致）：
        // 1) Dungeon.whiteDaymode：主进程按当前小时设置（7-22 点为白天），影响 Yog 夜战等生成分支；
        // 2) 节日状态：主进程在 TitleScene 里按本地日期（含农历）调用 Gregorian.LunarCheckDate()
        //    初始化 RegularLevel.holiday/chinaHoliday/birthday——中国节日（国庆/中秋/春节等）
        //    生成在 worker 里原本全部失效（0 楼掉落差异的直接原因之一）。
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        Dungeon.whiteDaymode = currentHour > 7 && currentHour < 22;
        try {
            Gregorian.LunarCheckDate();
            System.out.println("[SeedFinder] holiday applied: holiday=" + RegularLevel.holiday
                    + " chinaHoliday=" + RegularLevel.chinaHoliday
                    + " active=" + Gregorian.getActiveHolidayList().size());
        } catch (Throwable t) {
            // 不静默吞：打印完整异常，方便定位节日复刻失败的真实原因
            System.out.println("[SeedFinder] LunarCheckDate FAILED: " + t);
            t.printStackTrace(System.out);
        }
        logWorkerEnv();

        String cb = job.params.get("checkBranches");
        if (cb != null) {
            SeedFinder.Options.checkBranches = Boolean.parseBoolean(cb);
        }

        SeedFinderWorker.run(job);
    }

    /** 复刻主进程全局存档（根目录下全部 .dat 文件）到本 worker 的 external 根目录。
     *  worker 的 FileUtils 默认 External 指向私有目录，不复制的话 Badges/Rankings/Bones/
     *  Journal/keybindings 全部读到"无存档"——徽章门控的生成概率恒为 0、bones 掉落也缺失，
     *  查种结果与真实进度不适配。
     *  主进程 defaultFileType=Local（AndroidLauncher 设置），全局存档根目录 = getFilesDir()。
     *  与 BackupSaveScene.exportWholeSlotToMLSP 的 whole-save 备份范围一致（根目录 *.dat）。 */
    private static void copyMainSave(ContextWrapper context, File workerExternal) {
        File mainRoot = context.getFilesDir();
        if (mainRoot == null) return;
        File[] datFiles = mainRoot.listFiles((dir, name) -> name.endsWith(".dat"));
        int copied = 0;
        if (datFiles != null) {
            try {
                if (!workerExternal.exists()) {
                    workerExternal.mkdirs();
                }
            } catch (Throwable ignored) {
            }
            for (File src : datFiles) {
                try {
                    File dst = new File(workerExternal, src.getName());
                    FileInputStream in = new FileInputStream(src);
                    try {
                        FileOutputStream out = new FileOutputStream(dst);
                        try {
                            byte[] buf = new byte[8192];
                            int n;
                            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
                        } finally {
                            out.close();
                        }
                    } finally {
                        in.close();
                    }
                    copied++;
                } catch (Throwable ignored) {
                    // 单个文件复制失败：跳过该文件，不阻塞查种
                }
            }
        }
        // 诊断日志：确认主进程根目录到底有几个全局存档、复制成功几个
        //（若 dat=0 说明主进程本身就是新装空存档，worker 读不到进度是正常的）
        // 注意：copyMainSave 在 install 之前调用，Gdx.app 尚未安装，必须走 System.out
        //（worker 的日志本来也以 System.out + [SeedFinder] 前缀进 logcat，与 Gdx stub 格式一致）
        System.out.println("[SeedFinder] copyMainSave: root=" + mainRoot
                + " dat=" + (datFiles == null ? -1 : datFiles.length)
                + " copied=" + copied + " -> " + workerExternal);
        // 诊断：列出复制后的文件大小，确认 journal.dat 等存档文件确实非空（而非复制到错误路径）
        File[] dstFiles = workerExternal.listFiles();
        if (dstFiles != null) {
            StringBuilder sb = new StringBuilder("[SeedFinder] workFiles:");
            for (File f : dstFiles) sb.append(" ").append(f.getName()).append("=").append(f.length());
            System.out.println(sb);
        }
    }

    /** 强制作废并重载全部全局存档缓存（与 BackupSaveScene.resetGlobalCache 对齐）。
     *  install 的 new Game 可能在复制前触发这些类的静态初始化（缓存空存档），
     *  文件复制完成后必须重载一次才能让 Generator / Bones 读到真实进度。 */
    private static void reloadGlobalSaves() {
        try {
            Badges.global = null;
            Badges.loadGlobal();
            PaswordBadges.global = null;
            PaswordBadges.loadGlobal();
            Rankings.INSTANCE.records = null;
            Rankings.INSTANCE.load();
            CollectRankings.INSTANCE.records = null;
            CollectRankings.INSTANCE.load();
            Journal.resetForReload();
            Journal.loadGlobal();
            Bones.resetForReload();
        } catch (Throwable ignored) {
            // 任一重载失败不阻塞查种（Generator 预初始化仍会暴露真正的类初始化问题）
        }
        // 诊断：journal.dat 的 Document 页面状态是否恢复（决定 GuidePage/指南书页是否生成）。
        // 主进程玩过指南 → 页面 FOUND/READ → 不生成书页；worker 若一直 NOT_FOUND → 会生成书页，
        // 与主进程生成不一致。debug=true（INDEV）时 <clinit> 默认全 READ，不会走这条差异。
        try {
            System.out.println("[SeedFinder] document: debug=" + DeviceCompat.isDebug()
                    + " introFound=" + Document.ADVENTURERS_GUIDE.isPageFound(Document.GUIDE_INTRO)
                    + " introRead=" + Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_INTRO)
                    + " totalPages=" + Document.ADVENTURERS_GUIDE.pageNames().size());
        } catch (Throwable ignored) {
        }
    }

    /** 环境诊断：打印 worker 的挑战/难度/娱乐模式/节日/关键徽章解锁与 Generator 概率表，
     *  与主进程查种结果对照，用于定位“同种子生成不一致”。
     *  注意：worker 无头环境下 Gdx.app.log 可能不可靠，统一走 System.out + [SeedFinder] 前缀。 */
    private static void logWorkerEnv() {
        try {
            int challenges = SPDSettings.challenges();
            String difficulty = String.valueOf(SPDSettings.difficulty());
            String dlc = String.valueOf(SPDSettings.dlc());
            boolean killMg = Badges.isUnlocked(Badges.Badge.KILL_MG);
            boolean riceSword = PaswordBadges.filtered(true).contains(PaswordBadges.Badge.UNLOCK_RICESWORD)
                    || SPDSettings.isItemUnlock("RiceSword");
            float[] t5 = Generator.Category.WEP_T5.probs;
            System.out.println("[SeedFinder] workerEnv: challenges=" + challenges + " difficulty=" + difficulty
                    + " dlc=" + dlc + " holiday=" + RegularLevel.holiday
                    + " chinaHoliday=" + RegularLevel.chinaHoliday
                    + " activeHolidays=" + Gregorian.getActiveHolidayList().size()
                    + " KILL_MG=" + killMg + " riceSword=" + riceSword
                    + " WEP_T5=" + java.util.Arrays.toString(t5));
        } catch (Throwable t) {
            // 诊断失败不阻塞查种，但打印异常方便定位
            System.out.println("[SeedFinder] workerEnv FAILED: " + t);
        }
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
                case Local: {
                    // FileUtils 以 Local/External 为默认类型时传的是“defaultPath + 文件名”的完整路径
                    //（如 workDir/external/badges.dat），此时必须直接用该路径，不能再拼一次根目录；
                    // 直接调 files.local("相对名") 的路径才是相对的，才需要拼本 worker 的根目录。
                    File f = new File(path);
                    if (!f.isAbsolute()) f = new File(localRoot, path);
                    return new FileHandle(f);
                }
                case External: {
                    File f = new File(path);
                    if (!f.isAbsolute()) f = new File(externalRoot, path);
                    return new FileHandle(f);
                }
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
