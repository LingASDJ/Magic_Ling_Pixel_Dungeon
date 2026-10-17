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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune.isMagicImmuned;

import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.props.BlockingDrug;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Dirt_KnifeStand;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.UnlessFlower;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ChaoticCenser;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.SaltCube;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

// dot血量处理器
public class Regeneration extends Buff {

	{
		// 与其它 buff 不同，这个 buff 在英雄行动之后结算，并优先于其它效果处理
		// 如果能在挨打之前先结算掉一部分回血，回血的收益要大得多
		actPriority = HERO_PRIO - 1;
	}

	private static final String PARTIALREGEN = "partialRegen";
	// 未结算的回血值：正=回血，负=掉血，绝对值攒到 ≥1 才结算到 HP 上
	private float partialRegen = 0f;
	private float baseHealthRegenRate = 0.1f;

	@Override
	public boolean act() {
		if (target != null && target.isAlive()) {

			if (target instanceof Hero) {

				// TODO 香炉的实现，无需关心，与回血无关，可能存在一些问题
				if (ChaoticCenser.averageTurnsUntilGas() != -1) {
					Buff.affect(Dungeon.hero, ChaoticCenser.CenserGasTracker.class);
				}

				float healthRegenRate = baseHealthRegenRate;

				// ========== 加算区 ==========

				// 菊寿凝身buff的效果
				MarigoldBuff marigoldBuff = target.buff(MarigoldBuff.class);
				if (marigoldBuff != null) healthRegenRate += marigoldBuff.ExtendRegen();

				// 蓄血圣杯buff的加算效果
				ChaliceOfBlood.chaliceRegen regenBuff = target.buff(ChaliceOfBlood.chaliceRegen.class);
				if (regenBuff != null) healthRegenRate += regenBuff.RegenRateModifierAddValue();

				// ========== 乘算区 ==========

				// 蓄血圣杯buff的乘算效果
				if (regenBuff != null) healthRegenRate *= regenBuff.RegenRateModifierMultiplierValue();

				// 盐晶立方体的效果
				healthRegenRate *= SaltCube.healthRegenMultiplier();

				// 阻断药物

				if (((Hero) target).belongings.getItem(BlockingDrug.class) != null) healthRegenRate *= 0.8f;

				// ========== 额外加算区 ==========

				// 暂无

				// ========== 结算区 ==========

				// 可回血时，回血量不管正负值都加
				// 不可回血时加负值，正值归0且回血值也归0
				if (!canRegen() && healthRegenRate > 0f) {
					healthRegenRate = 0f;
					if (partialRegen > 0f) partialRegen = 0f;
				} else partialRegen += healthRegenRate;

				settle();

				spend(1f);
			}

			else if (Dungeon.isDLC(Conducts.Conduct.DEV)) GLog.n("存在非英雄单位进入Regeneration");

			diactivate();

		} else {

			diactivate();

		}

		return true;
	}

	// 可回血判据
	private boolean canRegen() {
		// 土刀架的放宽饱食度对自然回血的约束效果（不饿回血 → 不饿或只有一点饿回血）
		boolean dirtKnifeStand = hero.belongings.getItem(Dirt_KnifeStand.class) != null;
		// 幽寂花环的效果
		if (target.buff(UnlessFlower.UnlessFlowerTime.class) != null) return false;
		// 血上限效果
		if (target.HP >= regencap()) return false;
		// 极度饥饿
		if (((Hero) target).isStarving()) return false;
		// 有一点饿
		if (dirtKnifeStand && ((Hero) target).isSmallHunger()) return false;
		// 背水一战
		return regenOn();
	}

	// 回血值结算（支持负数回血，也就是说dot伤害也可以依赖于此实现）
	// TODO 但是这一实现需要完善或接入damage，但不建议这么做，会造成较多的耦合
	private void settle() {
		int whole = (int) partialRegen;
		if (whole == 0) return;
		partialRegen -= whole;

		if (whole > 0) {
			target.HP = Math.min(target.HP + whole, regencap());
			// 英雄满血时停止睡觉
			if (target.HP == regencap() && target instanceof Hero) {
				((Hero) target).resting = false;
			}
		} else {
			target.HP = Math.max(1, target.HP + whole);
		}
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(PARTIALREGEN, partialRegen);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		partialRegen = bundle.getFloat(PARTIALREGEN);
	}

	public int regencap(){
		return target.HT;
	}

	public static boolean regenOn(){
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null && !lock.regenOn()){
			return false;
		}
		return true;
	}
}
