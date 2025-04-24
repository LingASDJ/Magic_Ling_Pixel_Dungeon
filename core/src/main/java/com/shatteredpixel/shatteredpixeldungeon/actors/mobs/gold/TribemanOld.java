package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.watabou.utils.Random;

public class TribemanOld extends GoldMob {

    {
        spriteClass = TribemanOldSprite.class;

        HP = HT = 90;

        defenseSkill = 18;

        EXP = 11;
        maxLvl = 21;

        loot = Generator.Category.POTION;
        lootChance = 0.5f;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Dungeon.level.drop(new Gold( Random.Int(250,401)), pos).sprite.drop();
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (damage > 0 && Random.Int( 2 ) == 0) {
            Buff.affect( enemy, Bleeding.class ).set( damage );
        }

        return damage;
    }

    @Override
    public float attackDelay() {
        return 0.5f;
    }

    @Override
    public int damageRoll() {
        int tier = 5;
        int lvl = 1;
        return Random.NormalIntRange( 5,  Math.round(6.67f*(tier+1)) +    //40 base, up from 30
                lvl*Math.round(1.33f*(tier+1)) );
    }

    @Override
    public int attackSkill( Char target ) {
        return 30;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(8, 12);
    }

}
