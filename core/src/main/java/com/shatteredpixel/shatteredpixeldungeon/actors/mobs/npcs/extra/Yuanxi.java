package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YuanxiSprites;
import com.watabou.utils.Bundle;

public class Yuanxi extends NTNPC {

    {
        spriteClass = YuanxiSprites.class;
        properties.add(Property.UNKNOWN);
    }

    @Override
    public synchronized boolean isAlive() {
        return true;
    }

    @Override
    public int damageRoll() {
        return 0;
    }

    @Override
    protected boolean act() {
        if(Dungeon.level.heroFOV[pos]){
            sprite.showAlert();
            selfTeleCooldown--;
            if(!teleporting){
                ((YuanxiSprites) sprite).teleParticles(true);
                teleporting = true;
            }
            if(selfTeleCooldown == 0) {
                ScrollOfTeleportation.appear(this, Dungeon.level.randomRespawnCell(this));
                destroy();
                sprite.killAndErase();
            }
        }


        return super.act();
    }

    private boolean teleporting = false;
    private int selfTeleCooldown = 2;

    private static final String TELEPORTING = "teleporting";
    private static final String SELF_COOLDOWN = "self_cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TELEPORTING, teleporting);
        bundle.put(SELF_COOLDOWN, selfTeleCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        teleporting = bundle.getBoolean( TELEPORTING );
        selfTeleCooldown = bundle.getInt( SELF_COOLDOWN );
    }
}
