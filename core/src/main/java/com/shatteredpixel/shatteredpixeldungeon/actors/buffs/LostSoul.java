package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class LostSoul extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    private static final String MAX_HP_REDUCE = "maxHpReduce";
    private static final String THIEF_ID = "thief_id";

    public int hpLoss = 10;
    public int thiefSoulID = -1;

    @Override
    public boolean attachTo(Char target) {
        boolean success = super.attachTo(target);
        if (success && target instanceof Hero) {
            ((Hero) target).updateHT(false);
        }
        return success;
    }

    @Override
    public void detach() {
        super.detach();
        if (target instanceof Hero) {
            ((Hero) target).updateHT(false);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MAX_HP_REDUCE, hpLoss);
        bundle.put(THIEF_ID, thiefSoulID);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        hpLoss = bundle.getInt(MAX_HP_REDUCE);
        thiefSoulID = bundle.getInt(THIEF_ID);
    }

    public static class LostCount extends Buff {

        {
            type = buffType.NEUTRAL;
            announced = false;
        }

        private static final String COUNT_KEY = "lost_count";
        private int count = 0;

        @Override
        public boolean act() {
            int realCount = target.buffs(LostSoul.class).size();

            if (realCount == 0) {
                detach();
                return super.act();
            }

            if (this.count != realCount) {
                this.count = realCount;
                BuffIndicator.refreshHero();
            }

            spend(TICK);
            return super.act();
        }

        public int getCount() {
            return target.buffs(LostSoul.class).size();
        }

        @Override
        public int icon() {
            return BuffIndicator.LOST_SOUL;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", getCount(), getCount()*10);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(getCount());
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(COUNT_KEY, count);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            count = bundle.getInt(COUNT_KEY);
        }
    }


}