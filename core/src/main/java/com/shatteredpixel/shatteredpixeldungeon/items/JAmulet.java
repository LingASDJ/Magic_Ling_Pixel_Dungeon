/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DevItem.CrystalLing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MIME;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableSpell;
import com.shatteredpixel.shatteredpixeldungeon.scenes.JAmuletScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Game;

import java.util.ArrayList;

public class JAmulet extends Item {

    private static final String AC_END = "END";

    {
        image = ItemSpriteSheet.AMULET;

        unique = true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return Dungeon.isDLC(Conducts.Conduct.DEV)? new ItemSprite.Glowing(0x008888, 6f) : null;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean cling = false;
            boolean read = false;
            boolean book = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof MIME.GOLD_FIVE) {
                        cling = true;
                    } else if (ingredient instanceof CrystalLing) {
                        read = true;
                    } else if(ingredient instanceof UnstableSpell) {
                        book = true;
                    }
                }
            }

            return cling && read && book;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 16;
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
            return new JAmulet();
        }
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_END );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals(AC_END)) {
            showAmuletScene( false );
        }
    }

    @Override
    public boolean doPickUp(Hero hero) {
        if (super.doPickUp( hero)) {

            showAmuletScene( true );

            return true;
        } else {
            return false;
        }
    }

    private void showAmuletScene( boolean showText ) {

            JAmuletScene.noText = !showText;
            Game.switchScene(JAmuletScene.class, new Game.SceneChangeCallback() {
                @Override
                public void beforeCreate() {

                }

                @Override
                public void afterCreate() {

                }
            });

    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    public static class CrystalRecipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{JAmulet.class};
            inQuantity = new int[]{1};

            cost = 3;

            output = MIME.GOLD_FIVE.class;
            outQuantity = 1;
        }

    }

}