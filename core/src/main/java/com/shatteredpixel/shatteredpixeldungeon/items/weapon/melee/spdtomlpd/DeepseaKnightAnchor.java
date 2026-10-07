package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//深海骑士的锚
//五阶，力量需求20
//初始10-50，成长1-10，攻击延迟2
//在使用此武器攻击命中敌人后，清除自身的所有负面效果。
//武技：激愤
//获得5+等级回合激素涌动。消耗3点充能。这个武技不消耗回合。
//附有秘密姬的加护的大锚，足以粉碎坚物的它最后沉入了深海。
public class DeepseaKnightAnchor extends MeleeWeapon {
    {
        image = ItemSpriteSheet.SEA_SWORD;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        DLY = 2f;

        tier = 5;
    }
    @Override
    public int max(int lvl) { return 50 + lvl * 10; }

    @Override
    public int min(int lvl) { return 10 + lvl; }

    // ==================== 武技：激愤 ====================

    // 每次使用武技消耗的充能点数（由决斗者的 Charger buff 提供）
    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 3;
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        // 激素涌动回合数
        int duration = 5+buffedLvl();

        // 1. 先扣充能：beforeAbilityUsed 会按 baseChargeUse 的返回值扣掉对应充能
        beforeAbilityUsed(hero, null);

        // 2. 上激素涌动效果
        Buff.affect(hero, Adrenaline.class, duration);

        // 3. 播放使用动作，并消耗一个回合
        hero.sprite.operate(hero.pos);
        hero.next();

        // 4. 武技收尾：处理与武技相关的天赋联动
        afterAbilityUsed(hero);
    }

    // ==================== 特效 ====================

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        // 魔皇草同款净化
        PotionOfHealing.cure(attacker);
        return super.proc(attacker, defender, damage);
    }
}
