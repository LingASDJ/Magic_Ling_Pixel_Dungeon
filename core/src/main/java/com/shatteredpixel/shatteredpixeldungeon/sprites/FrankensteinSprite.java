package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class FrankensteinSprite extends MobSprite {

    public FrankensteinSprite() {
        super();

        texture( Assets.Sprites.ZOMBIE );

        TextureFilm frames = new TextureFilm( texture, 16, 20 );

        idle = new MovieClip.Animation( 1, true );
        idle.frames( frames, 0, 1 );

        run = new MovieClip.Animation( 14, true );
        run.frames( frames, 2,3,4,5 );

        attack = new MovieClip.Animation( 14, false );
        attack.frames( frames, 6,7,8,9,0 );

        die = new MovieClip.Animation( 14, false );
        die.frames( frames, 10, 12, 13 );

        play( idle );
    }

}

