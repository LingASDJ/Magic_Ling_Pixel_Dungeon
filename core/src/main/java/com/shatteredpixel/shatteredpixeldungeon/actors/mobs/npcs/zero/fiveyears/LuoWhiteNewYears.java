package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.FiveYearsPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LuoWhiteNewYearsSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndLuoWhite;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class LuoWhiteNewYears extends FiveYearsNPC {

        {
            spriteClass = LuoWhiteNewYearsSprite.class;
            plot1 = new FiveYearsPlot.LostWhiteFiverYearsPlot();
        }

        @Override
        public boolean interact(Char c) {
            sprite.turnTo( pos, c.pos );
            if(first){
                Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
                first = false;
            } else {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndLuoWhite());
                    }
                });
            }
            return true;
        }

    public static class LFRoad extends Torch {
        {
            image = ItemSpriteSheet.LAN_FIRE_ROAD;
        }
    }

}
