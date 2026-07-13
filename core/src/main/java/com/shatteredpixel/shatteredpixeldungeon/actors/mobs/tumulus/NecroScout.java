package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecroScoutSprite;
import com.watabou.utils.Random;

public class NecroScout extends Mob {

    {
        HP = HT = 45;
        defenseSkill = 25;
        EXP = 9;
        maxLvl = 17;

        baseSpeed = 1.5f;

        spriteClass = NecroScoutSprite.class;

        properties.add(Property.TUMULUS);

        loot = Random.Float() < 0.5f ? Generator.randomArmor() : Generator.randomWeapon();
        lootChance = 0.2f;
    }

    @Override
    public int attackProc(Char enemy, int damage) {

        int bleedDmg = Random.NormalIntRange(2, 5);
        Buff.affect(enemy, Bleeding.class).set(bleedDmg);

        int oppositeAdjacent = enemy.pos + (enemy.pos - pos);
        Ballistica trajectory = new Ballistica(enemy.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);

        WandOfBlastWave.throwChar(this, trajectory, 2, false, false, getClass());

        return super.attackProc(enemy, damage);
    }

    @Override
    public float lootChance(){
        return super.lootChance() * ((3f - Dungeon.LimitedDrops.NSR.count) / 3f);
    }

    @Override
    public Item createLoot(){
        Dungeon.LimitedDrops.NSR.count++;
        return super.createLoot();
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(9, 25);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 3);
    }

}
