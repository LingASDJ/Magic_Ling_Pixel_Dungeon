package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KusumiMagicGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class KusumiMagicGirl extends Mob {

    public boolean first=true;

    {
        HP = HT = 1;
        spriteClass = KusumiMagicGirlSprites.class;
        properties.add(Char.Property.UNKNOWN);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    @Override
    public void move(int step) {

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
    public void damage(int dmg, Object src, DamageType type) {
        super.damage(dmg, src, type);
        if(first){
            if (enemy == null && src instanceof Char) {
                enemy = (Char) src;
            }
            if (enemy != null && enemy.isAlive() && enemy != this) {
                enemy.damage(10,this,DamageType.REAL);
                GLog.n(Messages.get(this, "ha"));
                first = false;
            }
        }
    }

    @Override
    protected boolean act() {
        if(HP == 0){
            sprite.showAlert();
            selfTeleCooldown--;
            if(!teleporting){
                ((KusumiMagicGirlSprites) sprite).teleParticles(true);
                teleporting = true;
            }
            if(selfTeleCooldown == 0) {
                ScrollOfTeleportation.appear(this, Dungeon.level.randomRespawnCell(this));
                destroy();
                sprite.killAndErase();
                if (enemy != null && enemy.isAlive() && enemy != this) {
                    enemy.damage(10,this,DamageType.REAL);
                    GLog.n(Messages.get(this, "ha2"));
                }
            }
        }

        return super.act();
    }

    private boolean teleporting = false;
    private int selfTeleCooldown = 2;

    private static final String TELEPORTING = "teleporting";
    private static final String FIRST = "first";
    private static final String SELF_COOLDOWN = "self_cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TELEPORTING, teleporting);
        bundle.put(SELF_COOLDOWN, selfTeleCooldown);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        teleporting = bundle.getBoolean( TELEPORTING );
        selfTeleCooldown = bundle.getInt( SELF_COOLDOWN );
        first = bundle.getBoolean(FIRST);
    }
}

