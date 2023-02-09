/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CrivusFruitsFood;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

public class LifeTreeSword extends MeleeWeapon {

    {
        image = ItemSpriteSheet.LifeTreeSword;

        tier = 3;
    }

    public String desc() {
        return Messages.get(this, "desc")+"_"+getFood+"_";
    }

    public int proc(Char attacker, Char defender, int damage ) {
        if (defender.HP <= damage && getFood < 99){
            //判定为死亡
            getFood+=111;
        } else {
            Dungeon.level.drop( new CrivusFruitsFood(), defender.pos ).sprite.drop();
            getFood=0;
        }

        return super.proc(attacker, defender, damage);


    }

    private int getFood = 0;

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.getFood = bundle.getInt("getFood");
    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("getFood", this.getFood);
    }

    @Override
    public int max(int lvl) {
        return  12+lvl;
    }

    public int min(int lvl) {
        return  9+lvl*2;
    }

}
