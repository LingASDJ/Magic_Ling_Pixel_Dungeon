package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

//驱魔重锤
//四阶，力量需求17
//初始6-20，成长1-5，精准1.2
//在使用此武器的攻击命中后，获得一个持续20+等级*4回合、能够免疫一次法术攻击的护盾，每次命中刷新持续时间。
//对近战法术攻击者的克星。

public class ExorcistMaul extends MeleeWeapon{
    {
        image = ItemSpriteSheet.KILLDEMON_HAMMER;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        ACC = 1.2f;
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

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 持续时间 20+等级*4
        float duration = 20f + buffedLvl()*4;
        // affect：没有就创建，有就复用同一个实例
        QuMoHuDun buff = Buff.affect(attacker, QuMoHuDun.class);
        buff.refreshDuration(duration); // 每次命中都重置
        return super.proc(attacker, defender, damage);
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

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }
    }

    @Override
    public String targetingPrompt() {
        return null;
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        return 2;
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