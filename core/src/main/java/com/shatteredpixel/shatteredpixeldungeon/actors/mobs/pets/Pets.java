package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.watabou.utils.Bundle;

public abstract class Pets extends Mob {

    {
        alignment = Alignment.ALLY;
        HP = HT = 1;
        EXP = 0;
        intelligentAlly = true;
        properties.add(Property.PETS);
        state = PASSIVE;
        invisible = 1;
        baseSpeed = 1.5f;
    }

    @Override
    public float speed() {
        return baseSpeed;
    }


    protected int defendingPos = -1;
    protected boolean movingToDefendPos = false;

    public void defendPos( int cell ){
        defendingPos = cell;
        movingToDefendPos = true;
        aggro(null);
        state = WANDERING;
    }

    public void clearDefensingPos(){
        defendingPos = -1;
        movingToDefendPos = false;
    }

    private static final String DEFEND_POS = "defend_pos_pets";
    private static final String MOVING_TO_DEFEND = "moving_to_defend_pets";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DEFEND_POS, defendingPos);
        bundle.put(MOVING_TO_DEFEND, movingToDefendPos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(DEFEND_POS)) defendingPos = bundle.getInt(DEFEND_POS);
        movingToDefendPos = bundle.getBoolean(MOVING_TO_DEFEND);
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public boolean add(Buff buff ) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }


    protected boolean canAttack( Char enemy ) {
        return false;
    }
}
