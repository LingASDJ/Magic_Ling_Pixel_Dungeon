package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class WispSprite extends MobSprite {

    public WispSprite() {
        super();

        texture( Assets.Sprites.WISP);

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3,4,5,6,7);

        run = new Animation( 9, true );
        run.frames( frames, 8,9,10,11,12,13 );

        attack = new Animation( 11, false );
        attack.frames( frames, 14,15,16,17,18);

        die = new Animation( 11, false );
        die.frames( frames, 19,20,21,22,23,24);

        play( idle );
    }

}
