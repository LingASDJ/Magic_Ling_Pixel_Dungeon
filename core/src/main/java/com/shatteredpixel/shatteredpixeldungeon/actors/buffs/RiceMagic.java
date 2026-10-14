package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class RiceMagic extends FlavourBuff
{
    public static float duration( Char ch ) {
        return DURATION;
    }
    // 重要注释：一般认为该buff施加的回合数等同于造成该buff的伤害的数值
    public static final float DURATION	= 10f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public void detach() {
        super.detach();
        if (target.paralysed > 0)
            target.paralysed--;
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            if (! (target instanceof Hero))
            {
                if (!target.isAlive())
                {
                    if (Char.hasProp(target, Char.Property.BOSS))  Dungeon.level.drop( new MeatPie(), target.pos ).sprite.drop();
                    else Dungeon.level.drop( Generator.random( Generator.Category.FOOD ), target.pos ).sprite.drop();
                }
            }
            return true;
        }
        else  {
            return false;
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.FROST;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0f, 0.75f, 1f);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }
}
