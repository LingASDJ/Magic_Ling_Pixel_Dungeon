package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class FireSuperDr extends Buff {

    {
        type = buffType.POSITIVE;
    }

    private int level = 0;
    private int interval = 1;

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
        return BuffIndicator.ARMOR;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(Window.TITLE_COLOR)  ;
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(level);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
    }

    //These two methods allow for multiple instances of FireSuperDr  to stack in terms of duration
    // but only the stronger bonus is applied

    public static int currentLevel(Char ch ){
        int level = 0;
        for (FireSuperDr  b : ch.buffs(FireSuperDr .class)){
            level = Math.max(level, b.level);
        }
        return level;
    }

    //reset if a matching buff exists, otherwise append
    public static void conditionallyAppend(Char ch, int level, int interval){
        for (FireSuperDr  b : ch.buffs(FireSuperDr .class)){
            if (b.interval == interval){
                b.set(level, interval);
                return;
            }
        }
        Buff.append(ch, FireSuperDr .class).set(level, interval);
    }
}

