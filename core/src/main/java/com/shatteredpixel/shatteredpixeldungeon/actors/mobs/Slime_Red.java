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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedSprites;
import com.watabou.utils.Random;

public class Slime_Red extends Slime {

    {
        spriteClass = RedSprites.class;
        maxLvl = 4;
        lootChance = 0.15f;
        loot = PotionOfHealing.class;
        properties.add(Property.ACIDIC);
        if(Statistics.bossRushMode&& Dungeon.depth ==19){
            HT = HP = 40;
        } else {
            HT = HP = 20;
        }
    }

    @Override
    public int attackSkill( Char target ) {
        if(Statistics.bossRushMode && Dungeon.depth ==19){
            return 60;
        }
        return 12;
    }

    @Override
    public int damageRoll() {
        if(Statistics.bossRushMode&& Dungeon.depth ==19){
            return Random.NormalIntRange(20, 25);
        }
        return super.damageRoll();
    }

    @Override
    public int drRoll() {
        return super.drRoll() + (Statistics.bossRushMode && Dungeon.depth ==19 ?Random.NormalIntRange(0, 5) : 0);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(0, 6) < 4) {
            Buff.affect( enemy, Bleeding.class ).set( damage/3f );
        }
        return Slime_Red.super.attackProc(enemy, damage);
    }

}
