package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class ShubNiggurathSprite extends MobSprite {

    public ShubNiggurathSprite() {
        super();

        texture( Assets.Sprites.SHUBNIGGURATH );

        TextureFilm frames = new TextureFilm( texture, 42, 40 );

        idle = new MovieClip.Animation( 8, true );
        idle.frames( frames, 0,1,2,3,4,5 );

        run = new MovieClip.Animation( 8, true );
        run.frames( frames, 0,1,2,3,4,5 );

        attack = new MovieClip.Animation( 8, false );
        attack.frames( frames, 0,1,2,3,4,5 );

        die = new MovieClip.Animation( 8, false );
        die.frames( frames, 0,1,2,3,4,5 );

        play( idle );
    }

}