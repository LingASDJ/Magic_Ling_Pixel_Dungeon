package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CrossTownProc;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrossDiedSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class CrossDiedTower extends Mob {

    {
        spriteClass = CrossDiedSprites.class;

        HP = HT = 1;
        maxLvl = -1;

        properties.add(Property.MINIBOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.ELECTRIC);
        properties.add(Property.IMMOVABLE);

        state = HUNTING;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public void add( Buff buff ) {
    }

    @Override
    public boolean act() {

        if (Dungeon.level.heroFOV[pos]) {
            spend(TICK);
        } else {
            //失去范围
            for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
                if (mob instanceof CrossDiedTower) {
                    mob.die( true );
                }
            }
            for (Buff buff : hero.buffs()) {
                if (buff instanceof CrossTownProc) {
                    buff.detach();
                }
            }
            GLog.n(Messages.get(this,"dead"));
        }
        return true;
    }

}
