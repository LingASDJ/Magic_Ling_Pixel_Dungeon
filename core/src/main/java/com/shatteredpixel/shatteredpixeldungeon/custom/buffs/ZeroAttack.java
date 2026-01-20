package com.shatteredpixel.shatteredpixeldungeon.custom.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class ZeroAttack extends FlavourBuff {
    {
        type = buffType.NEUTRAL;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.WEAKNESS;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(.5f, .5f, .5f);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }
}
