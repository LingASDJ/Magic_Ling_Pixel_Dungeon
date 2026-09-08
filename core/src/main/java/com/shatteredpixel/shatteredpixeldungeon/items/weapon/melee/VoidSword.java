package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.MiningLevel;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

//虚空剑
//四阶，力量需求15
//初始6-30，成长2-5
//在进行攻击后，立刻将视野内可达位置中的一名随机敌人吸引至身前。
//这把剑身中传来强大的吸引力，仿佛你正置身死寂的黑洞之中。
//武技：奇点坍缩，消耗10充能，在指定位置放置1个奇点，奇点会持续定身9*9范围内的敌人，并且每回合会使范围内的所有敌人和物品向奇点方向强制位移一格。
//奇点每回合都会摧毁与他相邻或重叠的物品，接触奇点的非boss单位会直接死亡，boss单位会受到20%最大生命值的伤害随后摧毁奇点。
public class VoidSword extends MeleeWeapon{
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
            req -= 3;
        }
        return req;
    }

    // ========== 武器特效实现 ==========
    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Attraction(attacker, defender);
        return super.proc(attacker, defender, damage);
    }

    // 武器特效的吸引效果
    public void Attraction(Char attacker, Char defender) {
        PathFinder.buildDistanceMap(defender.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
        if (!(Dungeon.level instanceof MiningLevel)
                && PathFinder.distance[curUser.pos] == Integer.MAX_VALUE) {
            return;
        }
    }

    // ========== 武技实现 ==========

}
