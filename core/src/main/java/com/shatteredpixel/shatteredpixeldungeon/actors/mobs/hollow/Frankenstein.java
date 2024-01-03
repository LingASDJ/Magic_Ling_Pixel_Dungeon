package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FrankensteinSprite;
import com.watabou.utils.Random;

public class Frankenstein extends Mob {

    {
        spriteClass = FrankensteinSprite.class;

        baseSpeed = 0.85f;
        HP = HT = 90;
        EXP = 15;
        defenseSkill = 14;
        maxLvl = 34;
        properties.add(Char.Property.HOLLOW);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 27 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public int drRoll() {
        return 0;
    }
}


