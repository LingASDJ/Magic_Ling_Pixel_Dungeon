package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.FiveYearsThreePlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HollowKnightSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;

public class HKNewYears extends FiveYearsNPC {

    {
        spriteClass = HollowKnightSprite.class;
        plot1 = new FiveYearsThreePlot.HKFiveYearsPlot();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
            first = false;
        }
        return true;
    }

}

