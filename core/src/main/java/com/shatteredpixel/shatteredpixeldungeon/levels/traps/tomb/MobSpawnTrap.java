package com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class MobSpawnTrap extends Trap {

    {
        color = TOMB;
        shape = STARS;
        canBeHidden = false;
    }

    @Override
    public void activate() {

        ArrayList<Class<? extends Mob>> mobPool = MobSpawner.getMobRotation(Dungeon.depth);

        int spawnedCount = 0;

        for (Class<? extends Mob> mobClass : mobPool) {

            Mob mob = Reflection.newInstance(mobClass);
            if (mob == null) continue;

            if(Random.Int(16)<8){
                ChampionEnemy.rollForChampion(mob);
                ChampionEnemy.rollForStateLing(mob);
            }

            int spawnPos = Dungeon.level.randomRespawnCell(mob);
            if (spawnPos == -1) break;

            mob.pos = spawnPos;
            mob.state = mob.WANDERING;

            GameScene.add(mob);
            spawnedCount++;
        }

        if (spawnedCount > 0) {
            GLog.w(Messages.get(this, "res", spawnedCount));
        } else {
            GLog.w(Messages.get(this, "res_none"));
        }

        if (Dungeon.level.heroFOV[pos]) {
            GameScene.flash(0x80FFFFFF);
            Sample.INSTANCE.play(Assets.Sounds.DEATH);
        }
    }
}