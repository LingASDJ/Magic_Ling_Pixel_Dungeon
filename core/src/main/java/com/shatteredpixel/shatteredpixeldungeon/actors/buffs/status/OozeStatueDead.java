package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class OozeStatueDead extends Buff {

    public static class OozeStatueDeadStats extends OozeStatueDead {
        {
            type = buffType.NEGATIVE;
        }
    }

    private int interval = 0;
    public int level = 1;

    {
        type = buffType.POSITIVE;
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {

           if(Dungeon.level.map[target.pos] != Terrain.SALT_WATER){
               interval--;
           } else {
               interval++;
           }

           if(level == 1 && interval <0){
               detach();
           } else {
              if(interval>=10- Dungeon.depth/5){
                  if(Dungeon.depth<3){
                      Buff.affect(target, Ooze.class).set(Dungeon.depth/5f);
                  } else {
                      Buff.affect(target, Ooze.class).set(3+Dungeon.depth/5f);
                  }

                  detach();
              }
           }
        }
        spend(1f);
        return true;
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
    public int icon() {
        return BuffIndicator.HEX;
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(interval);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (interval + visualcooldown()) / (7 - Dungeon.depth/5f));
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0x8470FF);
    }

    @Override
    public String desc() {
        String result = Messages.get(this, "desc",10- Dungeon.depth/5, 2+Dungeon.depth/5f);

        result += "\n\n" + Messages.get(this, "desc_level", interval);
        result += "\n" + Messages.get(this, "desc_interval", 2+Dungeon.depth/5f);

        return result;
    }
}
