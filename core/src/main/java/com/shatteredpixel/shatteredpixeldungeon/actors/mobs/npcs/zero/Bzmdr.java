package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.BzmdrHotelPlot;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BzmdrSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Bzmdr extends NTNPC {
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

    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        BzmdrHotelPlot plot = new BzmdrHotelPlot();
        BzmdrHotelPlot.BzmdrHotelPlotEND plot2 = new  BzmdrHotelPlot.BzmdrHotelPlotEND();
        if (first) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            //Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.FOOD ) ), hero.pos );
            first=false;
        } else if(Statistics.amuletObtained) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot2,false));
                }
            });
        } else {
            yell("……");
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
