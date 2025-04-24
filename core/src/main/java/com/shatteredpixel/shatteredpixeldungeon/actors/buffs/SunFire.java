package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfSun;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class SunFire extends Buff{

    {
        type = buffType.NEGATIVE;
    }

    public WandOfSun.MiniSun source;
    public float duration = 2;

    @Override
    public boolean act(){
        duration--;
        spend(TICK);
        if(duration==0) detach();
        return super.act();
    }

    @Override
    public int icon() {
        return BuffIndicator.LIGHT;
    }

    private static final String DURATION = "duration";
    private static final String MINISUN = "minisun";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(DURATION,duration);
        bundle.put(MINISUN,source);
    }

    @Override
    public void restoreFromBundle( Bundle bundle){
        super.restoreFromBundle(bundle);
        source = (WandOfSun.MiniSun) bundle.get(MINISUN);
        duration = bundle.getFloat(DURATION);
    }
}
