package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.allsearch;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShadowHunstmanSprites;
import com.watabou.utils.Random;

public class ShadowHunstman extends Mob {

    {
        spriteClass = ShadowHunstmanSprites.class;

        HP = HT = 140;
        defenseSkill = 50;
        maxLvl = 5;
        properties.add(Property.SEARCH);
    }

    @Override
    protected boolean act() {
        if (HP<HT){
            HP ++;
            sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1);
        }
        return super.act();
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {

        if (HP >= damage && enemy != null) {
            enemy.damage((int) (damage*0.2f),this);
        }

        return super.defenseProc(enemy, damage);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 4 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 48;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 1);
    }

}

