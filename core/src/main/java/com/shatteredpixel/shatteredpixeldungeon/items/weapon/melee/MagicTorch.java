package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;


/**
 * 魔法火把
 */
public class MagicTorch extends MeleeWeapon {

    /**
     * 重写父类的属性
     * @param tier 等级
     * @param image 图片
     * @param ACC 命中率
     */ {
        image = ItemSpriteSheet.MAGIC_TORCH;
        tier = 2;
        ACC = 0.8f;
    }

    /**
     * 重写父类的方法
     *
     * @param lvl 等级
     * @return 最小伤害
     */
    @Override
    public int max(int lvl) {
        return 5 * (tier) +    //base
                lvl * (tier + 1);   //level scaling
    }

    /**
     * 重写父类的方法
     *
     * @param lvl 等级
     * @return 最大伤害
     */
    @Override
    public int STRReq(int lvl) {
        return (7 + tier * 2) - (int) (Math.sqrt(8 * lvl + 1) - 1) / 2;
        //19 base strength req, up from 18
    }

    /**
     * 重写父类的方法
     *
     * @param hero   英雄
     * @param action 动作
     */
    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        //当英雄使用魔法火把时，给英雄添加一个光环buff，光环buff会对周围的敌人造成伤害，伤害值为英雄的魔法火把的最大伤害值，持续时间为5回合，每回合造成一次伤害，伤害值为英雄的魔法火把的最大伤害值，光环buff的名称为“光环”
        if (action.equals(AC_EQUIP)) {
            //In addition to equipping itself, item reassigns itself to the quickslot
            //This is a special case as the item is being removed from inventory, but is staying with the hero.
            int slot = Dungeon.quickslot.getSlot(this);
            doEquip(hero);
            if (slot != -1) {
                Dungeon.quickslot.setSlot(slot, this);
                updateQuickslot();
            }
            //Buff.affect(hero, LighS.class);
        } else if (action.equals(AC_UNEQUIP)) {
            doUnequip(hero, true);
            //Buff.detach(hero, LighS.class);
        }
    }

    /**
     * 重写父类的proc方法
     *
     * @param attacker 攻击者
     * @param defender 防御者
     * @param damage   伤害
     */
    @Override
    public int proc(Char attacker, Char defender, int damage) {
        /**
         * @param defender 防御者
         * @param Burning.class 伤害类型
         * @param hero 伤害来源
         * @param 4f + level() / 5f 伤害值
         */
        Buff.affect(defender, Burning.class).reignite(hero, 4f + level() / 5f);

        return super.proc(attacker, defender, damage);
    }
}
