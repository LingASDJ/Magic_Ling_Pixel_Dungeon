package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BuffsOringinForWeapon;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;

// 浮空目标防御忽视buff
// buff持有者攻击时忽略浮空目标一定百分比的防御数值
public class FloatingTargetDefenseIgnore extends Buff {
    // 忽略的百分比
    public float DefenseIgnoreRate=0.5f;

    @Override
    public boolean act() {
        spend(TICK);
        return true;
    }
}
