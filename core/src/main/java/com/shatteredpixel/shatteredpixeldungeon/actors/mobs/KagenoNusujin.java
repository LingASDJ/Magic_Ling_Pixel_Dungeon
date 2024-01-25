package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RedTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KagenoNusujinSprite;
import com.watabou.utils.Random;

public class KagenoNusujin extends Thief {

   {
        spriteClass = KagenoNusujinSprite.class;
        properties.add(Property.NOBIG);
        HT = HP = 30;

        defenseSkill = 12;

        EXP = 10;

        maxLvl = 34;

        properties.add(Property.UNDEAD);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        int modifiedDamage;
        if (Random.Int(3) == 0) {
            modifiedDamage = damage + 2;

            RedTrap trapx = new RedTrap();
            trapx.pos = super.pos;
            trapx.activate();

            ShockingTrap trap = new ShockingTrap();
            trap.pos = enemy.pos;
            trap.activate();
            Buff.affect(this, Bleeding.class).set(5f);
        } else {
            modifiedDamage = damage + 4;
            RockfallTrap trap = new RockfallTrap();
            trap.pos = super.pos;
            trap.activate();
        }

        return modifiedDamage;
    }

    public int attackSkill(Char enemy) {
        return 6;
    }

    public int damageRoll() {
        return Random.NormalIntRange(3, 9);
    }

    public int drRoll() {
        return Random.NormalIntRange(0, 3);
    }
}
