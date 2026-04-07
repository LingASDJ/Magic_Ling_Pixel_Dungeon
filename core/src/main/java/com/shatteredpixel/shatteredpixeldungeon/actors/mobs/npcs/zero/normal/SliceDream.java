package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.SliceGirlPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.UnlessFlower;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SliceGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;

public class SliceDream extends FiveYearsNPC {

    {
        spriteClass = SliceGirlSprite.class;
        plot1 = new SliceGirlPlot();
        plot2 = new SliceGirlPlot.TND();
        plot3 = new SliceGirlPlot.END();
        plot4 = new SliceGirlPlot.ENDLOOP();
    }

    @Override
    protected boolean act() {
        UnlessFlower unlessFlower = Dungeon.hero.belongings.getItem(UnlessFlower.class);
        if(Statistics.amuletObtained && unlessFlower == null){
            die(true);
        }
        return super.act();
    }

    @Override
    public boolean interact(Char c) {
        UnlessFlower unlessFlower = Dungeon.hero.belongings.getItem(UnlessFlower.class);
        sprite.turnTo( pos, c.pos );
        if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
            first = false;
        } else if(!Statistics.amuletObtained){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
        } else if(unlessFlower != null && secnod){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot3,false)));
            secnod = false;
        } else {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot4,false)));
        }
        return true;
    }

}
