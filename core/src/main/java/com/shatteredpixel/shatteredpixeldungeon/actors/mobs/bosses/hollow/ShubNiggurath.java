package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.DamageBuff.ScaryDamageBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.Immunities.ScaryImmunitiesBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShubNiggurathSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShubNiggurath extends Boss {

    public int notDamage = 0;

    public boolean notFirst = false;

    public int maxReHeal = 0;

    private static final int MAX_SPLIT_COUNT = 18;

    private static final int MAX_REHEAL_COUNT = 5;

    {
        initBaseStatus(0, 0, 0, 0, 3200, 0, 0);
        initStatus(20);
        spriteClass = ShubNiggurathSprite.class;

        properties.add(Property.UNKNOWN);
        properties.add(Property.LARGE);
        properties.add(Property.MINIBOSS);
        noDropIceCoin = true;
    }
    int generation	= 0;


    private static final float SPLIT_DELAY	= 1f;

    @Override
    public int defenseProc(Char enemy, int damage ) {

        if (HP >= damage + 2) {
            ArrayList<Integer> candidates = new ArrayList<>();

            int[] neighbours = {pos + 2, pos - 2, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
            for (int n : neighbours) {
                if (!Dungeon.level.solid[n]
                        && Actor.findChar( n ) == null
                        && (Dungeon.level.passable[n] || Dungeon.level.avoid[n])
                        && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[n])) {
                    candidates.add( n );
                }
            }

            if (!candidates.isEmpty() && !hasTooManyShubs()) {

                int currentSplitCount = 0;
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                    if (mob instanceof ShubNiggurath) {
                        currentSplitCount++;
                    }
                }

                if (currentSplitCount < MAX_SPLIT_COUNT) {
                    ShubNiggurath clone = split();
                    clone.notFirst = true;
                    clone.state = clone.HUNTING;
                    GameScene.add( clone, SPLIT_DELAY ); //we add before assigning HP due to ascension

                    clone.HP = (HP - damage) / 2;
                    Dungeon.level.randomDestination(clone);
                    clone.pos = Dungeon.level.randomDestination(clone);
                    Actor.add( new Pushing( clone, pos, clone.pos ) );

                    Dungeon.level.occupyCell(clone);

                    HP -= clone.HP;
                }
            }
        }

        return super.defenseProc(enemy, damage);
    }

    @Override
    public boolean isAlive() {
        if(getClass() == ShubNiggurath.class && !notFirst) {

            boolean hasClone = false;
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof ShubNiggurathClone ||
                        (mob instanceof ShubNiggurath && ((ShubNiggurath) mob).notFirst)) {
                    hasClone = true;
                    break;
                }
            }

            if (HP <= 0) {
                if (!hasClone) {
                    return super.isAlive();
                }
                if (maxReHeal < MAX_REHEAL_COUNT) {
                    HP = 1000;
                    maxReHeal++;
                    Buff.prolong(hero, MindVision.class, 50000);
                } else {
                    return super.isAlive();
                }
                return true;
            }
        }
        return super.isAlive();
    }

    private ShubNiggurath split() {
        ShubNiggurath clone = new ShubNiggurath();
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
    public void damage(int dmg, Object src, DamageType type) {
        super.damage(dmg, src, type);
        BossHealthBar.assignBoss(this);
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 1;
            lock.addTime(dmg*multiple);
        }
        notDamage = 0;
    }

    private boolean tooManyShubs = false;

    public boolean hasTooManyShubs() {
        return tooManyShubs;
    }

    @Override
    protected boolean act() {
        alerted = false;
        state = PASSIVE;

        if(!notFirst){
            initProperty();
            Buff.affect(this, ChampionEnemy.Bomber.class);
        } else {
            Buff.detach(this, ChampionEnemy.Bomber.class);
        }

        int shubCount = 0;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof ShubNiggurath) {
                shubCount++;
            }
        }

        // 检查是否已经达到最大分裂次数
        tooManyShubs = shubCount >= MAX_SPLIT_COUNT;

        if(buff(YogSoul.AttackDamageMagic.class)!=null && Dungeon.level.distance(pos, hero.pos) <= 7 && !hasTooManyShubs()){
            if (enemy != null && enemy == hero && enemySeen) {
                boolean isNyzAlive = false;
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                    if (mob instanceof Nyarlathotep) {
                        isNyzAlive = true;
                        break;
                    }
                }
                boolean hasScaryBuff = false;
                for (Buff buff : enemy.buffs()) {
                    if(buff instanceof ScaryDamageBuff || buff instanceof ScaryImmunitiesBuff){
                        if (isNyzAlive) {
                            int heartDamage = (int) (8 * Random.NormalFloat(0.5f, 1));
                            enemy.damage(heartDamage, new DM100.LightningBolt());
                            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                                mob.HP += Math.min(heartDamage, mob.HT - mob.HP);
                            }
                        }
                    } else if (buff instanceof ScaryBuff) {
                        hasScaryBuff = true;
                        if (isNyzAlive) {
                            int heartDamage = (int) (8 * Random.NormalFloat(0.5f, 1));
                            enemy.damage(heartDamage, new DM100.LightningBolt());
                            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                                mob.HP += Math.min(heartDamage, mob.HT - mob.HP);
                            }
                        }
                        ((ScaryBuff) buff).damgeScary(4 * (isNyzAlive ? 2 : 1));
                    }
                }

                if (!hasScaryBuff) {
                    Buff.affect(enemy, ScaryBuff.class).set(100, 5);
                }
            }
        }

        if (buff(HeartMagicDamage.class) == null && (getClass() == ShubNiggurath.class) && !hasTooManyShubs() && notDamage >=8) {
            Buff.affect(this, HeartMagicDamage.class, 10f);
            ShubNiggurathClone clone = new ShubNiggurathClone();
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof ShubNiggurath) {
                    clone.HT = mob.HP;
                    clone.HP = mob.HP;
                    clone.notFirst = true;
                }
            }

            clone.pos = Dungeon.level.randomDestination(clone);
            GameScene.add(clone, 1f);
            Actor.add( new Pushing( clone, pos, clone.pos ) );
            Dungeon.level.occupyCell(clone);
            if(enemy != null){
                if(Dungeon.level.distance(pos, enemy.pos) <= 5){
                    boolean isNyzAlive = false;
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                        if (mob instanceof Nyarlathotep) {
                            isNyzAlive = true;
                            break;
                        }
                    }
                    boolean hasScaryBuff = false;
                    for (Buff buff : enemy.buffs()) {
                        if(buff instanceof ScaryDamageBuff || buff instanceof ScaryImmunitiesBuff){
                            if (isNyzAlive) {
                                int heartDamage = (int) (16 * Random.NormalFloat(0.5f, 1));
                                enemy.damage(heartDamage, new DM100.LightningBolt());
                                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                                    mob.HP += Math.min(heartDamage, mob.HT - mob.HP);
                                }
                            }
                        } else if (buff instanceof ScaryBuff) {
                            hasScaryBuff = true;
                            if (isNyzAlive) {
                                int heartDamage = (int) (16 * Random.NormalFloat(0.5f, 1));
                                enemy.damage(heartDamage, new DM100.LightningBolt());
                                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                                    mob.HP += Math.min(heartDamage, mob.HT - mob.HP);
                                }
                            }
                            ((ScaryBuff) buff).damgeScary(8 * (isNyzAlive ? 2 : 1));
                        }
                    }

                    if (!hasScaryBuff) {
                        Buff.affect(enemy, ScaryBuff.class).set(100, 5);
                    }
                }
            }
        }

        notDamage++;

        return super.act();
    }

    private static final String GENERATION	= "generation";
    private static final String NOTFIRST_INDEX	= "notfirst_index";
    private static final String NOT_DAMAGE	= "not_damage";

    private static final String REHEAL_HP	= "re_heal_hp";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( GENERATION, generation );
        bundle.put(NOTFIRST_INDEX, notFirst);
        bundle.put(NOT_DAMAGE, notDamage);
        bundle.put(REHEAL_HP, maxReHeal);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        generation = bundle.getInt( GENERATION );
        if (generation > 0) EXP = 0;
        notFirst = bundle.getBoolean(NOTFIRST_INDEX);
        notDamage = bundle.getInt(NOT_DAMAGE);
        maxReHeal = bundle.getInt(REHEAL_HP);
    }

    public static class ShubNiggurathClone extends ShubNiggurath {
        @Override
        protected boolean act() {

            boolean masterAlive = false;
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof ShubNiggurath && !((ShubNiggurath) mob).notFirst) {
                    masterAlive = true;
                    break;
                }
            }

            if (!masterAlive) {
                die(true);
            }

            if(pos == 312){
                die(true);
            }

            if (buff(HeartMagicDamage.class) == null) {
                Buff.affect(this, HeartMagicDamage.class, 10f);
                if (enemy != null && enemy == hero && enemySeen) {
                    boolean isNyzAlive = false;
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                        if (mob instanceof Nyarlathotep) {
                            isNyzAlive = true;
                            break;
                        }
                    }
                    boolean hasScaryBuff = false;
                    for (Buff buff : enemy.buffs()) {
                        if(buff instanceof ScaryDamageBuff || buff instanceof ScaryImmunitiesBuff){
                            if (isNyzAlive) {
                                int heartDamage = (int) (16 * Random.NormalFloat(0.5f, 1));
                                enemy.damage(heartDamage, new DM100.LightningBolt());
                                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                                    mob.HP += Math.min(heartDamage, mob.HT - mob.HP);
                                }
                            }
                        } else if (buff instanceof ScaryBuff) {
                            hasScaryBuff = true;
                            if (isNyzAlive) {
                                int heartDamage = (int) (16 * Random.NormalFloat(0.5f, 1));
                                enemy.damage(heartDamage, new DM100.LightningBolt());
                                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                                    mob.HP += Math.min(heartDamage, mob.HT - mob.HP);
                                }
                            }
                            ((ScaryBuff) buff).damgeScary(8 * (isNyzAlive ? 2 : 1));
                        }
                    }

                    if (!hasScaryBuff) {
                        Buff.affect(enemy, ScaryBuff.class).set(100, 5);
                    }
                }

            }
            return super.act();
        }
    }

    public static class HeartMagicDamage extends FlavourBuff {
        {
            type = buffType.POSITIVE;
        }
    }

}
