package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class PureSoul extends Buff {
    public int soul() {
        return (int)Math.ceil(soul);
    }

    public static final float PURESOULBASE	= 20f;

    {
        type = buffType.POSITIVE;
    }
    public int pos;
    public static int soul = 0;
    private int interval = 1;

    @Override
    public boolean act() {
        if (target.isAlive()) {
            spend(interval);
            if (--soul <= 0) {
                detach();
            }
        }
        return true;
    }

    public void set( int value, int time ) {
        if (Math.sqrt(interval)*soul <= Math.sqrt(time)*value) {
            soul = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    public static final float STARVING	= 240f;
    @Override
    public float iconFadePercent() {
        return Math.max(0, (Sanity.STARVING - soul) / Sanity.STARVING);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
        //if (soul >= 180) {
        //
        //} else if (sanity >= 120) {
        //    return Messages.get(this, "name2");
        //} else {
        //    return Messages.get(this, "name3");
        //}
    }



    @Override
    public String desc() {
        return Messages.get(this, "normal_desc", soul, dispTurns(visualcooldown()));
       //if (sanity >= 180) {
       //    return Messages.get(this, "normal_desc", sanity, dispTurns(visualcooldown()));
       //} else if (sanity >= 120) {
       //    return Messages.get(this, "crazy_desc", sanity, dispTurns(visualcooldown()));
       //} else {
       //    return Messages.get(this, "died_desc", sanity, dispTurns(visualcooldown()));
       //}
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( LEVEL, soul );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        soul = bundle.getInt(LEVEL);
    }


    @Override
    public void tintIcon(Image icon) {

        //if (soul >= 180) {
        //    icon.hardlight(0x00ff00);
        //} else if (soul >= 120) {
        //    icon.hardlight(0xffff00);
        //} else {
        //    icon.hardlight(0xff0000);
        //}
    }//

}

