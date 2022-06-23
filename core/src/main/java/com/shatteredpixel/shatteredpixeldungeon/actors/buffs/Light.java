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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilLantern;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Light extends FlavourBuff {
	private static final float DELAY = 5.0f;
	public static final int DISTANCE = 6;
	public static final float DURATION = 300.0f;

	public Light() {
		this.type = Buff.buffType.POSITIVE;
	}

	public boolean attachTo(Char target) {
		if (!Light.super.attachTo(target)) {
			return false;
		}
		if (Dungeon.level == null) {
			return true;
		}
		target.viewDistance = Math.max(Dungeon.level.viewDistance, 6);
		Dungeon.observe();
		return true;
	}

	public boolean act() {
		OilLantern lantern = Dungeon.hero.belongings.getItem(OilLantern.class);
		if (lantern == null || !lantern.isActivated() || lantern.getCharge() <= 0) {
			assert lantern != null;
			lantern.deactivate(Dungeon.hero, false);
			detach();
			return true;
		}
		lantern.spendCharge();
		spend(DELAY);
		return true;
	}

	public void detach() {
		this.target.viewDistance = Dungeon.level.viewDistance;
		Dungeon.observe();
		Light.super.detach();
	}

	public void weaken( int amount ){
		spend(-amount);
	}

	@Override
	public int icon() {
		return BuffIndicator.LIGHT;
	}

	public void fx(boolean on) {
		if (on) {
			this.target.sprite.add(CharSprite.State.ILLUMINATED);
		} else {
			this.target.sprite.remove(CharSprite.State.ILLUMINATED);
		}
	}

	public String toString() {
		return Messages.get(this, "name");
	}

	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}