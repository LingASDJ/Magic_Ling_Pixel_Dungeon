package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;

public class TianscarSprite extends MobSprite {

    public TianscarSprite() {
        super();

        texture("Tianscar.png");

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 1, 1, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 6, 7, 8, 9, 10,11 );

        attack = new Animation( 15, false );
        attack.frames( frames, 2, 3, 4, 5, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 11, 12, 13, 14 );

        play( idle );
    }
}

