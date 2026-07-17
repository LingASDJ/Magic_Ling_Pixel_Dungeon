package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class WormWhyHumanSprite extends MobSprite {

    public WormWhyHumanSprite() {
        super();

        texture( Assets.Sprites.WORM_WHYHUMAN);

        TextureFilm frames = new TextureFilm( texture, 21, 21 );

        idle = new Animation( 3, true );
        idle.frames( frames, 0,1,2,3,4,5,6,7,8,9);

        run = new Animation( 12, true );
        run.frames( frames, 10,11,12,13,14,15,16,17,18,19,20 );

        attack = new Animation( 12, false );
        attack.frames( frames, 21,22,23,24);

        die = new Animation( 12, false );
        die.frames( frames, 25,26,27,28,29 );

        play( idle );
    }

}

