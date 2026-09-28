package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

//驱魔重锤
//四阶，力量需求17
//初始6-20，成长1-5
//在使用此武器的攻击命中后，获得一个持续3回合、能够免疫一次法术攻击的护盾，每次命中刷新持续时间。
//装备驱魔重锤时，你的戒指和神器在处于魔法免疫时仍然可以生效。（包括副手）
//对近战法术攻击者的克星。
//武技：破魔，消耗3充能，获得10回合魔法免疫。

public class ExorcistMaul extends MeleeWeapon {
    {
        image = ItemSpriteSheet.KILLDEMON_HAMMER;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        DLY = 2f;

        tier = 4;
    }

    @Override
    public int max(int lvl) { return 20 + lvl * 5; }

    @Override
    public int min(int lvl) { return 6 + lvl; }

    @Override
    public int STRReq(int lvl){
        int req = STRReq(tier, lvl)+1;
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    // 内部护盾 buff：显示占位符图标
    public static class QuMoHuDun extends FlavourBuff {

        { type = buffType.POSITIVE; }

        // 把剩余时间重置为传入的回合数（每次攻击命中时调用）
        public void refreshDuration(float duration){
            timeToNow();    // 清掉之前的剩余时间
            spend(duration); // 从现在起重新计
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x5B3B8F);
        }
    }

    // 特效一：次数法伤盾
    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 持续时间 3
        float duration = 3;
        // affect：没有就创建，有就复用同一个实例
        QuMoHuDun buff = Buff.affect(attacker, QuMoHuDun.class);
        buff.refreshDuration(duration); // 每次命中都重置
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        return 3;
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        beforeAbilityUsed(hero, null);
        AttackIndicator.target(null);
        Buff.affect(hero, MagicImmune.class,10f);
        hero.spendAndNext(hero.attackDelay());
        afterAbilityUsed(hero);
    }
}