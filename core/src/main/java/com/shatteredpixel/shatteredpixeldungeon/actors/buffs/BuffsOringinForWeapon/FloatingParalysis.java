package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BuffsOringinForWeapon;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;

// 浮空麻痹buff，使得buff持有者的敌对目标在浮空时持续麻痹
public class FloatingParalysis extends Buff {
    {
        type = buffType.POSITIVE;
        actPriority = HERO_PRIO - 1;
    }

    @Override
    public boolean act() {
        if (target != null && Dungeon.level != null) {
            for (Char ch : Actor.chars()) {
                if (ch == target
                        || ch.alignment == target.alignment
                        || ch.buff(Levitation.class) == null) continue;
                Buff.prolong(ch, Paralysis.class, 1f);
            }
        }
        spend(TICK);
        return true;    }
}
