package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NyarlathotepSprite;

public class Nyarlathotep extends Boss {

    {
        initProperty();
        initBaseStatus(0, 0, 0, 0, 700, 0, 0);
        initStatus(20);
        spriteClass = NyarlathotepSprite.class;

        properties.add(Property.BOSS);
        properties.add(Property.ACIDIC);

        noDropIceCoin = true;
    }
}
