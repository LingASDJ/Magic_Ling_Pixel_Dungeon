package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IceFireScorpioSprite;
import com.watabou.utils.Random;

public class Ice_Scorpio extends Scorpio{
    {
        spriteClass = IceFireScorpioSprite.class;

        HP = HT =60;
        defenseSkill = 24;
        viewDistance = Light.DISTANCE;

        EXP = 14;
        maxLvl = 27;

        loot = Generator.Category.ARMOR;
        lootChance = 0.5f;

        properties.add(Property.DEMONIC);
        properties.add(Property.MINIBOSS);
    }

    @Override
    public int attackProc(Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int( 3,6 ) > 2) {
            Buff.prolong( enemy, Chill.class, Chill.DURATION );
            spend(3f);
        }
        return damage;
    }
}
