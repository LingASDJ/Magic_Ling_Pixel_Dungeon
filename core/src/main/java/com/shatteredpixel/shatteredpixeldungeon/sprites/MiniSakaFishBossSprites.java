package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class MiniSakaFishBossSprites extends MobSprite {

    public MiniSakaFishBossSprites() {
        super();

        texture( Assets.Sprites.SKFSBABY );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0,1,2,3 );

        run = new Animation( 8, true );
        run.frames( frames, 4,5,6,7 );

        die = new Animation( 16, false );
        die.frames( frames, 8,9,10,11 );

        play( idle );

    }
}

