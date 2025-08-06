package com.shatteredpixel.shatteredpixeldungeon.items.food.fantong;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class BoneSoup extends Food {

    {
        image = ItemSpriteSheet.BONESOUP;
        energy = Hunger.HUNGRY / 2f;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public int value() {
        return 8 * quantity;
    }

    @Override
    public void effect(Hero hero) {
        GLog.i(Messages.get(BoneSoup.class, "effect"));
        Barkskin.conditionallyAppend(hero, 5 + hero.lvl, 1);
    }
}
