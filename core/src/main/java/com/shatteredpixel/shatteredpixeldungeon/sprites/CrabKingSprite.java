package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class CrabKingSprite extends MobSprite {

    public CrabKingSprite() {
        super();

        texture( Assets.Sprites.CRAB_KING );

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new Animation( 4, true );
        idle.frames( frames, 0, 1, 2, 3 );

        run = new Animation( 9, true );
        run.frames( frames, 4,5,6,7,8,9);

        attack = new Animation( 9, false );
        attack.frames( frames, 10,11,12,13,14);

        die = new Animation( 11, false );
        die.frames( frames, 15,16,17,18,19,20 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xFFFFEA80;
    }

}
