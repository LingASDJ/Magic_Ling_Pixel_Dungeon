package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.watabou.utils.Bundle;

public abstract class ElementalBuff extends Buff {

    public static int level = 0;

    public int Scary = 1;

    public int elementalIcon;

    {
        type = buffType.POSITIVE;
    }
    private int interval = 1;

    public int level() {
        return level;
    }

    public void set( int value, int time ) {
        //decide whether to override, preferring high value + low interval
        if (Math.sqrt(interval)*level <= Math.sqrt(time)*value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }
    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend(interval);
            if (level <= 0) {
                detach();
            }

        }

        return true;
    }

    @Override
    public int icon() {
        return elementalIcon;
    }

    @Override
    public float iconFadePercent() {
        if (target instanceof Hero){
            float max = ((Hero) target).lvl;
            return Math.max(0, (max-level)/max);
        }
        return 0;
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("ScaryINT",Scary);
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        Scary = bundle.getInt("ScaryINT");
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
    }

}

