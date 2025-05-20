package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class TowerTimeSprite extends MobSprite {

    public TowerTimeSprite() {
        super();

        texture( Assets.Sprites.TowerTime );

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new MovieClip.Animation( 10, true );
        idle.frames( frames, 0 );

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 0 );

        attack = new MovieClip.Animation( 10, false );
        attack.frames( frames, 0 );

        die = new MovieClip.Animation( 9, false );
        die.frames( frames, 1,2,3,4,5 );

        play( idle );
    }

}
