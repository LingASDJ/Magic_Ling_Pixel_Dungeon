package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class ImmortalShieldAffecter extends TestItem {
    {
        image = ItemSpriteSheet.ROUND_SHIELD;
        defaultAction = AC_SWITCH;
    }
    private static final String AC_SWITCH = "switch";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SWITCH);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action ) {
        super.execute(hero, action);
        if(action.equals(AC_SWITCH)){
            if(isImmortal(hero)){
                Buff.detach(hero, ImmortalShield.class);
            }else{
                Buff.affect(hero, ImmortalShield.class);
            }
        }
    }

    private boolean isImmortal(Char target){
        return target.buff(ImmortalShield.class)!=null;
    }

    public static class ImmortalShield extends Buff{
        {
            type = buffType.NEUTRAL;
            announced = false;
            revivePersists = true;
        }

        @Override
        public boolean act(){
            spend(TICK);
            return true;
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add(CharSprite.State.SHIELDED);
            else target.sprite.remove(CharSprite.State.SHIELDED);
        }
    }
}
