package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class QliphothSprite extends MobSprite {

    public QliphothSprite(){
        super();

        texture( Assets.Sprites.QLPH );

        TextureFilm frames = new TextureFilm( texture, 24, 24 );

        idle = new MovieClip.Animation( 7, true );
        idle.frames( frames, 0,1,2,3,4);

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 0 );

        attack = new MovieClip.Animation( 10, false );
        attack.frames( frames, 0 );

        die = new MovieClip.Animation( 8, false );
        die.frames( frames, 5, 6,7,8,9,10,11,11,11 );

        play( idle );
    }

}
