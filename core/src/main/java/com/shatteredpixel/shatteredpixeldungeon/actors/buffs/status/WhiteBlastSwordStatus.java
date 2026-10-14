package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WhiteBlastSword;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class WhiteBlastSwordStatus extends Buff {
    {
        type = buffType.POSITIVE;
        skills = true;
    }

    @Override
    public void detach() {
        super.detach();

        if (!(target instanceof Hero)) return;
        Hero hero = (Hero) target;
        if (!hero.isAlive()) return;

        WhiteBlastSword sword = findSword(hero);
        if (sword == null || sword.SkillCharge < 30) return;

        sword.SkillCharge = 0;
        sword.judgement(hero);
    }

    private static WhiteBlastSword findSword(Hero hero) {
        if (hero.belongings.weapon() instanceof WhiteBlastSword)
            return (WhiteBlastSword) hero.belongings.weapon();
        return null;
    }

    @Override
    public int icon() {
        return BuffIndicator.TIME;
    }
}