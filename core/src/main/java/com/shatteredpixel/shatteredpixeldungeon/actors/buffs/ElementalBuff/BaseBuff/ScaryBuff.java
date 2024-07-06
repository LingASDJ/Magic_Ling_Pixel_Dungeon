package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.DamageBuff.ScaryDamageBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.IconFloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.TimeReset;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
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
        } else if(Scary>60) {
            detach();
            Buff.affect(target, TimeReset.MobsWither.class).set((Random.NormalIntRange(6,15)),1);
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


}
