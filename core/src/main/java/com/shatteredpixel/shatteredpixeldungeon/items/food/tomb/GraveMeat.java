package com.shatteredpixel.shatteredpixeldungeon.items.food.tomb;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class GraveMeat extends Food {

    {
        image = ItemSpriteSheet.GraveMeat;
        energy = Hunger.HUNGRY/2f;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    public int value() {
        return 5 * quantity;
    }

    public void effect(Hero hero){
        switch (Random.Int( 5 )) {
            case 0:
                GLog.w( Messages.get(this, "poison") );
                Buff.affect( hero, Poison.class ).set( hero.HT / 5 );
                break;
            case 1:
                GLog.w( Messages.get(this, "vertigo") );
                Buff.prolong( hero, Vertigo.class, 15f );
                break;
            case 2:
                Buff.affect(hero, Corrosion.class).set(5f, 2);
                GLog.w( Messages.get(this, "coison") );
                break;
            case 3:
                GLog.w( Messages.get(this, "weakness") );
                Buff.prolong( hero, Weakness.class, 45f );
                break;
            case 4:
                GLog.w( Messages.get(this, "downlevel") );
                Buff.prolong( hero, Degrade.class, 20f );
                break;
        }
    }
}

