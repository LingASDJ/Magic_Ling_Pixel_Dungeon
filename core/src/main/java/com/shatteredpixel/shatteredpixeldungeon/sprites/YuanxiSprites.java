package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.Yuanxi;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HalomethaneFlameParticle;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class YuanxiSprites extends MobSprite {

    private Emitter teleParticles;

    public YuanxiSprites() {
        super();

        texture( Assets.Sprites.YUANXI );

        TextureFilm frames = new TextureFilm( texture, 11, 15 );

        idle = new MovieClip.Animation( 2, true );
        idle.frames( frames, 0,1 );

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 0,1 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 0,1 );

        die = new MovieClip.Animation( 2, false );
        die.frames( frames, 0,1 );

        play( idle );
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        teleParticles = emitter();
        teleParticles.autoKill = false;
        teleParticles.pour(HalomethaneFlameParticle.FACTORY, 0.05f);
        teleParticles.on = false;
    }

    @Override
    public void update() {
        super.update();
        if (teleParticles != null){
            teleParticles.pos( this );
            teleParticles.visible = visible;
        }
    }

    @Override
    public void kill() {
        super.kill();

        if (teleParticles != null) {
            teleParticles.on = false;
        }
    }

    public void teleParticles(boolean value){
        if (teleParticles != null) teleParticles.on = value;
    }

    @Override
    public synchronized void play(Animation anim, boolean force) {
        if (teleParticles != null) teleParticles.on = false;
        super.play(anim, force);
    }

    @Override
    public int blood() {
        return 0xFF80706c;
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
                        ((Yuanxi)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    private boolean died = false;

    @Override
    public void onComplete( Animation anim ) {
        if (anim == die && !died) {
            died = true;
            emitter().burst(HalomethaneFlameParticle.FACTORY, 4 );
        }
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

}

