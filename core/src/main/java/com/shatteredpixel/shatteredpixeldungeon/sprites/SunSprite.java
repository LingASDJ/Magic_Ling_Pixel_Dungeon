package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SunSprite extends MobSprite {

    public SunSprite() {
        super();

        texture( Assets.Sprites.ANIMATIONS_SUN );

        TextureFilm frames = new TextureFilm( texture, 24, 24 );

        idle = new Animation( 12, true );
        idle.frames( frames, 0, 1, 2, 3,4,5,6,7,8,9 );

        run = new Animation( 12, true );
        run.frames( frames, 0, 1, 2, 3,4,5,6,7,8,9 );

        attack = new Animation( 14, false );
        attack.frames( frames, 0, 1, 2, 3,4,5,6,7,8,9 );

        die = new Animation( 14, false );
        die.frames( frames, 0, 1, 2, 3,4,5,6,7,8,9 );

        play( idle );
    }
}

