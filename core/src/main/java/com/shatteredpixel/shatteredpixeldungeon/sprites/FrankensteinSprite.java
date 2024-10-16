package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class FrankensteinSprite extends MobSprite {

    public FrankensteinSprite() {
        super();

        texture( Assets.Sprites.ZOMBIE );

        TextureFilm frames = new TextureFilm( texture, 16, 20 );

        idle = new MovieClip.Animation( 5, true );
        idle.frames( frames, 0,1,2,3,4 );

        run = new MovieClip.Animation( 9, true );
        run.frames( frames, 5,6,7,8,9,10,11 );

        attack = new MovieClip.Animation( 11, false );
        attack.frames( frames, 12,13,14,15,16,17 );

        die = new MovieClip.Animation( 11, false );
        die.frames( frames, 18,19,20,21 );

        play( idle );
    }

}

