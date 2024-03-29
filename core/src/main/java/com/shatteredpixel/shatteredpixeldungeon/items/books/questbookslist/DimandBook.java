package com.shatteredpixel.shatteredpixeldungeon.items.books.questbookslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class DimandBook extends Books {
    private static final String Read	= "Read";
    {
        image = ItemSpriteSheet.SOYBOOKS;
        unique = true;
    }

    @Override
    public int value() {
        return quantity * 20;
    }

    @Override
    public String info() {
        return desc();
    }

    @Override
    public void execute(final Hero hero, String action) {
        if (action.equals( Read ) && Statistics.deepestFloor<10) {
            Sample.INSTANCE.play( Assets.Sounds.READ );
            detach( hero.belongings.backpack );
            GLog.n(Messages.get(DimandBook.class,"action"));
            Statistics.mimicking = true;
        } else {
            detach( hero.belongings.backpack );
            GLog.n(Messages.get(DimandBook.class,"action_s"));
        }
    }
}

