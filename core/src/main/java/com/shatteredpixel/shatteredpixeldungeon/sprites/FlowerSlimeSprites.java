package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class FlowerSlimeSprites extends MobSprite {

    public FlowerSlimeSprites() {
        super();

        texture( Assets.Sprites.FLOWER_SLIME );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 12, true );
        idle.frames( frames, 0, 1, 2, 3 );

        run = new MovieClip.Animation( 16, true );
        run.frames( frames, 4,5 );

        attack = new MovieClip.Animation( 16, false );
        attack.frames( frames, 6,7,8 );

        die = new MovieClip.Animation( 12, false );
        die.frames( frames,9,10,11 );

        play( idle );
    }

}
