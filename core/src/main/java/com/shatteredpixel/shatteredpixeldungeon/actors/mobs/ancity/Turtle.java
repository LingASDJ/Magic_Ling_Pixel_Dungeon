package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DragonGirlBlue;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TurtleSprite;
import com.watabou.utils.Random;

public class Turtle extends Mob {

    {
        HP = HT = Random.NormalIntRange(50,75);
        spriteClass = TurtleSprite.class;
        EXP = 0;
        maxLvl = 32;
        defenseSkill = 4;
        loot = Generator.Category.SEED;
        lootChance = 0.06f;
    }

    @Override
    public int damageRoll() {
        return Char.combatRoll( 15, 27 );
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        DragonGirlBlue.Quest.survey_research_points += 100;
        Badges.validateAncityProgress();
    }

}
