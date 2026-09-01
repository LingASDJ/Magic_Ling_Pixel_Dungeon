package com.shatteredpixel.shatteredpixeldungeon.custom.utils.QingXian_ability_utils;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireMagicDied;

public class BuffClear {
    //  TODO 设计组：完成 BuffLevel 设计后，在调用处传入真实净化等级
    public static final int CLEAR_LEVEL = 0;

    //  POSITIVE, NEGATIVE, NEUTRAL 分别表示 正面buff 负面buff 中性buff
    //  不知道返回值有什么用，但先写上

    // 大净化术！
    public static boolean BigClean(Char defender, int BuffLevel){
        for (Buff b : defender.buffs()){
            if ( !(b instanceof ShieldBuff && defender instanceof FireMagicDied)
                    && b.CanClear(BuffLevel)){
                b.detach();
            }
        }
        return true;
    }

    // 负面效果净化
    public static boolean NegativeClean(Char defender, int BuffLevel){
        for (Buff b : defender.buffs()){
            if (b.type == Buff.buffType.NEGATIVE && b.CanClear(BuffLevel)){
                b.detach();
            }
        }
        return true;
    }

    // 正面效果净化
    public static boolean PositiveClean(Char defender, int BuffLevel){
        for (Buff b : defender.buffs()){
            if (b.type == Buff.buffType.POSITIVE && b.CanClear(BuffLevel)){
                b.detach();
            }
        }
        return true;
    }

    // 中性效果净化
    public static boolean NeutralClean(Char defender, int BuffLevel){
        for (Buff b : defender.buffs()){
            if (b.type == Buff.buffType.NEUTRAL && b.CanClear(BuffLevel)){
                b.detach();
            }
        }
        return true;
    }
}
