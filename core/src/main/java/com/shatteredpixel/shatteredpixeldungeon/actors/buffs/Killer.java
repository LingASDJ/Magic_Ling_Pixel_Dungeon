package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Killer extends Buff{
    {
        type = buffType.POSITIVE;
    }

    public int duration = 30;

    @Override
    public boolean act() {

        spend(TICK);
        duration--;

        if(duration<=0){
            detach();
        }
        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.KILLER;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 0.8f, 0f);
    }

    @Override
    public String desc() {
        return Messages.get(Killer.class,"desc",duration);
    }

    private static final String DURATION = "duration";
    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(DURATION,duration);
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        duration = bundle.getInt(DURATION);
        super.restoreFromBundle(bundle);
    }
}
