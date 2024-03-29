package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class BlessImmune extends ClearLanterBuff {

    {
        type = buffType.POSITIVE;
        immunities.addAll(AntiMagic.RESISTS);
    }

    public static int level = 0;
    private int interval = 1;

    public void detach() {
        super.detach();
        immunities.removeAll(AntiMagic.RESISTS);
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
    public boolean attachTo(Char target) {
        if (super.attachTo(target)){
            for (Buff b : target.buffs()){
                for (Class immunity : immunities){
                    if (b.getClass().isAssignableFrom(immunity)){
                        b.detach();
                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
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
        icon.hardlight(0xD2691E);
    }

    @Override
    public int icon() {
        return BuffIndicator.GOBUFF_UPRD;
    }


}
