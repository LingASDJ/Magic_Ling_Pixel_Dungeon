package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.BzmdrHotelPlot;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.FiveYearsThreePlot;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BzmdrSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BzmdrNewYears extends FiveYearsNPC {
    private int died;
    {
        spriteClass = BzmdrSprite.class;
        plot1 = new FiveYearsThreePlot.BzmdrNewYearsFiverYearsPlot();
        plot2 = new FiveYearsThreePlot.BzmdrNewYearsFiverYearsGoPlot();
        plot3 = new FiveYearsThreePlot.BzmdrNewYearsFiverYearsSoPlot();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
            first = false;
        } else if(secnod){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(
                                           sprite(),
                                           Messages.titleCase(name()),
                                           Messages.get(BzmdrNewYears.class, "messages2"),
                                           Messages.get(BzmdrNewYears.class, "yes"),
                                           Messages.get(BzmdrNewYears.class,"no")
                                   ) {
                                       @Override
                                       protected void onSelect(int index) {
                                           if (index==0){
                                               Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
                                               secnod = false;
                                           } else {
                                               Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot3,false)));
                                           }
                                       }
                                   }
                    );
                }});
        } else if(Statistics.amuletObtained) {
            BzmdrHotelPlot.BzmdrHotelPlotEND plot4 = new  BzmdrHotelPlot.BzmdrHotelPlotEND();
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot4,false));
                }
            });
        } else {
            yell("……");
        }
        return true;
    }

    public static class BzmdrGift extends Item {

        {
            image = ItemSpriteSheet.BZMDR_GIFT;
            cursed = true;
            cursedKnown = true;
            unique = true;
        }

        @Override
        public ArrayList<String> actions(Hero hero) {
            return new ArrayList<>();
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", Statistics.BzmdrCJHeroSTR,Statistics.BzmdrCJHeroViewDistance);
        }

        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            if (super.doPickUp(hero, pos)){
                GLog.n( Messages.get( this, "magic") );
                CellEmitter.get(hero.pos).burst(ShadowParticle.UP, 25);
                Sample.INSTANCE.play(Assets.Sounds.CURSED);
                return true;
            }
            return false;
        }
    }

    private String def_verb(){

        if(died<4)died++;

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

    private static final String DIED = "died";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DIED,died);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        died = bundle.getInt(DIED);
    }

}
