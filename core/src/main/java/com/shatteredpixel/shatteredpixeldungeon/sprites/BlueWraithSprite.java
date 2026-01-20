package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class BlueWraithSprite extends MobSprite {

    public BlueWraithSprite() {
        super();

        texture( Assets.Sprites.REDWRAITH );

        TextureFilm frames = new TextureFilm( texture, 14, 15 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 0, 1 );

        attack = new Animation( 10, false );
        attack.frames( frames, 0, 2, 3 );

        die = new Animation( 8, false );
        die.frames( frames, 0, 4, 5, 6, 7 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0x88000000;
    }
}
