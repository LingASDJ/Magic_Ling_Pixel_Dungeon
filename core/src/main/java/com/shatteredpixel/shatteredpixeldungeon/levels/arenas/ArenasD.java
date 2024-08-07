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

public class ArenasD extends LastLevelArenas {
	
	//phase 4
	protected static final int[] MAP_4_1 = {
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,W,W,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,e,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,e,s,s,s,s,s,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,e,c,c,c,c,c,s,s,s,c,G,G,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,e,w,w,w,W,W,W,c,c,s,s,G,G,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,W,w,w,w,w,w,W,c,c,G,G,s,G,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,W,W,W,w,w,w,w,c,G,G,G,s,G,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,m,c,c,c,c,w,w,w,c,s,s,s,G,G,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,m,G,G,G,c,s,s,s,s,s,c,G,G,G,m,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,G,G,s,s,s,c,w,w,w,c,c,c,c,m,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,G,s,G,G,G,c,w,w,w,w,W,W,W,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,G,s,G,G,c,c,W,w,w,w,w,w,W,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,G,G,s,s,c,c,W,W,W,w,w,w,e,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,G,G,c,s,s,s,c,c,c,c,c,e,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,s,s,s,s,s,e,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,e,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,W,W,W,  c,
		
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		
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
	
	protected static final int[] MAP_4_2 = {
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,W,W,W,c,c,c,W,W,W,c,c,c,W,W,W,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,e,e,W,s,s,s,s,s,W,G,G,G,B,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,e,G,w,c,W,W,W,c,s,s,G,G,G,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,e,G,w,c,c,c,c,c,G,G,s,G,G,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,e,G,G,G,w,c,c,c,c,c,B,G,G,s,G,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,W,w,w,w,W,w,w,w,w,w,W,B,G,s,W,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,s,c,c,c,w,G,G,G,G,G,w,c,c,c,s,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,s,W,c,c,w,G,W,e,W,G,w,c,c,W,s,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,s,W,c,c,w,G,e,e,e,G,w,c,c,W,s,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,s,W,c,c,w,G,W,e,W,G,w,c,c,W,s,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,s,c,c,c,w,G,G,G,G,G,w,c,c,c,s,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,W,s,G,B,W,w,w,w,w,w,W,w,w,w,W,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,G,s,G,G,B,c,c,c,c,c,w,G,G,G,e,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,G,G,s,G,G,c,c,c,c,c,w,G,e,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,G,G,G,s,s,c,W,W,W,c,w,G,e,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,B,G,G,G,W,s,s,s,s,s,W,e,e,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,W,W,W,c,c,c,W,W,W,c,c,c,W,W,W,W,  c,
		
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		
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
	
	protected static final int[] MAP_4_3 = {
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,W,W,W,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,e,c,w,c,w,c,c,w,c,w,w,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,e,e,e,w,c,c,w,c,w,c,w,c,c,w,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,e,e,e,m,w,w,c,w,c,c,w,c,w,c,w,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,S,m,e,G,G,c,w,c,w,w,c,w,c,c,w,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,m,G,G,G,G,c,w,c,c,w,c,w,w,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,G,G,e,e,G,w,c,w,c,w,c,c,w,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,e,c,c,B,e,G,c,w,c,c,w,c,w,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,e,c,c,c,c,c,s,c,w,w,c,w,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,e,m,c,c,e,m,e,c,G,G,c,w,c,w,w,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,e,s,e,c,G,e,m,c,e,e,G,c,w,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,m,c,e,c,c,G,e,c,B,e,G,G,w,c,w,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,e,s,c,m,c,c,c,c,c,G,G,G,m,w,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,e,c,s,c,e,e,c,c,c,G,G,e,e,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,m,c,s,c,s,m,e,e,c,m,m,e,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,e,e,m,e,e,c,c,c,c,S,e,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,W,W,  c,
		
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		
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
	
	protected static final int[] MAP_4_4 = {
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,m,g,G,G,c,c,c,G,G,G,w,w,w,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,e,e,e,B,G,B,c,B,c,B,G,B,w,B,w,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,m,e,m,g,G,g,m,e,m,g,G,G,w,w,w,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,g,B,g,B,G,B,e,B,e,B,G,B,G,B,G,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,G,G,G,G,s,s,s,s,s,s,s,G,G,G,G,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,G,B,g,B,s,B,s,B,s,B,s,B,g,B,G,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,c,c,m,e,s,s,s,s,s,s,s,e,m,c,c,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,c,B,e,B,s,B,s,B,s,B,s,B,e,B,c,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,c,c,m,e,s,s,s,s,s,s,s,e,m,c,c,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,G,B,g,B,s,B,s,B,s,B,s,B,g,B,G,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,G,G,G,G,s,s,s,s,s,s,s,G,G,G,G,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,G,B,G,B,G,B,e,B,e,B,G,B,g,B,g,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,w,w,w,G,G,g,m,e,m,g,G,g,m,e,m,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,w,B,w,B,G,B,c,B,c,B,G,B,e,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,w,w,w,G,G,G,c,c,c,G,G,g,m,e,e,W,  c,
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,  c,
		
		c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,  c,
		
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
