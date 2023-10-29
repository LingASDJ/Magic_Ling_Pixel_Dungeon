/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FireGhostDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public abstract class ChampionEnemy extends Buff {
	//public static final float shopDURATION	= 2000000000f;
	{
		type = buffType.POSITIVE;
	}

	protected int color;

	@Override
	public int icon() {
		return BuffIndicator.CORRUPT;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(color);
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.aura( color );
		else target.sprite.clearAura();
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	public void onAttackProc(Char enemy ){

	}

	public boolean canAttackWithExtraReach( Char enemy ){
		return false;
	}

	public float meleeDamageFactor(){
		return 1f;
	}

	public float damageTakenFactor(){
		return 1f;
	}

	public float evasionAndAccuracyFactor(){
		return 1f;
	}

	public float speedFactor(){
		return 1f;
	}

	{
		immunities.add(Corruption.class);
	}

	public static void rollForStateLing(Mob m){
		if (Dungeon.mobsToStateLing <= 0) Dungeon.mobsToStateLing = 8;

		Dungeon.mobsToStateLing--;

		Class<?extends ChampionEnemy> buffCls;

		int randomNumber = Random.Int(100);

		if (randomNumber < 5) {
			buffCls = ChampionEnemy.LongSider.class;
		} else if (randomNumber < 10) {
			buffCls = ChampionEnemy.Bomber.class;
		} else {
			switch (randomNumber % 5) {
				case 0: default:
					buffCls = ChampionEnemy.Small.class;
					break;
				case 1:
					buffCls = ChampionEnemy.Middle.class;
					break;
				case 2:
					buffCls = ChampionEnemy.Big.class;
					break;
				case 3:
					buffCls = ChampionEnemy.Sider.class;
					break;
			}
		}

		if (Dungeon.mobsToStateLing <= 0 && Dungeon.isChallenged(Challenges.SBSG) && !m.properties.contains(Char.Property.NOBIG)) {
			Buff.affect(m, buffCls);
			m.state = m.WANDERING;
		}
	}


	public static class LongSider extends ChampionEnemy {
		{
			color = 0xff00ff;
		}

		@Override
		public float meleeDamageFactor() {
			return 0.9f;
		}

		@Override
		public boolean canAttackWithExtraReach(Char enemy) {
			//attack range of 2
			return target.fieldOfView[enemy.pos] && Dungeon.level.distance(target.pos, enemy.pos) <= 2;
		}


		@Override
		public void onAttackProc(Char enemy) {

			float resurrectChance = 0.1f;
			if(Random.Float() <= resurrectChance) {
				Buff.prolong( enemy, Vertigo.class, 4f);
			}
		}


	}

	public static class Sider extends ChampionEnemy implements Callback {
		{
			color = 0xe59d9d;
		}

		public static class DarkBolt{}

		public void onZapComplete() {
			next();
		}

		@Override
		public void call() {
			next();
		}

		@Override
		public boolean canAttackWithExtraReach(Char enemy) {
			//attack range of 2
			/** 实现效果，此外还要关联CharSprite.java和Mob.java以实现远程效果*/
			if(Random.Float()<0.1f) {
				switch (Random.NormalIntRange(0,6)){
					//默认为毒雾
					case 1:default:
						GameScene.add(Blob.seed(enemy.pos, 45, ToxicGas.class));
						break;
					case 2:
						GameScene.add(Blob.seed(enemy.pos, 45, CorrosiveGas.class));
						break;
					case 3:
						GameScene.add(Blob.seed(enemy.pos, 45, ConfusionGas.class));
						break;
					case 4:
						GameScene.add(Blob.seed(enemy.pos, 45, StormCloud.class));
						break;
				}
				Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
			}

			target.sprite.zaplink( enemy.pos );
			int dmg = Random.NormalIntRange( target.damageRoll()/5+3, target.damageRoll()/5+7 );
			enemy.damage( dmg, new DarkBolt() );
			return target.fieldOfView[enemy.pos] && Dungeon.level.distance(target.pos, enemy.pos) <= 3;
		}



	}

	public static class Small extends ChampionEnemy {

		{
			color = 0x8f8f8f;
		}

		@Override
		public float meleeDamageFactor() {
			return 0.75f;
		}

		@Override
		public float speedFactor() {
			return 1.3f;
		}


	}

	public static class Bomber extends ChampionEnemy {

		{
			color = 0x00FF00;
		}

		@Override
		public float meleeDamageFactor() {
			return 0.7f;
		}

		@Override
		public float speedFactor() {
			return 0.5f;
		}

		public void detach() {
			new Bomb().explodeMobs(target.pos);
			super.detach();
		}
	}

	public static class Middle extends ChampionEnemy {

		{
			color = 0xFFFF00;
		}

		@Override
		public float meleeDamageFactor() {
			return 1.25f;
		}

		@Override
		public float speedFactor() {
			return 1.25f;
		}

		@Override
		public float damageTakenFactor() {
			return 0.3f;
		}

	}



	public static class Big extends ChampionEnemy {

		{
			color = 0xFF0000;
		}

		@Override
		public float meleeDamageFactor() {
			return 1.30f;
		}
		private final float resurrectChance = 0.05f;
		@Override
		public void onAttackProc(Char enemy) {
			if(Random.Float() <= resurrectChance) {
				Buff.affect( enemy, Bleeding.class ).set( 5 );
			}
		}

		@Override
		public void detach() {

			if (Random.Float() <= resurrectChance) {
				AlarmTrap xxx = new AlarmTrap();
				xxx.pos = target.pos;
				xxx.activate();
			}

			super.detach();
		}

	}

	public static void rollForChampion(Mob m){
		if (Dungeon.mobsToChampion <= 0) Dungeon.mobsToChampion = 7;

		Dungeon.mobsToChampion--;

		//we roll for a champion enemy even if we aren't spawning one to ensure that
		//mobsToChampion does not affect levelgen RNG (number of calls to Random.Int() is constant)
		Class<?extends ChampionEnemy> buffCls;
		switch (Random.Int(8)){
			case 0: default:    buffCls = Blazing.class;      break;
			case 1:             buffCls = Projecting.class;   break;
			case 2:             buffCls = AntiMagic.class;    break;
			case 3:             buffCls = Giant.class;        break;
			case 4:             buffCls = Blessed.class;      break;
			case 5:             buffCls = Growing.class;      break;
			case 6:             buffCls = Halo.class;      	  break;
			case 7:             buffCls = DelayMob.class;     break;
			//case 8:             buffCls = King.class;     	  break;
		}

		if (Dungeon.mobsToChampion <= 0 && Dungeon.isChallenged(Challenges.CHAMPION_ENEMIES)) {
			Buff.affect(m, buffCls);
			m.state = m.WANDERING;
		}
	}

	public static class Blazing extends ChampionEnemy {

		{
			color = 0xFF8800;
		}

		@Override
		public void onAttackProc(Char enemy) {
			Buff.affect(enemy, Burning.class).reignite(enemy);
		}

		@Override
		public void detach() {
			for (int i : PathFinder.NEIGHBOURS9){
				if (!Dungeon.level.solid[target.pos+i]){
					GameScene.add(Blob.seed(target.pos+i, 2, Fire.class));
				}
			}
			super.detach();
		}

		@Override
		public float meleeDamageFactor() {
			return 1.25f;
		}

		{
			immunities.add(Burning.class);
		}
	}

	public static class Halo extends ChampionEnemy {

		{
			color = 0x00FFFF;
		}

		@Override
		public void onAttackProc(Char enemy) {
			Buff.affect(enemy, HalomethaneBurning.class).reignite(enemy);
		}

		@Override
		public float meleeDamageFactor() {
			return 1.15f;
		}

		{
			immunities.add(HalomethaneBurning.class);
			immunities.add(Burning.class);
		}
	}

	public static class DelayMob extends ChampionEnemy {

		{
			color = 0x4B66AC;
		}

		@Override
		public float meleeDamageFactor() {
			return 1.45f;
		}


		@Override
		public float damageTakenFactor() {
			return 0.4f;
		}

	}

	public static class Projecting extends ChampionEnemy {

		{
			color = 0x8800FF;
		}

		@Override
		public float meleeDamageFactor() {
			return 1.25f;
		}

		@Override
		public boolean canAttackWithExtraReach( Char enemy ) {
			return target.fieldOfView[enemy.pos]; //if it can see it, it can attack it.
		}
	}

	public static class AntiMagic extends ChampionEnemy {

		{
			color = 0x00FF00;
		}

		@Override
		public float damageTakenFactor() {
			return 0.75f;
		}

		{
			immunities.addAll(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic.RESISTS);
		}

	}

	//Also makes target large, see Char.properties()
	public static class Giant extends ChampionEnemy {

		{
			color = 0x0088FF;
		}

		@Override
		public float damageTakenFactor() {
			return 0.25f;
		}

		@Override
		public boolean canAttackWithExtraReach(Char enemy) {
			//attack range of 2
			return target.fieldOfView[enemy.pos] && Dungeon.level.distance(target.pos, enemy.pos) <= 2;
		}
	}

	public static class Blessed extends ChampionEnemy {

		{
			color = 0xFFFF00;
		}

		@Override
		public float evasionAndAccuracyFactor() {
			return 3f;
		}
	}

	public static class Growing extends ChampionEnemy {

		{
			color = 0xFF0000;
		}

		private float multiplier = 1.19f;

		@Override
		public boolean act() {
			multiplier += 0.01f;
			spend(3*TICK);
			return true;
		}

		@Override
		public float meleeDamageFactor() {
			return multiplier;
		}

		@Override
		public float damageTakenFactor() {
			return 1f/multiplier;
		}

		@Override
		public float evasionAndAccuracyFactor() {
			return multiplier;
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", (int)(100*(multiplier-1)), (int)(100*(1 - 1f/multiplier)));
		}

		private static final String MULTIPLIER = "multiplier";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(MULTIPLIER, multiplier);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			multiplier = bundle.getFloat(MULTIPLIER);
		}
	}

	public static class King extends ChampionEnemy {

		{
			color = 0xe74032;
		}

		@Override
		public int icon() {
			return BuffIndicator.FIREDIED;
		}

		@Override
		public float meleeDamageFactor() {

			return 1.45f;
		}

		@Override
		public float damageTakenFactor() {
			return 0.70f;
		}

		@Override
		public void onAttackProc(Char enemy) {
			if(Random.NormalIntRange(1,8)>2){
				new FireGhostDead().spawnAround(enemy.pos+1);
			}
		}


	}

	public static class DeadSoulSX extends ChampionEnemy {

		{
			color = 0x808080;
		}

	}


}
