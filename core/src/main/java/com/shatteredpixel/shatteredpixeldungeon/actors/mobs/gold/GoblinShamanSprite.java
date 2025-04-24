package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Random;

public abstract class GoblinShamanSprite extends MobSprite {

    protected Class particleType;

    protected abstract int texOffset();

    public GoblinShamanSprite() {
        super();

        int c = texOffset();

        texture( Assets.Sprites.GOBLINSHAMAN );

        TextureFilm frames = new TextureFilm( texture, 12, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, c+0, c+0, c+0, c+1, c+0, c+0, c+1, c+1 );

        run = new Animation( 12, true );
        run.frames( frames, c+2, c+3, c+4, c+5, c+6, c+7, c+8 );

        attack = new Animation( 12, false );
        attack.frames( frames, Random.Int(c+9,c+13),Random.Int(c+9,c+13),Random.Int(c+9,c+13),Random.Int(c+9,c+13),Random.Int(c+9,c+13),Random.Int(c+9,c+13));

        die = new Animation( 12, false );
        die.frames( frames, c+14, c+15, c+16, c+17 );

        play( idle );
    }

    public static class ShamanStrength extends GoblinShamanSprite {
        @Override
        protected int texOffset() {
            return 0;
        }
    }

    public static class ShamanShield extends GoblinShamanSprite {
        @Override
        protected int texOffset() {
            return 18;
        }
    }

    public static class ShamanRegen extends GoblinShamanSprite {
        @Override
        protected int texOffset() {
            return 36;
        }
    }
    public static class ShamanFake extends GoblinShamanSprite {
        @Override
        protected int texOffset() {
            return 54;
        }
    }
}
