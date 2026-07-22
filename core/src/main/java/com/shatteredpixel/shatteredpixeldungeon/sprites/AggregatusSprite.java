package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Aggregatus;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class AggregatusSprite extends MobSprite {

    public AggregatusSprite() {
        super();

        texture( Assets.Sprites.AGGREATUS);
        TextureFilm frames = new TextureFilm( texture, 34, 30 );

        idle = new Animation( 9, true );
        idle.frames( frames, 0, 1, 2, 3, 4, 5, 6, 7 );

        run = new Animation( 9, true );
        run.frames( frames, 8, 9, 10, 11, 12, 13, 14 );

        attack = new Animation( 11, false );
        attack.frames( frames, 15, 16, 17, 18 );

        zap = new Animation( 11, false );
        zap.frames( frames, 15, 16, 17, 18 );

        die = new Animation( 12, false );
        die.frames( frames, 19, 20, 21, 22 );

        play( idle );
    }

    @Override
    public void zap( int cell ) {
        turnTo( ch.pos, cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.SHADOW,
                this,
                cell,
                () -> ((Aggregatus)ch).onZapComplete());
        Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
            return;
        }
        super.onComplete( anim );
    }
}

