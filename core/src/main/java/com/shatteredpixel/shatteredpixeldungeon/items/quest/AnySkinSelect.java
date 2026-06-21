package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class AnySkinSelect extends Item {
    {
        image = ItemSpriteSheet.HALLS_PAGE;
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

    public static class AnySkinCustomSelect extends Item {
        {
            image = ItemSpriteSheet.HALLS_PAGE;
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
}
