package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class TPDoorSprites extends MobSprite {

    public  TPDoorSprites () {
        super();

        texture( Assets.Sprites.TPDP );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 6, true );
        idle.frames( frames, 0,1,2 );

        run = idle.clone();

        attack = idle.clone();

        die = new Animation( 7, false );
        die.frames( frames, 1,2 );

        play( idle );
    }

}