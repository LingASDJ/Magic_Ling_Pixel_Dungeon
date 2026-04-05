package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.PianoLePlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PianoLeSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class PianoLe extends FiveYearsNPC {

    {
        spriteClass = PianoLeSprite.class;
        plot1 = new PianoLePlot();
    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }

    private String def_verb(){
        if(Statistics.amuletObtained){
            return Messages.get(this, "def_verb_1");
        } else {
            return Messages.get(this, "def_verb_2");
        }
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, Dungeon.hero.pos);
        if(first){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                   GameScene.show(new WndDialog(plot1,false));
                }
            });
            first=false;
        }
        return true;
    }

}
