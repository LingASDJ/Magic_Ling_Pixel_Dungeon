//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

/**
 * JunglePoison类，继承自MeleeWeapon，代表丛林之剑武器
 * 具有特殊的中毒效果，当敌人中毒程度达到一定程度时还会附加根缚和失明效果
 */
public class JunglePoison extends MeleeWeapon {

    {

        // 初始化武器的基本属性
        image = ItemSpriteSheet.JUNGLE_SWORD;
        tier = 3; // 设置武器等级为3
        DLY = 0.5f; // 设置武器攻击延迟为0.5秒
    }

    /**
     * 计算武器在指定等级下的最小伤害值
     * @param lvl 武器等级
     * @return 最小伤害值，为基础伤害6加上武器等级
     */
    @Override
    public int min(int lvl) {
        return 6 + lvl;
    }
    /**
     * 计算武器在指定等级下的最大伤害值
     * @param lvl 武器等级
     * @return 最大伤害值，为基础伤害12加上武器等级的两倍
     */
    @Override
    public int max(int lvl) {
        return 12 + lvl * 2;
    }

    /**
     * 武器攻击时的特殊效果处理
     * @param attacker 攻击者角色
     * @param defender 防御者角色
     * @param damage 基础伤害值
     * @return 处理后的伤害值，会先应用中毒效果
     */
    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 对防御者施加中毒效果，持续时间为2倍武器等级加3
        Buff.affect(defender, Poison.class).set(2*level()+3);

        // 获取防御者的中毒状态
        Poison enemypoison = defender.buff(Poison.class);
        if (enemypoison != null) {
            // 检查中毒程度是否达到防御者生命值的三分之一
            if(enemypoison.GetPoisonLevel() >= (float) defender.HT /3){
                // 如果中毒程度足够，则对防御者施加根缚和失明效果
                // 效果持续时间与中毒程度相同
                Buff.affect( defender, Roots.class, enemypoison.GetPoisonLevel() );
                Buff.affect( defender, Blindness.class, enemypoison.GetPoisonLevel() );
            }
        }

        return super.proc(attacker, defender, damage);
    }



}
