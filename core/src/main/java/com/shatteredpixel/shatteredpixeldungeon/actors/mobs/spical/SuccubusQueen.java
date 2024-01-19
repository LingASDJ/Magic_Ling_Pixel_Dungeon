package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Succubus;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SuccubusQueenSprite;
import com.watabou.utils.Random;

public class SuccubusQueen extends Succubus {

    {

        HP = HT = 200;

        defenseSkill = 30;

        spriteClass = SuccubusQueenSprite.class;

        viewDistance = 20;

        flying = true;

        EXP = 17;
        maxLvl = 30;

        loot = Generator.Category.RING;
        lootChance = 0.48f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 30, 40 );
    }

}
