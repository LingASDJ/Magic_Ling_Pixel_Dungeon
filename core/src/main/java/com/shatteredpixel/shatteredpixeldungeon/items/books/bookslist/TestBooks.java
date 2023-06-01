package com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AnkhInvulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class TestBooks extends Books {
    private static final String Read	= "Read";
    {
        image = ItemSpriteSheet.BLACKBOOK;
        unique= true;
    }

    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals( Read )) {
            if ( Dungeon.hero.buff(AnkhInvulnerability.class) == null ) {
                Buff.prolong(hero, AnkhInvulnerability.class, AnkhInvulnerability.DURATION*1000000f);
            } else {
                Buff.detach( hero, AnkhInvulnerability.class );
            }
        }
    }
}
