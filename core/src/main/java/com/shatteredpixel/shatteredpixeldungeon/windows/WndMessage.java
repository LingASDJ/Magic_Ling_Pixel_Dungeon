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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ui.Component;

public class WndMessage extends Window {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;
	private static final int MARGIN = 4;

	public WndMessage(String text) {

		super();

		RenderedTextBlock info = PixelScene.renderTextBlock(text, 6);
		info.maxWidth((PixelScene.landscape() ? WIDTH_L : WIDTH_P) - MARGIN * 2);

		if ((int) info.height() + MARGIN * 2 < Camera.main.height * .9f) {
			info.setPos(MARGIN, MARGIN);
			add(info);
			resize(
					(int) info.width() + MARGIN * 2,
					(int) info.height() + MARGIN * 2);
		} else {
			info.setPos(0, 0);
			resize(
					(int) info.width() + MARGIN * 2,
					(int) (Camera.main.height * .9f));
			Component component = new Component();
			ScrollPane sp = new ScrollPane(component);
			component.add(info);
			add(sp);
			info.setPos(0,0);
			component.setSize(info.width()+MARGIN*2, info.height() + MARGIN*2);
			sp.setRect(0, MARGIN, component.width(), (int) (Camera.main.height * .9f)-MARGIN*2);

		}

	}
}