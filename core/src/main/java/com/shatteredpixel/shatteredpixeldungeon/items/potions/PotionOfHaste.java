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

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.EXSG;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SentryRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PotionOfHaste extends Potion {
	
	{
		icon = ItemSpriteSheet.Icons.POTION_HASTE;
	}
	
	@Override
	public void apply(Hero hero) {
		identify();
		boolean blood = false;
		if(Dungeon.isChallenged(EXSG)) {
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (mob instanceof SentryRoom.Sentry) {
					blood = true;
				}
			}
			if(blood){
				GLog.w( Messages.get(this, "energetic") );
				Buff.prolong( hero, Haste.class, Haste.DURATION);
				SpellSprite.show(hero, SpellSprite.HASTE, 1, 1, 0);
			} else {
				Buff.affect(hero, Cripple.class, 8f);
				GLog.n( Messages.get(this, "energy_speed") );
			}
		} else {
			GLog.w( Messages.get(this, "energetic") );
			Buff.prolong( hero, Haste.class, Haste.DURATION);
			SpellSprite.show(hero, SpellSprite.HASTE, 1, 1, 0);

		}
	}

	@Override
	public String name() {
		return Dungeon.isChallenged(Challenges.EXSG) && isIdentified() ? Messages.get(this, "namex") : isIdentified() ?  Messages.get(this, "name") : super.name();
	}

	@Override
	public String desc() {
		return Dungeon.isChallenged(Challenges.EXSG) && isIdentified() ? Messages.get(this, "descx") : isIdentified() ?  Messages.get(this, "desc") : super.desc();
	}

	@Override
	public int value() {
		return isKnown() ? 40 * quantity : super.value();
	}
}
