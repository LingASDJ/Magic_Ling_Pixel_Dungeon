package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class BlessImmune extends ChampionHero {

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

    @Override
    public float damageTakenFactor() {
        return 0.75f;
    }

    {
        immunities.addAll(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic.RESISTS);
    }

    @Override
    public void fx(boolean on) {
    }

    @Override
    public String iconTextDisplay() {
        return "";
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0xD2691E);
    }

    @Override
    public int icon() {
        return BuffIndicator.GOBUFF_UPRD;
    }


}
