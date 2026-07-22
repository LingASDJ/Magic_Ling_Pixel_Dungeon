package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static java.lang.Math.min;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalBuff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class DeathBuff extends ElementalBuff {

    private static final int CAP = 100;
    private static final int LOCK_DURATION = 108;
    private static final int DECAY_TRIGGER = 72;
    private static final int DECAY_INTERVAL = 36;
    private static final int DECAY_INITIAL = 6;
    private static final int DECAY_SUBSEQUENT = 3;
    private static final float DAMAGE_PER_PERCENT = 0.5f; // 100%时+50%伤害

    private int lockTimer = 0;
    private int decayTimer = 0;
    private boolean isLocked = false;

    {
        elementalIcon = BuffIndicator.DEATH;
    }

    public float damageBonus() {
        return (level / 2f) / 100f;
    }

    public void setDecayTimer(int decayTimer) {
        this.decayTimer = decayTimer;
    }

    public void getDeath(int value) {
        decayTimer = min(decayTimer + value, 100);
        hero.sprite.showStatus(0x00ff00, String.valueOf(value));
    }

    @Override
    public void onDamageTaken(float percentHP) {
        if (isLocked) return;

        int gain = Math.round(percentHP / 3f);
        level = Math.min(CAP, level + gain);
        decayTimer = 0;

        if (level == CAP) {
            isLocked = true;
            lockTimer = LOCK_DURATION;
        }
    }


    @Override
    public boolean act() {
        if (!target.isAlive()) {
            detach();
            return true;
        }

        if (isLocked) {
            lockTimer--;
            if (lockTimer <= 0) {
                level = 0;
                isLocked = false;
                decayTimer = 0;
            }
            spend(1);
            return true;
        }

        decayTimer++;
        if (decayTimer == DECAY_TRIGGER) {
            level = Math.max(0, level - DECAY_INITIAL);
        } else if (decayTimer > DECAY_TRIGGER && (decayTimer - DECAY_TRIGGER) % DECAY_INTERVAL == 0) {
            level = Math.max(0, level - DECAY_SUBSEQUENT);
        }

        if (level <= 0) {
            detach();
            return true;
        }

        spend(1);
        return true;
    }

    private static final String LOCK_TIMER = "lockTimer";
    private static final String DECAY_TIMER = "decayTimer";
    private static final String IS_LOCKED = "isLocked";

    @Override
    public String desc() {
        StringBuilder sb = new StringBuilder(Messages.get(this, "desc"));
        sb.append("\n\n");
        sb.append(Messages.get(this, "level", level));
        if (isLocked) {
            sb.append("\n").append(Messages.get(this, "locked", lockTimer));
        } else {
            sb.append("\n").append(Messages.get(this, "bonus", Math.round(damageBonus() * 100)));
            if (decayTimer > 0) {
                int nextDecay = decayTimer < DECAY_TRIGGER ? DECAY_TRIGGER - decayTimer : DECAY_INTERVAL - (decayTimer - DECAY_TRIGGER) % DECAY_INTERVAL;
                sb.append("\n").append(Messages.get(this, "decay_in", nextDecay));
            }
        }
        return sb.toString();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LOCK_TIMER, lockTimer);
        bundle.put(DECAY_TIMER, decayTimer);
        bundle.put(IS_LOCKED, isLocked);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        lockTimer = bundle.getInt(LOCK_TIMER);
        decayTimer = bundle.getInt(DECAY_TIMER);
        isLocked = bundle.getBoolean(IS_LOCKED);
    }
}