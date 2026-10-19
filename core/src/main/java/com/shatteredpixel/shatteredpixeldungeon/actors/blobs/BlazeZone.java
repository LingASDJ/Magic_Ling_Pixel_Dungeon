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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ignite;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlazeZonParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * <div style="color:orange;font-weight:bold" >
 *     <h3>爆燃区域</h3>
 *     <ul>
 *       <li>来源：莫洛托夫鸡尾酒投掷落地时生成，以落点为中心的5x5（半径2）方形区域。</li>
 *       <li>存在10回合，中心区域形状不变（不衰减），到时间整体消失。</li>
 *       <li>落地当回合以及之后每回合，对中心区域内所有单位施加5层引燃，并造成"楼层/5"点火焰伤害。</li>
 *       <li>参考 {@link Fire}：火会向相邻的可燃地形蔓延，蔓延出去的火逐层衰减（4层起，每回合-1），
 *           烧尽且地形可燃时会摧毁该地形（烧穿木门、烧光草丛）。</li>
 *       <li>同时烧掉区域内地上的物品堆（卷轴烧毁、露水蒸发）与植物（枯萎）。</li>
 *       <li>火焰免疫的单位既不点燃也不受伤，但格子照常铺。</li>
 *     </ul>
 * </div>
 *
 * <p>分散实现的效果如下
 * <ul>
 *   <li>区域铺设— {@link #spawn(int)}，逐格写{@code cur}，靠{@link #RADIUS 半径}控制大小</li>
 *   <li>每回合结算— {@link #evolve()}（中心格：引燃+伤害+烧物品/植物；蔓延格：衰减+烧物品/植物/地形）</li>
 *   <li>持续时间— {@link #TURNS}，{@link #act()} 里倒计时，归零后{@link #fullyClear()}</li>
 *   <li>施加引燃— {@link Ignite#addLayers(int)}</li>
 *   <li>蔓延与烧毁— 参考 {@link Fire#evolve()}，相邻可燃格被引燃，烧尽摧毁地形</li>
 * </ul>
 *
 * @see Ignite                                            引燃
 * @see com.shatteredpixel.shatteredpixeldungeon.items.potions.MolotovCocktail 莫洛托夫鸡尾酒
 * @see Fire                                              普通火焰（蔓延/衰减/烧物品/烧地形的参考实现）
 */
public class BlazeZone extends Blob {

	/** 半径：2 表示以落点为中心的5x5 */
	public static final int RADIUS = 2;
	/** 爆燃区域存在回合数 */
	public static final int TURNS = 10;
	/** 每回合给中心区域内单位叠加的引燃层数 */
	public static final int IGNITE_LAYERS = 5;

	/**
	 * 中心爆燃格的层数标记：{@link #spawn(int)} 与 {@link #ignite(List, int)} 铺出的格子用这个值，
	 * 在 {@link #evolve()} 里不衰减、持续到 {@link #turnsLeft} 走完。
	 * 取值必须大于 {@link #SPREAD_FIRE}，避免和蔓延格的递减路径撞值。
	 */
	public static final int CENTER_FIRE = 5;
	/** 蔓延格初始层数：从中心火蔓延到相邻可燃地形时用，每回合 -1，烧尽且可燃则摧毁地形。 */
	public static final int SPREAD_FIRE = 4;

	private int turnsLeft = TURNS;

	private static final String TURNS_LEFT = "turnsLeft";

	/** 对外只暴露这一个入口：以cell为中心铺一片爆燃区域 */
	public static BlazeZone spawn( int cell ) {
		Level level = Dungeon.level;
		int width = level.width();
		int centerX = cell % width;
		int centerY = cell / width;

		BlazeZone zone = null;

		for (int y = centerY - RADIUS; y <= centerY + RADIUS; y++) {
			if (y < 0 || y >= level.height()) continue;
			for (int x = centerX - RADIUS; x <= centerX + RADIUS; x++) {
				if (x < 0 || x >= width) continue;
				int c = x + y * width;
				if (level.solid[c]) continue;    //墙里不用点火
				zone = Blob.seed( c, CENTER_FIRE, BlazeZone.class );
			}
		}

		if (zone != null) GameScene.add( zone );
		return zone;
	}

	/**
	 * 酒气遇火爆燃专用：把给定的这一批格子直接点成爆燃区域（不铺5x5、不按面积重算），
	 * 持续时间取传入的回合数。同一层已经有区域在烧时，取两者中剩得久的那个。
	 */
	public static BlazeZone ignite( List<Integer> cells, int turns ) {
		ArrayList<Integer> valid = new ArrayList<>();
		for (int cell : cells) {
			if (Dungeon.level.insideMap( cell ) && !Dungeon.level.solid[cell]) valid.add( cell );
		}
		if (valid.isEmpty()) return null;

		BlazeZone existing = (BlazeZone)Dungeon.level.blobs.get( BlazeZone.class );
		boolean fresh = (existing == null || existing.volume == 0);
		int remaining = fresh ? turns : Math.max( existing.turnsLeft, turns );

		BlazeZone zone = null;
		for (int cell : valid) {
			zone = Blob.seed( cell, CENTER_FIRE, BlazeZone.class );
		}

		if (zone != null) {
			zone.turnsLeft = remaining;
			GameScene.add( zone );
		}
		return zone;
	}

	@Override
	public void seed( Level level, int cell, int amount ) {
		boolean fresh = (cur == null || volume == 0);
		super.seed( level, cell, amount );
		//新的一片区域重新计时；已经在烧的旧区域不会因为再扔一瓶而刷新
		if (fresh) turnsLeft = TURNS;
	}

	@Override
	public boolean act() {
		//super.act() 里已经跑过本回合的 evolve()（叠引燃+烧血+蔓延），这里只负责倒计时
		boolean cont = super.act();

		if (turnsLeft > 0) {
			turnsLeft--;
			//第10回合结算完就整体熄灭；之后本blob只是挂在actor表里空转，不再重复clear
			if (turnsLeft == 0) fullyClear();
		}

		return cont;
	}

	@Override
	protected void evolve() {
		boolean[] flamable = Dungeon.level.flamable;
		int width = Dungeon.level.width();
		int length = Dungeon.level.length();
		int damage = Math.max( 1, Dungeon.depth / 5 );
		Level level = Dungeon.level;
		boolean observe = false;

		//参考 Fire：遍历 area 包围盒多一圈，这样外围的可燃格才能被检查并蔓延
		for (int i = area.left - 1; i <= area.right; i++) {
			for (int j = area.top - 1; j <= area.bottom; j++) {
				int cell = i + j * width;

				if (cell < 0 || cell >= length) continue;
				if (!level.insideMap( cell )) continue;

				if (cur[cell] > 0) {
					if (cur[cell] == CENTER_FIRE) {
						//中心爆燃格：不衰减，每回合保持火
						off[cell] = CENTER_FIRE;
						volume += CENTER_FIRE;
						//爆燃效果：5层引燃 + 楼层/5 伤害 + 烧物品/植物
						burnEffects( cell, IGNITE_LAYERS, damage );
					} else {
						//蔓延格：逐层衰减
						int fire = cur[cell] - 1;
						off[cell] = fire;
						volume += fire;
						//蔓延格也挂1层引燃、烧物品/植物，但不直接造成爆燃伤害
						burnEffects( cell, 1, 0 );
						//火灭了且地形可燃：烧掉地形
						if (fire <= 0 && flamable[cell]) {
							level.destroy( cell );
							observe = true;
							GameScene.updateMap( cell );
						}
					}
				} else if (flamable[cell]) {
					//无火但可燃：检查四邻是否有 BlazeZone 火，有就蔓延过来
					boolean hasAdjacentFire = false;
					if (cell % width != 0 && cur[cell - 1] > 0) {
						hasAdjacentFire = true;
					} else if (cell % width != width - 1 && cur[cell + 1] > 0) {
						hasAdjacentFire = true;
					} else if (cell >= width && cur[cell - width] > 0) {
						hasAdjacentFire = true;
					} else if (cell < length - width && cur[cell + width] > 0) {
						hasAdjacentFire = true;
					}

					if (hasAdjacentFire) {
						off[cell] = SPREAD_FIRE;
						volume += SPREAD_FIRE;
						//刚蔓延点燃：挂1层引燃 + 烧物品/植物
						burnEffects( cell, 1, 0 );
						area.union( i, j );
					} else {
						off[cell] = 0;
					}
				} else {
					off[cell] = 0;
				}
			}
		}

		if (observe) {
			Dungeon.observe();
		}
	}

	/**
	 * 对格子施加燃烧副作用：烧地上物品堆、烧植物、给单位挂引燃。
	 * @param igniteLayers 给单位叠的引燃层数
	 * @param damage       直接伤害（<=0 表示不造成直接伤害）
	 */
	private void burnEffects( int cell, int igniteLayers, int damage ) {
		//烧地上的物品堆（卷轴烧毁、露水蒸发）——参考 Fire.burn
		Heap heap = Dungeon.level.heaps.get( cell );
		if (heap != null) {
			heap.burn();
		}
		//烧植物——参考 Fire.burn
		Plant plant = Dungeon.level.plants.get( cell );
		if (plant != null) {
			plant.wither();
		}
		//给单位挂引燃
		Char ch = Actor.findChar( cell );
		if (ch == null || ch.isImmune( Fire.class ) || ch.isImmune( BlazeZone.class )) return;
		Buff.affect( ch, Ignite.class ).addLayers( igniteLayers );
		if (damage > 0) {
			ch.damage( damage, this, Char.DamageType.Element );
		}
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );     // 必须调，负责给this.emitter赋值
		emitter.pour(BlazeZonParticle.FACTORY, 0.03f );
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( TURNS_LEFT, turnsLeft );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		turnsLeft = bundle.getInt( TURNS_LEFT );
	}
}
