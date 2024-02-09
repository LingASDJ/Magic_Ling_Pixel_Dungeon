package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KongFuSprites;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class KongFu extends NTNPC {

    protected ArrayList<String> chat;
    protected ArrayList<String> B_chat;

    protected ArrayList<String> C_chat;

    {
        spriteClass = KongFuSprites.class;
        chat = new ArrayList<String>() {
            {
                add((Messages.get(KongFu.class, "a_message1")));
            }
        };

        B_chat = new ArrayList<String>() {
            {
                add((Messages.get(KongFu.class, "b_message1")));
            }
        };

        C_chat = new ArrayList<String>() {
            {
                add((Messages.get(KongFu.class, "c_message1")));
            }
        };
    }

    private boolean first=true;


    private static final String FIRST = "first";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);

        if(first){
            WndQuest.chating(this,chat);
            Dungeon.level.drop( new MeatPie(), hero.pos );
            first=false;
        } else if(Statistics.amuletObtained) {
            WndQuest.chating(this,B_chat);
        } else {
            WndQuest.chating(this,C_chat);
        }

        return true;
    }

}
