package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class FireWitchGirlSprites extends MobSprite {

    public FireWitchGirlSprites() {
        super();

        texture( Assets.Sprites.FSGR );

        TextureFilm frames = new TextureFilm( texture, 24, 24 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1,1,1 );

        run = new Animation( 8, true );
        run.frames( frames, 2,3, 4, 5 );

        attack = new Animation( 15, false );
        attack.frames( frames, 6,7, 8,0 );

        die = new Animation( 7, false );
        die.frames( frames, 9,10,11);

        play( idle );
    }

}

