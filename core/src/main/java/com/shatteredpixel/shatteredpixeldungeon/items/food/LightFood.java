package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.plants.AikeLaier;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class LightFood extends Food {

    public float energy = Hunger.STARVING;

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_EAT )) {
            detach( hero.belongings.backpack );
            Buff.affect(hero, Healing.class).setHeal((int) (0.4f * hero.HT + 14), 0.25f, 0);
            satisfy(hero);
            Buff.affect(hero, ChampionHero.Light.class, ChampionHero.DURATION-140f);
            hero.sprite.operate( hero.pos );
            hero.busy();
            Sample.INSTANCE.play( Assets.Sounds.DRINK );

            hero.spend( eatingTime() );
        }
    }

    {
        stackable = true;
        image = ItemSpriteSheet.LSPDA;
        //赋予快捷栏
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
        } else {
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

