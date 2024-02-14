package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
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

    {
        spriteClass = RotLasherSprite.class;

        HP = HT = Statistics.crivusfruitslevel2 ? 30 : 15;
        defenseSkill = 0;

        EXP = 1;

        state = WANDERING = new Waiting();
        HUNTING = new Hunting();
        properties.add(Char.Property.IMMOVABLE);
        properties.add(Char.Property.MINIBOSS);
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

        Ballistica chain = new Ballistica(pos, target, Ballistica.PROJECTILE);

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
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHAINSUSED, chainsUsed);
        bundle.put(CHAINXUSED,chains);
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
        return Random.NormalIntRange( 1, 7 );
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
        immunities.add( ToxicGas.class );
    }

    private class Waiting extends Mob.Wandering{}
}

