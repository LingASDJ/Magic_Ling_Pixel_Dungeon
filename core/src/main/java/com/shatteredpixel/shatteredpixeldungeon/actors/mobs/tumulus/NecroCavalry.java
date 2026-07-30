package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.custom.buffs.ZeroDefense;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spear;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecroCavalrySprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NecroCavalry extends Mob {

    {
        spriteClass = NecroCavalrySprite.class;
        properties.add(Property.TUMULUS);
        HP = HT = 80;
        defenseSkill = 20;
        baseSpeed = 1f;
        EXP = 11;
        maxLvl = 20;
        HUNTING = new Hunting();
        immunities.add(Roots.class);
        properties.add(Property.NOKOCK);
        properties.add(Property.NECRO);
        properties.add(Property.UNDEAD);
    }

    private boolean isLegion2Spawn = false;
    private boolean legion2Spawned = false;

    // 冲锋相关
    private int chargeTargetPos = -1;
    private float chargeCooldown = 0f;
    private List<Integer> chargePath;
    // 记录本轮冲锋已经攻击过的目标，避免重复攻击
    private HashSet<Integer> chargeAttackedTargets = new HashSet<>();

    private static final String LEG2_SPAWN = "legion2_spawn";
    private static final String LEG2_SPAWNED = "legion2_spawned";
    private static final String CHARGE_TARGET = "charge_target";
    private static final String CHARGE_CD = "charge_cd";
    private static final String CHARGE_PATH_SIZE = "charge_path_size";
    private static final String CHARGE_PATH_VAL = "charge_path_val_";
    private static final String CHARGE_ATTACKED_SIZE = "charge_attacked_size";
    private static final String CHARGE_ATTACKED_VAL = "charge_attacked_val_";

    // ========== 基础属性 ==========

    @Override
    public float speed() {
        // 铁骨烈蹄：移动速度无法低于1
        return Math.max(1f, super.speed());
    }

    @Override
    protected boolean canAttack(Char enemy) {
        // 重甲骑兵无法进行普通近战攻击
        return false;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(20, 30);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public int attackSkill(Char target) {
        return 30;
    }

    // ========== 军团II ==========

    private void spawnLegionII() {
        ArrayList<Integer> validCells = new ArrayList<>();
        for (int d : PathFinder.NEIGHBOURS8) {
            int p = pos + d;
            if (Dungeon.level.passable[p] && Actor.findChar(p) == null && p != Dungeon.hero.pos) {
                validCells.add(p);
            }
        }
        if (validCells.isEmpty()) return;

        int archerPos = Random.element(validCells);
        NecroArcher archer = new NecroArcher();
        archer.setLegionSpawn(true);  // 标记为军团召唤，不触发军团I
        archer.pos = archerPos;
        GameScene.add(archer);
        Dungeon.level.occupyCell(archer);
        validCells.remove(Integer.valueOf(archerPos));

        if (validCells.isEmpty()) return;
        int guardPos = Random.element(validCells);
        NecroGuard guard = new NecroGuard();
        guard.setLegionSpawn(true);  // 标记为军团召唤，不触发军团I
        guard.pos = guardPos;
        GameScene.add(guard);
        Dungeon.level.occupyCell(guard);
    }

    public void setLegion2Spawn(boolean val) {
        isLegion2Spawn = val;
    }

    // ========== 掉落机制 ==========

    // 极速药水掉落概率
    private float hasteDropChance = 1.0f;  // 首次必定掉落

    @Override
    public void rollToDropLoot() {
        if (Dungeon.hero.lvl > maxLvl + 2) return;

        // 必定掉落板甲
        Dungeon.level.drop(new PlateArmor().identify(), pos).sprite.drop();

        // 必定掉落长矛
        Dungeon.level.drop(new Spear().identify(), pos).sprite.drop();

        // 极速药水：每次掉落概率变为1/3
        if (Random.Float() < hasteDropChance) {
            Dungeon.level.drop(new PotionOfHaste(), pos).sprite.drop();
            hasteDropChance /= 3f;
        }

        super.rollToDropLoot();
    }

    // ========== 序列化 ==========

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEG2_SPAWN, isLegion2Spawn);
        bundle.put(LEG2_SPAWNED, legion2Spawned);
        bundle.put(CHARGE_TARGET, chargeTargetPos);
        bundle.put(CHARGE_CD, chargeCooldown);

        if (chargePath != null && !chargePath.isEmpty()) {
            bundle.put(CHARGE_PATH_SIZE, chargePath.size());
            for (int i = 0; i < chargePath.size(); i++) {
                bundle.put(CHARGE_PATH_VAL + i, chargePath.get(i));
            }
        } else {
            bundle.put(CHARGE_PATH_SIZE, 0);
        }

        // 存储已攻击目标
        if (chargeAttackedTargets != null && !chargeAttackedTargets.isEmpty()) {
            bundle.put(CHARGE_ATTACKED_SIZE, chargeAttackedTargets.size());
            int idx = 0;
            for (int id : chargeAttackedTargets) {
                bundle.put(CHARGE_ATTACKED_VAL + idx, id);
                idx++;
            }
        } else {
            bundle.put(CHARGE_ATTACKED_SIZE, 0);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        isLegion2Spawn = bundle.getBoolean(LEG2_SPAWN);
        legion2Spawned = bundle.getBoolean(LEG2_SPAWNED);
        chargeTargetPos = bundle.getInt(CHARGE_TARGET);
        chargeCooldown = bundle.getFloat(CHARGE_CD);

        int pathSize = bundle.getInt(CHARGE_PATH_SIZE);
        chargePath = new ArrayList<>();
        for (int i = 0; i < pathSize; i++) {
            chargePath.add(bundle.getInt(CHARGE_PATH_VAL + i));
        }
        if (chargePath.isEmpty()) chargePath = null;

        int attackedSize = bundle.getInt(CHARGE_ATTACKED_SIZE);
        chargeAttackedTargets = new HashSet<>();
        for (int i = 0; i < attackedSize; i++) {
            chargeAttackedTargets.add(bundle.getInt(CHARGE_ATTACKED_VAL + i));
        }
    }

    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            // 首次出场生成军团II
            if (!legion2Spawned && !isLegion2Spawn) {
                spawnLegionII();
                legion2Spawned = true;
            }

            // 冲锋冷却倒计时
            if (paralysed <= 0) chargeCooldown -= TICK;

            enemySeen = enemyInFOV;

            // ========== 正在冲锋分段移动 ==========
            if (chargePath != null && !chargePath.isEmpty()) {
                // 缠绕直接终止冲锋（虽然免疫Roots，但以防万一）
                if (rooted) {
                    endCharge();
                    spend(TICK);
                    return true;
                }

                // 过滤非法坐标：墙壁/深渊/地图外直接终止冲锋
                while (!chargePath.isEmpty()) {
                    int testCell = chargePath.get(0);
                    if (Dungeon.level.insideMap(testCell)
                            && Dungeon.level.passable[testCell]
                            && Dungeon.level.map[testCell] != Terrain.CHASM) {
                        break;
                    } else {
                        chargePath.remove(0);
                        // 下一格无法通行，直接结束冲锋
                        if (!chargePath.isEmpty()) {
                            chargePath.clear();
                        }
                    }
                }

                if (chargePath.isEmpty()) {
                    endCharge();
                    spend(TICK);
                    return true;
                }

                // 取路径下一格移动
                int nextCell = chargePath.remove(0);
                int oldPos = pos;
                pos = nextCell;
                Dungeon.level.occupyCell(NecroCavalry.this);
                sprite.move(oldPos, pos);

                // 冲锋过程中：检查周围3x3是否有可攻击的敌人
                // 攻击不消耗回合，但同一目标每轮冲锋只攻击一次
                performChargeAttack();

                // 路径走完，冲锋结束
                if (chargePath.isEmpty()) {
                    endCharge();
                }

                // 冲锋3倍移速：每格消耗 1/3 回合
                spend(1f / 3f);
                return moveSprite(oldPos, pos);
            }

            // 无目标进入游荡状态
            if (!enemyInFOV || enemy == null || isCharmedBy(enemy)) {
                state = WANDERING;
                target = Dungeon.level.randomDestination(NecroCavalry.this);
                spend(speed());
                return true;
            }

            // 冷却完毕、未被缠绕，准备冲锋
            if (chargeCooldown <= 0 && !rooted) {
                Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT);
                if (aim.collisionPos == enemy.pos) {
                    prepareCharge(enemy.pos);
                    return true;
                }
            }

            // 无法冲锋则正常寻路靠近
            int oldPos = pos;
            if (target != -1 && getCloser(target)) {
                spend(1f / speed());
                return moveSprite(oldPos, pos);
            } else {
                spend(TICK);
                if (!enemyInFOV) {
                    sprite.showLost();
                    state = WANDERING;
                    target = Dungeon.level.randomDestination(NecroCavalry.this);
                }
                return true;
            }
        }

        // 准备冲锋：原地蓄力1回合【核心修复函数】
        private void prepareCharge(int targetPos) {
            chargeTargetPos = targetPos;
            // 原地蓄力1回合
            spend(GameMath.gate(TICK, TICK, attackDelay()));

            Ballistica previewPath = new Ballistica(pos, chargeTargetPos, Ballistica.MAGIC_BOLT);
            List<Integer> rawPath = new ArrayList<>(previewPath.path);

            // ==========【核心修复1】硬性限制冲锋最多3格 ==========
            int maxChargeRange = 3;
            if (rawPath.size() > maxChargeRange + 1) {
                rawPath = rawPath.subList(0, maxChargeRange + 1);
            }

            // ==========【核心修复2】沿着路径向前查找第一个阻挡物，提前截断 ==========
            int validEndIndex = rawPath.size() - 1;
            for (int i = 1; i < rawPath.size(); i++) {
                int cell = rawPath.get(i);
                if (!Dungeon.level.insideMap(cell)
                        || !Dungeon.level.passable[cell]
                        || Dungeon.level.map[cell] == Terrain.CHASM) {
                    validEndIndex = i - 1;
                    break;
                }
            }

            // 起点之后没有有效格子，放弃冲锋
            if (validEndIndex < 1) {
                chargeTargetPos = -1;
                return;
            }

            List<Integer> finalPath = rawPath.subList(0, validEndIndex + 1);
            int safeEnd = finalPath.get(finalPath.size() - 1);

            // 绘制落点3x3预警
            if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[safeEnd]) {
                for (int d : PathFinder.NEIGHBOURS9) {
                    int cell = safeEnd + d;
                    if (!Dungeon.level.insideMap(cell)) continue;
                    int color = cell == safeEnd ? 0xFF0000 : 0x660000;
                    sprite.parent.addToBack(new ColorTargetedCell(cell, color));
                }
            }

            chargePath = new ArrayList<>(finalPath);
            chargePath.remove(0); // 移除自身位置，从下一格开始

            // 重置已攻击目标记录
            chargeAttackedTargets.clear();
        }

        // 冲锋过程中的攻击判定
        private void performChargeAttack() {
            int[] dirs = PathFinder.NEIGHBOURS9;
            for (int d : dirs) {
                int checkCell = pos + d;
                if (!Dungeon.level.insideMap(checkCell)) continue;

                Char ch = Actor.findChar(checkCell);
                if (ch != null
                        && ch.alignment != NecroCavalry.this.alignment
                        && !chargeAttackedTargets.contains(ch.id())) {

                    // 双倍攻击力伤害
                    int dmg = damageRoll() * 2;
                    ch.damage(dmg, NecroCavalry.this);
                    // 施加9回合破甲
                    Buff.affect(ch, ZeroDefense.class, 9f);

                    // 标记已攻击，本轮冲锋不再攻击同一目标
                    chargeAttackedTargets.add(ch.id());
                }
            }
        }

        // 结束冲锋状态
        private void endCharge() {
            chargePath = null;
            chargeTargetPos = -1;
            chargeCooldown = Random.NormalFloat(2f, 4f);
            chargeAttackedTargets.clear();
        }
    }
}