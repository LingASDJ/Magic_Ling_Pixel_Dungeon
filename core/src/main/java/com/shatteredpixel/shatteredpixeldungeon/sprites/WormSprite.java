package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class WormSprite extends MobSprite {

    public WormSprite() {
        super();

        texture( Assets.Sprites.WORM);

        TextureFilm frames = new TextureFilm( texture, 24, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3,4,5,6);

        run = new Animation( 9, true );
        run.frames( frames, 7,8,9,10,11,12 );

        attack = new Animation( 11, false );
        attack.frames( frames, 13,14,15,16);

        die = new Animation( 11, false );
        die.frames( frames, 17,18,19,20,21 );

        play( idle );
    }

}
