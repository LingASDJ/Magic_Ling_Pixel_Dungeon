package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ClearHStal extends Item {

    {
        image = ItemSpriteSheet.HITLCRYSTAL;

        stackable = true;
        unique = true;
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

