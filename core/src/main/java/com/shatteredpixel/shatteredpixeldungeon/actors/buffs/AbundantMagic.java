package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class AbundantMagic extends Buff{

    public float duration = 5f;

    {
        type = buffType.POSITIVE;
    }

    @Override
    public boolean act() {

        spend(1f);

        duration--;

        if (duration <= 0) detach();

        return super.act();
    }

    @Override
    public int icon() {
        return BuffIndicator.PARALYSIS;
    }

    @Override
    public String desc() {
        return Messages.get(AbundantMagic.class,"desc",duration);
    }

    public static final String DURATION = "duration";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( DURATION, duration );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        duration = bundle.getFloat( DURATION );
    }

}
