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

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;


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

        public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

            @Override
            public boolean testIngredients(ArrayList<Item> ingredients) {
                boolean cling = false;
                boolean read = false;
                boolean book = false;

                for (Item ingredient : ingredients){
                    if (ingredient.quantity() > 0) {
                        if (ingredient instanceof MIME.GOLD_ONE) {
                            cling = true;
                        } else if (ingredient instanceof MIME.GOLD_FOUR) {
                            read = true;
                        } else if(ingredient instanceof Books) {
                            book = true;
                        }
                    }
                }

                return cling && read && book;
            }

            @Override
            public int cost(ArrayList<Item> ingredients) {
                return 18;
            }

            @Override
            public Item brew(ArrayList<Item> ingredients) {
                if (!testIngredients(ingredients)) return null;

                for (Item ingredient : ingredients){
                    ingredient.quantity(ingredient.quantity() - 1);
                }

                return sampleOutput(null);
            }

            @Override
            public Item sampleOutput(ArrayList<Item> ingredients) {
                return new MIME.GOLD_FIVE();
            }
        }


    }

    public boolean doPickUp(Hero hero, int pos) {
        if (super.doPickUp(hero, pos)) {
            if(!isMimeSupported){
                Statistics.dimandchestmazeCollected++;
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
    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        isMimeSupported = bundle.getBoolean(COMBO);
    }

}
