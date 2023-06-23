package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SakaFishBossSprites extends MobSprite {

    public SakaFishBossSprites() {
        super();

        texture( Assets.Sprites.SKFS );

        TextureFilm frames = new TextureFilm( texture, 29, 15 );

        idle = new Animation( 12, true );
        idle.frames( frames, 7,7,8,9,10 );

        run = new Animation( 10, true );
        run.frames( frames, 2,3 );

        attack = new Animation( 15, false );
        attack.frames( frames, 4,5,6 );

        die = new Animation( 10, false );
        die.frames( frames, 1 );

        play( idle );
    }

}

