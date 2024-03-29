package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GreenSltingSprite;
import com.watabou.utils.Random;

public class GreenSlting extends Mob {

    {
        spriteClass = GreenSltingSprite.class;
        flying = true;
        HP = HT = 30;
        defenseSkill = 2;
        maxLvl = 7;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 8, 12 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 8;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(2, 4);
    }
}

