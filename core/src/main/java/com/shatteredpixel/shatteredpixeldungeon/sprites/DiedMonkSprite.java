package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DiedMonkSprite extends MobSprite {

    private Animation kick;
    private Animation pump;
    private Animation pumpAttack;

    private Emitter spray;
    private ArrayList<Emitter> pumpUpEmitters = new ArrayList<>();

    public DiedMonkSprite() {
        super();

        texture( Assets.Sprites.MONKDIED );

        TextureFilm frames = new TextureFilm( texture, 15, 14 );

        idle = new Animation( 6, true );
        idle.frames( frames, 1, 0, 1, 2 );

        run = new Animation( 15, true );
        run.frames( frames, 11, 12, 13, 14, 15, 16 );

        attack = new Animation( 12, false );
        attack.frames( frames, 3, 4, 3, 4 );

        pump = new Animation( 20, true );
        pump.frames( frames, 10,9,8,7,6,5,4 );

        pumpAttack = new Animation ( 20, false );
        pumpAttack.frames( frames, 4, 3, 2, 1, 0, 7);

        kick = new Animation( 10, false );
        kick.frames( frames, 5, 6, 5 );

        die = new Animation( 15, false );
        die.frames( frames, 1, 7, 8, 8, 9, 10 );

        spray = centerEmitter();
        spray.autoKill = false;
        spray.pour( DiedParticle.FACTORY, 0.04f );
        spray.on = false;

        play( idle );
    }

    public void zap( int pos ) {

        Char enemy = Actor.findChar(pos);

        //shoot lightning from eye, not sprite center.
        PointF origin = center();
        if (flipHorizontal){
            origin.y -= 6*scale.y;
            origin.x -= 1*scale.x;
        } else {
            origin.y -= 8*scale.y;
            origin.x += 1*scale.x;
        }
        if (enemy != null) {
            parent.add(new Lightning(origin, enemy.sprite.destinationCenter(), (Callback) ch));
        } else {
            parent.add(new Lightning(origin, pos, (Callback) ch));
        }
        Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );

        turnTo( ch.pos, pos );
        flash();
        play( zap );
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        if (ch.HP*2 <= ch.HT)
            spray(true);
    }

    public void pumpUp( int warnDist ) {
        if (warnDist == 0){
            clearEmitters();
        } else {
            play(pump);
            Sample.INSTANCE.play( Assets.Sounds.CHARGEUP, 1f, warnDist == 1 ? 0.8f : 1f );
            PathFinder.buildDistanceMap(ch.pos, BArray.not(Dungeon.level.solid, null), 2);
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] <= warnDist) {
                    Emitter e = CellEmitter.get(i);
                    e.pour(DiedParticle.FACTORY, 0.04f);
                    pumpUpEmitters.add(e);
                }
            }
        }
    }

    public void clearEmitters(){
        for (Emitter e : pumpUpEmitters){
            e.on = false;
        }
        pumpUpEmitters.clear();
    }

    public void triggerEmitters(){
        for (Emitter e : pumpUpEmitters){
            e.burst(ElmoParticle.FACTORY, 10);
        }
        Sample.INSTANCE.play( Assets.Sounds.BURNING );
        pumpUpEmitters.clear();
    }

    public void pumpAttack() { play(pumpAttack); }

    @Override
    public void play(Animation anim) {
        if (anim != pump && anim != pumpAttack){
            clearEmitters();
        }
        super.play(anim);
    }

    @Override
    public void update() {
        super.update();
        spray.pos(center());
        spray.visible = visible;
    }

    @Override
    public int blood() {
        return 0xFF000000;
    }

    public void spray(boolean on){
        spray.on = on;
    }

    public static class DiedParticle extends PixelParticle.Shrinking {

        public static final Emitter.Factory FACTORY = new Emitter.Factory() {
            @Override
            public void emit( Emitter emitter, int index, float x, float y ) {
                ((DiedMonkSprite.DiedParticle)emitter.recycle( DiedMonkSprite.DiedParticle.class )).reset( x, y );
            }
        };

        public DiedParticle() {
            super();

            color( 0xff0000 );
            lifespan = 0.3f;

            acc.set( 0, +50 );
        }

        public void reset( float x, float y ) {
            revive();

            this.x = x;
            this.y = y;

            left = lifespan;

            size = 4;
            speed.polar( -Random.Float( PointF.PI ), Random.Float( 32, 48 ) );
        }

        @Override
        public void update() {
            super.update();
            float p = left / lifespan;
            am = p > 0.5f ? (1 - p) * 2f : 1;
        }
    }

    @Override
    public void attack( int cell ) {
        super.attack( cell );
        if (Random.Float() < 0.5f) {
            play( kick );
        }
    }

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete( anim == kick ? attack : anim );
            super.onComplete(anim);

            if (anim == pumpAttack) {

                triggerEmitters();

                idle();
                ch.onAttackComplete();
            } else if (anim == die) {
                spray.killAndErase();
            } else if (anim == zap) {
                idle();
            }
    }
}

