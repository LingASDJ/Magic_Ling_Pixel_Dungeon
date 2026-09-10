package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

//滴落者
//三阶,力量需求14
//初始5-20,成长1-4
//当你连续在同一格内停留2回合,将此格淹没为水地块。
public class TheDrip extends MeleeWeapon {

    {
        image = ItemSpriteSheet.WATER_SWORD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;
        tier = 3;
    }

    @Override
    public int max(int lvl) { return 20 + lvl * 4; }

    @Override
    public int min(int lvl) { return 5 + lvl; }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        Buff.affect(ch, HolderTracker.class);   // 装备上自动挂
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        boolean result = super.doUnequip(hero, collect, single);
        if (result) {
            Buff.detach(hero, HolderTracker.class);  // 卸掉自动删buff
        }
        return result;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 持械怪第一次攻击也会走到这里,attacker 就是怪物本身,同样挂上追踪
        Buff.affect(attacker, HolderTracker.class);
        return super.proc(attacker, defender, damage);
    }

    public static boolean createWater(int cell) {
        // 检测地形为可水化地形
        if (!Dungeon.level.insideMap(cell)) return false;
        if (Dungeon.level.solid[cell])      return false;
        if (Dungeon.level.pit[cell])        return false;
        if (Dungeon.level.map[cell] == Terrain.CHASM) return false;
        if (Dungeon.level.water[cell])      return false;
        if(Dungeon.level.exit() == cell || Dungeon.level.entrance() == cell ) return false;

        Level.set(cell, Terrain.WATER);     // 改地形 + 碰撞数据
        GameScene.updateMap(cell);          // 刷新贴图
        Dungeon.observe();
        return true;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    // 基础消耗3，连续使用递减，最低1
    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        ConsecutiveCastTracker tracker = hero.buff(ConsecutiveCastTracker.class);
        int base = 3;
        if (tracker != null) {
            int cost = base - tracker.stack;
            return Math.max(1, cost);
        }
        return base;
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {


        if (target == null) {
            return;
        }

        int dist = Dungeon.level.distance(hero.pos, target);
        if (dist > 5){
            GLog.w(Messages.get(this, "ability_target_range"));
            hero.belongings.abilityWeapon = null;
            return;
        }

        // 标记连续释放buff
        ConsecutiveCastTracker tracker = Buff.affect(hero, ConsecutiveCastTracker.class);
        tracker.stack++;

        ArrayList<Integer> waterCells = new ArrayList<>();
        // 目标点 3x3
        for (int off : PathFinder.NEIGHBOURS9) {
            int c = target + off;
            if (!Dungeon.level.insideMap(c)) continue;
            if (createWater(c)) {
                waterCells.add(c);
            }
            // 范围内单位麻痹3回合
            Char ch = Dungeon.level.findMob(c);
            if (ch != null && ch.isAlive()) {
                Buff.prolong(ch, Paralysis.class, 3f);
            }
        }

        // 统计本3*3内原本就存在的水地块数量
        int existingWaterCount = 0;
        for (int off : PathFinder.NEIGHBOURS9) {
            int c = target + off;
            if (Dungeon.level.insideMap(c) && Dungeon.level.water[c]) {
                existingWaterCount++;
            }
        }

        // 每1格原有水，联通水域随机向外蔓延1格
        for (int i = 0; i < existingWaterCount; i++) {
            ArrayList<Integer> connectedWater = new ArrayList<>();
            // 先找出所有连通水地块（BFS简易版）
            boolean[] visited = new boolean[Dungeon.level.length()];
            ArrayList<Integer> queue = new ArrayList<>();
            for(int c : waterCells){
                if(Dungeon.level.water[c]) queue.add(c);
            }
            while (!queue.isEmpty()){
                int curr = queue.remove(0);
                if(visited[curr]) continue;
                visited[curr] = true;
                connectedWater.add(curr);
                for(int n : PathFinder.NEIGHBOURS8){
                    int nc = curr + n;
                    if(Dungeon.level.insideMap(nc) && Dungeon.level.water[nc] && !visited[nc]){
                        queue.add(nc);
                    }
                }
            }
            // 随机选一个连通水域格子，随机方向尝试扩展一格
            if (!connectedWater.isEmpty()){
                int src = Random.element(connectedWater);
                ArrayList<Integer> dirList = new ArrayList<>();
                for(int d : PathFinder.NEIGHBOURS8){
                    dirList.add(d);
                }
                Random.shuffle(dirList);
                for(int d : dirList){
                    int expandCell = src + d;
                    if(createWater(expandCell)){
                        waterCells.add(expandCell);
                        break;
                    }
                }

            }
        }
        Splash.at( DungeonTilemap.tileCenterToWorld( target ), -PointF.PI/2, PointF.PI/2, 0x5bc1e3, 100, 0.01f);
        Sample.INSTANCE.play(Assets.Sounds.GAS, 1f, 0.75f);
        beforeAbilityUsed(hero,null);
        hero.spendAndNext(hero.attackDelay());
        afterAbilityUsed(hero);
    }

    // 追踪连续释放层数，用来减少充能消耗
    public static class ConsecutiveCastTracker extends Buff {
        public int stack = 0;

        // 回合结束重置：不释放武技则层数清零
        @Override
        public boolean act() {
            stack = 0;
            detach();
            spend(TICK);
            return true;
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put("stack", stack);
        }
        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            stack = bundle.getInt("stack");
        }
    }

    public static class HolderTracker extends Buff {

        private int lastCell = -1;  // 上一回合的格子
        private int turns = 0;      // 连续停留回合数

        @Override
        public boolean act() {
            if (!target.isAlive()) { detach(); return true; }

            int cell = target.pos;
            if (cell == lastCell) {
                turns++;
                // 连续第2回合仍停留 → 生成水,成功后复位
                if (turns >= 2 && createWater(cell) ) {
                    turns = 0;
                }
            } else {
                lastCell = cell;
                turns = 0;
            }

            spend(TICK);
            return true;
        }
    }
}
