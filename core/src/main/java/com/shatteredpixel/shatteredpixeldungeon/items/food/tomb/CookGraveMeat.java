package com.shatteredpixel.shatteredpixeldungeon.items.food.tomb;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class CookGraveMeat extends Food {

    {
        image = ItemSpriteSheet.CookGraveMeat;
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
        }
    }

    public static Food cook( int quantity ) {
        CookGraveMeat result = new CookGraveMeat();
        result.quantity = quantity;
        return result;
    }

}


