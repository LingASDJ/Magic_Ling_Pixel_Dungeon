package com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist;

import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PinkRandomBooks extends Books {

    {
        image = ItemSpriteSheet.PINKBOOKS;
        unique = true;
    }

    @Override
    public String info() {
        return desc()+"\n\n"+authorx;
    }
}
