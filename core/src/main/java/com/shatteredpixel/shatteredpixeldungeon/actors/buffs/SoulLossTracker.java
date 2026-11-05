package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus.LichKing;
import com.watabou.utils.Bundle;

/**
 * 夺魂：巫妖在场时，非巫妖单位每累计损失 20 点生命，就会被施加 1 层失魂。
 *
 * <p>每回合结算一次（比较上一回合结束时的生命值），累计值不会因为治疗而倒退；
 * 巫妖死亡或目标死亡后自行解除。
 */
public class SoulLossTracker extends Buff {

    /** 每损失多少生命换算 1 层失魂。 */
    public static final int HP_PER_STACK = 20;

    {
        type = buffType.NEUTRAL;
        announced = false;
    }

    private int progress = 0;
    private int lastHP;

    @Override
    public boolean attachTo(Char target) {
        boolean success = super.attachTo(target);
        if (success) {
            //记录附身时的生命值，避免把附身前的伤害算进来
            lastHP = target.HP;
        }
        return success;
    }

    @Override
    public boolean act() {
        if (target == null || !target.isAlive() || !LichKing.isLichAlive()) {
            detach();
            return true;
        }

        if (lastHP > target.HP) {
            progress += lastHP - target.HP;
            while (progress >= HP_PER_STACK) {
                progress -= HP_PER_STACK;
                applySoulStack(target);
            }
        }
        lastHP = target.HP;

        spend(TICK);
        return true;
    }

    private void applySoulStack(Char target) {
        LostSoul soul = new LostSoul();
        //失魂不是一次性状态，高频叠加时不再刷屏公告
        soul.announced = false;
        soul.attachTo(target);

        //英雄需要 LostCount 才能在状态栏看到失魂层数
        if (target == Dungeon.hero && target.buff(LostSoul.LostCount.class) == null) {
            Buff.affect(target, LostSoul.LostCount.class);
        }
    }

    private static final String PROGRESS = "progress";
    private static final String LAST_HP = "lastHP";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PROGRESS, progress);
        bundle.put(LAST_HP, lastHP);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        progress = bundle.getInt(PROGRESS);
        lastHP = bundle.getInt(LAST_HP);

        //读档后以当前生命值为准，避免把离线期间的差值补算成失魂
        if (target != null) {
            lastHP = target.HP;
        }
    }
}
