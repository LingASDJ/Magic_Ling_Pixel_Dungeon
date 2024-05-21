/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * Rivals Pixel Dungeon
 * Copyright (C) 2019-2020 Marshall M.
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

package com.shatteredpixel.shatteredpixeldungeon.levels.arenas;

import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

import com.watabou.utils.Random;

public class LastLevelArenas {
	
	//random map methods
	public static int[] randomPhase1Map() {
		return Random.oneOf( ArenasA.MAP_1_1, ArenasA.MAP_1_2, ArenasA.MAP_1_3, ArenasA.MAP_1_4 );
	};
	
	public static int[] randomPhase2Map() {
		return Random.oneOf( ArenasB.MAP_2_1, ArenasB.MAP_2_2, ArenasB.MAP_2_3, ArenasB.MAP_2_4 );
	};
	
	public static int[] randomPhase3Map() {
		return Random.oneOf( ArenasC.MAP_3_1, ArenasC.MAP_3_2, ArenasC.MAP_3_3, ArenasC.MAP_3_4 );
	};
	
	public static int[] randomPhase4Map() {
		return Random.oneOf( ArenasD.MAP_4_1, ArenasD.MAP_4_2, ArenasD.MAP_4_3, ArenasD.MAP_4_4 );
	};
	
	public static int[] randomPhase5Map() {
		return ArenasE.MAP_5_1;
	};
	
	public static int[] randomWonMap() {
		return MAP_WON;
	};
	
	//terrain constants
	protected static final int c = Terrain.WALL;
	protected static final int e = Terrain.EMPTY;
	protected static final int g = Terrain.GRASS;
	protected static final int W = Terrain.WALL;
	
	protected static final int m = Terrain.EMBERS;
	protected static final int p = Terrain.PEDESTAL;
	protected static final int s = Terrain.EMPTY_SP;
	protected static final int G = Terrain.HIGH_GRASS;
	protected static final int t = Terrain.INACTIVE_TRAP;
	protected static final int E = Terrain.STATUE;
	protected static final int S = Terrain.STATUE_SP;
	protected static final int B = Terrain.BOOKSHELF;
	protected static final int w = Terrain.WATER;
	
	protected static final int[] MAP_WON = {
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,B,B,G,G,G,B,B,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,B,G,G,G,G,G,B,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,G,G,S,w,S,G,G,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,G,w,w,p,w,w,G,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,w,w,S,e,S,w,w,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,w,w,m,m,e,w,w,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,w,e,e,m,w,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,m,e,e,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,m,e,m,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,m,m,e,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,e,m,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,e,m,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,m,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,m,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,e,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,
		
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c
	};
}
