package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class FlowerSlimeSprites extends MobSprite {

    public FlowerSlimeSprites() {
        super();

        texture( Assets.Sprites.FLOWER_SLIME );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1 );

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 2,3 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 9, 10, 11, 5, 0 );

        die = new MovieClip.Animation( 10, false );
        die.frames( frames, 4,5,6,7 );

        play( idle );
    }

}
