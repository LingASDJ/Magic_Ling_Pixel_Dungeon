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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.ExorcistMaul;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class MagicImmune extends FlavourBuff {

	public static final float DURATION = 20f;
	
	{
		type = buffType.POSITIVE;
		announced = true;
	}
	
	{
		immunities.addAll(AntiMagic.RESISTS);
	}

	// 魔法免疫判断
	public static boolean isMagicImmuned(Char ch) {
		// 拥有魔法免疫buff
		if (ch.buff(MagicImmune.class) == null) return false;
		// 且 不拥有魔法免疫忽略buff
		if (ch instanceof Hero) {
			Hero hero = (Hero) ch;
			if (hero.belongings.weapon() instanceof ExorcistMaul)     return false;
			if (hero.belongings.secondWep() instanceof ExorcistMaul)  return false;
		}
		// 通过种种截断后，来到返回为真
		return true;
	}

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)){
			for (Buff b : target.buffs()){
				for (Class immunity : immunities){
					if (b.getClass().isAssignableFrom(immunity)){
						b.detach();
						break;
					}
				}
			}
			if (target instanceof Hero){
				((Hero) target).updateHT(false);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void detach() {
		super.detach();
		if (target instanceof Hero){
			((Hero) target).updateHT(false);
		}
	}

	@Override
	public int icon() {
		return BuffIndicator.COMBO;
	}
	
	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(0, 1, 0);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}
}
