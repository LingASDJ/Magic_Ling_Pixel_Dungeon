package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MerchantSword extends MeleeWeapon{
    {
        image = ItemSpriteSheet.MACE;


        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 2;

    }
    @Override
    public int max(int lvl) {
        return  15 + lvl*3;
    }
    @Override
    public int min(int lvl) {
        return  2 + lvl;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc" , 20 + (buffedLvl() * 5));
    }
}
