package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MageHand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class MageHandControlBuff extends Buff implements ActionIndicator.Action {
    public MageHand.MageHandControl magesStaffcontrol;
    {
        type = buffType.POSITIVE;
    }


    @Override
    public String actionName() {
        return Messages.get(MageHand.MageHandControl.class,MageHand.MageHandControl.AC_HAND);
    }

    @Override
    public boolean act() {

        if(magesStaffcontrol ==  null){
             magesStaffcontrol = Dungeon.hero.belongings.getItem(MageHand.MageHandControl.class);
        } else {
            magesStaffcontrol.updateQuickslot();
        }

        spend(TICK);
        ActionIndicator.setAction(this);
        return true;
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
            magesStaffcontrol.execute(Dungeon.hero, MageHand.MageHandControl.AC_HAND);
        }
    }
}
