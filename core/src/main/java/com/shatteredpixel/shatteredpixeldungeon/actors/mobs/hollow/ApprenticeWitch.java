package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ApprenticeWitchSprite;
import com.watabou.utils.Random;

public class ApprenticeWitch extends Mob {

    {
        spriteClass = ApprenticeWitchSprite.class;

        baseSpeed = 1.2f;
        HP = HT = 150;
        defenseSkill = 24;
        maxLvl = 35;
        properties.add(Property.HOLLOW);

        properties.add(Property.ICY);
        properties.add(Property.FIERY);
        properties.add(Property.ELECTRIC);
    }

//    @Override
//    protected boolean act() {
//        return super.act();
//    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 20, 34 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(5, 10);
    }
}

