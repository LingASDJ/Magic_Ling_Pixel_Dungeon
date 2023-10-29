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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.OrangeSprites;
import com.watabou.utils.Random;

public class Slime_Orange extends Slime {
    {
        spriteClass = OrangeSprites.class;
        lootChance = 0.25f; //by default, see rollToDropLoot()
        loot = PotionOfLiquidFlame.class;
        properties.add(Property.ACIDIC);
        maxLvl = 4;
        EXP=3;
    }
    private int combo = 0;
    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(0, 10) > 7) {
        }
        int damage2 = Slime_Orange.super.attackProc(enemy, this.combo + damage);
        if (Dungeon.level.flamable[enemy.pos]) {
            GameScene.add(Blob.seed(enemy.pos, 1, Fire.class));
        }
        if (enemy.buff(Burning.class) == null) {
            Buff.affect(enemy, Burning.class).reignite(enemy);
        }
        return damage2;
    }
}
