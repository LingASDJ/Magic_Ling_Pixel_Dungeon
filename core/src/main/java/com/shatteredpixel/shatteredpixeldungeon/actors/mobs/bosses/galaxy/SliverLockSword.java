package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SliverLockSword extends MeleeWeapon implements Item.AnimationItem {

    public SliverLockSword() {
        image = ItemSpriteSheet.SLIVER_LOCK;
        tier = 6;
        DLY = 1F;
        animation = true;
    }

//    @Override
//    public int image() {
//        super.image = ItemSpriteSheet.RGJT_2;
//        if (level() >= 6) {
//            super.image = ItemSpriteSheet.RGJT_2;
//            RCH=2;
//            ACC = 2f;
//        } else if (level() >= 3) {
//            ACC = 2f;
//            super.image = ItemSpriteSheet.RGJT_2;
//        }
//        return image;
//    }

//    public String statsInfo(){
//        return (Messages.get(this,"stats_info"));
//    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        if(level()>8){
            Buff.prolong( defender, Slow.class, 2f );
        }

        damage= (new Unstable()).proc(this, attacker, defender, damage+level());

        return super.proc(attacker, defender, damage);
    }


    public int min(int level) {
        return 12 + level * 4;
    }

    public int max(int level) {
        return 24 + level * 8;
    }
}

