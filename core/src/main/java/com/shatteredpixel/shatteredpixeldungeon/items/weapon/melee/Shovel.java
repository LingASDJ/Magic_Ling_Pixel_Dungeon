package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BuffsOringinForWeapon.PreventTombWraithSpawn;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

//铲子
//二阶，力量需求12
//初始2-15，成长1-3
//在使用此武器击杀敌人后，有（20+3*等级）%概率在那个位置生成一堆残骸，手持此武器时打开坟墓不会生成怨灵。
//挖掘生者的坟墓。
//武技：寻宝，消耗2充能，在原地发掘一个普通宝箱。发掘时有5%概率发掘出可以直接打开的金宝箱！每一层至多被寻宝5次。

public class Shovel extends MeleeWeapon{
    {
        image = ItemSpriteSheet.SHOVEL;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 2;
    }

    @Override
    public int max(int lvl) { return 15 + lvl * 3; }
    @Override
    public int min(int lvl) { return 2 + lvl; }

    // ========== 关于怨灵结界 ==========

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        Buff.affect(ch, PreventTombWraithSpawn.class);   // 装备上自动挂
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        boolean result = super.doUnequip(hero, collect, single);
        if (result) {
            Buff.detach(hero, PreventTombWraithSpawn.class);  // 卸下自动删buff
        }
        return result;
    }

    // ========== 关于遗骸 ==========

    // 生成遗骸堆的概率判定
    private boolean dropRoll(){
        return Random.Float() < 0.2 + 0.03 * buffedLvl();
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        final Char def = defender;

        // 击杀刷遗骸
        Actor.add(new Actor() {
            {
                actPriority = VFX_PRIO;
            }

            @Override
            protected boolean act() {
                if (!def.isAlive() && dropRoll()) {
                    Heap heap = Dungeon.level.drop(Generator.random(), def.pos);
                    heap.setHauntedIfCursed();
                    heap.type = Heap.Type.SKELETON;
                    heap.sprite.view( heap );
                }
                Actor.remove(this);
                return true;
            }
        });

        return super.proc(attacker, defender, damage);
    }

    // ========== 武技：寻宝 ==========
    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 2;
    }  // 基础充能消耗

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        // 判定还能不能挖
        if (Dungeon.level.canBeFoundTreasure <= 0)
        {
            GLog.w(Messages.get(this,"fail_to_find_treasure"));
            return;
        }
        // 先扣充能：beforeAbilityUsed 会按 baseChargeUse 的返回值扣掉对应充能
        beforeAbilityUsed(hero, null);
        // 播放使用动作，并消耗一个回合
        hero.sprite.operate(hero.pos);
        // 5% 概率挖出“金宝箱”（用上锁宝箱也就是金宝箱外观，但实际上无需钥匙）
        boolean golden = Random.Float() < 0.05f;
        // 生成一件宝箱物品
        Item reward = genChestItem(golden);
        GLog.b(Messages.get(this,"success_to_find_treasure"));
        // 放到英雄脚下
        Heap heap = Dungeon.level.drop(reward, hero.pos);
        // 决定箱子的皮肤与描述
        if (golden) {
            heap.type = Heap.Type.GOLDEN_CHEST;
        } else {
            heap.type = Heap.Type.CHEST;
        }
        // 判定是否诅咒
        heap.setHauntedIfCursed();
        // 显示外观
        heap.sprite.view( heap );
        heap.sprite.drop();
        // 武技收尾：处理与武技相关的天赋联动
        Dungeon.level.canBeFoundTreasure--;
        afterAbilityUsed(hero);
    }

    // ========== 一些详细实现 ==========

    // 加入贵重判定的物品生成，最终就用这个
    private Item genChestItem(boolean golden){
        Item reward;
        if(golden){
            do {
                reward = tryGenChestItem();
            } while (!isValuableChestItem(reward));  // 物品如果是不贵重的物品就重抽一次
        }
        else{
            reward = tryGenChestItem();
        }
        return reward;
    }

    // 物品生成
    private Item tryGenChestItem() {
        Item reward;
        do {
            // 从金币、飞镖（法杖）、护甲、武器、戒指随机抽一个
            switch (Random.Int(5)) {
                case 0:  reward = new Gold().random();                       break;
                case 1:  reward = Generator.randomMissile();                 break;
                case 2:  reward = Generator.randomArmor();                   break;
                case 3:  reward = Generator.randomWeapon();                  break;
                default: reward = Generator.random(Generator.Category.RING); break;
            }
        } while (reward == null || Challenges.isItemBlocked(reward));  // 物品如果为空或是被挑战锁了的物品就重抽一次
        return reward;
    }

    // 贵重判定：仿 RegularLevel.java:739-740
    private boolean isValuableChestItem(Item item) {
        // 神器：50% 概率算贵重
        // 可升级装备：概率 1/(4 - level)，等级越高越容易触发
        return (item instanceof Artifact && Random.Int(2) == 0)
                || (item.isUpgradable() && Random.Int(4 - item.level()) == 0);
    }

    // 武技描述
    @Override
    public String abilityInfo() {
        int canBeFoundTimes = levelKnown ? Dungeon.level.canBeFoundTreasure: 5;
        if (levelKnown){
            return Messages.get(this, "ability_desc", canBeFoundTimes);
        } else {
            return Messages.get(this, "typical_ability_desc", canBeFoundTimes);
        }
    }
}
