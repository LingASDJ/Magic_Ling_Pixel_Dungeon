package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ImperialExorcism extends MeleeWeapon {

    {
        tier = 1;
        image = ItemSpriteSheet.IMPRIA_EXORCISM;
    }

    @Override
    public int min(int lvl) {
        return 2 + lvl;
    }
    @Override
    public int max(int lvl) {
        return 7 + lvl * 3;
    }
}
