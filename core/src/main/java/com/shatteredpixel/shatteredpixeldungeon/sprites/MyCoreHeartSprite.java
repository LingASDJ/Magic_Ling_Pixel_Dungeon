package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class MyCoreHeartSprite extends MobSprite {

    public MyCoreHeartSprite() {
        super();

        texture( Assets.Sprites.MYSTIC_CORE );

        TextureFilm frames = new TextureFilm( texture, 28, 37 );

        idle = new MovieClip.Animation( 9, true );
        idle.frames( frames, 0,1,2,3,4,5,6,7,8,9);

        run = new MovieClip.Animation( 9, true );
        run.frames( frames, 0,1,2,3,4,5,6,7,8,9);

        attack = new MovieClip.Animation( 16, false );
        attack.frames( frames, 0,1,2,3);

        die = new MovieClip.Animation( 9, false );
        die.frames( frames, 10,11,12,13,14,15,16);

        play( idle );
    }

}
