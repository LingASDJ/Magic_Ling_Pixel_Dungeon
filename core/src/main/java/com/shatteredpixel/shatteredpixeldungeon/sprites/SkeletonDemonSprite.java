package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SkeletonDemonSprite extends MobSprite {

    public SkeletonDemonSprite() {
        super();

        texture( Assets.Sprites.BONE_BEAST);

        TextureFilm frames = new TextureFilm( texture, 33, 24 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3,4,5);

        run = new Animation( 11, true );
        run.frames( frames,  6,7,8,9,10,11);

        attack = new Animation( 11, false );
        attack.frames( frames, 12,13,14,15);

        die = new Animation( 11, false );
        die.frames( frames, 16,17,18,19,20);

        play( idle );
    }

}

