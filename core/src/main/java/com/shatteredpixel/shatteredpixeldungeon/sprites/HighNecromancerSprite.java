package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.HighNecromancer;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class HighNecromancerSprite extends MobSprite {

    private Animation charging;
    private Emitter summoningBones;

    public HighNecromancerSprite(){
        super();

        texture( Assets.Sprites.NECRO_TOMB );
        TextureFilm film = new TextureFilm( texture, 23, 23 );

        idle = new Animation( 5, true );
        idle.frames( film, 0,1,2,3,4,5 );

        run = new Animation( 11, true );
        run.frames( film, 6,7,8,9,10,11,12,13 );

        attack = new Animation( 11, false );
        attack.frames( film, 19,20,21,22,23 );

        zap = new Animation( 11, false );
        zap.frames( film, 14,15,16,17,18 );

        charging= new Animation( 11, true);
        charging.frames( film, 14,15,16,17,18 );

        die = new Animation( 11, false );
        die.frames( film, 24,25,26,27,28 );
        
        idle();
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        if (ch instanceof HighNecromancer && ((HighNecromancer) ch).summoning){
            zap(((HighNecromancer) ch).summoningPos);
        }
    }

    @Override
    public void update() {
        super.update();
        if (summoningBones != null && ((HighNecromancer) ch).summoningPos != -1){
            summoningBones.visible = Dungeon.level.heroFOV[((HighNecromancer) ch).summoningPos];
        }
    }

    @Override
    public void die() {
        super.die();
        if (summoningBones != null){
            summoningBones.on = false;
            summoningBones = null;
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (summoningBones != null){
            summoningBones.on = false;
            summoningBones = null;
        }
    }

    public void cancelSummoning(){
        if (summoningBones != null){
            summoningBones.on = false;
            summoningBones = null;
        }
    }

    public void finishSummoning(){
        if (summoningBones != null) {
            if (summoningBones.visible) {
                Sample.INSTANCE.play(Assets.Sounds.BLAST);
                summoningBones.burst(Speck.factory(Speck.STAR), 5);
            } else {
                summoningBones.on = false;
            }
            summoningBones = null;
        }
        idle();
    }

    public void charge(){
        play(charging);
    }

    @Override
    public void zap(int cell) {
        super.zap(cell);
        if (ch instanceof HighNecromancer && ((HighNecromancer) ch).summoning){
            if (summoningBones != null){
                summoningBones.on = false;
            }
            summoningBones = CellEmitter.get(((HighNecromancer) ch).summoningPos);
            summoningBones.pour(Speck.factory(Speck.BLUE_LIGHT), 0.2f);
            summoningBones.visible = Dungeon.level.heroFOV[((HighNecromancer) ch).summoningPos];
            if (visible || summoningBones.visible ) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP, 1f, 0.8f );
        }
    }

    @Override
    public void onComplete(Animation anim) {
        super.onComplete(anim);
        if (anim == zap){
            if (ch instanceof HighNecromancer){
                if (((HighNecromancer) ch).summoning){
                    charge();
                } else {
                    ((HighNecromancer)ch).onZapComplete();
                    idle();
                }
            } else {
                idle();
            }
        }
    }
}
