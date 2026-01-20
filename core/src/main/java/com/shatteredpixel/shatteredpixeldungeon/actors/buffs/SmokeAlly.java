package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class SmokeAlly extends Buff{

    public Char.Alignment align;
    public Char ch;

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)){
            align = target.alignment;
            ch = target;
            if(target.alignment == Char.Alignment.ENEMY ){
                target.alignment = Char.Alignment.NEUTRAL;
                if(target instanceof Mob && ((Mob)target).enemy() instanceof Hero){
                    ((Mob) target).enemyReset();
                }
            }
            if (target.buff(PinCushion.class) != null){
                target.buff(PinCushion.class).detach();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach(){
        ch.alignment = align;
        super.detach();
    }

    public int icon() {
        return BuffIndicator.HEART;
    }

}
