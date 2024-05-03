package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SKINITEM extends Item {

    {
        image = ItemSpriteSheet.SKIN_1;
    }

    @Override
    public int iceCoinValue() {
        return 1;
    }


    public static class SKIN_WA extends SKINITEM {
        {
            image = ItemSpriteSheet.SKIN_2;
        }
        @Override
        public int iceCoinValue() {
            return 195;
        }
    }

    public static class SKIN_MA extends SKINITEM {
        {
            image = ItemSpriteSheet.SKIN_2;
        }
        @Override
        public int iceCoinValue() {
            return 275;
        }
    }

    public static class SKIN_RA extends SKINITEM {
        {
            image = ItemSpriteSheet.SKIN_3;
        }
        @Override
        public int iceCoinValue() {
            return 155;
        }
    }

    public static class SKIN_HA extends SKINITEM {
        {
            image = ItemSpriteSheet.SKIN_4;
        }
        @Override
        public int iceCoinValue() {
            return 235;
        }
    }

    public static class SKIN_DA extends SKINITEM {
        {
            image = ItemSpriteSheet.SKIN_5;
        }
        @Override
        public int iceCoinValue() {
            return 50;
        }
    }

    public static class SKIN_WB extends SKINITEM {
        {
            image = ItemSpriteSheet.SKIN_6;
        }
        @Override
        public int iceCoinValue() {
            return 300;
        }
    }

    public static class SKIN_MB extends SKINITEM {
        {
            image = ItemSpriteSheet.SKIN_7;
        }
        @Override
        public int iceCoinValue() {
            return 300;
        }
    }

    public static class SKIN_RB extends SKINITEM {
        {
            image = ItemSpriteSheet.SKIN_8;
        }
        @Override
        public int iceCoinValue() {
            return 300;
        }
    }

    public static class SKIN_HB extends SKINITEM {
        {
            image = ItemSpriteSheet.SKIN_9;
        }
        @Override
        public int iceCoinValue() {
            return 300;
        }
    }

    public static class SKIN_DB extends SKINITEM {
        {
            image = ItemSpriteSheet.SKIN_10;
        }
        @Override
        public int iceCoinValue() {
            return 300;
        }
    }

}
