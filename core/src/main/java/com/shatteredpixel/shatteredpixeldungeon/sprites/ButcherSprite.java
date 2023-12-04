package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ButcherSprite extends MobSprite {

    public ButcherSprite() {
        super();

        texture( Assets.Sprites.BTSLIMH );

        TextureFilm frames = new TextureFilm( texture, 20, 16 );

        idle = new Animation( 3, true );
        idle.frames( frames, 0, 1 );

        run = new Animation( 12, true );
        run.frames( frames, 14,15,16,17 );

        attack = new Animation( 18, false );
        attack.frames( frames, 4,5,6,7,8,9,0 );

        die = new Animation( 18, false );
        die.frames( frames, 10, 12, 13 );

        play( idle );
    }

}

