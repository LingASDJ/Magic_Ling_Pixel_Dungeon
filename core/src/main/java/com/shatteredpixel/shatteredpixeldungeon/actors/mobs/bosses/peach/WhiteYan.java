package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.peach;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.MagicFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhiteYanBossSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class WhiteYan extends Boss {

    private int fireBanningCooldown;
    private float summonCD = 50f;

    {
        initStatus(100);
        initProperty();
        spriteClass = WhiteYanBossSprite.class;
        initBaseStatus(28, 36, 20, 10, 1000, 5, 8);
        immunities.add(Blob.class);
        immunities.add(Buff.class);
        HUNTING = new Hunting();
    }

    @Override
    public boolean act() {

        if(Random.Int(100)>=60 && fireBanningCooldown == 25) {
            if (enemy != null) {
                Banning(enemy.pos);
            }
            Banning(pos);
            fireBanningCooldown = 0;
        } else {
            fireBanningCooldown++;
        }


        float count = 0;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof SoulCrystal) {
                count += mob.spawningWeight();
            }
        }

        //魂烈晶
        if(summonCD<0f && count<3){
            summonCD += Math.max(60f - 2f, 40f);

            List<Integer> neighbours = new ArrayList<>();
            for (int direction : PathFinder.NEIGHBOURS8) {
                neighbours.add(direction);
            }

            Random.shuffle(neighbours);

            for (int i : neighbours) {
                if (!Dungeon.level.solid[pos + i]) {
                    summonCaster(pos + i);
                    break;
                }
            }
        }
        summonCD -= 1/speed();

        return super.act();
    }

    public static class DiedSoulBolt{}

    private void zap() {
        spend( 1f );

        if(enemy == null){
            return;
        }

        if (hit( this, enemy, true )) {
            int dmg = damageRoll();
            enemy.damage( dmg, new DiedSoulBolt() );
            int oppositeAdjacent = enemy.pos + (enemy.pos - pos);
            Ballistica trajectory = new Ballistica(enemy.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
            WandOfBlastWave.throwChar(enemy, trajectory, 1, false, false, this);
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }

        if (enemy == Dungeon.hero && !enemy.isAlive()) {
            Dungeon.fail( getClass() );
            GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if(Dungeon.level.distance(pos,target)>3)
            return false;
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
    }

    @Override
    protected boolean getCloser(int target) {
        if (state == HUNTING) {
            if(Dungeon.level.distance(pos,target)>3)
                return super.getCloser( target );
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser(target);
        }
    }

    //徘徊
    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV;
            if (Dungeon.level.distance(pos,target)>3) {
                WhiteYan.this.state = WANDERING;
            }
            return super.act( enemyInFOV, justAlerted );
        }

    }

    protected void fallingRockVisual(int pos){
        Camera.main.shake(0.4f, 2f);
        CellEmitter.get( pos - Dungeon.level.width() ).start(Speck.factory(Speck.STAR), 0.08f, 10);
    }

    protected void summonCaster(int pos){
        if(pos != -1){
            SoulCrystal caster;
            caster = new SoulCrystal();
            caster.pos = pos;
            GameScene.add(caster, Random.Float(2f, 8f));
            Dungeon.level.mobs.add(caster);
            fallingRockVisual(pos);
            Dungeon.level.passable[pos] = false;
        }
    }

    //宴-火
    public static class BanningFire extends Blob {

        @Override
        protected void evolve() {

            //boolean[] flamable = Dungeon.level.flamable;
            int cell;
            int fire = 0;

            Freezing freeze = (Freezing)Dungeon.level.blobs.get( Freezing.class );
            //燃烧效果粒子总和
            Level l = Dungeon.level;

            boolean[] flamable = Dungeon.level.flamable;
            for (int i = area.left-1; i <= area.right; i++) {
                for (int j = area.top-1; j <= area.bottom; j++) {
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0) {

                        if (l.water[cell]){
                            cur[cell] = 0;
                        }

                        if (freeze != null && freeze.volume > 0 && freeze.cur[cell] > 0){
                            freeze.clear(cell);
                            off[cell] = cur[cell] = 0;
                            continue;
                        }

                        burn( cell );

                        fire = cur[cell] - 1;


                    } else if (freeze == null || freeze.volume <= 0 || freeze.cur[cell] <= 0) {

                        if (flamable[cell]
                                && (cur[cell-1] > 0
                                || cur[cell+1] > 0
                                || cur[cell-Dungeon.level.width()] > 0
                                || cur[cell+Dungeon.level.width()] > 0)) {
                            fire = 4;
                            burn( cell );
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

            Dungeon.observe();

        }

        public static void burn( int pos ) {
            Char ch = Actor.findChar( pos );
            if (ch != null && !ch.isImmune(BanningFire.class) && !(ch instanceof SoulCrystal || ch instanceof WhiteYan)) {
                Buff.affect( ch, Burning.class ).reignite( ch,5f );
                Buff.prolong( ch, Blindness.class, 2f );
            }

            Heap heap = Dungeon.level.heaps.get( pos );
            if (heap != null) {
                heap.burn();
            }

            Plant plant = Dungeon.level.plants.get( pos );
            if (plant != null){
                plant.wither();
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use( emitter );
            emitter.pour( MagicFlameParticle.FACTORY, 0.03f );
        }

        @Override
        public String tileDesc() {
            return Messages.get(this, "desc");
        }
    }

    private void Banning(int targetingPos){
        for (int i : PathFinder.NEIGHBOURS8) {
            if (!Dungeon.level.solid[targetingPos + i]) {
                CellEmitter.get(targetingPos + i).burst(ElmoParticle.FACTORY, 5);
                GameScene.add(Blob.seed(targetingPos + i, 2, BanningFire.class));
            } else {
                Char target = Actor.findChar(targetingPos + i);
                if (target != null && target != this) {
                    Buff.affect(target, Burning.class).reignite(target);
                }
            }
        }
    }

    private static final String BANNINGFIRE = "banningfire";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BANNINGFIRE, fireBanningCooldown);
        bundle.put("summonCD", summonCD);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        fireBanningCooldown = bundle.getInt(BANNINGFIRE);
        summonCD = bundle.getFloat("summonCD");
    }
}
