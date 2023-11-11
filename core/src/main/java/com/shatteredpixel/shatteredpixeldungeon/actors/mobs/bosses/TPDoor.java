package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TPDoorSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class TPDoor extends Mob {

    {
        spriteClass = TPDoorSprites.class;

        HP = HT = 100;

        properties.add(Property.BOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.ELECTRIC);
        properties.add(Property.IMMOVABLE);

        state = PASSIVE;

        baseSpeed =0;
    }

    private int kill = 0;

    @Override
    public void damage(int dmg, Object src) {



        if (dmg >= 20){
            //20
            dmg = 20;
            if (hero.buff(DiamondKnight.DiedDamager.class) == null) {
                Buff.affect(this, DiamondKnight.DiedDamager.class);
                TeleportationTrap run = new TeleportationTrap();
                run.pos = super.pos;
                run.activate();
            }
        } else {
            dmg = 0;
            if(kill == 0){
                GLog.n(Messages.get(this,"lowdamage"));
                kill = 10;
            } else {
                kill--;
            }
        }
        super.damage(dmg, src);
    }

    @Override
    public void die( Object cause ) {

        super.die(cause);
        GameScene.flash(0x808080);
        Statistics.TPDoorDieds = true;
        ((ColdChestBossLevel) Dungeon.level).progress();
    }

}

