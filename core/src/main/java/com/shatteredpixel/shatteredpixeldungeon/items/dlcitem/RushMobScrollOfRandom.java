package com.shatteredpixel.shatteredpixeldungeon.items.dlcitem;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHardNotification;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class RushMobScrollOfRandom extends DLCItem {

    {
        image = ItemSpriteSheet.SCROLL_GOLD;

        stackable = true;
        unique = true;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals( AC_READ )) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show( new WndHardNotification(new ItemSprite(ItemSpriteSheet.SCROLL_GOLD),
                            name(),
                            Messages.get(RushMobScrollOfRandom.class,"dlc_desc"),
                            "OK",
                            0));
                }
            });
        }
    }

    @Override
    public int iceCoinValue() {
        return 300;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}
