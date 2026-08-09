package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.IconFloatingText;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class DeathBuff extends ElementalBuff {
    public static int level = 0;
    // 配置常量
    private static final int CAP = 100;                 // 侵蚀上限100%
    private static final int LOCK_DURATION = 108;      // 满层锁定持续回合
    private static final int DECAY_TRIGGER = 72;       // 多久无新增开始衰减
    private static final int DECAY_INTERVAL = 36;       // 后续衰减间隔
    private static final int DECAY_INITIAL = 6;         // 首次衰减数值
    private static final int DECAY_SUBSEQUENT = 3;      // 后续每次衰减数值

    private int lockTimer = 0;
    public int decayTimer = 0;
    private boolean isLocked = false;

    {
        elementalIcon = BuffIndicator.DEATH;
    }

    /**
     * 受到伤害增幅：每2%死亡侵蚀 → 受到伤害+1%，上限100侵蚀=+50%承受伤害
     */
    public float damageBonus() {
        return level / 200f;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (CAP - level) / (float) CAP);
    }

    /**
     * 受到伤害触发侵蚀增长
     * @param percentHP 损失生命值占最大生命值百分比
     * 规则：每损失3%最大生命值，增加1点侵蚀
     */
    @Override
    public void onDamageTaken(float percentHP) {
        if (isLocked) return;

        int gain = Math.round(percentHP / 3f);
        int oldLevel = level;
        level = Math.min(CAP, level + gain);

        decayTimer = 0;

        // 只有数值真正上涨时才弹出文字
        if (level > oldLevel) {
            target.sprite.showStatusWithIcon(CharSprite.NEGATIVE, String.valueOf(level), IconFloatingText.DEATH);
        }

        if (level == CAP) {
            isLocked = true;
            lockTimer = LOCK_DURATION;
        }
    }

    /**
     * 侵蚀进度专用方法
     * @param addLevel 增加的侵蚀数值
     */
    public void getDeath(int addLevel) {
        if (isLocked) return;

        level = Math.min(CAP, level + addLevel);
        decayTimer = 0;

        // 到达上限触发锁定
        if (level == CAP) {
            isLocked = true;
            lockTimer = LOCK_DURATION;
        }
    }

    @Override
    public String iconTextDisplay() {
        return String.valueOf(level)+"%";
    }

    @Override
    public boolean act() {
        if (!target.isAlive()) {
            detach();
            return true;
        }

        // 锁定状态处理
        if (isLocked) {
            lockTimer--;
            if (lockTimer <= 0) {
                level = 0;
                isLocked = false;
                decayTimer = 0;
            }
            spend(1);
            return true;
        }

        // 正常状态：侵蚀衰减计时
        decayTimer++;
        if (decayTimer == DECAY_TRIGGER) {
            level = Math.max(0, level - DECAY_INITIAL);
        } else if (decayTimer > DECAY_TRIGGER && (decayTimer - DECAY_TRIGGER) % DECAY_INTERVAL == 0) {
            level = Math.max(0, level - DECAY_SUBSEQUENT);
        }

        // 侵蚀归零，移除buff
        if (level <= 0) {
            detach();
            return true;
        }

        spend(1);
        return true;
    }

    private static final String LOCK_TIMER = "lockTimer";
    private static final String DECAY_TIMER = "decayTimer";
    private static final String IS_LOCKED = "isLocked";

    @Override
    public String desc() {
        StringBuilder sb = new StringBuilder(Messages.get(this, "desc"));
        sb.append("\n\n");
        sb.append(Messages.get(this, "level", level));
        if (isLocked) {
            sb.append("\n").append(Messages.get(this, "locked", lockTimer));
        } else {
            sb.append("\n").append(Messages.get(this, "bonus", Math.round(damageBonus() * 100)));
            if (decayTimer > 0) {
                int nextDecay;
                if (decayTimer < DECAY_TRIGGER) {
                    nextDecay = DECAY_TRIGGER - decayTimer;
                } else {
                    nextDecay = DECAY_INTERVAL - (decayTimer - DECAY_TRIGGER) % DECAY_INTERVAL;
                }
                sb.append("\n").append(Messages.get(this, "decay_in", nextDecay));
            }
        }
        return sb.toString();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LOCK_TIMER, lockTimer);
        bundle.put(DECAY_TIMER, decayTimer);
        bundle.put(IS_LOCKED, isLocked);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        lockTimer = bundle.getInt(LOCK_TIMER);
        decayTimer = bundle.getInt(DECAY_TIMER);
        isLocked = bundle.getBoolean(IS_LOCKED);
    }
}