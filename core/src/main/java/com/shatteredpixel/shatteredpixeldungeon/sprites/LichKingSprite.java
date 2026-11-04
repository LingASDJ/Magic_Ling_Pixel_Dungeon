package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class LichKingSprite extends MobSprite {

    public LichKingSprite() {
        super();

        texture( Assets.Sprites.LICH );

        TextureFilm frames = new TextureFilm( texture, 41, 32 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3,4,5 );

        run = new Animation( 9, true );
        run.frames( frames, 6,7,8,9,10,11);

        attack = new Animation( 11, false );
        attack.frames( frames, 12,13,14,15,16 );

        die = new Animation( 11, false );
        die.frames( frames, 22,23,24,25,26,27,28 );

        zap= new Animation( 11, false );
        zap.frames( frames, 17,18,19,20,21);

        play( idle );
    }

}

