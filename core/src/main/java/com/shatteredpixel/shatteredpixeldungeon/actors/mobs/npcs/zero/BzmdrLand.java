package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.BzmdrLandPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BzmdrSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BzmdrLand extends NTNPC {
    private int died;

    protected ArrayList<String> X_chat;

    private static final String DIED = "died";
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
            return Messages.get(Bzmdr.class, "def_verb_1");
        } else {
            return Messages.get(Bzmdr.class, "def_verb_2");
        }

    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }

    {
        spriteClass = BzmdrSprite.class;

        chat = new ArrayList<String>() {
            {
                add((Messages.get(BzmdrLand.class, "a_message1")));
                add((Messages.get(BzmdrLand.class, "a_message2")));
                add((Messages.get(BzmdrLand.class, "a_message3")));
            }
        };

        X_chat = new ArrayList<String>() {
            {
                add((Messages.get(BzmdrLand.class, "c_message1")));
                add((Messages.get(BzmdrLand.class, "c_message2")));
            }
        };
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

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        BzmdrLandPlot plot = new BzmdrLandPlot();

        if(Dungeon.isChallenged(Challenges.CS)){
            if(Statistics.amuletObtained && secnod){
                WndQuest.chating(this,X_chat);
                secnod = false;
            } else if(first) {
                WndQuest.chating(this,chat);
                Item item = ( Generator.randomUsingDefaults( Generator.Category.STONE ));
                Dungeon.level.drop( item , hero.pos );
                Item item2 = ( Generator.randomUsingDefaults( Generator.Category.STONE ));
                Dungeon.level.drop( item2 , hero.pos );
                first = false;
            } else {
                yell("……");
            }
        } else {
            if (first) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(plot,false));
                    }
                });
                first=false;
            } else {
                yell("……");
            }
        }
        return true;
    }

}
