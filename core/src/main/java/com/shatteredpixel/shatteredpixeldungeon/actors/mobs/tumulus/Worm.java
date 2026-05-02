package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WormSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Worm extends Mob {

    {
        HP = HT = 55;
        defenseSkill = 10;
        EXP = 8;
        maxLvl = 16;
        spriteClass = WormSprite.class;
        baseSpeed = 0.5f;
        properties.add(Property.TUMULUS);

        loot = PotionOfHealing.class;
        lootChance = 0.2f;
    }


    private int stackedDR = 0;
    private static final int BASE_MAX_DEF = 12;
    private static final int MAX_TOTAL_DEF = 48;

    private int getMaxDefense(){
        return Math.min(BASE_MAX_DEF + stackedDR, MAX_TOTAL_DEF);
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        int add = Random.NormalIntRange(0, 6);
        stackedDR = Math.min(stackedDR + add, MAX_TOTAL_DEF - BASE_MAX_DEF);
        return super.defenseProc(enemy, damage);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, getMaxDefense());
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(8, getMaxDefense());
    }

    @Override
    public int attackSkill(Char target) {
        return 22;
    }

    // 掉落逻辑：最多7瓶治疗药，概率递减
    @Override
    public float lootChance(){
        return super.lootChance() * ((7f - Dungeon.LimitedDrops.WORM_HP.count) / 7f);
    }

    @Override
    public Item createLoot(){
        Dungeon.LimitedDrops.WORM_HP.count++;
        return super.createLoot();
    }

    private static final String STACKDR = "stackdr";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
       bundle.put(STACKDR,stackedDR);
    }


    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        stackedDR = bundle.getInt(STACKDR);
    }

    @Override
    public String description() {
        return Messages.get(this,"desc",stackedDR);
    }
}