package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireDragonSprite;

public class FireDragon extends Boss {
    {
        initProperty();
        initBaseStatus(10, 20, 15, 15, 300, 8, 12);
        initStatus(60);
        spriteClass = FireDragonSprite.class;
    }
}
