package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

//末日节杖
//五阶，力量需求18
//初始4-25，成长1-5，精准1.5
//装备时，你可以主动降低此武器的1级真实等级，这视为你使用了一张灵爆秘卷。
//爆发性的能量蕴藏在这柄木杖中，似乎可以如阅读卷轴一般的使用它。
public class DoomsdayScepter extends MeleeWeapon{

    // 灵爆：降低 1 级真实等级，视为读取一张灵爆秘卷
    public static final String AC_BURST = "BURST";

    {
        image = ItemSpriteSheet.SKIN_5;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        ACC = 1.5f;

        tier = 5;
    }

    @Override
    public int max(int lvl) { return 25 + lvl * 5; }

    @Override
    public int min(int lvl) { return 4 + lvl; }

    // 装备且真实等级 ≥1 时，显示“灵爆”动作
    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero) && trueLevel() >= 1) {
            actions.add(AC_BURST);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_BURST)) {
            if (!isEquipped(hero)) {
                GLog.w(Messages.get(this, "need_equip"));
                return;
            }
            if (trueLevel() < 1) {
                GLog.w(Messages.get(this, "no_level"));
                return;
            }
            // 视为读取一张灵爆秘卷：走读卷轴完整流程（限制、效果、读卷轴联动），真正读出才扣等级
            ScrollOfPsionicBlast scroll = new ScrollOfPsionicBlast();
            scroll.curUser = hero;
            if (scroll.tryRead(hero)) {
                degrade();
                updateQuickslot();
            }
        }
    }
}
