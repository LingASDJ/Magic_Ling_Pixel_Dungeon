package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerGodSprite;
import com.watabou.utils.Random;

public class TowerGods extends Boss {

    {
        initProperty();
        initBaseStatus(15, 20, 33, 10, 400, 0, 0);
        initStatus(120);
        first = true;
        spriteClass = TowerGodSprite.class;

        viewDistance = 100;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 20 );
    }

    @Override
    public boolean act() {
        alerted = false;
        state = PASSIVE;
        return super.act();
    }

}
