package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Preferences;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.noosa.Game;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 查种子子进程入口（Android）：由父进程用 app_process 拉起。
 * 只做最小 bootstrap（native 库 → AndroidFiles/Preferences → 无头环境），
 * 不创建窗口，扫描结束即退出。
 *
 * Android 特有处理：
 *   - 显式 System.load(nativeLibDir/*.so)：app_process 子进程默认搜不到应用的 native 库
 *   - AssetManager.addAssetPath(apk)：让 AndroidFiles 能从 APK 读取 assets
 *   - AndroidSharedPreferences：直接读 settings.xml，绕过 SharedPreferences 对象
 */
public class AndroidSeedFinderWorker {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("用法: AndroidSeedFinderWorker <jobFile>");
            System.exit(2);
            return;
        }
        File jobFile = new File(args[0]);
        SeedFinderJob job = null;
        try {
            job = SeedFinderJob.read(jobFile);

            //与父进程保持一致：DeviceCompat.isDebug() = Game.version.contains("INDEV")
            Game.version = job.gameVersion;
            Game.versionCode = job.gameVersionCode;

            String apkPath = job.params.get("apkPath");
            String filesDir = job.params.get("filesDir");
            String nativeLibDir = job.params.get("nativeLibDir");

            //1. 先加载 native 库（libgdx.so 等），再调 GdxNativesLoader.load()
            if (nativeLibDir != null) loadNativeLibs(nativeLibDir);

            //2. 构造 AndroidFiles（AssetManager + APK 路径）
            Files files = createAndroidFiles(apkPath, filesDir);

            //3. 构造 Preferences（读父进程的 settings.xml）
            Preferences prefs = new AndroidSharedPreferences(filesDir);

            //4. 安装无头环境
            SeedFinderHeadless.install(files, filesDir, prefs);

            //5. 兜底：若 XML 未读到 checkBranches，用任务透传的值覆盖
            String cb = job.params.get("checkBranches");
            if (cb != null) SeedFinder.Options.checkBranches = Boolean.parseBoolean(cb);

            SeedFinderWorker.run(job);
        } catch (Throwable t) {
            writeError(job, jobFile, t);
            System.exit(2);
        }
        System.exit(0);
    }

    /** 显式加载 nativeLibDir 下的所有 .so（System.load 用绝对路径，绕过 linker 限制） */
    private static void loadNativeLibs(String nativeLibDir) {
        File dir = new File(nativeLibDir);
        File[] soFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File d, String name) {
                return name.endsWith(".so");
            }
        });
        if (soFiles == null) return;
        //libgdx.so 优先加载（其它库可能依赖它）
        for (File so : soFiles) {
            if (so.getName().contains("gdx")) {
                try { System.load(so.getAbsolutePath()); } catch (Throwable ignored) {}
            }
        }
        for (File so : soFiles) {
            if (so.getName().contains("gdx")) continue;
            try { System.load(so.getAbsolutePath()); } catch (Throwable ignored) {}
        }
    }

    /** 反射构造 AndroidFiles：new AssetManager() → addAssetPath(apk) → new AndroidFiles(am, filesDir) */
    private static Files createAndroidFiles(String apkPath, String filesDir) throws Exception {
        Class<?> assetManagerClass = Class.forName("android.content.res.AssetManager");
        Constructor<?> amCtor = assetManagerClass.getDeclaredConstructor();
        amCtor.setAccessible(true);
        Object assetManager = amCtor.newInstance();
        Method addAssetPath = assetManagerClass.getMethod("addAssetPath", String.class);
        addAssetPath.invoke(assetManager, apkPath);

        Class<?> androidFilesClass = Class.forName("com.badlogic.gdx.backends.android.AndroidFiles");
        Constructor<?> ctor = androidFilesClass.getDeclaredConstructor(assetManagerClass, String.class);
        ctor.setAccessible(true);
        return (Files) ctor.newInstance(assetManager, filesDir);
    }

    private static void writeError(SeedFinderJob job, File jobFile, Throwable t) {
        try {
            int index = job != null ? job.index : indexFromJobFileName(jobFile);
            File dir = job != null && job.outDir != null ? new File(job.outDir) : jobFile.getParentFile();
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            SeedFinderJob.writeAtomic(new File(dir, "worker-" + index + ".error"), sw.toString());
        } catch (Throwable ignored) {
            //错误文件都写不出来时只能靠 worker-<i>.log 排查
        }
    }

    private static int indexFromJobFileName(File jobFile) {
        String name = jobFile.getName();
        int start = name.indexOf('-') + 1;
        int end = name.indexOf('.');
        if (start <= 0 || end <= start) return -1;
        try {
            return Integer.parseInt(name.substring(start, end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
