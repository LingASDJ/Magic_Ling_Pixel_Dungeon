package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public class SelectFoor extends Buff{

    //the amount of turns remaining before beneficial passive effects turn off
    private float left = 50; //starts at 50 turns

    @Override
    public boolean act() {
        spend(TICK);



        if (left >= 1)
            left --;

        return true;
    }

    public void addTime(float time){
        left += time;
    }

    public boolean regenOn(){
        return left >= 1;
    }

    private final String LEFT = "left";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( LEFT, left );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        left = bundle.getFloat( LEFT );
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

}
