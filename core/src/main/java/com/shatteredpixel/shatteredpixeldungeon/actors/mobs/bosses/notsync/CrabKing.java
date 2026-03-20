package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

public class CrabKing extends Boss {

    {
        initProperty();
        initBaseStatus(5, 15, 14, 0, 100, 0, 6);
        initStatus(30);
        spriteClass = CrabKingSprite.class;
    }


    public static class ReloopLife extends FlavourBuff {


        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.SKYBULE_COLOR);
        }

        @Override
        public int icon() {
            return BuffIndicator.RECHARGING;
        }
    }

}
