package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlowerSlimeSprites;
import com.watabou.utils.Random;

public class FlowerSlime extends Mob {

    {
        spriteClass = FlowerSlimeSprites.class;

        loot = Generator.Category.SEED;
        lootChance = 0.09f;

        HP = HT = Random.NormalIntRange(5,8);
        defenseSkill = 2;
        maxLvl = 5;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 5 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 7;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }

}

