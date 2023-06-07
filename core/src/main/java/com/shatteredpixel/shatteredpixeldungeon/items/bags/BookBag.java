package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class BookBag extends Bag {

    {
        image = ItemSpriteSheet.BOOKBAG;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        return new ArrayList<>(); //yup, no dropping this one
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
        return 10;
    }

    @Override
    public int value() {
        return 30;
    }

}

