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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.AnomaloCaris;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.ThreeLeafBug;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.Turtle;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.ArmyFlag;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrystalDiedTower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DCrystal;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DiamondKnight;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DictFish;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfFuze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneral;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfMaster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfSolider;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireDragon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireMagicDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Qliphoth;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.QliphothLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.RoomStone;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.SakaFishBoss;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.TPDoor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.bossrush.Rival;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.bossrush.SkyGoo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.BleedCrystal;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.DeadDogCerberus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.Morphs;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.MyCoreHeart;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.Nyarlathotep;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.ShubNiggurath;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.TowerGods;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.TowerMachine;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.TowerMind;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.TowerTime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.YogSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.CrivusStarFruits;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.CrivusStarFruitsLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.DiedClearElemet;
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.HermitCrabNoShell;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.Mayfly;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.Prisoner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.RoyalGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.TribemanOld;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.ApprenticeWitch;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Butcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Crumb;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Frankenstein;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Ghost_Halloween;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.HollowMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.PumkingBomber;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Pumking_Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Vampire;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.allsearch.HelpTeleportPoint;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.allsearch.ShadowHunstman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Anger;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Junko;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Pink;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Smart;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DragonGirlBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MageHand;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nyz;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.DeathRong;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.YetYog;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.MiniSaka;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.BloodsSwarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.DM111;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.DeadEye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.DemonFodder;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.GhoulPlus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.GiantFlowerSlime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.GuardCapital;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.NewBornCrab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.VeryColdRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.WarlockHead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rlpt.DrTerror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rlpt.GunHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.DM275;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.GnollHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.GreenSlting;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.SkyDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.SuccubusQueen;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Aggregatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.GraveRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroAcolyte;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroArcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroCavalry;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroPioneer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroScout;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroWarlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.SkeletonDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.SmallSkeletonDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.ThiefSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Wisp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Worm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.WormWhyHuman;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.utils.MobsUtilsRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SentryRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GnollRockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TenguDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.CorpseDustTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.DeadSoulTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.MobSpawnTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.AikeLaier;
import com.shatteredpixel.shatteredpixeldungeon.plants.BlandfruitBush;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Mageroyal;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.SkyBlueFireBloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

//contains all the game's various entities, mostly enemies, NPCS, and allies, but also traps and plants
public enum Bestiary {

	FOREST,
	FOREST_BOSS,
	ICEPRISON,
	ICEPRISON_BOSS,
	ICECAVE,
	ICECAVE_BOSS,
	CITY,
	CITY_BOSS,
	HELL,
	HELL_BOSS,


	GOLDMOB,
	GOLDMOB_EX,
	ANCITY,
	SHOP,
	ANCIENT_CITY,
	MINIGAMES,
	ANCIENT_CITY_BOSS,

	TUMULUS,
	CHURCH,

	UNIVERSAL,
	QUEST,
	RLPT,
	RARE_T,
    RARE,


	BOSSRUSH,
	NEUTRAL,

	ALLY,
	TRAP,
	PLANT;

	//tracks whether an entity has been encountered
	private final LinkedHashMap<Class<?>, Boolean> seen = new LinkedHashMap<>();
	//tracks enemy kills, trap activations, plant tramples, or just sets to 1 for seen on allies
	private final LinkedHashMap<Class<?>, Integer> encounterCount = new LinkedHashMap<>();

	//should only be used when initializing
	private void addEntities(Class<?>... classes ){
		for (Class<?> cls : classes){
			seen.put(cls, false);
			encounterCount.put(cls, 0);
		}
	}

	public Collection<Class<?>> entities(){
		return seen.keySet();
	}

	public String title(){
		return Messages.get(this, name() + ".title");
	}

	public int totalEntities(){
		return seen.size();
	}

	public int totalSeen(){
		int seenTotal = 0;
		for (boolean entitySeen : seen.values()){
			if (entitySeen) seenTotal++;
		}
		return seenTotal;
	}

	static {

		FOREST.addEntities(
				Rat.class, FlowerSlime.class, Katydid.class,
				Gnoll.class, ClearElemental.class, Crab.class, Swarm.class,
				Slime_Red.class, Slime_Orange.class, GreenSlting.class);

		FOREST_BOSS.addEntities(
				//Boss T1
				Qliphoth.class, QliphothLasher.class,
				//Boss T2
				CrivusStarFruits.class, CrivusStarFruitsLasher.class,
				//Boss T3
				FireDragon.class,
				DiedClearElemet.ClearElemetalBlood.class,
				DiedClearElemet.ClearElemetalGold.class,
				DiedClearElemet.ClearElemetalGreen.class,
				DiedClearElemet.ClearElemetalPure.class,
				DiedClearElemet.ClearElemetalDark.class);

		ICEPRISON.addEntities(
				Skeleton.class, BrownBat.class, Thief.class,
				DM100.class, Necromancer.class, Guard.class,
				GnollHero.class);
				//EX
				//NewBlackHost.class, SpectralNecromancer.class,
				//Bandit.class, SRPDHBLR.class

		ICEPRISON_BOSS.addEntities(
				//Boss T1
				Tengu.class,
				//Boss T2
				DiamondKnight.class,
				TPDoor.class,
				DCrystal.class,
				DimandMimic.class);

		ICECAVE.addEntities(Bat.class, ColdMagicRat.class,
				FireGhost.class, RedSwarm.class,
				Spinner.class, Brute.class, DM200.class,

				FlameB01.class, FlameC02.class,
				GnollShiled.class, SkullShaman.class, RedNecromancer.class);

		ICECAVE_BOSS.addEntities(
				DM275.class,
				DM300.class,
				NewDM720.class, MoloHR.class,
				MagicGirlDead.class);

		CITY.addEntities(
				Shaman.RedShaman.class, Shaman.BlueShaman.class, Shaman.PurpleShaman.class,
				Monk.class, Elemental.FrostElemental.class, Elemental.ShockElemental.class,
				Elemental.FireElemental.class,Elemental.HaloWar.class,Ghoul.class,
				BruteBot.class, Warlock.class, Golem.class, ShieldHuntsman.class, IceGolem.class);

		CITY_BOSS.addEntities(
				//Boss T1
				DwarfKing.class,
				//Boss T2
				DwarfMaster.class,
				//Boss T3
				DwarfGeneral.class,
				DwarfFuze.class,
				DwarfSolider.class,
				ArmyFlag.class);

		HELL.addEntities(
				Scorpio.class, Ice_Scorpio.class,
				Fire_Scorpio.class,
				Eye.class, DemonSpawner.class,
				RipperDemon.class, Succubus.class, SuccubusQueen.class
		);

		HELL_BOSS.addEntities(
				//Boss T1
				YogDzewa.class,
				YogDzewa.Larva.class, YogFist.BurningFist.class, YogFist.SoiledFist.class,
				YogFist.RottingFist.class, YogFist.RustedFist.class,
				YogFist.BrightFist.class, YogFist.DarkFist.class,
				YogFist.HaloFist.class, YogFist.FreezingFist.class,

				//Boss T2
				YogReal.class);

		GOLDMOB.addEntities(
				GnollBlind.class, Mayfly.class, OGPDLLS.class, OGPDZSLS.class, Slime_Qs.class,
				Slime_Sn.class, GiantWorm.class, HermitCrab.class, HermitCrabNoShell.class,

				Prisoner.class, GnollThrower.class, BoomSkull.class,

				Drake.class, TribemanOld.class
		);

		GOLDMOB_EX.addEntities(
				GnollTwilight.class,
				GoblinShaman.ShamanStrength.class,
				GoblinShaman.ShamanFake.class,
				GoblinShaman.ShamanRegen.class,
				GoblinShaman.ShamanShield.class,

				Artillerist.class, RoyalGuard.class,

				Gorgon.class, DemonLord.class
		);

		ANCITY.addEntities(
				Turtle.class,
				ThreeLeafBug.class,
				AnomaloCaris.class,
				RoomStone.class,
				DictFish.class,
				SakaFishBoss.class
		);

		SHOP.addEntities(
				ColdGurad.class,
				FireMagicDied.class,
				CrystalDiedTower.class
		);

		ANCIENT_CITY.addEntities(
		Frankenstein.class, Butcher.class, Crumb.class, ApprenticeWitch.class,
		Ghost_Halloween.class, HollowMimic.class, Pumking_Ghost.class,PumkingBomber.class,
		Vampire.class);

		MINIGAMES.addEntities(
				Ghost_Anger.class,
				Ghost_Junko.class,
				Ghost_Pink.class,
				Ghost_Smart.class,

				HelpTeleportPoint.class,
				ShadowHunstman.class,

				MobsUtilsRoom.RedGolem.class,
				MobsUtilsRoom.RedSnake.class,
				MobsUtilsRoom.RedTorchHuntsman.class,
				MobsUtilsRoom.RedMagicShieldMan.class,
				MobsUtilsRoom.RedShaman.class,
				MobsUtilsRoom.RedSpider.class,
				DM275.class, OldDM300.class
		);

		ANCIENT_CITY_BOSS.addEntities(
				//Boss T1
				DeadDogCerberus.class, BleedCrystal.class,
				//Boss T2
				Morphs.class,
				Nyarlathotep.class, ShubNiggurath.class, YogSoul.class,
				TowerGods.class, TowerMind.class, TowerMachine.class, TowerTime.class,
				MyCoreHeart.class
				);

		//古墓测试
		TUMULUS.addEntities(
				DeviceCompat.isDebug() || DeviceCompat.isMDP()
						? new Class<?>[]{
								GraveRat.class, Worm.class, Wisp.class, NecroScout.class,
						ThiefSoul.class, SkeletonDemon.class, SmallSkeletonDemon.class,
						NecroPioneer.class, WormWhyHuman.class
				}

						: new Class<?>[]{Albino.class,
						Salamander.class,
						SRPDHBLR.class, NewBlackHost.class,
						Bandit.class, SpectralNecromancer.class,
						ArmoredBrute.class, DM201.class, MolotovHuntsman.class,
						Elemental.ChaosElemental.class, Senior.class,
						Acidic.class,

						TormentedSpirit.class, PhantomPiranha.class,
						CrystalMimic.class, ArmoredStatue.class}
		);

		CHURCH.addEntities(
				DeviceCompat.isDebug() || DeviceCompat.isMDP()
						? new Class<?>[]{
						//T2
						NecroGuard.class, NecroArcher.class,
						NecroAcolyte.class,

						NecroCavalry.class, Aggregatus.class, NecroWarlock.class}

						: new Class<?>[]{Albino.class,
						Salamander.class,
						SRPDHBLR.class, NewBlackHost.class,
						Bandit.class, SpectralNecromancer.class,
						ArmoredBrute.class, DM201.class, MolotovHuntsman.class,
						Elemental.ChaosElemental.class, Senior.class,
						Acidic.class,

						TormentedSpirit.class, PhantomPiranha.class,
						CrystalMimic.class, ArmoredStatue.class}
		);
		

		UNIVERSAL.addEntities(Wraith.class, Piranha.class, Mimic.class, GoldenMimic.class, EbonyMimic.class,  GreenDiamndMimic.class,Statue.class, GuardianTrap.Guardian.class, SentryRoom.Sentry.class);

		RARE.addEntities(
				Albino.class,
                Salamander.class,
				SRPDHBLR.class, NewBlackHost.class,
				Bandit.class, SpectralNecromancer.class,
				ArmoredBrute.class, DM201.class, MolotovHuntsman.class,
				Elemental.ChaosElemental.class, Senior.class,
				Acidic.class,

				TormentedSpirit.class, PhantomPiranha.class,
				CrystalMimic.class, ArmoredStatue.class);

		RARE_T.addEntities(
				GiantFlowerSlime.class, NewBornCrab.class,
				DM111.class, GuardCapital.class,
				VeryColdRat.class, BloodsSwarm.class,
				GhoulPlus.class, WarlockHead.class,
				DemonFodder.class, DeadEye.class
		);

		RLPT.addEntities(
				KagenoNusujin.class,
				GunHuntsman.class,
				RedMurderer.class,
				RandomBlueFireDE.class,
				DrTerror.class,
				DrTerror.BombTech.class
		);

		QUEST.addEntities(FetidRat.class, GnollTrickster.class, GreatCrab.class,
				Elemental.NewbornFireElemental.class, RotLasher.class, RotHeart.class,
				CrystalWisp.class, CrystalGuardian.class, CrystalSpire.class, GnollGuard.class, GnollSapper.class, GnollGeomancer.class, Goo.class, SkyDead.class);

		BOSSRUSH.addEntities(
				SkyGoo.class,
				SlimeKing.class,
				GreenStingCV.class,
				Rival.class,
				DwarfMaster.class
		);

		NEUTRAL.addEntities(
				Ghost.class, RatKing.class, Shopkeeper.class,
				Wandmaker.class, Blacksmith.class, Imp.class,
				Sheep.class, Bee.class, Nyz.class, YetYog.class,
				DeathRong.class, DragonGirlBlue.class);

		ALLY.addEntities(MirrorImage.class, PrismaticImage.class,
				DriedRose.GhostHero.class,
				WandOfWarding.Ward.class, WandOfWarding.Ward.WardSentry.class, WandOfLivingEarth.EarthGuardian.class,
				ShadowClone.ShadowAlly.class, SmokeBomb.NinjaLog.class, SpiritHawk.HawkAlly.class, MiniSaka.class, MageHand.class);

		TRAP.addEntities(
				WornDartTrap.class, PoisonDartTrap.class, DisintegrationTrap.class, GatewayTrap.class,DeadSoulTrap.class,

				ChillingTrap.class, BurningTrap.class, ShockingTrap.class, AlarmTrap.class, GrippingTrap.class, TeleportationTrap.class, OozeTrap.class,

				FrostTrap.class, BlazingTrap.class, StormTrap.class, GuardianTrap.class, FlashingTrap.class, WarpingTrap.class,MobSpawnTrap.class,

				ConfusionTrap.class, ToxicTrap.class, CorrosionTrap.class,CorpseDustTrap.class,
				FlockTrap.class, SummoningTrap.class, WeakeningTrap.class, CursingTrap.class,
				GeyserTrap.class, ExplosiveTrap.class, RockfallTrap.class, PitfallTrap.class,
				DistortionTrap.class, DisarmingTrap.class, GrimTrap.class

				 );

		PLANT.addEntities(Rotberry.class, Sungrass.class, Fadeleaf.class, Icecap.class,
				Firebloom.class, Sorrowmoss.class, Swiftthistle.class, Blindweed.class,
				Stormvine.class, Earthroot.class, Mageroyal.class, Starflower.class,
				BlandfruitBush.class, AikeLaier.class, SkyBlueFireBloom.class,
				WandOfRegrowth.Dewcatcher.class, WandOfRegrowth.Seedpod.class, WandOfRegrowth.Lotus.class);

	}

	//some mobs and traps have different internal classes in some cases, so need to convert here
	private static final HashMap<Class<?>, Class<?>> classConversions = new HashMap<>();
	static {
		classConversions.put(CorpseDust.DustWraith.class,      Wraith.class);

		classConversions.put(Necromancer.NecroSkeleton.class,  Skeleton.class);

		classConversions.put(TenguDartTrap.class,              PoisonDartTrap.class);
		classConversions.put(GnollRockfallTrap.class,          RockfallTrap.class);

		classConversions.put(DwarfKing.DKGhoul.class,          Ghoul.class);
		classConversions.put(DwarfKing.DKWarlock.class,        Warlock.class);
		classConversions.put(DwarfKing.DKMonk.class,           Monk.class);
		classConversions.put(DwarfKing.DKGolem.class,          Golem.class);

		classConversions.put(YogDzewa.YogRipper.class,         RipperDemon.class);
		classConversions.put(YogDzewa.YogEye.class,            Eye.class);
		classConversions.put(YogDzewa.YogScorpio.class,        Scorpio.class);
	}

	public static boolean isSeen(Class<?> cls){
		for (Bestiary cat : values()) {
			if (cat.seen.containsKey(cls)) {
				return cat.seen.get(cls);
			}
		}
		return false;
	}

	public static void setSeen(Class<?> cls){
		if (classConversions.containsKey(cls)){
			cls = classConversions.get(cls);
		}
		for (Bestiary cat : values()) {
			if (cat.seen.containsKey(cls) && !cat.seen.get(cls)) {
				cat.seen.put(cls, true);
				Journal.saveNeeded = true;
			}
		}
		Badges.validateCatalogBadges();
	}

	public static int encounterCount(Class<?> cls) {
		for (Bestiary cat : values()) {
			if (cat.encounterCount.containsKey(cls)) {
				return cat.encounterCount.get(cls);
			}
		}
		return 0;
	}

	//used primarily when bosses are killed and need to clean up their minions
	public static boolean skipCountingEncounters = false;

	public static void countEncounter(Class<?> cls){
		countEncounters(cls, 1);
	}

	public static void countEncounters(Class<?> cls, int encounters){
		if (skipCountingEncounters){
			return;
		}
		if (classConversions.containsKey(cls)){
			cls = classConversions.get(cls);
		}
		for (Bestiary cat : values()) {
			if (cat.encounterCount.containsKey(cls) && cat.encounterCount.get(cls) != Integer.MAX_VALUE){
				cat.encounterCount.put(cls, cat.encounterCount.get(cls)+encounters);
				if (cat.encounterCount.get(cls) < -1_000_000_000){ //to catch cases of overflow
					cat.encounterCount.put(cls, Integer.MAX_VALUE);
				}
				Journal.saveNeeded = true;
			}
		}
	}

	private static final String BESTIARY_CLASSES    = "bestiary_classes";
	private static final String BESTIARY_SEEN       = "bestiary_seen";
	private static final String BESTIARY_ENCOUNTERS = "bestiary_encounters";

	public static void store( Bundle bundle ){

		ArrayList<Class<?>> classes = new ArrayList<>();
		ArrayList<Boolean> seen = new ArrayList<>();
		ArrayList<Integer> encounters = new ArrayList<>();

		for (Bestiary cat : values()) {
			for (Class<?> entity : cat.entities()) {
				if (cat.seen.get(entity) || cat.encounterCount.get(entity) > 0){
					classes.add(entity);
					seen.add(cat.seen.get(entity));
					encounters.add(cat.encounterCount.get(entity));
				}
			}
		}

		Class<?>[] storeCls = new Class[classes.size()];
		boolean[] storeSeen = new boolean[seen.size()];
		int[] storeEncounters = new int[encounters.size()];

		for (int i = 0; i < storeCls.length; i++){
			storeCls[i] = classes.get(i);
			storeSeen[i] = seen.get(i);
			storeEncounters[i] = encounters.get(i);
		}

		bundle.put( BESTIARY_CLASSES, storeCls );
		bundle.put( BESTIARY_SEEN, storeSeen );
		bundle.put( BESTIARY_ENCOUNTERS, storeEncounters );

	}

	public static void restore( Bundle bundle ){

		if (bundle.contains(BESTIARY_CLASSES)
				&& bundle.contains(BESTIARY_SEEN)
				&& bundle.contains(BESTIARY_ENCOUNTERS)){
			Class<?>[] classes = bundle.getClassArray(BESTIARY_CLASSES);
			boolean[] seen = bundle.getBooleanArray(BESTIARY_SEEN);
			int[] encounters = bundle.getIntArray(BESTIARY_ENCOUNTERS);

			for (int i = 0; i < classes.length; i++){
				for (Bestiary cat : values()){
					if (cat.seen.containsKey(classes[i])){
						cat.seen.put(classes[i], seen[i]);
						cat.encounterCount.put(classes[i], encounters[i]);
					}
				}
			}
		}

	}

}
