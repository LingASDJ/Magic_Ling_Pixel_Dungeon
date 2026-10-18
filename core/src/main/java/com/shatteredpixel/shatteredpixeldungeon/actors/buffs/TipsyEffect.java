package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

// 微醺：物理伤害提升15%，精准和闪避均下降15%。
public class TipsyEffect extends FlavourBuff{
    {
        type = buffType.NEUTRAL;
        announced = true;
    }

    // 物理伤害修正系数
    public float PhysicDamageModifierMultiplier = 0.15f;
    public float PhysicDamageModifier(){
        return PhysicDamageModifierMultiplier + 1f;
    }
    // 命中修正系数
    public float AccuracyModifierMultiplier = 0.15f;
    public float AccuracyModifier(){
        return 1f - AccuracyModifierMultiplier;
    }
    // 闪避修正系数
    public float DefenceModifierMultiplier = 0.15f;
    public float DefenceModifier(){
        return 1f - DefenceModifierMultiplier;
    }

    // UI 图标
    @Override
    public int icon() {
        return BuffIndicator.HUNGER;  // TODO 暂用
    }
}
