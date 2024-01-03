package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class GreenSltingSprite extends MobSprite {

    public GreenSltingSprite() {
        super();

        texture( Assets.Sprites.GREEN );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 24, true );
        idle.frames( frames, 0, 0, 0, 1 );

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 0,0,1,1,2,2 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 2,3 );

        die = new MovieClip.Animation( 10, false );
        die.frames( frames, 2,3,4 );

        play( idle );
    }

}
