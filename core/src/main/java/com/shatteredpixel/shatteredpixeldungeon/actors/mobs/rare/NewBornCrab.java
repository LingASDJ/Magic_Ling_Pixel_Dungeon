package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabSprite;
import com.watabou.utils.Random;

public class NewBornCrab extends Mob {

    {
        spriteClass = CrabSprite.NewBornCrabSprite.class;

        HP = HT = 12;
        defenseSkill = 6;
        baseSpeed = 2.5f;

        EXP = 4;
        maxLvl = 9;
        isAnimal = true;

        loot = new MysteryMeat();
        lootChance = 1f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 7 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 3);
    }
}

