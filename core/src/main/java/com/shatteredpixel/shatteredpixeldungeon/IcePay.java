package com.shatteredpixel.shatteredpixeldungeon;

import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public class IcePay implements Bundlable {
    public static boolean rip = false;

    public final String RIPACTIVE  = "ripactive";

    @Override
    public void restoreFromBundle(Bundle bundle) {
       rip = bundle.getBoolean(RIPACTIVE);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(RIPACTIVE,rip);
    }
}
