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

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.RLPT;

import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.AnomaloCaris;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.ThreeLeafBug;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.Turtle;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.ApprenticeWitch;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Butcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Crumb;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Frankenstein;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.PumkingBomber;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class Bestiary {

	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth ){
		ArrayList<Class<? extends Mob>> mobs = standardMobRotation( depth );
		addRareMobs(depth, mobs);
		swapMobAlts(mobs);
		Random.shuffle(mobs);
		return mobs;
	}

	//支离破碎
	private static ArrayList<Class<? extends Mob>> standardMobRotation(int i) {

		//TODO 暂时这样 后续优化
		if(Dungeon.branch!=0){
			switch (Dungeon.branch){
				case 1:
				if(Dungeon.depth == 17 || Dungeon.depth == 18){
					return new ArrayList<>(Arrays.asList(
							Turtle.class,Turtle.class,Turtle.class,Turtle.class,Turtle.class,Turtle.class, Turtle.class));
				}
				case 2:
				if(Dungeon.depth == 18 || Dungeon.depth == 17){
					return new ArrayList<>(Arrays.asList(
							Turtle.class, ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class,ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class,ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class,ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class,ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class,ThreeLeafBug.class, AnomaloCaris.class, AnomaloCaris.class,
							ThreeLeafBug.class));
				}
				case 5:
					if(Dungeon.depth == 17){
						return new ArrayList<>(Arrays.asList(BlueWraith.class));
					}

			}
		}else if (Dungeon.isChallenged(RLPT) && !Dungeon.isDLC(Conducts.Conduct.BOSSRUSH)){
			Dungeon_RLPT();
		}

			switch (i) {
				//正常刷怪
				case 1:
					//3x rat, 1x snake
					return new ArrayList<>(Arrays.asList(
							Rat.class, Rat.class));
				case 2:
					return new ArrayList<>(Arrays.asList(FlowerSlime.class,
							FlowerSlime.class, FlowerSlime.class, Gnoll.class, Gnoll.class,
							Gnoll.class, Gnoll.class));
				case 3:
					return new ArrayList<>(Arrays.asList(FlowerSlime.class,
							FlowerSlime.class, Salamander.class,
							Salamander.class, FlowerSlime.class, FlowerSlime.class,
							ClearElemental.class,Crab.class,Swarm.class));
				case 4:
					return new ArrayList<>(Arrays.asList(Salamander.class,
							ClearElemental.class, Slime_Red.class,
							Slime_Orange.class, Swarm.class,Crab.class));
				case 5:
					return new ArrayList<>(Arrays.asList(FlowerSlime.class, FlowerSlime.class,Slime.class,Slime.class,Swarm.class,Crab.class));
				case 6:
					return new ArrayList<>(Arrays.asList(Skeleton.class,
							Skeleton.class,BrownBat.class,BrownBat.class));

				case 7:
					return new ArrayList<>(Arrays.asList(Skeleton.class,
							Thief.class,DM100.class,Necromancer.class,Guard.class,DM100.class));
				case 8:
					return new ArrayList<>(Arrays.asList(Skeleton.class,
							Skeleton.class,
							Thief.class, Guard.class,Necromancer.class, DM100.class));
				case 9:
					return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
							Thief.class,Thief.class,BrownBat.class,BrownBat.class, DM100.class,
							BrownBat.class));

				case 10:
					return new ArrayList<>(Arrays.asList(Bat.class,
							Brute.class, Brute.class,
							Necromancer.class,Necromancer.class));
				case 11:
					return new ArrayList<>(Arrays.asList(Bat.class, DM100.class,DM100.class,ColdMagicRat.class,
							ColdMagicRat.class));
				case 12:
					//1x bat, 1x brute, 2x shaman, 2x spinner, 2x DM-300
					return new ArrayList<>(Arrays.asList(
							Bat.class,
							ColdMagicRat.class,FireGhost.class,ColdMagicRat.class));
				case 13:
					//1x bat, 1x brute, 2x shaman, 2x spinner, 2x DM-300
					return new ArrayList<>(Arrays.asList(
							Bat.class,
							Brute.class,Spinner.class,
							DM100.class,RedSwarm.class));

				case 14:
					return new ArrayList<>(Arrays.asList(
							Bat.class,
							Spinner.class,
							ColdMagicRat.class,RedSwarm.class,Shaman.random()));
				case 15:
					//1x bat, 1x brute, 2x shaman, 2x spinner, 2x DM-300
					return new ArrayList<>(Arrays.asList(
							Bat.class,
							Spinner.class,

							Brute.class,FireGhost.class,ColdMagicRat.class,RedSwarm.class));

				// City
				case 16:
					//5x elemental, 5x warlock, 1x monk, 2x silvercrab
					return new ArrayList<>(Arrays.asList(
							Monk.class,
							Shaman.random()));
				case 17:
					//2x elemental, 2x warlock, 2x monk, 1x silvercrab
					return new ArrayList<>(Arrays.asList(
							Elemental.random(),
							Monk.class,FireGhost.class));
				case 18:
					//1x elemental, 1x warlock, 2x monk, 3x golem
					return new ArrayList<>(Arrays.asList(
							Elemental.random(),
							Random.NormalFloat(1,6)>=3 ? BruteBot.class : Warlock.class,
							Monk.class,
							Golem.class,FireGhost.class));
				case 19:
					//1x elemental, 1x warlock, 2x monk, 3x golem
					return new ArrayList<>(Arrays.asList(
							Monk.class,
							Golem.class, Warlock.class,ShieldHuntsman.class,
							Random.NormalFloat(1,6)>3 ? BruteBot.class : Warlock.class));
				case 20:
					//1x elemental, 1x warlock, 2x monk, 3x golem
					return new ArrayList<>(Arrays.asList(
							Elemental.random(),
							Random.NormalFloat(1,6)>3 ? BruteBot.class : Warlock.class,
							Monk.class,
							Golem.class, Golem.class,Ice_Scorpio.class));

				case 21:
					//3x succubus, 3x evil eye
					return new ArrayList<>(Arrays.asList(
							Eye.class,ShieldHuntsman.class));
				case 22:
					//3x succubus, 3x evil eye
					return Dungeon.isDLC(Conducts.Conduct.BOSSRUSH) ? new ArrayList<>(Arrays.asList(
							Eye.class,ShieldHuntsman.class,RedMurderer.class,MolotovHuntsman.class)) :
							new ArrayList<>(Arrays.asList(
									Eye.class,ShieldHuntsman.class));
				case 23:
					//1x: succubus, 2x evil eye, 3x scorpio
					return Dungeon.isDLC(Conducts.Conduct.BOSSRUSH) ? new ArrayList<>(Arrays.asList(
							Eye.class,ShieldHuntsman.class,Fire_Scorpio.class,Ice_Scorpio.class,RedMurderer.class)) :
							new ArrayList<>(Arrays.asList(
									Eye.class,ShieldHuntsman.class,Ice_Scorpio.class));
				//前半段决战
				case 24:
					//1x succubus, 2x evil eye, 3x scorpio
					return new ArrayList<>(Arrays.asList(
							Succubus.class,
							Eye.class,
							Scorpio.class, Succubus.class,Fire_Scorpio.class,Ice_Scorpio.class,ShieldHuntsman.class));

				case 26:
					return new ArrayList<>(Arrays.asList(
							Frankenstein.class, Crumb.class));

				case 27:
					return new ArrayList<>(Arrays.asList(
							Frankenstein.class, Crumb.class, Butcher.class,Butcher.class,Crumb.class));

				case 28:
					return new ArrayList<>(Arrays.asList(
							ApprenticeWitch.class, Crumb.class, ApprenticeWitch.class,Butcher.class,Crumb.class));

				case 29:
					return new ArrayList<>(Arrays.asList(
							ApprenticeWitch.class, Crumb.class, PumkingBomber.class, ApprenticeWitch.class));

				default:
					return new ArrayList<>(Arrays.asList(FlowerSlime.class, FlowerSlime.class,
							FlowerSlime.class,
							FlowerSlime.class, FlowerSlime.class, FlowerSlime.class,
							FlowerSlime.class, FlowerSlime.class, FlowerSlime.class,
							FlowerSlime.class));
			}

	}

	private static ArrayList<Class<? extends Mob>> Dungeon_RLPT() {
		switch (Dungeon.depth) {
			case 1:
				//开发者测试怪组
				if(DeviceCompat.isDebug() && DeviceCompat.isDesktop()){
					return new ArrayList<>(Arrays.asList(
							Frankenstein.class, ApprenticeWitch.class,
							FlowerSlime.class));
				} else {
					return new ArrayList<>(Arrays.asList(
							Rat.class, Rat.class));
				}

			case 2:
				return new ArrayList<>(Arrays.asList(FlowerSlime.class,
						FlowerSlime.class, FlowerSlime.class, Gnoll.class, Gnoll.class,
						Gnoll.class, Gnoll.class, Albino.class));
			case 3:
				return new ArrayList<>(Arrays.asList(FlowerSlime.class,
						FlowerSlime.class, Salamander.class, FlowerSlime.class, FlowerSlime.class,
						ClearElemental.class,Crab.class,Swarm.class));
			case 4:
				return new ArrayList<>(Arrays.asList(Salamander.class,
						ClearElemental.class, Slime_Red.class,
						Slime_Orange.class, Swarm.class,Crab.class));
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
				switch (Random.Int(3)) {
					case 0:
						return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
								Thief.class,Thief.class,BrownBat.class,
								Spinner.class,Shaman.random()));
					case 1:
						return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
								Thief.class,Thief.class,Thief.class,
								DM100.class,Necromancer.class,Necromancer.class,
								DM100.class));
					case 2:
						return new ArrayList<>(Arrays.asList(
								Skeleton.class, Skeleton.class, Skeleton.class,
								Thief.class,Thief.class,BrownBat.class, DM100.class,
								Spinner.class, Necromancer.class,
								Necromancer.class,
								Brute.class,
								Bat.class));
				}
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				switch (Random.Int(4)) {
					case 0:
						return new ArrayList<>(Arrays.asList(Skeleton.class, BlackHost.class,
								Thief.class,DM100.class,Necromancer.class,IceGolem.class));
					case 1:
						return new ArrayList<>(Arrays.asList(Skeleton.class,
								Thief.class,
								DM100.class,Necromancer.class,SpectralNecromancer.class,
								DM100.class));
					case 2:
						return new ArrayList<>(Arrays.asList(
								Monk.class,
								Warlock.class));
					case 3:
						return new ArrayList<>(Arrays.asList(
								Elemental.random(),
								Monk.class, Monk.class,
								Golem.class));
				}
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
				switch (Random.Int(6)) {
					case 0:
						return new ArrayList<>(Arrays.asList(Skeleton.class,
								Thief.class,DM100.class,Necromancer.class,Monk.class,
								Golem.class));
					case 1:
						return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
								Thief.class,Thief.class,Fire_Scorpio.class,
								DM200.class,SkullShaman.class,
								DM100.class, Ice_Scorpio.class,Monk.class,
								Golem.class));
					case 2:
						return new ArrayList<>(Arrays.asList(
								Succubus.class,
								Scorpio.class,Monk.class,
								Golem.class));
					case 3:
						return new ArrayList<>(Arrays.asList(
								Fire_Scorpio.class,Albino.class,Monk.class,
								Golem.class));
					case 4:
						return new ArrayList<>(Arrays.asList(
								ShieldHuntsman.class,
								Monk.class,
								Golem.class));
					case 5:
						return new ArrayList<>(Arrays.asList(
								Succubus.class,
								Eye.class, Eye.class,
								Scorpio.class, Eye.class,Succubus.class,Monk.class,
								Golem.class));
				}
			default:
				return new ArrayList<>(Arrays.asList(FlowerSlime.class, FlowerSlime.class,
						FlowerSlime.class,
						FlowerSlime.class, FlowerSlime.class, FlowerSlime.class,
						FlowerSlime.class, FlowerSlime.class, FlowerSlime.class,
						FlowerSlime.class));
		}
	}

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
				return;
		}
	}

	//switches out regular mobs for their alt versions when appropriate
	private static void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){
		for (int i = 0; i < rotation.size(); i++){
			if (Random.Int( 50 ) == 0) {
				Class<? extends Mob> cl = rotation.get(i);
				if (cl == FlowerSlime.class) {
					cl = Albino.class;
				} else if (cl == Guard.class) {
					cl = SRPDHBLR.class;
				} else if (cl == Thief.class) {
					cl = Bandit.class;
				} else if (cl == Necromancer.class) {
					cl = SpectralNecromancer.class;
				} else if (cl == BrownBat.class) {
					cl = NewBlackHost.class;
				} else if (cl == Brute.class) {
					cl = ArmoredBrute.class;
				} else if (cl == DM200.class) {
					cl = DM201.class;
				} else if (cl == Monk.class) {
					cl = Senior.class;
				} else if (cl == Golem.class) {
					cl = IceGolem.class;
				} else if (cl == Scorpio.class) {
					cl = Acidic.class;
				} else if (cl == Spinner.class) {
					cl = GnollShiled.class;
				} else if (cl == FireGhost.class) {
					cl = MolotovHuntsman.class;
				}
				rotation.set(i, cl);
			}
		}
	}
}
