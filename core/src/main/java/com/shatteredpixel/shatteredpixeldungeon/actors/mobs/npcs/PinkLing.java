package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.util.ArrayList;

public class PinkLing extends NTNPC{
    {
        spriteClass = PinkLingSprite.class;
        properties.add(Property.IMMOVABLE);
        chat = new ArrayList<String>() {
            {
                add(Messages.get(PinkLing.class, "not-yet"));
            }
        };
    }


}
