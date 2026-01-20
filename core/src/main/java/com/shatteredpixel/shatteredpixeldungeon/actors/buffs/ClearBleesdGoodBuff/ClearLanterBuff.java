package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;

abstract public class ClearLanterBuff extends Buff {

    {
        type = buffType.POSITIVE;
    }

    @Override
    public String heroMessage() {
        return "";
    }
}
