package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeadDogSleepCerberusSprite;

public class CerbusSleep extends NPC {

    {
        spriteClass = DeadDogSleepCerberusSprite.class;
        properties.add(Property.IMMOVABLE);
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public boolean add(Buff buff ) {
        return false;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean reset() {
        return true;
    }

}
