package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class BlessMixShiled extends ClearLanterBuff {

    {
        type = buffType.POSITIVE;
    }

    public static int level = 0;
    private int interval = 1;



    @Override
    public boolean act() {
        if (target.isAlive()) {

            if(Dungeon.hero.buff(BlessMixShiled.class) != null && hero.HT == hero.HP && Dungeon.hero.buff(LanterBarrier.class) == null){
                if(Dungeon.depth <= 5) {
                    Buff.affect(hero, LanterBarrier.class).setShield(((20)));
                } else {
                    Buff.affect(hero, LanterBarrier.class).setShield(((20) * Dungeon.depth / 5));
                }
                spend(150f);
                GLog.w("你的血量已满，奖励你"+(Dungeon.depth<=5 ? 20 : (20) * Dungeon.depth / 5)+"点护盾值。");
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


    public static class LanterBarrier extends XSBarrier{
        @Override
        public boolean act() {
            incShield();
            return super.act();
        }
    }
    public static class XSBarrier extends ShieldBuff {

        {
            type = buffType.POSITIVE;
        }

        float partialLostShield;

        @Override
        public void incShield(int amt) {
            super.incShield(amt);
            partialLostShield = 0;
        }

        @Override
        public void setShield(int shield) {
            super.setShield(shield);
            if (shielding() == shield) partialLostShield = 0;
        }

        @Override
        public boolean act() {

            partialLostShield += Math.min(1f, shielding()/20f);

            if (partialLostShield >= 1f) {
                absorbDamage(1);
                partialLostShield = 0;
            }

            if (shielding() <= 0){
                detach();
            }

            spend( 10f );

            return true;
        }

        @Override
        public void fx(boolean on) {
            if (on) {
                target.sprite.add(CharSprite.State.SHIELDED);
            } else if (target.buff(Blocking.BlockBuff.class) == null) {
                target.sprite.remove(CharSprite.State.SHIELDED);
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.Pink_COLOR);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(shielding());
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", shielding());
        }

        private static final String PARTIAL_LOST_SHIELD = "partial_lost_shield";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(PARTIAL_LOST_SHIELD, partialLostShield);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            partialLostShield = bundle.getFloat(PARTIAL_LOST_SHIELD);
        }
    }

}



