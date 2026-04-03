package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.ArchettoWeightLessPlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ArchettoSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;

public class ArchettoWeightLess extends FiveYearsNPC {

    {
        spriteClass = ArchettoSprite.class;
        plot1 = new ArchettoWeightLessPlot();
        properties.add(Property.UNKNOWN);
    }


    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, c.pos);

        if(first) {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1, false)));
            first = false;
        }

        return true;
    }
}
