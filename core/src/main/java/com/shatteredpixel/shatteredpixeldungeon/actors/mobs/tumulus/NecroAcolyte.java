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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecroAcolyteSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WispSprite;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class NecroAcolyte extends Mob {

    {
        spriteClass = NecroAcolyteSprite.class;

        HP = HT = 55;
        defenseSkill = 18;
        properties.add(Property.TUMULUS);
        properties.add(Property.UNDEAD);
        HUNTING = new Hunting();
    }

    // 召唤状态标记、召唤坐标、20回合冷却
    public boolean summoning = false;
    public int summoningPos = -1;
    public int summonCooldown = 0;

    // 记录自身召唤的所有WispDead鬼火ID
    private ArrayList<Integer> wispIDs = new ArrayList<>();

    // 独立属性函数
    @Override
    public int damageRoll() {
        // 普攻无物理伤害，降头仅debuff
        return 0;
    }

    @Override
    public int attackSkill( Char target ) {
        // 命中22
        return 22;
    }

    @Override
    public int drRoll() {
        // 防御0~3
        return super.drRoll() + Random.NormalIntRange(0, 3);
    }

    @Override
    protected boolean act() {
        // 非狩猎状态取消召唤动画
        if (summoning && state != HUNTING){
            summoning = false;
            if (sprite instanceof NecroAcolyteSprite) {
                ((NecroAcolyteSprite) sprite).cancelSummoning();
            }
        }
        // 冷却倒计时
        if (summonCooldown > 0) summonCooldown--;

        return super.act();
    }

    // 攻击替换：下级降头术，施加5回合虚弱+幻惑
    @Override
    public int attackProc(Char target, int damage) {
        damage = 0;
        Buff.affect(target, Weakness.class, 5f);
        Buff.affect(target, Hex.class, 5f);
        target.sprite.burst(0x662266, 10);
        return damage;
    }

    // 统计存活且归属自身的鬼火数量
    private int countOwnedWispDead() {
        int alive = 0;
        for (int id : wispIDs) {
            Actor a = Actor.findById(id);
            if (a instanceof WispDead && ((WispDead) a).isAlive() && ((WispDead) a).alignment == this.alignment) {
                alive++;
            }
        }
        return alive;
    }

    // 召唤鬼火主流程，复刻原版Necromancer推挤/阻挡逻辑
    public void summonMinion() {
        if (Actor.findChar(summoningPos) != null) {
            // 不可移动单位直接中断召唤
            if (Char.hasProp(Actor.findChar(summoningPos), Property.IMMOVABLE)){
                summoning = false;
                ((NecroAcolyteSprite)sprite).finishSummoning();
                spend(TICK);
                return;
            }

            int pushPos = pos;
            for (int c : PathFinder.NEIGHBOURS8) {
                int cell = summoningPos + c;
                if (Actor.findChar(cell) == null
                        && Dungeon.level.passable[cell]
                        && (Dungeon.level.openSpace[cell] || !hasProp(Actor.findChar(summoningPos), Property.LARGE))
                        && Dungeon.level.trueDistance(pos, cell) > Dungeon.level.trueDistance(pos, pushPos)) {
                    pushPos = cell;
                }
            }

            // 可推开障碍物
            if (pushPos != pos) {
                Char blocker = Actor.findChar(summoningPos);
                Actor.add(new Pushing(blocker, blocker.pos, pushPos));
                blocker.pos = pushPos;
                Dungeon.level.occupyCell(blocker);
            } else {
                // 无空位，等待一回合
                spend(TICK);
                return;
            }
        }

        summoning = false;

        // 生成鬼火WispDead
        WispDead wisp = new WispDead();
        wisp.pos = summoningPos;
        GameScene.add(wisp);
        Dungeon.level.occupyCell(wisp);
        ((NecroAcolyteSprite)sprite).finishSummoning();

        // 同步精英、同盟buff到召唤物
        for (Buff b : buffs(AllyBuff.class)){
            Buff.affect(wisp, b.getClass());
        }
        for (Buff b : buffs(ChampionEnemy.class)){
            Buff.affect(wisp, b.getClass());
        }
        wispIDs.add(wisp.id());
        // 召唤冷却重置20回合
        summonCooldown = 20;
    }

    // 死亡时清除所有自己召唤的鬼火
    @Override
    public void die(Object cause) {
        for (int id : wispIDs){
            Actor a = Actor.findById(id);
            if (a instanceof WispDead && ((WispDead) a).alignment == alignment){
                ((WispDead) a).die(null);
            }
        }
        super.die(cause);
    }
    
    public static class WispDead extends Wisp {
        {
            spriteClass = WispDeadSprite.class;
            immunities.add(HalomethaneBurning.class);
        }
        public static class  WispDeadSprite extends WispSprite {

            public WispDeadSprite(){
                super();
                brightness(0.75f);
                tint(1, 0.4f, 0.4f, 0.4f);
            }

            @Override
            public void resetColor() {
                super.resetColor();
                brightness(0.75f);
                tint(1, 0.4f, 0.4f, 0.4f);
            }
        }
    }

    // 存档键
    private static final String SUMMONING = "summoning";
    private static final String SUMMON_POS = "summoning_pos";
    private static final String COOLDOWN = "summon_cd";
    private static final String WISP_ID_ARR = "wisp_ids";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SUMMONING, summoning);
        if (summoning) bundle.put(SUMMON_POS, summoningPos);
        bundle.put(COOLDOWN, summonCooldown);

        int[] ids = new int[wispIDs.size()];
        int idx = 0;
        for (Integer i : wispIDs) ids[idx++] = i;
        bundle.put(WISP_ID_ARR, ids);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        summoning = bundle.getBoolean(SUMMONING);
        if (bundle.contains(SUMMON_POS)) summoningPos = bundle.getInt(SUMMON_POS);
        summonCooldown = bundle.getInt(COOLDOWN);

        wispIDs.clear();
        if (bundle.contains(WISP_ID_ARR)) {
            for (int id : bundle.getIntArray(WISP_ID_ARR)) wispIDs.add(id);
        }
    }

    // 狩猎AI，对标原版死灵法师逻辑
    private class Hunting extends Mob.Hunting{
        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV;
            if (enemySeen) target = enemy.pos;

            // 正在召唤则执行生成鬼火
            if (summoning){
                summonMinion();
                return true;
            }

            // 判定：看到敌人、冷却完毕、没有自己的鬼火 → 寻找召唤点召唤
            if (enemySeen && summonCooldown <= 0 && countOwnedWispDead() <= 0) {
                summoningPos = -1;
                PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), Dungeon.level.distance(pos, enemy.pos)+3);

                // 寻找敌人身旁可召唤空位
                for (int c : PathFinder.NEIGHBOURS8){
                    int cell = enemy.pos + c;
                    if (Actor.findChar(cell) == null
                            && PathFinder.distance[cell] != Integer.MAX_VALUE
                            && Dungeon.level.passable[cell]
                            && (!hasProp(NecroAcolyte.this, Property.LARGE) || Dungeon.level.openSpace[cell])
                            && fieldOfView[cell]
                            && (summoningPos == -1 || Dungeon.level.trueDistance(pos, cell) < Dungeon.level.trueDistance(pos, summoningPos))) {
                        summoningPos = cell;
                    }
                }

                if (summoningPos != -1){
                    summoning = true;
                    sprite.zap(summoningPos);
                    spend(TICK);
                    return true;
                } else {
                    spend(TICK);
                    return true;
                }
            }

            // 不满足召唤条件，走普通追击逻辑
            return super.act(enemyInFOV, justAlerted);
        }
    }
}