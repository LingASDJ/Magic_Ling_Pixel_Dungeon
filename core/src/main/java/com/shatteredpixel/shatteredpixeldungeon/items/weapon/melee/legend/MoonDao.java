package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MoonDao extends MeleeWeapon {
    public MoonDao() {
        image = ItemSpriteSheet.MOONDAILY;
        tier = 3;
        ACC = 1.54F;
        DLY = 0.3F;
    }

//    public int proc(Char attacker, Char defender, int damage ) {
//        int dmg;
//        dmg = (new Blocking()).proc(this, attacker, defender, damage);
//        damage = dmg;
//        return super.proc(attacker, defender, damage);
//    }
    public String statsInfo(){
    return (Messages.get(this,"stats_info"));
}
    @Override
    public int proc(Char attacker, Char defender, int damage) {

        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                Buff.affect(defender, Chill.class, 2+level()/4f);
            }
        }
        damage= (new Blocking()).proc(this, attacker, defender, damage);

        return super.proc(attacker, defender, damage);
    }


    public int min(int level) {
        return 3 + level;
    }

    @Override
    public int iceCoinValue() {
        return 175 + tier*25;
    }

    public int max(int level) {
        return 10 + level * 3;
    }
}
