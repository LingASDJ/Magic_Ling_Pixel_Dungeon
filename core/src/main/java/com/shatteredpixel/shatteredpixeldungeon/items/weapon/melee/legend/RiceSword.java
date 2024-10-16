package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RiceSword extends MeleeWeapon {

    {
        image = ItemSpriteSheet.RICESWORD;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.3f;
        tier = 5;
        RCH = 2;
    }

    @Override
    public int iceCoinValue() {
        if (Badges.isUnlocked(Badges.Badge.NYZ_SHOP)){
            return (int) (200 * 0.9f);
        }
        return 200;
    }


    @Override
    public int proc(Char attacker, Char defender, int damage) {

        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                Buff.affect(defender, Hex.class, 2+level()/4f);
            }
        }

        return super.proc(attacker, defender, damage);
    }
    @Override
    public int min(int lvl) {
        return tier + lvl * 3;
    }

    @Override
    public int max(int lvl) {
        Hunger hungerBuff = hero.buff(Hunger.class);

        if(hero.buff(WellFed.class) != null){
            return 5*(tier+1) + lvl * hungerBuff.hungerDamage() + lvl;
        } else {
            return 5*(tier+1) + lvl * hungerBuff.hungerNoWEDamage() + lvl;
        }
    }
}
