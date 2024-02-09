package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.MOREROOM;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeepSeaSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeepSea extends NTNPC {

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
        spriteClass = DeepSeaSprite.class;

        chat = new ArrayList<String>() {
            {
                add((Messages.get(DeepSea.class, "a_message1")));
            }
        };

        B_chat = new ArrayList<String>() {
            {
                add((Messages.get(DeepSea.class, "b_message1")));
            }
        };

        C_chat = new ArrayList<String>() {
            {
                if(Challenges.activeChallenges() == 16){
                    add((Messages.get(DeepSea.class, "c_message3")));
                } else if(Challenges.activeChallenges() == 13){
                    add((Messages.get(DeepSea.class, "c_message2")));
                } else {
                    add((Messages.get(DeepSea.class, "c_message1")));
                }

                if(Dungeon.isChallenged(MOREROOM)){
                    add((Messages.get(DeepSea.class, "d_message1")));
                }
                
                if(Statistics.lanterfireactive){
                    add((Messages.get(DeepSea.class, "e_message1")));
                } else {
                    add((Messages.get(DeepSea.class, "e_message2")));
                }

                if(Dungeon.isChallenged(CS)){
                    add((Messages.get(DeepSea.class, "f_message1")));
                }
            }
        };

        D_chat = new ArrayList<String>() {
            {
               switch (Random.Int(3)){
                   case 0:default:
                       add((Messages.get(DeepSea.class, "g_message1")));
                       break;
                   case 1:
                       add((Messages.get(DeepSea.class, "g_message2")));
                       break;
                   case 2:
                       add((Messages.get(DeepSea.class, "g_message3")));
                       break;
               }
            }
        };

    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);

        if(first){
            WndQuest.chating(this,chat);
            first=false;
        } else if(secnod) {
            WndQuest.chating(this,B_chat);
            secnod = false;
        } else if(!Statistics.amuletObtained) {
            WndQuest.chating(this,C_chat);
        } else {
            WndQuest.chating(this,D_chat);
        }

        return true;
    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }
    private String def_verb(){

        switch (Random.Int(4)){
            case 0:default:
                return Messages.get(DeepSea.class, "dx_message1");
            case 1:
                return Messages.get(DeepSea.class, "dx_message2");
            case 2:
                return Messages.get(DeepSea.class, "dx_message3");
            case 3:
                return Messages.get(DeepSea.class, "dx_message4");
        }

    }


}
