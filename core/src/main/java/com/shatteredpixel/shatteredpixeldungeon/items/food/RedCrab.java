package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RedCrab extends Food {

    {
        image = ItemSpriteSheet.REDCRAB;
        energy = Hunger.HUNGRY;
    }

    @Override
    public int value() {
        return 90 * quantity;
    }

}
