package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BleedCrystalSprite;

public class BleedCrystal extends Mob {

    {
        spriteClass = BleedCrystalSprite.class;
        properties.add(Property.IMMOVABLE);
        HP = HT = 45;
        properties.add(Property.MINIBOSS);
        state = PASSIVE;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return 0;
    }

    @Override
    public void damage( int dmg, Object src ) {
        //do nothing
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {
        return true;
    }

}
