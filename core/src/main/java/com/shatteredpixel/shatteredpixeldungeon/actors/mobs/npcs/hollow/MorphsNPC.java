package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GodNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.MorphsEndTheaterPlot;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.MorphsGodEndTheaterPlot;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.MorphsNPCPlot;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.minigame.MorphsAllEndPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.LingBag;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MorpheusSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class MorphsNPC extends GodNPC {

    {
        spriteClass = MorpheusSprite.class;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.UNKNOWN);
        maxLvl = -1;
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

        sprite.turnTo(pos, Dungeon.hero.pos);


        if(Dungeon.depth == 31 && Statistics.deepestFloor == 31){
            LingBag lingBag = Dungeon.hero.belongings.getItem(LingBag.class);
            if(lingBag != null){
                MorphsNPCPlot plot = new MorphsNPCPlot();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(plot,false));
                    }
                });
            } else {
                yell(Messages.get(this, "lingbag"));
            }
            return true;
        } else if(Dungeon.depth == 32 && first) {
            Plot plot;
            if(Statistics.miniGamesTotalLevel >= 16) {
                plot = new MorphsAllEndPlot.MorphsPacManPeactPlot();
            } else if(Statistics.miniGamesTotalLevel >= 13 ){
                plot = new MorphsAllEndPlot.MorphsPacManVeryGoodlPlot();
            } else if(Statistics.miniGamesTotalLevel >= 10 ) {
                plot = new MorphsAllEndPlot.MorphsPacManGoodPlot();
            } else if(Statistics.miniGamesTotalLevel >= 7) {
                plot = new MorphsAllEndPlot.MorphsPacManEndPlot();
            } else if(Statistics.miniGamesTotalLevel >= 4) {
                plot = new MorphsAllEndPlot.MorphsPacManNormalPlot();
            } else {
                plot = new MorphsAllEndPlot();
            }
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first = false;
            return true;
        } else {

            if(Statistics.miniGamesTotalLevel > 13  ){
                ((MorpheusSprite) sprite).SelectActivate();
                MorphsGodEndTheaterPlot plot = new MorphsGodEndTheaterPlot();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(plot,false));
                    }
                });
            } else {
                MorphsEndTheaterPlot plot = new MorphsEndTheaterPlot();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(plot,false));
                    }
                });
            }
            return true;
        }
    }

}
