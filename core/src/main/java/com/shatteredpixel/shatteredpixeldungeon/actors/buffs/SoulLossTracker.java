package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.watabou.utils.Bundle;

public class SoulLossTracker extends Buff {

    {
        type = buffType.NEUTRAL;
    }

    private int progress = 0;
    private int lastHP;

    @Override
    public boolean act() {
        if (target.isAlive()) {
            if (lastHP > target.HP) {
                progress += lastHP - target.HP;
                while (progress >= 20) {
                    progress -= 20;
                    LostSoul soul = new LostSoul();   // 新建实例叠层
                    soul.attachTo(target);
                }
            }
            lastHP = target.HP;
        }
        spend(TICK);
        return true;
    }

    private static final String PROGRESS = "progress";
    private static final String LAST_HP = "lastHP";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PROGRESS, progress);
        bundle.put(LAST_HP, lastHP);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        progress = bundle.getInt(PROGRESS);
        lastHP = bundle.getInt(LAST_HP);
    }
}