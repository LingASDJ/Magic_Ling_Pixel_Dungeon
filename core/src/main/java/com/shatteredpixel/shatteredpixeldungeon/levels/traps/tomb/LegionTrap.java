package com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroArcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroGuard;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class LegionTrap extends Trap {

    {
        color = TOMB;
        shape = LARGE_DOT;
        canBeHidden = false;
    }

    @Override
    public void activate() {

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof NecroGuard) {
               ((NecroGuard) mob).resetLegion();
            }
            if (mob instanceof NecroArcher) {
                ((NecroArcher) mob).resetLegion();
            }
        }


        if (Dungeon.level.heroFOV[pos]) {
            GameScene.flash(0x80FF8080);
            Sample.INSTANCE.play(Assets.Sounds.ALERT);
        }

        GLog.w(Messages.get(this, "legion"));
    }

}
