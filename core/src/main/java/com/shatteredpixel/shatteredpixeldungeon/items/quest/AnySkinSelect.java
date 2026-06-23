package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndZeroShop;
import com.watabou.noosa.Game;

import java.util.ArrayList;

public class AnySkinSelect extends Item {
    {
        image = ItemSpriteSheet.HALLS_PAGE;
        unique = true;
    }
    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public static class AnySkinCustomSelect extends Item {

        public static final String AC_CONNECT		= "CONECT";

        {
            image = ItemSpriteSheet.BEACON;
            unique = true;
        }

        @Override
        public ArrayList<String> actions( Hero hero ) {
            ArrayList<String> actions = super.actions( hero );
            actions.add( AC_CONNECT);
            return actions;
        }

        @Override
        public void execute( Hero hero, String action ) {

            super.execute(hero, action);

            if (action.equals(AC_CONNECT)) {
                Game.runOnRenderThread(() -> GameScene.show(new WndZeroShop()));
            }
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
}
