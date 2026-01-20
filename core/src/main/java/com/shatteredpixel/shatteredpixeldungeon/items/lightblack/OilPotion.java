package com.shatteredpixel.shatteredpixeldungeon.items.lightblack;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DHXD;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class OilPotion extends Item {
    public static final String AC_REFILL = "REFILL";

    public OilPotion() {
        image = ItemSpriteSheet.SKPOTION;
        stackable = false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = OilPotion.super.actions(hero);
        actions.add(AC_REFILL);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        OilPotion.super.execute(hero, action);
        if (action.equals(AC_REFILL)) {
            OilLantern lantern = Dungeon.hero.belongings.getItem(OilLantern.class);
            if(lantern != null) Refill(lantern);
        }
    }

//    @Override
//    public boolean doPickUp(Hero hero, int pos) {
//        if (super.doPickUp(hero, pos) && Statistics.AutoOilPotion) {
//            OilLantern lantern = Dungeon.hero.belongings.getItem(OilLantern.class);
//            if(lantern != null) {
//                Refill(lantern);
//                GLog.i(Messages.get(OilLantern.class, "lanterreload"));
//            } else {
//                GLog.i(Messages.get(OilLantern.class, "must"));
//            }
//
//        }
//        return true;
//    }


    public void Refill(OilLantern lantern) {
        lantern.flasks++;
        detach(Dungeon.hero.belongings.backpack);
    }

    @Override
    public int value() {
        return Dungeon.isChallenged(DHXD) ? quantity * 40 : quantity * 20;
    }
}
