package com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;

abstract public class AllSearchIQuest extends Item {

    {
        stackable = false;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}
