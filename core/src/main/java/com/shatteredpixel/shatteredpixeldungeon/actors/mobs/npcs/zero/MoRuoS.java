package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.MoRuosPlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MoRuoSSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class MoRuoS extends NTNPC {

    {
        spriteClass = MoRuoSSprite.class;
        properties.add(Property.IMMOVABLE);
    }

    private boolean first=true;

    private static final String FIRST = "first";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }
    private static String[] TXT_RANDOM = {
            Messages.get(MoRuoS.class,"roll1"),
            Messages.get(MoRuoS.class,"roll2"),
            Messages.get(MoRuoS.class,"roll3"),
            Messages.get(MoRuoS.class,"roll4"),
            Messages.get(MoRuoS.class,"roll5",Dungeon.hero.name()),
            Messages.get(MoRuoS.class,"roll6"),
            Messages.get(MoRuoS.class,"roll7"),
    };

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, Dungeon.hero.pos);

        MoRuosPlot plot = new MoRuosPlot();
        if(first){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first=false;
        }else {
            yell(TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
        }

        return true;
    }

}
