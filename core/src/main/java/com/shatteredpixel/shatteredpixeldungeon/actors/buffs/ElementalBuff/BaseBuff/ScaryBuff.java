package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.DamageBuff.ScaryDamageBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.IconFloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.TimeReset;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Random;

public class ScaryBuff extends ElementalBuff {

    {
        elementalIcon = BuffIndicator.SCARY;
        type = buffType.NEUTRAL;
    }

    @Override
    public String name() {
        String result;
        result = target instanceof Hero ? Messages.get(this, "name") : Messages.get(this, "enemyname");
        if (Scary > 80){
            result  += "-T4";
        } else if (Scary > 70) {
            result  += "-T3";
        } else if (Scary > 60) {
            result  += "-T2";
        } else  if (Scary > 50) {
            result += "-T1";
        }

        return result;
    }

    @Override
    public String desc() {
        String result;
        result = target instanceof Hero ? Messages.get(this, "desc",Scary) : Messages.get(this, "enemydesc",Scary);

        if (Scary > 80)   result  += "\n" + Messages.get(this, "effect_4");
        if (Scary > 70)   result  += "\n" + Messages.get(this, "effect_3");
        if (Scary > 60)   result  += "\n" + Messages.get(this, "effect_2");
        if (Scary > 50)   result  += "\n" + Messages.get(this, "effect_1");

        if (Scary < 50)   result  += "\n" + Messages.get(this, "no_effect");

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
    public void onDamageTaken(float percentHP) {

    }

    @Override
    public boolean act() {
        super.act();

        Char ch = target;

        if(Scary>=100 && ch instanceof Hero){
            detach();
            Buff.affect(ch, ScaryDamageBuff.class).set((40),1);
        } else if(Scary>60 && ch instanceof Mob) {
            detach();
            Buff.affect(ch, TimeReset.MobsWither.class).set((Random.NormalIntRange(6,15)),1);
        } else if(Scary>0 && ch instanceof Hero) {
            Scary--;
        } else if(Scary>0) {
            Scary--;
        } else {
            detach();
        }
        return true;
    }

    @Override
    public int icon() {

        if(Scary >= 50){
            return BuffIndicator.SCARY_PINK;
        } else
            return BuffIndicator.SCARY;
    }



}
