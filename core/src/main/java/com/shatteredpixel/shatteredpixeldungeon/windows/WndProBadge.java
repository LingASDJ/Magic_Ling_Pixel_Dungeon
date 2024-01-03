/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.effects.PasswordBadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

public class WndProBadge extends Window {

    private static final int WIDTH = 120;
    private static final int MARGIN = 4;

    public WndProBadge(PaswordBadges.Badge badge) {

        super();

        Image icon = PasswordBadgeBanner.image( badge.image );
        icon.scale.set( 2 );

        add( icon );

        RenderedTextBlock title = PixelScene.renderTextBlock( badge.title(), 9 );
        title.maxWidth(WIDTH - MARGIN * 2);
        title.align(RenderedTextBlock.CENTER_ALIGN);
        title.hardlight(TITLE_COLOR);

        add(title);

        String desc = badge.desc();

        RenderedTextBlock info = PixelScene.renderTextBlock( desc, 6 );
        info.maxWidth(WIDTH - MARGIN * 2);
        info.align(RenderedTextBlock.CENTER_ALIGN);
        add(info);

        float w = Math.max( icon.width(), Math.max(title.width(), info.width()) ) + MARGIN * 2;

        icon.x = (w - icon.width()) / 2f;
        icon.y = MARGIN;
        PixelScene.align(icon);

        title.setPos((w - title.width()) / 2, icon.y + icon.height() + MARGIN);
        PixelScene.align(title);

        info.setPos((w - info.width()) / 2, title.bottom() + MARGIN);
        PixelScene.align(info);
        resize( (int)w, (int)(info.bottom() + MARGIN) );

        PasswordBadgeBanner.highlight( icon, badge.image );
    }
}