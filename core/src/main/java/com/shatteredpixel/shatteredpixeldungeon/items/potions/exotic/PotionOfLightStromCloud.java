package com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PotionOfLightStromCloud extends ExoticPotion {

    {
        icon = ItemSpriteSheet.Icons.POTION_SRTDIED;
    }

    @Override
    public void apply(Hero hero) {
        identify();

    }

}
