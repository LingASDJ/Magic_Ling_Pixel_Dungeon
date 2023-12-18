package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ButcherSprite;
import com.watabou.utils.Random;

public class Butcher extends Mob {
    {
        spriteClass = ButcherSprite.class;

        HP = HT = 150;
        defenseSkill = 18;

        maxLvl = 40;

        EXP = 16;

        properties.add(Property.HOLLOW);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 17, 45 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return 0;
    }

}
