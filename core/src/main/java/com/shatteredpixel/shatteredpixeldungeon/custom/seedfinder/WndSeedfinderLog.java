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

package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTabbed;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class WndSeedfinderLog extends WndTabbed {

    protected static final int WIDTH_MIN = 120;
    protected static final int WIDTH_MAX = 280;
    protected static final int GAP = 1;

    private ArrayList<RenderedTextBlock> texts = new ArrayList<>();

    public WndSeedfinderLog(Image icon, String title, String... messages) {

        super();

        int width = WIDTH_MIN;

//        PointerArea blocker = new PointerArea(0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height);
//        //do not go back on screen click
//        blocker.camera = PixelScene.uiCamera;
//        add(blocker);

        IconTitle titlebar = new IconTitle(icon, title);
        titlebar.setRect(0, 0, width, 0);
        add(titlebar);

        RenderedTextBlock largest = null;
        for (int i = 0; i < messages.length; i++) {

            RenderedTextBlock text = PixelScene.renderTextBlock(6);
            text.text(messages[i], width);
            text.setPos(titlebar.left(), titlebar.bottom() + 2 * GAP);
            add(text);
            texts.add(text);

            if (largest == null || text.height() > largest.height()) {
                largest = text;
            }

            int finalI = i;
            add(new LabeledTab(numToNumeral(finalI)) {
                @Override
                protected void select(boolean value) {
                    super.select(value);
                    texts.get(finalI).visible = value;
                }
            });
        }

        while (PixelScene.landscape()
                && largest.bottom() > (PixelScene.MIN_HEIGHT_L - 20)
                && width < WIDTH_MAX) {
            width += 10;
            titlebar.setRect(0, 0, width, 0);

            largest = null;
            for (RenderedTextBlock text : texts) {
                text.setPos(titlebar.left(), titlebar.bottom() + 2 * GAP);
                text.maxWidth(width);
                if (largest == null || text.height() > largest.height()) {
                    largest = text;
                }
            }
        }

        bringToFront(titlebar);

        resize(width, (int) largest.bottom() + 2);

        layoutTabs();
        select(0);

    }

    private String numToNumeral(int num) {
        return Integer.toString(num);

    }
}
