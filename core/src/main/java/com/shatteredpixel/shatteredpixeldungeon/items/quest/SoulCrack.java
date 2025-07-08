package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class SoulCrack extends Item {

    {
        image = ItemSpriteSheet.SOUL_CRACK_A;
        cursed = false;
        unique = true;
        stackable = true;
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

    public static class RedSoulCrack extends SoulCrack {
        {
            image = ItemSpriteSheet.SOUL_CRACK_A;
        }
    }

    public static class YellowSoulCrack extends SoulCrack {
        {
            image = ItemSpriteSheet.SOUL_CRACK_B;
        }
        @Override
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(Window.CYELLOW, 3f);
        }
    }

    public static class BlueSoulCrack extends SoulCrack {
        {
            image = ItemSpriteSheet.SOUL_CRACK_C;
        }
        @Override
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(Window.BLUE_COLOR, 3f);
        }
    }

    public static class GreenSoulCrack extends SoulCrack {
        {
            image = ItemSpriteSheet.SOUL_CRACK_D;
        }
        @Override
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(Window.G_COLOR, 3f);
        }
    }


    public static class PinkSoulCrack extends SoulCrack {
        {
            image = ItemSpriteSheet.SOUL_CRACK_E;
        }
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(Window.Pink_COLOR, 3f);
        }
    }

}
