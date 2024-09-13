package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GodNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.TrueYogPlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YetYogSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;

public class TrueYog extends GodNPC {
    {
        spriteClass = YetYogSprite.class;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.UNKNOWN);
        maxLvl = -1;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        TrueYogPlot plot = new TrueYogPlot();
        Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot,false)));
        return true;
    }

}
