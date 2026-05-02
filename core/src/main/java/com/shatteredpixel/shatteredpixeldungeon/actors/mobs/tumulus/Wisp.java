package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WispSprite;
import com.watabou.utils.Random;

public class Wisp extends Mob {

    {
        HP = HT = 25;
        defenseSkill = 50;
        EXP = 9;
        maxLvl = 16;

        spriteClass = WispSprite.class;

        properties.add(Property.TUMULUS);
        properties.add(Property.DEMONIC);

        loot = PotionOfLiquidFlame.class;
        lootChance = 0.1428f;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        Buff.affect( enemy, HalomethaneBurning.class ).reignite( enemy, Random.Int(2,6) );
        return super.attackProc(enemy, damage);
    }

    @Override
    public Item createLoot() {
        Item drop = new PotionOfLiquidFlame();

        int replaceCount = Dungeon.LimitedDrops.WISP_PHANTOM_FIRE.count;
        float replaceChance = (float) (1.0 / Math.pow(3, replaceCount + 1));

        if (Random.Float() < replaceChance) {
            drop = new PotionOfLiquidFlameX();
            Dungeon.LimitedDrops.WISP_PHANTOM_FIRE.count++;
        }

        return drop;
    }

    @Override
    public int attackSkill(Char target) {
        return 21;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(8, 15);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }



}
