package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TribesmanSprite;
import com.watabou.utils.Random;

public class GnollHero extends Mob {

    {
        spriteClass = TribesmanSprite.class;
        HP = HT = 30;
        baseSpeed = 1.4f;
        maxLvl = 30;
        EXP = 9;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Dungeon.level.drop(new Gold( 400), pos).sprite.drop();
    }

    @Override
    public float attackDelay() {
        return 0.5f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 21 );
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
