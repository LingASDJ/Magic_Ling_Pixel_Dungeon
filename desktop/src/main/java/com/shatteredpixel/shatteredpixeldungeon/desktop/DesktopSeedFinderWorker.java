package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.SeedFinderHeadless;
import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.SeedFinderJob;
import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.SeedFinderWorker;
import com.watabou.noosa.Game;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 查种子子进程入口：只做最小 bootstrap（Game 静态字段 + 无头环境 + 偏好设置），
 * 不创建窗口，扫描结束即退出。
 */
public class DesktopSeedFinderWorker {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("用法: DesktopSeedFinderWorker <jobFile>");
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

            //无头环境：偏好设置/外部目录与父进程同源，并补齐世界生成依赖的字体的等
            String basePath = job.params.get("basePath");
            Preferences prefs = basePath != null
                    ? new Lwjgl3Preferences(SPDSettings.DEFAULT_PREFS_FILE, basePath) : null;
            SeedFinderHeadless.install(new Lwjgl3Files(), basePath, prefs);

            SeedFinderWorker.run(job);
        } catch (Throwable t) {
            writeError(job, jobFile, t);
            System.exit(2);
        }
        System.exit(0);
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