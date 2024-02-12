package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MoonDao extends MeleeWeapon {
    public MoonDao() {
        image = ItemSpriteSheet.MOONDAILY;
        tier = 3;
        ACC = 1.24F;
        DLY = 0.5F;
    }

    public int proc(Char attacker, Char defender, int damage ) {
        int dmg;
        dmg = (new Blocking()).proc(this, attacker, defender, damage);
        damage = dmg;
        return super.proc(attacker, defender, damage);
    }

    public int min(int level) {
        return (this.tier + 1) + (this.tier + 1) * level;
    }

    @Override
    public int iceCoinValue() {
        return 275;
    }

    public int max(int level) {
        return (this.tier + 1) * 2 + (this.tier + 1) * level;
    }
}
