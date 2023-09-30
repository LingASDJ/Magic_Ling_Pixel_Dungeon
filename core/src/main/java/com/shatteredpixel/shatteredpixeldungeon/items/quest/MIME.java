/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;


public class MIME extends Item {

    public boolean isMimeSupported = false;

    {
        image = ItemSpriteSheet.DG21;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
    @Override
    public int value() {
        return quantity *  Random.Int(2500,5500);
    }

    public static class GOLD_ONE extends MIME {
        {
            image = ItemSpriteSheet.MIME_ONE;
        }
        @Override
        public int value() {
            return quantity * 750;
        }
    }

    public static class GOLD_TWO extends MIME {
        {
            image = ItemSpriteSheet.MIME_TWO;
        }
        @Override
        public int value() {
            return quantity * 450;
        }
    }

    public static class GOLD_THREE extends MIME {
        {
            image = ItemSpriteSheet.MIME_THREE;
        }
        @Override
        public int value() {
            return quantity * 150;
        }
    }

    public static class GOLD_FOUR extends MIME {
        {
            image = ItemSpriteSheet.MIME_FOUR;
        }
        @Override
        public int value() {
            return quantity * 250;
        }
    }

    public static class GOLD_FIVE extends MIME {
        {
            image = ItemSpriteSheet.MIME_FIVE;
        }
        @Override
        public int value() {
            return quantity * 1000;
        }
    }

    public boolean doPickUp(Hero hero, int pos) {
        if (super.doPickUp(hero, pos)) {
            if(!isMimeSupported){
                isMimeSupported = true;
            }
            return true;
        } else {
            return false;
        }
    }

    private static final String COMBO = "combox";
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        isMimeSupported = bundle.getBoolean(COMBO);
    }

    public void restoreInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        isMimeSupported = bundle.getBoolean(COMBO);
    }

}
