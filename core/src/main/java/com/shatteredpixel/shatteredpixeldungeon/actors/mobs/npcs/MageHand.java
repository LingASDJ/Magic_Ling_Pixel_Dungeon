package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.MageHandControlBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.*;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand.WandOfBlueFuck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand.WandOfHightHunderStorm;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MageHandSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * 敕灵天女召唤出的法师之手。
 * <p>场景中的 MageHand 只维护当前存活实体的运行状态；跨消散保存的装备由
 * MageHandControl 负责。实体存在时，装备会临时绑定在实体字段中，避免同一件
 * 法杖同时被控制器和实体重复存档。</p>
 */
public class MageHand extends DirectableAlly {

    /**
     * 让英雄获得法师之手视野范围的常驻标记。
     */
    public static class HandWareness extends FlavourBuff {
        public int distance = 2;
        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 123456789f;

        @Override
        public boolean act() {
            // 常驻视野标记：每回合正常推进，而不是交给 FlavourBuff 立即 detach。
            spend(TICK);
            return true;
        }

        @Override
        public void detach() {
            super.detach();
            GameScene.updateFog();
        }
    }

    // 法师之手仅负责跟随，不主动寻找敌人。
    {
        spriteClass = MageHandSprite.class;
        flying = true;
        properties.add(Property.UNKNOWN);
        immunities.add(Blob.class);
        viewDistance = 10;
        WANDERING = new Wandering();
    }

    /**
     * 纯跟随状态。法师之手不会切换到 HUNTING，也不会自主施法。
     */
    private class Wandering extends Mob.Wandering {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {

                enemySeen = false;

                int oldPos = pos;
                target = hero.pos;

            if (getCloser(target)) {
                spend(1 / speed());
                return moveSprite(oldPos, pos);
            } else {
                spend(TICK);
            }

            return true;
        }

        @Override
        protected boolean noticeEnemy() {
            spend(TICK);
            return super.noticeEnemy();
        }
    }

    @Override
    public void aggro(Char ch) {
        // 旧控制器攻击功能已删除，任何仇恨变化都立即回到跟随状态。
        enemy = null;
        target = hero.pos;
        state = WANDERING;
    }

    @Override
    protected Char chooseEnemy() {
        // 禁止基类 AI 自动选择敌人。
        return null;
    }

    /**
     * 每次受到伤害事件消耗 1 点装备法杖的整数充能。
     * 没有装备或充能已经耗尽时，再次受到伤害时，法师之手直接消散并进入冷却。
     */
    @Override
    public void damage(int dmg, Object src, DamageType type) {
        Wand wand = getEquippedWandRuntime();

        if (wand == null || wand.curCharges <= 0) {
            dissipate();
            return;
        }

        // 受击扣除 1 点整数充能。扣到 0 时只是无法再施法，下一次受击才会消散。
        wand.curCharges = Math.max(0, wand.curCharges - 1);
        wand.updateQuickslot();

        MageHandControl control =
                hero.belongings.getItem(MageHandControl.class);
        if (control != null) {
            control.updateQuickslot();
        }
    }

    private boolean dissipated = false;

    /**
     * 实体最后一次行动时所在的楼层。
     * 控制器用它判断这只手是不是被遗留在了旧楼层，从而在换层后把它召回，
     * 而不是凭空再生成一只新手导致装备看起来“消失”。
     */
    public int lastLevelDepth = -1;
    public int lastLevelBranch = -1;

    /**
     * 移除当前实体并把装备交还给控制器。
     * 消散不是普通死亡，因此不掉落装备，也不会触发原死亡掉落逻辑。
     */
    private void dissipate() {
        if (dissipated) {
            return;
        }
        dissipated = true;

        MageHandControl control =
                hero.belongings.getItem(MageHandControl.class);

        if (control != null) {
            // 装备转存回控制器，避免消散后法杖跟着实体一起消失。
            control.storeGearFrom(this);
            control.mageHand = null;
            control.updateQuickslot();
        }

        // 控制器 buff 必须保留：它是控制器窗口的常驻入口。
        // 消散后玩家仍然可以打开窗口查看状态并重新召唤。
        Buff.affect(hero, MageHandControlBuff.class);

        // 视野共享随实体一起结束。
        Buff.detach(hero, HandWareness.class);

        pendingWand = null;
        pendingTarget = null;
        pendingFollowUp = false;

        HP = 0;
        Actor.remove(this);
        Dungeon.level.mobs.remove(this);

        // 播放法师之手自己的死亡动画；动画结束后由 MobSprite.onComplete 淡出并清理 sprite。
        if (sprite != null) {
            sprite.die();
        }

        Buff.affect(hero, SummonCooldown.class, SummonCooldown.DURATION);

        GLog.w(Messages.get(this, "vanished"));
    }

    @Override
    public String description() {
        String desc = super.description();

        // 实体存在时，法杖暂时保存在法师之手身上，因此详情直接读取实体字段。
        if(magesStaff != null){
            desc += "\n\n" + Messages.get(this, "desc_staff", magesStaff.name());
        }

        if(equippedWand != null){
            desc += "\n\n" + Messages.get(this, "desc_equipped_wand", equippedWand.name());
            desc += "\n" + Messages.get(this, "desc_charges", equippedWand.curCharges, equippedWand.maxCharges);
        }

        return desc;
    }

    // 法师之手自行推进法杖充能时使用的曲线参数。
    private static final float BASE_CHARGE_DELAY = 10f;
    private static final float SCALING_CHARGE_ADDITION = 40f;
    private static final float NORMAL_SCALE_FACTOR = 0.875f;

    /**
     * 法师之手每次行动都会自行推进装备法杖的充能。
     * 这里不使用 Wand.Charger，避免与普通充能系统重复结算。
     */
    @Override
    public void spend(float time) {
        super.spend(time);

        if (equippedWand != null) {
            chargeWand(equippedWand);
        }
        if (magesStaff != null) {
            chargeWand(magesStaff.wand);
        }
    }

    private void chargeWand(Wand equippedWand) {
        if (equippedWand == null || equippedWand.curCharges >= equippedWand.maxCharges ) {
            return;
        }

        // 计算本回合的充能量
        float chargeAmount = chargeRate(equippedWand, getChargeMultiplier());

        // 累积部分充能
        equippedWand.partialCharge += chargeAmount;

        // 处理完整的充能
        while (equippedWand.partialCharge >= 1f && equippedWand.curCharges < equippedWand.maxCharges) {
            equippedWand.partialCharge -= 1f;
            equippedWand.curCharges++;
        }

        // 如果法杖已充满，重置部分充能值
        if (equippedWand.curCharges >= equippedWand.maxCharges) {
            equippedWand.partialCharge = 0f;
        }
    }

    /**
     * 法师之手每回合为法杖推进的充能速度（1 点充能所需回合数的倒数）。
     * 充能曲线与原实现保持一致，窗口的“下一充能回合”也复用它。
     */
    public static float chargeRate(Wand wand, float multiplier) {
        if (wand == null) {
            return 0f;
        }

        int missingCharges = Math.max(0, wand.maxCharges - wand.curCharges);
        float turnsToCharge = BASE_CHARGE_DELAY
                + (SCALING_CHARGE_ADDITION * (float) Math.pow(NORMAL_SCALE_FACTOR, missingCharges));
        if (turnsToCharge <= 0f) {
            return 1f;
        }
        return multiplier / turnsToCharge;
    }

    /**
     * 距离下一次整数充能还需要多少回合；已经充满或没有法杖时返回 0。
     */
    public int estimateTurnsToNextCharge() {
        Wand wand = getEquippedWandRuntime();
        if (wand == null || wand.curCharges >= wand.maxCharges) {
            return 0;
        }
        float rate = chargeRate(wand, getChargeMultiplier());
        if (rate <= 0f) {
            return -1;
        }
        return Math.max(1, (int) Math.ceil((1f - wand.partialCharge) / rate));
    }

    /**
     * 充能进度，0 到 1。整数充能和当前的部分充能一起折算，供窗口进度条使用。
     */
    public static float chargeProgress(Wand wand) {
        if (wand == null || wand.maxCharges <= 0) {
            return 0f;
        }
        float progress = (wand.curCharges + wand.partialCharge) / (float) wand.maxCharges;
        return GameMath.gate(0f, progress, 1f);
    }

    /**
     * 返回法师之手法杖的充能倍率。
     */
    private float getChargeMultiplier() {
        float multiplier = 1f;
        //灵能加速
        int points = hero.pointsInTalent(
                Talent.PSIONIC_ACCELERATION
        );

        // +1 = 150%，+2 = 200%，+3 = 250%
        multiplier += 0.5f * points;

        // 英雄充能相关的buff（与灵能加速加算）
        for (Recharging bonus : hero.buffs(Recharging.class)) {
            if (bonus != null && bonus.remainder() > 0f) {
                multiplier += 0.25f * bonus.remainder();
            }
        }

        return multiplier;
    }

    // 当前存活实体的临时装备引用；消散时会转移到控制器。
    public Wand equippedWand = null;

    public MagesStaff magesStaff = null;

    public MageHand(){
        super();
        HP = HT = 1;
        if (equippedWand != null) {
            equippedWand.partialCharge = 0f;
        }
    }

    /**
     * 仅在实体仍存在时直接绑定法杖，主要用于兼容旧调用路径。
     * 正常控制器装卸应优先经过 MageHandControl。
     */
    public void equipWand(Wand wand) {
        if (equippedWand != null) {
            equippedWand.stopCharging();
        }

        wand.stopCharging();
        wand.partialCharge = 0f;

        this.equippedWand = wand;
        GLog.i(Messages.get(this, "wand_equipped", wand.name()));
    }
    // 返回本次实际可施法的法杖，老魔杖优先于普通法杖字段。
    private Wand getEquippedWandRuntime() {
        if (magesStaff != null && magesStaff.wand != null) {
            return magesStaff.wand;
        }

        return equippedWand;
    }

    // 普通弹道会避开英雄，防止法师之手误伤英雄。
    private boolean isHeroInAttackPath(Ballistica attack) {
        for (int i = 0; i < attack.path.size(); i++) {
            int p = attack.path.get(i);
            if (p == hero.pos) {
                return true;
            }
        }
        return false;
    }

    public Wand getEquippedWand() {
        return equippedWand;
    }

    public Wand getEquippedMageStaffWand() {
        return magesStaff.wand;
    }

    /**
     * 维持控制器、视野 Buff 和显隐状态，然后交给跟随状态机。
     */
    @Override
    protected boolean act() {
        lastLevelDepth = Dungeon.depth;
        lastLevelBranch = Dungeon.branch;

        MageHandControlBuff buff = hero.buff(MageHandControlBuff.class);
        if (buff == null) {
            Buff.affect(hero, MageHandControlBuff.class);
        }

        MageHand.HandWareness mageHandWareness = hero.buff(MageHand.HandWareness.class);
        if(mageHandWareness == null){
            Buff.affect(hero, MageHand.HandWareness.class);
        }

        // 法师之手始终可见；没有装备法杖时也保持可见，否则玩家会以为它丢在了别处。
        invisible = 0;

        return super.act();
    }

    public void gainCharge(float charge) {
        // 外部充能先累积到 partialCharge，再由完整的 1 点转成整数充能。
        // 老魔杖的充能由其内部 wand 字段结算，不在这里重复处理。
        if (equippedWand != null && equippedWand.curCharges < equippedWand.maxCharges) {
            equippedWand.partialCharge += charge;
            while (equippedWand.partialCharge >= 1f && equippedWand.curCharges < equippedWand.maxCharges) {
                equippedWand.partialCharge -= 1f;
                equippedWand.curCharges++;
            }
            if (equippedWand.curCharges >= equippedWand.maxCharges) {
                equippedWand.partialCharge = 0f;
                equippedWand.curCharges = equippedWand.maxCharges;
            }
        }
    }


    /**
     * 施法动画结束后的统一回调。
     * 实际使用的法杖和目标来自 pending 字段，而不是回调时的当前装备，
     * 以免动画期间换装备或目标引用变化造成漏结算。
     */
    public void onZapComplete() {
        if (pendingWand != null && pendingTarget != null) {
            castPendingWand();
        }

        pendingWand = null;
        pendingTarget = null;
        pendingFollowUp = false;

        next();
    }

    // 使用发起施法时保存的法杖和目标完成一次追加施法。
    private void castPendingWand() {
        Wand wand = pendingWand;
        Char target = pendingTarget;

        if (wand == null || target == null) {
            return;
        }

        int chargeCost = Math.max(1, wand.chargesPerCast());

        if (wand.curCharges < chargeCost) {
            return;
        }

        enemy = target;
        this.target = target.pos;

        finishHandWandCast(wand);
    }


    /**
     * 旧调用路径的兼容入口。新追加施法优先使用 pending 事务。
     */
    private void zap() {
        if (magesStaff != null && enemy != null) {
            if (magesStaff.wand.curCharges > 0) {
                zapWithMagesStaff();
            }
        } else if (equippedWand != null && enemy != null) {
            if (equippedWand.curCharges > 0) {
                zapWithEquippedWand();
            }
        }
    }

    private void zapWithMagesStaff() {
        // 兼容入口同样复用统一结算方法，避免普通法杖和老魔杖出现两套规则。
        if (magesStaff != null
                && magesStaff.wand != null) {
            finishHandWandCast(magesStaff.wand);
        }
    }

    private void zapWithEquippedWand() {
        finishHandWandCast(equippedWand);
    }

    /**
     * 统一完成法杖效果、扣除充能、刷新控制器并触发法师之手施法天赋。
     * 普通法杖和老魔杖都经过这里，确保两边的结算一致。
     */
    private void finishHandWandCast(Wand wand) {
        runHandWandEffect(wand);

        int chargeCost = Math.max(1, wand.chargesPerCast());
        wand.curCharges = Math.max(0, wand.curCharges - chargeCost);
        wand.updateQuickslot();

        MageHandControl control =
                hero.belongings.getItem(MageHandControl.class);

        if (control != null && control.mageHand == this) {
            control.updateQuickslot();
        }

        finalizeWandUse();
    }

    private Wand pendingWand;
    private Char pendingTarget;
    private boolean pendingFollowUp;

    /**
     * 在法师之手施法期间临时替换法杖的实际来源。
     * 魔弹法杖额外关闭只属于英雄的特殊联动。
     */
    private void runHandWandEffect(Wand wand) {
        WandOfMagicMissile magicMissile = null;

        if (wand instanceof WandOfMagicMissile) {
            magicMissile = (WandOfMagicMissile) wand;
            magicMissile.castByMageHand = true;
        }

        wand.setCasterOverride(this);

        try {
            executeWandEffect(wand);
        } finally {
            wand.setCasterOverride(null);

            if (magicMissile != null) {
                magicMissile.castByMageHand = false;
            }
        }
    }

    /**
     * 按法杖类型分派实际效果：
     * 哨卫法杖需要特殊落点处理，诅咒法杖走诅咒施法，
     * 锥形等特殊法杖使用 AI 施法入口，其余走普通 onZap。
     */
    private void executeWandEffect(Wand wand) {
        if (wand instanceof WandOfWarding && enemy != null) {
            // 获取敌人周围半径2的所有位置
            ArrayList<Integer> validPositions = new ArrayList<>();
            int radius = 3;

            // 遍历敌人周围8个方向的所有点
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    // 跳过中心点和距离不是2的点
                    if (dx == 0 && dy == 0) continue;
                    if (Math.abs(dx) + Math.abs(dy) != radius) continue;

                    int cell = enemy.pos + dx + dy * Dungeon.level.width();

                    // 检查位置是否有效
                    if (cell >= 0 && cell < Dungeon.level.length() &&
                            Dungeon.level.passable[cell] &&
                            Actor.findChar(cell) == null) {

                        // 检查是否是封闭位置
                        boolean solidFound = false;
                        boolean passableFound = false;

                        // 检查周围8个方向
                        for (int i : PathFinder.NEIGHBOURS8) {
                            int neighbourPos = cell + i;
                            if (neighbourPos >= 0 && neighbourPos < Dungeon.level.length()) {
                                if (Dungeon.level.solid[neighbourPos]) {
                                    solidFound = true;
                                } else if (!Dungeon.level.solid[neighbourPos] &&
                                        Actor.findChar(neighbourPos) == null) {
                                    passableFound = true;
                                }
                            }
                        }

                        // 只有当位置不是封闭的时才添加到有效位置列表
                        if (solidFound && passableFound) {
                            validPositions.add(cell);
                        }
                    }
                }
            }

            if (!validPositions.isEmpty()) {
                // 随机选择一个位置
                int targetPos = Random.element(validPositions);

                // 创建法术轨迹
                Ballistica attack = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET);

                // 检查是否需要创建新的哨位
                Char existingWard = Actor.findChar(targetPos);
                if (existingWard instanceof WandOfWarding.Ward) {
                    // 如果已存在哨位，直接施法
                    if (wand.cursed) {
                        CursedWand.cursedZap(wand, this, attack, this::next);
                    } else {
                        wand.onZap(attack);
                    }
                } else {
                    // 如果不存在哨位，创建新的并设置handSummon
                    WandOfWarding.Ward ward = new WandOfWarding.Ward();
                    ward.pos = targetPos;
                    ward.wandLevel = wand.level();
                    ward.handSummon = true;  // 设置handSummon为true
                    GameScene.add(ward, 1f);
                    Dungeon.level.occupyCell(ward);
                    ward.sprite.emitter().burst(MagicMissile.WardParticle.UP, ward.tier);
                }
            }
        } else if (wand.cursed) {
            CursedWand.cursedZap(
                    wand == equippedWand ? wand : null,
                    this,
                    new Ballistica(pos, enemy().pos, Ballistica.STOP_TARGET),
                    this::next
            );
        } else if (isSpecialWand(wand)) {
            wand.onAIZap(new Ballistica(pos, enemy().pos, wand.collisionProperties));
        } else {
            wand.onZap(new Ballistica(pos, enemy().pos, wand.collisionProperties));
        }
    }



    private boolean isSpecialWand(Wand wand) {
        // 这些法杖需要 AI 施法入口来生成锥形或持续区域效果，
        // 不能统一调用普通 onZap。
        return wand instanceof WandOfFireblast ||
                wand instanceof WandOfBlueFuck ||
                wand instanceof WandOfScale ||
                wand instanceof WandOfRegrowth || wand instanceof WandOfHightHunderStorm;
    }

    // 每次成功的法师之手施法都要推进相关 T3 天赋。
    private void finalizeWandUse() {
        Talent.onMageHandSpellCast(this);
        Talent.onBattlemageSpellCast(this);
    }

    //在消息记录中提示召唤成功。
    public void sayAppeared(){
        GLog.p(Messages.get(this, "summoned"));
    }

    // 只有存活实体自己携带装备时，才需要在这份 Bundle 中写入它们。
    private static final String WAND =        "wand";
    private static final String MAGE_STAFF = "mage_staff";
    private static final String LAST_DEPTH = "last_depth";
    private static final String LAST_BRANCH = "last_branch";

    /**
     * 保存当前存活实体身上的装备。
     * <p>消散后的装备会先转交给 MageHandControl，因此正常情况下这里只负责
     * “实体仍存在”这一种存档状态，避免同一件法杖被保存两次。</p>
     */
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);

        bundle.put(LAST_DEPTH, lastLevelDepth);
        bundle.put(LAST_BRANCH, lastLevelBranch);

        if (equippedWand != null)  bundle.put( WAND,equippedWand );
        if(magesStaff != null) bundle.put( MAGE_STAFF, magesStaff );
    }

    /**
     * 恢复实体时重新取得它临时携带的装备。
     * <p>读档会重新创建对象图，旧 Wand.Charger 可能还指向反序列化前的引用，
     * 因此这里停止旧充能链接；实际充能继续由 MageHand.spend() 手动推进。</p>
     */
    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);

        if (bundle.contains(LAST_DEPTH)) {
            lastLevelDepth = bundle.getInt(LAST_DEPTH);
        }
        if (bundle.contains(LAST_BRANCH)) {
            lastLevelBranch = bundle.getInt(LAST_BRANCH);
        }

        if (bundle.contains(WAND))
            equippedWand = (Wand) bundle.get( WAND );
        if (bundle.contains(MAGE_STAFF))
            magesStaff = (MagesStaff) bundle.get( MAGE_STAFF );

        // 读档后重新链接法杖到法师之手
        if (equippedWand != null) {
            equippedWand.stopCharging();
        }

        if (magesStaff != null && magesStaff.wand != null) {
            magesStaff.wand.stopCharging();
        }
    }

    /**
     * 死亡入口统一转交消散流程。
     * <p>法师之手不会走普通怪物掉落，装备会回到 MageHandControl。</p>
     */
    @Override
    public void die( Object cause ) {
        dissipate();
    }

    /**
     * 法师之手消散后的重新召唤冷却。
     * <p>Buff 的剩余时间就是冷却时间，图标使用 TIME 并按时长逐渐淡出。</p>
     */
    public static class SummonCooldown extends FlavourBuff {
        public static final float DURATION = 10f;

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public float iconFadePercent() {
            return GameMath.gate(
                    0,
                    visualcooldown() / DURATION,
                    1
            );
        }
    }

    /**
     * 法师之手的装备窗口。
     * <p>窗口通过 MageHandControl 读写装备，所以即使场景中的法师之手已经消散，
     * 玩家仍可查看、取出或更换控制器中保存的法杖，并能直接在这里召唤。</p>
     */
    private static class WndMageHand extends Window {

        private static final int BTN_SIZE  = 32;
        private static final float GAP     = 5;
        private static final int WIDTH     = 184;
        private static final float CIRCLE_RADIUS = 42f;

        private ItemButton btnWand;
        private RedButton btnSummon;
        public MageHand.MageHandControl magesStaffcontrol;

        private RenderedTextBlock message;
        private RenderedTextBlock status;
        private Bar statusDot;

        private RenderedTextBlock wandName;
        private RenderedTextBlock damageText;
        private RenderedTextBlock chargeText;
        private RenderedTextBlock nextCharge;

        private Bar damageBarBg;
        private Bar damageBarFill;
        private Bar chargeBarBg;
        private Bar chargeBarFill;
        private Bar nextBarBg;
        private Bar nextBarFill;

        private MagicCircle magicCircle;

        private float circleCenterX;
        private float circleCenterY;
        private float rightX;
        private float rightWidth;
        private float refreshTimer = 0f;
        private boolean firstRefresh = true;

        WndMageHand(final MageHandControl control){
            magesStaffcontrol = control;

            // 打开窗口时先清理一次悬空引用，避免控制器还指着旧楼层的手，
            // 导致召唤按钮被错误禁用、法杖却看不到。
            MageHand currentHand = MageHandControl.findHandInCurrentLevel();
            if (control.mageHand != null && control.mageHand != currentHand) {
                control.recoverHand(control.mageHand);
            }
            if (currentHand != null) {
                control.mageHand = currentHand;
            }

            IconTitle titlebar = new IconTitle();
            titlebar.icon( new ItemSprite(ItemSpriteSheet.WAND_HAND_CONTROL) );
            titlebar.label( Messages.get(this, "title") );
            titlebar.setRect( 0, 0, WIDTH, 0 );
            add( titlebar );

            message = PixelScene.renderTextBlock(Messages.get(this, "desc"), 6);
            message.maxWidth( WIDTH );
            message.setPos(0, titlebar.bottom() + GAP);
            add( message );

            // 状态指示点：颜色由 refresh() 根据在场 / 冷却 / 未召唤切换。
            statusDot = new Bar(6, 6, 0xFFFFFFFF);
            add(statusDot);

            status = PixelScene.renderTextBlock("", 7);
            status.maxWidth(WIDTH - 10);
            add(status);

            // 法杖装备槽（保持原有取出 / 更换逻辑）。
            btnWand = new ItemButton(){
                @Override
                public Chrome.Type getType() {
                    return Chrome.Type.BLANK;
                }
                @Override
                protected void onClick() {
                    // 点击已有装备时先尝试放回背包，再清空控制器中的对应槽位。
                    // 背包放不下时才掉落到英雄脚下。
                    if (control.getMageStaff() != null) {
                        MagesStaff currentStaff = control.getMageStaff();
                        item(new WndBag.Placeholder(ItemSpriteSheet.WAND_HOLDER));

                        if (!currentStaff.doPickUp(hero)) {
                            Dungeon.level.drop(currentStaff, hero.pos);
                        }

                        control.equipMageStaff(null);

                    } else if (control.getWand() != null) {
                        Wand currentWand = control.getWand();
                        item(new WndBag.Placeholder(ItemSpriteSheet.WAND_HOLDER));

                        if (!currentWand.doPickUp(hero)) {
                            Dungeon.level.drop(currentWand, hero.pos);
                        }

                        control.equipWand(null);

                    } else {
                        // 两个槽位都为空时，按物品类型分别放入普通法杖或老魔杖。
                        GameScene.selectItem(new WndBag.ItemSelector() {
                            @Override
                            public String textPrompt() {
                                return Messages.get(WndMageHand.class, "wand_prompt");
                            }

                            @Override
                            public boolean itemSelectable(Item item) {
                                return item instanceof Wand || item instanceof MagesStaff;
                            }

                            @Override
                            public Class<?extends Bag> preferredBag(){
                                return MagicalHolster.class;
                            }

                            @Override
                            public void onSelect(Item item) {
                                if (!(item instanceof Wand) && !(item instanceof MagesStaff)) {
                                    // 窗口取消时不做任何操作

                                } else if (item.isEquipped(hero)) {
                                    GLog.w( Messages.get(WndMageHand.class, "cant_equip") );
                                    hide();
                                } else if (!item.isIdentified()) {
                                    GLog.w( Messages.get(WndMageHand.class, "cant_unidentified"));
                                    hide();
                                } else {
                                    if (item instanceof MagesStaff) {
                                        control.equipMageStaff((MagesStaff) item);
                                        item(control.getMageStaff());
                                    } else {
                                        control.equipWand((Wand) item);
                                        item(control.getWand());
                                    }

                                    item.detach(hero.belongings.backpack);
                                }
                            }
                        });
                    }
                }

                @Override
                protected boolean onLongClick() {
                    if (item() != null && item().name() != null){
                        GameScene.show(new WndInfoItem(item()));
                        return true;
                    }
                    return false;
                }
            };

            circleCenterX = 52f;
            circleCenterY = message.bottom() + GAP + CIRCLE_RADIUS + 4f;
            rightX = 104f;
            rightWidth = WIDTH - rightX - 4f;

            // 八角魔法阵由代码绘制，法杖图标叠在法阵中心。
            magicCircle = new MagicCircle(circleCenterX, circleCenterY,
                    CIRCLE_RADIUS);
            add(magicCircle);

            btnWand.setRect(circleCenterX - BTN_SIZE / 2f,
                    circleCenterY - BTN_SIZE / 2f, BTN_SIZE, BTN_SIZE);

            if (control.getMageStaff() != null) {
                btnWand.item(control.getMageStaff());
            } else if (control.getWand() != null) {
                btnWand.item(control.getWand());
            } else {
                btnWand.item(
                        new WndBag.Placeholder(ItemSpriteSheet.WAND_HOLDER)
                );
            }
            add( btnWand );

            // 右侧信息改为进度条，只保留法杖名、数值和状态文字。
            wandName = PixelScene.renderTextBlock("", 6);
            wandName.maxWidth((int) rightWidth);
            add(wandName);

            damageText = PixelScene.renderTextBlock("", 6);
            damageText.maxWidth((int) rightWidth);
            add(damageText);

            damageBarBg = new Bar(rightWidth, 4, 0xFF2A2A33);
            add(damageBarBg);
            damageBarFill = new Bar(1, 4, 0xFFE67E22);
            add(damageBarFill);

            chargeText = PixelScene.renderTextBlock("", 6);
            chargeText.maxWidth((int) rightWidth);
            add(chargeText);

            chargeBarBg = new Bar(rightWidth, 4, 0xFF2A2A33);
            add(chargeBarBg);
            chargeBarFill = new Bar(1, 4, 0xFF9B59B6);
            add(chargeBarFill);

            nextCharge = PixelScene.renderTextBlock("", 6);
            nextCharge.maxWidth((int) rightWidth);
            add(nextCharge);

            nextBarBg = new Bar(rightWidth, 4, 0xFF2A2A33);
            add(nextBarBg);
            nextBarFill = new Bar(1, 4, 0xFF8E44AD);
            add(nextBarFill);

            btnSummon = new RedButton(Messages.get(this, "summon"), 7) {
                @Override
                protected void onClick() {
                    if (magesStaffcontrol.summonHand(Dungeon.hero, true)) {
                        hide();
                    }
                }
            };
            btnSummon.setRect(0, 0, WIDTH, 16);
            add(btnSummon);

            refresh();
        }

        @Override
        public void update() {
            super.update();
            refreshTimer += Game.elapsed;
            if (refreshTimer >= 0.1f) {
                refreshTimer = 0f;
                refresh();
            }
        }

        /**
         * 实时刷新窗口：状态、法杖、伤害、充能格、进度条和按钮都从这里更新。
         * 数据直接从控制器和当前楼层的法师之手读取，所以窗口打开期间
         * 法师之手受击、消散、换装或充能推进都会立刻反映出来。
         */
        private void refresh() {
            MageHandControl control = magesStaffcontrol;
            if (control == null || Dungeon.hero == null) {
                return;
            }

            // 修正控制器指向：窗口打开期间法师之手也可能消散或被留在旧楼层。
            MageHand currentHand = MageHandControl.findHandInCurrentLevel();
            if (control.mageHand != null && control.mageHand != currentHand) {
                control.recoverHand(control.mageHand);
            }
            if (currentHand != null) {
                control.mageHand = currentHand;
            }

            boolean summoned = control.mageHand != null;
            SummonCooldown cooldown = Dungeon.hero.buff(SummonCooldown.class);

            // 状态文字和色点。
            String statusString;
            if (summoned) {
                statusString = Messages.get(this, "status_summoned");
            } else if (cooldown != null) {
                statusString = Messages.get(this, "status_cooling",
                        (int) cooldown.visualcooldown());
            } else {
                statusString = Messages.get(this, "status_missing");
            }
            if (!status.text().equals(statusString)) {
                status.text(statusString);
            }
            int statusColor;
            if (summoned) {
                statusColor = 0x4CAF50;
            } else if (cooldown != null) {
                statusColor = 0xF39C12;
            } else {
                statusColor = 0x777777;
            }
            statusDot.hardlight(statusColor);

            Wand wand = control.getActiveWand();

            // 装备槽图标随换装同步。
            Item shown = control.getMageStaff() != null
                    ? control.getMageStaff()
                    : control.getWand();
            if (shown != null) {
                if (btnWand.item() != shown) {
                    btnWand.item(shown);
                }
            } else if (!(btnWand.item() instanceof WndBag.Placeholder)) {
                btnWand.item(new WndBag.Placeholder(ItemSpriteSheet.WAND_HOLDER));
            }

            String wandNameString = wand == null
                    ? Messages.get(this, "no_wand")
                    : formatWandName(wand);
            if (!wandName.text().equals(wandNameString)) {
                wandName.text(wandNameString);
            }

            String damageString = damageOf(wand);
            if (!damageText.text().equals(damageString)) {
                damageText.text(damageString);
            }

            String chargeString = chargeOf(wand);
            if (!chargeText.text().equals(chargeString)) {
                chargeText.text(chargeString);
            }

            String nextString = nextChargeOf(wand, control, summoned);
            if (!nextCharge.text().equals(nextString)) {
                nextCharge.text(nextString);
            }

            // 八角法阵随状态与充能进度变化；右侧三条进度条显示伤害、充能和下一充能。
            float chargeProgress = 0f;

            if (wand != null) {
                float max = Math.max(1, wand.maxCharges);
                float partial = GameMath.gate(0f, wand.partialCharge, 1f);
                chargeProgress = GameMath.gate(0f,
                        (wand.curCharges + partial) / max, 1f);

                boolean hasDamage = wand instanceof DamageWand;
                damageBarBg.visible = hasDamage;
                damageBarFill.visible = hasDamage;
                if (hasDamage) {
                    float ratio = GameMath.gate(0f,
                            ((DamageWand) wand).max() / 60f, 1f);
                    damageBarFill.setSize(Math.max(1f, rightWidth * ratio), 4);
                }

                chargeBarBg.visible = true;
                chargeBarFill.visible = true;
                chargeBarFill.setSize(Math.max(1f, rightWidth * chargeProgress), 4);

                boolean charging = wand.curCharges < wand.maxCharges;
                nextBarBg.visible = charging;
                nextBarFill.visible = charging;
                if (charging) {
                    nextBarFill.setSize(Math.max(1f, rightWidth * partial), 4);
                }
            } else {
                damageBarBg.visible = false;
                damageBarFill.visible = false;
                chargeBarBg.visible = false;
                chargeBarFill.visible = false;
                nextBarBg.visible = false;
                nextBarFill.visible = false;
            }

            magicCircle.setColor(wandFxColor(wand));
            if (firstRefresh) {
                firstRefresh = false;
                // 窗口刚打开时直接采用当前状态，避免每次打开都重播破碎粒子。
                magicCircle.initState(wand != null, summoned, chargeProgress);
            } else {
                magicCircle.updateState(wand != null, summoned, chargeProgress);
            }

            // 召唤按钮：在场或冷却中置灰，文本随状态变化。
            String summonLabel;
            if (summoned) {
                summonLabel = Messages.get(this, "status_summoned");
            } else if (cooldown != null) {
                summonLabel = Messages.get(this, "status_cooling",
                        (int) cooldown.visualcooldown());
            } else {
                summonLabel = Messages.get(this, "summon");
            }
            if (!btnSummon.text().equals(summonLabel)) {
                btnSummon.text(summonLabel);
            }
            btnSummon.enable(!summoned && cooldown == null);

            layoutRefresh();
        }

        private void layoutRefresh() {
            float y = circleCenterY - CIRCLE_RADIUS;

            wandName.setPos(rightX, y);
            y = wandName.bottom() + 2;

            damageText.setPos(rightX, y);
            y = damageText.bottom() + 2;

            if (damageBarBg.visible) {
                damageBarBg.x = rightX;
                damageBarBg.y = y;
                damageBarFill.x = rightX;
                damageBarFill.y = y;
                y += 4 + 4;
            }

            chargeText.setPos(rightX, y);
            y = chargeText.bottom() + 2;

            if (chargeBarBg.visible) {
                chargeBarBg.x = rightX;
                chargeBarBg.y = y;
                chargeBarFill.x = rightX;
                chargeBarFill.y = y;
                y += 4 + 4;
            }

            nextCharge.setPos(rightX, y);
            y = nextCharge.bottom() + 2;

            if (nextBarBg.visible) {
                nextBarBg.x = rightX;
                nextBarBg.y = y;
                nextBarFill.x = rightX;
                nextBarFill.y = y;
                y += 4 + 4;
            }

            status.maxWidth((int) (rightWidth - 10));
            statusDot.x = rightX;
            statusDot.y = y + 1;
            status.setPos(rightX + 10, y);
            float statusBottom = status.bottom();

            float circleBottom = circleCenterY + CIRCLE_RADIUS;
            float contentBottom = Math.max(circleBottom, statusBottom);
            btnSummon.setRect(0, contentBottom + GAP, WIDTH, 16);

            int newHeight = (int) (btnSummon.bottom() + 2);
            if (newHeight != (int) height) {
                resize(WIDTH, newHeight);
            }
        }

        /**
         * 读取法杖 fx 粒子的主色。各法杖的 fx(Ballistica, Callback)
         * 使用不同的粒子调色板，这里按法杖类型取对应颜色；
         * 未覆盖的法杖返回法阵原始紫色。
         */
        private static int wandFxColor(Wand wand) {
            if (wand == null) {
                return 0xFF9B59B6;
            }

            // 烈阳法杖的子类也统一用金色，避免只按类名匹配时漏掉。
            if (wand instanceof WandOfSun) {
                return 0xFFD700;
            }

            switch (wand.getClass().getSimpleName()) {
                case "WandOfMagicMissile":
                    return 0x8AB4FF;
                case "WandOfFireblast":
                case "WandOfFirebolt":
                    return 0xFF7043;
                case "WandOfFrost":
                case "WandOfGodIce":
                    return 0x81D4FA;
                case "WandOfLightning":
                case "WandOfHightHunderStorm":
                    return 0xFFEB3B;
                case "WandOfDisintegration":
                    return 0xCE93D8;
                case "WandOfCorrosion":
                    return 0x9CCC65;
                case "WandOfCorruption":
                    return 0xBA68C8;
                case "WandOfPrismaticLight":
                    return 0xFFFFFF;
                case "WandOfLivingEarth":
                    return 0xC49A6C;
                case "WandOfRegrowth":
                    return 0x66BB6A;
                case "WandOfTransfusion":
                    return 0xE57373;
                case "WandOfWarding":
                    return 0x64B5F6;
                case "WandOfBlastWave":
                    return 0xB0BEC5;
                case "WandOfSun":
                    return 0xFFD700;
                case "WandOfVenom":
                    return 0x9CCC65;
                case "WandOfBlueFuck":
                    return 0x64B5F6;
                case "WandOfScale":
                    return 0x80CBC4;
                case "WandOfAnmy":
                    return 0xFFAB91;
                default:
                    return 0x9B59B6;
            }
        }

        private String damageOf(Wand wand) {
            if (wand instanceof DamageWand) {
                return Messages.get(this, "damage_line",
                        ((DamageWand) wand).min(),
                        ((DamageWand) wand).max());
            }
            return Messages.get(this, "damage_none");
        }

        private String chargeOf(Wand wand) {
            if (wand == null) {
                return Messages.get(this, "charge_none");
            }
            if (wand.curCharges >= wand.maxCharges) {
                return Messages.get(this, "charge_full");
            }
            return Messages.get(this, "charge_line",
                    wand.curCharges, wand.maxCharges);
        }

        private String nextChargeOf(Wand wand, MageHandControl control, boolean summoned) {
            if (wand == null) {
                return Messages.get(this, "next_charge_none");
            }
            if (wand.curCharges >= wand.maxCharges) {
                return Messages.get(this, "next_charge_full");
            }
            if (!summoned || control.mageHand == null) {
                return Messages.get(this, "next_charge_paused");
            }
            int turns = control.mageHand.estimateTurnsToNextCharge();
            return turns > 0
                    ? Messages.get(this, "next_charge", turns)
                    : Messages.get(this, "next_charge_paused");
        }

        private String formatWandName(Wand wand) {
            int lvl = wand.level();
            String name = wand.name();
            if (lvl > 0) {
                name += " +" + lvl;
            }
            if (name.length() > 11) {
                name = name.substring(0, 10) + "…";
            }
            return name;
        }

        /**
         * 纯色块。直接设置 Image 的 width/height 并重建顶点，
         * 不依赖 ColorBlock 的 scale 路径，保证在窗口里一定绘制出来。
         */
        private static class Bar extends Image {

            Bar(float width, float height, int color) {
                super(TextureCache.createSolid(color));
                setSize(width, height);
            }

            void setSize(float width, float height) {
                this.width = width;
                this.height = height;
                // 只改 width/height 不会重建顶点，必须调用 updateVertices()，
                // 否则 ColorBlock 之外的 Bar 只会画出一个 1x1 的小点。
                updateVertices();
            }
        }

        /**
         * 代码绘制的八角魔法阵：外圈、内圈、八角星和中心五角星。
         * 线条由小 Bar 采样构成，外圈 8 个顶点会按充能进度点亮，整体缓慢旋转。
         * <p>法杖被取下时法阵破碎，随后聚合成一个微弱的法阵表示“需要法杖作为核心”；
         * 装回法杖时重新聚合。法师之手不在场时法阵若隐若现。</p>
         */
        private static class MagicCircle extends Group {

            private static final float ROTATION_SPEED = 0.35f; // 弧度/秒
            private static final float SHATTER_TIME = 0.55f;
            private static final float REFORM_TIME = 0.75f;
            private static final float FAINT_FADE_TIME = 0.35f;
            private static final int DEFAULT_COLOR = 0xFF9B59B6;

            private enum State {
                NORMAL, SHATTERING, FAINT, REFORMING, DISSIPATED
            }

            private final float cx;
            private final float cy;
            private final ArrayList<Bar> lines = new ArrayList<>();
            private final ArrayList<float[]> lineRel = new ArrayList<>();
            private final ArrayList<float[]> lineScatter = new ArrayList<>();
            private final Bar[] dots = new Bar[8];
            private final float[] dotRel = new float[16];
            private final float[] dotScatter = new float[24];

            private float angle = 0f;
            private float flickerTime = 0f;
            private float stateTime = 0f;
            private float chargeProgress = 0f;
            private State state = State.NORMAL;
            private int color = 0;

            MagicCircle(float cx, float cy, float radius) {
                this.cx = cx;
                this.cy = cy;

                PointF[] outer = new PointF[8];
                PointF[] inner = new PointF[8];

                for (int i = 0; i < 8; i++) {
                    double a = Math.toRadians(i * 45);
                    outer[i] = new PointF(
                            cx + (float) Math.cos(a) * radius,
                            cy + (float) Math.sin(a) * radius);
                    inner[i] = new PointF(
                            cx + (float) Math.cos(a) * radius * 0.62f,
                            cy + (float) Math.sin(a) * radius * 0.62f);
                }

                // 外圈、内圈、八角星 {8/3}
                for (int i = 0; i < 8; i++) {
                    addLine(outer[i], outer[(i + 1) % 8]);
                    addLine(inner[i], inner[(i + 1) % 8]);
                    addLine(outer[i], outer[(i + 3) % 8]);
                }

                // 中心五角星
                PointF[] star = new PointF[5];
                for (int i = 0; i < 5; i++) {
                    double a = Math.toRadians(-90 + i * 72);
                    star[i] = new PointF(
                            cx + (float) Math.cos(a) * radius * 0.45f,
                            cy + (float) Math.sin(a) * radius * 0.45f);
                }
                for (int i = 0; i < 5; i++) {
                    addLine(star[i], star[(i + 2) % 5]);
                }

                for (int i = 0; i < 8; i++) {
                    Bar dot = new Bar(3, 3, 0xFFFFFFFF);
                    dot.x = outer[i].x - 1.5f;
                    dot.y = outer[i].y - 1.5f;
                    dot.alpha(0.9f);
                    add(dot);
                    dots[i] = dot;

                    float relX = outer[i].x - cx;
                    float relY = outer[i].y - cy;
                    float len = (float) Math.sqrt(relX * relX + relY * relY);
                    float dirX;
                    float dirY;
                    if (len > 0.5f) {
                        dirX = relX / len;
                        dirY = relY / len;
                    } else {
                        double a = Random.Float() * Math.PI * 2;
                        dirX = (float) Math.cos(a);
                        dirY = (float) Math.sin(a);
                    }
                    dotRel[2 * i] = relX;
                    dotRel[2 * i + 1] = relY;
                    dotScatter[3 * i] = dirX;
                    dotScatter[3 * i + 1] = dirY;
                    dotScatter[3 * i + 2] = 10f + Random.Float() * 16f;
                }

                setColor(DEFAULT_COLOR);
            }

            private void addLine(PointF a, PointF b) {
                float dx = b.x - a.x;
                float dy = b.y - a.y;
                float len = (float) Math.sqrt(dx * dx + dy * dy);
                int steps = Math.max(1, (int) (len / 2.5f));

                for (int i = 0; i <= steps; i++) {
                    float t = i / (float) steps;
                    float x = a.x + dx * t;
                    float y = a.y + dy * t;

                    Bar block = new Bar(1.5f, 1.5f, 0xFFFFFFFF);
                    block.x = x;
                    block.y = y;
                    block.alpha(0.55f);
                    add(block);
                    lines.add(block);

                    float relX = x - cx;
                    float relY = y - cy;
                    float dist = (float) Math.sqrt(relX * relX + relY * relY);
                    if (dist < 0.5f) {
                        double a2 = Random.Float() * Math.PI * 2;
                        relX = (float) Math.cos(a2);
                        relY = (float) Math.sin(a2);
                        dist = 1f;
                    }

                    lineRel.add(new float[]{x - cx, y - cy});
                    lineScatter.add(new float[]{
                            relX / dist,
                            relY / dist,
                            12f + Random.Float() * 22f
                    });
                }
            }

            /** 使用法杖 fx 颜色重新着色；没有 fx 时由调用方传默认紫色。 */
            public void setColor(int newColor) {
                int rgb = newColor & 0xFFFFFF;
                if (rgb == color) {
                    return;
                }
                color = rgb;
                for (Bar line : lines) {
                    // 用 color() 而不是 hardlight()：hardlight 对带 alpha 的 ARGB
                    // 会误读红色通道，导致颜色偏色（金色会变绿/偏色）。
                    line.color(rgb);
                }
                for (Bar dot : dots) {
                    dot.color(rgb);
                }
            }

            /**
             * 窗口初次打开：直接进入目标状态，不播放破碎/聚合动画。
             */
            public void initState(boolean hasWand, boolean summoned, float progress) {
                chargeProgress = progress;
                stateTime = 0f;

                if (!hasWand) {
                    // 没法杖时直接显示微弱法阵，避免每次打开窗口都播放破碎粒子。
                    state = State.FAINT;
                    stateTime = FAINT_FADE_TIME;
                } else if (!summoned) {
                    state = State.DISSIPATED;
                } else {
                    state = State.NORMAL;
                }
            }

            /**
             * 由窗口刷新调用，只负责状态切换；动画进度在 update() 里推进。
             */
            public void updateState(boolean hasWand, boolean summoned, float progress) {
                chargeProgress = progress;

                // 没法杖的优先级最高：即使法师之手不在场，也按“没有核心”处理。
                if (!hasWand) {
                    if (state == State.NORMAL
                            || state == State.REFORMING
                            || state == State.DISSIPATED) {
                        state = State.SHATTERING;
                        stateTime = 0f;
                    }
                    return;
                }

                if (!summoned) {
                    // 有法杖但法师之手不在场：法阵若隐若现。
                    if (state != State.DISSIPATED) {
                        state = State.DISSIPATED;
                        stateTime = 0f;
                    }
                    return;
                }

                // 有法杖且法师之手在场：从失能/破碎/若隐若现状态重新聚合。
                if (state == State.FAINT
                        || state == State.SHATTERING
                        || state == State.DISSIPATED) {
                    state = State.REFORMING;
                    stateTime = 0f;
                }
            }

            @Override
            public void update() {
                super.update();

                angle += Game.elapsed * ROTATION_SPEED;
                flickerTime += Game.elapsed;
                stateTime += Game.elapsed;

                float scatter;
                float alphaScale;

                switch (state) {
                    case SHATTERING: {
                        float t = Math.min(1f, stateTime / SHATTER_TIME);
                        scatter = t;
                        alphaScale = 1f - t;
                        if (t >= 1f) {
                            // 破碎后重新凝聚成微弱的法阵，表示需要法杖作为核心供能。
                            state = State.FAINT;
                            stateTime = 0f;
                        }
                        break;
                    }
                    case FAINT: {
                        float ramp = Math.min(1f, stateTime / FAINT_FADE_TIME);
                        float pulse = 0.5f + 0.5f * (float) Math.sin(flickerTime * 1.6f);
                        scatter = 0f;
                        alphaScale = ramp * (0.18f + 0.10f * pulse);
                        break;
                    }
                    case REFORMING: {
                        float t = Math.min(1f, stateTime / REFORM_TIME);
                        scatter = 1f - t;
                        alphaScale = t;
                        if (t >= 1f) {
                            state = State.NORMAL;
                            stateTime = 0f;
                        }
                        break;
                    }
                    case DISSIPATED:
                        scatter = 0f;
                        alphaScale = 1f;
                        break;
                    case NORMAL:
                    default:
                        scatter = 0f;
                        alphaScale = 1f;
                        break;
                }

                float cos = (float) Math.cos(angle);
                float sin = (float) Math.sin(angle);

                for (int i = 0; i < lines.size(); i++) {
                    Bar block = lines.get(i);
                    float[] rel = lineRel.get(i);
                    float[] sc = lineScatter.get(i);

                    float rx = rel[0] * cos - rel[1] * sin;
                    float ry = rel[0] * sin + rel[1] * cos;
                    block.x = cx + rx + sc[0] * sc[2] * scatter;
                    block.y = cy + ry + sc[1] * sc[2] * scatter;

                    float alpha;
                    if (state == State.DISSIPATED) {
                        alpha = 0.55f * flicker(i, 0.12f, 0.9f);
                    } else {
                        alpha = 0.55f * alphaScale;
                    }
                    block.alpha(alpha);
                    block.visible = alpha > 0.01f;
                }

                int lit = Math.round(8 * chargeProgress);
                for (int i = 0; i < 8; i++) {
                    Bar dot = dots[i];

                    float rx = dotRel[2 * i] * cos - dotRel[2 * i + 1] * sin;
                    float ry = dotRel[2 * i] * sin + dotRel[2 * i + 1] * cos;
                    dot.x = cx + rx + dotScatter[3 * i] * dotScatter[3 * i + 2] * scatter - 1.5f;
                    dot.y = cy + ry + dotScatter[3 * i + 1] * dotScatter[3 * i + 2] * scatter - 1.5f;

                    float alpha;
                    if (state == State.DISSIPATED) {
                        alpha = 0.9f * flicker(i, 0.18f, 0.6f);
                    } else if (state == State.NORMAL) {
                        alpha = (i < lit ? 0.95f : 0.2f) * alphaScale;
                    } else {
                        alpha = 0.9f * alphaScale;
                    }
                    dot.alpha(alpha);
                    dot.visible = alpha > 0.01f;
                }
            }

            /** 每个块相位不同的闪烁，做出若隐若现的效果。 */
            private float flicker(int index, float speed, float amplitude) {
                return 0.05f + amplitude * (0.5f + 0.5f
                        * (float) Math.sin(flickerTime * 3.2f + index * speed));
            }
        }
    }

    /**
     * 法师之手的永久控制器和装备容器。
     * <p>控制器本身是背包中的物品，不会随法师之手消散。场景实体存在时，
     * 法师之手存在时，实际装备临时放在 MageHand 上；法师之手消散后，装备再转存到 storedWand /storedMageStaff，从而保证无论实体是否在场都只有一份有效存档。
     */
    public static class MageHandControl extends Item {
        public static final String AC_HAND = "HAND";
        public static final String AC_SUMMON_HAND = "SUMMON_HAND";
        private static final String STORED_WAND = "stored_wand";
        private static final String STORED_MAGE_STAFF = "stored_mage_staff";

        public Wand storedWand;
        public MagesStaff storedMageStaff;
        public MageHand mageHand = null;

        /**
         * 保存控制器自己的离线装备槽。
         * 法师之手存在时，装备位于实体字段中，此处通常为空。
         */
        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);

            if (storedWand != null) {
                bundle.put(STORED_WAND, storedWand);
            }

            if (storedMageStaff != null) {
                bundle.put(STORED_MAGE_STAFF, storedMageStaff);
            }
        }

        // 只恢复控制器保存的离线装备；实体装备由 MageHand 自己恢复。
        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);

            if (bundle.contains(STORED_WAND)) {
                storedWand = (Wand) bundle.get(STORED_WAND);
            }

            if (bundle.contains(STORED_MAGE_STAFF)) {
                storedMageStaff =
                        (MagesStaff) bundle.get(STORED_MAGE_STAFF);
            }
        }

        /**
         * 返回当前实际装备的普通法杖。
         * 实体在场时读取实体，实体不在场时读取控制器。调用者不需要关心装备当前位于哪一侧。
         */
        public Wand getWand() {
            return mageHand != null
                    ? mageHand.equippedWand
                    : storedWand;
        }

        // 老魔杖与普通法杖使用同一套“实体优先、控制器兜底”的读取规则。
        public MagesStaff getMageStaff() {
            return mageHand != null
                    ? mageHand.magesStaff
                    : storedMageStaff;
        }

        /**
         * 召唤实体时把控制器保存的装备交给实体。
         * 交接完成后清空控制器中的槽位，并停止已有的 Wand.Charger，防止同一根法杖同时被两套对象引用和充能。
         */
        public void bindStoredGear(MageHand hand) {
            hand.equippedWand = storedWand;
            hand.magesStaff = storedMageStaff;

            storedWand = null;
            storedMageStaff = null;

            if (hand.equippedWand != null) {
                hand.equippedWand.stopCharging();
            }

            if (hand.magesStaff != null
                    && hand.magesStaff.wand != null) {
                hand.magesStaff.wand.stopCharging();
            }
            updateQuickslot();
        }

        /**
         * 法师之手消散时把仍装备在法师之手上的物品转回控制器。
         * 这里只负责转移引用；随后会由消散流程移出场景。
         */
        public void storeGearFrom(MageHand hand) {
            // 只转移实体上真实存在的装备，避免覆盖控制器里已经保存的离线装备。
            if (hand.equippedWand != null) {
                storedWand = hand.equippedWand;
            }
            if (hand.magesStaff != null) {
                storedMageStaff = hand.magesStaff;
            }

            hand.equippedWand = null;
            hand.magesStaff = null;
        }

        /**
         * 更换普通法杖。
         * 普通法杖和老魔杖共用一个逻辑装备位，因此装备普通法杖会清空老魔杖槽位
         */
        public void equipWand(Wand wand) {
            Wand oldWand = getWand();

            if (oldWand != null && oldWand != wand) {
                oldWand.stopCharging();
            }

            if (mageHand != null) {
                mageHand.equippedWand = wand;
                mageHand.magesStaff = null;
            } else {
                storedWand = wand;
                storedMageStaff = null;
            }

            if (wand != null) {
                wand.stopCharging();
            }

            updateQuickslot();
        }

        /**
         * 更换老魔杖
         * 老魔杖同样占用唯一的装备位，装备后会清空普通法杖槽位。
         */
        public void equipMageStaff(MagesStaff staff) {
            MagesStaff oldStaff = getMageStaff();

            if (oldStaff != null && oldStaff != staff
                    && oldStaff.wand != null) {
                oldStaff.wand.stopCharging();
            }

            if (mageHand != null) {
                mageHand.magesStaff = staff;
                mageHand.equippedWand = null;
            } else {
                storedMageStaff = staff;
                storedWand = null;
            }

            if (staff != null && staff.wand != null) {
                staff.wand.stopCharging();
            }

            updateQuickslot();
        }

        // 控制器是唯一物品，使用固定的背包图标。
        {
            unique = true;
            image = ItemSpriteSheet.WAND_HAND_CONTROL;
        }

        @Override
        public String defaultAction(){
            return AC_SUMMON_HAND;
        }

        // 法师之手不在场时显示暗红色微光，提示控制器仍保存着已装备的法杖。
        @Override
        public ItemSprite.Glowing glowing() {

            return mageHand != null ? null : new ItemSprite.Glowing(0x880000, 1f) ;
        }

        // 控制器本身不是装备强化目标。
        @Override
        public boolean isUpgradable() {
            return false;
        }

        /**
         * 快捷栏状态显示当前装备法杖的充能。
         * 统一通过 getMageStaff()/getWand() 读取，所以换装或消散后仍然正确。
         */
        @Override
        public String status() {
            MagesStaff staff = getMageStaff();
            if (staff != null && staff.wand != null) {return staff.wand.curCharges + "/" + staff.wand.maxCharges;}
            Wand wand = getWand();
            if (wand != null) {return wand.curCharges + "/" + wand.maxCharges;}

            return "";
        }



        // 控制器始终可见、可识别，不需要鉴定。
        @Override
        public boolean isIdentified() {
            return true;
        }

        @Override
        public ArrayList<String> actions(Hero hero) {
            // HAND 只打开装备窗口；SUMMON_HAND 才执行召唤流程。
            ArrayList<String> actions = super.actions(hero);
            actions.add(AC_HAND);
            actions.add(AC_SUMMON_HAND);
            return actions;
        }

        /**
         * 处理装备窗口和召唤动作。
         * 当前层已有实体时只补齐链接并打开窗口，不会重复生成。
         */
        @Override
        public void execute(Hero hero, String action) {
            super.execute(hero, action);

            switch (action) {
                case AC_HAND:
                    GameScene.show(new WndMageHand(this));
                    break;
                case AC_SUMMON_HAND:
                    MageHand existing = findHandInCurrentLevel();
                    if (existing != null) {
                        this.mageHand = existing;
                        GameScene.show(new WndMageHand(this));
                    } else {
                        summonHand(hero, true);
                    }
                    break;
            }
        }

        /** 在当前楼层查找仍然存活的法师之手。 */
        public static MageHand findHandInCurrentLevel() {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof MageHand && mob.isAlive()) {
                    return (MageHand) mob;
                }
            }
            return null;
        }

        /**
         * 召唤法师之手。
         * <p>手动召唤会消耗一回合并播放入场提示；换层后的自动召回不消耗回合。
         * 装备始终从控制器读取，因此旧楼层实体被回收后法杖不会丢失。</p>
         *
         * @return 是否成功让一只法师之手出现在当前楼层
         */
        public boolean summonHand(Hero hero, boolean manual) {
            MageHand existing = findHandInCurrentLevel();
            if (existing != null) {
                this.mageHand = existing;
                return true;
            }

            SummonCooldown cooldown = hero.buff(SummonCooldown.class);
            if (cooldown != null) {
                if (manual) {
                    GLog.w(Messages.get(
                            MageHand.class,
                            "summon_cooldown",
                            (int) cooldown.visualcooldown()
                    ));
                }
                return false;
            }

            // 旧楼层的实体仍然挂在控制器上时，先把装备收回控制器再召唤，
            // 否则会生成一只没有法杖的“白板手”。
            if (this.mageHand != null) {
                recoverHand(this.mageHand);
                this.mageHand = null;
            }

            ArrayList<Integer> spawnPoints = findMageHandSpawnPoints(hero.pos);
            if (spawnPoints.isEmpty()) {
                if (manual) {
                    GLog.w(Messages.get(MageHand.class, "no_space"));
                }
                return false;
            }

            MageHand newHand = new MageHand();
            newHand.pos = Random.element(spawnPoints);
            newHand.lastLevelDepth = Dungeon.depth;
            newHand.lastLevelBranch = Dungeon.branch;

            this.mageHand = newHand;
            bindStoredGear(newHand);

            GameScene.add(newHand, 1f);
            Dungeon.level.occupyCell(newHand);
            Bestiary.setSeen(MageHand.class);

            if (manual) {
                hero.spend(1f);
                hero.busy();
                hero.sprite.operate(hero.pos);

                newHand.sayAppeared();
                Invisibility.dispel(hero);
                Talent.onArtifactUsed(hero);
            }

            updateQuickslot();
            return true;
        }

        /**
         * 回收一只已经无法跟随玩家的法师之手（通常留在旧楼层）。
         * 把装备转回控制器并从 Actor 调度中移除实体，保证之后可以重新召唤且装备不丢。
         */
        public void recoverHand(MageHand hand) {
            if (hand == null) {
                return;
            }

            if (hand.dissipated) {
                if (hand == this.mageHand) {
                    this.mageHand = null;
                }
                return;
            }

            if (hand == this.mageHand) {
                this.mageHand = null;
            }

            // 只把实体上真实存在的装备转回控制器，避免覆盖控制器中已有的离线装备。
            if (hand.equippedWand != null) {
                storedWand = hand.equippedWand;
            }
            if (hand.magesStaff != null) {
                storedMageStaff = hand.magesStaff;
            }
            hand.equippedWand = null;
            hand.magesStaff = null;

            hand.dissipated = true;
            hand.HP = 0;
            hand.pendingWand = null;
            hand.pendingTarget = null;

            Actor.remove(hand);
            Dungeon.level.mobs.remove(hand);

            if (hand.sprite != null) {
                hand.sprite.killAndErase();
            }

            updateQuickslot();
        }

        /** 当前实际生效的法杖（老魔杖优先）。 */
        public Wand getActiveWand() {
            MagesStaff staff = getMageStaff();
            if (staff != null && staff.wand != null) {
                return staff.wand;
            }
            return getWand();
        }
    }
    /**
     * 寻找法师之手可用的召唤位置。
     * <p>先收集英雄周围两格内的所有可站立位置；如果存在八方向相邻格，
     * 优先使用相邻格，否则才使用半径两格的位置。这样既保持召唤位置稳定，
     * 也在英雄被围住时提供退路。</p>
     */
    private static ArrayList<Integer> findMageHandSpawnPoints(int center) {
        ArrayList<Integer> adjacent = new ArrayList<>();
        ArrayList<Integer> nearby = new ArrayList<>();

        int width = Dungeon.level.width();
        int length = Dungeon.level.length();

        // 先显式重建到英雄的距离图。
        // PathFinder.distance 是全局静态数组，直接读取会残留上一次寻路（甚至上一楼层）
        // 的结果，导致周围明明有空地也提示“空间不足”，只能退出重进才恢复。
        PathFinder.buildDistanceMap(center, BArray.or(
                Dungeon.level.passable, Dungeon.level.avoid, null));

        for (int y = -2; y <= 2; y++) {
            for (int x = -2; x <= 2; x++) {
                if (x == 0 && y == 0) {
                    continue;
                }

                int cell = center + x + y * width;

                if (cell < 0 || cell >= length) {
                    continue;
                }

                // 防止从地图左边界绕到右边界
                if (Dungeon.level.trueDistance(cell, center) > 2.5f) {
                    continue;
                }

                if (Actor.findChar(cell) != null) {
                    continue;
                }

                if (Dungeon.level.pit[cell] || Dungeon.level.solid[cell]) {
                    continue;
                }

                if (!Dungeon.level.passable[cell] && !Dungeon.level.avoid[cell]) {
                    continue;
                }

                // 距离图里不可达的格子（隔墙、封闭区域）不能召唤。
                if (PathFinder.distance[cell] == Integer.MAX_VALUE) {
                    continue;
                }

                if (Dungeon.level.adjacent(cell, center)) {
                    adjacent.add(cell);
                } else {
                    nearby.add(cell);
                }
            }
        }

        // 相邻格可用时优先选择相邻格，否则回退到两格范围内的其他位置。
        return adjacent.isEmpty() ? nearby : adjacent;
    }

    /**
     * 请求法师之手对英雄刚刚命中的敌人追加施法。
     * <p>本方法只做条件检查并创建 pending 事务，真正的伤害和充能扣除会在
     * 施法动画结束后的 onZapComplete() 中完成。这样目标、法杖和充能消耗
     * 都对应发起追击的那一刻，不会因动画期间换装而改变。</p>
     *
     * @return 是否成功登记了一次追加施法
     */
    public static boolean tryFollowUpCast(Char target) {
        if (target == null
                || !target.isAlive()
                || target.alignment != Alignment.ENEMY) {
            return false;
        }

        MageHand hand = null;

        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof MageHand && mob.isAlive()) {
                hand = (MageHand) mob;
                break;
            }
        }

        if (hand == null) {
            return false;
        }

        Wand wand;

        if (hand.magesStaff != null
                && hand.magesStaff.wand != null) {
            wand = hand.magesStaff.wand;
        } else {
            wand = hand.equippedWand;
        }

        if (wand == null
                || wand.curCharges
                < Math.max(1, wand.chargesPerCast())) {
            return false;
        }

        if (!hasHandLineOfFire(hand, target, wand)) {
            return false;
        }

        hand.pendingWand = wand;
        hand.pendingTarget = target;
        hand.pendingFollowUp = true;

        // 兼容旧施法路径：动画入口仍通过 enemy/target 找到当前目标。
        hand.enemy = target;
        hand.target = target.pos;

        if (hand.sprite != null
                && (hand.sprite.visible || target.sprite.visible)) {
            hand.sprite.zap(target.pos);
        } else {
            hand.zap();
        }

        return true;
    }

    /**
     * 判断法师之手能否安全地把法术送到目标。
     * <p>不同法杖使用不同弹道判定：解离需要穿透路径，锥形法杖使用普通直线，
     * 其他法杖则要求弹道确实命中目标且不穿过英雄。</p>
     */
    private static boolean hasHandLineOfFire(
            MageHand hand,
            Char target,
            Wand wand
    ) {
        if (wand instanceof WandOfDisintegration) {
            // 解离必须允许弹道继续穿透，因此使用 WONT_STOP 检查目标是否在射线上。
            Ballistica ray = new Ballistica(
                    hand.pos,
                    target.pos,
                    Ballistica.WONT_STOP
            );

            return ray.path.contains(target.pos);
        }

        if (wand instanceof WandOfFireblast
                || wand instanceof WandOfScale
                || wand instanceof WandOfRegrowth
                || wand instanceof WandOfBlueFuck) {

            // 锥形法杖的 AI 效果由落点方向决定，只要直线能到达目标即可。
            Ballistica direct = new Ballistica(
                    hand.pos,
                    target.pos,
                    Ballistica.MAGIC_BOLT
            );

            return direct.collisionPos == target.pos;
        }

        // 普通法杖要求弹道最终停在目标，并且英雄不在同一条攻击路径上。
        Ballistica normal = new Ballistica(
                hand.pos,
                target.pos,
                Ballistica.MAGIC_BOLT
        );

        return normal.collisionPos == target.pos
                && !hand.isHeroInAttackPath(normal);
    }
}
