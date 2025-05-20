package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerMindSprite;
import com.watabou.utils.Random;

public class TowerMind extends Boss {

    {
        initProperty();
        initBaseStatus(10, 45, 33, 45, 200, 0, 0);
        initStatus(120);
        first = true;
        spriteClass = TowerMindSprite.class;

        viewDistance = 100;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 45 );
    }

    @Override
    public boolean act() {
        alerted = false;
        state = PASSIVE;
        return super.act();
    }
}