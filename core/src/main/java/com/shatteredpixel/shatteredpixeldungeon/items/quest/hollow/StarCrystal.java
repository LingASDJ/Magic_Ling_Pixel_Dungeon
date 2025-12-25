package com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class StarCrystal extends Item {

    {
        image = ItemSpriteSheet.STAR_CRYSTAL;
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


    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(Window.DeepPK_COLOR, 8f);
    }

    @Override
    public int value() {
        return 32;
    }

}

