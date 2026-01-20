package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class AltVampiric extends Weapon.Enchantment {

    private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0x660022 );

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage ) {

        // Directly calculate heal amount as a fixed range from 10% to 25% of damage dealt
        float healPercentage = Random.Float(0.2f, 0.45f);
        int healAmt = Math.round(damage * healPercentage);
        healAmt = Math.min( healAmt, attacker.HT - attacker.HP );
        if (healAmt < 1) {
            healAmt = Math.min( 1+Dungeon.depth/5, attacker.HT - attacker.HP );
            attacker.HP += healAmt;
            attacker.sprite.showStatusWithIcon( CharSprite.PINKTEXT, Integer.toString( healAmt ), FloatingText.PINKHEAL );
        } else {
            if (attacker.isAlive()) {
                attacker.HP += healAmt;
                attacker.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString( healAmt ), FloatingText.HEALING );

            }
        }


        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return RED;
    }
}
