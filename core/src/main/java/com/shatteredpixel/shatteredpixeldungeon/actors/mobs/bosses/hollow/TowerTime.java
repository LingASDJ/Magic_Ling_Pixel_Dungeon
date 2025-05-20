package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerTimeSprite;
import com.watabou.utils.Random;

public class TowerTime extends Boss {

    {
        initProperty();
        initBaseStatus(10, 20, 33, 0, 300, 0, 0);
        initStatus(120);
        first = true;
        spriteClass = TowerTimeSprite.class;

        viewDistance = 100;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 20 );
    }

    @Override
    public boolean act() {
        alerted = false;
        state = PASSIVE;
        return super.act();
    }
}
