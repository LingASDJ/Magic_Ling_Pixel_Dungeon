package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TribesmanSprite;
import com.watabou.utils.Random;

public class GnollHero extends Mob {

    {
        spriteClass = TribesmanSprite.class;
        HP = HT = 50;
        baseSpeed = 2f;
    }



    @Override
    public float attackDelay() {
        return 0.5f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 20, 32 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 30;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(10, 16);
    }

}
