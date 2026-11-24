package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MageHand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.utils.Bundle;

/**
 * 法师之手控制器的常驻状态 buff。
 * <p>它随英雄存档一起保存，负责维护控制器、实体链接、待命充能和冷却状态。
 * 实体既可以由玩家主动攻击立即召出，也会在扫描到有效战斗目标时自动出现。</p>
 */
public class MageHandControlBuff extends Buff implements ActionIndicator.Action {

    private static final String LAST_DEPTH = "last_depth";
    private static final String LAST_BRANCH = "last_branch";
    private static final String QUIET_TURNS = "quiet_turns";

    public MageHand.MageHandControl magesStaffcontrol;

    /** 上一次 act 时所在的楼层，用于检测换层。 */
    private int lastDepth = Integer.MIN_VALUE;
    private int lastBranch = Integer.MIN_VALUE;

    // 没有敌人后先保留实体一回合，避免战斗刚结束就立刻闪烁消失。
    private int quietTurns = 0;

    {
        type = buffType.POSITIVE;
    }

    @Override
    public String actionName() {
        return Messages.get(MageHand.MageHandControl.class, MageHand.MageHandControl.AC_HAND);
    }

    @Override
    public boolean act() {
        Hero hero = Dungeon.hero;
        if (hero == null) {
            spend(TICK);
            return true;
        }

        if (magesStaffcontrol == null) {
            magesStaffcontrol = hero.belongings.getItem(MageHand.MageHandControl.class);
        }

        // 记录楼层只用于识别遗留在旧楼层的实体，不再据此自动生成法师之手。
        if (lastDepth != Dungeon.depth || lastBranch != Dungeon.branch) {
            lastDepth = Dungeon.depth;
            lastBranch = Dungeon.branch;
            quietTurns = 0;
        }

        if (magesStaffcontrol != null) {
            MageHand handInLevel = MageHand.MageHandControl.findHandInCurrentLevel();
            MageHand linked = magesStaffcontrol.mageHand;

            // 控制器还挂着一只不在当前层、或已经被移除的手：
            // 先把法杖收回控制器，避免下一次召唤出现没有法杖的“白板手”。
            if (linked != null && linked != handInLevel) {
                magesStaffcontrol.recoverHand(linked);
                magesStaffcontrol.mageHand = null;
            }

            if (handInLevel != null) {
                magesStaffcontrol.mageHand = handInLevel;

                /*
                 * 敌人消失后先让实体停留 1 个行动回合，再回到控制器待命。
                 * 有待结算追击时绝不待命，避免打断尚在飞行的协同施法。
                 */
                if (MageHand.hasActiveCombat(hero)
                        || handInLevel.hasPendingCast()) {
                    quietTurns = 0;
                } else if (++quietTurns > 1) {
                    handInLevel.standby();
                    quietTurns = 0;
                }
            } else {
                quietTurns = 0;

                /*
                 * 自动扫描出现与玩家攻击触发并存。控制器仍会像虹卫一样在战斗
                 * 开始时把待命实体放回场上，但不会在没有法杖或冷却期间出现。
                 */
                if (hero.buff(MageHand.SummonCooldown.class) == null) {
                    if (MageHand.hasActiveCombat(hero)) {
                        MageHand.ensureCombatHand(hero);
                        handInLevel = MageHand.MageHandControl.findHandInCurrentLevel();
                    }

                    /*
                     * 没有实体且不在冷却时，控制器中的法杖继续充能。
                     * 如果刚因没有空位而召唤失败，也保持待命充能。
                     */
                    Wand standbyWand = magesStaffcontrol.getActiveWand();
                    if (handInLevel == null && standbyWand != null) {
                        MageHand.chargeStandbyWand(standbyWand);
                    }
                }
            }

            magesStaffcontrol.updateQuickslot();
        }

        spend(TICK);
        ActionIndicator.setAction(this);
        return true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LAST_DEPTH, lastDepth);
        bundle.put(LAST_BRANCH, lastBranch);
        bundle.put(QUIET_TURNS, quietTurns);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        lastDepth = bundle.getInt(LAST_DEPTH);
        lastBranch = bundle.getInt(LAST_BRANCH);
        if (bundle.contains(QUIET_TURNS)) {
            quietTurns = bundle.getInt(QUIET_TURNS);
        }
        // 读档后延迟到下一次 act 再根据战斗状态决定是否自动出现。
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public int actionIcon() {
        return HeroIcon.BATTLEMAGE;
    }

    @Override
    public int indicatorColor() {
        return 0x6B2D95;
    }

    @Override
    public void doAction() {
        if (magesStaffcontrol != null) {
            magesStaffcontrol.execute(Dungeon.hero, MageHand.MageHandControl.AC_HAND);
        }
    }
}
