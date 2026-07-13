package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.custom.buffs.ZeroDefense;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecroCavalrySprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class NecroCavalry extends Mob {
    {
        spriteClass = NecroCavalrySprite.class;
        properties.add(Property.TUMULUS);
        HP = HT = 130;
        defenseSkill = 20;
        baseSpeed = 1f;
        EXP = 12;
        maxLvl = 16;
        HUNTING = new Hunting();
        immunities.add(Roots.class);
        properties.add(Property.NOKOCK);
    }

    private boolean isLegion2Spawn = false;
    private boolean legion2Spawned = false;

    // 冲锋相关
    private int chargeTargetPos = -1;
    private float chargeCooldown = 0f;
    private List<Integer> chargePath; // 存储完整冲锋路径，分段移动
    private static final String LEG2_SPAWN = "legion2_spawn";
    private static final String LEG2_SPAWNED = "legion2_spawned";
    private static final String CHARGE_TARGET = "charge_target";
    private static final String CHARGE_CD = "charge_cd";
    private static final String CHARGE_PATH_SIZE = "charge_path_size";
    private static final String CHARGE_PATH_VAL = "charge_path_val_";

    // 开局生成亡灵军团2：1亡灵弓手+1亡灵守卫
    private void spawnLegionII() {
        ArrayList<Integer> validCells = new ArrayList<>();
        int[] dirs = PathFinder.NEIGHBOURS8;
        for (int d : dirs) {
            int p = pos + d;
            if (Dungeon.level.passable[p] && Actor.findChar(p) == null && p != Dungeon.hero.pos) {
                validCells.add(p);
            }
        }
        if (validCells.isEmpty()) return;

        int archerPos = Random.element(validCells);
        NecroArcher archer = new NecroArcher();
        archer.setLegionSpawn(true);
        archer.pos = archerPos;
        GameScene.add(archer);
        Dungeon.level.occupyCell(archer);
        validCells.remove(Integer.valueOf(archerPos));

        if (validCells.isEmpty()) return;
        int guardPos = Random.element(validCells);
        NecroGuard guard = new NecroGuard();
        guard.setLegionSpawn(true);
        guard.pos = guardPos;
        GameScene.add(guard);
        Dungeon.level.occupyCell(guard);
    }

    public void setLegion2Spawn(boolean val) {
        isLegion2Spawn = val;
    }

    @Override
    public float speed() {
        float spd = super.speed();
        return Math.max(1f, spd);
    }

    // 重甲骑兵无法进行普通近战攻击，仅依靠冲锋输出
    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(25, 35);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public int attackSkill(Char target) {
        return 30;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEG2_SPAWN, isLegion2Spawn);
        bundle.put(LEG2_SPAWNED, legion2Spawned);
        bundle.put(CHARGE_TARGET, chargeTargetPos);
        bundle.put(CHARGE_CD, chargeCooldown);

        // 手动存储 List<Integer> 路径
        if (chargePath != null && !chargePath.isEmpty()) {
            bundle.put(CHARGE_PATH_SIZE, chargePath.size());
            for (int i = 0; i < chargePath.size(); i++) {
                bundle.put(CHARGE_PATH_VAL + i, chargePath.get(i));
            }
        } else {
            bundle.put(CHARGE_PATH_SIZE, 0);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        isLegion2Spawn = bundle.getBoolean(LEG2_SPAWN);
        legion2Spawned = bundle.getBoolean(LEG2_SPAWNED);
        chargeTargetPos = bundle.getInt(CHARGE_TARGET);
        chargeCooldown = bundle.getFloat(CHARGE_CD);

        // 手动读取路径列表
        int pathSize = bundle.getInt(CHARGE_PATH_SIZE);
        chargePath = new ArrayList<>();
        for (int i = 0; i < pathSize; i++) {
            chargePath.add(bundle.getInt(CHARGE_PATH_VAL + i));
        }
        if (chargePath.isEmpty()) chargePath = null;
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

            // ========== 正在冲锋分段移动（无jump动画，纯act移动，不锁玩家） ==========
            if (chargePath != null && !chargePath.isEmpty()) {
                // 缠绕直接终止冲锋
                if (rooted) {
                    chargePath = null;
                    chargeTargetPos = -1;
                    spend(TICK);
                    return true;
                }

                // 取路径下一格移动
                int nextCell = chargePath.remove(0);
                int oldPos = pos;
                pos = nextCell;
                Dungeon.level.occupyCell(NecroCavalry.this);
                sprite.move(oldPos, pos);

                // 路径走完，抵达终点触发3x3AOE伤害
                if (chargePath.isEmpty()) {
                    int[] nineDir = PathFinder.NEIGHBOURS9;
                    for (int d : nineDir) {
                        int checkCell = pos + d;
                        Char ch = Actor.findChar(checkCell);
                        if (ch != null && ch.alignment != NecroCavalry.this.alignment) {
                            int dmg = damageRoll() * 2;
                            ch.damage(dmg, NecroCavalry.this);
                            Buff.affect(ch, ZeroDefense.class, 9f);
                        }
                    }
                    Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH);
                    // 重置冲锋状态，刷新冷却
                    chargeCooldown = Random.NormalFloat(2f, 4f);
                    chargePath = null;
                    chargeTargetPos = -1;
                }

                // 冲锋3倍移速消耗，玩家不受任何锁定
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

            // 冷却完毕、未被缠绕，原地蓄力1回合准备冲锋
            if (chargeCooldown <= 0 && !rooted) {
                Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET);
                // 无障碍物直达目标才开启冲锋
                if (aim.collisionPos == enemy.pos) {
                    chargeTargetPos = enemy.pos;
                    // 原地蓄力1回合
                    spend(GameMath.gate(TICK, TICK, attackDelay()));

                    // 计算完整冲锋路径并修正悬崖落点
                    Ballistica previewPathBall = new Ballistica(pos, chargeTargetPos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET);
                    int endCell = previewPathBall.collisionPos;
                    if (Dungeon.level.map[endCell] == Terrain.CHASM) {
                        endCell = previewPathBall.path.get(previewPathBall.path.size() - 2);
                    }

                    // 绘制落点3x3预警，无路径红线，无锁住玩家代码
                    if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[chargeTargetPos]) {
                        int[] dirs = PathFinder.NEIGHBOURS9;
                        for (int d : dirs) {
                            int cell = endCell + d;
                            int color = cell == endCell ? 0xFF0000 : 0x660000;
                            sprite.parent.addToBack(new ColorTargetedCell(cell, color));
                        }
                    }

                    // 生成修正悬崖后的冲锋路径
                    Ballistica chargeBall = new Ballistica(pos, chargeTargetPos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET);
                    List<Integer> rawPath = chargeBall.path;
                    if (Dungeon.level.map[chargeBall.collisionPos] == Terrain.CHASM || Dungeon.level.map[chargeBall.collisionPos] == Terrain.WALL ) {
                        rawPath = rawPath.subList(0, rawPath.size() - 1);
                    }
                    chargePath = new ArrayList<>(rawPath);
                    chargePath.remove(0); // 移除自身当前位置，从下一格开始移动

                    return true;
                }
            }

            // 无法冲锋则正常寻路靠近敌人
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
    }
}