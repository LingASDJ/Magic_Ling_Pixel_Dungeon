package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MoonDao extends MeleeWeapon {
    public MoonDao() {
        image = ItemSpriteSheet.MOONDAILY;
        tier = 3;
        ACC = 1.24F;
        DLY = 0.5F;
        enchant(new Blocking());
    }


    public int min(int level) {
        return (this.tier + 1) * 3 + (this.tier + 1) * level;
    }

    @Override
    public int iceCoinValue() {
        return 170;
    }

    public int max(int level) {
        return (this.tier + 1) * 10 + (this.tier + 1) * level;
    }
}
