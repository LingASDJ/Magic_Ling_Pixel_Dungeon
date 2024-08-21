package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FrankensteinSprite;
import com.watabou.utils.Random;

public class Frankenstein extends Mob {

    {
        spriteClass = FrankensteinSprite.class;

        baseSpeed = 1.2f;
        HP = HT = 90;
        EXP = 15;
        defenseSkill = 14;
        maxLvl = 34;
        properties.add(Char.Property.HOLLOW);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );


        if(enemy!=null && enemy == hero) {
            for (Buff buff : hero.buffs()) {
                if (buff instanceof ScaryBuff) {
                    //5损伤
                    ((ScaryBuff) buff).damgeScary( 5);
                } else {
                    //未寻找到元损立刻附加
                    Buff.affect( enemy, ScaryBuff.class ).set( (100), 1 );
                }
            }
        }
        return damage;
    }

    @Override
    public int damageRoll() {
        return Char.combatRoll( 20, 35 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public int drRoll() {
        return 0;
    }
}


