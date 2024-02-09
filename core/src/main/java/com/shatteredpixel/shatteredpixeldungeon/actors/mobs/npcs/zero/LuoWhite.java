package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.LuoWhitePlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LuoWhiteSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class LuoWhite extends NTNPC {


    {
        spriteClass = LuoWhiteSprite.class;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        LuoWhitePlot plot = new LuoWhitePlot();
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndDialog(plot,false));
            }
        });
        return true;
    }


}
