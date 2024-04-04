package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class ClearElemtGuardGirlSprites extends MobSprite {
    public ClearElemtGuardGirlSprites(){
        super();

        texture( Assets.Sprites.FAYINA );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 7, true );
        idle.frames( frames, 0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9);

        run = new MovieClip.Animation( 14, true );
        run.frames( frames, 4,5,6,7,8,9 );

        attack = new MovieClip.Animation( 14, false );
        attack.frames( frames, 10,11,12,13,14 );

        die = new MovieClip.Animation( 11, false );
        die.frames( frames,  15,16,17,18 );

        play( idle );
    }
}
