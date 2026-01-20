package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class CrivusStarFruitsSprite extends MobSprite {
    public CrivusStarFruitsSprite(){
        super();

        perspectiveRaise = 0.2f;

        texture( Assets.Sprites.CFZS );

        TextureFilm frames = new TextureFilm( texture, 22, 26 );

        idle = new MovieClip.Animation( 8, true );
        idle.frames( frames, 0,1,2,3);

        run = new MovieClip.Animation( 1, true );
        run.frames( frames, 0 );

        attack = new MovieClip.Animation( 1, false );
        attack.frames( frames, 0 );

        die = new MovieClip.Animation( 8, false );
        die.frames( frames,  4, 5, 6,6,6 );

        play( idle );
    }
}
