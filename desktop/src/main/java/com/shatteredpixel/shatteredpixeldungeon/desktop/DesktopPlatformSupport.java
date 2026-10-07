/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.Game;
import com.watabou.utils.PlatformSupport;
import com.watabou.utils.Point;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DesktopPlatformSupport extends PlatformSupport {

	//we recall previous window sizes as a workaround to not save maximized size to settings
	//have to do this as updateDisplaySize is called before maximized is set =S
	protected static Point[] previousSizes = null;

	@Override
	public void updateDisplaySize() {
		if (previousSizes == null){
			previousSizes = new Point[2];
			previousSizes[0] = previousSizes[1] = new Point(Game.width, Game.height);
		} else {
			previousSizes[1] = previousSizes[0];
			previousSizes[0] = new Point(Game.width, Game.height);
		}
		if (!SPDSettings.fullscreen()) {
			SPDSettings.windowResolution( previousSizes[0] );
		}
	}

	private static boolean first = false;

	@Override
	public void updateSystemUI() {
		Gdx.app.postRunnable( new Runnable() {
			@Override
			public void run () {
				if (SPDSettings.fullscreen()){
					int monitorNum = 0;
					if (!first){
						Graphics.Monitor[] monitors = Gdx.graphics.getMonitors();
						for (int i = 0; i < monitors.length; i++){
							if (((Lwjgl3Graphics.Lwjgl3Monitor)Gdx.graphics.getMonitor()).getMonitorHandle()
									== ((Lwjgl3Graphics.Lwjgl3Monitor)monitors[i]).getMonitorHandle()) {
								monitorNum = i;
							}
						}
					} else {
						monitorNum = SPDSettings.fulLScreenMonitor();
					}

					Graphics.Monitor[] monitors = Gdx.graphics.getMonitors();
					if (monitors.length <= monitorNum) {
						monitorNum = 0;
					}
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode(monitors[monitorNum]));
					SPDSettings.fulLScreenMonitor(monitorNum);
				} else {
					Point p = SPDSettings.windowResolution();
					Gdx.graphics.setWindowedMode( p.x, p.y );
				}
				first = false;
			}
		} );
	}

	@Override
	public boolean connectedToUnmeteredNetwork() {
		return true; //no easy way to check this in desktop, just assume user doesn't care
	}

	@Override
	public boolean supportsVibration() {
		return ControllerHandler.vibrationSupported();
	}

	//TODO backported openURI fix from libGDX-1.10.1-SNAPSHOT, remove when updating libGDX
	public boolean openURI( String uri ){
		if (SharedLibraryLoader.isMac) {
			try {
				(new ProcessBuilder("open", (new URI(uri).toString()))).start();
				return true;
			} catch (Throwable t) {
				return false;
			}
		} else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI(uri));
				return true;
			} catch (Throwable t) {
				return false;
			}
		} else if (SharedLibraryLoader.isLinux) {
			try {
				(new ProcessBuilder("xdg-open", (new URI(uri).toString()))).start();
				return true;
			} catch (Throwable t) {
				return false;
			}
		}
		return false;
	}

	/* FONT SUPPORT */

	//custom pixel font, for use with Latin and Cyrillic languages
	private static FreeTypeFontGenerator basicFontGenerator;
	//droid sans fallback, for asian fonts
	private static FreeTypeFontGenerator asianFontGenerator;

	@Override
	public void setupFontGenerators(int pageSize, boolean systemfont) {
		//don't bother doing anything if nothing has changed
		if (fonts != null && this.pageSize == pageSize && this.systemfont == systemfont){
			return;
		}
		this.pageSize = pageSize;
		this.systemfont = systemfont;

		resetGenerators(false);
		fonts = new HashMap<>();

		basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));
		asianFontGenerator = SPDSettings.systemFont() ?  new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf")) : new FreeTypeFontGenerator(Gdx.files.internal("fonts/fusion_pixel.ttf"));
		fallbackFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));

		fonts.put(basicFontGenerator, new HashMap<>());
		fonts.put(asianFontGenerator, new HashMap<>());
		fonts.put(fallbackFontGenerator, new HashMap<>());

		packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
	}

	private static Matcher asianMatcher = Pattern.compile("\\p{InHangul_Syllables}|" +
			"\\p{InCJK_Unified_Ideographs}|\\p{InCJK_Symbols_and_Punctuation}|\\p{InHalfwidth_and_Fullwidth_Forms}|" +
			"\\p{InHiragana}|\\p{InKatakana}").matcher("");

	@Override
	protected FreeTypeFontGenerator getGeneratorForString( String input ){
		if (asianMatcher.reset(input).find()){
			return asianFontGenerator;
		} else {
			return basicFontGenerator;
		}
	}

	//splits on newlines, underscores, and chinese/japaneses characters
	private Pattern regularsplitter = Pattern.compile(
			"(?<=\n)|(?=\n)|(?<=_)|(?=_)|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})");

	//additionally splits on words, so that each word can be arranged individually
	private Pattern regularsplitterMultiline = Pattern.compile(
			"(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})");

	@Override
	public String[] splitforTextBlock(String text, boolean multiline) {
		if (multiline) {
			return regularsplitterMultiline.split(text);
		} else {
			return regularsplitter.split(text);
		}
	}
	@Override
	public void updateGame(String url, UpdateCallback listener) {
		// TODO
	}

	@Override
	public void install(File file) {
		// TODO
	}

	/* PLATFORM BRIDGES (used by core code on desktop only) */

	@Override
	public boolean supportsFileDialogs(){
		return true;
	}

	@Override
	public void openFileDialog(String title, String[] extensions, Consumer<String> callback){
		// Swing dialog must run on the AWT event dispatch thread (EDT)
		javax.swing.SwingUtilities.invokeLater(() -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle(title);
			chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			chooser.setPreferredSize(new java.awt.Dimension(800, 600));
			if (extensions != null && extensions.length > 0) {
				chooser.setFileFilter(new FileNameExtensionFilter(
						"(*." + String.join(", *.", extensions) + ")", extensions));
			}

			// a hidden undecorated owner frame keeps the dialog modal and on top
			javax.swing.JFrame tempOwnerFrame = new javax.swing.JFrame();
			tempOwnerFrame.setUndecorated(true);
			tempOwnerFrame.setSize(1, 1);
			tempOwnerFrame.setLocationRelativeTo(null);
			tempOwnerFrame.setVisible(true);

			int ret = chooser.showOpenDialog(tempOwnerFrame);
			tempOwnerFrame.dispose();

			final File selected = chooser.getSelectedFile();
			final String path = (ret == JFileChooser.APPROVE_OPTION && selected != null)
					? selected.getAbsolutePath() : null;

			// deliver the result back on the GL render thread
			Gdx.app.postRunnable(() -> callback.accept(path));
		});
	}

	@Override
	public void runAsync(Runnable task){
		new Thread(task).start();
	}

	@Override
	public long getHttpDate(String url){
		try {
			setupInsecureTls();
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(4000);
			conn.setReadTimeout(4000);
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.connect();
			return conn.getDate();
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public boolean openDirectory(String path){
		if (path == null || path.isEmpty()) return false;
		try {
			File f = new File(path);
			if (!f.isDirectory()) return false;
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(f.toURI());
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void setupInsecureTls(){
		try {
			TrustManager[] trustAllCerts = new TrustManager[] {
					new X509TrustManager() {
						public X509Certificate[] getAcceptedIssuers() {
							return null;
						}
						public void checkClientTrusted(X509Certificate[] certs, String authType) {
						}
						public void checkServerTrusted(X509Certificate[] certs, String authType) {
						}
					}
			};

			// install an all-trusting TrustManager
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// create a HostnameVerifier that does not validate the host name
			HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
		} catch (Exception e) {
			Game.reportException(e);
		}
	}

	@Override
	public String httpGet(String url){
		try {
			setupInsecureTls();

			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(4000);
			conn.setReadTimeout(4000);
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");

			conn.connect();

			InputStream inputStream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			inputStream.close();

			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void ensureSettingsXmlValid(FileHandle settingsHandle) {
		if (settingsHandle == null || !settingsHandle.exists()) return;
		String raw;
		try {
			raw = settingsHandle.readString("UTF-8");
		} catch (Exception e) {
			Game.reportException(e);
			return;
		}
		// 已含 DOCTYPE：尝试解析，能解析即为标准格式，不动
		if (raw.contains("<!DOCTYPE properties")) {
			try (InputStream in = settingsHandle.read()) {
				Properties p = new Properties();
				p.loadFromXML(in);
				return;
			} catch (Exception e) {
				Game.reportException(e);
			}
		}

		StringBuilder out = new StringBuilder();
		out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.append("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">\n");
		out.append("<properties>\n");
		Pattern p = Pattern.compile(
				"<entry\\s+key=\"([^\"]*)\"[^>]*>(.*?)</entry>",
				Pattern.DOTALL);
		Matcher m = p.matcher(raw);
		boolean found = false;
		while (m.find()) {
			out.append("<entry key=\"").append(escapeXmlText(unescapeXmlText(m.group(1)))).append("\">")
					.append(escapeXmlText(unescapeXmlText(m.group(2)))).append("</entry>\n");
			found = true;
		}
		if (!found) return;
		out.append("</properties>\n");
		try {
			settingsHandle.writeString(out.toString(), false, "UTF-8");
		} catch (Exception e) {
			Game.reportException(e);
		}
	}

	@Override
	public byte[] convertAndroidMapSettings(byte[] raw) {
		if (raw == null || raw.length == 0) return null;
		String text;
		try {
			text = new String(raw, "UTF-8");
		} catch (Exception e) {
			return null;
		}
		if (!text.contains("<map")) return null; // 不是安卓 map 格式
		Properties props = new Properties();
		Pattern p = Pattern.compile(
				"<(string|int|long|boolean|float)\\s+name=\"([^\"]*)\"[^>]*>(.*?)</\\1>",
				Pattern.DOTALL);
		Matcher m = p.matcher(text);
		boolean found = false;
		while (m.find()) {
			props.setProperty(unescapeXmlText(m.group(2)), unescapeXmlText(m.group(3)));
			found = true;
		}
		if (!found) return null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			props.storeToXML(bos, null);
			return bos.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}

	/** 宽松解码 XML 实体（与 BackupSaveScene 原实现一致） */
	private static String unescapeXmlText(String s) {
		if (s == null || s.indexOf('&') == -1) return s;
		StringBuilder sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != '&') {
				sb.append(c);
				continue;
			}
			int semi = s.indexOf(';', i);
			if (semi == -1 || semi - i > 10) {
				sb.append(c);
				continue;
			}
			String ent = s.substring(i + 1, semi);
			switch (ent) {
				case "amp":  sb.append('&');  i = semi; continue;
				case "lt":   sb.append('<');  i = semi; continue;
				case "gt":   sb.append('>');  i = semi; continue;
				case "quot": sb.append('"');  i = semi; continue;
				case "apos": sb.append('\''); i = semi; continue;
			}
			if (ent.length() > 1 && ent.charAt(0) == '#') {
				try {
					int cp = (ent.length() > 2 && (ent.charAt(1) == 'x' || ent.charAt(1) == 'X'))
							? Integer.parseInt(ent.substring(2), 16)
							: Integer.parseInt(ent.substring(1));
					sb.appendCodePoint(cp);
					i = semi;
					continue;
				} catch (NumberFormatException ignored) {
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/** 与 java.util.Properties.storeToXML 一致的转义（与 BackupSaveScene 原实现一致） */
	private static String escapeXmlText(String s) {
		StringBuilder sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case '&':  sb.append("&amp;");  break;
				case '<':  sb.append("&lt;");   break;
				case '>':  sb.append("&gt;");   break;
				case '"':  sb.append("&quot;"); break;
				case '\'': sb.append("&apos;"); break;
				case '\t': sb.append("&#09;");  break;
				case '\n': sb.append("&#10;");  break;
				case '\r': sb.append("&#13;");  break;
				default:   sb.append(c);
			}
		}
		return sb.toString();
	}
}
