package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MageHand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

/**
 * 法师之手控制器的常驻状态 buff。
 * <p>它随英雄存档一起保存，是“法师之手是否存在 / 是否跟随到新楼层”的权威状态，
 * 类似戒指通过 RingBuff 把运行状态挂在英雄身上。实体本身只负责战斗与跟随，
 * 即使实体被销毁、被遗留在旧楼层，这个 buff 仍然存在并可把装备收回控制器。</p>
 */
public class MageHandControlBuff extends Buff implements ActionIndicator.Action {

    private static final String LAST_DEPTH = "last_depth";
    private static final String LAST_BRANCH = "last_branch";

    public MageHand.MageHandControl magesStaffcontrol;

    /** 上一次 act 时所在的楼层，用于检测换层。 */
    private int lastDepth = Integer.MIN_VALUE;
    private int lastBranch = Integer.MIN_VALUE;

    /** 换层后是否还需要尝试把法师之手召回。 */
    private boolean restoreDue = false;

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

        // 换层检测：只有真正换了楼层才自动召回，避免战斗或读档时凭空召唤。
        if (lastDepth != Dungeon.depth || lastBranch != Dungeon.branch) {
            lastDepth = Dungeon.depth;
            lastBranch = Dungeon.branch;
            restoreDue = true;
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
                restoreDue = false;
            } else if (restoreDue && hero.buff(MageHand.SummonCooldown.class) == null) {
                restoreDue = false;
                if (magesStaffcontrol.summonHand(hero, false)) {
                    GLog.i(Messages.get(MageHand.class, "auto_summoned"));
                } else {
                    GLog.w(Messages.get(MageHand.class, "no_space_auto"));
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
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        lastDepth = bundle.getInt(LAST_DEPTH);
        lastBranch = bundle.getInt(LAST_BRANCH);
        // 读档后不自动召唤：当前层已有的手会在下一次 act 重新链接，
        // 没有手则等玩家主动召唤，避免读档瞬间刷出一只新手。
        restoreDue = false;
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
