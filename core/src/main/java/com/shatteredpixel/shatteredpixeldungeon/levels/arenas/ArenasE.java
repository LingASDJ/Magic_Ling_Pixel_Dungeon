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

public class ArenasE extends LastLevelArenas {
	
	//phase 5
	protected static final int[] MAP_5_1 = {
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		
		W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,B,B,B,B,B,G,G,G,G,G,B,B,B,B,B,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,B,B,B,G,G,G,g,g,g,G,G,G,B,B,B,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,B,B,G,G,g,g,g,g,g,g,g,G,G,B,B,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,B,G,G,g,g,G,G,G,G,G,g,g,G,G,B,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,B,G,g,g,G,G,w,w,w,G,G,g,g,G,B,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,G,G,g,g,G,S,s,w,s,S,G,g,g,G,G,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,G,e,e,e,w,s,s,s,s,s,w,e,e,e,G,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,G,e,e,w,w,w,s,p,s,w,w,w,e,e,G,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,G,e,e,w,w,s,s,s,s,s,w,w,e,e,G,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,G,G,w,w,e,S,s,m,s,S,e,w,w,G,G,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,B,G,w,w,m,e,m,e,m,e,m,w,w,G,B,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		W,c,G,w,w,e,m,e,e,e,m,e,w,w,G,c,W,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,w,w,e,m,e,m,e,m,e,w,w,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,w,w,e,m,e,m,e,w,w,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,W,m,e,e,e,m,W,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,W,W,W,W,W,W,W,W,W,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,
		
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
