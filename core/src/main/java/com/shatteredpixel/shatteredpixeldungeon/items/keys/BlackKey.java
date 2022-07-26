package com.shatteredpixel.shatteredpixeldungeon.items.keys;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class BlackKey extends Key {

    {
        image = ItemSpriteSheet.BLACK_KEY;
    }

    public BlackKey() {
        this( 0 );
    }

    public BlackKey( int depth ) {
        super();
        this.depth = depth;
    }


}
