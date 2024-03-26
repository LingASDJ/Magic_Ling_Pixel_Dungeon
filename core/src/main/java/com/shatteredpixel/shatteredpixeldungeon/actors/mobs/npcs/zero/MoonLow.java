package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.MoonLowPlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MoonLowSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class MoonLow extends NTNPC {
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
        spriteClass = MoonLowSprite.class;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        MoonLowPlot plot = new MoonLowPlot();
        MoonLowPlot.MoonLowPlotGO plot2 = new MoonLowPlot.MoonLowPlotGO();
        if (first) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            //Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.FOOD ) ), hero.pos );
            first=false;
        } else if(Challenges.activeChallenges()>0) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot2,false));
                }
            });
        }
        return true;
    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }
    private String def_verb(){

        switch (Random.Int(2)){
            case 0:default:
                return Messages.get(MoonLow.class, "dx_message1");
//            case 1:
//                return Messages.get(MoonLow.class, "dx_message2");
            case 1:
                return Messages.get(MoonLow.class, "dx_message3");
        }

    }


}
