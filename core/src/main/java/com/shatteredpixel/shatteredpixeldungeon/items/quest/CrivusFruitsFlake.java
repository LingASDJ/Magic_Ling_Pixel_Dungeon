package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class CrivusFruitsFlake extends Item {

    {
        image = ItemSpriteSheet.CrivusFruitflake;
        stackable = true;
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
    public int value() {
        return quantity * 90;
    }

    @Override
    public int energyVal() {
        return quantity * 3;
    }
}
