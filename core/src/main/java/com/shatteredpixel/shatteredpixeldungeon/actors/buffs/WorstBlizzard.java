package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class WorstBlizzard extends FlavourBuff{
    public static final float DURATION = 10f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public boolean attachTo(Char target) {
        Buff.detach( target, Burning.class );
        return super.attachTo(target);
    }

    public float speedFactor(){
        return 0.2f;
    }

    @Override
    public int icon() {
        return BuffIndicator.FROST;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.CHILLED);
        else target.sprite.remove(CharSprite.State.CHILLED);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", (int)(speedFactor()*100))+"%";
    }
}
