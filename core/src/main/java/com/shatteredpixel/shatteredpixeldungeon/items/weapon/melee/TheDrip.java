package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

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
        Buff.detach(hero, HolderTracker.class); // 卸下自动移除
        return super.doUnequip(hero, collect, single);
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