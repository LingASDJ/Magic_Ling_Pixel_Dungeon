package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GreenSltingSprite;
import com.watabou.utils.Random;

public class GreenSlting extends Mob {

    {
        spriteClass = GreenSltingSprite.class;
        flying = true;
        HP = HT = 30;
        defenseSkill = 2;
        maxLvl = 30;
        EXP = 5;
        loot = Food.class;
        lootChance = 1f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 5 + Dungeon.depth, 9 + Dungeon.depth );
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

