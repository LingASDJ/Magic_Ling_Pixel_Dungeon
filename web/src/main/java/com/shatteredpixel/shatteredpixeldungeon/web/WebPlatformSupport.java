/*
 * Magic Ling Pixel Dungeon
 * Web platform support: font generation, text splitting and browser-specific
 * no-ops. Regex-free implementations are used wherever possible because
 * TeaVM's java.util.regex support is limited (no Unicode block properties).
 */

package com.shatteredpixel.shatteredpixeldungeon.web;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.utils.PlatformSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class WebPlatformSupport extends PlatformSupport {

	private static FreeTypeFontGenerator basicFontGenerator;
	private static FreeTypeFontGenerator asianFontGenerator;

	@Override
	public void updateDisplaySize() {
		// Browser canvas sizing is owned by WebApplicationConfiguration.
	}

	@Override
	public void updateSystemUI() {
		// Browser chrome and fullscreen prompts cannot be controlled like native UI.
	}

	@Override
	public boolean connectedToUnmeteredNetwork() {
		return true;
	}

	@Override
	public boolean supportsVibration() {
		return Gdx.input.isPeripheralAvailable(com.badlogic.gdx.Input.Peripheral.Vibrator);
	}

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

	@Override
	protected FreeTypeFontGenerator getGeneratorForString(String input) {
		for (int i = 0; i < input.length(); i++) {
			if (isAsianChar(input.charAt(i))) {
				return asianFontGenerator;
			}
		}
		return basicFontGenerator;
	}

	@Override
	public String[] splitforTextBlock(String text, boolean multiline) {
		if (text == null || text.length() == 0) return new String[]{""};
		ArrayList<String> pieces = new ArrayList<>();
		StringBuilder current = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			boolean doubleAsterisk = ch == '*' && i + 1 < text.length() && text.charAt(i + 1) == '*';
			boolean boundary = ch == '\n' || ch == '_' || isAsianChar(ch) || (multiline && ch == ' ');
			if (doubleAsterisk) {
				flushTextPiece(current, pieces);
				pieces.add("**");
				i++;
			} else if (boundary) {
				flushTextPiece(current, pieces);
				pieces.add(String.valueOf(ch));
			} else {
				current.append(ch);
			}
		}
		flushTextPiece(current, pieces);
		return pieces.toArray(new String[0]);
	}

	private static void flushTextPiece(StringBuilder current, ArrayList<String> pieces) {
		if (current.length() > 0) {
			pieces.add(current.toString());
			current.setLength(0);
		}
	}

	private static boolean isAsianChar(char ch) {
		return (ch >= 0xAC00 && ch <= 0xD7AF)      // Hangul Syllables
				|| (ch >= 0x4E00 && ch <= 0x9FFF)  // CJK Unified Ideographs
				|| (ch >= 0x3000 && ch <= 0x303F)  // CJK Symbols and Punctuation
				|| (ch >= 0xFF00 && ch <= 0xFFEF)  // Halfwidth and Fullwidth Forms
				|| (ch >= 0x3040 && ch <= 0x309F)  // Hiragana
				|| (ch >= 0x30A0 && ch <= 0x30FF); // Katakana
	}

	@Override
	public void updateGame(String url, UpdateCallback listener) {
		// Web builds can't download and install APKs; no-op.
	}

	@Override
	public void install(File file) {
		// Web builds can't install APKs; no-op.
	}
}
