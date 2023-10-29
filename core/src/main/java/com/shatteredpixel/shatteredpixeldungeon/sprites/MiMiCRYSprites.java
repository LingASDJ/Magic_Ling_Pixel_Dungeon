package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class MiMiCRYSprites extends MimicSprite {

    public MiMiCRYSprites() {
        super();

        texture( Assets.Sprites.MIMICRY );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 5, true );
        idle.frames( frames, 0, 1, 2 );

        run = new MovieClip.Animation( 9, true );
        run.frames( frames, 2,3,4,5 );

        attack = new MovieClip.Animation( 6, false );
        attack.frames( frames,4,5,6);

        die = new MovieClip.Animation( 6, false );
        die.frames( frames, 7,8,9,10,11,12 );

        play( idle );
    }

}

