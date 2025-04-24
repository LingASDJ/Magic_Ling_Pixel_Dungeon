package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GodNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.MorphsNPCPlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MorpheusSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class MorphsNPC extends GodNPC {

    {
        spriteClass = MorpheusSprite.class;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.UNKNOWN);
        maxLvl = -1;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, Dungeon.hero.pos);
        MorphsNPCPlot plot = new MorphsNPCPlot();
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndDialog(plot,false));
            }
        });
        return true;
    }

}
