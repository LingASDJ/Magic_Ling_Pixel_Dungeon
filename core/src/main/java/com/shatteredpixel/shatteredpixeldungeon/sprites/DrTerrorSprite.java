package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;

public class DrTerrorSprite extends MobSprite {

    public DrTerrorSprite() {
        super();

        texture( "SRPD/AsenathWaite.png" );

        TextureFilm frames = new TextureFilm( texture, 13, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 2,3,4,5,6,7 );

        attack = new Animation( 15, false );
        attack.frames( frames, 13,14,15, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 8,9,10,11,12 );

        play( idle );
    }

}