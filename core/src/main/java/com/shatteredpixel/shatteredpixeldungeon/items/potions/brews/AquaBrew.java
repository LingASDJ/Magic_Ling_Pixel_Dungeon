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

package com.shatteredpixel.shatteredpixeldungeon.items.potions.brews;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStormClouds;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;

public class AquaBrew extends Brew {

	{
		image = ItemSpriteSheet.MISC_WATERBOMB;

		talentChance = 1/(float)Recipe.OUT_QUANTITY;
	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc");
		if(Dungeon.isChallenged(Challenges.AQUAPHOBIA)){
			desc += "\n\n" + Messages.get(this, "salt");
		}
		return desc;
	}

	protected void DEM(int cell) {
		final int color = splashColor();
		Char ch = Actor.findChar(cell);
		if (ch != null) {
			Buff.affect(ch, Burning.class).reignite(ch, 8f);
			Buff.affect(ch, Blindness.class, 8f);
			Splash.at(ch.sprite.center(), color, 5);
		} else {
			Splash.at(cell, color, 5);
		}
	}

	@Override
	public void shatter(int cell) {

		if (Dungeon.level.map[cell] == Terrain.SALT_WATER) {
			for (int offset : PathFinder.NEIGHBOURS8) {
				if (Dungeon.level.map[cell + offset] == Terrain.SALT_WATER) {
					Level.set(cell + offset, Terrain.EMPTY_SP);
					DEM(cell + offset);
					GameScene.add(Blob.seed(cell + offset, 5, Fire.class));
					GameScene.updateMap(cell + offset);
					Dungeon.level.addVisuals();
				}
			}
		} else {
			GeyserTrap geyser = new GeyserTrap();
			geyser.pos = cell;
			geyser.source = this;
			int userPos = curUser.pos;
			if (userPos != cell){
				Ballistica aim = new Ballistica(userPos, cell, Ballistica.STOP_TARGET);
				if (aim.path.size() > aim.dist+1) {
					geyser.centerKnockBackDirection = aim.path.get(aim.dist + 1);
				}
			}
			geyser.activate();
		}





	}

	@Override
	public int value() {
		return (int)(60 * (quantity/(float)Recipe.OUT_QUANTITY));
	}

	@Override
	public int energyVal() {
		return (int)(12 * (quantity/(float)Recipe.OUT_QUANTITY));
	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		private static final int OUT_QUANTITY = 8;

		{
			inputs =  new Class[]{PotionOfStormClouds.class};
			inQuantity = new int[]{1};

			cost = 8;

			output = AquaBrew.class;
			outQuantity = OUT_QUANTITY;
		}

	}

}
