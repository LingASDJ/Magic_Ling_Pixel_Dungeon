package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
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
        Buff.detach( target, HalomethaneBurning.class );
        Buff.detach( target, FrostBurning.class );
        return super.attachTo(target);
    }

    public float speedFactor(){
        return 0.2f;
    }

    @Override
    public int icon() {
        return BuffIndicator.SNOW_RAIN;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.CHILLED_2);
        else target.sprite.remove(CharSprite.State.CHILLED_2);
    }

    @Override
    public String desc() {
        String desc;

        if(Dungeon.iceLevel()){
            desc = Messages.get(this, "effect", (int)(speedFactor()*100), Dungeon.depth*15/5, Dungeon.depth*6/5);
        } else {
            desc = Messages.get(this, "desc", (int)(speedFactor()*100));
        }

        return desc;
    }
}
