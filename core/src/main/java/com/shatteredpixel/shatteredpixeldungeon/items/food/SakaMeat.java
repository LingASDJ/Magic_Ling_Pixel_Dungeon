package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class SakaMeat extends Food {

    {
        image = ItemSpriteSheet.FISHBONE;
        energy = 200f;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        Buff.affect(hero, Bless.class, 50f);
        GLog.w( Messages.get(SakaMeat.class, "oks") );
    }

    public int value() {
        return 25 * quantity;
    }
}

