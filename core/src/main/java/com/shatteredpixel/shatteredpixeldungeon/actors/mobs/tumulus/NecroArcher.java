package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.AuraParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecroArcherSprite;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class NecroArcher extends Mob {
    {
        spriteClass = NecroArcherSprite.class;
        properties.add(Property.TUMULUS);
        HP = HT = 50;
        defenseSkill = 20;
        baseSpeed = 1f;
        EXP = 7;
        maxLvl = 14;
        properties.add(Property.NECRO);
        properties.add(Property.UNDEAD);
    }

    private int knockbackCooldown = 0;

    private boolean isLegionSpawn = false;
    private boolean legionSpawned = false;

    // 连击状态
    private int comboCount = 0;
    private Char comboEnemy = null;
    private boolean isComboing = false;   // 标记动画正在播放

    private static final String KNOCKBACK_CD = "knockback_cooldown";
    private static final String LEGION_SPAWN = "legion_spawn";
    private static final String LEGION_SPAWNED = "legion_spawned";

    // ---------- 军团生成 ----------
    private void spawnLegionAlly() {
        ArrayList<Integer> candidates = new ArrayList<>();
        int[] dirs = PathFinder.NEIGHBOURS8;
        for (int d : dirs) {
            int p = pos + d;
            if (Dungeon.level.passable[p]
                    && Actor.findChar(p) == null
                    && p != Dungeon.hero.pos) {
                candidates.add(p);
            }
        }
        if (candidates.isEmpty()) return;

        int validPos = Random.element(candidates);
        Mob ally;
        if (Random.Boolean()) {
            ally = new NecroArcher();
        } else {
            ally = new NecroGuard();
        }
        if (ally instanceof NecroArcher) {
            ((NecroArcher) ally).setLegionSpawn(true);
        } else if (ally instanceof NecroGuard) {
            ((NecroGuard) ally).setLegionSpawn(true);
        }
        ally.pos = validPos;
        GameScene.add(ally);
        Dungeon.level.occupyCell(ally);
    }

    public void setLegionSpawn(boolean val) {
        isLegionSpawn = val;
    }

    // ---------- act 覆盖 ----------
    @Override
    protected boolean act() {
        // 仅原生本体首次行动生成军团
        if (!legionSpawned && !isLegionSpawn) {
            spawnLegionAlly();
            legionSpawned = true;
        }
        if (knockbackCooldown > 0) knockbackCooldown--;

        // ★ 连击动画进行中，阻止其他动作，但保持调度（无延迟）
        if (isComboing) {
            spend(0); // 立即再次调度 act，但 isComboing 为 true，不会执行额外动作
            return true;
        }

        if (buff(NecroAuraBuff.class) == null) {
            Buff.affect(this, NecroAuraBuff.class);
        }

        // 正常行为（包括攻击、移动等）
        return super.act();
    }

    // ---------- doAttack 覆盖 ----------
    @Override
    public boolean doAttack(Char enemy) {
        // 如果已经在连击过程中（由 onAttackComplete 驱动），直接发起攻击，不再重新计算
        if (comboCount > 0) {
            isComboing = true;
            return super.doAttack(enemy);
        }

        // 计算连击数
        int strikeBonus = getStrikeBonus();
        int strikeTimes = 1 + strikeBonus;
        if(strikeTimes > 1){
            FloatingText.show(sprite.x, sprite.y, strikeTimes + Messages.get(Combo.class,"name"), strikeTimes > 2 ? CharSprite.NEGATIVE : CharSprite.WARNING);
        }


        // 单次攻击，正常执行
        if (strikeTimes <= 1) {
            return super.doAttack(enemy);
        }

        // 多次连击：初始化状态，执行第一次攻击
        comboCount = strikeTimes;
        comboEnemy = enemy;
        isComboing = true;
        return super.doAttack(enemy);
    }

    // ---------- onAttackComplete 覆盖 ----------
    @Override
    public void onAttackComplete() {
        // 清除动画标志（本次攻击动画已结束）
        isComboing = false;

        // 执行本次攻击（造成伤害，并设置正常的攻击延迟 spend）
        super.onAttackComplete();

        // 如果还有剩余攻击次数且目标存活
        if (comboCount > 1 && comboEnemy != null && comboEnemy.isAlive() && canAttack(comboEnemy)) {
            comboCount--;

            // ★ 覆盖 spend 为 0，使当前角色立即继续行动，其他实体不会插队
            spend(0);

            // 发起下一次攻击（播放动画，并在完成后再次调用 onAttackComplete）
            isComboing = true;
            super.doAttack(comboEnemy);
        } else {
            // 连击结束或目标死亡，清除状态
            comboCount = 0;
            comboEnemy = null;
        }
    }

    private int getStrikeBonus() {
        int count = 0;
        int range = 2;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob == this) continue;
            if (!(mob instanceof NecroArcher) && !(mob instanceof NecroGuard)) continue;
            int dist = Dungeon.level.distance(pos, mob.pos);
            if (dist <= range) {
                count++;
                if (count >= 3) break;
            }
        }
        return count;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return !Dungeon.level.adjacent(pos, enemy.pos)
                && (super.canAttack(enemy) || new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(7, 15);
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(KNOCKBACK_CD, knockbackCooldown);
        bundle.put(LEGION_SPAWN, isLegionSpawn);
        bundle.put(LEGION_SPAWNED, legionSpawned);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        knockbackCooldown = bundle.getInt(KNOCKBACK_CD);
        isLegionSpawn = bundle.getBoolean(LEGION_SPAWN);
        legionSpawned = bundle.getBoolean(LEGION_SPAWNED);

        comboCount = 0;
        comboEnemy = null;
        isComboing = false;
    }

    public boolean hasNearbyAllies() {
        int range = 2; // 曼哈顿距离 ≤ 2 → 5×5 范围
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob == this) continue;
            if (!(mob instanceof NecroArcher) && !(mob instanceof NecroGuard)) continue;
            if (Dungeon.level.distance(pos, mob.pos) <= range) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    public static class NecroAuraBuff extends Buff {

        private static final int RANGE = 2; // 正方形范围半径
        private HashSet<Integer> previousBorder = new HashSet<>();
        private boolean lastHasAllies = false;
        private int lastPos = -1;

        {
            type = buffType.POSITIVE;
        }

        private static class AuraManager {
            private static HashMap<Integer, Emitter> emitters = new HashMap<>();
            private static HashMap<Integer, Integer> counts = new HashMap<>();

            public static void addCell(int cell) {
                int count = counts.getOrDefault(cell, 0);
                if (count == 0) {
                    Emitter e = CellEmitter.get(cell);
                    e.pour(AuraParticle.FACTORY, 0.04f);
                    emitters.put(cell, e);
                }
                counts.put(cell, count + 1);
            }

            public static void removeCell(int cell) {
                int count = counts.getOrDefault(cell, 0);
                if (count <= 1) {
                    Emitter e = emitters.remove(cell);
                    if (e != null) {
                        e.on = false;
                    }
                    counts.remove(cell);
                } else {
                    counts.put(cell, count - 1);
                }
            }

            public static void clearAll() {
                for (Emitter e : emitters.values()) {
                    e.on = false;
                }
                emitters.clear();
                counts.clear();
            }
        }

        @Override
        public boolean act() {
            if (!(target instanceof NecroArcher)) {
                detach();
                return true;
            }

            NecroArcher archer = (NecroArcher) target;
            boolean hasAllies = archer.hasNearbyAllies();
            boolean posChanged = (target.pos != lastPos);

            if (hasAllies != lastHasAllies || posChanged) {
                if (hasAllies) {
                    updateBorder(archer.pos);
                } else {
                    removeAllBorders();
                }
                lastHasAllies = hasAllies;
                lastPos = target.pos;
            }

            spend(TICK);
            return true;
        }

        private void updateBorder(int center) {
            // 1. 计算 5×5 正方形范围内的所有格子（切比雪夫距离 ≤ RANGE）
            HashSet<Integer> cellsInRange = new HashSet<>();
            for (int dx = -RANGE; dx <= RANGE; dx++) {
                for (int dy = -RANGE; dy <= RANGE; dy++) {
                    int cell = center + dx + dy * Dungeon.level.width();
                    if (Dungeon.level.insideMap(cell)) {
                        cellsInRange.add(cell);
                    }
                }
            }

            // 2. 筛选边界格子：只保留最外环（使用四方向邻居）
            HashSet<Integer> newBorder = new HashSet<>();
            for (int cell : cellsInRange) {
                // 跳过不可通行或不可停留的格子（如墙壁）
                if (!Dungeon.level.passable[cell] && !Dungeon.level.avoid[cell]) continue;

                boolean isBorder = false;
                // 检查四个方向（上下左右）
                for (int n : PathFinder.NEIGHBOURS4) {
                    int neighbor = cell + n;
                    // 如果邻居不在范围内（或超出地图），则当前格是边界
                    if (!cellsInRange.contains(neighbor) || !Dungeon.level.insideMap(neighbor)) {
                        isBorder = true;
                        break;
                    }
                }
                if (isBorder) {
                    newBorder.add(cell);
                }
            }

            // 3. 对比新旧边界，更新全局管理器
            // 新增的格子
            HashSet<Integer> added = new HashSet<>(newBorder);
            added.removeAll(previousBorder);
            for (int cell : added) {
                AuraManager.addCell(cell);
            }

            // 移除的格子
            HashSet<Integer> removed = new HashSet<>(previousBorder);
            removed.removeAll(newBorder);
            for (int cell : removed) {
                AuraManager.removeCell(cell);
            }

            previousBorder = newBorder;
        }

        private void removeAllBorders() {
            for (int cell : previousBorder) {
                AuraManager.removeCell(cell);
            }
            previousBorder.clear();
        }

        @Override
        public void detach() {
            removeAllBorders();
            super.detach();
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            previousBorder.clear();
            lastHasAllies = false;
            lastPos = -1;
        }
    }
}