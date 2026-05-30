//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class JunglePoison extends MeleeWeapon {

    {
        image = ItemSpriteSheet.JUNGLE_SWORD;
        tier = 3;
        DLY = 0.5f;
    }

    @Override
    public int min(int lvl) {
        return 6 + lvl;
    }

    @Override
    public int max(int lvl) {
        return 12 + lvl * 2;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Buff.affect(defender, Poison.class).extend(2*level()+3);

        Poison enemypoison = defender.buff(Poison.class);
        if (enemypoison != null) {
            if(enemypoison.GetPoisonLevel() >= (float) defender.HT /3){
                Buff.affect( defender, Roots.class, enemypoison.GetPoisonLevel() );
                Buff.affect( defender, Blindness.class, enemypoison.GetPoisonLevel() );
            }
        }

        return super.proc(attacker, defender, damage);
    }



}
