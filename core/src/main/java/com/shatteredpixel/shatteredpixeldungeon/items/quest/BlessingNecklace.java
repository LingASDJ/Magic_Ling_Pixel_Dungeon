package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class BlessingNecklace extends Ankh {

    {
        image = ItemSpriteSheet.CRYSTAL_QUESTION;
        blessed = true;
        cursedKnown = levelKnown = true;
        bones = false;
    }

    public String desc() {
        return Messages.get(BlessingNecklace.class, "desc");
    }

    @Override
    public boolean isUpgradable(){
        return false;
    }
}

