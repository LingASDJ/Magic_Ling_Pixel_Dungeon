/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HighNecromancerSprite;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.List;

public class HighNecromancer extends Mob {

    {
        spriteClass = HighNecromancerSprite.class;

        HP = HT = 65;

        defenseSkill = 20;

        baseSpeed = 1.5f;

        EXP = 12;
        maxLvl = 22;

        loot = null;
        lootChance = 1f;

        properties.add(Property.UNDEAD);

        HUNTING = new Hunting();
    }

    public boolean summoning = false;
    public int summoningPos = -1;
    protected boolean firstSummon = true;

    public Mob minion = null;
    private int storedMinionID = -1;

    // 掉落计数器：控制卷轴掉落衰减（每次掉落概率×1/3）
    private int scrollDropCount = Dungeon.LimitedDrops.scrollDropCount.count;

    // 召唤权重配置
    private static final class SummonEntry {
        public Class<? extends Mob> mobCls;
        public int weight;
        SummonEntry(Class<? extends Mob> cls, int w){
            mobCls = cls;
            weight = w;
        }
    }

    private static final List<SummonEntry> summonPool = new ArrayList<>();
    static {
        // !!!【重要】替换成你项目里对应的怪物类完整Class
        summonPool.add(new SummonEntry( NecroScout.class,       10 )); //死灵斥候
        summonPool.add(new SummonEntry( NecroArcher.class,      10 )); //死灵射手
        summonPool.add(new SummonEntry( NecroGuard.class,       10 )); //死灵卫兵
        summonPool.add(new SummonEntry( SkeletonDemon.class,     8 ));  //骸骨魔
        summonPool.add(new SummonEntry( Aggregatus.class,        5 )); //怨念集合体
        summonPool.add(new SummonEntry( NecroAcolyte.class,      5 ));  //死灵术士
        summonPool.add(new SummonEntry( NecroCavalry.class,      5 ));  //死灵骑兵
    }

    // 随从伤害加成Buff：+1~5固定伤害
    public static class MinionDamageBonus extends Buff{
        public int dmgBonus;

        @Override
        public boolean act() {
            //持续buff，永久存在直到召唤物消失
            spend(TICK);
            return true;
        }

        public int getBonus(){
            return dmgBonus;
        }

        private static final String BONUS = "bonus";
        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(BONUS, dmgBonus);
        }
        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            dmgBonus = bundle.getInt(BONUS);
        }
    }

    @Override
    protected boolean act() {
        if (summoning && state != HUNTING){
            summoning = false;
            if (sprite instanceof HighNecromancerSprite) ((HighNecromancerSprite) sprite).cancelSummoning();
        }

        // ========== 赴死契约效果：存在随从时每回合回血 ==========
        if (minion != null && minion.isAlive() && Dungeon.level.mobs.contains(minion) && minion.alignment == alignment){
            //自身回血
            this.HP = Math.min(HP + 15, HT);
            if (sprite.visible){
                sprite.showStatusWithIcon(CharSprite.POSITIVE, "15", FloatingText.HEALING);
            }
            //随从回血
            minion.HP = Math.min(minion.HP + 15, minion.HT);
            if (minion.sprite.visible){
                minion.sprite.showStatusWithIcon(CharSprite.POSITIVE, "15", FloatingText.HEALING);
            }
        }

        return super.act();
    }

    @Override
    public void aggro(Char ch) {
        super.aggro(ch);
        if (minion != null && minion.isAlive()
                && Dungeon.level.mobs.contains(minion)
                && minion.alignment == alignment){
            minion.aggro(ch);
        }
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 8);
    }

    // 不能普通攻击
    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    // 随机召唤权重选择单位
    private Mob selectRandomMinion(){
        int totalWeight = 0;
        for(SummonEntry e : summonPool) totalWeight += e.weight;
        int roll = Random.Int(totalWeight);
        int cur = 0;
        for(SummonEntry e : summonPool){
            cur += e.weight;
            if (roll < cur){
                try {
                    return e.mobCls.newInstance();
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    public void summonMinion(){
        if (Actor.findChar(summoningPos) != null) {
            if (Char.hasProp(Actor.findChar(summoningPos), Property.IMMOVABLE)){
                summoning = false;
                ((HighNecromancerSprite)sprite).finishSummoning();
                spend(TICK);
                return;
            }

            int pushPos = pos;
            for (int c : PathFinder.NEIGHBOURS8) {
                if (Actor.findChar(summoningPos + c) == null
                        && Dungeon.level.passable[summoningPos + c]
                        && (Dungeon.level.openSpace[summoningPos + c] || !hasProp(Actor.findChar(summoningPos), Property.LARGE))
                        && Dungeon.level.trueDistance(pos, summoningPos + c) > Dungeon.level.trueDistance(pos, pushPos)) {
                    pushPos = summoningPos + c;
                }
            }

            if (pushPos != pos) {
                Char ch = Actor.findChar(summoningPos);
                Actor.add( new Pushing( ch, ch.pos, pushPos ) );
                ch.pos = pushPos;
                Dungeon.level.occupyCell(ch );
            } else {
                spend(TICK);
                return;
            }
        }

        summoning = firstSummon = false;

        minion = selectRandomMinion();
        if (minion == null){
            summoning = false;
            ((HighNecromancerSprite)sprite).finishSummoning();
            spend(TICK);
            return;
        }

        minion.pos = summoningPos;
        GameScene.add( minion );
        Dungeon.level.occupyCell( minion );
        ((HighNecromancerSprite)sprite).finishSummoning();

        // 继承盟友buff、精英buff
        for (Buff b : buffs(AllyBuff.class)){
            Buff.affect(minion, b.getClass());
        }
        for (Buff b : buffs(ChampionEnemy.class)){
            Buff.affect( minion, b.getClass());
        }

        // 给予随从伤害加成buff 1~5
        MinionDamageBonus dmgBuff = Buff.affect(minion, MinionDamageBonus.class);
        dmgBuff.dmgBonus = Random.IntRange(1,5);
    }

    @Override
    public void die(Object cause) {
        if (storedMinionID != -1){
            Actor ch = Actor.findById(storedMinionID);
            storedMinionID = -1;
            if (ch instanceof Mob){
                minion = (Mob) ch;
            }
        }

        //死亡时清除自己召唤的随从
        if (minion != null && minion.isAlive() && minion.alignment == alignment){
            minion.die(null);
        }

        super.die(cause);
    }

    //==================== 掉落逻辑：卷轴，排除嬗变，概率每次×1/3 ====================
    @Override
    public Item createLoot() {
        float dropMult = (float) Math.pow(1f/3f, scrollDropCount);
        if (Random.Float() > dropMult){
            return null;
        }
        scrollDropCount++;

        List<Class<? extends Scroll>> scrollPool = new ArrayList<>();
        for (Class<? extends Scroll> scrollCls : (Class<? extends Scroll>[]) Generator.Category.SCROLL.classes){
            if (scrollCls != ScrollOfTransmutation.class){
                scrollPool.add(scrollCls);
            }
        }
        if (scrollPool.isEmpty()) return null;
        try {
            return Reflection.newInstance(Random.getRandomElement(scrollPool));
        } catch (Exception e){
            return null;
        }
    }

    //==================== 存档序列化 ====================
    private static final String SUMMONING = "summoning";
    private static final String FIRST_SUMMON = "first_summon";
    private static final String SUMMONING_POS = "summoning_pos";
    private static final String MY_MINION = "my_minion";
    private static final String SCROLL_DROP_COUNT = "scroll_drop_count";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SUMMONING, summoning);
        bundle.put(FIRST_SUMMON, firstSummon);
        bundle.put(SCROLL_DROP_COUNT, scrollDropCount);
        if (summoning){
            bundle.put(SUMMONING_POS, summoningPos);
        }
        if (minion != null){
            bundle.put(MY_MINION, minion.id());
        } else if (storedMinionID != -1){
            bundle.put(MY_MINION, storedMinionID);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        summoning = bundle.getBoolean(SUMMONING);
        if (bundle.contains(FIRST_SUMMON)) firstSummon = bundle.getBoolean(FIRST_SUMMON);
        scrollDropCount = bundle.getInt(SCROLL_DROP_COUNT);
        if (summoning){
            summoningPos = bundle.getInt(SUMMONING_POS);
        }
        if (bundle.contains(MY_MINION )){
            storedMinionID = bundle.getInt(MY_MINION );
        }
    }

    //==================== AI行为逻辑 ====================
    private class Hunting extends Mob.Hunting{

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV;
            if (enemySeen){
                target = enemy.pos;
            }

            if (storedMinionID != -1){
                Actor ch = Actor.findById(storedMinionID);
                storedMinionID = -1;
                if (ch instanceof Mob){
                    minion = (Mob) ch;
                }
            }

            if (summoning){
                summonMinion();
                return true;
            }

            //校验随从是否有效
            if (minion != null &&
                    (!minion.isAlive()
                            || !Dungeon.level.mobs.contains(minion)
                            || minion.alignment != alignment)){
                minion = null;
            }

            //敌人可视，距离≤4，没有随从 → 召唤
            if (enemySeen && Dungeon.level.distance(pos, enemy.pos) <= 4 && minion == null){
                summoningPos = -1;
                PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), Dungeon.level.distance(pos, enemy.pos)+3);

                for (int c : PathFinder.NEIGHBOURS8){
                    if (Actor.findChar(enemy.pos+c) == null
                            && PathFinder.distance[enemy.pos+c] != Integer.MAX_VALUE
                            && Dungeon.level.passable[enemy.pos+c]
                            && (!hasProp(HighNecromancer.this, Property.LARGE) || Dungeon.level.openSpace[enemy.pos+c])
                            && fieldOfView[enemy.pos+c]
                            && Dungeon.level.trueDistance(pos, enemy.pos+c) < Dungeon.level.trueDistance(pos, summoningPos)){
                        summoningPos = enemy.pos+c;
                    }
                }

                if (summoningPos != -1){
                    summoning = true;
                    sprite.zap( summoningPos );
                    if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[summoningPos]){
                        Dungeon.hero.interrupt();
                    }
                    spend( firstSummon ? TICK : 2*TICK );
                } else {
                    spend(TICK);
                }
                return true;
            } else {
                // 有随从 / 敌人不在范围 → 使用普通游荡追击逻辑
                return super.act(enemyInFOV, justAlerted);
            }
        }
    }
}