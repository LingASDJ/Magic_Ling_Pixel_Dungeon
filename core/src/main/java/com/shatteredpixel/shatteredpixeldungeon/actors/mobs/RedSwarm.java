/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.BloodsSwarm;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedSwarmSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class RedSwarm extends Mob implements Callback {

    private static final float TIME_TO_ZAP	= 3f;

    {
        spriteClass = RedSwarmSprite.class;

        HP = HT = 32;
        defenseSkill = 4;

        EXP = 15;

        flying = true;

        maxLvl = 18;

        loot = Generator.Category.POTION;
        lootChance = 0.1f;

        WANDERING = new Wandering();

        properties.add(Property.UNDEAD);
    }

    public static final float SPLIT_DELAY	= 1f;

    int generation	= 0;

    public boolean spawnBloods = false;

    public RedSwarm split() {
        RedSwarm clone = new RedSwarm ();
        clone.generation = generation + 1;
        clone.EXP = 0;
        if (buff( Poison.class ) != null) {
            Buff.affect( clone, Poison.class ).set(2);
        }
        for (Buff b : buffs(AllyBuff.class)){
            Buff.affect( clone, b.getClass());
        }
        for (Buff b : buffs(ChampionEnemy.class)){
            Buff.affect( clone, b.getClass());
        }

        return clone;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 12 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 6;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(1, 4);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
    }

    //used so resistances can differentiate between melee and magical attacks
    public static class DarkBolt{}

    private void zap() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {
            if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                Buff.affect(enemy, Burning.class).reignite(enemy);
                Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
            }

            int dmg = Random.NormalIntRange( 6, 7 );
            enemy.damage( dmg, new DarkBolt() );

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "fire_kill") );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public Item createLoot(){

        // 1/6 chance for healing, scaling to 0 over 8 drops
        if (Random.Int(2) == 0 && Random.Int(8) > Dungeon.LimitedDrops.WARLOCK_HP.count ){
            Dungeon.LimitedDrops.WARLOCK_HP.drop();
            return new PotionOfHealing();
        } else {
            Item i = Generator.random(Generator.Category.POTION);
            int healingTried = 0;
            while (i instanceof PotionOfHealing){
                healingTried++;
                i = Generator.random(Generator.Category.POTION);
            }

            //return the attempted healing potion drops to the pool
            if (healingTried > 0){
                for (int j = 0; j < Generator.Category.POTION.classes.length; j++){
                    if (Generator.Category.POTION.classes[j] == PotionOfHealing.class){
                        Generator.Category.POTION.probs[j] += healingTried;
                    }
                }
            }

            return i;
        }

    }

    private static final String GENERATION	= "generation";
    private static final String SPAWN_BLOODS = "spawnBloods";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( GENERATION, generation );
        bundle.put( SPAWN_BLOODS, spawnBloods );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        generation = bundle.getInt( GENERATION );
        if (generation > 0) EXP = 0;
        spawnBloods = bundle.getBoolean( SPAWN_BLOODS );
    }

    public class Wandering extends Mob.Wandering {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            if (enemyInFOV) {
                enemySeen = true;
                alerted = true;
                state = HUNTING;
                target = enemy.pos;
            } else if(spawnBloods) {
                enemySeen = false;

                int oldPos = pos;
                int minDistance = 5;
                int nearestPos = -1;

                // 遍历所有怪物，寻找最近的BloodsSwarm
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob instanceof BloodsSwarm) {
                        int distance = Dungeon.level.distance(pos, mob.pos);
                        if (distance < minDistance) {
                            minDistance = distance;
                            nearestPos = mob.pos;
                        }
                    }
                }

                if (nearestPos != -1) {
                    target = nearestPos;
                }

                if (getCloser(target)) {
                    spend(1 / speed());
                    return moveSprite(oldPos, pos);
                } else {
                    spend(TICK);
                }
            } else {
                return super.act(false, justAlerted);
            }

            return true;
        }
    }

}
