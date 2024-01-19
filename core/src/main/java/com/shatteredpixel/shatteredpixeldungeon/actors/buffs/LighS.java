package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilLantern;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class LighS extends FlavourBuff {
    private static final float DELAY = 7.0f;
    public static final int DISTANCE = 6;
    public static final float DURATION = 300.0f;

    public LighS() {
        this.type = Buff.buffType.POSITIVE;
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            if (Dungeon.level != null) {
                target.viewDistance = Math.max( Dungeon.level.viewDistance, DISTANCE );
                Dungeon.observe();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean act() {
        OilLantern lantern = hero.belongings.getItem(OilLantern.class);
        if (hero.buff(LostInventory.class) == null){
            if ( !lantern.isActivated() || lantern.getCharge() <= 0) {
                lantern.deactivate(hero, false);
                detach();
                return true;
            }
            lantern.spendCharge();
        } else {
            GLog.n(Messages.get(this,"tip"));
            detach();
        }





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

