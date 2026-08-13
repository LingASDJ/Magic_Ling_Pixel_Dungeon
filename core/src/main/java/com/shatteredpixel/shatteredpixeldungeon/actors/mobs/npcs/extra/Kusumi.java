package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KusumiSprites;
import com.watabou.utils.Random;

public class Kusumi extends FiveYearsNPC {

    {
        spriteClass = KusumiSprites.class;
        properties.add(Property.UNKNOWN);
    }

    private static String[] TXT_RANDOM = {
            Messages.get(Kusumi.class,"roll1"),
            Messages.get(Kusumi.class,"roll2"),
            Messages.get(Kusumi.class,"roll3")
    };

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, hero.pos);
        if(first){
            yell(Messages.get(this,"no_talk"));
            first = false;
        } else if(secnod) {
            yell(TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
            secnod = false;
        } else {
            yell("……");
        }
        return true;
    }

    private String def_verb(){
        FloatingText.show(sprite.x+10, sprite.y, pos, Messages.get(this, "cute"), CharSprite.NEGATIVE);
        return "";
    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }

}
