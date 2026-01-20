package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class RedNecromancerSprite_EX extends MobSprite {

    private Animation charging;
    private Emitter summoningBones;

    public RedNecromancerSprite_EX(){
        super();

        texture( Assets.Sprites.NECROREDEX );
        TextureFilm film = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 1, true );
        idle.frames( film, 0, 0, 0, 1, 0, 0, 0, 0, 1 );

        run = new Animation( 8, true );
        run.frames( film, 0, 0, 0, 2, 3, 4 );

        zap = new Animation( 10, false );
        zap.frames( film, 5, 6, 7, 8 );

        charging = new Animation( 5, true );
        charging.frames( film, 7, 8 );

        die = new Animation( 10, false );
        die.frames( film, 9, 10, 11, 12 );

        attack = zap.clone();

        idle();
    }

    //@Override
    //public void link(Char ch) {
    //    super.link(ch);
    //    if (ch instanceof RedNecromancer && ((RedNecromancer) ch).summoning){
    //        zap(((RedNecromancer) ch).summoningPos);
    //    }
    //}
//
    //@Override
    //public void update() {
    //    super.update();
    //    if (summoningBones != null && ((RedNecromancer) ch).summoningPos != -1){
    //        summoningBones.visible = Dungeon.level.heroFOV[((RedNecromancer) ch).summoningPos];
    //    }
    //}

    @Override
    public void die() {
        super.die();
        if (summoningBones != null){
            summoningBones.on = false;
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (summoningBones != null){
            summoningBones.killAndErase();
        }
    }

    public void cancelSummoning(){
        if (summoningBones != null){
            summoningBones.on = false;
        }
    }

    public void finishSummoning(){
        if (summoningBones.visible) {
            Sample.INSTANCE.play(Assets.Sounds.BONES);
            summoningBones.burst(Speck.factory(Speck.RATTLE), 5);
        } else {
            summoningBones.on = false;
        }
        idle();
    }

    public void charge(){
        play(charging);
    }

   //Override
   //ublic void zap(int cell) {
   //   super.zap(cell);
   //   if (ch instanceof RedNecromancer && ((RedNecromancer) ch).summoning){
   //       if (summoningBones != null){
   //           summoningBones.on = false;
   //       }
   //       summoningBones = CellEmitter.get(((RedNecromancer) ch).summoningPos);
   //       summoningBones.pour(Speck.factory(Speck.RATTLE), 0.2f);
   //       summoningBones.visible = Dungeon.level.heroFOV[((RedNecromancer) ch).summoningPos];
   //       if (visible || summoningBones.visible ) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP, 1f, 0.8f );
   //   }
   //

   //Override
   //ublic void onComplete(Animation anim) {
   //   super.onComplete(anim);
   //   if (anim == zap){
   //       if (ch instanceof RedNecromancer){
   //           if (((RedNecromancer) ch).summoning){
   //               charge();
   //           } else {
   //               ((RedNecromancer)ch).onZapComplete();
   //               idle();
   //           }
   //       } else {
   //           idle();
   //       }
   //   }
   //
}

