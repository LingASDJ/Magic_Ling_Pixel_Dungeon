package com.shatteredpixel.shatteredpixeldungeon.custom.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;

public class FlavourBuffOpen extends FlavourBuff {
    public void setDuration(float time){
        spend(time);
    }
}
