package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class GoodLuck extends FlavourBuff {

    {
        type = buffType.POSITIVE;
    }

    public static final float DURATION	= 30f;

    @Override
    public int icon() {
        return BuffIndicator.HASTE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 0.8f, 0f);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

}
