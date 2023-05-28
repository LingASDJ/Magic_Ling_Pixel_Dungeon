package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SeaVastGirlSprites extends MobSprite {

    public SeaVastGirlSprites() {
        super();

        texture( Assets.Sprites.VSGR );

        TextureFilm frames = new TextureFilm( texture, 24, 24 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1,1,1,2 );

        run = new Animation( 8, true );
        run.frames( frames, 3, 4, 5, 6 );

        attack = new Animation( 15, false );
        attack.frames( frames, 7, 8, 9, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 0 );

        play( idle );
    }

}
