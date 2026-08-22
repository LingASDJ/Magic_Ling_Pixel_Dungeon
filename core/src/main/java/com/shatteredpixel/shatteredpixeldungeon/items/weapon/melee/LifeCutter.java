package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

//断生者
//四阶，力量需求17
//初始6-24，成长2-6
//每次命中都会让敌人一分为二：本体与分身各继承当前生命值与生命上限的一半。

public class LifeCutter extends MeleeWeapon{
    {
        image = ItemSpriteSheet.SKIN_5;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
    }

    @Override
    public int STRReq(int lvl){
        int req = STRReq(tier, lvl)+1;
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    @Override
    public int max(int lvl) { return 24 + lvl * 6; }

    @Override
    public int min(int lvl) { return 6 + lvl * 2; }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 命中即标记目标：真正的分裂在伤害结算（Char.damage）后进行
        if (defender instanceof Mob
                && defender.alignment == Char.Alignment.ENEMY
                && !Char.hasProp(defender, Char.Property.BOSS)
                && !Char.hasProp(defender, Char.Property.MINIBOSS)){
            Buff.affect(defender, SplitMark.class);
        }
        return super.proc(attacker, defender, damage);
    }

    // 伤害结算（Char.damage）后调用：目标仍存活才分裂，本体与分身各继承一半血量和血量上限
    public static void trySplit(Char defender, Object src){
        if (!defender.isAlive()
                || !(defender instanceof Mob)
                || defender.alignment != Char.Alignment.ENEMY
                || Char.hasProp(defender, Char.Property.BOSS)
                || Char.hasProp(defender, Char.Property.MINIBOSS)
                || defender.HP < 2){
            return;
        }

        int cell = freeCellNear(defender);
        if (cell == -1){
            return;
        }

        try {
            int halfHP = defender.HP / 2;
            int halfHT = defender.HT / 2;
            defender.HP = halfHP;
            defender.HT = halfHT;

            Mob copy = Reflection.newInstance(((Mob) defender).getClass());
            copy.HP = halfHP;
            copy.HT = halfHT;
            copy.isEndLess = ((Mob) defender).isEndLess;

            GameScene.add(copy);
            ScrollOfTeleportation.appear(copy, cell);
            if (src instanceof Char){
                copy.aggro((Char) src);
            }
        } catch (Exception ignored) {
            // 遇到无法复制的特殊敌人时，静默跳过分裂
        }
    }

    // 在敌人周围找一个可站立的空格，找不到返回 -1
    private static int freeCellNear(Char ch){
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int n : PathFinder.NEIGHBOURS8) {
            int cell = ch.pos + n;
            if (Dungeon.level.passable[cell]
                    && Actor.findChar(cell) == null
                    && (!Char.hasProp(ch, Char.Property.LARGE) || Dungeon.level.openSpace[cell])) {
                candidates.add(cell);
            }
        }
        return candidates.size() > 0 ? Random.element(candidates) : -1;
    }

    // 一次性分裂标记：挂在目标身上，伤害结算后消费
    public static class SplitMark extends Buff {
    }
}