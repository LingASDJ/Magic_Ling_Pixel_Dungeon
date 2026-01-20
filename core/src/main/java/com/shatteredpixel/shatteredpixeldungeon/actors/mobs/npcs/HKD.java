package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HollowKnightSprite;
import com.watabou.utils.Bundle;

public class HKD extends NTNPC {

    private int id;
    private static final String ID = "died";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ID, id);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        id = bundle.getInt(ID);
    }

    {
        spriteClass = HollowKnightSprite.class;
    }

    @Override
    public boolean interact(Char c) {
        id++;
        sprite.turnTo(pos, hero.pos);
        switch (id){
            case 1:
                yell(Messages.get(this,"id_1"));
            break;
            case 2:
                yell(Messages.get(this,"id_2"));
                Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.SCROLL ) ), hero.pos );
            break;
            default:
            case 3:
                yell(Messages.get(this,"id_3"));
                destroy();
                sprite.die();
             break;
        }
        return true;
    }

}
