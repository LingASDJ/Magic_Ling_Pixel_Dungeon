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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;

/**
 * 刘海屏安全区装饰器
 * 填充 PixelCamera 排除的安全区区域，避免黑屏
 */
public class SafeInsetDecorator extends Component {

    private ColorBlock leftDeco;   // 安全区A装饰（横屏=左，竖屏=上）
    private ColorBlock rightDeco;  // 安全区B装饰（横屏=右，竖屏=下）

    public SafeInsetDecorator() {
        super();
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        leftDeco = new ColorBlock(Game.width, 1, 0xFF010102);
        rightDeco = new ColorBlock(Game.width, 1, 0xFF010102);

        leftDeco.visible = true;
        rightDeco.visible = true;

        add(leftDeco);
        add(rightDeco);
    }

    @Override
    public synchronized void update() {
        float zoom = PixelScene.uiCamera.zoom;
        boolean landscape = Game.width > Game.height;

        // 屏幕物理像素 → uiCamera 坐标
        float w = Game.width / zoom;
        float h = Game.height / zoom;
        float sA = Game.safeInsetA / zoom;
        float sB = Game.safeInsetB / zoom;

        if (landscape) {
            // 横屏：A=左, B=右
            setBlock(leftDeco, 0, 0, sA, h);
            setBlock(rightDeco, w - sB, 0, sB, h);
        } else {
            // 竖屏：A=上, B=下
            setBlock(leftDeco, 0, 0, w, sA);
            setBlock(rightDeco, 0, h - sB, w, sB);
        }
    }

    private void setBlock(ColorBlock block, float x, float y, float w, float h) {
        if (w > 0.5f && h > 0.5f) {
            block.visible = true;
            block.x = x;
            block.y = y;
            block.size(w, h);
            block.origin.set(0, 0);
        } else {
            block.visible = false;
        }
    }

    public void refresh() {
        layout();
    }
}