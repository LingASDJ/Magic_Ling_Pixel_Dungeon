package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SmallLightSprites extends MobSprite {

    public SmallLightSprites() {
        super();

        texture( Assets.Sprites.SMSLIGHT );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0,1,2 );

        run = new Animation( 8, true );
        run.frames( frames, 0,1,2 );

        die = new Animation( 8, false );
        die.frames( frames, 3,4,5,6 );

        play( idle );

    }
}
