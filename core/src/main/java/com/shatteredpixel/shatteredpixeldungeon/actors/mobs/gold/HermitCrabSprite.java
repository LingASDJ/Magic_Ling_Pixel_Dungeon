package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class HermitCrabSprite extends MobSprite {

    public HermitCrabSprite() {
        super();

        texture( Assets.Sprites.GOLD_CRAB );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 32, 33, 32, 34 );

        run = new Animation( 15, true );
        run.frames( frames, 35, 36, 37, 38 );

        attack = new Animation( 12, false );
        attack.frames( frames, 39, 40, 41 );

        die = new Animation( 12, false );
        die.frames( frames, 42, 43, 44, 45 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xFFFFEA80;
    }
}