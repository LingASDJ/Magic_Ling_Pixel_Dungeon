package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

/**
 * <p><font color="orange"><b><h3>酩酊大醉：持续期间无法获得醉意，攻击伤害提升40%，闪避归0，精准下降30%，无法伏击，移动时有50%概率随机移动（同眩晕），受到伤害的50%变为延迟伤害。</h3></b></font></p>
 *
 * <p>效果的实现分散在 6 个文件里，改动任意一处都要记得同步检查其他几处：
 * <ul>
 *   <li>无法获得醉意 — {@link Inebriation#addInebriation(Char, float)}</li>
 *   <li>伤害 +40%    — {@link Char#attack(Char, float, float, float, Char.DamageType)}</li>
 *   <li>精准 -30% / 闪避归 0 — {@link Char#hit(Char, Char, float, boolean)}</li>
 *   <li>无法伏击      — {@link Hero#canSurpriseAttack()}</li>
 *   <li>50% 随机移动  — {@link Char#move(int, boolean)}</li>
 *   <li>受伤 50% 转延时 — {@link Char#damage(int, Object, Char.DamageType)}</li>
 * </ul>
 *
 * @see com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Inebriation
 * @see com.shatteredpixel.shatteredpixeldungeon.actors.blobs.AlcoholGas
 * @see com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity.DeferedDamage
 */

public class Smashed extends FlavourBuff {
    {
        type = buffType.NEUTRAL;
        announced = true;
    }

    public float delayDamage = 0f;

    // 伤害修正系数 +40%
    public float PhysicDamageModifierMultiplier = 0.4f;
    public float PhysicDamageModifier(){
        return PhysicDamageModifierMultiplier + 1f;
    }
    // 命中修正系数 -30%
    public float AccuracyModifierMultiplier = 0.3f;
    public float AccuracyModifier(){
        return 1f - AccuracyModifierMultiplier;
    }
    // 闪避修正系数 -100%
    public float DefenceModifierMultiplier = 1f;
    public float DefenceModifier(){
        return 1f - DefenceModifierMultiplier;
    }
    // 受伤延时-量系数 50% 在Char.java中搜索“酩酊大醉的延时伤害实现”
    public float damageDelayMultiplier = 0.5f;
    // 无法伏击在Hero.java中搜索“酩酊大醉的无法伏击实现”
}
