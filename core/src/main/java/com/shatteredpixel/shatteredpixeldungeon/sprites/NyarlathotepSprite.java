package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class NyarlathotepSprite extends MobSprite {

    public NyarlathotepSprite() {
        super();

        texture( Assets.Sprites.NYARLATHOTEP );

        TextureFilm frames = new TextureFilm( texture, 20, 18 );

        idle = new MovieClip.Animation( 6, true );
        idle.frames( frames, 0,1,2,3,0,1,2,3,4,5,6,7,8,9,10,11,8,9,10,11,12,13,14,15,16,17,18,19,16,17,18,19,20,21,22,23
        );

        run = new MovieClip.Animation( 6, true );
        run.frames( frames, 0,1,2,3,0,1,2,3,4,5,6,7,8,9,10,11,8,9,10,11,12,13,14,15,16,17,18,19,16,17,18,19,20,21,22,23
        );
        attack = new MovieClip.Animation( 9, false );
        attack.frames( frames, 24,25,26,27
        );

        die = new MovieClip.Animation( 6, false );
        die.frames( frames, 16,17,18,19,16,17,18,19,20,21,22,23);

        play( idle );
    }

}
