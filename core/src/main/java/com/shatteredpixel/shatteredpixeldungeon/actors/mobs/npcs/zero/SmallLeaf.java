package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.SmallLeafPlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SmallLeafSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Random;

public class SmallLeaf extends FiveYearsNPC {

    {
        spriteClass = SmallLeafSprite.class;
        properties.add(Property.UNKNOWN);
        plot1 = new SmallLeafPlot();
        plot2 = new SmallLeafPlot.Second();
        plot3 = new SmallLeafPlot.EndLess();
    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }

    private String def_verb(){
        if(Random.Int(100)>=50){
            return Messages.get(this, "def_verb_1");
        } else {
            return Messages.get(this, "def_verb_2");
        }
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, Dungeon.hero.pos);
        if(first){
            if(!SPDSettings.SmallLeafGetCoin() && !Dungeon.isDLC(Conducts.Conduct.DEV)){
                Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
            } else if(Challenges.activeChallenges() > 10 && !SPDSettings.SmallLeafHCGetCoin() && !Dungeon.isDLC(Conducts.Conduct.DEV))  {
                Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
            } else {
                Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot3,false)));
            }
            first = false;
        }
        return true;
    }

}
