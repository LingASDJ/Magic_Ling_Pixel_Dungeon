package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class FoundChest extends Buff {
    public int distance = 2;
    {
        type = buffType.NEUTRAL;
    }

    private int level = 0;
    private int interval = 1;

    public int NoLoot = 0;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend( interval );
            if (level <= 0) {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    public int level() {
        return level;
    }

    public void set( int value, int time ) {
        if (level <= value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.DK;
    }


    @Override
    public String desc() {
        return Messages.get(this, "desc", Math.min(Statistics.KillMazeMimic,5));
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";
    private static final String NOLOOT    = "noloot";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
        bundle.put( NOLOOT, NoLoot );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
        NoLoot = bundle.getInt(NOLOOT);
    }
}

