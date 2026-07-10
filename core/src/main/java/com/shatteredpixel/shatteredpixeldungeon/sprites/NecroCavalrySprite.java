package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class NecroCavalrySprite extends MobSprite {

    public NecroCavalrySprite() {
        super();

        texture( Assets.Sprites.NECRO_CAVALRY);

        TextureFilm frames = new TextureFilm( texture, 47, 30 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3,4,5);

        run = new Animation( 11, true );
        run.frames( frames, 6,7,8,9,10,11,12);

        attack = new Animation( 11, false );
        attack.frames( frames, 13,14,15,16);

        die = new Animation( 11, false );
        die.frames( frames, 17,18,19,20,21,22,23);

        play( idle );
    }

}