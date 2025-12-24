package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class GhoulPlusSprite extends MobSprite {

    public GhoulPlusSprite() {
        super();

        texture( Assets.Sprites.GHOUL_HILL );

        TextureFilm frames = new TextureFilm( texture, 22, 20 );

        idle = new Animation( 7, true );
        idle.frames( frames,  0,1,2,3,4);

        run = new Animation( 9, true );
        run.frames( frames, 5,6,7,8);

        attack = new Animation( 11, false );
        attack.frames( frames, 9,10,11,12 );

        die = new Animation( 11, false );
        die.frames( frames, 13,14,15,16,17);

        play( idle );
    }

}
