package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class SkullSprite extends MobSprite {

    public SkullSprite() {
        super();

        texture( Assets.Sprites.SKULLS);

        TextureFilm frames = new TextureFilm( texture, 10, 10 );

        idle = new Animation( 6, true );
        idle.frames( frames, 0, 4, 5, 4, 0, 4, 5, 4, 0, 4, 5, 4, 0, 4, 5, 4, 0, 4, 5, 4,1,2,3,1,2,3 );

        run = idle.clone();

        attack = new Animation( 8, false );
        attack.frames( frames, 6,7);

        die = new Animation( 12, false );
        die.frames( frames, 6,7,8,9 );

        play( idle );
    }
}
