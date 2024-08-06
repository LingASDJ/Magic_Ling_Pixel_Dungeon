package com.shatteredpixel.shatteredpixeldungeon.items.dlcitem;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;

import java.util.ArrayList;

public abstract class DLCItem extends Item {
    public static final String AC_READ	= "READ";
    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(AC_DROP);
        actions.remove(AC_THROW);
        actions.add(AC_READ);
        return actions;
    }

}
