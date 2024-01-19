/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.keys;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LockSword;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class CrystalKey extends Key {
	
	{
		image = ItemSpriteSheet.CRYSTAL_KEY;
	}

	public CrystalKey() {
		this( 0 );
	}
	
	public CrystalKey( int depth ) {
		super();
		this.depth = depth;
	}

	@Override
	public boolean doPickUp(Hero hero, int pos) {
		super.doPickUp(hero, pos);
		LockSword lockSword = Dungeon.hero.belongings.getItem(LockSword.class);
		if(lockSword != null) {
			int index = 25;
			lockSword.lvl += index;
			int lvl = lockSword.lvl;
			if (lvl >= 100 && lvl <= 1000 && lvl % 100 == 0) {
				// 提醒气泡的显示逻辑
				GLog.p(Messages.get(Key.class,"locksword"));
			}
			hero.sprite.showStatus(0x123456ff, String.valueOf(index));
			return true;
		}

		return true;
	}

	
}
