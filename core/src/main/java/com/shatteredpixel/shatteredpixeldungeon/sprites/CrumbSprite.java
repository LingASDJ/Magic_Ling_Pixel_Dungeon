package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class CrumbSprite extends MobSprite {
    public CrumbSprite() {
        super();

        texture( Assets.Sprites.CRUMB );

        TextureFilm frames = new TextureFilm( texture, 10, 13 );

        idle = new MovieClip.Animation( 16, true );
        idle.frames( frames, 0,1,2 );

        run = new MovieClip.Animation( 13, true );
        run.frames( frames, 0,1,2 );

        attack = new MovieClip.Animation( 14, false );
        attack.frames( frames, 0,1,2 );

        die = new MovieClip.Animation( 8, false );
        die.frames( frames, 3,4,5 );

        play( idle );
    }
}
