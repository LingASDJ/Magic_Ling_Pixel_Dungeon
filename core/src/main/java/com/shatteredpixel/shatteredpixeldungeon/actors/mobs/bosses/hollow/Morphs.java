package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MorpheusSprite;

public class Morphs extends Boss {

    /**
     * 无敌判定
     * @param effect 无敌效果
     * @return true:无敌
     */
    @Override
    public boolean isInvulnerable(Class effect) {
        return true;
    }

    public boolean FourToneActive = false;

    {
        initProperty();
        initBaseStatus(0, 0, 0, 0, 1000, 0, 0);
        initStatus(0);

        spriteClass = MorpheusSprite.class;

        viewDistance = 100;

        properties.add(Property.BOSS);
    }



}
