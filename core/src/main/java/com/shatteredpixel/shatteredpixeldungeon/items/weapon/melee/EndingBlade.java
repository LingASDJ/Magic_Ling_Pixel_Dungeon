package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * 终焉 - 5阶彩蛋武器
 *
 * 核心特性：
 * - 必定诅咒，无法强化/附魔/净化/嬗变
 * - 装备后无法取下
 * - 基础攻击20（固定值），成长4
 * - 力量需求5，攻速固定为1
 * - 命中率根据等级变化：25% → 50% → 100%
 * - 即死效果概率：20% → 22% → 25% → 30%
 *
 * 浊焰能量系统：
 * - 每次攻击+1（每层最多层数*10），击杀+5
 * - 升级公式：每 8x+25 点能量升一级，上限25级
 *
 * 等级效果：
 * L1-3: 每15回合自身燃烧1回合，即死22%
 * L4-6: 4格灵视，即死25%
 * L7-9: 吞天(10%概率吸血25%)，即死25%
 * L10: 幻惑，命中率50%
 * L11-12: 虚弱，攻击距离3
 * L13-14: 魔法伤害，命中率100%
 * L15+: 即死30%，灵视6格，升级给予易伤/恍惚/失明，护甲2正2诅附魔，武器随机附魔(x-14)种，击杀回血(5+x-20)%
 *
 * 主动技能：
 * Top1(L6+): 3x3恐惧+30魔法伤害，CD5，耗血10%
 * Top2(L10+): 死亡诅咒区域，CD30，耗血30%
 * Top3(L15+): 净化debuff+赐福/充能/精力充沛/急速+浊焰攻心，耗血60%，每局1次
 *
 * 特殊机制：
 * - 尝试净化触发浊焰审判（怪物伤害x2/血量x2/移速*0.85，玩家血量上限降至50%）
 */
public class EndingBlade extends MeleeWeapon {

    {
        image = ItemSpriteSheet.ENDDIED;
        tier = 5;
        cursed = true;
        enchant(Enchantment.randomCurse());
    }

    // ==================== 动作常量 ====================
    public static final String AC_PERFECT_SHOT = "perfect_shot";   // 咔！完美镜头！
    public static final String AC_ACTOR_ENTRY = "actor_entry";     // 咔！演员入场！
    public static final String AC_READY_WRAP = "ready_wrap";       // 咔！准备杀青！

    // ==================== Bundle键 ====================
    private static final String FIRE_ENERGY = "fire_energy";
    private static final String COOLDOWN_TOP1 = "cooldown_top1";
    private static final String COOLDOWN_TOP2 = "cooldown_top2";
    private static final String TOP3_USED = "top3_used";
    private static final String TRIAL_MODE = "trial_mode";
    private static final String TURN_COUNTER = "turn_counter";
    private static final String DUNGEON_DEPTH = "dungeon_depth";
    private static final String ATTACK_ENERGY_THIS_FLOOR = "attack_energy_this_floor";

    // ==================== 核心属性 ====================
    /** 浊焰能量 */
    public int fireEnergy = 0;

    /** Top1冷却 */
    private int cooldownTop1 = 0;
    /** Top2冷却 */
    private int cooldownTop2 = 0;
    /** Top3是否已使用 */
    public boolean top3Used = false;
    /** 浊焰审判模式 */
    public boolean trialMode = false;
    /** 回合计数器 */
    private int turnCounter = 0;
    /** 记录当前层数（用于攻击能量上限） */
    private int dungeonDepth = 0;
    /** 本层通过攻击获得的能量 */
    private int attackEnergyThisFloor = 0;

    // ==================== 常量 ====================
    /** 等级上限 */
    public static final int MAX_LEVEL = 25;
    /** 灵视范围 - L4-6 */
    public static final int MINDVISION_RANGE_LOW = 4;
    /** 灵视范围 - L15+ */
    public static final int MINDVISION_RANGE_HIGH = 6;

    // ==================== 序列化 ====================

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRE_ENERGY, fireEnergy);
        bundle.put(COOLDOWN_TOP1, cooldownTop1);
        bundle.put(COOLDOWN_TOP2, cooldownTop2);
        bundle.put(TOP3_USED, top3Used);
        bundle.put(TRIAL_MODE, trialMode);
        bundle.put(TURN_COUNTER, turnCounter);
        bundle.put(DUNGEON_DEPTH, dungeonDepth);
        bundle.put(ATTACK_ENERGY_THIS_FLOOR, attackEnergyThisFloor);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        fireEnergy = bundle.getInt(FIRE_ENERGY);
        cooldownTop1 = bundle.getInt(COOLDOWN_TOP1);
        cooldownTop2 = bundle.getInt(COOLDOWN_TOP2);
        top3Used = bundle.getBoolean(TOP3_USED);
        trialMode = bundle.getBoolean(TRIAL_MODE);
        turnCounter = bundle.getInt(TURN_COUNTER);
        dungeonDepth = bundle.getInt(DUNGEON_DEPTH);
        attackEnergyThisFloor = bundle.getInt(ATTACK_ENERGY_THIS_FLOOR);
    }

    // ==================== 动作列表 ====================

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero)) {
            int lvl = getRealLevel();
            if (lvl >= 6) {
                actions.add(AC_PERFECT_SHOT);
            }
            if (lvl >= 10) {
                actions.add(AC_ACTOR_ENTRY);
            }
            if (lvl >= 15) {
                actions.add(AC_READY_WRAP);
            }
        }
        return actions;
    }

    // ==================== 等级系统 ====================

    /**
     * 获取武器真实等级（基于浊焰能量）
     * 升级公式：每 8x+25 点浊焰能量升一级，上限25
     */
    public int getRealLevel() {
        int calculated = calculateLevel(fireEnergy);
        return Math.min(calculated, MAX_LEVEL);
    }

    /**
     * 根据浊焰能量计算等级
     */
    private int calculateLevel(int energy) {
        int level = 0;
        int remaining = energy;
        while (remaining >= 8 * level + 25) {
            remaining -= 8 * level + 25;
            level++;
        }
        return level;
    }

    /**
     * 获取升级到下一级所需的浊焰能量
     */
    private int energyToNextLevel(int currentLevel) {
        return 8 * currentLevel + 25;
    }

    @Override
    public int level() {
        return getRealLevel();
    }

    // ==================== 描述 ====================

    @Override
    public String desc() {
        StringBuilder desc = new StringBuilder();
        desc.append(Messages.get(this, "desc"));
        desc.append("\n\n").append(Messages.get(this, "energy_desc", fireEnergy, getRealLevel()));

        int currentLvl = getRealLevel();
        int accumulated = accumulatedEnergyToLevel(currentLvl);
        int nextLevelNeed = energyToNextLevel(currentLvl);
        int currentProgress = fireEnergy - accumulated;
        int remaining = Math.max(0, nextLevelNeed - currentProgress);

        desc.append("\n").append(Messages.get(this, "next_level", remaining));

        if (trialMode) {
            desc.append("\n\n").append(Messages.get(this, "trial_mode_active"));
        }

        if (currentLvl >= 1) {
            desc.append("\n\n").append(Messages.get(this, "current_effects"));
            if (currentLvl >= 1 && currentLvl <= 3) {
                desc.append("\n").append(Messages.get(this, "effect_l1_3"));
            }
            if (currentLvl >= 4 && currentLvl <= 6) {
                desc.append("\n").append(Messages.get(this, "effect_l4_6"));
            }
            if (currentLvl >= 7 && currentLvl <= 9) {
                desc.append("\n").append(Messages.get(this, "effect_l7_9"));
            }
            if (currentLvl >= 10) {
                desc.append("\n").append(Messages.get(this, "effect_l10"));
            }
            if (currentLvl >= 11 && currentLvl <= 12) {
                desc.append("\n").append(Messages.get(this, "effect_l11_12"));
            }
            if (currentLvl >= 13 && currentLvl <= 14) {
                desc.append("\n").append(Messages.get(this, "effect_l13_14"));
            }
            if (currentLvl >= 15) {
                desc.append("\n").append(Messages.get(this, "effect_l15_plus"));
            }
        }

        return desc.toString();
    }

    /**
     * 计算升到当前等级所需的累计能量
     */
    private int accumulatedEnergyToLevel(int targetLevel) {
        int total = 0;
        for (int i = 0; i < targetLevel; i++) {
            total += 8 * i + 25;
        }
        return total;
    }

    // ==================== 动作执行 ====================

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        switch (action) {
            case AC_PERFECT_SHOT:
                executePerfectShot(hero);
                break;
            case AC_ACTOR_ENTRY:
                executeActorEntry(hero);
                break;
            case AC_READY_WRAP:
                executeReadyWrap(hero);
                break;
        }
    }

    // ==================== Top1: 咔！完美镜头！ ====================

    /**
     * 对周围3x3区域造成恐惧+30点魔法伤害
     * 冷却5回合，消耗最大生命值10%
     */
    private void executePerfectShot(Hero hero) {
        if (cooldownTop1 > 0) {
            GLog.n(Messages.get(this, "cooldown_top1", cooldownTop1));
            return;
        }

        int hpCost = Math.max(1, Math.round(hero.HT * 0.10f));
        if (hero.HP <= hpCost) {
            GLog.n(Messages.get(this, "not_enough_hp"));
            return;
        }

        hero.damage(hpCost, this);

        for (int i : PathFinder.NEIGHBOURS9) {
            int cell = hero.pos + i;
            if (cell >= 0 && cell < Dungeon.level.length()) {
                Char ch = Actor.findChar(cell);
                if (ch != null && ch != hero && ch.alignment != Char.Alignment.ALLY) {
                    Buff.affect(ch, Terror.class, Terror.DURATION);
                    ch.damage(30, new Eye.DeathGaze());
                }
                CellEmitter.get(cell).burst(Speck.factory(Speck.LIGHT), 4);
            }
        }

        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        hero.sprite.operate(hero.pos);
        GLog.p(Messages.get(this, "perfect_shot_used"));

        cooldownTop1 = 5;
        hero.spendAndNext(Actor.TICK);
    }

    // ==================== Top2: 咔！演员入场！ ====================

    /**
     * 对指定区域施展死亡诅咒
     * 冷却30回合，消耗最大生命值30%
     */
    private void executeActorEntry(Hero hero) {
        if (cooldownTop2 > 0) {
            GLog.n(Messages.get(this, "cooldown_top2", cooldownTop2));
            return;
        }

        int hpCost = Math.max(1, Math.round(hero.HT * 0.30f));
        if (hero.HP <= hpCost) {
            GLog.n(Messages.get(this, "not_enough_hp"));
            return;
        }

        curUser = hero;
        curItem = this;
        GameScene.selectCell(actorEntrySelector);
    }

    private CellSelector.Listener actorEntrySelector = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target == null) return;

            Hero hero = Dungeon.hero;
            int hpCost = Math.max(1, Math.round(hero.HT * 0.30f));
            hero.damage(hpCost, EndingBlade.this);

            DeathCurse curse = Buff.affect(hero, DeathCurse.class);
            curse.setPos(target);
            curse.setDuration(5);

            CellEmitter.get(target).burst(Speck.factory(Speck.EVOKE), 8);
            Sample.INSTANCE.play(Assets.Sounds.CURSED);
            GLog.p(Messages.get(EndingBlade.this, "actor_entry_used"));

            cooldownTop2 = 30;
            hero.spendAndNext(Actor.TICK);
        }

        @Override
        public String prompt() {
            return Messages.get(EndingBlade.this, "prompt_actor_entry");
        }
    };

    // ==================== Top3: 咔！准备杀青！ ====================

    /**
     * 移除debuff，给予赐福/充能/精力充沛/急速，然后浊焰攻心
     * 消耗最大生命值60%，每局一次
     */
    private void executeReadyWrap(Hero hero) {
        if (top3Used) {
            GLog.n(Messages.get(this, "top3_already_used"));
            return;
        }

        int hpCost = Math.max(1, Math.round(hero.HT * 0.60f));
        if (hero.HP <= hpCost) {
            GLog.n(Messages.get(this, "not_enough_hp"));
            return;
        }

        hero.damage(hpCost, this);

        // 移除所有负面buff
        for (Buff buff : hero.buffs().toArray(new Buff[0])) {
            if (buff.type == Buff.buffType.NEGATIVE) {
                buff.detach();
            }
        }

        // 给予正面buff（20回合）
        Buff.affect(hero, Bless.class, 20f);
        Buff.affect(hero, Recharging.class, 20f);
        Buff.affect(hero, Stamina.class, 20f);
        Buff.affect(hero, Haste.class, 20f);

        // 浊焰攻心（30回合）
        Buff.affect(hero, TurbulentFlameHeart.class, 30f);

        hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 8);
        CellEmitter.get(hero.pos).start(ShaftParticle.FACTORY, 0.2f, 5);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);

        GLog.p(Messages.get(this, "ready_wrap_used"));
        top3Used = true;
        hero.spendAndNext(Actor.TICK);
    }

    // ==================== 武器核心属性 ====================

    @Override
    public int STRReq(int lvl) {
        return 5;
    }

    @Override
    public float speedFactor(Char owner) {
        return 1f;
    }

    @Override
    public int min(int lvl) {
        return 20 + lvl * 4;
    }

    @Override
    public int max(int lvl) {
        return 20 + lvl * 4;
    }

    @Override
    public float accuracyFactor(Char owner, Char target) {
        int lvl = getRealLevel();
        if (lvl >= 13) {
            return super.accuracyFactor(owner, target);
        } else if (lvl >= 10) {
            return super.accuracyFactor(owner, target) * 0.50f;
        } else {
            return super.accuracyFactor(owner, target) * 0.25f;
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    /**
     * 尝试净化时触发浊焰审判模式
     */
    public void onAttemptCleanse() {
        if (!trialMode) {
            trialMode = true;
            hero.updateHT(false);
            GLog.n(Messages.get(this, "trial_mode_triggered"));
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            GameScene.fadeToBlack(1f,2f);
        }
    }

    // ==================== 装备/卸载处理 ====================

    /**
     * 装备后无法取下
     */
    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        GLog.n(Messages.get(this, "cannot_unequip"));
        return false;
    }

    @Override
    public boolean doEquip(Hero hero) {
        boolean result = super.doEquip(hero);
        if (result) {
            applyLevelBuffs(hero);
        }
        return result;
    }

    // ==================== 攻击处理 ====================

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 每次攻击+1浊焰能量（每层最多层数*10）
        if (dungeonDepth != Dungeon.depth) {
            dungeonDepth = Dungeon.depth;
            attackEnergyThisFloor = 0;
        }
        int maxAttackEnergy = dungeonDepth * 10;
        if (attackEnergyThisFloor < maxAttackEnergy) {
            fireEnergy += 1;
            attackEnergyThisFloor += 1;
        }

        int lvl = getRealLevel();

        // 击杀判定
        if (defender.HP <= damage) {
            fireEnergy += 5;

            // Level15+：击杀回复生命值
            if (lvl >= 15 && attacker instanceof Hero) {
                int healPercent = 5 + (lvl - 20);
                if (healPercent > 0) {
                    int healAmount = Math.max(1, Math.round(attacker.HT * healPercent / 100f));
                    attacker.HP = Math.min(attacker.HT, attacker.HP + healAmount);
                    attacker.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
                }
            }
        }

        // 即死效果
        float instakillChance = getInstakillChance(lvl);
        if (Random.Float() < instakillChance && defender != attacker && defender.isAlive()) {
            defender.die(this);
            GLog.n(Messages.get(this, "instakill", defender.name()));
            CellEmitter.get(defender.pos).burst(Speck.factory(Speck.LIGHT), 6);
        }

        // Level7-9：吞天（10%概率吸血25%）
        if (lvl >= 7 && lvl <= 9 && Random.Float() < 0.10f && defender != attacker) {
            int heal = Math.max(1, Math.round(damage * 0.25f));
            attacker.HP = Math.min(attacker.HT, attacker.HP + heal);
            attacker.sprite.emitter().burst(Speck.factory(Speck.HEALING), 4);
            GLog.p(Messages.get(this, "devour_heaven", heal));
        }

        // Level15+：随机附魔效果
        if (lvl >= 15 && defender != attacker) {
            int extraEnchantments = lvl - 14;
            for (int i = 0; i < extraEnchantments; i++) {
                if (Random.Float() < 0.3f) {
                    Enchantment ench = Random.Int(2) == 0
                            ? Enchantment.random()
                            : Enchantment.randomCurse();
                    ench.proc(this, attacker, defender, damage);
                }
            }
        }

        return super.proc(attacker, defender, damage);
    }

    private float getInstakillChance(int lvl) {
        if (lvl >= 15) return 0.30f;
        else if (lvl >= 4) return 0.25f;
        else if (lvl >= 1) return 0.22f;
        else return 0.20f;
    }

    // ==================== 等级Buff管理 ====================

    private void applyLevelBuffs(Hero hero) {
        int lvl = getRealLevel();

        // Level4-6：4格灵视
        if (lvl >= 4 && lvl <= 6) {
            Buff.affect(hero, EndingBladeMindVision.class, 2f).setRange(MINDVISION_RANGE_LOW);
        }

        // Level10：幻惑
        if (lvl >= 10) {
            Buff.affect(hero, Vertigo.class, 2f);
        }

        // Level11-12：虚弱
        if (lvl >= 11 && lvl <= 12) {
            Buff.affect(hero, Weakness.class, 2f);
        }

        // Level15+：灵视6格 + 虚弱 + 每次升级给予易伤/恍惚/失明
        if (lvl >= 15) {
            Buff.affect(hero, EndingBladeMindVision.class, 2f).setRange(MINDVISION_RANGE_HIGH);
            Buff.affect(hero, Weakness.class, 2f);
            Buff.affect(hero, Vulnerable.class, 2f);
            Buff.affect(hero, Drowsy.class, 2f);
            Buff.affect(hero, Blindness.class, 2f);
        }
    }

    private void removeLevelBuffs(Hero hero) {
        Buff.detach(hero, EndingBladeMindVision.class);
        Buff.detach(hero, Vertigo.class);
        Buff.detach(hero, Weakness.class);
        Buff.detach(hero, Vulnerable.class);
        Buff.detach(hero, Drowsy.class);
        Buff.detach(hero, Blindness.class);
    }

    // ==================== 每回合更新 ====================

    /**
     * 由游戏系统每回合调用
     */
    public void onTurnUpdate(Hero hero) {
        turnCounter++;

        if (cooldownTop1 > 0) cooldownTop1--;
        if (cooldownTop2 > 0) cooldownTop2--;

        int lvl = getRealLevel();

        // Level1-3：每15回合自身燃烧1回合
        if (lvl >= 1 && lvl <= 3 && turnCounter % 15 == 0) {
            Buff.affect(hero, Burning.class).reignite(hero, 1.0f);
            GLog.n(Messages.get(this, "self_burning"));
        }

        // 持续刷新等级buff
        applyLevelBuffs(hero);
    }

    // ==================== 攻击距离 ====================

    @Override
    public int reachFactor(Char owner) {
        int lvl = getRealLevel();
        if (lvl >= 11) {
            return 3;
        }
        return super.reachFactor(owner);
    }

    // ==================== 伤害类型 ====================

    /**
     * Level13-14：攻击变为魔法伤害
     */
    @Override
    public int damageRoll(Char owner) {
        int dmg = super.damageRoll(owner);
        int lvl = getRealLevel();
        if (lvl >= 13 && lvl <= 14) {
            // 魔法伤害标记
        }
        return dmg;
    }

    // ==================== 信息面板 ====================

    @Override
    public String info() {
        StringBuilder info = new StringBuilder();
        info.append(desc());
        int lvl = getRealLevel();

        info.append("\n\n").append(Messages.get(MeleeWeapon.class, "stats_known",
                tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq()));

        if (lvl >= 13) {
            info.append("\n").append(Messages.get(this, "accuracy_100"));
        } else if (lvl >= 10) {
            info.append("\n").append(Messages.get(this, "accuracy_50"));
        } else {
            info.append("\n").append(Messages.get(this, "accuracy_25"));
        }

        info.append("\n").append(Messages.get(this, "instakill_chance",
                (int)(getInstakillChance(lvl) * 100)));

        if (lvl >= 11) {
            info.append("\n").append(Messages.get(this, "reach_3"));
        }

        info.append("\n\n").append(Messages.get(this, "cannot_unequip_warn"));

        if (cursed && isEquipped(hero)) {
            info.append("\n\n").append(Messages.get(Weapon.class, "cursed_worn"));
        }

        return info.toString();
    }

    // ==================== 内部Buff类 ====================

    /**
     * 浊焰攻心
     * 每回合失去最大生命值2%，获得80%伤害减免
     */
    public static class TurbulentFlameHeart extends FlavourBuff {

        public static final float DURATION = 30f;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public boolean act() {
            if (target instanceof Hero) {
                Hero hero = (Hero) target;
                int hpLoss = Math.max(1, Math.round(hero.HT * 0.02f));
                hero.damage(hpLoss, this);
            }
            spend(TICK);
            return true;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }

        @Override
        public int icon() {
            return BuffIndicator.FIRE;
        }
    }

    /**
     * 自定义灵视Buff（支持范围设定）
     */
    public static class EndingBladeMindVision extends FlavourBuff {

        private int range = 4;

        public void setRange(int range) {
            this.range = range;
        }

        public int getRange() {
            return range;
        }

        @Override
        public boolean act() {
            spend(TICK);
            return true;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", range);
        }

        @Override
        public int icon() {
            return BuffIndicator.MIND_VISION;
        }

        private static final String RANGE = "range";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(RANGE, range);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            range = bundle.getInt(RANGE);
        }
    }

    /**
     * 死亡诅咒
     * 在指定区域持续施加燃烧和中毒
     */
    public static class DeathCurse extends Buff {

        private int pos = -1;
        private int duration = 0;

        public void setPos(int pos) {
            this.pos = pos;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        @Override
        public boolean act() {
            if (duration > 0) {
                duration--;

                Char ch = Actor.findChar(pos);
                if (ch != null && !(ch instanceof NPC) && ch.alignment != Char.Alignment.ALLY) {
                    Buff.affect(ch, Burning.class).reignite(ch, 2.0f);
                    Buff.affect(ch, Poison.class).set(2 + duration);

                    if (ch.HP <= 0 && ch instanceof Mob) {
                        Mob mob = (Mob) ch;
                        if (mob.isAlive()) {
                            AllyBuff.affectAndLoot(mob, hero, Corruption.class);
                            Buff.affect(mob, Adrenaline.class, 20f);
                            Buff.affect(mob, Haste.class, 20f);
                        }
                    }
                }

                CellEmitter.get(pos).start(Speck.factory(Speck.SMOKE), 0.1f, 2);

                spend(TICK);
            } else {
                detach();
            }
            return true;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", duration);
        }
    }

    /**
     * 浊焰审判模式Buff
     * 怪物伤害x2，血量x2，移速*0.85
     */
    public static class TrialModeBuff extends Buff {

        private int originalHT = 0;
        private float originalSpeed = 1f;

        @Override
        public boolean attachTo(Char target) {
            if (super.attachTo(target)) {
                if (target instanceof Mob) {
                    Mob mob = (Mob) target;
                    originalHT = mob.HT;
                    originalSpeed = mob.baseSpeed;

                    mob.HT = Math.round(mob.HT * 2f);
                    mob.HP = mob.HT;
                }
                return true;
            }
            return false;
        }

        @Override
        public void detach() {
            if (target instanceof Mob) {
                Mob mob = (Mob) target;
                mob.HT = originalHT;
                if (mob.HP > mob.HT) mob.HP = mob.HT;
                mob.baseSpeed = originalSpeed;
            }
            super.detach();
        }

        @Override
        public boolean act() {
            spend(TICK);
            return true;
        }
    }
}