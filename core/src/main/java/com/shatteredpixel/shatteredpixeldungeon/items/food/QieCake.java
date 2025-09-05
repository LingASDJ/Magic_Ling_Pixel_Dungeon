package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ToxicImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class QieCake extends Food{

    {
        image = ItemSpriteSheet.Qie_Cake;
        energy = 200f;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        Buff.affect(hero, Barkskin.class).set(6, 20);
        Buff.affect(hero, ToxicImbue.class).set(20f);
    }
}
