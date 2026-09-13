package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BuffsOringinForWeapon;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class DoomsdayScepterVulnerable extends FlavourBuff {
    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    public int icon() {
        return BuffIndicator.CORRUPT;
    }
}
