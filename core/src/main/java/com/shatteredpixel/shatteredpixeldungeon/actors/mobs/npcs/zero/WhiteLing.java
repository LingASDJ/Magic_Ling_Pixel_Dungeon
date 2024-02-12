package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.LingPlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhiteGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WhiteLing extends NTNPC {

    {
        spriteClass = WhiteGirlSprites.class;
        maxLvl = -1;
        properties.add(Property.UNKNOWN);
        baseSpeed = 0;
        properties.add(Property.IMMOVABLE);

        chat = new ArrayList<String>() {
            {
                add((Messages.get(WhiteLing.class, "c_message1")));
                add((Messages.get(WhiteLing.class, "c_message2")));
                add((Messages.get(WhiteLing.class, "c_message3")));
            }
        };

    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }
    private String def_verb(){
        if(Random.Int(100)>=50){
            return Messages.get(this, "def_verb_1");
        } else {
            return Messages.get(this, "def_verb_2");
        }

    }

    @Override
    public float attackDelay() {
        return 0;
    }

    @Override
    public int damageRoll() {
        return 0;
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
        bundle.put(RD, RD);
    }



    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
    }

    @Override
    protected boolean act() {

        throwItem();

        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );
        return true;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {
        LingPlot plot = new LingPlot();
        LingPlot.LingPlotGood B_plot = new LingPlot.LingPlotGood();
        sprite.turnTo(pos, Dungeon.hero.pos);
        if(first){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first=false;
        } else if(secnod){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(B_plot,false));
                }
            });
            secnod=false;
        } else if(rd) {
            ((WhiteGirlSprites)sprite).go();
            WndQuest.chating(this,chat);
            GameScene.scene.add(new Delayer(2f){
                @Override
                protected void onComplete() {
                    ((WhiteGirlSprites)sprite).sleep();
                }
            });
            rd = false;
        } else {
            ((WhiteGirlSprites)sprite).sleep();
            WndQuest.chating(this,chat);
        }

        return true;
    }
}
