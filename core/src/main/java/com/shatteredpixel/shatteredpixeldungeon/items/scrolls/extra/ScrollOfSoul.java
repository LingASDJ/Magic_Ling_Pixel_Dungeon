package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.extra;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class ScrollOfSoul extends Scroll {

    public int soulLimit;

    {
        image = ItemSpriteSheet.SOUL_SCROLL;
        unique = true;
        soulLimit = 300;
    }

    @Override
    public boolean collect() {
        Buff.affect(hero, SoulLess.class).set((soulLimit), 1);
        return super.doPickUp(hero);
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isKnown() {
        return true;
    }

    @Override
    public void doRead() {
        detach(curUser.belongings.backpack);
        new Flare( 5, 32 ).color( 0x4B9AA1, true ).show( curUser.sprite, 2f );
        Buff.affect(hero, UpgradeSoul.class).set((Math.max(50,soulLimit)), 1);
        identify();
        readAnimation();
        Buff.detach(hero, SoulLess.class);
    }

    @Override
    public String desc() {
        return Messages.get(this,"desc",Math.max(50,soulLimit));
    }

    public static class SoulLess extends Buff {

        {
            type = buffType.POSITIVE;
        }

        private int level = 0;
        private int interval = 1;
        private int carriedTurns = 0;

        @Override
        public boolean act() {
            if (target.isAlive()) {
                spend( interval );
                carriedTurns++;

                ScrollOfSoul scrollOfSoul = Dungeon.hero.belongings.getItem(ScrollOfSoul.class);

                if(scrollOfSoul != null){
                    if (carriedTurns >= 20) {
                        scrollOfSoul.soulLimit--;
                        carriedTurns = 0;
                    }
                } else {
                    for (Heap heap : Dungeon.level.heaps.valueList()) {
                        for (Item item : heap.items) {
                            if(item instanceof ScrollOfSoul) {
                                if (carriedTurns >= 20) {
                                    ((ScrollOfSoul) item).soulLimit--;
                                    carriedTurns = 0;
                                }
                            }
                        }
                    }
                }
            } else {
                detach();
            }
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.WICKBONE;
        }

        public int level() {
            return level;
        }

        public void set( int value, int time ) {
            if (level <= value) {
                level = value;
                interval = time;
                spend(time - cooldown() - 1);
                carriedTurns = 0;
            }
        }

        private static final String LEVEL	    = "level";
        private static final String INTERVAL    = "interval";
        private static final String CARRIED_TURNS = "carriedTurns";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( INTERVAL, interval );
            bundle.put( LEVEL, level );
            bundle.put(CARRIED_TURNS, carriedTurns);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            interval = bundle.getInt( INTERVAL );
            level = bundle.getInt( LEVEL );
            carriedTurns = bundle.getInt(CARRIED_TURNS);
        }
    }

    public static class UpgradeSoul extends Buff {

        {
            type = buffType.POSITIVE;
        }

        private int level = 0;
        private int interval = 1;
        private int carriedTurns = 0;

        public int attackDamageMulti;
        public int shieldDamageMulti;

        @Override
        public boolean act() {
            if (target.isAlive()) {
                spend( interval );
                carriedTurns++;

                for (Buff b : target.buffs()) {
                    if (b.type == Buff.buffType.NEGATIVE
                            && !(b instanceof AllyBuff)
                            && !(b instanceof LostInventory)
                            && !(b instanceof Hunger) ) {
                        b.detach();
                    }
                }

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
                carriedTurns = 0;
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.UPGRADE_SOUL;
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
        public String iconTextDisplay() {
            return Integer.toString(level);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", level,
                   Math.min(90,attackDamageMulti),
                    Math.min(90,shieldDamageMulti)
            );
        }

        private static final String LEVEL	    = "level";
        private static final String INTERVAL    = "interval";
        private static final String CARRIED_TURNS = "carriedTurns";
        private static final String DG    = "dg";
        private static final String SG    = "sg";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( INTERVAL, interval );
            bundle.put( LEVEL, level );
            bundle.put(CARRIED_TURNS, carriedTurns);
            bundle.put(DG, (float) attackDamageMulti);
            bundle.put(SG, (float) shieldDamageMulti);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            interval = bundle.getInt( INTERVAL );
            level = bundle.getInt( LEVEL );
            carriedTurns = bundle.getInt(CARRIED_TURNS);
            attackDamageMulti = bundle.getInt(DG);
            shieldDamageMulti = bundle.getInt(SG);
        }
    }


}
