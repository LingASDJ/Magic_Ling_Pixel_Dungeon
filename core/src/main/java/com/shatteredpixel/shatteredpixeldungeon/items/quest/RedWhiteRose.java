package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class RedWhiteRose extends Item {

    {
        image = ItemSpriteSheet.REDWHITEROSE;
        cursed = false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(Window.GDX_COLOR, 3f);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public int level() {
        return 99;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

}

