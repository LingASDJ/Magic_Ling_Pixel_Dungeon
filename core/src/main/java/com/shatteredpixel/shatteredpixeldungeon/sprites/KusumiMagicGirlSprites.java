package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.KusumiMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FrostFlameParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class KusumiMagicGirlSprites extends CharSprite {

    private Emitter teleParticles;

    public KusumiMagicGirlSprites() {

        texture( Assets.Sprites.KUSUMI_MG);

        TextureFilm ren = new TextureFilm(this.texture, 16, 18);

        idle = new Animation(12, true);
        idle.frames(ren,  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1);

        run = new Animation(12, true);
        run.frames(ren, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1);

        die = new Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        teleParticles = emitter();
        teleParticles.autoKill = false;
        teleParticles.pour(FrostFlameParticle.FACTORY, 0.05f);
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
                        ((KusumiMagicGirl)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    private boolean died = false;

    @Override
    public void onComplete( Animation anim ) {
        if (anim == die && !died) {
            died = true;
            emitter().burst(FrostFlameParticle.FACTORY, 4 );
        }
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

}
