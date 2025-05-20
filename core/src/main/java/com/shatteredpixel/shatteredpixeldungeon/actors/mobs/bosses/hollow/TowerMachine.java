package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerMachineSprite;
import com.watabou.utils.Random;

public class TowerMachine extends Boss {

    {
        initProperty();
        initBaseStatus(50, 75, 33, 0, 400, 0, 0);
        initStatus(120);
        first = true;
        spriteClass = TowerMachineSprite.class;

        viewDistance = 100;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(50, 75);
    }

    @Override
    public boolean act() {
        alerted = false;
        state = PASSIVE;
        return super.act();
    }
}

