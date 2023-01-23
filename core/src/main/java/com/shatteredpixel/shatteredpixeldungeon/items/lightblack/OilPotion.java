package com.shatteredpixel.shatteredpixeldungeon.items.lightblack;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class OilPotion extends Item {
    public static final String AC_REFILL = "REFILL";

    public OilPotion() {
        image = ItemSpriteSheet.SKPOTION;
        stackable = true;
    }

    public boolean isIdentified() {
        return true;
    }

    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = OilPotion.super.actions(hero);
        actions.add(AC_REFILL);
        return actions;
    }

    public void execute(Hero hero, String action) {
        OilPotion.super.execute(hero, action);
        if (action.equals(AC_REFILL)) {
            OilLantern lantern = Dungeon.hero.belongings.getItem(OilLantern.class);
            Refill(lantern);
        }
    }

    public void Refill(OilLantern lantern) {
        lantern.flasks++;
        detach(Dungeon.hero.belongings.backpack);
    }

//    public int proc(Char attacker, Char defender, int damage) {
//        Buff.prolong(defender, Slow.class, 10.0f);
//        return OilPotion.super.proc(attacker, defender, damage);
//    }

    @Override
    public int value() {
        return quantity * 20;
    }
}
