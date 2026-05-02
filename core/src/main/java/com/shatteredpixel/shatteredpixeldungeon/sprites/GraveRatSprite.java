package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class GraveRatSprite extends MobSprite {

    public GraveRatSprite() {
        super();

        texture( Assets.Sprites.GRAVERAT);

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 9, true );
        idle.frames( frames, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5,6);

        run = new Animation( 9, true );
        run.frames( frames, 7,8,9,10,11 );

        attack = new Animation( 11, false );
        attack.frames( frames, 12,13,14,15);

        die = new Animation( 11, false );
        die.frames( frames, 16,17,18,19 );

        play( idle );
    }

}
