package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KatydidSprites;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Katydid extends Mob {

    {
        spriteClass = KatydidSprites.class;
        EXP = 3;
        HP = HT = 4;
        defenseSkill = 16;

        loot = Generator.Category.SEED;
        lootChance = 0.4f;

        maxLvl = 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(1, 2);
    }



    @Override
    public int damageRoll() {
        return Char.combatRoll( 2, 4 );
    }

    public void onZapComplete() {
        onAttackComplete();
        next();
    }

    private int combo = 0;

    @Override
    public int attackSkill( Char target ) {
        return 10;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if(Dungeon.level.distance(pos,target)>2)
            return false;
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (damage > 0 && Random.Int( 4 ) == 0) {
            Buff.affect( enemy, Bleeding.class ).set( damage/2f );
        }

        return damage;
    }

    @Override
    protected boolean getCloser(int target) {
        combo = 0;
        if (state == HUNTING) {
            if(Dungeon.level.distance(pos,target)>2)
                return super.getCloser( target );
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser(target);
        }
        //return false;
    }

    @Override
    public void aggro(Char ch) {
        //cannot be aggroed to something it can't see
        if (ch == null || fieldOfView == null || fieldOfView[ch.pos]) {
            super.aggro(ch);
        }
    }

    private static final String COMBO = "combo";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(COMBO, combo);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        combo = bundle.getInt( COMBO );
    }

}

