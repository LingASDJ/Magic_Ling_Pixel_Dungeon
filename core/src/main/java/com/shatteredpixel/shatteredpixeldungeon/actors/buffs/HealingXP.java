package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class HealingXP extends Healing{

    @Override
    public int icon() {
        return BuffIndicator.HEALING;
    }
}
