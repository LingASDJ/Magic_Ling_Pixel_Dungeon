package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class BloodLoss extends Buff {

    {
        actPriority = HERO_PRIO - 1;

    }

    @Override
    public boolean act() {
        if (Dungeon.depth > 2 && !Dungeon.bossLevel())
            target.damage(target.HT / 20, this);
        if (!target.isAlive()) {
            GLog.n(Messages.get(this, "death"));
            Dungeon.fail(BloodLoss.class);
        }

        int initialTurns = 16;
        int turnsSpent = initialTurns - 2 * (Dungeon.depth / 5);

        if (turnsSpent > 0) {
            spend(turnsSpent);
        } else {
            spend(4f);
        }

        return true;
    }
}
