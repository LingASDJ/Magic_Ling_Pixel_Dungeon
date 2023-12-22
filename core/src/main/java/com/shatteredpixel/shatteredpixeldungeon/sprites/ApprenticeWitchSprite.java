package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ApprenticeWitchSprite extends MobSprite {

    public ApprenticeWitchSprite() {
        super();

        texture( Assets.Sprites.APWHEEL );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0, 0, 0, 0, 0,1,2,3,3,3,3,3,2,1 );

        run = new Animation( 16, true );
        run.frames( frames, 14,15,16,17 );

        attack = new Animation( 16, false );
        attack.frames( frames, 4,5,6,7,8,9 );

        die = new Animation( 16, false );
        die.frames( frames, 10, 11,12, 13 );

        play( idle );
    }

}
