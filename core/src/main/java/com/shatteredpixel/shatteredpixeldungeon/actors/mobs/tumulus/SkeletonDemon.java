package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkeletonDemonSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SkeletonDemon extends Mob {

    {
        spriteClass = SkeletonDemonSprite.class;

        HP = HT = 75;
        defenseSkill = 10;

        EXP = 10;
        maxLvl = 18;

        loot = MysteryMeat.class;
        lootChance = 0.2f;

        properties.add(Property.UNDEAD);
        properties.add(Property.TUMULUS);
    }

    // 掉落概率衰减：最多7个治疗类掉落，拾取越多概率越低
    @Override
    public float lootChance(){
        return super.lootChance() * ((7f - Dungeon.LimitedDrops.WORM_HP.count) / 7f);
    }

    @Override
    public Item createLoot(){
        // 1/5概率替换为三种怪物掉落之一
        boolean useMobLoot = Random.Float() < 0.2f;
        Item drop;

        if (!useMobLoot) {
            // 不走替换，原生掉落神秘肉
            drop = super.createLoot();
        } else {
            // 筛选可选掉落池，WORM_HP满7则移除墓穴蠕虫掉落
            ArrayList<Integer> pool = new ArrayList<>();
            pool.add(0); // 墓园巨鼠：治疗药水
            if (Dungeon.LimitedDrops.WORM_HP.count < 7) {
                pool.add(1); // 墓穴蠕虫：治疗药水（上限未满才加入池）
            }
            pool.add(2); // 鬼火：火焰药剂

            int pick = Random.element(pool);
            if (pick == 0 || pick == 1) {
                // 巨鼠/蠕虫：治疗药水，占用WORM_HP计数
                drop = new PotionOfHealing();
                Dungeon.LimitedDrops.WORM_HP.count++;
            } else {
                // 鬼火分支：基础燃烧药剂，可概率替换高级X版
                drop = new PotionOfLiquidFlame();
                int replaceCount = Dungeon.LimitedDrops.WISP_PHANTOM_FIRE.count;
                float replaceChance = (float) (1.0 / Math.pow(3, replaceCount + 1));
                if (Random.Float() < replaceChance) {
                    drop = new PotionOfLiquidFlameX();
                    Dungeon.LimitedDrops.WISP_PHANTOM_FIRE.count++;
                }
            }
        }
        return drop;
    }

    private static final float SPLIT_DELAY = 1f;

    int generation = 0; //标记是否为召唤生成的单位，召唤物EXP=0

    private static final String GENERATION = "generation";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(GENERATION, generation);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        generation = bundle.getInt(GENERATION);
        if (generation > 0) EXP = 0;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(5, 20);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (HP >= damage + 2) {
            ArrayList<Integer> candidates = new ArrayList<>();
            int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
            for (int n : neighbours) {
                if (!Dungeon.level.solid[n]
                        && Actor.findChar(n) == null
                        && (Dungeon.level.passable[n] || Dungeon.level.avoid[n])
                        && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[n])) {
                    candidates.add(n);
                }
            }

            if (candidates.size() > 0) {
                Mob summonMob = new SmallSkeletonDemon();

                int spawnPos = Random.element(candidates);
                summonMob.pos = spawnPos;
                summonMob.state = summonMob.HUNTING;

                float hpPercent = Random.Float(0.25f, 0.50f);
                int summonHP = Math.round((HP - damage) * hpPercent);
                summonMob.HP = summonMob.HT = summonHP;
                summonMob.isEndLess = isEndLess;

                generation = this.generation + 1;
                if (generation > 0) {
                    summonMob.maxLvl = -1;
                }

                syncBuffToSummon(summonMob);

                GameScene.add(summonMob, SPLIT_DELAY);
                Actor.add(new Pushing(summonMob, pos, spawnPos));
                Dungeon.level.occupyCell(summonMob);
            }
        }

        return super.defenseProc(enemy, damage);
    }

//    private Mob createRandomSummon() {
//        Mob mob;
//        int roll = Random.Int(3);
//        switch (roll) {
//            case 1:
//                mob = new Wisp();
//            break;
//            case 2:
//                mob = new Worm();
//                break;
//            default:
//                mob = new GraveRat();
//                break;
//        };
//        return mob;
//    }

    private void syncBuffToSummon(Mob targetMob) {
        if (buff(Poison.class) != null) {
            Buff.affect(targetMob, Poison.class).set(2);
        }
        for (Buff b : buffs(AllyBuff.class)) {
            Buff.affect(targetMob, b.getClass());
        }
        for (Buff b : buffs(ChampionEnemy.class)) {
            Buff.affect(targetMob, b.getClass());
        }
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
    }
}