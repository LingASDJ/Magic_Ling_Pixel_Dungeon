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

package com.watabou.utils;

import java.util.Arrays;
import java.util.LinkedList;

public class PathFinder {
	
	public static int[] distance;
	private static int[] maxVal;
	
	private static boolean[] goals;
	private static int[] queue;
	private static boolean[] queued; //currently only used in getStepBack, other can piggyback on distance
	
	private static int size = 0;
	private static int width = 0;

	private static int[] dir;
	private static int[] dirLR;

	//performance-light shortcuts for some common pathfinder cases
	//they are in array-access order for increased memory performance
	public static int[] NEIGHBOURS1;
	public static int[] NEIGHBOURS4;
	public static int[] NEIGHBOURS8;
	public static int[] NEIGHBOURS9;

	public static int[] NEIGHBOURS13;

	public static int[] NEIGHBOURS13_4;

	public static int[] NEIGHBOURS5;

	public static int[] CROSS;

	public static int[] CROSS_7x7;

	//similar to their equivalent neighbour arrays, but the order is clockwise.
	//Useful for some logic functions, but is slower due to lack of array-access order.
	public static int[] CIRCLE4;
	public static int[] CIRCLE8;

	public static int[] CIRCLE7;

	public static int[] NEIGHBOURS49;
	
	public static void setMapSize( int width, int height ) {
		
		PathFinder.width = width;
		PathFinder.size = width * height;
		
		distance = new int[size];
		goals = new boolean[size];
		queue = new int[size];
		queued = new boolean[size];

		maxVal = new int[size];
		Arrays.fill(maxVal, Integer.MAX_VALUE);

		dir = new int[]{-1, +1, -width, +width, -width-1, -width+1, +width-1, +width+1};

		// 以下列表存储了在这一层中各个方位的偏置值，以下方位分别是：左上、左、左下、上、下、右上、右、右下
		dirLR = new int[]{-1-width, -1, -1+width, -width, +width, +1-width, +1, +1+width};

		NEIGHBOURS1 = new int[]{-width};
		NEIGHBOURS4 = new int[]{-width, -1, +1, +width};
		NEIGHBOURS8 = new int[]{-width-1, -width, -width+1, -1, +1, +width-1, +width, +width+1};
		NEIGHBOURS9 = new int[]{-width-1, -width, -width+1, -1, 0, +1, +width-1, +width, +width+1};
		NEIGHBOURS13 = new int[]{
				         -width*2,
				-width-1, -width, -width+1,
			-2,	-1, 		 			+1, +2,
				+width-1, +width, +width+1,
						+width*2
		};

		NEIGHBOURS13_4 = new int[]{
				-width*2,
				-width-1, -width, -width+1,
				-2,	-1, 		 0,			+1, +2,
				+width-1, +width, +width+1,
				+width*2
		};
		NEIGHBOURS5 = new int[]{-2*width-2, -2*width-1, -2*width, -2*width+1, -2*width+2,
				-width-2,   -width-1,   -width,   -width+1,   -width,
				-2,         -1,         0,        +1,          +2,
				+width-2,   +width-1,   +width,   +width+1,   +width+2,
				+2*width-2, +2*width-1, +2*width, +2*width+1, +2*width+2};

		NEIGHBOURS49 = new int[] {
				-width-1, -width, -width+1,
				-1, 0, +1,
				+width-1, +width, +width+1,
				-2*width-2, -2*width-1, -2*width, -2*width+1, -2*width+2,
				-width*2-1, -width*2, -width*2+1,
				-width-2, -width+2,
				-width*2-2, -width*2-1, -width*2, -width*2+1, -width*2+2,
				-width-2, -width+2,
				-width-1, -width, -width+1,
				-1, 0, +1,
				+width-1, +width, +width+1,
				+width*2-2, +width*2-1, +width*2, +width*2+1, +width*2+2,
				+width-2, +width+2,
				+width*2-2, +width*2-1, +width*2, +width*2+1, +width*2+2,
				-width-2, -width+2,
				-2*width-2, -2*width-1, -2*width, -2*width+1, -2*width+2,
				-width*2-1, -width*2, -width*2+1,
				-width-1, -width, -width+1,
				-1, 0, +1,
				+width-1, +width, +width+1
		};

		CROSS = new int[]{-width, -1, 0, +1, +width};

		CIRCLE8 = new int[]{-7*width, -1, 0, +1, +7*width};

		CIRCLE4 = new int[]{-width, +1, +width, -1};
		CIRCLE8 = new int[]{-width-1, -width, -width+1, +1, +width+1, +width, +width-1, -1};

		CIRCLE7 = new int[]{
				-3*width-3, -3*width-2, -3*width-1, -3*width, -3*width+1, -3*width+2, -3*width+3,
				-2*width-3, -2*width-2, -2*width-1, -2*width, -2*width+1, -2*width+2, -2*width+3,
				-width-3, -width-2, -width-1, -width, -width+1, -width+2, -width+3,
				-3, -2, -1, 0, +1, +2, +3,
				+width-3, +width-2, +width-1, +width, +width+1, +width+2, +width+3,
				+2*width-3, +2*width-2, +2*width-1, +2*width, +2*width+1, +2*width+2, +2*width+3,
				+3*width-3, +3*width-2, +3*width-1, +3*width, +3*width+1, +3*width+2, +3*width+3
		};
	}

	public static Path find( int from, int to, boolean[] passable ) {

		if (!buildDistanceMap( from, to, passable )) {
			return null;
		}
		
		Path result = new Path();
		int s = from;

		// From the starting position we are moving downwards,
		// until we reach the ending point
		do {
			int minD = distance[s];
			int mins = s;
			
			for (int i=0; i < dir.length; i++) {
				
				int n = s + dir[i];
				try {
					int thisD = distance[n];
					if (thisD < minD) {
						minD = thisD;
						mins = n;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("ArrayIndex-Error");
				}
			}
			s = mins;
			result.add( s );
		} while (s != to);
		
		return result;
	}
	
	public static int getStep( int from, int to, boolean[] passable ) {
		
		if (!buildDistanceMap( from, to, passable )) {
			return -1;
		}
		
		// From the starting position we are making one step downwards
		int minD = distance[from];
		int best = from;
		
		int step, stepD;
		
		for (int i=0; i < dir.length; i++) {

			if ((stepD = distance[step = from + dir[i]]) < minD) {
				minD = stepD;
				best = step;
			}
		}

		return best;
	}
	
	public static int getStepBack( int cur, int from, int lookahead, boolean[] passable, boolean canApproachFromPos ) {

		int d = buildEscapeDistanceMap( cur, from, lookahead, passable );
		if (d == 0) return -1;

		if (!canApproachFromPos) {
			//We can't approach the position we are retreating from
			//re-calculate based on this, and reduce the target distance if need-be
			int head = 0;
			int tail = 0;

			int newD = distance[cur];
			BArray.setFalse(queued);

			queue[tail++] = cur;
			queued[cur] = true;

			while (head < tail) {
				int step = queue[head++];

				if (distance[step] > newD) {
					newD = distance[step];
				}

				int start = (step % width == 0 ? 3 : 0);
				int end = ((step + 1) % width == 0 ? 3 : 0);
				for (int i = start; i < dirLR.length - end; i++) {

					int n = step + dirLR[i];
					if (n >= 0 && n < size && passable[n]) {
						if (distance[n] < distance[cur]) {
							passable[n] = false;
						} else if (distance[n] >= distance[step] && !queued[n]) {
							// Add to queue
							queue[tail++] = n;
							queued[n] = true;
						}
					}
				}

			}

			d = Math.min(newD, d);
		}

		for (int i=0; i < size; i++) {
			goals[i] = distance[i] == d;
		}
		if (!buildDistanceMap( cur, goals, passable )) {
			return -1;
		}

		int s = cur;
		
		// From the starting position we are making one step downwards
		int minD = distance[s];
		int mins = s;
		
		for (int i=0; i < dir.length; i++) {

			int n = s + dir[i];
			int thisD = distance[n];
			
			if (thisD < minD) {
				minD = thisD;
				mins = n;
			}
		}

		return mins;
	}
	
	private static boolean buildDistanceMap( int from, int to, boolean[] passable ) {
		
		if (from == to) {
			return false;
		}

		System.arraycopy(maxVal, 0, distance, 0, maxVal.length);
		
		boolean pathFound = false;
		
		int head = 0;
		int tail = 0;
		
		// Add to queue
		queue[tail++] = to;
		distance[to] = 0;
		
		while (head < tail) {
			
			// Remove from queue
			int step = queue[head++];
			if (step == from) {
				pathFound = true;
				break;
			}
			int nextDistance = distance[step] + 1;
			
			int start = (step % width == 0 ? 3 : 0);
			int end   = ((step+1) % width == 0 ? 3 : 0);
			for (int i = start; i < dirLR.length - end; i++) {

				int n = step + dirLR[i];
				if (n == from || (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance))) {
					// Add to queue
					queue[tail++] = n;
					distance[n] = nextDistance;
				}
					
			}
		}
		
		return pathFound;
	}
	
	public static void buildDistanceMap( int to, boolean[] passable, int limit ) {
		
		System.arraycopy(maxVal, 0, distance, 0, maxVal.length);
		
		int head = 0;
		int tail = 0;
		
		// Add to queue
		queue[tail++] = to;
		distance[to] = 0;
		
		while (head < tail) {
			
			// Remove from queue
			int step = queue[head++];
			
			int nextDistance = distance[step] + 1;
			if (nextDistance > limit) {
				return;
			}
			
			int start = (step % width == 0 ? 3 : 0);
			int end   = ((step+1) % width == 0 ? 3 : 0);
			for (int i = start; i < dirLR.length - end; i++) {

				int n = step + dirLR[i];
				if (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance)) {
					// Add to queue
					queue[tail++] = n;
					distance[n] = nextDistance;
				}
					
			}
		}
	}
	
	private static boolean buildDistanceMap( int from, boolean[] to, boolean[] passable ) {
		
		if (to[from]) {
			return false;
		}
		
		System.arraycopy(maxVal, 0, distance, 0, maxVal.length);
		
		boolean pathFound = false;
		
		int head = 0;
		int tail = 0;
		
		// Add to queue
		for (int i=0; i < size; i++) {
			if (to[i]) {
				queue[tail++] = i;
				distance[i] = 0;
			}
		}
		
		while (head < tail) {
			
			// Remove from queue
			int step = queue[head++];
			if (step == from) {
				pathFound = true;
				break;
			}
			int nextDistance = distance[step] + 1;
			
			int start = (step % width == 0 ? 3 : 0);
			int end   = ((step+1) % width == 0 ? 3 : 0);
			for (int i = start; i < dirLR.length - end; i++) {

				int n = step + dirLR[i];
				if (n == from || (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance))) {
					// Add to queue
					queue[tail++] = n;
					distance[n] = nextDistance;
				}
					
			}
		}
		
		return pathFound;
	}

	//the lookahead is the target number of cells to retreat toward from our current position's
	// distance from the position we are escaping from. Returns the highest found distance, up to the lookahead
	private static int buildEscapeDistanceMap( int cur, int from, int lookAhead, boolean[] passable ) {
		
		System.arraycopy(maxVal, 0, distance, 0, maxVal.length);
		
		int destDist = Integer.MAX_VALUE;
		
		int head = 0;
		int tail = 0;
		
		// Add to queue
		queue[tail++] = from;
		distance[from] = 0;
		
		int dist = 0;
		
		while (head < tail) {
			
			// Remove from queue
			int step = queue[head++];
			dist = distance[step];
			
			if (dist > destDist) {
				return destDist;
			}
			
			if (step == cur) {
				destDist = dist + lookAhead;
			}
			
			int nextDistance = dist + 1;
			
			int start = (step % width == 0 ? 3 : 0);
			int end   = ((step+1) % width == 0 ? 3 : 0);
			for (int i = start; i < dirLR.length - end; i++) {

				int n = step + dirLR[i];
				if (n >= 0 && n < size && passable[n] && distance[n] > nextDistance) {
					// Add to queue
					queue[tail++] = n;
					distance[n] = nextDistance;
				}
					
			}
		}
		
		return dist;
	}

	// 牢的地图在代码里不是二维的而是一维的，地图本质上是一个大的矩形，矩形的横边长记为宽度，牢是将二维地图映射到一维上然后利用一维数组进行计算的
	public static void buildDistanceMap( int to, boolean[] passable ) {
		
		System.arraycopy(maxVal, 0, distance, 0, maxVal.length);

		// head表示队列首的索引、tail表示队列尾的索引
		int head = 0;
		int tail = 0;
		// 加入目标位置到队列尾
		queue[tail++] = to;
		// 先设置到目标位置的距离为0，这也就意味着目标位置是我们的出发位置，distance是记录到每点的距离的距离表
		distance[to] = 0;
		while (head < tail) {
			// 从搜索队列里取一个待搜索位点（该位点本身的距离是已知的，但是它的毗邻可能是未知的），
			// head++也就意味着该点（队列里第head个）不会再被选择了，也就是弹出了
			int step = queue[head++];
			// 把当前位点的距离再加1，这就是毗邻未搜索位点的距离了
			int nextDistance = distance[step] + 1;
			// 对地图宽度取模得到的就是列序或者说纵坐标（最左上记为（0，0））
			// 如果当前纵坐标为0，则start置3，否则为0，
			// 从后文可以看到置3也就是不检索以当前格为中心的左边一列毗邻（当前格的左上一格、正左一格、左下一格）
			// 注：这里的毗邻指的是周围一圈，而不是曼哈顿距离最小格
			// 曼哈顿距离最小格指的是直接相连的格子，曼哈顿距离描述的是离散点阵间的距离，比如正左一格的曼哈顿距离为1，左上一格则为2
			int start = (step % width == 0 ? 3 : 0);
			// 如果当前纵坐标为地图宽度-1，则end置3，否则为0，
			// 从后文可以看到置3也就是不检索以当前格为中心的右边一列毗邻（当前格的右上一格、正右一格、右下一格）
			int end   = ((step+1) % width == 0 ? 3 : 0);
			// 遍历周围8格，dirLR的定义见PathFinder.java的public static void setMapSize( int width, int height )开头处
			for (int i = start; i < dirLR.length - end; i++) {
				// n代表当前格某个方向上的毗邻一格
				int n = step + dirLR[i];
				// 0一般是最左上格，size一般是最右下格，两者一般是超出可达范围地图的，这里同时也是为了防数组越界
				// passable一般是记录地格是否是可达地格的布尔值表，其索引对应映射到地图上
				// 这里的意思是地图内上一点n，若n可达且既有距离表中记录的到达n的距离大于目前累计的距离，则更新距离表
				if (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance)) {
					// 将该点记录进搜索队列，这意味着它已经被记录过距离了，之后要检查它的毗邻
					queue[tail++] = n;
					// 该点的距离就是nextDistance
					distance[n] = nextDistance;
				}
			}
		}
	}
	
	@SuppressWarnings("serial")
	public static class Path extends LinkedList<Integer> {
	}
}
