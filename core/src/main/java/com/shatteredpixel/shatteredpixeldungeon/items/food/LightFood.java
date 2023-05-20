package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.plants.AikeLaier;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class LightFood extends Food {

    public float energy = Hunger.STARVING;

    {
        stackable = true;
        image = ItemSpriteSheet.LSPDA;
        defaultAction = AC_EAT;
        bones = true;
    }



    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{AikeLaier.Seed.class, Food.class, FrozenCarpaccio.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 6;

            output = LightFood.class;
            outQuantity = 1;
        }

    }

    protected void satisfy( Hero hero ){
        if (Dungeon.isChallenged(Challenges.NO_FOOD)){
            Buff.affect(hero, Hunger.class).satisfy(energy/3f);
            Buff.affect(hero, Healing.class).setHeal((int) (0.6f * hero.HT + 14), 0.25f, 0);
        } else {
            Buff.affect(hero, Healing.class).setHeal((int) (0.8f * hero.HT + 14), 0.25f, 0);
            Buff.affect(hero, Hunger.class).satisfy(energy);
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

    @Override
    public int value() {
        return 10 * quantity;
    }
}

