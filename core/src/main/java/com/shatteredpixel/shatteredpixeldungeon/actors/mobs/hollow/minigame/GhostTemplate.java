package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MiniGhostSprite;
import com.watabou.utils.Bundle;

abstract public class GhostTemplate extends Mob {

    public boolean active = false;

    {
        immunities.add(Buff.class);
        immunities.add(Blob.class);
    }

    @Override
    public void move(int step) {
        super.move(step);
        Mob mob = GhostTemplate.this;
        if(active){
            ((MiniGhostSprite)mob.sprite).moveGet();
        } else {
            ((MiniGhostSprite)mob.sprite).moveNow();
        }
    }

    private static final String ACTIVE = "ghost_active";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ACTIVE,active);
    }


    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        active = bundle.getBoolean(ACTIVE);
    }

}
