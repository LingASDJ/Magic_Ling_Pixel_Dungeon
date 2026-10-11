package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Marigold extends Plant {

    {
        image = 18;
        seedClass = Marigold.Seed.class;
    }

    @Override
    public void activate(Char ch) {

    }

    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_MARIGOLD;

            plantClass = Marigold.class;
        }
    }

}
