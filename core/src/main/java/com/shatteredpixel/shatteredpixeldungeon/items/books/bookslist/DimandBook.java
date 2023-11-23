package com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist;

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
    public String info() {
        return desc();
    }

    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals( Read )) {
            Sample.INSTANCE.play( Assets.Sounds.READ );
            detach( hero.belongings.backpack );
            GLog.n(Messages.get(DimandBook.class,"action"));
            Statistics.mimicking = true;
        }
    }
}

