package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EgretSprite;

public class Egret extends Boss {
    {
        initProperty();
        initBaseStatus(0, 0, 20, 15, 800, 0, 8);
        initStatus(30);
        spriteClass = EgretSprite.class;
        immunities.add(Bleeding.class);
        properties.add(Property.ICY);
    }
}
