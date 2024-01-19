package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM275Sprite;
import com.watabou.utils.Random;

public class DM275 extends Mob {

    {
        spriteClass = DM275Sprite.class;

        HP = HT = 160;
        EXP = 30;
        defenseSkill = 18;
        maxLvl = 30;
        baseSpeed = 0.85f;

        properties.add(Property.LARGE);
        properties.add(Property.MINIBOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.FIERY);
    }

    @Override
    public int attackSkill( Char target ) {
        return 48;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(8, 16);
    }

}
