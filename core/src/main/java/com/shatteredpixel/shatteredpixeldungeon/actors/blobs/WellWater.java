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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LockSword;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public abstract class WellWater extends Blob {

	@Override
	protected void evolve() {
		int cell;
		for (int i=area.top-1; i <= area.bottom; i++) {
			for (int j = area.left-1; j <= area.right; j++) {
				cell = j + i* Dungeon.level.width();
				if (Dungeon.level.insideMap(cell)) {
					off[cell] = cur[cell];
					volume += off[cell];
				}
			}
		}
	}
	
	protected boolean affect( int pos ) {
		
		Heap heap;
		
		if (pos == Dungeon.hero.pos && affectHero( Dungeon.hero )) {
			
			clear(pos);
			if (volume <= 0 && landmark() != null) Notes.remove(landmark());
			return true;
			
		} else if ((heap = Dungeon.level.heaps.get( pos )) != null) {
			
			Item oldItem = heap.peek();
			Item newItem = affectItem( oldItem, pos );
			
			if (newItem != null) {
				
				if (newItem == oldItem || oldItem instanceof LockSword) {

				} else if (oldItem.quantity() > 1) {

					oldItem.quantity( oldItem.quantity() - 1 );
					heap.drop( newItem );
					
				} else {
					heap.replace( oldItem, newItem );
				}
				
				heap.sprite.link();
				clear(pos);
				if (volume <= 0 && landmark() != null) Notes.remove(landmark());
				
				return true;
				
			} else {
				
				int newPlace;
				do {
					newPlace = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
				} while (!Dungeon.level.passable[newPlace] && !Dungeon.level.avoid[newPlace]);
				Dungeon.level.drop( heap.pickUp(), newPlace ).sprite.drop( pos );
				
				return false;
				
			}
			
		} else {
			
			return false;
			
		}
	}
	
	protected abstract boolean affectHero( Hero hero );
	
	protected abstract Item affectItem( Item item, int pos );

	public static void affectCell( int cell ) {

		// 外层数组包含所有井水类型
		Class<?>[] waters = {WaterOfHealth.class, WaterOfAwareness.class, WaterOfTransmutation.class, WaterOfOil.class};

		for (Class<?>waterClass : waters) {
			WellWater water = (WellWater)Dungeon.level.blobs.get( waterClass );
			if (water != null &&
					water.volume > 0 &&
					water.cur[cell] > 0 &&
					water.affect( cell )) {

				Level.set( cell, Terrain.EMPTY_WELL );
				GameScene.updateMap( cell );

				if (Dungeon.level.feeling == Level.Feeling.THREEWELL) {
					Class<?>[] threeWaters = {WaterOfHealth.class, WaterOfAwareness.class, WaterOfTransmutation.class};
					for (Class<?> threeWaterClass : threeWaters) {
						Blob blob = Dungeon.level.blobs.get(threeWaterClass);
						if (blob != null) {

							for (int blobCell : blob.getActiveCells()) {
								Level.set(blobCell, Terrain.EMPTY_WELL);
								GameScene.updateMap(blobCell);
							}
							blob.fullyClear();
						}
					}
				}
			}
		}
	}
}
