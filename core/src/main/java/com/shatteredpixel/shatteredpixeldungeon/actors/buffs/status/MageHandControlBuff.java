package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.utils.Bundle;

public class MageHandControlBuff extends Buff implements ActionIndicator.Action {
    public MagesStaff.MageHandControl magesStaffcontrol;
    {
        type = buffType.POSITIVE;
    }


    @Override
    public String actionName() {
        return Messages.get(MagesStaff.MageHandControl.class,MagesStaff.MageHandControl.AC_HAND);
    }

    @Override
    public boolean act() {

        if(magesStaffcontrol ==  null){
             magesStaffcontrol = Dungeon.hero.belongings.getItem(MagesStaff.MageHandControl.class);
        } else {
            magesStaffcontrol.updateQuickslot();
        }

        spend(TICK);
        ActionIndicator.setAction(this);
        return true;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        ActionIndicator.setAction(this);
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public int actionIcon() {
        return HeroIcon.BATTLEMAGE;
    }

    @Override
    public int indicatorColor() {
        return 0x6B2D95;
    }

    @Override
    public void doAction() {
        if(magesStaffcontrol != null) {
            magesStaffcontrol.execute(Dungeon.hero, MagesStaff.MageHandControl.AC_HAND);
        }
    }
}
