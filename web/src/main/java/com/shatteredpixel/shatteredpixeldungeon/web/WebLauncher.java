/*
 * Magic Ling Pixel Dungeon
 * Web launcher: sets up the TeaVM web backend, browser storage and fonts.
 */

package com.shatteredpixel.shatteredpixeldungeon.web;

import com.badlogic.gdx.Files;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;

import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;

public class WebLauncher {

	public static void main(String[] args) {

		Game.version = "0.9.6.5-WEB";
		Game.versionCode = 2026100600;

		// saves go into the browser's IndexedDB-backed local storage
		FileUtils.setDefaultFileProperties(Files.FileType.Local, "magic-ling-pixel-dungeon/");

		WebApplicationConfiguration config = new WebApplicationConfiguration("canvas");
		config.width = 0;
		config.height = 0;
		config.storagePrefix = "magic-ling-pixel-dungeon";
		config.showDownloadLogs = true;
		// 本 fork 的着色器按 WebGL2/GLES3 编写（#version 300 es + gl30），必须启用 WebGL2 上下文
		config.useGL30 = true;
		// load the FreeType implementation shipped by gdx-freetype-teavm before the game starts
		config.preloadListener = assetLoader -> assetLoader.loadScript("freetype.js");

		new WebApplication(new WebShatteredPixelDungeon(new WebPlatformSupport()), config);
	}
}
