package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.tomb;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GoreSprite;

public class Gore extends NTNPC {

    {
        spriteClass = GoreSprite.class;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.TUMULUS);
        properties.add(Property.DEMONIC);
    }

}
