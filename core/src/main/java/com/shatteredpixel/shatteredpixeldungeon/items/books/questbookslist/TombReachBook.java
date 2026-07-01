package com.shatteredpixel.shatteredpixeldungeon.items.books.questbookslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class TombReachBook extends Books {
    private static final String Read	= "Read";
    {
        image = ItemSpriteSheet.REACH_TOMB;
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove( AC_DROP );
        actions.remove( AC_THROW );
        return actions;
    }

    @Override
    public int value() {
        return quantity * 20;
    }


    @Override
    public void execute(final Hero hero, String action) {
        if (action.equals( Read )) {
            Sample.INSTANCE.play( Assets.Sounds.READ );
            detach( hero.belongings.backpack );
            GLog.n(Messages.get(this,"action"));
            Statistics.Tomb_Reach = true;
        }
    }

}

