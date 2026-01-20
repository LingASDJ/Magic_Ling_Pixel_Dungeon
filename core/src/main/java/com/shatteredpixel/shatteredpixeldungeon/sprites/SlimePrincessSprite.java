package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SlimePrincessSprite extends MobSprite {

    public SlimePrincessSprite() {
        super();

        texture( Assets.Sprites.SLIMEPRINCESS );

        TextureFilm frames = new TextureFilm( texture, 16 , 18);

        idle = new Animation( 3, true );
        idle.frames( frames, 0, 1, 1, 0 );

        run = new Animation( 10, true );
        run.frames( frames, 0, 2, 3, 3, 2, 0 );

        attack = new Animation( 15, false );
        attack.frames( frames, 3, 4 );

        die = new Animation( 10, false );
        die.frames( frames, 0, 4, 4, 0 );

        play(idle);
    }

    @Override
    public int blood() {
        return 0xFF88CC44;
    }

}
