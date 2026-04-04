package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.SmallBluePlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DragonGirlBlueSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;

public class SmallBlue extends FiveYearsNPC {

    {
        spriteClass = DragonGirlBlueSprite.class;
        plot1 = new SmallBluePlot();
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

}
