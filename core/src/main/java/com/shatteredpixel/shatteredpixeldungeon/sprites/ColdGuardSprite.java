package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdGurad;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class ColdGuardSprite extends MobSprite {

    public ColdGuardSprite() {
        super();

        texture( Assets.Sprites.CRID );

        TextureFilm frames = new TextureFilm( texture, 12, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        run = new MovieClip.Animation( 15, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 8, 9, 10 );

        die = new MovieClip.Animation( 8, false );
        die.frames( frames, 11, 12, 13, 14 );

        play( idle );
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.SHADOW,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((ColdGurad)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    public void play( Animation anim ) {
        if (anim == die) {
            emitter().burst( ShadowParticle.UP, 4 );
        }
        super.play( anim );
    }
}
