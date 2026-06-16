package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.ZeroDreamPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZeroDreamSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndZeroShop;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

public class ZeroDreamShop extends NTNPC {
    private static final String KEY_FIRST = "first";
    public boolean firstVisit = true;

    public static final Item[] SHOP_ITEMS = new Item[15];

    {
        spriteClass = ZeroDreamSprite.class;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(KEY_FIRST, firstVisit);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        firstVisit = bundle.getBoolean(KEY_FIRST);
    }

    @Override
    protected boolean act() {
        sprite.turnTo(pos, hero.pos);
        spend(TICK);
        throwItem();
        return super.act();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, hero.pos);
        if (firstVisit) {
            Game.runOnRenderThread(() -> {
                GameScene.show(new WndDialog(new ZeroDreamPlot(), false));
            });
            firstVisit = false;
        } else {
            Game.runOnRenderThread(() -> GameScene.show(new WndZeroShop()));
        }
        return true;
    }
}