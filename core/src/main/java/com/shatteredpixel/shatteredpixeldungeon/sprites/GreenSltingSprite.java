package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class GreenSltingSprite extends MobSprite {

    public GreenSltingSprite() {
        super();

        texture( Assets.Sprites.GREEN );

        TextureFilm frames = new TextureFilm( texture, 16, 20 );

        idle = new MovieClip.Animation( 5, true );
        idle.frames( frames, 0, 1, 2, 3 );

        run = new MovieClip.Animation( 14, true );
        run.frames( frames, 4,5,6,7 );

        attack = new MovieClip.Animation( 14, false );
        attack.frames( frames, 8,9,10,11,12 );

        die = new MovieClip.Animation( 14, false );
        die.frames( frames, 13,14,15 );

        play( idle );
    }

}
