package com.shatteredpixel.shatteredpixeldungeon.items.armor;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class LamellarArmor extends Armor {

    {
        image = ItemSpriteSheet.ARMOR_LAMELLAR;
    }

    public LamellarArmor() {
        super( 6 );
    }

    @Override
    public int STRReq(int lvl){
        return STRReq(5, lvl);
    }


}