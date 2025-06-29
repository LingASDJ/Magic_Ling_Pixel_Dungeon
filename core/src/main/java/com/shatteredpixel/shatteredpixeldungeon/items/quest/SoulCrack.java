package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class SoulCrack extends Item {

    {
        image = ItemSpriteSheet.SOUL_CRACK;
        cursed = false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(Window.R_COLOR, 3f);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public static class RedSoulCrack extends SoulCrack {}

    public static class YellowSoulCrack extends SoulCrack {
        @Override
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(Window.CYELLOW, 3f);
        }
    }

    public static class BlueSoulCrack extends SoulCrack {
        @Override
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(Window.BLUE_COLOR, 3f);
        }
    }

    public static class GreenSoulCrack extends SoulCrack {
        @Override
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(Window.G_COLOR, 3f);
        }
    }


    public static class PinkSoulCrack extends SoulCrack {
        private float time;
        @Override
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(Window.Pink_COLOR, 3f);
        }
    }

}
