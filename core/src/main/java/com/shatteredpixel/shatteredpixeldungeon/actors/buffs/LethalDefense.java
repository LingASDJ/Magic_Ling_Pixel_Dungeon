package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class LethalDefense extends Buff{

    public enum PendingAction {
        WAITING_SKILL,
        WAITING_FINISHER
    }

    private PendingAction pendingAction;
    private float duration;
    //固定为 4 回合
    public void waitFor(PendingAction action) {
        pendingAction = action;
        duration = 4f;
    }
    //判断是否符合触发方向
    public boolean matches(boolean isFinisher) {
        if (pendingAction == PendingAction.WAITING_SKILL) {
            return !isFinisher;
        }

        return pendingAction == PendingAction.WAITING_FINISHER
                && isFinisher;
    }
    //存档
    private static final String DURATION = "duration";
    private static final String PENDING_ACTION = "pending_action";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DURATION, duration);
        bundle.put(PENDING_ACTION, pendingAction);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        duration = bundle.getFloat(DURATION);

        if (bundle.contains(PENDING_ACTION)) {
            pendingAction = bundle.getEnum(
                    PENDING_ACTION,
                    PendingAction.class
            );
        }
    }

    /**以战养战循环
     战技之后：等待终结技 4 回合；
     终结技之后：等待战技 4 回合；
     连续使用同类动作不会触发奖励，只会重新开始等待相反类型；
     可以形成“战技→终结技→战技→终结技”的连续循环。
     */
    public static void onComboAction(
            Hero hero,
            boolean isFinisher,
            Char enemy
    ) {
        if (hero == null
                || hero.subClass != HeroSubClass.GLADIATOR) {
            return;
        }

        int points = hero.pointsInTalent(
                Talent.LETHAL_DEFENSE
        );

        if (points <= 0) {
            return;
        }

        LethalDefense tracker =
                hero.buff(LethalDefense.class);

        boolean matched =
                tracker != null
                        && tracker.matches(isFinisher);

        if (matched) {
            int comboGain = points;

            Combo combo = Buff.affect(
                    hero,
                    Combo.class
            );

            for (int i = 0; i < comboGain; i++) {
                combo.hit(enemy);
            }

            int adrenalineTurns = points - 1;
            if (adrenalineTurns > 0) {
                Buff.prolong(
                        hero,
                        Adrenaline.class,
                        adrenalineTurns
                );
            }

            tracker.detach();
        }

        // 反转下一步等待方向，并重新开始4回合窗口。
        LethalDefense next = Buff.affect(
                hero,
                LethalDefense.class
        );

        next.waitFor(
                isFinisher
                        ? PendingAction.WAITING_SKILL
                        : PendingAction.WAITING_FINISHER
        );
    }

    @Override
    public boolean act() {
        duration-=TICK;
        spend(TICK);
        if (duration <= 0) {
            detach();
        }
        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.COMBO;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0f, (4f - duration) / 4f);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int)duration);
    }

    @Override
    public String desc() {
        String key = pendingAction == PendingAction.WAITING_SKILL
                ? "desc_skill"
                : "desc_finisher";

        return Messages.get(this, key, dispTurns(duration));
    }

}
