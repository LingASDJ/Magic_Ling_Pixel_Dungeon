package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class GiantFlowerSlimeSprites extends MobSprite {
    public GiantFlowerSlimeSprites() {
        super();

        texture( Assets.Sprites.GIANT_FLOWER_SLIME );

        TextureFilm frames = new TextureFilm( texture, 20, 20 );

        idle = new MovieClip.Animation( 7, true );
        idle.frames( frames, 0,1,2,3,4 );

        run = new MovieClip.Animation( 9, true );
        run.frames( frames, 5,6,7,8 );

        attack = new MovieClip.Animation( 11, false );
        attack.frames( frames, 9,10,11,12 );

        die = new MovieClip.Animation( 11, false );
        die.frames( frames,13,14,15,16,17);

        play( idle );
    }
}
