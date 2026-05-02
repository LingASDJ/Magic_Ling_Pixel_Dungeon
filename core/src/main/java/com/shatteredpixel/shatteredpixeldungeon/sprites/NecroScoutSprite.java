package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class NecroScoutSprite extends MobSprite {

    public NecroScoutSprite() {
        super();

        texture( Assets.Sprites.NECRO_SCOUT);

        TextureFilm frames = new TextureFilm( texture, 22, 19 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,2,2,2,2,2,2,3
        );

        run = new Animation( 11, true );
        run.frames( frames, 4,5,6,7,8,9,10,11,12);

        attack = new Animation( 11, false );
        attack.frames( frames, 13,14,15,16);

        die = new Animation( 11, false );
        die.frames( frames, 17,18,19,20,21);

        play( idle );
    }

}
