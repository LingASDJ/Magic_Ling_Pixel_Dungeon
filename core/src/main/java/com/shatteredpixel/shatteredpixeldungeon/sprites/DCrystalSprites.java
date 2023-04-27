package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class DCrystalSprites extends MobSprite {

    public  DCrystalSprites () {
        super();

        texture( Assets.Sprites.DREA );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 1, false );
        idle.frames( frames, 0 );

        run = idle.clone();

        attack = idle.clone();

        die = new Animation( 1, false );
        die.frames( frames, 2 );

        play( idle );
    }

}
