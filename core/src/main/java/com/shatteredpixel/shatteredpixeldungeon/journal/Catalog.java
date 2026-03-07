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
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ArcaneBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.RegrowthBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShockBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShrapnelBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.BrokenBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.DeepBloodBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.GrassKingBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.HellFireBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.IceCityBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.MagicGirlBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.NoKingMobBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.YellowSunBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist.BzmdrBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist.DeYiZiBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist.MoneyMoreBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist.PinkRandomBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.questbookslist.DimandBook;
import com.shatteredpixel.shatteredpixeldungeon.items.books.questbookslist.HollowCityBook;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Cake;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CrivusFruitsFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.LightFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.PhantomMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.RatTail;
import com.shatteredpixel.shatteredpixeldungeon.items.food.RedCrab;
import com.shatteredpixel.shatteredpixeldungeon.items.food.RiceDumplings;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SakaMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Switch;
import com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.BoneSoup;
import com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.ZakoSoup;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfNoWater;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.AquaBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfFeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfNukeCole;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.WaterSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.props.ArmorScalesOfBzmdr;
import com.shatteredpixel.shatteredpixeldungeon.items.props.BlockingDrug;
import com.shatteredpixel.shatteredpixeldungeon.items.props.BottleWraith;
import com.shatteredpixel.shatteredpixeldungeon.items.props.BrokenBone;
import com.shatteredpixel.shatteredpixeldungeon.items.props.BrokenRing;
import com.shatteredpixel.shatteredpixeldungeon.items.props.CatGirlCosplay;
import com.shatteredpixel.shatteredpixeldungeon.items.props.CloakFragmentsOfBzmdr;
import com.shatteredpixel.shatteredpixeldungeon.items.props.ConfusedMieMieTalisman;
import com.shatteredpixel.shatteredpixeldungeon.items.props.DeadOrAlive;
import com.shatteredpixel.shatteredpixeldungeon.items.props.DeliciousRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Dirt_KnifeStand;
import com.shatteredpixel.shatteredpixeldungeon.items.props.DreamSeed;
import com.shatteredpixel.shatteredpixeldungeon.items.props.EmotionalAggregation;
import com.shatteredpixel.shatteredpixeldungeon.items.props.EmotionalAggregationB;
import com.shatteredpixel.shatteredpixeldungeon.items.props.FaintGlimmer;
import com.shatteredpixel.shatteredpixeldungeon.items.props.HeartOfCrystalFractal;
import com.shatteredpixel.shatteredpixeldungeon.items.props.KillEye;
import com.shatteredpixel.shatteredpixeldungeon.items.props.KnightStabbingSword;
import com.shatteredpixel.shatteredpixeldungeon.items.props.LuckyGlove;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Monocular;
import com.shatteredpixel.shatteredpixeldungeon.items.props.NewStem;
import com.shatteredpixel.shatteredpixeldungeon.items.props.NoteOfBzmdr;
import com.shatteredpixel.shatteredpixeldungeon.items.props.PortableWhetstone;
import com.shatteredpixel.shatteredpixeldungeon.items.props.PureRouge;
import com.shatteredpixel.shatteredpixeldungeon.items.props.RapidEarthRoot;
import com.shatteredpixel.shatteredpixeldungeon.items.props.RustedGoldCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.props.StarDust;
import com.shatteredpixel.shatteredpixeldungeon.items.props.StarSachet;
import com.shatteredpixel.shatteredpixeldungeon.items.props.TerrorDoll;
import com.shatteredpixel.shatteredpixeldungeon.items.props.TerrorDollB;
import com.shatteredpixel.shatteredpixeldungeon.items.props.TheGriefOfSpeechless;
import com.shatteredpixel.shatteredpixeldungeon.items.props.WenStudyingPaperOne;
import com.shatteredpixel.shatteredpixeldungeon.items.props.WenStudyingPaperTwo;
import com.shatteredpixel.shatteredpixeldungeon.items.props.YanStudyingPaperOne;
import com.shatteredpixel.shatteredpixeldungeon.items.props.YanStudyingPaperTwo;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DwarfToken;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.AllSearchIQuest;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.PacManQuest;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

//For items, but includes a few item-like effects, such as enchantments
public enum Catalog {

	//EQUIPMENT
	MELEE_WEAPONS,
	ARMOR,
	ENCHANTMENTS,
	GLYPHS,
	THROWN_WEAPONS,
	WANDS,
	RINGS,
	ARTIFACTS,
	TRINKETS,
	BOOKS,
	MISC_EQUIPMENT,

	//CONSUMABLES
	POTIONS,
	SEEDS,
	SCROLLS,
	STONES,
	FOOD,
	EXOTIC_POTIONS,
	EXOTIC_SCROLLS,
	BOMBS,
	PROPS_LEVEL1_GOOD,
	PROPS_LEVEL2_GOOD,
	PROPS_LEVEL3_GOOD,
	PROPS_LEVEL1_BAD,
	PROPS_LEVEL2_BAD,
	PROPS_LEVEL3_BAD,
	PROPS_LEVEL1_CHAOS,
	MINIGAMES,
	TIPPED_DARTS,
	BREWS_ELIXIRS,
	SPELLS,
	MISC_CONSUMABLES;

	//tracks whether an item has been collected while identified
	private final LinkedHashMap<Class<?>, Boolean> seen = new LinkedHashMap<>();
	//tracks upgrades spent for equipment, uses for consumables
	private final LinkedHashMap<Class<?>, Integer> useCount = new LinkedHashMap<>();

	public Collection<Class<?>> items(){
		return seen.keySet();
	}

	//should only be used when initializing
	private void addItems( Class<?>... items){
		for (Class<?> item : items){
			seen.put(item, DeviceCompat.isDesktop_Dev());
			useCount.put(item, 0);
		}
	}

	public String title(){
		return Messages.get(this, name() + ".title");
	}

	public int totalItems(){
		return seen.size();
	}

	public int totalSeen(){
		int seenTotal = 0;
		for (boolean itemSeen : seen.values()){
			if (itemSeen) seenTotal++;
		}
		return seenTotal;
	}

	static {

		MELEE_WEAPONS.addItems(Generator.Category.WEP_T1.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T2.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T3.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T4.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T5.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T6.classes);

		ARMOR.addItems(Generator.Category.ARMOR.classes);

		THROWN_WEAPONS.addItems(Generator.Category.MIS_T1.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T2.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T3.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T4.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T5.classes);

		ENCHANTMENTS.addItems(Weapon.Enchantment.common);
		ENCHANTMENTS.addItems(Weapon.Enchantment.uncommon);
		ENCHANTMENTS.addItems(Weapon.Enchantment.rare);
		ENCHANTMENTS.addItems(Weapon.Enchantment.curses);

		GLYPHS.addItems(Armor.Glyph.common);
		GLYPHS.addItems(Armor.Glyph.uncommon);
		GLYPHS.addItems(Armor.Glyph.rare);
		GLYPHS.addItems(Armor.Glyph.curses);

		WANDS.addItems(Generator.Category.WAND.classes);

		RINGS.addItems(Generator.Category.RING.classes);

		ARTIFACTS.addItems(Generator.Category.ARTIFACT.classes);

		TRINKETS.addItems(Generator.Category.TRINKET.classes);

		PROPS_LEVEL1_GOOD.addItems(
				StarSachet.class,
				RapidEarthRoot.class,
				PortableWhetstone.class,
				NewStem.class,
				LuckyGlove.class,
				EmotionalAggregation.class
		);

		PROPS_LEVEL2_GOOD.addItems(
				WenStudyingPaperOne.class,
				Monocular.class,
				ArmorScalesOfBzmdr.class,
				DeliciousRecipe.class,
				YanStudyingPaperTwo.class,
				KnightStabbingSword.class
		);

		PROPS_LEVEL3_GOOD.addItems(
				KillEye.class,
				PureRouge.class,
				FaintGlimmer.class
		);

		PROPS_LEVEL1_BAD.addItems(
				BrokenBone.class,
				RustedGoldCoin.class,
				ConfusedMieMieTalisman.class,
				BottleWraith.class,
				BlockingDrug.class
		);

		PROPS_LEVEL2_BAD.addItems(
				TheGriefOfSpeechless.class,
				TerrorDoll.class,
				TerrorDollB.class,
				YanStudyingPaperOne.class,
				WenStudyingPaperTwo.class,
				CloakFragmentsOfBzmdr.class,
				EmotionalAggregationB.class,
				HeartOfCrystalFractal.class,
				Dirt_KnifeStand.class
		);

		PROPS_LEVEL3_BAD.addItems(
				NoteOfBzmdr.class,
				StarDust.class,
				BrokenRing.class
		);

		PROPS_LEVEL1_CHAOS.addItems(
				DeadOrAlive.class,
				DreamSeed.class,
				CatGirlCosplay.class
		);


		BOOKS.addItems(MagicGirlBooks.class, BrokenBooks.class, GrassKingBooks.class, IceCityBooks.class,
				NoKingMobBooks.class, HollowCityBook.class, DeepBloodBooks.class, DimandBook.class,
				BzmdrBooks.class, DeYiZiBooks.class, MoneyMoreBooks.class, PinkRandomBooks.class,
				HellFireBooks.class, YellowSunBooks.class);

		MISC_EQUIPMENT.addItems(BrokenSeal.class, SpiritBow.class, Waterskin.class, VelvetPouch.class,
				PotionBandolier.class, ScrollHolder.class, MagicalHolster.class, Amulet.class);



		POTIONS.addItems(Generator.Category.POTION.classes);

		SCROLLS.addItems(Generator.Category.SCROLL.classes);

		SEEDS.addItems(Generator.Category.SEED.classes);

		STONES.addItems(Generator.Category.STONE.classes);

		FOOD.addItems( Food.class, Pasty.class, MysteryMeat.class, ChargrilledMeat.class,
				StewedMeat.class, FrozenCarpaccio.class, SmallRation.class, Berry.class,
				Blandfruit.class, PhantomMeat.class, MeatPie.class,
				Switch.class, Cake.class, LightFood.class, RedCrab.class,
				SakaMeat.class, CrivusFruitsFood.class,  SmallRation.BlackMoon.class,

				//FTJ
				BoneSoup.class, RatTail.class, ZakoSoup.class,

				//DWJ
				RiceDumplings.RiceDumplingsBottle.class,
				RiceDumplings.RiceDumplingsLink.class,
				RiceDumplings.RiceDumplingsOrange.class,
				RiceDumplings.RiceDumplingsPink.class,
				RiceDumplings.RiceDumplingsRed.class);

		EXOTIC_POTIONS.addItems(ExoticPotion.exoToReg.keySet().toArray(new Class[0]));


		EXOTIC_SCROLLS.addItems(ExoticScroll.exoToReg.keySet().toArray(new Class[0]));

		BOMBS.addItems( Bomb.class, FrostBomb.class, Firebomb.class, Flashbang.class, RegrowthBomb.class,
				WoollyBomb.class, Noisemaker.class, ShockBomb.class, HolyBomb.class, ArcaneBomb.class, ShrapnelBomb.class);

		TIPPED_DARTS.addItems(TippedDart.types.values().toArray(new Class[0]));

		BREWS_ELIXIRS.addItems( UnstableBrew.class, InfernalBrew.class, BlizzardBrew.class,
				ShockingBrew.class, CausticBrew.class, AquaBrew.class, ElixirOfHoneyedHealing.class,
				ElixirOfAquaticRejuvenation.class, ElixirOfArcaneArmor.class, ElixirOfDragonsBlood.class,
				ElixirOfIcyTouch.class, ElixirOfToxicEssence.class, ElixirOfMight.class, ElixirOfFeatherFall.class,
				WaterSoul.class, ElixirOfNukeCole.class);

		SPELLS.addItems( UnstableSpell.class, WildEnergy.class, TelekineticGrab.class, PhaseShift.class,
				Alchemize.class, CurseInfusion.class, MagicalInfusion.class, Recycle.class,
				ReclaimTrap.class, SummonElemental.class, BeaconOfReturning.class);

		MISC_CONSUMABLES.addItems( Gold.class, EnergyCrystal.class, Dewdrop.class,
				IronKey.class, GoldenKey.class, CrystalKey.class, SkeletonKey.class,
				TrinketCatalyst.class, Stylus.class, Torch.class, Honeypot.class, Ankh.class,
				CorpseDust.class, Embers.class, CeremonialCandle.class, DarkGold.class, DwarfToken.class,
				GooBlob.class, TengusMask.class, MetalShard.class, KingsCrown.class,
				LiquidMetal.class, ArcaneResin.class, PotionOfNoWater.class);

		MINIGAMES.addItems(
				PacManQuest.SmallPoint.class,
				PacManQuest.BigPoint.class,

				PacManQuest.Gumdrop.class,
				PacManQuest.Lollipop.class,
				PacManQuest.Chocolate.class,
				PacManQuest.SugarBomb.class,
				PacManQuest.Toffee.class,

				AllSearchIQuest.THEATER_CARDS.class,
				AllSearchIQuest.HOLLOW_SUGARS.class,
				AllSearchIQuest.GREEN_PRISM.class,
				AllSearchIQuest.GNOLL_WOOD.class,
				AllSearchIQuest.FOUR_KIDS.class,

				AllSearchIQuest.CrystalHeartChoco.class,
				AllSearchIQuest.CreateWorldHeartModel.class,
				AllSearchIQuest.GhostBlueModel.class,
				AllSearchIQuest.GreenDamModel.class,
				AllSearchIQuest.GreenStingModel.class,

				AllSearchIQuest.HollowLantern.class,
				AllSearchIQuest.HollowGoldCards.class,
				AllSearchIQuest.HollowCityProps.class
		);

	}



	public static ArrayList<Catalog> equipmentCatalogs = new ArrayList<>();
	static {
		equipmentCatalogs.add(MELEE_WEAPONS);
		equipmentCatalogs.add(ARMOR);
		equipmentCatalogs.add(ENCHANTMENTS);
		equipmentCatalogs.add(GLYPHS);
		equipmentCatalogs.add(THROWN_WEAPONS);
		equipmentCatalogs.add(WANDS);
		equipmentCatalogs.add(RINGS);
		equipmentCatalogs.add(ARTIFACTS);

		equipmentCatalogs.add(PROPS_LEVEL1_GOOD);
		equipmentCatalogs.add(PROPS_LEVEL2_GOOD);
		equipmentCatalogs.add(PROPS_LEVEL3_GOOD);

		equipmentCatalogs.add(PROPS_LEVEL1_BAD);
		equipmentCatalogs.add(PROPS_LEVEL2_BAD);
		equipmentCatalogs.add(PROPS_LEVEL3_BAD);

		equipmentCatalogs.add(PROPS_LEVEL1_CHAOS);

		equipmentCatalogs.add(TRINKETS);
		equipmentCatalogs.add(BOOKS);
		equipmentCatalogs.add(MISC_EQUIPMENT);
		equipmentCatalogs.add(MINIGAMES);
	}

	public static ArrayList<Catalog> consumableCatalogs = new ArrayList<>();
	static {
		consumableCatalogs.add(POTIONS);
		consumableCatalogs.add(SCROLLS);
		consumableCatalogs.add(SEEDS);
		consumableCatalogs.add(STONES);
		consumableCatalogs.add(FOOD);
		consumableCatalogs.add(EXOTIC_POTIONS);
		consumableCatalogs.add(EXOTIC_SCROLLS);
		consumableCatalogs.add(BOMBS);
		consumableCatalogs.add(TIPPED_DARTS);
		consumableCatalogs.add(BREWS_ELIXIRS);
		consumableCatalogs.add(SPELLS);
		consumableCatalogs.add(MISC_CONSUMABLES);
		consumableCatalogs.add(MINIGAMES);
	}

	public static boolean isSeen(Class<?> cls){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(cls)) {
				return cat.seen.get(cls);
			}
		}
		return false;
	}

	public static void setSeen(Class<?> cls){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(cls) && !cat.seen.get(cls)) {
				cat.seen.put(cls, true);
				Journal.saveNeeded = true;
			}
		}
		Badges.validateCatalogBadges();
	}

	public static int useCount(Class<?> cls){
		for (Catalog cat : values()) {
			if (cat.useCount.containsKey(cls)) {
				return cat.useCount.get(cls);
			}
		}
		return 0;
	}

	public static void countUse(Class<?> cls){
		countUses(cls, 1);
	}

	public static void countUses(Class<?> cls, int uses){
		for (Catalog cat : values()) {
			if (cat.useCount.containsKey(cls) && cat.useCount.get(cls) != Integer.MAX_VALUE) {
				cat.useCount.put(cls, cat.useCount.get(cls)+uses);
				if (cat.useCount.get(cls) < -1_000_000_000){ //to catch cases of overflow
					cat.useCount.put(cls, Integer.MAX_VALUE);
				}
				Journal.saveNeeded = true;
			}
		}
	}

	private static final String CATALOG_CLASSES = "catalog_classes";
	private static final String CATALOG_SEEN    = "catalog_seen";
	private static final String CATALOG_USES    = "catalog_uses";

	public static void store( Bundle bundle ){

		ArrayList<Class<?>> classes = new ArrayList<>();
		ArrayList<Boolean> seen = new ArrayList<>();
		ArrayList<Integer> uses = new ArrayList<>();

		for (Catalog cat : values()) {
			for (Class<?> item : cat.items()) {
				if (cat.seen.get(item) || cat.useCount.get(item) > 0){
					classes.add(item);
					seen.add(cat.seen.get(item));
					uses.add(cat.useCount.get(item));
				}
			}
		}

		Class<?>[] storeCls = new Class[classes.size()];
		boolean[] storeSeen = new boolean[seen.size()];
		int[] storeUses = new int[uses.size()];

		for (int i = 0; i < storeCls.length; i++){
			storeCls[i] = classes.get(i);
			storeSeen[i] = seen.get(i);
			storeUses[i] = uses.get(i);
		}

		bundle.put( CATALOG_CLASSES, storeCls );
		bundle.put( CATALOG_SEEN, storeSeen );
		bundle.put( CATALOG_USES, storeUses );

	}

	//pre-v2.5
	private static final String CATALOG_ITEMS = "catalog_items";

	public static void restore( Bundle bundle ){

		//old logic for pre-v2.5 catalog-specific badges
		Badges.loadGlobal();
		if (bundle.contains(CATALOG_ITEMS)) {
			for (Class<?> cls : Arrays.asList(bundle.getClassArray(CATALOG_ITEMS))){
				for (Catalog cat : values()) {
					if (cat.seen.containsKey(cls)) {
						cat.seen.put(cls, true);
					}
				}
			}
		}
		//end of old logic

		if (bundle.contains(CATALOG_CLASSES)){
			Class<?>[] classes = bundle.getClassArray(CATALOG_CLASSES);
			boolean[] seen = bundle.getBooleanArray(CATALOG_SEEN);
			int[] uses = bundle.getIntArray(CATALOG_USES);

			for (int i = 0; i < classes.length; i++){
				for (Catalog cat : values()) {
					if (cat.seen.containsKey(classes[i])) {
						cat.seen.put(classes[i], seen[i]);
						cat.useCount.put(classes[i], uses[i]);
					}
				}

			}
		}

	}

}