package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ThreeLeafBugSprite extends MobSprite {

    public ThreeLeafBugSprite() {
        super();

        texture( Assets.Sprites.THREEBUG );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 10, true );
        idle.frames( frames, 0, 1, 2 , 0, 1, 2 );

        run = new Animation( 15, true );
        run.frames( frames, 1,3,1,4 );

        attack = new Animation( 12, false );
        attack.frames( frames, 3, 4, 5, 0 );

        die = new Animation( 12, false );
        die.frames( frames, 6, 7, 8, 9,10,11,12 );

        play( idle );
    }

}

