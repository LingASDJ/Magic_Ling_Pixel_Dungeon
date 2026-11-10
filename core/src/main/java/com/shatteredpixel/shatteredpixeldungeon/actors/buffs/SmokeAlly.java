package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfAnmy;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class SmokeAlly extends Buff{

    public Char.Alignment align;
    public Char ch;

    @Override
    public boolean attachTo(Char target) {

        // The smoke is only meant to pacify enemies. If the target is already an
        // ally (e.g. a honeyed bee), do not attach at all, otherwise we would
        // remember ALLY as the "original" alignment and later restore it.
        if (target.alignment == Char.Alignment.ALLY) {
            return false;
        }

        if (super.attachTo(target)){
            // Only record the alignment on the first attach. attachTo can be
            // called again for the same buff instance after loading a save, and
            // overwriting align there would record NEUTRAL as the original state.
            if (align == null) {
                align = target.alignment;
            }
            ch = target;
            if(target.alignment == Char.Alignment.ENEMY ){
                target.alignment = Char.Alignment.NEUTRAL;
                if(target instanceof Mob && ((Mob)target).enemy() instanceof Hero){
                    ((Mob) target).enemyReset();
                }
            }
            if (target.buff(PinCushion.class) != null){
                target.buff(PinCushion.class).detach();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach(){

        Char target = ch != null ? ch : this.target;
        if (target != null && target.alignment == Char.Alignment.NEUTRAL) {
            // If another effect has genuinely converted the target into an ally
            // while the smoke was active, keep that conversion. But if the
            // target is still neutral, it is neutral only because of the smoke,
            // so restore its original alignment. This prevents the smoke from
            // permanently turning animals into friendlies.
            if (hasOtherAllySource(target)) {
                target.alignment = Char.Alignment.ALLY;
            } else {
                target.alignment = align != null ? align : Char.Alignment.ENEMY;
            }
        }

        if (this.target != null) {
            super.detach();
        }
    }

    private static boolean hasOtherAllySource(Char target) {
        return !target.buffs(ScrollOfSirensSong.Enthralled.class).isEmpty()
                || !target.buffs(Corruption.class).isEmpty()
                || !target.buffs(WandOfAnmy.AllyToRestart.class).isEmpty();
    }

    private static final String ALIGN = "align";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ALIGN, align);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(ALIGN)) {
            align = bundle.getEnum(ALIGN, Char.Alignment.class);
        }
    }

    public int icon() {
        return BuffIndicator.HEART;
    }

}
