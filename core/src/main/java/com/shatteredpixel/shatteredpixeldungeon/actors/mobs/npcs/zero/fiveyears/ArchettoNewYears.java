package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.FiveYearsThreePlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ArchettoSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;

public class ArchettoNewYears extends FiveYearsNPC {

    {
        spriteClass = ArchettoSprite.class;
        plot1 = new FiveYearsThreePlot.ArchettoFiverYearsPlot();
        plot2 = new FiveYearsThreePlot.ArchettoBFiverYearsPlot();
        plot3 = new FiveYearsThreePlot.ArchettoCFiverYearsPlot();
        properties.add(Property.UNKNOWN);
    }

    @Override
    public String name(){
        if(!SPDSettings.CatSee()){
            return "???";
        }
        return super.name();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(!SPDSettings.CatSee()){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
        } else if(secnod){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
            secnod = false;
        } else {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot3,false)));
        }
        return true;
    }
}
