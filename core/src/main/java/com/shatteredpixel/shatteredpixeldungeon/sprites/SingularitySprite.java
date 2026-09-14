package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class SingularitySprite extends MobSprite {

    public SingularitySprite() {
        super();

        texture( Assets.Sprites.ANIMATIONS_DARKSUN );

        TextureFilm frames = new TextureFilm( texture, 27, 15 );

        idle = new MovieClip.Animation( 12, true );
        idle.frames( frames, 0,1,2,3,4,5,6,7 );

        run = new MovieClip.Animation( 12, true );
        run.frames( frames, 0,1,2,3,4,5,6,7 );

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 0,1,2,3,4,5,6,7 );

        die = new MovieClip.Animation( 12, false );
        die.frames( frames, 0,1,2,3,4,5,6,7 );

        play( idle );
    }
}

