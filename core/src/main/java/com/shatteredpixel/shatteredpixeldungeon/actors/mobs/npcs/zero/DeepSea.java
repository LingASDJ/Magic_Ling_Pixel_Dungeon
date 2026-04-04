package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.DeepSeaPlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeepSeaSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Random;

public class DeepSea extends FiveYearsNPC {

    {
        spriteClass = DeepSeaSprite.class;
        plot1 = new DeepSeaPlot();
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);

        if(first) {
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

        switch (Random.Int(4)){
            case 1:
                return Messages.get(DeepSea.class, "dx_message2");
            case 2:
                return Messages.get(DeepSea.class, "dx_message3");
            case 3:
                return Messages.get(DeepSea.class, "dx_message4");
            default:
                return Messages.get(DeepSea.class, "dx_message1");
        }

    }


}
