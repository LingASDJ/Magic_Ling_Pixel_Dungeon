package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.SothothLasher;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class SothothLasherSprite extends MobSprite {

    public SothothLasherSprite() {
        super();

        texture( Assets.Sprites.LATS );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 6, true );
        idle.frames( frames, 0,1,2,3);

        run = new Animation( 6, true );
        run.frames( frames, 0,1,2,3);

        attack = new Animation( 24, false );
        attack.frames( frames, 5,6,7,8,9 );

        die = new Animation( 12, false );
        die.frames( frames, 10,11,12,13,14 );

        zap = attack.clone();

        play( idle );
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.HALOFIRE,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((SothothLasher)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

    @Override
    public int blood() {
        return 0xFF88CC44;
    }
}

