package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.FiveYearsTwoPlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZakoSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;

public class ZakoFlowerNewYears extends FiveYearsNPC {

    {
        spriteClass = ZakoSprite.class;
        plot1 = new FiveYearsTwoPlot.FlowerFiveYearsPlot();
        plot2 = new FiveYearsTwoPlot.FlowerFiveYearsEndPlot();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
            first = false;
        } else {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
        }
        return true;
    }
}
