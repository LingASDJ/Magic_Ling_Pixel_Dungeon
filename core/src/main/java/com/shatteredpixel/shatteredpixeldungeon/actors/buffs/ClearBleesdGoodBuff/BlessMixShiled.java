package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class BlessMixShiled extends Buff {

    {
        type = buffType.POSITIVE;
    }

    public static int level = 0;
    private int interval = 1;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            if(Dungeon.hero.buff(BlessMixShiled.class) != null && hero.HT == hero.HP){
                if(Dungeon.depth <= 5) {
                    Buff.affect(hero, Barrier.class).setShield(((30)));
                } else {
                    Buff.affect(hero, Barrier.class).setShield(((30) * Dungeon.depth / 5));
                }
                spend(300f);
                GLog.w("你的血量已满，奖励你一些护盾值。");
            }

            spend(interval);
            if (level <= 0) {

                detach();
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

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0x00ffff);
    }

    @Override
    public int icon() {
        return BuffIndicator.GOBUFF_UPRD;
    }


}



