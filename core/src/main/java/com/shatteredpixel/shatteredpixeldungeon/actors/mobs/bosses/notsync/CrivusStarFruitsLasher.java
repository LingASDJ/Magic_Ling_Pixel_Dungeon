package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RotLasherSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class CrivusStarFruitsLasher extends Mob {
    private boolean chainsUsed = false;

    public void onZapComplete(){
        for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
            if(boss instanceof CrivusStarFruits && Dungeon.level.distance(pos, boss.pos) <= 50 && Statistics.crivusfruitslevel3) {

                //最高加到半血
                if (boss.HP < boss.HT/2){

                    if (sprite.visible || boss.sprite.visible) {
                        sprite.parent.add(new Beam.GlassRayS(sprite.center(), boss.sprite.center()));
                    }

                    boss.HP = Math.min(boss.HP + 2, boss.HT/2);
                    if (boss.sprite.visible) boss.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );

                    //不符合的情况下给予3回合激素涌动
                }
                next();
            }
        }

    }

    {
        spriteClass = RotLasherSprite.class;

        HP = HT = Statistics.crivusfruitslevel2 ? 40 : 50;
        defenseSkill = 0;

        EXP = 1;

        state = WANDERING;

        HUNTING = new Hunting();
        properties.add(Char.Property.IMMOVABLE);
        properties.add(Char.Property.MINIBOSS);
    }

    @Override
    public void damage(int dmg, Object src) {
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*2+4);
        if (AntiMagic.RESISTS.contains(src.getClass()) && Statistics.crivusfruitslevel2) {
            dmg = 0;
        } else if (src instanceof Burning && !Statistics.crivusfruitslevel2) {
            Buff.affect( this, HalomethaneBurning.class ).reignite( this, 2f );
        }
        super.damage(dmg, src);
    }

    public String info(){
        StringBuilder desc = new StringBuilder(super.info());

        if(Statistics.crivusfruitslevel3){
            desc.append("\n\n").append(Messages.get(this, "xlin"));
        } else if(Statistics.crivusfruitslevel2){
            desc.append("\n\n").append(Messages.get(this, "xling"));
        }

        return desc.toString();
    }

    {
        immunities.add( Paralysis.class );
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( ToxicGas.class );
        immunities.add( CrivusFruits.DiedBlobs.class );
        immunities.add( Terror.class );
        immunities.add( Dread.class );
        immunities.add( Vertigo.class );
    }

    private boolean chain(int target){
        if (chainsUsed || enemy.properties().contains(Property.IMMOVABLE))
            return false;

        Ballistica chain = new Ballistica(pos, target, Ballistica.STOP_TARGET);

        if (chain.collisionPos != enemy.pos
                || chain.path.size() < 2
                || Dungeon.level.pit[chain.path.get(1)])
            return false;
        else {
            int newPos = -1;
            for (int i : chain.subPath(1, chain.dist)){
                if (!Dungeon.level.solid[i] && Actor.findChar(i) == null){
                    newPos = i;
                    break;
                }
            }

            if (newPos == -1){
                return false;
            } else {
                final int newPosFinal = newPos;
                this.target = newPos;

                if (sprite.visible || enemy.sprite.visible) {
                    yell(Messages.get(this, "scorpion"));
                    new Item().throwSound();
                    Sample.INSTANCE.play(Assets.Sounds.CHAINS);
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                        if (mob instanceof CrivusStarFruitsLasher){
                            Buff.affect(mob, Blindness.class, 3f);
                        }
                    }
                    sprite.parent.add(new Chains(sprite.center(),
                            enemy.sprite.destinationCenter(),
                            Effects.Type.GLASSCHAIN,
                            new Callback() {
                                public void call() {
                                    Actor.add(new Pushing(enemy, enemy.pos, newPosFinal, new Callback() {
                                        public void call() {
                                            pullEnemy(enemy, newPosFinal);
                                        }
                                    }));
                                    next();
                                }
                            }));
                } else {
                    pullEnemy(enemy, newPos);
                }
            }
            chainsUsed = true;
        }
        return true;
    }

    private void pullEnemy( Char enemy, int pullPos ){
        enemy.pos = pullPos;
        enemy.sprite.place(pullPos);
        Dungeon.level.occupyCell(enemy);
        Cripple.prolong(enemy, Cripple.class, 4f);
        if (enemy == Dungeon.hero) {
            Dungeon.hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }



    private int chains;
    private final String CHAINSUSED = "chainsused";

    private final String CHAINXUSED = "chainxused";
    @Override
    public boolean isInvulnerable(Class effect) {
        return Statistics.crivusfruitslevel2 && !Statistics.crivusfruitslevel3;
    }
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHAINSUSED, chainsUsed);
        bundle.put(CHAINXUSED,chains);
    }
    @Override
    protected boolean act() {
        GameScene.add(Blob.seed(pos, Statistics.crivusfruitslevel2 ? 10 : 0, CorrosiveGas.class));
        GameScene.add(Blob.seed(pos, Statistics.crivusfruitslevel2 ? 0 : 10, ToxicGas.class));

        if (Statistics.crivusfruitslevel3) {
            onZapComplete();
            spend(12f);
        }

        if(hero.buff(CrivusStarFruits.DiedDamager.class) == null){
            Buff.affect(this,CrivusStarFruits.DiedDamager.class);
        }

        return super.act();
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        GameScene.add(Blob.seed(pos, Statistics.crivusfruitslevel2 ? 10 : 0, CrivusFruits.DiedBlobs.class));
        return super.defenseProc(enemy, damage);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        chainsUsed = bundle.getBoolean(CHAINSUSED);
        chains = bundle.getInt(CHAINXUSED);
    }

    private class Hunting extends Mob.Hunting{
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemySeen = enemyInFOV;

            if (!chainsUsed
                    && enemyInFOV
                    && !isCharmedBy( enemy )
                    && !canAttack( enemy )
                    && Dungeon.level.distance( pos, enemy.pos ) < 6
                    && chain(enemy.pos)){
                return !(sprite.visible || enemy.sprite.visible);
            } else {
                chains++;
                if(chains>=60 && chainsUsed){
                    chainsUsed = false;
                    chains = 0;
                }
                return super.act( enemyInFOV, justAlerted );
            }

        }
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    public int damageRoll() {
        return Char.combatRoll( 1, 7 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 6;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    {
        immunities.add( CorrosiveGas.class );
        immunities.add( CrivusFruits.DiedBlobs.class );
    }

}

