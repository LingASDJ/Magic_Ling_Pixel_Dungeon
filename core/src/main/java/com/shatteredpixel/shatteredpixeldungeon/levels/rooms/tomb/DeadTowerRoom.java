package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.tomb;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.DeathBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Aggregatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroAcolyte;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroArcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroCavalry;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroPioneer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroWarlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.SkeletonDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Wisp;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.CustomLuaRoom;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeadTowerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class DeadTowerRoom extends CustomLuaRoom.FullLuaCustomRoom {

    {
        width = 17;
        height = 17;
        map_lua_file = Assets.Map_Luas.Tomb_DeathTower_MapLua;
    }

    public static class DeadTower extends Mob {
        {
            spriteClass = DeadTowerSprite.class;
            HT = HP = 150;
            defenseSkill = 0;
            baseSpeed = 0f;
            properties.add(Property.IMMOVABLE);
            properties.add(Property.TUMULUS);
            properties.add(Property.INORGANIC);
            properties.add(Property.NOKOCK);
        }

        // ==========配置常量==========
        private static final int SHIELD_BASE = 15;
        private static final int SHIELD_MAX_MULTIPLIER = 4;
        private static final int AURA_RADIUS = 4;        //9x9光环
        private static final int SUMMON_RADIUS = 3;      //7x7召唤投放区域
        private static final int SUMMON_CD = 10;
        private static final int DEATH_BUFF_DEC = 10;

        private int summonTimer = 0;
        private boolean firstSummon = false;
        private int nextSummonPos = -1;

        private static final String TAG_SUMMON_TIMER = "summonTimer";
        private static final String TAG_FIRST_SUMMON = "firstSummon";
        private static final String TAG_NEXT_SPAWN = "nextSummonPos";

        //精准0
        @Override
        public int attackSkill(Char target) {
            return 0;
        }

        //移动速度0
        @Override
        public float speed() {
            return 0f;
        }

        @Override
        public void move(int step) {

        }

        @Override
        protected boolean act() {

            // 光环1：玩家死亡侵蚀最低锁定30%
            DeathBuff buff = hero.buff(DeathBuff.class);
            if (buff != null && buff.level < 30) {
                buff.level = 30;
                buff.decayTimer = 0;
            } else {
                Buff.affect(hero, DeathBuff.class).set((2), 5);
            }

            // 光环2：获取9x9半径4范围内所有单位，给亡灵附加护盾
            List<Char> auraChars = new ArrayList<>();
            for (int dx = -AURA_RADIUS; dx <= AURA_RADIUS; dx++){
                for (int dy = -AURA_RADIUS; dy <= AURA_RADIUS; dy++){
                    int cell = pos + dx + dy * Dungeon.level.width();
                    if (Dungeon.level.insideMap(cell)){
                        Char c = Actor.findChar(cell);
                        if (c != null){
                            auraChars.add(c);
                        }
                    }
                }
            }
            for (Char ch : auraChars) {
                if (ch instanceof Mob) {
                    Mob mob = (Mob) ch;
                    if (mob.properties().contains(Property.UNDEAD) || mob.properties().contains(Property.NECRO)) {
                        Barrier barrier = mob.buff(Barrier.class);
                        if (barrier == null) {
                            barrier = Buff.affect(mob, Barrier.class);
                        }
                        int maxShield = SHIELD_BASE * SHIELD_MAX_MULTIPLIER;
                        barrier.setShield(Math.min(barrier.shielding() + SHIELD_BASE, maxShield));
                    }
                }
            }

            boolean hasTarget = enemy != null && enemy.isAlive();
            boolean enemyTarget = enemy != null && Dungeon.level.distance(pos,enemy.pos)<=viewDistance;
            if (hasTarget && enemyTarget) {
                if (!firstSummon) {
                    // 初次索敌：立刻准备召唤，预警标记
                    nextSummonPos = pickSpawnCell();
                    if (nextSummonPos != -1){
                        triggerWarningEffect(nextSummonPos);
                    }
                    summonEnemy();
                    firstSummon = true;
                    summonTimer = SUMMON_CD;
                } else {
                    summonTimer--;
                    //倒计时剩余2回合 预警目标格子
                    if (summonTimer == 2 && nextSummonPos == -1) {
                        nextSummonPos = pickSpawnCell();
                        if (nextSummonPos != -1) {
                            triggerWarningEffect(nextSummonPos);
                        }
                    }
                    if (summonTimer <= 0) {
                        summonEnemy();
                        summonTimer = SUMMON_CD;
                        nextSummonPos = -1;
                    }
                }
            } else {
                // 失去目标，停止召唤，重置状态
                firstSummon = false;
                summonTimer = 0;
                nextSummonPos = -1;
            }

            spend(TICK);
            return true;
        }

        private void triggerWarningEffect(int cellPos){
            sprite.parent.add(new ColorTargetedCell(cellPos, Window.CYELLOW));
        }

        // 仅挑选可用出生格子（不生成怪物，只获取坐标用于预警）
        private int pickSpawnCell() {
            ArrayList<Integer> spawnCells = new ArrayList<>();
            for (int cell = 0; cell < Dungeon.level.length(); cell++) {
                if (Dungeon.level.insideMap(cell)
                        && Dungeon.level.passable[cell]
                        && Actor.findChar(cell) == null
                        && Dungeon.level.distance(pos, cell) <= SUMMON_RADIUS) {
                    spawnCells.add(cell);
                }
            }
            if (spawnCells.isEmpty()) return -1;
            return Random.element(spawnCells);
        }



        /**
         * 召唤机制：优先拖拽塔7x7范围之外的死灵，若无则生成全新死灵投放至7x7内
         */
        private void summonEnemy() {
            int spawnPos;
            if(nextSummonPos != -1
                    && Dungeon.level.passable[nextSummonPos]
                    && Actor.findChar(nextSummonPos) == null){
                spawnPos = nextSummonPos;
            }else {
                spawnPos = pickSpawnCell();
            }
            if (spawnPos == -1) return;

            // 寻找：距离塔 >3 的亡灵（7×7范围外）
            Mob targetMob = null;
            for (Mob mob : Dungeon.level.mobs){
                if (mob != this
                        && mob.isAlive()
                        && (mob.properties().contains(Property.UNDEAD) || mob.properties().contains(Property.NECRO))
                        && Dungeon.level.distance(pos, mob.pos) > SUMMON_RADIUS){

                    targetMob = mob;
                }
            }

            if (targetMob != null){
                // 找到外部死灵 → 传送进圈
                ScrollOfTeleportation.appear(targetMob, spawnPos);
                sprite.parent.add(new Beam.DeathRayS(sprite.center(), targetMob.sprite.center()));
            } else {
                Mob undead = createRandomUndead();
                undead.pos = spawnPos;
                GameScene.add(undead);
                ScrollOfTeleportation.appear(undead,spawnPos);
                sprite.parent.add(new Beam.DeathRayS(sprite.center(), undead.sprite.center()));
                Dungeon.level.occupyCell(undead);
            }
        }

        private Mob createRandomUndead() {
            int roll = Random.Int(9);
            switch (roll) {
                case 1: return new NecroAcolyte();
                case 2: return new Wisp();
                case 3: return new NecroCavalry();
                case 4: return new NecroGuard();
                case 5: return new NecroPioneer();
                case 6: return new NecroWarlock();
                case 7: return new SkeletonDemon();
                case 8: return new Aggregatus();
                default:
                    return new NecroArcher();
            }
        }

        @Override
        public void die(Object cause) {
            super.die(cause);

            DeathBuff buff = hero.buff(DeathBuff.class);
            if (buff != null) {
                buff.level -= 10;
            }

            Item drop;
            if (Random.Boolean()) {
                drop = new ScrollOfTransmutation();
            } else {
                drop = new PotionOfExperience();
            }
            Dungeon.level.drop(drop, pos).sprite.drop();
        }

        private int drDroll = Dungeon.depth;
        private int getDamageDroll;

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(TAG_SUMMON_TIMER, summonTimer);
            bundle.put(TAG_FIRST_SUMMON, firstSummon);
            bundle.put(TAG_NEXT_SPAWN, nextSummonPos);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            summonTimer = bundle.getInt(TAG_SUMMON_TIMER);
            firstSummon = bundle.getBoolean(TAG_FIRST_SUMMON);
            nextSummonPos = bundle.getInt(TAG_NEXT_SPAWN);
        }

        public void getDamageDroll(int dr){
            getDamageDroll += dr;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0,drDroll-getDamageDroll);
        }
    }

    protected void placeDeathSpire( Level level ) {
        int deadPos = (top + 8) * level.width() + left + 8;
        Mob n = new DeadTower();
        n.pos = deadPos;
        level.mobs.add(n);
    }

    @Override
    public void paint(Level level) {
        super.paint(level);
        placeDeathSpire(level);
    }

    @Override
    public boolean canConnect(Point p) {
        int midX = left + 8;
        int midY = top + 8;
        if (p.x == midX && p.y == top) return true;
        if (p.x == midX && p.y == bottom) return true;
        if (p.x == left && p.y == midY) return true;
        return p.x == right && p.y == midY;
    }

}
