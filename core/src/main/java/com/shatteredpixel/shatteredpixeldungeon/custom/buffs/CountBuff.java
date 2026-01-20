package com.shatteredpixel.shatteredpixeldungeon.custom.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.watabou.utils.Bundle;

public class CountBuff extends Buff {

    private float count = 0;

    public void countUp( float inc ){
        count += inc;
    }

    public void countDown( float inc ){
        count -= inc;
    }

    public float count(){
        return count;
    }

    private static final String COUNT = "count";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COUNT, count);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        count = bundle.getFloat(COUNT);
    }

}
