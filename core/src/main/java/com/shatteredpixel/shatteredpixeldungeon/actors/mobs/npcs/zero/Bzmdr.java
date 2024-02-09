package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BzmdrSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Bzmdr extends NTNPC {

    protected ArrayList<String> chat;
    protected ArrayList<String> B_chat;
    protected ArrayList<String> C_chat;
    protected ArrayList<String> D_chat;
    protected ArrayList<String> E_chat;

    private int died;

    private boolean first=true;
    private boolean secnod=true;
    private boolean rd=true;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";

    private static final String DIED = "died";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
        bundle.put(DIED,died);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
        died = bundle.getInt(DIED);
    }

    {
        spriteClass = BzmdrSprite.class;

        chat = new ArrayList<String>() {
            {
                add((Messages.get(Bzmdr.class, "a_message1")));
            }
        };

        B_chat = new ArrayList<String>() {
            {
                add((Messages.get(Bzmdr.class, "b_message1")));
            }
        };

        C_chat = new ArrayList<String>() {
            {
                if(Statistics.lanterfireactive){
                    add((Messages.get(Bzmdr.class, "c_message2")));
                } else if(Challenges.activeChallenges()==0){
                    add((Messages.get(Bzmdr.class, "c_message1")));
                } else {
                    add((Messages.get(Bzmdr.class, "c_message3")));
                }

            }
        };

        D_chat = new ArrayList<String>() {
            {
                if(Challenges.activeChallenges()>=16){
                    add((Messages.get(Bzmdr.class, "d_message2")));
                } else if(Challenges.activeChallenges()>=13) {
                    add((Messages.get(Bzmdr.class, "d_message1")));
                }
            }
        };

        E_chat = new ArrayList<String>() {
            {
                add((Messages.get(Bzmdr.class, "e_message1")));
                add((Messages.get(Bzmdr.class, "e_message2")));
                add((Messages.get(Bzmdr.class, "e_message3")));
            }
        };

    }

    @Override
    public boolean interact(Char c) {
        if(first){
            WndQuest.chating(this,chat);
            first=false;
        } else if(secnod) {
            WndQuest.chating(this,B_chat);
            if(Random.NormalIntRange(0,100)<=50){
                Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.SEED ) ), hero.pos );
            } else {
                Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.STONE) ), hero.pos );
            }
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

    private String def_verb(){

        if(died<4)died++;

        //3次攻击后 立刻一个即死陷阱
        if(died==3){
            GrimTrap trapx = new GrimTrap();
            trapx.pos = hero.pos;
            trapx.activate();
            died = 0;
        }

        if(Random.Int(100)>=50){
            return Messages.get(this, "def_verb_1");
        } else {
            return Messages.get(this, "def_verb_2");
        }

    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }


}
