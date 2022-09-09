package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public class Cost extends Buff {



    {
        type = buffType.POSITIVE;
    }

    public static int cost = 0;
    private int interval = 1;



    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend(interval);

            if (cost == 0) {
                detach();
            }

        }

        return true;
    }

    public int level() {
        return cost;
    }

    public void set( int value, int time ) {
        //decide whether to override, preferring high value + low interval
        if (Math.sqrt(interval)*cost <= Math.sqrt(time)*value) {
            cost = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    @Override
    public float iconFadePercent() {
        if (target instanceof Hero){
            float max = ((Hero) target).lvl;
            return Math.max(0, (max-cost)/max);
        }
        return 0;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", cost, dispTurns(visualcooldown()));
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, cost );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        cost = bundle.getInt( LEVEL );
    }

}

