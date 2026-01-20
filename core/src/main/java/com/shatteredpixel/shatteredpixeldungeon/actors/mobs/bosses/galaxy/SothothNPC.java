package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GodNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.BossYogPlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SothothNPCSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class SothothNPC extends GodNPC {

    {
        spriteClass = SothothNPCSprite.class;
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

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        if(first){
            BossYogPlot plot = new BossYogPlot();
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first = false;
        }
        return true;
    }
}
