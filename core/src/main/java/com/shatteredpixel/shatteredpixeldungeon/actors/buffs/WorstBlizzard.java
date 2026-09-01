package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class WorstBlizzard extends FlavourBuff{

    public static final float DURATION = 10f;
    public int wandlevel = 0;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    public void setWandlevel(int i){
        wandlevel = i;
    }


    @Override
    public boolean attachTo(Char target) {
        Buff.detach( target, Burning.class );
        Buff.detach( target, HalomethaneBurning.class );
        Buff.detach( target, FrostBurning.class );
        return super.attachTo(target);
    }

    public float speedFactor(){
        return Math.max( 0.1f, 1-(0.2f+0.05f*wandlevel) );
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
            desc = Messages.get(this, "effect", (int)(100-speedFactor()*100), Dungeon.depth*15/5, Dungeon.depth*6/5);
        } else {
            desc = Messages.get(this, "desc", (int)(100-speedFactor()*100));
        }

        return desc;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("wandlevel",wandlevel);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        wandlevel = bundle.getInt("wandlevel");
    }

}
