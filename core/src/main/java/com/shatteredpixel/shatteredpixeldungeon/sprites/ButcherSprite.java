package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ButcherSprite extends MobSprite {

    public ButcherSprite() {
        super();

        texture( Assets.Sprites.BTSLIMH );

        TextureFilm frames = new TextureFilm( texture, 20, 16 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 1 );

        run = new Animation( 16, true );
        run.frames( frames, 2,3,4,5,6,7 );

        attack = new Animation( 16, false );
        attack.frames( frames, 9,10,11,12 );

        die = new Animation( 16, false );
        die.frames( frames, 13, 14, 15 );

        play( idle );
    }

}

