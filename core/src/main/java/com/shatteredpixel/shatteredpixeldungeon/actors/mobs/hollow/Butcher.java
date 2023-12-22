package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ButcherSprite;
import com.watabou.utils.Random;

public class Butcher extends Mob {
    {
        spriteClass = ButcherSprite.class;

        HP = HT = 120;
        defenseSkill = 18;

        maxLvl = 40;

        EXP = 16;

        properties.add(Property.HOLLOW);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 30, 50 );
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
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return 0;
    }

}
