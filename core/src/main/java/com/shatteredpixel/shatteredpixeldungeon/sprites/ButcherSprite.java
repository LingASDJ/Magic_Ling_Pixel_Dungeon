package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ButcherSprite extends MobSprite {

    public ButcherSprite() {
        super();

        texture( Assets.Sprites.BTSLIMH );

        TextureFilm frames = new TextureFilm( texture, 20, 17 );

        idle = new Animation( 11, true );
        idle.frames( frames, 0,1,2,3,4 );

        run = new Animation( 12, true );
        run.frames( frames, 5,6,7,8,9,10 );

        attack = new Animation( 11, false );
        attack.frames( frames, 11,12,13,14,15,16,17 );

        die = new Animation( 11, false );
        die.frames( frames, 18,19,20,21,22,23 );

        play( idle );
    }

}

