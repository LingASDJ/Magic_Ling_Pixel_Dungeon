package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class FlareBullet extends Item {

    {
        image = ItemSpriteSheet.FLARE;

        stackable = true;
        unique = true;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{PotionOfLiquidFlame.class, UnstableBrew.class, PotionOfLevitation.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 5;

            output = FlareBullet.class;
            outQuantity = 1;
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}

