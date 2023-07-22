package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SakaFishBossSprites;

public class SakaFishBoss extends Boss {

    {
        spriteClass = SakaFishBossSprites.class;
        initBaseStatus(19, 25, 6, 0, 700, 5, 12);
        state = SLEEPING;
        initProperty();
        initStatus(76);
        HP=700;
        defenseSkill = 10;
        HT=700;
        baseSpeed=1.5f;
        viewDistance = 30;
    }


}
