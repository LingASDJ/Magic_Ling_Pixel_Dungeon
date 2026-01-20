package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BzmdrSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BzmdrDungeon extends NTNPC {
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
                add((Messages.get(BzmdrLand.class, "x_message1")));
                add((Messages.get(BzmdrLand.class, "x_message2")));
                add((Messages.get(BzmdrLand.class, "x_message3")));
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
        if(first) {
            WndQuest.chating(this, chat);
            switch (Random.NormalIntRange(1,3)){
                case 1:
                    Item item = (Generator.randomUsingDefaults(Generator.Category.SCROLL));
                    Dungeon.level.drop(item, hero.pos);
                    break;
                case 2:
                    Item item2 = (Generator.randomUsingDefaults(Generator.Category.POTION));
                    Dungeon.level.drop(item2, hero.pos);
                    break;
                case 3:
                    Item item3 = (Generator.randomUsingDefaults(Generator.Category.MIS_T4));
                    Dungeon.level.drop(item3, hero.pos);
                    break;
            }
            first = false;
        } else {
            yell("……");
        }
        return true;
    }

}

