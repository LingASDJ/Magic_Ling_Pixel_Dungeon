package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AlowGlyph;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;

public class AncityStone extends Armor.AlowGlyph {

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        //no proc effect, see Hero.isImmune and GhostHero.isImmune and ArmoredStatue.isImmune
        return damage;
    }

}

