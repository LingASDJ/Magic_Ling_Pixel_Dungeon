package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class FourYearsAnimation extends MobSprite {

    public  FourYearsAnimation() {
        super();

        texture( Assets.Interfaces.Four_YEARS );

        TextureFilm frames = new TextureFilm( texture, 126, 34 );

        idle = new Animation( 11, true );
        idle.frames( frames, 0,1,1,1,1,1,1,1,1,1,1,2,3,4,5,6,6,6,6,6,6,6,6,6,6,7,8,9,10,11,11,11,11,11,11,11,11,11,11,12,13,14 );

        run = new Animation( 10, true );
        run.frames( frames, 6, 7, 8, 9, 10 );

        attack = new Animation( 15, false );
        attack.frames( frames, 2, 3, 4, 5, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 11, 12, 13, 14 );

        play( idle );
    }

}
