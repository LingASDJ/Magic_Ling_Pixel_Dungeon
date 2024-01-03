package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Cerberus;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class CerberusSprite extends MobSprite {

    private Animation skills;

    public  CerberusSprite() {
        super();

        texture( Assets.Sprites.CSBR );

        TextureFilm frames = new TextureFilm( texture, 32, 32);

        idle = new MovieClip.Animation( 10, true );
        idle.frames( frames, 0, 1, 2, 3, 4 ,6, 7,8 );

        run = new MovieClip.Animation( 14, true );
        run.frames( frames, 9,10,11,12,13,14,15,16,17 );

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 18,19,20,21 );

        skills = new MovieClip.Animation( 12, false );
        skills.frames( frames, 22, 23, 24, 25, 26 );

        die = new MovieClip.Animation( 14, false );
        die.frames( frames, 27, 28, 29, 30, 31, 32, 33, 34, 35 );

        zap = attack.clone();

        play( idle );
    }

    public void attack(int cell) {
        if (!Dungeon.level.adjacent(cell, this.ch.pos)) {
            MagicMissile.boltFromChar( parent,
                    MagicMissile.SHAMAN_RED,
                    this,
                    cell,
                    new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );
            this.play(zap);
            this.turnTo(this.ch.pos, cell);
        } else {
            super.attack(cell);
        }

    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( skills );

        MagicMissile.boltFromChar( parent,
                MagicMissile.ELMO,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Cerberus)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    //Skills Animation
    public void Skills( int cell ){
        turnTo( ch.pos, cell );
        play( skills );
    }
    private Emitter chargeParticles;
    @Override
    public void link(Char ch) {
        super.link(ch);

        chargeParticles = centerEmitter();
        chargeParticles.autoKill = false;
        chargeParticles.pour(MagicMissile.MagicParticle.ATTRACTING, 0.05f);
        chargeParticles.on = false;

        if (((Cerberus)ch).beamCharged) play(attack);
    }

    @Override
    public void update() {
        super.update();
        if (chargeParticles != null){
            chargeParticles.pos( center() );
            chargeParticles.visible = visible;
        }
    }

    @Override
    public void die() {
        super.die();
        if (chargeParticles != null){
            chargeParticles.on = false;
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (chargeParticles != null){
            chargeParticles.killAndErase();
        }
    }

    public void charge( int pos ){
        turnTo(ch.pos, pos);
        if (visible) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
    }

}

