package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap.curse;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BlackHost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewBlackHost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.MagicFireParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class CursedDiedBlos extends Blob {

    @Override
    protected void evolve() {

        boolean[] flamable = Dungeon.level.flamable;
        int cell;
        int fire;

        Freezing freeze = (Freezing)Dungeon.level.blobs.get( Freezing.class );

        boolean observe = false;

        for (int i = area.left-1; i <= area.right; i++) {
            for (int j = area.top-1; j <= area.bottom; j++) {
                cell = i + j*Dungeon.level.width();
                if (cur[cell] > 0) {

                    if (freeze != null && freeze.volume > 0 && freeze.cur[cell] > 0){
                        freeze.clear(cell);
                        off[cell] = cur[cell] = 0;
                        continue;
                    }

                    cursed( cell );

                    fire = cur[cell] - 1;
                    if (fire <= 0 && flamable[cell]) {

                        Dungeon.level.destroy( cell );

                        observe = true;
                        GameScene.updateMap( cell );

                    }

                } else if (freeze == null || freeze.volume <= 0 || freeze.cur[cell] <= 0) {

                    if (flamable[cell]
                            && (cur[cell-1] > 0
                            || cur[cell+1] > 0
                            || cur[cell-Dungeon.level.width()] > 0
                            || cur[cell+Dungeon.level.width()] > 0)) {
                        fire = 4;
                        cursed( cell );
                        area.union(i, j);
                    } else {
                        fire = 0;
                    }

                } else {
                    fire = 0;
                }

                volume += (off[cell] = fire);
            }
        }

        if (observe) {
            Dungeon.observe();
        }
    }

    public static void cursed( int pos ) {
        Char ch = Actor.findChar( pos );

        if(ch != null){
            if(ch == hero){
                if(Random.Int(100) <= 10){
                    if (Dungeon.level.heroFOV[ pos ]) {
                        CellEmitter.get(pos).burst(ShadowParticle.UP, 5);
                        Sample.INSTANCE.play(Assets.Sounds.CURSED);
                    }

                    Heap heap = Dungeon.level.heaps.get( pos );
                    if (heap != null){
                        for (Item item : heap.items){
                            if (item.isUpgradable() && !(item instanceof MissileWeapon))
                                CursingTrap.curse( (Hero) ch );
                        }
                    }



                    if (hero.pos == pos && !hero.flying){
                        curse((Hero) ch);
                        if(Random.Int(100) <= 10 && hero.buff(Weakness.class) == null) {
                            Buff.prolong( ch, Weakness.class, Weakness.DURATION/5 );
                        }
                    }
                }
            } else {

                    if (!(ch instanceof NewBlackHost || ch instanceof BlackHost)) {
                        if(Random.Int(100) <= 10 && ch.buff(CrivusFruits.CFBarrior.class) == null) {
                            Buff.affect(ch, CrivusFruits.CFBarrior.class).setShield(Random.Int(6,21));
                        }
                    }

            }
        }


    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.pour( MagicFireParticle.FACTORY, 0.03f );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}

