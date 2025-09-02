package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class AbundantMagic extends FlavourBuff{
    public static final float DURATION	= 5f;
    {
        type = buffType.POSITIVE;
    }

    @Override
    public int icon() {
        return BuffIndicator.WAND_MAGIC;
    }
    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }
}
