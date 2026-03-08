package com.shatteredpixel.shatteredpixeldungeon.items.props;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class KnightStabbingSword extends Prop{

    {
        rareness = 1;
        image = ItemSpriteSheet.KINGHTSTABBINGSWORD;
    }

    public static class NoRoundTracker extends FlavourBuff {};

}
