package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.Actor.add;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.QingXian_ability_utils.BuffClear;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * 终焉 - 5阶彩蛋武器 v3.0
 *<BR>
 * 核心特性：<BR>
 * - 必定诅咒，无法强化/附魔/净化<BR>
 * - 装备后无法取下<BR>
 * - 基础攻击15（固定值），成长4<BR>
 * - 力量需求10，攻速固定为1<BR>
 * - 命中率根据等级变化：25% → 50% → 80%<BR>
 * - 即死效果概率：10% → 15% → 20%<BR>
 *<BR><BR>
 * 升级方式：<BR>
 * - 只能通过升级卷轴和蝶变升级<BR>
 * - 蝶变不会变为其他武器，每层只能蝶变一次<BR>
 * - 必须在装备状态下才能升级<BR>
 * - 每300回合只能升级一次<BR>
 *<BR><BR>
 * 等级效果：<BR>
 * L1: 每15回合自身燃烧1回合，新增5%腐化效果<BR>
 * L4: 4格灵视，即死15%<BR>
 * L7: 吞天(15%概率吸血30%)，即死25%<BR>
 * L10: 幻惑，命中率50%<BR>
 * L11: 虚弱，攻击距离3<BR>
 * L13: 命中率80%<BR>
 * L15+: 即死20%，腐化15%，灵视6格，易伤/恍惚/失明，护甲2正2诅附魔，武器随机附魔(x-14)种，
 *        击杀50%概率获得10%最大生命奥术护盾(可叠加，自然流失)<BR>
 *<BR><BR>
 * 主动技能：<BR>
 * Top1(L6+): 3x3恐惧+30魔法伤害，CD5，耗血10%<BR>
 * Top2(L10+): 死亡诅咒区域，CD30，耗血30%<BR>
 * Top3(L15+): 净化debuff+赐福/充能/精力充沛/急速+浊焰攻心，耗血60%，每局1次，此后阻断终焉的常驻buff效果<BR>
 *<BR><BR>
 * 特殊机制：<BR>
 * - 尝试净化触发浊焰审判（怪物伤害x2/血量x2/移速*0.85，玩家血量上限降至50%）<BR>
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
    private static final String COOLDOWN_TOP1 = "cooldown_top1";
    private static final String COOLDOWN_TOP2 = "cooldown_top2";
    private static final String TOP3_USED = "top3_used";
    private static final String TRIAL_MODE = "trial_mode";
    private static final String TURN_COUNTER = "turn_counter";
    private static final String LAST_UPGRADE_TURN = "last_upgrade_turn";
    private static final String TRANSMUTED_REGIONS = "transmuted_regions";
    private static final String COUNTR = "countr";
    private static final String EB_LEVEL = "eb_level";
    // ==================== 核心属性 ====================
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
    public int fireCounter = 0;
    /** 上次升级的回合数 */
    private int lastUpgradeTurn = -9999;
    /** 蝶变升级记录 */
    private int transmutedRegions = 0;

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(EB_LEVEL, level());
        bundle.put(COOLDOWN_TOP1, cooldownTop1);
        bundle.put(COOLDOWN_TOP2, cooldownTop2);
        bundle.put(TOP3_USED, top3Used);
        bundle.put(TRIAL_MODE, trialMode);
        bundle.put(TURN_COUNTER, turnCounter);
        bundle.put(LAST_UPGRADE_TURN, lastUpgradeTurn);
        bundle.put(TRANSMUTED_REGIONS, transmutedRegions);
        bundle.put(COUNTR, fireCounter);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        level(bundle.getInt(EB_LEVEL));
        cooldownTop1 = bundle.getInt(COOLDOWN_TOP1);
        cooldownTop2 = bundle.getInt(COOLDOWN_TOP2);
        top3Used = bundle.getBoolean(TOP3_USED);
        trialMode = bundle.getBoolean(TRIAL_MODE);
        turnCounter = bundle.getInt(TURN_COUNTER);
        lastUpgradeTurn = bundle.getInt(LAST_UPGRADE_TURN);
        transmutedRegions = bundle.getInt(TRANSMUTED_REGIONS);
        fireCounter = bundle.getInt(COUNTR);
    }

    // ==================== 动作列表 ====================

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero)) {
            int lvl = level();
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

    // ==================== 升级系统重制 ====================

    /**
     * 检查是否可以升级
     * 条件：
     * 1. 必须已装备
     * 2. 距离上次升级至少300回合
     */
    public static class Cooldown extends FlavourBuff{
        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.GDX_COLOR);
        }
    }

    /**
     * 通过升级卷轴升级
     */
    @Override
    public boolean isUpgradable() {
        return isEquipped(hero) && hero.buff(Cooldown.class) == null;
    }

    @Override
    public Item upgrade() {
        // 终焉的升级规则：仅装备中、每 300 回合一次
        if (!isEquipped(hero) || hero.buff(Cooldown.class) != null){
            return this;                       // 静默拒绝，不弹 GLog
        }
        level( level() + 1 );                  // 只加等级，不碰附魔
        cursed = true;                         // 必定诅咒不因升级改变
        Buff.affect(hero, Cooldown.class, 300f);  // 冷却规则也集中在这里
        return this;
    }

    // 蝶变升级

    private static int transmuteRegion() {
        return Dungeon.depth / 5;
    }

    public boolean canTransmuteUpgrade() {
        return isEquipped(hero)
                && hero.buff(Cooldown.class) == null
                && (transmutedRegions & (1 << transmuteRegion())) == 0;
    }

    public boolean transmuteUpgrade() {
        if (!canTransmuteUpgrade()) return false;
        upgrade();                                   // 内部已处理等级+1、诅咒保持、300回合冷却
        transmutedRegions |= (1 << transmuteRegion());
        return true;
    }

    @Override
    public Item upgrade(boolean enchant) {
        return upgrade();                      // 终焉不走"强化+附魔"路径
    }

    @Override
    public String desc() {
        StringBuilder desc = new StringBuilder();
        desc.append(Messages.get(this, "desc"));

        int lvl = level();

        desc.append("\n\n").append(Messages.get(this, "level_desc", lvl));
        if(hero != null){
            if (hero.buff(Cooldown.class)!=null) {
                desc.append("\n").append(Messages.get(this, "upgrade_cooldown"));
            } else {
                desc.append("\n").append(Messages.get(this, "upgrade_ready"));
            }
        } else {
            desc.append("\n").append(Messages.get(this, "upgrade_ready"));
        }


        if (trialMode) {
            desc.append("\n\n").append(Messages.get(this, "trial_mode_active"));
        }

        if (lvl >= 1) {
            desc.append("\n\n").append(Messages.get(this, "current_effects"));
            desc.append("\n").append(Messages.get(this, "effect_l1"));
            if (lvl >= 4 && lvl <= 6) {
                desc.append("\n").append(Messages.get(this, "effect_l4_6"));
            }
            if (lvl >= 7 && lvl <= 9) {
                desc.append("\n").append(Messages.get(this, "effect_l7_9"));
            }
            if (lvl >= 10) {
                desc.append("\n").append(Messages.get(this, "effect_l10"));
            }
            if (lvl >= 11 && lvl <= 12) {
                desc.append("\n").append(Messages.get(this, "effect_l11_12"));
            }
            if (lvl >= 13 && lvl <= 14) {
                desc.append("\n").append(Messages.get(this, "effect_l13_14"));
            }
            if (lvl >= 15) {
                desc.append("\n").append(Messages.get(this, "effect_l15_plus"));
            }
        }

        return desc.toString();
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
                if (ch != null && ch != hero && !(ch instanceof NPC) && ch.alignment != Char.Alignment.ALLY) {
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

            if (!Dungeon.level.insideMap(target) || Dungeon.level.solid[target] || !Dungeon.level.passable[target]) {
                GLog.n(Messages.get(EndingBlade.this, "invalid_target"));
                return;
            }

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

        // 移除所有可清除的负面buff
        BuffClear.NegativeClean(hero, BuffClear.CLEAR_LEVEL);

        // 给予正面buff（20回合）
        Buff.affect(hero, Bless.class, 20f);
        Buff.affect(hero, Recharging.class, 20f);
        Buff.affect(hero, Stamina.class, 20f);
        Buff.affect(hero, Haste.class, 20f);

        // 浊焰攻心（30回合）
        Buff.affect(hero, TurbulentFlameHeart.class).set(30f);

        hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 8);
        CellEmitter.get(hero.pos).start(ShaftParticle.FACTORY, 0.2f, 5);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);

        GLog.p(Messages.get(this, "ready_wrap_used"));
        top3Used = true;
        removeLevelBuffs(hero);
        hero.spendAndNext(Actor.TICK);
    }

    // ==================== 武器核心属性 ====================

    @Override
    public int STRReq(int lvl) {
        return 10;
    }

    @Override
    public float speedFactor(Char owner) {
        return 1f;
    }

    @Override
    public int min(int lvl) {
        return 15 + lvl * 4;
    }

    @Override
    public int max(int lvl) {
        return 15 + lvl * 4;
    }

    public float AccRate() {
        if (level()>=13) return 0.8f;
        if (level()>=10) return 0.5f;
        return 0.25f;
    }

    /**
     * 尝试净化时触发浊焰审判模式
     */
    public void onAttemptCleanse() {
        if (!trialMode) {
            trialMode = true;
            Hero hero = Dungeon.hero;

            // 血量上限降至50%
            hero.HT = Math.max(1, Math.round(hero.HT * 0.50f));
            if (hero.HP > hero.HT) {
                hero.HP = hero.HT;
            }

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (!(mob instanceof NPC)) {
                    Buff.affect(mob, TrialModeBuff.class);
                }
            }

            GLog.n(Messages.get(this, "trial_mode_triggered"));
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            GameScene.fadeToBlack(1f,1f);
        }
    }

    // ==================== 装备/卸载处理 ====================

    /**
     * 装备后无法取下
     */
    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if(true){
        // if(!(Dungeon.isDLC(Conducts.Conduct.DEV))){
            GLog.n(Messages.get(this, "cannot_unequip"));
        } else {
            return super.doUnequip(hero,collect,single);
        }
        return false;
    }

    @Override
    public boolean canUnequip(Hero hero) {
        // 与 doUnequip 保持一致：终焉一律不允许卸下
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
        int lvl = level();

        final Char def = defender;
        final Char atk = attacker;

        Actor.add(new Actor(){
            {
                actPriority = VFX_PRIO;
            }

            @Override
            protected boolean act(){

                // 击杀判定
                if (!def.isAlive()) {
                    // Level15+：击杀50%概率获得10%最大生命奥术护盾
                    if (lvl >= 15 && atk instanceof Hero && Random.Float() < 0.50f) {
                        int shieldAmount = Math.max(1, Math.round(atk.HT * 0.10f));
                        Buff.affect(atk, Barrier.class).incShield(shieldAmount);
                        atk.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 4);
                        GLog.p(Messages.get(this, "arcane_shield_gain", shieldAmount));
                    }
                }

                Actor.remove(this);
                return true;
            }
        });

        // 即死效果
        float instakillChance = getInstakillChance(lvl);
        if (Random.Float() < instakillChance && defender != attacker && defender.isAlive()
                && !Char.hasProp(defender, Char.Property.BOSS)
                && !Char.hasProp(defender, Char.Property.MINIBOSS)) {
            defender.die(this);
            GLog.n(Messages.get(this, "instakill", defender.name()));
            CellEmitter.get(defender.pos).burst(Speck.factory(Speck.LIGHT), 6);
        }

        // 腐化效果
        float corruptChance = getCorruptChance(lvl);
        if (Random.Float() < corruptChance && defender != attacker && defender.isAlive() && defender instanceof Mob
                && !Char.hasProp(defender, Char.Property.BOSS)
                && !Char.hasProp(defender, Char.Property.MINIBOSS)) {
            Hero hero = (attacker instanceof Hero) ? (Hero) attacker : Dungeon.hero;
            AllyBuff.affectAndLoot((Mob) defender, hero, Corruption.class);
            GLog.p(Messages.get(this, "corrupted", defender.name()));
        }

        // Level7：吞天（15%概率吸血30%）
        if (lvl >= 7 && Random.Float() < 0.15f && defender != attacker) {
            int heal = Math.max(1, Math.round(damage * 0.30f));
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

    // 即死概率
    private float getInstakillChance(int lvl) {
        if (lvl >= 15) return 0.20f;
        else if (lvl >= 4) return 0.15f;
        else return 0.10f;
    }

    // 腐化概率
    private float getCorruptChance(int lvl) {
        if (lvl >= 15) return 0.15f;
        else if (lvl >= 1) return 0.05f;
        else return 0f;
    }

    // ==================== 等级Buff管理 ====================

    private void applyLevelBuffs(Hero hero) {
        if(!top3Used){
            int lvl = level();

            // Level4-14：4格灵视
            if (lvl >= 4 && lvl < 15) {
                Buff.affect(hero, MindVision.class, 12f).setRange(4);
            }

            // Level7：吞天
            if(lvl >= 7){
                Buff.affect(hero, SkyRoll.class, 2f);
            }

            // Level10：幻惑
            if (lvl >= 10) {
                Buff.affect(hero, Hex.class, 2f);
            }

            // Level11-12：虚弱
            if (lvl >= 11) {
                Buff.affect(hero, Weakness.class, 2f);
            }

            // Level15+：灵视6格 + 虚弱 + 易伤/恍惚/失明
            if (lvl >= 15) {
                Buff.affect(hero, MindVision.class, 12f).setRange(6);
                Buff.affect(hero, Weakness.class, 2f);
                Buff.affect(hero, Vulnerable.class, 2f);
                Buff.affect(hero, Daze.class, 2f);
                Buff.affect(hero, Blindness.class, 2f);
            }
        }
    }

    public static class SkyRoll extends FlavourBuff{};

    private void removeLevelBuffs(Hero hero) {
        Buff.detach(hero, MindVision.class);
        Buff.detach(hero, Hex.class);
        Buff.detach(hero, Weakness.class);
        Buff.detach(hero, Vulnerable.class);
        Buff.detach(hero, Daze.class);
        Buff.detach(hero, Blindness.class);
        Buff.detach(hero, SkyRoll.class);
    }

    // ==================== 每回合更新 ====================

    /**
     * 由游戏系统每回合调用
     */
    public void onTurnUpdate(Hero hero) {
        turnCounter++;

        if(trialMode){
            fireCounter++;
        }

        if (cooldownTop1 > 0) cooldownTop1--;
        if (cooldownTop2 > 0) cooldownTop2--;

        int lvl = level();

        // Level1+：每15回合自身燃烧1回合
        if (lvl >= 1 && turnCounter % 15 == 0) {
            Buff.affect(hero, Burning.class).reignite(hero, 1.0f);
            GLog.n(Messages.get(this, "self_burning"));
        }

        // 持续刷新等级buff
        applyLevelBuffs(hero);
    }

    // ==================== 攻击距离 ====================

    @Override
    public int reachFactor(Char owner) {
        int lvl = level();
        if (lvl >= 11) {
            return 3;
        }
        return super.reachFactor(owner);
    }

    // ==================== 信息面板 ====================

    @Override
    public String info() {
        StringBuilder info = new StringBuilder();
        info.append(desc());
        int lvl = level();

        info.append("\n\n").append(Messages.get(MeleeWeapon.class, "stats_known",
                tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq()));

        if (lvl >= 13) {
            info.append("\n").append(Messages.get(this, "accuracy_80"));
        } else if (lvl >= 10) {
            info.append("\n").append(Messages.get(this, "accuracy_50"));
        } else {
            info.append("\n").append(Messages.get(this, "accuracy_25"));
        }

        info.append("\n").append(Messages.get(this, "instakill_chance",
                (int)(getInstakillChance(lvl) * 100)));
        info.append("\n").append(Messages.get(this, "corrupt_chance",
                (int)(getCorruptChance(lvl) * 100)));

        if (lvl >= 11) {
            info.append("\n").append(Messages.get(this, "reach_3"));
        }

        info.append("\n\n").append(Messages.get(this, "cannot_unequip_warn"));
        info.append("\n").append(Messages.get(this, "upgrade_rule"));


        if (cursed && isEquipped(hero) && hero != null) {
            info.append("\n\n").append(Messages.get(Weapon.class, "cursed_worn"));
        }

        return info.toString();
    }

    // ==================== 内部Buff类 ====================

    /**
     * 浊焰攻心
     * 每回合失去最大生命值2%，获得80%伤害减免
     */
    public static class TurbulentFlameHeart extends Buff {

        private float left;
        private static final String LEFT = "left";

        {
            type = buffType.POSITIVE;
        }

        public void set(float duration) {
            left = duration;
        }

        @Override
        public boolean act() {
            if (target instanceof Hero && target.isAlive()) {
                Hero hero = (Hero) target;
                int hpLoss = Math.max(1, Math.round(hero.HT * 0.02f));
                hero.damage(hpLoss, this);
            }
            spend(TICK);
            left -= TICK;
            if (left <= 0) {
                detach();
            }
            return true;
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(LEFT, left);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            left = bundle.getFloat(LEFT);
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
     * 死亡诅咒
     * 在指定区域持续施加燃烧和中毒以及死亡后复活为盟友
     */
    public static class DeathCurseTracker extends Buff {

        private float left;
        private static final String LEFT = "left";

        {
            actPriority = Actor.VFX_PRIO; // 在伤害结算之后行动
        }

        public void set(float duration) {
            left = duration;
        }

        @Override
        public boolean attachTo(Char target) {
            if (super.attachTo(target)){
                target.deathMarked = true;      // 0 血时不会立刻死亡
                return true;
            }
            return false;
        }

        @Override
        public boolean act() {
            if (target instanceof Mob && target.HP <= 0 && !(target instanceof NPC)
                    && target.alignment != Char.Alignment.ALLY){
                Mob mob = (Mob) target;
                mob.HP = Math.max(1, mob.HT / 2);              // 复活为盟友
                AllyBuff.affectAndLoot(mob, hero, Corruption.class);
                Buff.affect(mob, Adrenaline.class, 20f);        // 豺狼狂暴
                Buff.affect(mob, Haste.class, 20f);             // 极速
                detach();
            } else {
                spend(TICK);
                left -= TICK;
                if (left <= 0) detach();
            }
            return true;
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(LEFT, left);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            left = bundle.getFloat(LEFT);
        }

        @Override
        public void detach() {
            super.detach();
            if (target != null) target.deathMarked = false;
        }
    }

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

                    Buff.affect(ch, DeathCurseTracker.class).set(duration + 1);
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

        private static final String POS = "dc_pos";
        private static final String DURATION = "dc_duration";
        @Override public void storeInBundle(Bundle bundle){
            super.storeInBundle(bundle);
            bundle.put(POS, pos);
            bundle.put(DURATION, duration);
        }
        @Override public void restoreFromBundle(Bundle bundle){
            super.restoreFromBundle(bundle);
            pos = bundle.getInt(POS);
            duration = bundle.getInt(DURATION);
        }
    }

    /**
     * 浊焰审判模式Buff
     * 怪物伤害x2，血量x2，移速*0.85
     */
    public static class TrialModeBuff extends Buff {


        @Override
        public boolean act() {
            if (target instanceof Mob) {
                if(!((Mob) target).isEndLess){
                    Mob mob = (Mob) target;
                    mob.HT = Math.round(mob.HT * 2f);
                    mob.HP = Math.round(mob.HP * 2f);
                    mob.baseSpeed *= 0.85f;
                    ((Mob) target).isEndLess = true;
                }
            }
            spend(TICK);
            return true;
        }

        @Override
        public String desc() {
            return target == hero ? Messages.get(this,"desc") : Messages.get(this,"mob_desc");
        }

        @Override
        public int icon() {
            return target == hero ? BuffIndicator.FIRE_DEH : BuffIndicator.FIRE_DEM;
        }
    }
}
