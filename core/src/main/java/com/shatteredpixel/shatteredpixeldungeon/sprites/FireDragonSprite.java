package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class FireDragonSprite extends MobSprite {
    public FireDragonSprite() {
        super();

        texture( Assets.Sprites.FRDG );

        TextureFilm frames = new TextureFilm( texture, 24, 24 );

        idle = new Animation( 8, true );
        idle.frames( frames, 4,5,9,10,11 );

        run = new Animation( 8, true );
        run.frames( frames, 1, 2, 1, 2 );

        attack = new Animation( 15, false );
        attack.frames( frames, 3, 4, 3, 4 );

        die = new Animation( 7, false );
        die.frames( frames, 10,11,12);

        play( idle );
    }
}
