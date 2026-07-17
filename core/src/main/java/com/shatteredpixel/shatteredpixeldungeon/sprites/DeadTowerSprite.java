package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class DeadTowerSprite extends MobSprite {
    public DeadTowerSprite() {
        super();

        texture( Assets.Sprites.DEATH_SPRITE);

        TextureFilm frames = new TextureFilm( texture, 21, 25 );

        idle = new Animation( 6, true );
        idle.frames( frames, 0,1,2,3,4,5,6,7,8,9,10,11);

        attack = new Animation( 12, false );
        attack.frames( frames, 5,6,7,8,0 );

        run = new Animation( 12, true );
        run.frames( frames,  9,10,11,12,13,14,15,16);

        die = new Animation( 12, false );
        die.frames( frames, 12,13,14,15,16,17);

        play( idle );
    }
}
