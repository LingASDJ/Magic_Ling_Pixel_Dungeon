package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.zeroItemLevel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QinWolfSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class QinYueWolf extends NTNPC {


    protected ArrayList<String> chat;
    protected ArrayList<String> B_chat;
    protected ArrayList<String> C_chat;
    protected ArrayList<String> D_chat;
    protected ArrayList<String> E_chat;

    {
        spriteClass = QinWolfSprite.class;
        chat = new ArrayList<String>() {
            {
                add((Messages.get(QinYueWolf.class, "a_message1")));
                add((Messages.get(QinYueWolf.class, "a_message2")));
                add((Messages.get(QinYueWolf.class, "a_message3")));
            }
        };

        B_chat = new ArrayList<String>() {
            {
                add((Messages.get(QinYueWolf.class, "b_message1")));
                add((Messages.get(QinYueWolf.class, "b_message2")));
                add((Messages.get(QinYueWolf.class, "b_message3")));
            }
        };

        C_chat = new ArrayList<String>() {
            {
                add((Messages.get(QinYueWolf.class, "c_message1")));
            }
        };

        D_chat = new ArrayList<String>() {
            {
                add((Messages.get(QinYueWolf.class, "d_message1")));
                add((Messages.get(QinYueWolf.class, "d_message2")));
            }
        };

        E_chat = new ArrayList<String>() {
            {
                add((Messages.get(QinYueWolf.class, "e_message1")));
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

        if(first){
            WndQuest.chating(this,chat);
            first=false;
        } else if(secnod) {
            WndQuest.chating(this,B_chat);

            if(Statistics.zeroItemLevel >=4 && Dungeon.depth == 0) {
                Dungeon.level.drop(new Gold(1), hero.pos);
            } else {
                Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.POTION ) ), hero.pos );
            }


            zeroItemLevel++;
            secnod = false;
        } else if(!Statistics.amuletObtained) {
            WndQuest.chating(this,C_chat);
        } else if(rd) {
            WndQuest.chating(this,D_chat);
            rd = false;
        } else {
            WndQuest.chating(this,E_chat);
        }

        return true;
    }

}
