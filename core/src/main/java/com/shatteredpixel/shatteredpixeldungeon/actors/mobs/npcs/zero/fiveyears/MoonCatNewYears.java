package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoonCat;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.FiveYearsPlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MoonCatSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Random;

public class MoonCatNewYears extends FiveYearsNPC {

    {
        spriteClass = MoonCatSprite.class;
        plot1 = new FiveYearsPlot.MoonCatFiveYearsPlot();
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

    @Override
    public String defenseVerb() {
        return def_verb();
    }
    private String def_verb(){
        ((MoonCatSprite)sprite).What_Up();
        GameScene.scene.add(new Delayer(1f){
            @Override
            protected void onComplete() {
                sprite.idle();
            }
        });
        if(Random.Int(100)>=50){
            return Messages.get(MoonCat.class, "def_verb_1");
        } else {
            return Messages.get(MoonCat.class, "def_verb_2");
        }
    }
}
