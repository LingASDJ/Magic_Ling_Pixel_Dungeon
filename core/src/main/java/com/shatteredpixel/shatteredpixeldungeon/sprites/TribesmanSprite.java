package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class TribesmanSprite extends MobSprite {

    public TribesmanSprite() {
        super();

        texture( Assets.Sprites.HEROGNOLL );

        TextureFilm frames = new TextureFilm( texture, 13, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        run = new Animation( 12, true );
        run.frames( frames, 5, 6, 7, 8 );

        attack = new Animation( 12, false );
        attack.frames( frames, 2, 3, 4, 0 );

        die = new Animation( 12, false );
        die.frames( frames, 9, 10, 11 );

        play( idle );
    }
}