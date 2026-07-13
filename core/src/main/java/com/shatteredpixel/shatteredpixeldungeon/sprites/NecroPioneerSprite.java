package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class NecroPioneerSprite extends MobSprite {

    public NecroPioneerSprite() {
        super();

        texture( Assets.Sprites.NECRO_PIONEER);

        TextureFilm frames = new TextureFilm( texture, 30, 20 );

        idle = new Animation( 12, true );
        idle.frames( frames, 0,1,2,3,4,5,6,7,8,9);

        run = new Animation( 12, true );
        run.frames( frames, 10,11,12,13,14,15,16);

        attack = new Animation( 12, false );
        attack.frames( frames, 17,18,19,20,21);

        zap = new Animation( 12, false );
        zap.frames( frames,  22,23,24,25,26);

        die = new Animation( 12, false );
        die.frames( frames, 27,28,29,30,31);

        play( idle );
    }

}
