package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class LegendWeapon extends MeleeWeapon {

    {
        defaultAction = AC_KING;
    }
    protected Buff passiveBuff;
    protected Buff activeBuff;

    public void activate( Char ch ) {
        passiveBuff = passiveBuff();
        passiveBuff.attachTo(ch);
    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        if (super.doUnequip( hero, collect, single )) {

            if (passiveBuff != null) {
                passiveBuff.detach();
                passiveBuff = null;
            }

            return true;

        } else {

            return false;

        }
    }

    protected static final String AC_KING       = "KING";
    protected int legend;
    protected int min;
    protected int max;
    protected int baseMin;
    protected int baseMax;


    protected int cooldown = 0;

    public class LegendWeaponBuff extends Buff {

        public boolean isCursed() {
            return cursed;
        }

        public void charge(Hero target, float amount){
            this.charge(target, amount);
        }

    }

    protected LegendWeapon.LegendWeaponBuff passiveBuff() {
        return null;
    }

    protected LegendWeapon.LegendWeaponBuff activeBuff() {return null; }

    public void charge(Hero target, float amount){
        //do nothing by default;
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        cooldown = bundle.getInt("cooldown");

    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("cooldown", cooldown);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_KING);
        return actions;
    }


    public int Lmin() {
        if(level()==0) {
            return baseMin;
        } else {
            return min * level();
        }
    }

    public int Lmax() {
        if(level()==0){
            return baseMax;
        } else {
            return  max*level();   //scaling unchanged
        }

    }

    @Override
    public String statsInfo() {
        if (isEquipped(hero)) {
            if (isIdentified()) {
                return Messages.get(LegendWeapon.class, "stats_desc", legend, Messages.get(this, "king_desc"), Lmin(),
                        Lmax());
            } else {
                return Messages.get(LegendWeapon.class, "typical_stats_desc", 9);
            }
        } else {
           return "";
        }

    }

}
