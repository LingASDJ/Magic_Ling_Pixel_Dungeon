/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class WornShortsword extends MeleeWeapon {

	{
		image = ItemSpriteSheet.WORN_SHORTSWORD;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1.1f;
		RCH = 3;    //lots of extra reach
		tier = 1;
		
		bones = false;
	}

	/**
	 * 这个方法是用来计算武器的最大伤害的
	 * @param attacker 玩家
	 * @param defender 怪物
	 * @param damage 伤害
	 * @return 返回玩家，怪物，伤害
	 */
	@Override
	public int proc(Char attacker, Char defender, int damage ) {
		//为什么下面需要for循环？
		//因为有可能有多个怪物，所以需要循环
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			/** 为什么这里检查if？
			* 因为有可能是怪物，但是不是Golem，所以需要检查
			* 当是魔像的时候，返回的伤害还会追加5上去
			* 如果希望最大值能给予5，你也可以使用Math.max(damage, 5);
			**/
			if(mob instanceof Golem) {
				damage+=5;
			}
		}
		return super.proc(attacker, defender, damage);
	}




}
