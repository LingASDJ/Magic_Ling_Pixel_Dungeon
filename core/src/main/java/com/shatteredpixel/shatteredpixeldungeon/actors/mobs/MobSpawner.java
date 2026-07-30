/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DHXD;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.MOREROOM;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.RLPT;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.AnomaloCaris;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.ThreeLeafBug;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.Turtle;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.dragon.PiraLand;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.dragon.RiceRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.Artillerist;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.BoomSkull;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.DemonLord;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.Drake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.GiantWorm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.GnollBlind;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.GnollThrower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.GnollTwilight;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.GoblinShaman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.Gorgon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.HermitCrab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.Mayfly;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.Prisoner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.RoyalGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.TribemanOld;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.ApprenticeWitch;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Butcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Crumb;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Frankenstein;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Ghost_Halloween;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.PumkingBomber;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Pumking_Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.allsearch.ShadowHunstman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.BloodsSwarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.DM111;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.DeadEye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.GhoulPlus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.GiantFlowerSlime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.GuardCapital;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.NewBornCrab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.VeryColdRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.WarlockHead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rlpt.DrTerror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rlpt.GunHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.GraveRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroAcolyte;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroArcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroPioneer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroScout;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.SkeletonDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.SmallSkeletonDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.ThiefSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Wisp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Worm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.WormWhyHuman;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.RatSkull;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.utils.MobsUtilsRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class MobSpawner extends Actor {
	{
		actPriority = BUFF_PRIO; //as if it were a buff.
	}

	@Override
	protected boolean act() {
		if (Dungeon.level.mobCount() < Dungeon.level.mobLimit()) {
			if (Dungeon.level.spawnMob(12)){
				spend(Dungeon.level.respawnCooldown());
			} else {
				//try again in 1 turn
				spend(TICK);
			}
		} else {
			spend(Dungeon.level.respawnCooldown());
		}
		return true;
	}

	public void resetCooldown(){
		spend(-cooldown());
		spend(Dungeon.level.respawnCooldown());
	}

	/**
	 * 统一入口：按优先级匹配不同模式怪物池
	 * 优先级：Hollow假日 > BossRush > 随机模式RandMode > RLPT挑战 > 标准普通模式
	 */
	public static ArrayList<Class<? extends Mob>> getMobRotation(int depth ){
		ArrayList<Class<? extends Mob>> mobs;

		if(Dungeon.depth == 33 && Statistics.Hollow_Holiday) {
			mobs = getNormalMobPool(depth);
		} else if(Statistics.Tomb_Reach && Dungeon.depth >= 11 && Dungeon.depth <= 20){
			mobs = getTombMobPool(depth);
		} else if(Statistics.bossRushMode){
			mobs = getBossRushMobPool(depth);
		} else if(Statistics.RandMode){
			mobs = getRandomModeMobPool(depth);
		} else if(Dungeon.isChallenged(RLPT) && Dungeon.depth >= 6){
			mobs = getRlptMobPool(depth);
		} else {
			mobs = getNormalMobPool(depth);
		}

		//所有模式统一后置处理
		addRareMobs(depth, mobs);
		swapMobAlts(mobs);
		Random.shuffle(mobs);
		return mobs;
	}

	// ===================== 公共工具：特殊分支怪物池 =====================
	/**
	 * 判断当前是否为特殊子分支，返回对应怪物列表；无特殊分支返回null
	 */
	private static ArrayList<Class<? extends Mob>> getSpecialBranchMobPool(){
		if(Dungeon.branch == 0 || Statistics.bossRushMode){
			return null;
		}
		int branch = Dungeon.branch;
		int depth = Dungeon.depth;

		switch (branch){
			case 1:
				if(depth == 17 || depth == 18){
					return new ArrayList<>(Arrays.asList(
							Turtle.class,Turtle.class,Turtle.class,Turtle.class,Turtle.class,Turtle.class, Turtle.class));
				}
				break;
			case 2:
				if(depth == 17 || depth == 18){
					return new ArrayList<>(Arrays.asList(
							Turtle.class, ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class,ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class,ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class,ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class,ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class,ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class));
				}
				break;
			case 3:
				if(depth == 11 || depth == 12|| depth == 13|| depth == 14){
					return new ArrayList<>(Arrays.asList(PiraLand.class, RiceRat.class));
				}
				if(depth == 31){
					return new ArrayList<>(
							Arrays.asList(
									MobsUtilsRoom.GreenSlingSP.class,
									MobsUtilsRoom.DM275RPG_SP.class,
									MobsUtilsRoom.FlameC02SP.class,
									ShadowHunstman.class,
									ApprenticeWitch.class,
									Butcher.class,
									Pumking_Ghost.class,
									PumkingBomber.class
							));
				}
				break;
			case 5:
				if(depth == 17){
					return new ArrayList<>(Arrays.asList(BlueWraith.class,TormentedSpirit.class));
				}
				break;
		}
		return null;
	}

	// ===================== 1. 标准普通模式怪物池 =====================
	private static ArrayList<Class<? extends Mob>> getNormalMobPool(int depth) {
		ArrayList<Class<? extends Mob>> branchList = getSpecialBranchMobPool();
		if(branchList != null) return branchList;

		ArrayList<Class<? extends Mob>> res;
		switch (depth){
			case 1:
				res = new ArrayList<>(Arrays.asList(Rat.class, Rat.class));
				break;
			case 2:
				res = new ArrayList<>(Arrays.asList(FlowerSlime.class,
						FlowerSlime.class, FlowerSlime.class, Gnoll.class, Gnoll.class,
						Gnoll.class, Gnoll.class));
				break;
			case 3:
				res = new ArrayList<>(Arrays.asList(FlowerSlime.class,
						FlowerSlime.class, Katydid.class,
						Katydid.class, FlowerSlime.class, FlowerSlime.class,
						ClearElemental.class,Crab.class,Swarm.class));
				break;
			case 4:
				res = new ArrayList<>(Arrays.asList(Katydid.class,
						ClearElemental.class, Slime_Red.class,
						Slime_Orange.class, Swarm.class,Crab.class));
				break;
			case 5:
				res = new ArrayList<>(Arrays.asList(FlowerSlime.class, FlowerSlime.class,Slime.class,Slime.class,Swarm.class,Crab.class));
				break;
			case 6:
				res = new ArrayList<>(Arrays.asList(Skeleton.class,
						Skeleton.class,BrownBat.class,BrownBat.class,Thief.class,Thief.class));
				break;
			case 7:
				res = new ArrayList<>(Arrays.asList(Skeleton.class,
						Thief.class,DM100.class,Necromancer.class,Guard.class,DM100.class));
				break;
			case 8:
				res = new ArrayList<>(Arrays.asList(Skeleton.class,
						Skeleton.class,
						Thief.class, Guard.class,Necromancer.class, DM100.class));
				break;
			case 9:
				res = new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
						Thief.class,Thief.class,BrownBat.class,BrownBat.class, DM100.class,
						BrownBat.class,Thief.class,Thief.class));
				break;
			case 10:
				res = new ArrayList<>(Arrays.asList(Bat.class,
						Brute.class, Brute.class,
						Necromancer.class,Necromancer.class));
				break;
			case 11:
				res = new ArrayList<>(Arrays.asList(Bat.class, DM200.class,DM200.class,ColdMagicRat.class,
						ColdMagicRat.class));
				break;
			case 12:
				res = new ArrayList<>(Arrays.asList(
						Bat.class,
						ColdMagicRat.class,FireGhost.class,ColdMagicRat.class));
				break;
			case 13:
				res = new ArrayList<>(Arrays.asList(
						Bat.class,
						Brute.class,Spinner.class,
						DM200.class,RedSwarm.class));
				break;
			case 14:
				res = new ArrayList<>(Arrays.asList(
						Bat.class,
						Spinner.class,
						DM200.class,Shaman.random()));
				break;
			case 15:
				res = new ArrayList<>(Arrays.asList(
						Bat.class,
						Spinner.class,
						Brute.class,FireGhost.class,ColdMagicRat.class,RedSwarm.class));
				break;
			case 16:
				res = new ArrayList<>(Arrays.asList(Monk.class,Ghoul.class));
				break;
			case 17:
				res = new ArrayList<>(Arrays.asList(Elemental.random(),Monk.class,Ghoul.class));
				break;
			case 18:
				res = new ArrayList<>(Arrays.asList(
						Elemental.random(),
						Random.NormalFloat(1,10)>=3 ? BruteBot.class : Warlock.class,
						Monk.class,
						Golem.class,FireGhost.class));
				break;
			case 19:
				res = new ArrayList<>(Arrays.asList(
						Monk.class,
						Golem.class, Warlock.class,ShieldHuntsman.class,
						Random.NormalFloat(1,10)>3 ? BruteBot.class : Warlock.class));
				break;
			case 20:
				res = new ArrayList<>(Arrays.asList(
						Elemental.random(),
						Random.NormalFloat(1,10)>3 ? BruteBot.class : Warlock.class,
						Monk.class,
						Golem.class, Golem.class,Ice_Scorpio.class));
				break;
			case 21:
				if (Statistics.bossRushMode) {
					res = new ArrayList<>(Arrays.asList(
							PiraLand.class, PiraLand.class, PhantomPiranha.class,
							RiceRat.class, RiceRat.class, PhantomPiranha.class));
				} else {
					res = new ArrayList<>(Arrays.asList(Eye.class, Scorpio.class, Fire_Scorpio.class));
				}
				break;
			case 22:
				res = new ArrayList<>(Arrays.asList(Eye.class, Ice_Scorpio.class,Fire_Scorpio.class));
				break;
			case 23:
				res = new ArrayList<>(Arrays.asList(Eye.class,ShieldHuntsman.class,Ice_Scorpio.class));
				break;
			case 24:
				res = new ArrayList<>(Arrays.asList(
						Succubus.class,
						Eye.class,
						Scorpio.class, Succubus.class,Fire_Scorpio.class,Ice_Scorpio.class,ShieldHuntsman.class));
				break;
			case 27:
				res = new ArrayList<>(Arrays.asList(
						Frankenstein.class, Frankenstein.class,
						Crumb.class, Crumb.class,
						Frankenstein.class,
						Crumb.class, Crumb.class,
						Eye.class));
				break;
			case 28:
				res = new ArrayList<>(Arrays.asList(
						Crumb.class, Crumb.class,
						Ghost_Halloween.class,Ghost_Halloween.class,
						Pumking_Ghost.class
				));
				break;
			case 29:
				res = new ArrayList<>(Arrays.asList(
						ApprenticeWitch.class,
						Butcher.class,Butcher.class,
						PumkingBomber.class, Pumking_Ghost.class
				));
				break;
			case 30:
				res = new ArrayList<>(Arrays.asList(
						ApprenticeWitch.class,
						Butcher.class,Butcher.class,
						Pumking_Ghost.class
						, PumkingBomber.class
				));
				break;
			case 32: case 34: case 36: case 37:case 38:
			case 40: case 41:
				if(Statistics.bossRushMode){
					res = getBossRushMobPool(depth);
				}else{
					res = new ArrayList<>(Arrays.asList(
							ApprenticeWitch.class,
							ApprenticeWitch.class,
							Butcher.class,Butcher.class,
							Pumking_Ghost.class
							, PumkingBomber.class
					));
				}
				break;
			default:
				res = new ArrayList<>(Arrays.asList(FlowerSlime.class, FlowerSlime.class,
						FlowerSlime.class,
						FlowerSlime.class, FlowerSlime.class, FlowerSlime.class,
						FlowerSlime.class, FlowerSlime.class, FlowerSlime.class,
						FlowerSlime.class));
				break;
		}
		return res;
	}

	// ===================== 2. 随机全怪 RandMode 怪物池 =====================
	private static ArrayList<Class<? extends Mob>> getRandomModeMobPool(int depth) {
		ArrayList<Class<? extends Mob>> branchList = getSpecialBranchMobPool();
		if(branchList != null) return branchList;

		ArrayList<Class<? extends Mob>> res;
		switch (depth) {
			case 1:
				res = new ArrayList<>(Arrays.asList(Rat.class, GnollBlind.class));
				break;
			case 2:
				res = new ArrayList<>(Arrays.asList(FlowerSlime.class,
						Rat.class, OGPDZSLS.class, OGPDLLS.class,
						GnollBlind.class, Mayfly.class));
				break;
			case 3:
				res = new ArrayList<>(Arrays.asList(FlowerSlime.class,
						FlowerSlime.class,
						Slime_Qs.class, Katydid.class, GiantWorm.class, Mayfly.class,
						ClearElemental.class,Crab.class,Swarm.class, HermitCrab.class));
				break;
			case 4:
				res = new ArrayList<>(Arrays.asList(Katydid.class,
						Slime_Sn.class, Slime_Red.class, HermitCrab.class, HermitCrab.class,
						Slime_Orange.class,  GiantWorm.class, Mayfly.class,Crab.class));
				break;
			case 5:
				res = new ArrayList<>(Arrays.asList(FlowerSlime.class, FlowerSlime.class,Slime.class,Slime.class,Swarm.class,Crab.class));
				break;
			case 6:
				res = new ArrayList<>(Arrays.asList(Skeleton.class,
						Skeleton.class,BrownBat.class,BrownBat.class,Thief.class,Thief.class));
				break;
			case 7:
				res = new ArrayList<>(Arrays.asList(Skeleton.class,
						Thief.class, BoomSkull.class, Prisoner.class,Necromancer.class,Guard.class,DM100.class,
						GnollThrower.class));
				break;
			case 8:
				res = new ArrayList<>(Arrays.asList(Skeleton.class,
						Skeleton.class,
						Thief.class, Guard.class, BoomSkull.class, Prisoner.class,GnollThrower.class,GnollThrower.class));
				break;
			case 9:
				res = new ArrayList<>(Arrays.asList(
						Skeleton.class, Prisoner.class, Skeleton.class,
						Thief.class,Thief.class,BrownBat.class,
						Guard.class, DM100.class,GnollThrower.class));
				break;
			case 10:
				res = new ArrayList<>(Arrays.asList(Bat.class,
						Brute.class, Brute.class,
						Necromancer.class,Necromancer.class));
				break;
			case 11:
				res = new ArrayList<>(Arrays.asList(Bat.class, Drake.class,DM200.class,ColdMagicRat.class));
				break;
			case 12:
				res = new ArrayList<>(Arrays.asList(
						Bat.class,DM200.class,
						ColdMagicRat.class, Drake.class , GoblinShaman.random()));
				break;
			case 13:
				res = new ArrayList<>(Arrays.asList(
						GnollTwilight.class,DM200.class,GoblinShaman.random(),
						GoblinShaman.random(),
						Brute.class,Spinner.class, TribemanOld.class,
						Drake.class,RedSwarm.class));
				break;
			case 14:
				res = new ArrayList<>(Arrays.asList(
						Bat.class,Drake.class,GnollTwilight.class,GnollTwilight.class,
						Spinner.class,TribemanOld.class,
						ColdMagicRat.class,RedSwarm.class,Shaman.random()));
				break;
			case 15:
				res = new ArrayList<>(Arrays.asList(
						Bat.class,
						Spinner.class,
						Brute.class,FireGhost.class,ColdMagicRat.class,RedSwarm.class));
				break;
			case 16:
				res = new ArrayList<>(Arrays.asList(Monk.class,Ghoul.class, Artillerist.class));
				break;
			case 17:
				res = new ArrayList<>(Arrays.asList(Ghoul.class,Monk.class,FireGhost.class, Artillerist.class));
				break;
			case 18:
				res = new ArrayList<>(Arrays.asList(
						Ghoul.class,
						Random.NormalFloat(1,10)>=3 ? BruteBot.class : Warlock.class,
						Monk.class, RoyalGuard.class,
						Golem.class,FireGhost.class, Artillerist.class));
				break;
			case 19:
				res = new ArrayList<>(Arrays.asList(
						Monk.class,RoyalGuard.class,RoyalGuard.class,
						Golem.class, Warlock.class,ShieldHuntsman.class,
						Random.NormalFloat(1,10)>3 ? BruteBot.class : Warlock.class, Artillerist.class));
				break;
			case 20:
				res = new ArrayList<>(Arrays.asList(
						Elemental.random(),
						Random.NormalFloat(1,10)>3 ? BruteBot.class : Warlock.class,
						Monk.class,
						Golem.class, Golem.class,Ice_Scorpio.class, Fire_Scorpio.class));
				break;
			case 21:
				if (Statistics.bossRushMode) {
					res = new ArrayList<>(Arrays.asList(
							PiraLand.class, PiraLand.class, PhantomPiranha.class,
							RiceRat.class, RiceRat.class, PhantomPiranha.class));
				} else {
					res = new ArrayList<>(Arrays.asList(Eye.class, ShieldHuntsman.class, Fire_Scorpio.class));
				}
				break;
			case 22:
				res = new ArrayList<>(Arrays.asList(Eye.class,ShieldHuntsman.class, Gorgon.class));
				break;
			case 23:
				res = new ArrayList<>(Arrays.asList(
						Eye.class,ShieldHuntsman.class,Ice_Scorpio.class
						, Fire_Scorpio.class,DemonLord.class, DemonLord.class,
						Gorgon.class));
				break;
			case 24:
				res = new ArrayList<>(Arrays.asList(
						Succubus.class,
						Eye.class, DemonLord.class,
						Scorpio.class, Succubus.class,Fire_Scorpio.class,Ice_Scorpio.class,ShieldHuntsman.class));
				break;
			case 27:
				res = new ArrayList<>(Arrays.asList(
						Frankenstein.class,
						Crumb.class, Crumb.class,
						Frankenstein.class,
						Crumb.class, Crumb.class,
						Eye.class));
				break;
			case 28:
				res = new ArrayList<>(Arrays.asList(
						Crumb.class, Crumb.class,
						Ghost_Halloween.class,Ghost_Halloween.class,
						Pumking_Ghost.class, Frankenstein.class
				));
				break;
			case 29:
				res = new ArrayList<>(Arrays.asList(
						ApprenticeWitch.class,
						Butcher.class,Butcher.class,
						PumkingBomber.class, Pumking_Ghost.class
				));
				break;
			case 30:
				res = new ArrayList<>(Arrays.asList(
						ApprenticeWitch.class,
						Butcher.class,Butcher.class,
						Pumking_Ghost.class
						, PumkingBomber.class
				));
				break;
			default:
				res = new ArrayList<>(Arrays.asList(FlowerSlime.class, FlowerSlime.class,
						FlowerSlime.class,
						FlowerSlime.class, FlowerSlime.class, FlowerSlime.class,
						FlowerSlime.class, FlowerSlime.class, FlowerSlime.class,
						FlowerSlime.class));
				break;
		}
		return res;
	}

	// ===================== 3. RLPT挑战模式怪物池 =====================
	private static ArrayList<Class<? extends Mob>> getRlptMobPool(int depth) {
		ArrayList<Class<? extends Mob>> branchList = getSpecialBranchMobPool();
		if(branchList != null) return branchList;

		ArrayList<Class<? extends Mob>> res;
		switch (depth) {
			case 6:
				res = new ArrayList<>(Arrays.asList(Skeleton.class,
						Skeleton.class,BrownBat.class,BrownBat.class,Thief.class,Thief.class));
				break;
			case 7:
				res = new ArrayList<>(Arrays.asList(Skeleton.class,
						Thief.class,DM100.class,Necromancer.class,Guard.class));
				break;
			case 8: case 9:
				int rand = Random.Int(3);
				if(rand == 0){
					res = new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
							Thief.class,Thief.class,BrownBat.class,
							Spinner.class,Shaman.random(), KagenoNusujin.class));
				}else if(rand == 1){
					res = new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
							Thief.class,Thief.class,Thief.class,
							DM100.class,Necromancer.class,Necromancer.class,
							DM100.class, KagenoNusujin.class));
				}else{
					res = new ArrayList<>(Arrays.asList(
							Skeleton.class, Skeleton.class, Skeleton.class,
							Thief.class,Thief.class,BrownBat.class, DM100.class,
							Spinner.class, Necromancer.class,
							Necromancer.class,
							Brute.class,
							Bat.class));
				}
				break;
			case 11:
				res = new ArrayList<>(Arrays.asList(Bat.class,DM100.class,ColdMagicRat.class,
						ColdMagicRat.class));
				break;
			case 12:
				res = new ArrayList<>(Arrays.asList(
						Bat.class,
						ColdMagicRat.class, GnollGuard.class));
				break;
			case 13: case 14:
				int r4 = Random.Int(4);
				if(r4 == 0){
					res = new ArrayList<>(Arrays.asList(Bat.class, GunHuntsman.class,DM100.class,ColdMagicRat.class,
							ColdMagicRat.class));
				}else{
					res = new ArrayList<>(Arrays.asList(
							Elemental.random(),
							Monk.class, Monk.class,
							Golem.class));
				}
				break;
			case 16:
				res = new ArrayList<>(Arrays.asList(Monk.class,Ghoul.class));
				break;
			case 17:
				res = new ArrayList<>(Arrays.asList(Elemental.random(),Monk.class,FireGhost.class));
				break;
			case 18: case 19:
				int r6 = Random.Int(6);
				if(r6 == 2){
					res = new ArrayList<>(Arrays.asList(Scorpio.class,Monk.class,Ghoul.class));
				}else if(r6 == 3){
					res = new ArrayList<>(Arrays.asList(Fire_Scorpio.class,Monk.class,Golem.class));
				}else if(r6 == 5){
					res = new ArrayList<>(Arrays.asList(
							Eye.class, Eye.class,
							Scorpio.class, Eye.class,Succubus.class,Monk.class,
							Golem.class));
				}else{
					res = new ArrayList<>(Arrays.asList(
							ShieldHuntsman.class,
							Monk.class,
							Golem.class,Ghoul.class));
				}
				break;
			case 21: case 22:
				res = new ArrayList<>(Arrays.asList(Eye.class,ShieldHuntsman.class));
				break;
			case 23:
				res = new ArrayList<>(Arrays.asList(Eye.class,ShieldHuntsman.class));
				break;
			case 24:
				res = new ArrayList<>(Arrays.asList(
						Succubus.class,
						Eye.class,
						Scorpio.class, Succubus.class,Ice_Scorpio.class,ShieldHuntsman.class));
				break;
			case 26:
				res = new ArrayList<>(Arrays.asList(Frankenstein.class, Crumb.class));
				break;
			case 27:
				res = new ArrayList<>(Arrays.asList(Frankenstein.class, Crumb.class,Crumb.class));
				break;
			case 28:
				res = new ArrayList<>(Arrays.asList(ApprenticeWitch.class, Crumb.class));
				break;
			case 29:
				res = new ArrayList<>(Arrays.asList(Ghost_Halloween.class, PumkingBomber.class, ApprenticeWitch.class));
				break;
			case 30:
				res = new ArrayList<>(Arrays.asList(
						ApprenticeWitch.class,
						Butcher.class,Butcher.class,
						Pumking_Ghost.class
						, PumkingBomber.class
				));
				break;
			default:
				res = new ArrayList<>(Arrays.asList(FlowerSlime.class, FlowerSlime.class,
						FlowerSlime.class));
				break;
		}
		return res;
	}

	// ===================== 4. BossRush 专用怪物池 =====================
	private static ArrayList<Class<? extends Mob>> getBossRushMobPool(int depth){
		ArrayList<Class<? extends Mob>> branchList = getSpecialBranchMobPool();
		if(branchList != null) return branchList;

		ArrayList<Class<? extends Mob>> res;
		switch (depth){
			case 21:
				res = new ArrayList<>(Arrays.asList(
						PiraLand.class, PiraLand.class, PhantomPiranha.class,
						RiceRat.class, RiceRat.class, PhantomPiranha.class));
				break;
			case 32: case 34: case 36: case 37:case 38:
			case 40: case 41:
				res = new ArrayList<>(Arrays.asList(
						Succubus.class,	ApprenticeWitch.class,
						Butcher.class,Butcher.class,
						PumkingBomber.class, Pumking_Ghost.class,
						DemonLord.class,
						Gorgon.class,
						Eye.class,Eye.class, Eye.class,
						Scorpio.class, Eye.class,Succubus.class,Monk.class,
						Golem.class,IceGolem.class,Fire_Scorpio.class,Fire_Scorpio.class,
						Scorpio.class, Succubus.class,Ice_Scorpio.class,ShieldHuntsman.class));
				break;
			default:
				//非rush专属深度复用普通池
				res = getNormalMobPool(depth);
				break;
		}
		return res;
	}

	// ===================== 5. 古墓 专用怪物池 =====================
	private static ArrayList<Class<? extends Mob>> getTombMobPool(int depth){
		ArrayList<Class<? extends Mob>> branchList = getSpecialBranchMobPool();
		if(branchList != null) return branchList;

		ArrayList<Class<? extends Mob>> res;
		switch (depth) {
			case 11:
				res = new ArrayList<>(Arrays.asList(
						GraveRat.class, Worm.class, Wisp.class, GraveRat.class, ThiefSoul.class));
				break;
			case 12:
				res = new ArrayList<>(Arrays.asList(
						GraveRat.class, Worm.class, Wisp.class, ThiefSoul.class, ThiefSoul.class));
				break;
			case 13:
				res = new ArrayList<>(Arrays.asList(
						GraveRat.class, Worm.class, Wisp.class, Worm.class, ThiefSoul.class,
						NecroScout.class));
				break;
			case 14:
				res = new ArrayList<>(Arrays.asList(
						GraveRat.class, Worm.class, NecroScout.class, Worm.class, ThiefSoul.class,
						NecroScout.class));
				break;
			case 16:
				res = new ArrayList<>(Arrays.asList(
						NecroArcher.class, NecroGuard.class, NecroAcolyte.class));
				break;
			case 17:
				res = new ArrayList<>(Arrays.asList(
						NecroArcher.class, NecroGuard.class
						, NecroGuard.class, NecroAcolyte.class, NecroAcolyte.class ));
				break;
			default:
				//非Tomb专属深度复用普通池
				res = getNormalMobPool(depth);
				break;
		}
		return res;
	}

	// ===================== 原有通用工具（无修改，兼容Java8） =====================
	//has a chance to add a rarely spawned mobs to the rotation
	public static void addRareMobs( int depth, ArrayList<Class<?extends Mob>> rotation ){

		switch (depth){

			// Sewers
			default:
				return;
			case 4:
				if (Random.Float() < 0.025f) rotation.add(Thief.class);
				return;

			// Prison
			case 9:
				if (Random.Float() < 0.025f) rotation.add(Bat.class);
				return;

			// Caves
			case 14:
				if (Random.Float() < 0.025f) rotation.add(Ghoul.class);
				return;

			// City
			case 19:
				if (Random.Float() < 0.025f) rotation.add(Succubus.class);
		}
	}

	//switches out regular mobs for their alt versions when appropriate
	private static void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){

		float altChance = 1/50f * RatSkull.exoticChanceMultiplier();
		float lanterChance = Dungeon.isChallenged(DHXD) && hero.lanterfire < 30 ? 0.35f : 0f;
		float moreChance = Dungeon.isChallenged(MOREROOM) ? 0.1f : 0f;

		for (int i = 0; i < rotation.size(); i++){
			if (Random.Float() < lanterChance + altChance + moreChance ) {
				Class<? extends Mob> cl = rotation.get(i);
				if (cl == Rat.class) {
					cl = Albino.class;
				} else if (cl == FlowerSlime.class) {
					cl = GiantFlowerSlime.class;
				} else if (cl == Katydid.class) {
					cl = Salamander.class;
				} else if (cl == Crab.class) {
					cl = NewBornCrab.class;
				} else if (cl == Guard.class) {
					cl = GuardCapital.class;
				} else if (cl == Thief.class) {
					cl = Bandit.class;
				} else if (cl == Necromancer.class) {
					cl = SpectralNecromancer.class;
				} else if (cl == BrownBat.class) {
					cl = NewBlackHost.class;
				} else if (cl == Brute.class) {
					cl = ArmoredBrute.class;
				} else if (cl == DM100.class) {
					cl = DM111.class;
				} else if (cl == DM200.class) {
					cl = DM201.class;
				} else if (cl == ColdMagicRat.class) {
					cl = VeryColdRat.class;
				} else if (cl == Monk.class) {
					cl = Senior.class;
				} else if (cl == Warlock.class) {
					cl = WarlockHead.class;
				} else if (cl == Ghoul.class) {
					cl = GhoulPlus.class;
				} else if (cl == Golem.class) {
					cl = IceGolem.class;
				} else if (cl == Scorpio.class) {
					cl = Acidic.class;
				} else if (cl == Spinner.class) {
					cl = GnollShiled.class;
				} else if (cl == RedSwarm.class) {
					cl = BloodsSwarm.class;
				} else if (cl == Elemental.class && Dungeon.isChallenged(RLPT)) {
					cl = RandomBlueFireDE.class;
				} else if (cl == FireGhost.class && Dungeon.isChallenged(RLPT)) {
					cl = RedMurderer.class;
				} else if (cl == ShieldHuntsman.class && Dungeon.isChallenged(RLPT)) {
					cl = DrTerror.class;
				} else if (cl == FireGhost.class) {
					cl = MolotovHuntsman.class;
				} else if (cl == MobsUtilsRoom.DM275RPG_SP.class) {
					cl = MobsUtilsRoom.DM275X.class;
				} else if (cl == Eye.class) {
					cl = DeadEye.class;
				} else if (cl == SmallSkeletonDemon.class) {
					cl = SkeletonDemon.class;
				} else if (cl == Worm.class) {
					cl = WormWhyHuman.class;
				} else if (cl == NecroScout.class) {
					cl = NecroPioneer.class;
				}
				rotation.set(i, cl);
			}
		}
	}
}