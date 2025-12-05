package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.CrashHandler;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.news.NewsImpl;
import com.shatteredpixel.shatteredpixeldungeon.update.UpdateImpl;
import com.shatteredpixel.shatteredpixeldungeon.update.Updates;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Point;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class DesktopLauncher {


	public static void main(String[] args) {

		if (!DesktopLaunchValidator.verifyValidJVMState(args)) {
			return;
		}

		// detection for FreeBSD (which is equivalent to linux for us)
		// TODO might want to merge request this to libGDX
		if (System.getProperty("os.name").contains("FreeBSD")) {
			SharedLibraryLoader.isLinux = true;
			// this overrides incorrect values set in SharedLibraryLoader's static initializer
			SharedLibraryLoader.isIos = false;
			SharedLibraryLoader.is64Bit = System.getProperty("os.arch").contains("64") || System.getProperty("os.arch").startsWith("armv8");
		}

		final String title;
		if (DesktopLauncher.class.getPackage().getSpecificationTitle() == null) {
			title = System.getProperty("Specification-Title");
		} else {
			title = DesktopLauncher.class.getPackage().getSpecificationTitle();
		}

		CrashHandler.getInstance().init();

		// 然后设置一个组合的异常处理器
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				System.out.println("Uncaught exception: " + throwable.getClass().getName() + " - " + throwable.getMessage());

				// 记录到文件
				try {
					String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					FileHandle crashFile = Gdx.files.local("crash_" + timestamp + ".log");
					String errorInfo = String.format(
							"JVM Error\n" +
									"Time: %s\n" +
									"Error: %s\n" +
									"Message: %s\n" +
									"StackTrace: %s\n",
							timestamp,
							throwable.getClass().getName(),
							throwable.getMessage(),
							formatStackTrace(throwable.getStackTrace())
					);
					crashFile.writeString(errorInfo, false);
					System.out.println("Crash log written to: " + crashFile.path());
				} catch (Exception e) {
					System.err.println("Failed to log JVM error: " + e.getMessage());
				}

				// 显示错误对话框
				try {
					// 创建一个模态对话框，确保必须确认才能关闭
					JOptionPane optionPane = new JOptionPane(
							Messages.get(DesktopLauncher.class, "crash_message") + "\n\n" +
									Messages.get(DesktopLauncher.class, "crash_log_saved") + "\n\n" +
									Messages.get(DesktopLauncher.class, "version") + ": " + Game.version + "\n\n" +
									Messages.get(DesktopLauncher.class, "seed") + ": " + Dungeon.seed + "\n\n",
							JOptionPane.ERROR_MESSAGE,
							JOptionPane.DEFAULT_OPTION);

					// 创建对话框并设置为模态
					JDialog dialog = optionPane.createDialog(null, title + " - Crash Error");
					dialog.setModal(true);
					dialog.setAlwaysOnTop(true);
					dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
					dialog.setVisible(true);

					// 确保对话框被确认
					while (dialog.isVisible()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
				} catch (Exception e) {
					System.err.println("Failed to display error dialog: " + e.getMessage());
				}

				// 使用 CrashHandler 处理异常
				CrashHandler.getInstance().uncaughtException(thread, throwable);

				System.exit(1);
			}

			private String formatStackTrace(StackTraceElement[] stackTrace) {
				StringBuilder sb = new StringBuilder();
				for (StackTraceElement element : stackTrace) {
					sb.append(element.toString()).append("\n");
				}
				return sb.toString();
			}
		});

		Game.version = DesktopLauncher.class.getPackage().getSpecificationVersion();
		if (Game.version == null) {
			Game.version = System.getProperty("Specification-Version");
		}

		try {
			Game.versionCode = Integer.parseInt(DesktopLauncher.class.getPackage().getImplementationVersion());
		} catch (NumberFormatException e) {
			Game.versionCode = Integer.parseInt(System.getProperty("Implementation-Version"));
		}

		if (NewsImpl.supportsNews()) {
			News.service = NewsImpl.getNewsService();
			Updates.service = UpdateImpl.getUpdateService();
		}

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setTitle(title);

		String basePath = "";
		if (SharedLibraryLoader.isWindows) {
			if (System.getProperties().getProperty("os.name").equals("Windows XP")) {
				basePath = "Application Data/.shatteredpixel/Magic Ling Pixel Dungeon";
			} else {
				basePath = "AppData/Roaming/.shatteredpixel/Magic Ling Pixel Dungeon/";
			}
		} else if (SharedLibraryLoader.isMac) {
			basePath = "Library/Application Support/Magic Ling Pixel Dungeon/";
		} else if (SharedLibraryLoader.isLinux) {
			String XDGHome = System.getenv().get("XDG_DATA_HOME");
			if (XDGHome == null) XDGHome = ".local/share/";
			basePath = XDGHome + ".shatteredpixel/magicling-pixel-dungeon/";

			// copy over files from old linux save DIR, pre-1.2.0
			FileHandle oldBase = new Lwjgl3FileHandle(".shatteredpixel/magicling-pixel-dungeon/", Files.FileType.External);
			FileHandle newBase = new Lwjgl3FileHandle(XDGHome + ".shatteredpixel/magicling-pixel-dungeon/", Files.FileType.External);
			if (oldBase.exists()) {
				if (!newBase.exists()) {
					oldBase.copyTo(newBase.parent());
				}
				oldBase.deleteDirectory();
				oldBase.parent().delete(); // only regular delete, in case of saves from other PD versions
			}
		}

		config.setPreferencesConfig(basePath, Files.FileType.External);
		SPDSettings.set(new Lwjgl3Preferences(SPDSettings.DEFAULT_PREFS_FILE, basePath));
		FileUtils.setDefaultFileProperties(Files.FileType.External, basePath);

		config.setWindowSizeLimits(720, 400, -1, -1);
		Point p = SPDSettings.windowResolution();
		config.setWindowedMode(p.x, p.y);

		config.setMaximized(SPDSettings.windowMaximized());

		// going fullscreen on launch is a bit buggy
		// so game always starts windowed and then switches in DesktopPlatformSupport.updateSystemUI
		// config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

		// records whether window is maximized or not for settings
		DesktopWindowListener listener = new DesktopWindowListener();
		config.setWindowListener(listener);

		config.setWindowIcon("icons/icon_16.png", "icons/icon_32.png", "icons/icon_48.png",
				"icons/icon_64.png", "icons/icon_128.png", "icons/icon_256.png");

		new Lwjgl3Application(new ShatteredPixelDungeon(new DesktopPlatformSupport()), config);
	}
}
