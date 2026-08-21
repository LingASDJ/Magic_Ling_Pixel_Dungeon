package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

//驱魔重锤
//四阶，力量需求17
//初始6-20，成长1-5，精准1.2
//在使用此武器的攻击命中后，获得一个持续20+等级*4回合、能够免疫一次法术攻击的护盾，每次命中刷新持续时间。
//对近战法术攻击者的克星。

public class ExorcistMaul extends MeleeWeapon{
    {
        image = ItemSpriteSheet.SKIN_5;

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
        float duration = 20f + level()*4;
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
            return BuffIndicator.COMBO;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }
    }
}