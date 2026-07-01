package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ThiefSoulSprite extends MobSprite {

    public ThiefSoulSprite() {
        super();

        texture( Assets.Sprites.THIEF_SOUL);

        TextureFilm frames = new TextureFilm( texture, 17, 15 );

        idle = new Animation( 9, true );
        idle.frames( frames, 0,0,0,0,0,0,0,0,0,1,2,2,2,2,2,2,2,2,2,3);

        run = new Animation( 11, true );
        run.frames( frames,  4,5,6,7,8,9);

        attack = new Animation( 11, false );
        attack.frames( frames, 10,11,12);

        die = new Animation( 11, false );
        die.frames( frames, 13,14,15,16);

        play( idle );
    }

}

