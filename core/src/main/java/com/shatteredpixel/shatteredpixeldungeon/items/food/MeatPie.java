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

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.EXSG;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MeatPie extends Food {
	private static final int[] foodLimit = new int[] { 1, 2, 2, 1, 1 };

	{
		image = ItemSpriteSheet.MEAT_PIE;
		energy = Hunger.STARVING * 2f;
	}

	@Override
	protected void satisfy(Hero hero) {
		if (Dungeon.isChallenged(Challenges.EXSG)) {
			Buff.prolong(hero, Haste.class, 8f);

			int region = Math.min(Statistics.deepestFloor / 5, foodLimit.length - 1);
			// Can we get more STR from food?
			if (Statistics.GetFoodLing < foodLimit[region]) {
				// Random chance
				if (Random.Float() > 0.25f + hero.STR / 50f) {
					Statistics.GetFoodLing++;
					hero.STR++;
					hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "eat_msg_1"));
					GLog.p(Messages.get(this, "eat_msg_2"));
				}
			} else { // You reached the limit of STR from food.
				GLog.w(Messages.get(this, "eat_msg_3", Statistics.GetFoodLing));
			}
		}
		super.satisfy(hero);
		Buff.affect(hero, WellFed.class).reset();
	}

	@Override
	public int value() {
		return 40 * quantity;
	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			boolean pasty = false;
			boolean ration = false;
			boolean meat = false;

			for (Item ingredient : ingredients) {
				if (ingredient.quantity() > 0) {
					if (ingredient instanceof Pasty || ingredient instanceof PhantomMeat) {
						pasty = true;
					} else if (ingredient.getClass() == Food.class) {
						ration = true;
					} else if (ingredient instanceof MysteryMeat
							|| ingredient instanceof StewedMeat
							|| ingredient instanceof ChargrilledMeat
							|| ingredient instanceof FrozenCarpaccio) {
						meat = true;
					}
				}
			}

			return pasty && ration && meat;
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 6;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients))
				return null;

			for (Item ingredient : ingredients) {
				ingredient.quantity(ingredient.quantity() - 1);
			}

			return sampleOutput(null);
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return new MeatPie();
		}
	}

	@Override
	public String desc() {
		if (Dungeon.isChallenged(EXSG) && Dungeon.hero != null) {
			Hero hero = Dungeon.hero;
			int region = Math.min(Statistics.deepestFloor / 5, foodLimit.length - 1);
			int maxStr = foodLimit[region];
			int currentStr = Statistics.GetFoodLing;

			// 计算当前真实概率
			float chance = 1f - (0.25f + hero.STR / 50f);
			chance = Math.max(chance, 0f); // 防止负数
			int percent = Math.round(chance * 100);

			// 把 3 个参数传给多语言：当前/上限/成功率
			return Messages.get(this, "descx", percent, currentStr, maxStr);
		} else {
			return Messages.get(this, "desc");
		}
	}
}
