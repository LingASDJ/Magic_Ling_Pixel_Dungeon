package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.AlcoholGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

//醉意：提升至30%时持续获得微醺直到醉意<=15%，提升至100%时转变为30回合酩酊大醉。如果不处于酒气中，每回合会自然衰减1%。
public class Inebriation extends Buff{

    public static final float MAX_VALUE = 100f;            // 最大醉意值
    public static final float PER_TURN_IN_GAS = 10f;       // 在酒气中每回合 +10%
    public static final float DECAY_PER_TURN   = -1f;       // 离开后每回合 -1%
    public static final float THRESHOLD_FULL   = 100f;     // 转酩酊大醉阈值
    public static final float THRESHOLD_TIPSY_ON  = 30f;   // 微醺开阈值
    public static final float THRESHOLD_TIPSY_OFF  = 15f;  // 微醺关阈值
    {
        type = buffType.NEUTRAL;
        announced = false;
    }

    private boolean enableTipsyEffect =false;              // 微醺施加开关
    public float value = 0;                                // 醉意值（0-100）

    // 自然醒酒 以及 达到阈值带来其他buff
    @Override
    public boolean act() {

        if (target == null || !target.isAlive()) { detach(); return true; }    // 空保护

        boolean inGas = Blob.volumeAt(target.pos, AlcoholGas.class) > 0;       // 在醉酒气体中判据

        // 大于等于100%时转化为30回合的酩酊大醉
        if (value >= THRESHOLD_FULL){
            if (target.buff(Smashed.class)==null) Buff.affect(target, Smashed.class, 30f);
            value = 0f;  // 后续分支会在归0时清理本buff，本分支仅作归0
        }

        // 大于30%时打开微醺施加开关
        if (value >= THRESHOLD_TIPSY_ON){
            enableTipsyEffect = true;
        }
        // 小于15%时关闭微醺施加开关
        else if (value <= THRESHOLD_TIPSY_OFF){
            enableTipsyEffect = false;
        }

        // 持续获得微醺
        if(enableTipsyEffect){
            Buff.prolong(target, TipsyEffect.class, 2f);
        }

        if (inGas)  addInebriation(target, PER_TURN_IN_GAS);  // 在醉酒气体中且不处于酩酊大醉时加10%
        else        addInebriation(target, DECAY_PER_TURN);   // 不在时-1%

        // 达0%时消除本buff
        if (value <= 0){
            detach();
            return true;
        }

        spend(TICK);
        return true;
    }

    // 提供静态方法，方便外部（内部也用这样方便维护）（比如药水、武器）随时给角色加醉意值
    public static void addInebriation(Char ch, float amount) {
        if (ch == null || amount == 0f) return;
        if (amount > 0 && ch.buff(Smashed.class) != null) return;  // 酩酊大醉情况下阻断醉意增长
        Inebriation buff = Buff.affect(ch, Inebriation.class);
        buff.value += amount;

        // 夹逼到0~MAX_VALUE之间
        buff.value = Math.min(MAX_VALUE, buff.value);
        buff.value = Math.max(0f, buff.value);
    }

    // 获取当前的醉意值
    public static float getValue(Char ch) {
        Inebriation buff = ch.buff(Inebriation.class);
        return buff == null ? 0 : buff.value;
    }

    private static final String VALUE = "value";      // 醉意值序列化
    private static final String ENABLE_TIPSY = "enableTipsyEffect";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(VALUE, value);
        bundle.put(ENABLE_TIPSY, enableTipsyEffect);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        value = bundle.getFloat(VALUE);
        enableTipsyEffect = bundle.getBoolean(ENABLE_TIPSY);
    }

    // UI 图标
    @Override
    public int icon() {
        return BuffIndicator.INEBRIATION;
    }

}
