package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

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
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
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

        wand.curCharges--;
        wand.updateQuickslot();

        if (wand.curCharges <= 0) {
            dissipate();
        }
    }

    private boolean dissipated = false;

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
            control.storedMageStaff = magesStaff;
            control.storedWand = equippedWand;
            control.mageHand = null;
            control.updateQuickslot();
        }

        Buff.detach(hero, MageHandControlBuff.class);

        Actor.remove(this);
        Dungeon.level.mobs.remove(this);
        HP = 0;

        if (sprite != null) {
            sprite.killAndErase();
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

        // 计算缺失的充能数
        int missingCharges = equippedWand.maxCharges - equippedWand.curCharges;
        missingCharges = Math.max(0, missingCharges);

        // 计算充能所需回合数
        float turnsToCharge = BASE_CHARGE_DELAY
                + (SCALING_CHARGE_ADDITION * (float) Math.pow(NORMAL_SCALE_FACTOR, missingCharges));

        // 计算本回合的充能量
        float chargeAmount = 1f / turnsToCharge;

        // 应用充能加成（如果有）
        chargeAmount *= getChargeMultiplier();

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
        MageHandControlBuff buff = hero.buff(MageHandControlBuff.class);
        if (buff == null) {
            Buff.affect(hero, MageHandControlBuff.class);
        }

        MageHand.HandWareness mageHandWareness = hero.buff(MageHand.HandWareness.class);
        if(mageHandWareness == null){
            Buff.affect(hero, MageHand.HandWareness.class);
        }

        if(equippedWand != null || magesStaff != null){
            invisible = 0;
        } else {
            invisible = 1;
        }

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

    /**
     * 保存当前存活实体身上的装备。
     * <p>消散后的装备会先转交给 MageHandControl，因此正常情况下这里只负责
     * “实体仍存在”这一种存档状态，避免同一件法杖被保存两次。</p>
     */
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);

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
     * 玩家仍可查看、取出或更换控制器中保存的法杖。</p>
     */
    private static class WndMageHand extends Window {

        private static final int BTN_SIZE  = 32;
        private static final float GAP     = 5;
        private static final int WIDTH     = 116;

        private ItemButton btnWand;
        public MageHand.MageHandControl magesStaffcontrol;
        WndMageHand(final MageHandControl control){
            magesStaffcontrol = control;
            IconTitle titlebar = new IconTitle();
            titlebar.icon( new ItemSprite(ItemSpriteSheet.MAGES_STAFF) );
            titlebar.label( Messages.get(this, "title") );
            titlebar.setRect( 0, 0, WIDTH, 0 );
            add( titlebar );

            RenderedTextBlock message =
                    PixelScene.renderTextBlock(Messages.get(this, "desc"), 6);
            message.maxWidth( WIDTH );
            message.setPos(0, titlebar.bottom() + GAP);
            add( message );

            // 法杖按钮居中显示
            btnWand = new ItemButton(){
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

            // 计算法杖按钮的居中位置
            float wandX = 25;
            btnWand.setRect( wandX, message.top() + message.height() + GAP, BTN_SIZE, BTN_SIZE );

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
            //add( btnAction2 );

            resize(WIDTH, (int)(btnWand.bottom() + 2));
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
            storedWand = hand.equippedWand;
            storedMageStaff = hand.magesStaff;

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
         * 召唤顺序分成四步：复用场上实体、检查冷却、寻找出生点、创建并绑定装备。这样读档后即使控制器尚未建立链接，也不会重复生成。
         */
        @Override
        public void execute(Hero hero, String action) {
            super.execute(hero, action);

            switch (action) {
                case AC_HAND:
                    GameScene.show(new WndMageHand(this));
                    break;
                case AC_SUMMON_HAND:
                    // 先检查当前楼层是否已经存在法师之手（可能是读档后未链接的情况）
                    MageHand existingHand = null;
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (mob instanceof MageHand) {
                            existingHand = (MageHand) mob;
                            break;
                        }
                    }
                    if (existingHand != null) {
                        // 链接到已存在的手，避免重复创建
                        MageHandControl.this.mageHand = existingHand;
                        GameScene.show(new WndMageHand(this));
                        break;
                    }

                    //召唤冷却检查
                    if (hero.buff(SummonCooldown.class) != null) {
                        GLog.w(Messages.get(
                                MageHand.class,
                                "summon_cooldown",
                                (int) hero.buff(SummonCooldown.class).visualcooldown()
                        ));
                        break;
                    }

                    ArrayList<Integer> spawnPoints =
                            findMageHandSpawnPoints(hero.pos);

                    if (spawnPoints.isEmpty()) {
                        GLog.w(Messages.get(MageHand.class, "no_space"));
                        break;
                    }

                    MageHand newHand = new MageHand();
                    newHand.pos = Random.element(spawnPoints);

                    mageHand = newHand;
                    bindStoredGear(newHand);

                    GameScene.add(newHand, 1f);
                    Dungeon.level.occupyCell(newHand);
                    Bestiary.setSeen(MageHand.class);

                    hero.spend(1f);
                    hero.busy();
                    hero.sprite.operate(hero.pos);

                    newHand.sayAppeared();

                    Invisibility.dispel(hero);
                    Talent.onArtifactUsed(hero);
                    updateQuickslot();

                    break;
            }
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

        for (int y = -2; y <= 2; y++) {
            for (int x = -2; x <= 2; x++) {
                if (x == 0 && y == 0) {
                    continue;
                }

                int cell = center + x + y * width;

                if (cell < 0 || cell >= Dungeon.level.length()) {
                    continue;
                }

                // 防止从地图左边界绕到右边界
                if (Dungeon.level.trueDistance(cell, center) > 2.5f) {
                    continue;
                }

                if (Actor.findChar(cell) != null
                        || !Dungeon.level.passable[cell]
                        || Dungeon.level.pit[cell]
                        || Dungeon.level.solid[cell]) {
                    continue;
                }

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
