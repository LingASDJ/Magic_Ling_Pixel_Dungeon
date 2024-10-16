package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.ApprenticeWitch;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class ApprenticeWitchSprite extends MobSprite {

    public ApprenticeWitchSprite() {
        super();

        texture( Assets.Sprites.APWHEEL );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 9, true );
        idle.frames( frames, 0,0,0,0,0,0,0,0,0,0,1,2,3,3,3,3,3,3,3,3,3,3,4,5 );

        run = new Animation( 9, true );
        run.frames( frames, 6,7,8,9 );

        attack = new Animation( 9, false );
        attack.frames( frames, 10,11,12,13,14,15 );

        die = new Animation( 9, false );
        die.frames( frames, 16,17,18,19,20,21 );

        zap = attack.clone();

        play( idle );
    }

    public void zap( int cell ) {
        super.zap( cell );
        MagicMissile.boltFromChar( parent,
                MagicMissile.HALOFIRE,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((ApprenticeWitch)ch).onZapComplete(cell);
                        ((ApprenticeWitch)ch).isTargetingTeleport = !((ApprenticeWitch) ch).isTargetingTeleport;

                    }
                }
         );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

    public void targeting( int pos ){
        turnTo(ch.pos, pos);
        play(idle);
    }

}
