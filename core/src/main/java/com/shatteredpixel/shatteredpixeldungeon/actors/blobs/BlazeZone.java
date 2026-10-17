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
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HellFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * <div style="color:orange;font-weight:bold" >
 *     <h3>爆燃区域</h3>
 *     <ul>
 *       <li>来源：莫洛托夫鸡尾酒投掷落地时生成，以落点为中心的5x5（半径2）圆形/方形区域。</li>
 *       <li>存在10回合，期间形状不变（不扩散、不衰减），到时间整体消失。</li>
 *       <li>落地当回合（投掷回合结束）以及之后每回合，对区域内所有单位施加5层引燃，并造成"楼层/5"点火焰伤害。</li>
 *       <li>火焰免疫的单位既不点燃也不受伤，但格子照常铺。</li>
 *       <li>不点燃可燃地形、不产生普通火焰，也烧不掉地上的物品。</li>
 *     </ul>
 * </div>
 *
 * <p>分散实现的效果如下
 * <ul>
 *   <li>区域铺设— {@link #spawn(int)}，逐格写{@code cur}，靠{@link #RADIUS 半径}控制大小</li>
 *   <li>每回合结算— {@link #evolve()}（伤害、引燃），{@link #IGNITE_LAYERS 层数}在这里用</li>
 *   <li>持续时间— {@link #TURNS}，{@link #act()} 里倒计时，归零后{@code fullyClear()}</li>
 *   <li>施加引燃— {@link Ignite#addLayers(int)}</li>
 * </ul>
 *
 * @see Ignite                                            引燃
 * @see com.shatteredpixel.shatteredpixeldungeon.items.potions.MolotovCocktail 莫洛托夫鸡尾酒
 * @see Fire                                              普通火焰（对比：会扩散、依赖可燃地形）
 */
public class BlazeZone extends Blob {

	/** 半径：2 表示以落点为中心的5x5 */
	public static final int RADIUS = 2;
	/** 爆燃区域存在回合数 */
	public static final int TURNS = 10;
	/** 每回合给区域内单位叠加的引燃层数 */
	public static final int IGNITE_LAYERS = 5;

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
				zone = Blob.seed( c, 1, BlazeZone.class );
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
			zone = Blob.seed( cell, 1, BlazeZone.class );
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
		//super.act() 里已经跑过本回合的 evolve()（叠引燃+烧血），这里只负责倒计时
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
		int damage = Math.max( 1, Dungeon.depth / 5 );
		Level level = Dungeon.level;

		for (int cell = 0; cell < cur.length; cell++) {
			if (cur[cell] <= 0) {
				off[cell] = 0;
				continue;
			}

			//本blob不扩散也不衰减：有的格子一直烧到turnsLeft走完
			off[cell] = cur[cell];
			volume += off[cell];

			if (!level.insideMap(cell) || level.solid[cell]) continue;

			Char ch = Actor.findChar( cell );
			if (ch == null || ch.isImmune(Fire.class) || ch.isImmune(BlazeZone.class)) continue;

			Buff.affect( ch, Ignite.class ).addLayers( IGNITE_LAYERS );
			ch.damage( damage, this, Char.DamageType.Element );
		}
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );     // 必须调，负责给this.emitter赋值
		emitter.pour( HellFlameParticle.FACTORY, 0.03f );
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
