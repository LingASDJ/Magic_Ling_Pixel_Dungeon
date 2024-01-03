package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SliceGirlSprite extends MobSprite {

    public SliceGirlSprite() {
        super();

        texture( Assets.Sprites.SWTICH );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 6, true );
        idle.frames( frames, 0, 1, 2, 3, 2 ,1 );

        run = new Animation( 12, true );
        run.frames( frames, 14,15,16,17 );

        attack = new Animation( 18, false );
        attack.frames( frames, 4,5,6,7,8,9,0 );

        die = new Animation( 18, false );
        die.frames( frames, 10, 12, 13 );

        play( idle );
    }

}
