package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;

//钉入矛
//五阶，力量需求18
//初始4-30，成长1-5，攻击距离2
//若目标敌人的3*3范围内有墙地块，则攻击它造成的伤害提升50%。
//能够狠狠的把敌人钉在墙里。
public class NailingSpear extends MeleeWeapon{
    {
        image = ItemSpriteSheet.SKIN_5;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        RCH  = 2;

        tier = 5;
    }

    @Override
    public int max(int lvl) { return 30 + lvl * 5; }

    @Override
    public int min(int lvl) { return 4 + lvl; }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 目标 3*3 范围内有墙地块时，伤害 +50% 四舍五入
        if (nearWall(defender)) {
            damage = Math.round(damage * 1.5f);
        }
        return super.proc(attacker, defender, damage);
    }

    // 目标 3*3 范围内是否存在墙地块
    private boolean nearWall(Char defender) {
        for (int i : PathFinder.NEIGHBOURS9) {
            int pos = defender.pos + i;
            if (Dungeon.level.insideMap(pos)) {
                int terrain = Dungeon.level.map[pos];
                if (terrain == Terrain.WALL || terrain == Terrain.WALL_DECO) {
                    return true;
                }
            }
        }
        return false;
    }
}
