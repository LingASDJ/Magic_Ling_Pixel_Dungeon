package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.SakaFishBoss;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class SakaFishBossSprites extends MobSprite {
    private Animation leap;

    private int zapPos;
    private Animation activeIdle;

    private Animation charging;
    private Emitter chargeParticles;
    public SakaFishBossSprites() {
        super();

        texture( Assets.Sprites.SKFS );

        TextureFilm frames = new TextureFilm( texture, 29, 12 );

        idle = new Animation( 4, true );
        idle.frames( frames, 14,15,16,17 );

        activeIdle = new Animation( 4, true );
        activeIdle.frames( frames, 18,19,20,21);

        run = new Animation( 10, true );
        run.frames( frames, 2,3 );

        attack = new Animation( 15, false );
        attack.frames( frames, 4,5,6 );
        zap = attack.clone();

        die = new Animation( 10, false );
        die.frames( frames, 1 );

        leap = new Animation( 2, true );
        leap.frames( frames, 52,53,54,55 );

        charging = new Animation( 20, true);
        charging.frames( frames, 28, 29,30,31,32,33,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31,29,30,31 );

        play( idle );

    }

    //哦 创死你
    public void leapPrep( int cell ){
        turnTo( ch.pos, cell );
        play( leap );
    }

    public void activate(){
        idle = activeIdle.clone();
        idle();
    }

    @Override
    public void zap( int pos ) {
        zapPos = pos;
        super.zap( pos );
    }

    //超级激光
    public void charge( int pos ){
        turnTo(ch.pos, pos);
        play(charging);
        if (visible) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        chargeParticles = centerEmitter();
        chargeParticles.autoKill = false;
        chargeParticles.pour(MagicMissile.MagicParticle.ATTRACTING, 0.05f);
        chargeParticles.on = false;

        if (((SakaFishBoss)ch).beamCharged) play(charging);

        if (((SakaFishBoss) ch).state != ((SakaFishBoss) ch).SLEEPING){
            activate();
        }
        renderShadow = false;
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

    @Override
    public void play(Animation anim) {
        if (chargeParticles != null) chargeParticles.on = anim == charging;
        super.play(anim);
    }

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete( anim );

        if (anim == zap) {
            idle();
            if (Actor.findChar(zapPos) != null){
                parent.add(new Beam.DeathRayS(center(), Actor.findChar(zapPos).sprite.center()));
            } else {
                parent.add(new Beam.DeathRayS(center(), DungeonTilemap.raisedTileCenterToWorld(zapPos)));
            }
            ((SakaFishBoss)ch).deathGaze();
            ch.next();
        } else if (anim == die){
            chargeParticles.killAndErase();
        }
    }



}

