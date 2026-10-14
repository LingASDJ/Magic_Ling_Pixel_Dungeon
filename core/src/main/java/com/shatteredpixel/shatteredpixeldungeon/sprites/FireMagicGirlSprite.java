package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireMagicDied;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class FireMagicGirlSprite extends MobSprite {

    private Animation pump;
    private Animation pumpAttack;

    private Emitter spray;
    private ArrayList<Emitter> pumpUpEmitters = new ArrayList<>();

    public FireMagicGirlSprite() {
        super();

        texture( Assets.Sprites.FRAS );

        TextureFilm frames = new TextureFilm( texture, 24, 24 );

        idle = new Animation( 10, true );
        idle.frames( frames, 0, 0, 0, 1, 1, 1,2,2,2 );

        run = new Animation( 10, true );
        run.frames( frames, 3,4,5 );

        pump = new Animation( 12, true );
        pump.frames( frames, 6,7,6,7,6,7);

        pumpAttack = new Animation ( 12, false );
        pumpAttack.frames( frames,7,8,7,8,7,8);

        attack = new Animation( 10, false );
        attack.frames( frames, 6,7,8);

        die = new Animation( 10, false );
        die.frames( frames,9,10,11 );
        zap = attack.clone();
        play(idle);

        spray = centerEmitter();
        if (spray != null) {
            spray.autoKill = false;
            spray.pour(Speck.factory(Speck.STAR),8);
            spray.on = false;
        }
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

            // Rebuild the emitter list every pump-up instead of appending. The old code
            // kept adding CellEmitters here without clearing, so repeated pump-ups
            // accumulated duplicate emitters and triggerEmitters() would burst an
            // ever-growing number of particles -> mobile frame freeze.
            pumpUpEmitters.clear();

            // ch.fieldOfView[] already encodes line-of-sight from the boss. The old
            // code re-ran two full new Ballistica raycasts per cell (O(level area)
            // allocations) to re-verify LOS that fieldOfView already gives us. On a
            // phone this was a big per-attack hitch; keep only the cheap FOV + distance
            // checks, and start the central emitter once instead of inside the loop.
            CellEmitter.get( ch.pos ).start( Speck.factory( Speck.STAR ), 0.14f, 8 );
            if (ch.fieldOfView != null) {
                for (int i = 0; i < Dungeon.level.length(); i++){
                    if (ch.fieldOfView[i]
                            && Dungeon.level.distance(i, ch.pos) <= warnDist){
                        pumpUpEmitters.add( CellEmitter.get(i) );
                    }
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
    public int blood() {
        return 0xFF000000;
    }

    public void spray(boolean on){
        spray.on = on;
    }

    @Override
    public void update() {
        super.update();
        if(spray != null){
            spray.pos(center());
            spray.visible = visible;
        }
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {
            play( zap );
            MagicMissile.boltFromChar( parent,
                    MagicMissile.FIRE,
                    this,
                    cell,
                    new Callback() {
                        @Override
                        public void call() {
                            ((FireMagicDied)ch).onZapComplete();
                        }
                    } );
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
            turnTo( ch.pos , cell );

        } else {

            super.attack( cell );

        }
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.FIRE,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((FireMagicDied)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    //纯施法动画：只播放施法动作（不发射弹道、不结算伤害），用于喷火等技能的蓄力前摇
    public void cast( int cell ) {
        turnTo( ch.pos , cell );
        play( zap );
    }

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete(anim);

        if (anim == pumpAttack) {

            triggerEmitters();

            idle();
            ch.onAttackComplete();
        } else if (anim == die) {
            spray.killAndErase();
        }
    }
}
