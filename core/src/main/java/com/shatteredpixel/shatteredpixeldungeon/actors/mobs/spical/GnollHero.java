package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TribesmanSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GnollHero extends NTNPC {

    {
        spriteClass = TribesmanSprite.class;
        HP = HT = 120;
        properties.add(Property.NPC);
        baseSpeed = 2f;
        chat = new ArrayList<String>() {
            {
                add(Messages.get(GnollHero.class, "chat_1"));
                add(Messages.get(GnollHero.class, "chat_2"));
                add(Messages.get(GnollHero.class, "chat_3"));
            }
        };
    }



    @Override
    public float attackDelay() {
        return 0.5f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 20, 40 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 30;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(10, 16);
    }

}
