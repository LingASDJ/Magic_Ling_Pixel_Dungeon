package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SpawnereEvilSprite extends MobSprite {

    public SpawnereEvilSprite() {
        super();

        texture( Assets.Sprites.EVIL_SPAWN );

        TextureFilm frames = new TextureFilm( texture, 12, 13 );

        idle = new Animation( 9, true );
        idle.frames( frames,  0,1,2,3,4,5,6);

        run = new Animation( 9, true );
        run.frames( frames, 0,1,2,3,4,5,6);

        attack = new Animation( 15, false );
        attack.frames( frames, 0 );

        die = new Animation( 11, false );
        die.frames( frames, 7,8,9,10,11,12,13
        );

        play( idle );
    }

}

