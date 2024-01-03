package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;

public class GhostNPC extends NTNPC{

    {
        spriteClass = GhostSprite.class;
    }

    @Override
    protected boolean act() {
        if(Statistics.deadGo){
            die(true);
        }
        return super.act();
    }

}
