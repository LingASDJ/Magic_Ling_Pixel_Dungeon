package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.watabou.utils.Random;

public class Prisoner extends GoldMob {
    {
        spriteClass = PrisonerSprite.class;

        HP = HT = 80;
        defenseSkill = 0;

        baseSpeed = 1f;

        EXP = 5;
        maxLvl = 12;

        loot = Random.oneOf(Generator.Category.SEED,Generator.Category.STONE);
        lootChance =0.4f;
    }
    public int damageRoll() {
        return Random.NormalIntRange( 2, 12 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(1, 4);
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
    }

    @Override
    public Item createLoot() {
        if(super.createLoot() instanceof Plant.Seed){
            int BonusRate = (int)(1f/Random.Float(0.4f,1.0f));
            return super.createLoot().quantity(BonusRate);
        }
        return super.createLoot();
    }
}
