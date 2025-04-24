package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class TribemanOldSprite extends MobSprite {

    public TribemanOldSprite() {
        super();

        texture( Assets.Sprites.HEROGNOLLOLD );

        TextureFilm frames = new TextureFilm( texture, 13, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1,  12, 13, 12, 13, 12, 13 );

        run = new Animation( 12, true );
        run.frames( frames, 5, 6, 7, 8 );

        attack = new Animation( 12, false );
        attack.frames( frames, 2, 3, 4, 0 );

        die = new Animation( 12, false );
        die.frames( frames, 9, 10, 11 );

        play( idle );
    }
}
