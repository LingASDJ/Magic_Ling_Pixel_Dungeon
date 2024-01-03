package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class BlessGoRead extends ClearLanterBuff {

    {
        type = buffType.POSITIVE;
    }

    public static int level = 0;
    private int interval = 1;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend(interval);
            if (level <= 0) {
                detach();
            }
            if(hero.lanterfire > 90 && !Statistics.noGoReadHungry) {
                //effectively 1HP at lvl 0-5, 2HP lvl 6-8, 3HP lvl 9, and 5HP lvl 10.
                target.HP = Math.min( target.HT, target.HP + 2);
                spend(3f);
            }


        }

        return true;
    }

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
    public float iconFadePercent() {
        if (target instanceof Hero){
            float max = ((Hero) target).lvl;
            return Math.max(0, (max-level)/max);
        }
        return 0;
    }

    @Override
    public String toString() {
            return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        if(hero.lanterfire<90 && !Statistics.noGoReadHungry){
            return Messages.get(this, "desc_lanter");
        } else if (Statistics.noGoReadHungry) {
            return Messages.get(this, "desc_rest");
        } else
            return Messages.get(this, "desc");

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

    @Override
    public void tintIcon(Image icon) {

        if(hero.lanterfire<90 && !Statistics.noGoReadHungry){
            icon.hardlight(Window.CYELLOW);
        } else if (Statistics.noGoReadHungry) {
            icon.hardlight(0x993333);
        } else
            icon.hardlight(0xFF1493);
    }

    @Override
    public int icon() {

        if(hero.lanterfire<90 && !Statistics.noGoReadHungry){
            return BuffIndicator.DEBUFF_DOWN;
        } else if (Statistics.noGoReadHungry) {
            return BuffIndicator.DEBUFF_DOWN;
        } else
            return BuffIndicator.GOBUFF_UPRD;
    }


}

