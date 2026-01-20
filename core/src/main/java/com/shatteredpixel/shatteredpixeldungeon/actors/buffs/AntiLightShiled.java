package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Potential;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class AntiLightShiled extends RandomBuff{
    {
        immunities.add(Electricity.class);
        immunities.add(WandOfLightning.class);
        immunities.add(Lightning.class);
        immunities.add(Shocking.class);
        immunities.add(Potential.class);
    }
    private int interval = 1;
    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.SHIELDED);
        else target.sprite.remove(CharSprite.State.SHIELDED);
    }
    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0xFF0000);
    }
    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend( interval );
            if (--level <= 0) {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.ARMOR;
    }
}