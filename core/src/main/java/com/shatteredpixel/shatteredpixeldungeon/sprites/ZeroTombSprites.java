package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class ZeroTombSprites extends MobSprite {
    public ZeroTombSprites(){
        super();

        texture( Assets.Sprites.TOMB );

        TextureFilm frames = new TextureFilm( texture, 16, 32 );

        idle = new MovieClip.Animation( 12, true );
        idle.frames( frames, 1);

        run = new MovieClip.Animation( 14, true );
        run.frames( frames, 4,5,6,7,8,9 );

        attack = new MovieClip.Animation( 14, false );
        attack.frames( frames, 10,11,12,13,14 );

        die = new MovieClip.Animation( 11, false );
        die.frames( frames,  15,16,17,18 );

        play( idle );
    }
}

