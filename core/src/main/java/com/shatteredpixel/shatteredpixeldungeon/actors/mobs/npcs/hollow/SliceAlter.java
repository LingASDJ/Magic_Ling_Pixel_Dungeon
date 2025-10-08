package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.SliceAlterPlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SliceAlterSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

public class SliceAlter extends NTNPC {

    {
        spriteClass = SliceAlterSprite.class;
        properties.add(Property.UNKNOWN);
    }

    public boolean seenBefore = false;

    @Override
    protected boolean act() {
        if (!seenBefore && Dungeon.level.heroFOV[pos] && Dungeon.level.distance(pos, hero.pos) <= 5) {
            seenBefore = true;
            SliceAlterPlot plot = new SliceAlterPlot();
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot,false)));
            spend(TICK);
            return true;
        }
        return super.act();
    }

    private static final String SEEN_BEFORE = "SennBefore";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SEEN_BEFORE,seenBefore);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        seenBefore = bundle.getBoolean(SEEN_BEFORE);
    }


}
