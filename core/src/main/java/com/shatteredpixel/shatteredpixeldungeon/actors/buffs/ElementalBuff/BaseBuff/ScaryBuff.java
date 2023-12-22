package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.DamageBuff.ScaryDamageBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.IconFloatingText;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class ScaryBuff extends ElementalBuff {

    {
        elementalIcon = BuffIndicator.SCARY;
        type = buffType.NEGATIVE;
    }

    @Override
    public String desc() {
        String result;
        result = Messages.get(this, "desc",Scary);
        return result;
    }

    public void damgeScary(int value) {
        Scary = Math.min(Scary + value, 100);
        target.sprite.showStatusWithIcon(CharSprite.NEGATIVE, Integer.toString(value), IconFloatingText.HEARTDEMON);
    }


    @Override
    public String iconTextDisplay() {
        return Integer.toString(Scary);
    }

    @Override
    public boolean act() {
        super.act();

        if(Scary>=100){
            detach();
            Buff.affect(target, ScaryDamageBuff.class, 50f);
            AlarmTrap am = new AlarmTrap();
            am.pos = target.pos;
            am.activate();
        }

        return true;
    }
}
