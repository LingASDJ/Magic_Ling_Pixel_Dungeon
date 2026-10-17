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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * <div style="color:orange;font-weight:bold" >
 *     <h3>引燃</h3>
 *     <ul>
 *       <li>来源：莫洛托夫鸡尾酒炸出的爆燃区域，落地当回合以及之后每回合给区域内的单位挂5层（可叠加）。</li>
 *       <li>每回合自然减少1层，层数归零时解除。</li>
 *       <li>只要还带着引燃，燃烧/磷火缠身/霜火/地狱火的剩余时长就不再减少（冻结，但也不会补满）。</li>
 *       <li>身上没有任何燃烧类buff时，立刻获得剩余时长＝引燃层数的燃烧。</li>
 *       <li>英雄同时带着引燃和任意燃烧类buff时，每回合被点燃一件背包物品（规则同燃烧：卷轴/生肉，容器内不烧）。</li>
 *       <li>引燃本身不造成伤害，伤害来自爆燃区域。</li>
 *     </ul>
 * </div>
 *
 * <p>分散实现的效果如下
 * <ul>
 *   <li>施加— 见{@link com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BlazeZone#evolve() 爆燃区域}
 *       {@link com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BlazeZone#IGNITE_LAYERS 每回合叠加层数}</li>
 *   <li>冻结燃烧类buff— 分别写在{@link Burning#act()} {@link FrostBurning#act()}
 *       {@link HalomethaneBurning#act()} {@link HellBurning#act()} 的倒计时那一行</li>
 *   <li>补燃烧— {@link Burning#setLeft(float)}（原来的reignite只会延长，不会缩短）</li>
 *   <li>文本与图标— {@code actors.properties} 的{@code actors.buffs.ignite.*}，图标复用现成火焰帧</li>
 * </ul>
 *
 * @see Burning            燃烧
 * @see FrostBurning       霜火
 * @see HalomethaneBurning 磷火缠身
 * @see HellBurning        地狱火
 * @see com.shatteredpixel.shatteredpixeldungeon.items.potions.MolotovCocktail 莫洛托夫鸡尾酒
 * @see com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BlazeZone    爆燃区域
 */
public class Ignite extends Buff {

	// 层数上限，避免长时间泡在爆燃区域里把燃烧时长堆到很高
	public static final int MAX_LAYERS = 99;

	private int layers = 0;

	private static final String LAYERS = "layers";

	{
		type = buffType.NEGATIVE;
		announced = true;
	}

	// 燃烧类buff：身上有任意一种时，引燃就不再给它们倒计时
	public static boolean hasFireBuff( Char ch ) {
		return ch.buff(Burning.class) != null
				|| ch.buff(FrostBurning.class) != null
				|| ch.buff(HalomethaneBurning.class) != null
				|| ch.buff(HellBurning.class) != null;
	}

	public Ignite addLayers( int amount ) {
		layers = Math.min( MAX_LAYERS, layers + amount );
		return this;
	}

	public int layers() {
		return layers;
	}

	@Override
	public boolean act() {

		if (layers <= 0 || !target.isAlive()) {
			detach();
			return true;
		}

		//1. 身上没有任何燃烧类buff：补一份"剩余时长 = 引燃层数"的燃烧
		if (!hasFireBuff(target)) {
			Burning burning = Buff.affect( target, Burning.class );
			if (target.isImmune(Burning.class)) {
				//火焰免疫（如硫磺护甲）：走reignite的免疫补偿分支，本体不会被点燃
				burning.reignite( target, layers );
			} else {
				burning.setLeft( layers );
			}
		}

		//2. 英雄同时带着引燃和燃烧类buff：每回合点燃一件背包物品
		if (target instanceof Hero && hasFireBuff(target)) {
			burnInventory( (Hero)target );
		}

		//3. 每回合自然减少1层
		if (--layers <= 0) {
			detach();
			return true;
		}

		spend( TICK );
		return true;
	}

	private void burnInventory( Hero hero ) {
		if (hero.belongings.lostInventory()) return;

		ArrayList<Item> burnable = new ArrayList<>();
		//和燃烧一样：不会烧到专用包里的东西
		for (Item i : hero.belongings.backpack.items) {
			if (!i.unique && (i instanceof Scroll || i instanceof MysteryMeat || i instanceof FrozenCarpaccio)) {
				burnable.add( i );
			}
		}
		if (burnable.isEmpty()) return;

		Item toBurn = Random.element( burnable ).detach( hero.belongings.backpack );
		GLog.w( Messages.get(this, "burnsup", toBurn.title()) );
		if (toBurn instanceof MysteryMeat || toBurn instanceof FrozenCarpaccio) {
			ChargrilledMeat steak = new ChargrilledMeat();
			if (!steak.collect( hero.belongings.backpack )) {
				Dungeon.level.drop( steak, hero.pos ).sprite.drop();
			}
		}
		Heap.burnFX( hero.pos );
	}

	@Override
	public int icon() {
		// TODO 占位的
		return BuffIndicator.FIRE_DEM;
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString( layers );
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", layers);
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LAYERS, layers );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		layers = bundle.getInt( LAYERS );
	}
}
