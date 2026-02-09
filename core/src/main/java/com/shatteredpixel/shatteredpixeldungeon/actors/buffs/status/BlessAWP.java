package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.watabou.utils.Bundle;

public class BlessAWP {
    public static class WeaponGetReady extends Buff {

        {
            type = buffType.NEUTRAL;
        }

        private int level = 0;
        private int interval = 1;

        @Override
        public boolean act() {
            if (target.isAlive()) {

                spend( interval );
                if (level <= 0) {
                    detach();
                }

            } else {

                detach();

            }

            return true;
        }

        public void set( int value, int time ) {
            if (level <= value) {
                level = value;
                interval = time;
                spend(time - cooldown() - 1);
            }
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
    }

    public static class ArmorGetReady extends Buff {

        {
            type = buffType.NEUTRAL;
        }

        private int level = 0;
        private int interval = 1;

        @Override
        public boolean act() {
            if (target.isAlive()) {

                spend( interval );
                if (level <= 0) {
                    detach();
                }

            } else {

                detach();

            }

            return true;
        }

        public void set( int value, int time ) {
            if (level <= value) {
                level = value;
                interval = time;
                spend(time - cooldown() - 1);
            }
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
    }
}
