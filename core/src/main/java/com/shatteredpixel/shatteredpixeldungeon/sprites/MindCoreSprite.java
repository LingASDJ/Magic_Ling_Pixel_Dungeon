package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class MindCoreSprite extends MobSprite {
    public MindCoreSprite() {
        super();

        texture( Assets.Sprites.MINDCODE );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 9, true );
        idle.frames( frames, 0,1,2,3,4,5 );

        run = new Animation( 9, true );
        run.frames( frames, 0,1,2,3,4,5 );

        attack = new Animation( 9, false );
        attack.frames( frames, 0,1,2,3,4,5 );

        die = new Animation( 9, false );
        die.frames( frames, 0,1,2,3,4,5 );

        play( idle );
    }
}
