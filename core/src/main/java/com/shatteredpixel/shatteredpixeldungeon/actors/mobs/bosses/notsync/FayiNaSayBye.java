package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.FiveYearsFourPlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ClearElemtGuardGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;

public class FayiNaSayBye extends FiveYearsNPC {

    {
        spriteClass = ClearElemtGuardGirlSprites.class;
        plot1 = new FiveYearsFourPlot.FayinaSayByePlotOne();
        plot2 = new FiveYearsFourPlot.FayinaSayByePlotTwo();
        plot3 = new FiveYearsFourPlot.FayinaSayByeEndPlot();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
            first = false;
        } else if(secnod) {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
            secnod = false;
        } else {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot3,false)));
        }
        return true;
    }



}
