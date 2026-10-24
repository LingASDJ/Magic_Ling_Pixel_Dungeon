package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class Revealed extends FlavourBuff {

    public static final float DURATION = 5f;

    {
        type = buffType.NEGATIVE;
    }

    @Override
    public int icon() {
        return BuffIndicator.MIND_VISION;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 0.4f, 0.4f);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(
                0,
                (DURATION - visualcooldown()) / DURATION
        );
    }
}