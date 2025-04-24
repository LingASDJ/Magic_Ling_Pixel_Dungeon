package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class LanFireGo extends Item {

    {
        image = ItemSpriteSheet.EMPTY;
        stackable = true;
        unique = true;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public String desc() {
        return "";
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
