package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.sprites.LanFireSprites;

public class LanFire extends NPC {
    {
        spriteClass = LanFireSprites.class;
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
    }
}
