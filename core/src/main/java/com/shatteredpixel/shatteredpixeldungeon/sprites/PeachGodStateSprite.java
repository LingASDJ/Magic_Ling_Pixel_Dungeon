package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class PeachGodStateSprite extends MobSprite {

    public PeachGodStateSprite() {
        super();

        texture( Assets.Sprites.PEACHGODSTATUE );

        TextureFilm frames = new TextureFilm( texture, 16, 20 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 0 );

        run = new Animation( 10, true );
        run.frames( frames, 6, 7, 8, 9, 10 );

        attack = new Animation( 15, false );
        attack.frames( frames, 2, 3, 4, 5, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 11, 12, 13, 14 );

        play( idle );
    }

}
