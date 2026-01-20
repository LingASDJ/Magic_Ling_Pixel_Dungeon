package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.WaloKePlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndWaloKe;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class WaloKe extends NTNPC {

    private static final String FIRST = "first";
    private boolean first=true;
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    {
        spriteClass = ShopkKingSprite.class;
    }
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }


    protected boolean act() {
        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );

        throwItem();
        return WaloKe.super.act();
    }

    public static Item shop1;
    public static Item shop2;
    public static Item shop3;

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        WaloKePlot plot = new WaloKePlot();
        if (first) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first=false;
        } else {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndWaloKe());
                }
            });

        }
        return true;
    }
}

