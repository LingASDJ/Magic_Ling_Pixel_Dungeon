package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class PiraLandSprite extends MobSprite {
    public PiraLandSprite() {
        super();

        texture( Assets.Sprites.PIRANHA_LAND );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 3, true );
        idle.frames( frames, 0, 1, 2, 3 );

        run = new Animation( 9, true );
        run.frames( frames, 4, 5, 6 );

        attack = new Animation( 12, false );
        attack.frames( frames, 7, 8, 0 );

        die = new Animation( 12, false );
        die.frames( frames, 9, 10, 11 );

        play( idle );
    }
}
