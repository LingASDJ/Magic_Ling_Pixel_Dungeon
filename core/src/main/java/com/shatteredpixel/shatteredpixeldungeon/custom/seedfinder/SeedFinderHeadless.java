package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;
import com.watabou.utils.GameSettings;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 查种子子进程的最小无头环境。
 * 世界生成会触发一批 UI 相关的静态初始化（如 ItemSpriteSheet -&gt; TextureCache -&gt; Pixmap、
 * Catalog -&gt; QuickRecipe -&gt; ItemSlot -&gt; BitmapText），它们要读 assets、要字体并访问 Gdx 静态字段；
 * 子进程没有窗口，这里把这些依赖补齐，让生成走与父进程完全相同的代码路径。
 */
public final class SeedFinderHeadless {

	private static boolean installed;

	private SeedFinderHeadless() {}

	/**
	 * 由各平台的子进程入口调用一次。
	 * @param files 已按平台初始化过的 assets 解析器（打包后 assets 仍在 classpath 内）
	 * @param externalPath 与父进程一致的外部目录（Gdx 的 External 类型由此解析，可为 null）
	 * @param prefs 与父进程指向同一存储的偏好设置；为 null 时给一个空实现
	 */
	public static synchronized void install(Files files, String externalPath, Preferences prefs) {
		if (installed) return;
		installed = true;

		//Android 子进程会预先 System.load 所有 .so；此处 loadLibrary 可能因"已加载"抛异常，吞掉即可
		try {
			GdxNativesLoader.load();
		} catch (Throwable ignored) {
		}
		Gdx.files = files;
		Gdx.gl = Gdx.gl20 = stub(GL20.class);
		Gdx.graphics = stub(Graphics.class);
		Preferences settings = prefs != null ? prefs : stub(Preferences.class);
		Gdx.app = new StubApplication(settings);

		//少量静态代码路径会用到 Game.instance（如 Game.scene()）
		new Game(PixelScene.class, null);

		GameSettings.set(settings);
		if (externalPath != null)
			FileUtils.setDefaultFileProperties(Files.FileType.External, externalPath);

		//父进程是在“查种子模式”下做生成的，子进程必须一致
		SeedFinder.SeedFinding = true;

		//世界生成会构造 ItemSlot（Catalog 的静态初始化），需要缩放与像素字体
		if (PixelScene.pixelFont == null) {
			PixelScene.pixelFont = BitmapText.Font.colorMarked(
					TextureCache.get(Assets.Fonts.PIXELFONT), 0, BitmapText.Font.LATIN_FULL);
			PixelScene.pixelFont.baseLine = 6;
			PixelScene.pixelFont.tracking = -1;
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T stub(Class<T> type) {
		return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, DEFAULT_STUB);
	}

	/** 无头环境下不会被真正渲染，代理只需返回各类型的默认值 */
	private static final InvocationHandler DEFAULT_STUB = new InvocationHandler() {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) {
			String name = method.getName();
			if (method.getDeclaringClass() == Object.class) {
				if ("toString".equals(name)) return "seedfinder-stub";
				if ("hashCode".equals(name)) return System.identityHashCode(proxy);
				if ("equals".equals(name)) return proxy == args[0];
			}
			Class<?> type = method.getReturnType();
			if (type == boolean.class) return false;
			if (type == int.class) return 0;
			if (type == long.class) return 0L;
			if (type == float.class) return 0f;
			if (type == double.class) return 0d;
			if (type == short.class) return (short) 0;
			if (type == byte.class) return (byte) 0;
			if (type == char.class) return (char) 0;
			return null;
		}
	};

	/** 只提供日志（写进子进程的输出文件）与偏好设置，其余一律为空 */
	private static final class StubApplication implements Application {

		private final Preferences prefs;
		private ApplicationLogger logger;
		private int logLevel = LOG_INFO;

		StubApplication(Preferences prefs) {
			this.prefs = prefs;
		}

		@Override
		public ApplicationListener getApplicationListener() {
			return null;
		}

		@Override
		public Graphics getGraphics() {
			return Gdx.graphics;
		}

		@Override
		public Audio getAudio() {
			return null;
		}

		@Override
		public Input getInput() {
			return null;
		}

		@Override
		public Files getFiles() {
			return Gdx.files;
		}

		@Override
		public Net getNet() {
			return null;
		}

		@Override
		public void log(String tag, String message) {
			if (logLevel >= LOG_INFO) System.out.println("[" + tag + "] " + message);
		}

		@Override
		public void log(String tag, String message, Throwable exception) {
			log(tag, message);
			if (exception != null) exception.printStackTrace(System.out);
		}

		@Override
		public void error(String tag, String message) {
			if (logLevel >= LOG_ERROR) System.err.println("[" + tag + "] " + message);
		}

		@Override
		public void error(String tag, String message, Throwable exception) {
			error(tag, message);
			if (exception != null) exception.printStackTrace(System.err);
		}

		@Override
		public void debug(String tag, String message) {
			if (logLevel >= LOG_DEBUG) System.out.println("[" + tag + "] " + message);
		}

		@Override
		public void debug(String tag, String message, Throwable exception) {
			debug(tag, message);
			if (exception != null) exception.printStackTrace(System.out);
		}

		@Override
		public void setLogLevel(int logLevel) {
			this.logLevel = logLevel;
		}

		@Override
		public int getLogLevel() {
			return logLevel;
		}

		@Override
		public void setApplicationLogger(ApplicationLogger applicationLogger) {
			this.logger = applicationLogger;
		}

		@Override
		public ApplicationLogger getApplicationLogger() {
			return logger;
		}

		@Override
		public ApplicationType getType() {
			return ApplicationType.HeadlessDesktop;
		}

		@Override
		public int getVersion() {
			return 0;
		}

		@Override
		public long getJavaHeap() {
			return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		}

		@Override
		public long getNativeHeap() {
			return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		}

		@Override
		public Preferences getPreferences(String name) {
			return prefs;
		}

		@Override
		public Clipboard getClipboard() {
			return null;
		}

		@Override
		public void postRunnable(Runnable runnable) {
			if (runnable != null) runnable.run();
		}

		@Override
		public void exit() {
		}

		@Override
		public void addLifecycleListener(LifecycleListener listener) {
		}

		@Override
		public void removeLifecycleListener(LifecycleListener listener) {
		}
	}
}