package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DwarfGeneralSprite;

public class DwarfGeneral extends Boss {

    {
        initProperty();
        initBaseStatus(12, 24, 23, 18, 1000, 4, 10);
        initStatus(40);

        properties.add(Property.DEMONIC);

        spriteClass = DwarfGeneralSprite.class;
    }



}
