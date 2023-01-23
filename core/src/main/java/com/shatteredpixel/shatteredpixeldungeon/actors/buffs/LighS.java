package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilLantern;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class LighS extends FlavourBuff {
    private static final float DELAY = 7.0f;
    public static final int DISTANCE = 6;
    public static final float DURATION = 300.0f;

    public LighS() {
        this.type = Buff.buffType.POSITIVE;
    }

    public boolean attachTo(Char target) {
        if (!LighS.super.attachTo(target)) {
            return false;
        }
        if (Dungeon.level == null) {
            return true;
        }
        target.viewDistance = Math.max(Dungeon.level.viewDistance, 6);
        Dungeon.observe();
        return true;
    }

    public boolean act() {
        OilLantern lantern = Dungeon.hero.belongings.getItem(OilLantern.class);
        if ( !lantern.isActivated() || lantern.getCharge() <= 0) {
            lantern.deactivate(Dungeon.hero, false);
            detach();
            return true;
        }
        lantern.spendCharge();

        if(Dungeon.depth>20){
            spend(DELAY + 6f);
        } else if (Dungeon.depth>15){
            spend(DELAY + 4f);
        } else if (Dungeon.depth>10){
            spend(DELAY + 2f);
        } else {
            spend(DELAY);
        }

        return true;
    }

    public void detach() {
        this.target.viewDistance = Dungeon.level.viewDistance;
        Dungeon.observe();
        LighS.super.detach();
    }

    public int icon() {
        return 22;
    }

    public String toString() {
        return Messages.get(this, "name");
    }

    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }
}

