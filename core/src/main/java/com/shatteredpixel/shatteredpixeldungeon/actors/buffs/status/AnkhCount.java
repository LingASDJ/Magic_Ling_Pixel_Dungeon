package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class AnkhCount extends Buff {

    public static class AnkhCountStats extends AnkhCount{
        {
            type = buffType.NEGATIVE;
        }
    }

    {
        type = Buff.buffType.POSITIVE;
    }

    private int level = 0;
    private int interval = 1;

    private int ankhs = 0;


    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend( interval );
            if (--level <= 0) {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    @Override
    public void detach() {
       super.detach();
        for (int i = 0; i < level; i++) {
            Dungeon.level.drop(new Ankh(),target.pos);
        }
    }

    public int level() {
        return level;
    }

    public void set( int value, int time) {
        if (level <= value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
          }
    }

    @Override
    public int icon() {
        return BuffIndicator.FIREDIED;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    private static final String ID = "id";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
        bundle.put( ID, ankhs );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
        ankhs = bundle.getInt( ID );
    }
}

