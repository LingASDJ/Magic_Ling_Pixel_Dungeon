package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class CrivusFruitsFood extends Food {
    {
        stackable = true;
        image = ItemSpriteSheet.CrivusFruitFood;

        defaultAction = AC_EAT;

        bones = true;
    }

    protected void satisfy( Hero hero ){
        Buff.affect(hero, Hunger.class).satisfy(50f);
        Buff.prolong(hero, Bless.class, 20f);
        Buff.affect(hero, Adrenaline.class, 10f);
        hero.spend( eatingTime() );
        GLog.p(Messages.get(this, "eat_msg2"));
    }
}
