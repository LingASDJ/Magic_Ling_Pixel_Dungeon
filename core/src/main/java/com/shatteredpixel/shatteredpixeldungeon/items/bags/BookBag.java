package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class BookBag extends Bag {

    {
        image = ItemSpriteSheet.BOOKBAG;
    }

    @Override
    public boolean canHold( Item item ) {
        if (item instanceof Books){
            return super.canHold(item);
        } else {
            return false;
        }
    }

    public int capacity(){
        return 30;
    }

    @Override
    public int value() {
        return 90;
    }

}

