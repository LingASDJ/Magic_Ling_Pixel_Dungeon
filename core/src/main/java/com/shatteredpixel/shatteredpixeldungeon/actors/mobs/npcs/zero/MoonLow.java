package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.MoonLowJuicePlot;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.MoonLowOldRoomPlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MoonLowSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Random;

public class MoonLow extends FiveYearsNPC {

    {
        spriteClass = MoonLowSprite.class;
        plot1 = new MoonLowOldRoomPlot();
        plot2 = new MoonLowJuicePlot();
    }
    private static final String[] TXT_RANDOM = {Messages.get(MoonLow.class,"world1"),Messages.get(MoonLow.class,"world2")};
    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(Statistics.moonlowgetAloneRoom){
            if(first){
                Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
                first = false;
            } else {
                yell(Messages.get(this,"drink"));
            }
        } else {
            if(first){
                Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
                first = false;
            } else {
                yell(TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
            }
        }
        return true;
    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }

    private String def_verb(){
        if (Random.Int(2) == 1) {
            return Messages.get(MoonLow.class, "dx_message3");
        }
        return Messages.get(MoonLow.class, "dx_message1");

    }


}
