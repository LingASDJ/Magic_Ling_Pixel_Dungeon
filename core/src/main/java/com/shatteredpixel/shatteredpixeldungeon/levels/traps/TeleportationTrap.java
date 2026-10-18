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

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class TeleportationTrap extends Trap {

	{
		color = TEAL;
		shape = DOTS;
	}

	//周期性触发的传送（如空间残片）：只搬动普通掉落物，不搬动贵重物品
	public boolean keepValuableItems = false;

	@Override
	public void activate() {

		for (int i : PathFinder.NEIGHBOURS9){
			Char ch = Actor.findChar(pos + i);
			if (ch != null){
				if (ScrollOfTeleportation.teleportChar(ch)) {
					if (ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).HUNTING) {
						((Mob) ch).state = ((Mob) ch).WANDERING;
					}
				}
			}
			Heap heap = Dungeon.level.heaps.get(pos + i);

			if (heap != null && heap.type == Heap.Type.HEAP && !(keepValuableItems && hasValuableItem(heap))){

				//先确认落点，再取物：落点无效时保留原地物品，避免物品被取走却无处放置而凭空消失
				int cell = Dungeon.level.randomRespawnCell( null );

				if (cell != -1) {
					Item item = heap.pickUp();

					if (item != null) {
						Dungeon.level.drop( item, cell );
						if (item instanceof Honeypot.ShatteredPot){
							((Honeypot.ShatteredPot)item).movePot(pos, cell);
						}
						Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
						CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
					}
				}
			}
		}

	}

	//贵重物品判定：Item.unique（升级卷轴、力量药水、神器、藏品、钥匙、任务道具等）
	//与 DeadEye 解离、小偷窃取、燃烧等机制采用的判定保持一致
	public static boolean hasValuableItem( Heap heap ){
		if (heap == null) return false;
		for (Item item : heap.items){
			if (item.unique) return true;
		}
		return false;
	}
}
