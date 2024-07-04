package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.DamageBuff.ScaryDamageBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneral;
import com.shatteredpixel.shatteredpixeldungeon.effects.IconFloatingText;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ScaryBuff extends ElementalBuff {

    {
        elementalIcon = BuffIndicator.SCARY;
        type = buffType.NEGATIVE;
    }

    @Override
    public String name() {
        String result;
        result = target instanceof Hero ? Messages.get(this, "name") : Messages.get(this, "enemyname");
        return result;
    }

    @Override
    public String desc() {
        String result;
        result = target instanceof Hero ? Messages.get(this, "desc",Scary) : Messages.get(this, "enemydesc",Scary);
        return result;
    }

    public void damgeScary(int value) {
        Scary = Math.min(Scary + value, 100);
        if(target instanceof Hero){
            target.sprite.showStatusWithIcon(CharSprite.NEGATIVE, Integer.toString(value), IconFloatingText.HEARTDEMON);
        }


    }


    @Override
    public String iconTextDisplay() {
        return Integer.toString(Scary);
    }

    @Override
    public boolean act() {
        super.act();

        if(Scary>=100 && target instanceof Hero){
            detach();
            Buff.affect(target, ScaryDamageBuff.class).set((40),1);
        } else if(Scary>25) {
            detach();
            Buff.affect(target, MobsWither.class).set((Random.NormalIntRange(6,15)),1);
        } else if(Scary>0 && target instanceof Hero) {
            Scary--;
            spend(12f);
        } else if(Scary>0 ) {
            Scary--;
            spend(4f);
        } else {
            detach();
        }

        return true;
    }

    public static class MobsWither extends DwarfGeneral.Wither {

        {
            type = buffType.NEGATIVE;
            announced = true;
        }


        public static final float DURATION	= 30f;
        private float damageInc = 0;
        @Override
        public boolean act() {
            if (target.isAlive()) {

                damageInc = Random.Int(2,5);
                target.damage((int)damageInc, this);
                damageInc -= (int)damageInc;

                spend(1f);
                if (--level <= 0) {
                    detach();
                }
                if (target == hero && !target.isAlive()){
                    GLog.n(Messages.get(DwarfGeneral.Wither.class, "on_kill"));
                }


            } else {
                detach();
            }

            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.POISON;
        }

        public static final String DAMAGE = "damage_inc";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DAMAGE, damageInc);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            damageInc = bundle.getFloat(DAMAGE);
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 0f, 0f);
        }

    }
}
