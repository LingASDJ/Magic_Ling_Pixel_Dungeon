package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ZakoSprite extends MobSprite {

    public ZakoSprite() {
        super();

        texture( Assets.Sprites.ZAKO );

        TextureFilm frames = new TextureFilm( texture, 13, 15 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        play( idle );
    }
}

