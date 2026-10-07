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

package com.watabou.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.Game;

import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public abstract class PlatformSupport {
	public void setOnscreenKeyboardVisible(boolean value){
		Gdx.input.setOnscreenKeyboardVisible(value);
	}
	public abstract void updateDisplaySize();

	public abstract void updateSystemUI();

	public abstract boolean connectedToUnmeteredNetwork();

	public abstract boolean supportsVibration();

	public void vibrate( int millis ){
		if (ControllerHandler.isControllerConnected()) {
			ControllerHandler.vibrate(millis);
		} else {
			Gdx.input.vibrate( millis );
		}
	}

	//TODO should consider spinning this into its own class, rather than platform support getting ever bigger
	protected static HashMap<FreeTypeFontGenerator, HashMap<Integer, BitmapFont>> fonts;

	protected static FreeTypeFontGenerator fallbackFontGenerator;

	protected int pageSize;
	protected PixmapPacker packer;
	protected boolean systemfont;

	public abstract void setupFontGenerators(int pageSize, boolean systemFont );

    protected FreeTypeFontGenerator getGeneratorForString(String input) {
        return null;
    }

    public String[] splitforTextBlock(String text, boolean multiline) {
		// Simple default splitter (no regex, so it is safe on all platforms).
		// Platforms with CJK text rendering requirements should override this.
		if (text == null || text.length() == 0) return new String[]{""};
		ArrayList<String> pieces = new ArrayList<>();
		StringBuilder cur = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\n' || c == '_' || c > 255) {
				if (cur.length() > 0) {
					pieces.add(cur.toString());
					cur.setLength(0);
				}
				if (c == '\n' || c == '_') pieces.add(String.valueOf(c));
			} else {
				cur.append(c);
			}
		}
		if (cur.length() > 0) pieces.add(cur.toString());
		return pieces.toArray(new String[0]);
	}

	/**
	 * Whether this platform can show a native file-open dialog.
	 * Defaults to false (web cannot browse the local filesystem).
	 */
	public boolean supportsFileDialogs(){
		return false;
	}

	/**
	 * Shows a native file-open dialog and delivers the selected absolute path
	 * (or null when cancelled / unsupported) to the callback. The callback is
	 * always invoked on the GL render thread.
	 */
	public void openFileDialog(String title, String[] extensions, Consumer<String> callback){
		//default: unsupported, deliver null
		if (callback != null) com.badlogic.gdx.Gdx.app.postRunnable(() -> callback.accept(null));
	}

	/**
	 * Performs a synchronous HTTP GET and returns the response body, or null
	 * when the request failed / the platform has no synchronous HTTP support.
	 */
	public String httpGet(String url){
		return null;
	}

	/**
	 * Installs an insecure, trust-all TLS configuration for outgoing HTTPS
	 * requests. Needed by some desktop environments with self-signed game
	 * servers; a no-op on platforms whose networking cannot be configured
	 * this way (web uses the browser's native TLS).
	 */
	public void setupInsecureTls(){
		//default: no-op
	}

	/**
	 * Runs a task outside the GL render thread. Default implementation defers
	 * it to the render thread (safe everywhere); desktop overrides with a real
	 * background thread to keep networking off the UI thread.
	 */
	public void runAsync(Runnable task){
		if (task != null) com.badlogic.gdx.Gdx.app.postRunnable(task);
	}

	/**
	 * Returns the HTTP Date header of {@code url} as epoch milliseconds, or
	 * -1 when the request failed / the platform cannot read response headers.
	 * Used by the title screen's NTP check.
	 */
	public long getHttpDate(String url){
		return -1;
	}

	/**
	 * Opens a local directory in the system file manager.
	 * Returns false when unsupported (web/mobile).
	 */
	public boolean openDirectory(String path){
		return false;
	}

	/**
	 * Validates/normalizes a settings.xml backup (Android-era raw entry format).
	 * No-op by default (web).
	 */
	public void ensureSettingsXmlValid(FileHandle settingsHandle){
		//default: no-op
	}

	/**
	 * Converts an Android-era map settings backup to a standard format.
	 * Pass-through by default (web).
	 */
	public byte[] convertAndroidMapSettings(byte[] raw){
		return raw;
	}

	public void resetGenerators(){
		resetGenerators( true );
	}

	public void resetGenerators( boolean setupAfter ){
		if (fonts != null) {
			for (FreeTypeFontGenerator generator : fonts.keySet()) {
				for (BitmapFont f : fonts.get(generator).values()) {
					f.dispose();
				}
				fonts.get(generator).clear();
				generator.dispose();
			}
			fonts.clear();
			if (packer != null) {
				for (PixmapPacker.Page p : packer.getPages()) {
					p.getTexture().dispose();
				}
				packer.dispose();
			}
			fonts = null;
		}
		if (setupAfter) setupFontGenerators(pageSize, systemfont);
	}

	public void reloadGenerators(){
		if (packer != null) {
			for (FreeTypeFontGenerator generator : fonts.keySet()) {
				for (BitmapFont f : fonts.get(generator).values()) {
					f.dispose();
				}
				fonts.get(generator).clear();
			}
			if (packer != null) {
				for (PixmapPacker.Page p : packer.getPages()) {
					p.getTexture().dispose();
				}
				packer.dispose();
			}
			packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
		}
	}

	//flipped is needed because Shattered's graphics are y-down, while GDX graphics are y-up.
	//this is very confusing, I know.
	public BitmapFont getFont(int size, String text, boolean flipped, boolean border, boolean fallback) {
		FreeTypeFontGenerator generator = fallback ? fallbackFontGenerator : getGeneratorForString(text);

		if (generator == null){
			return null;
		}

		int key = size;
		if (border) key += Short.MAX_VALUE; //surely we'll never have a size above 32k
		if (flipped) key = -key;
		if (!fonts.get(generator).containsKey(key)) {
			FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameters.size = size;
			parameters.flip = flipped;
			if (border) {
				parameters.borderWidth = parameters.size / 10f;
			}
			if (size >= 20){
				parameters.renderCount = 2;
			} else {
				parameters.renderCount = 3;
			}
			parameters.hinting = FreeTypeFontGenerator.Hinting.None;
			parameters.spaceX = -(int) parameters.borderWidth;
			parameters.incremental = true;
			parameters.characters = "�";
			parameters.packer = packer;

			try {
				BitmapFont font = generator.generateFont(parameters);
				font.getData().missingGlyph = font.getData().getGlyph('�');
				fonts.get(generator).put(key, font);
			} catch ( Exception e ) {
				Game.reportException(e);
				return null;
			}
		}

		return fonts.get(generator).get(key);
	}

	public void setHonorSilentSwitch( boolean value ){
		//does nothing by default
	}

	public boolean isAndroid() {
		return Gdx.app.getType() == Application.ApplicationType.Android;
	}

	public boolean isDesktop() {
		return Gdx.app.getType() == Application.ApplicationType.Desktop;
	}

	public boolean openURI( String URI ) {
		return Gdx.net.openURI(URI);
	}

	// 更新游戏
	public abstract void updateGame(String url, UpdateCallback listener);

	public abstract void install(File file);
	public interface UpdateCallback {

		/**
		 * 最开始调用(在onStart之前调用)
		 *
		 * @param isDownloading 为true时，表示已经在下载；为false时，表示当前未开始下载，即将开始下载
		 */
		void onDownloading(boolean isDownloading);

		/**
		 * 开始
		 */
		void onStart(String url);

		/**
		 * 更新进度
		 *
		 * @param progress  当前进度大小
		 * @param total     总文件大小
		 * @param isChanged 进度百分比是否有改变，（主要可以用来过滤无用的刷新，从而降低刷新频率）
		 */
		void onProgress(long progress, long total, boolean isChanged);

		/**
		 * 完成
		 *
		 * @param file APK文件
		 */
		void onFinish(File file);

		/**
		 * 错误
		 *
		 * @param e 异常
		 */
		void onError(Exception e);

		/**
		 * 取消
		 */
		void onCancel();
	}
}