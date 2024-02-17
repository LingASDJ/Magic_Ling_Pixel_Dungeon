package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class NoneSprite extends MobSprite {

    public NoneSprite() {
        super();

        texture( Assets.Sprites.NONE );

        TextureFilm frames = new TextureFilm( texture, 20 ,20 );

        idle = new MovieClip.Animation( 14, true );
        idle.frames( frames, 0,0,1,1,2,2,3,3,4 );

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 6, 7, 8, 9, 10 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 2, 3, 4, 5, 0 );

        die = new MovieClip.Animation( 10, false );
        die.frames( frames, 11, 12, 13, 14 );

        play( idle );
    }

}

