package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.zeroItemLevel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.XiaYuanSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class XiaYuan extends NTNPC {


    protected ArrayList<String> chat;
    protected ArrayList<String> B_chat;
    protected ArrayList<String> C_chat;

    {
        spriteClass = XiaYuanSprite.class;
        chat = new ArrayList<String>() {
            {
                add((Messages.get(XiaYuan.class, "a_message1")));
                add((Messages.get(XiaYuan.class, "a_message2")));
                add((Messages.get(XiaYuan.class, "a_message3")));
            }
        };

        B_chat = new ArrayList<String>() {
            {
                add((Messages.get(XiaYuan.class, "b_message1")));
                add((Messages.get(XiaYuan.class, "b_message2")));
            }
        };

        C_chat = new ArrayList<String>() {
            {
                add((Messages.get(XiaYuan.class, "c_message1")));
            }
        };

        properties.add(Property.IMMOVABLE);
    }

    private boolean first=true;
    private boolean secnod=true;
    private boolean rd=true;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        if (first) {
            WndQuest.chating(this, chat);
            zeroItemLevel++;
            if (Statistics.zeroItemLevel >= 4 && Dungeon.depth == 0) {
                Dungeon.level.drop(new Gold(1), hero.pos);
            } else {
                Dungeon.level.drop(new RandomChest(), hero.pos);
            }
            first = false;
        } else if (Statistics.amuletObtained) {
            WndQuest.chating(this, C_chat);
        } else {
            WndQuest.chating(this, B_chat);
            secnod = false;
        }
        return true;
    }

}

