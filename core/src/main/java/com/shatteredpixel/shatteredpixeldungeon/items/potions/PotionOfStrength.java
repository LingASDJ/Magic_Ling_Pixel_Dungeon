/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.EXSG;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PotionOfStrength extends Potion {

	{
		icon = ItemSpriteSheet.Icons.POTION_STRENGTH;

		unique = true;

		talentFactor = 2f;
	}
	
	@Override
	public void apply( Hero hero ) {
		identify();
		if(Dungeon.isChallenged(EXSG)) {
			hero.STR--;
			hero.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "esg_1"));
			GLog.n(Messages.get(this, "esg_2"));
			hero.attackSkill += 3;
		} else {
			hero.STR++;
			hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "msg_1"));
			GLog.p(Messages.get(this, "msg_2"));
		}

		Badges.validateStrengthAttained();
		//Badges.validateDuelistUnlock();
	}

	@Override
	public int value() {
		return isKnown() ? 50 * quantity : super.value();
	}

	@Override
	public int energyVal() {
		return isKnown() ? 10 * quantity : super.energyVal();
	}

	@Override
	public String name() {
		return Dungeon.isChallenged(Challenges.EXSG) && isIdentified() ? Messages.get(this, "namex") : isIdentified() ?  Messages.get(this, "name") : super.name();
	}

	@Override
	public String desc() {
		return Dungeon.isChallenged(Challenges.EXSG) && isIdentified() ? Messages.get(this, "descx") : isIdentified() ?  Messages.get(this, "desc") : super.desc();
	}
}
