package com.shatteredpixel.shatteredpixeldungeon.custom.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class PositiveBuffProhibition extends FlavourBuff {
    {
        type = buffType.NEUTRAL;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.DEGRADE;
    }

    @Override
    public void tintIcon(Image icon) {
        super.tintIcon(icon);
        icon.hardlight(0f, 1f, 0f);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

    @Override
    public boolean attachTo(Char target) {
        for(Buff b: target.buffs()){
            if(b.type == buffType.POSITIVE && !b.revivePersists){
                b.detach();
            }
        }
        return super.attachTo(target);
    }
}
