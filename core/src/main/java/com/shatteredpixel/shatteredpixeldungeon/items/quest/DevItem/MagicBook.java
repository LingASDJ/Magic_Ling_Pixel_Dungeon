package com.shatteredpixel.shatteredpixeldungeon.items.quest.DevItem;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MagicBook extends Item {

    {
        image = ItemSpriteSheet.BOOKSQINYUE;
        cursed = false;
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
