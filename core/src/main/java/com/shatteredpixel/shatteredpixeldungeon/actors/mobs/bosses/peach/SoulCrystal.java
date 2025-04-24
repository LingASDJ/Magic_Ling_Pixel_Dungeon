package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.peach;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrstalSprite;

//魂烈晶
public class SoulCrystal extends Boss {

    {
        spriteClass = CrstalSprite.class;

        HP = HT = 45;

        maxLvl = -99;

        EXP = 10;

        properties.add(Property.BOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.IMMOVABLE);

        viewDistance = 9;
        state = WANDERING = new Waiting();
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }

    private class Waiting extends Mob.Wandering{}

}
