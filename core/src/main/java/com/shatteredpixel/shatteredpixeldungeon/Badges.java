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

package com.shatteredpixel.shatteredpixeldungeon;

import static com.shatteredpixel.shatteredpixeldungeon.Statistics.amuletObtained;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.chalMultiplier;
import static com.shatteredpixel.shatteredpixeldungeon.windows.LevelChecker.A_SCORE;
import static com.shatteredpixel.shatteredpixeldungeon.windows.LevelChecker.B_SCORE;
import static com.shatteredpixel.shatteredpixeldungeon.windows.LevelChecker.SSSP_SCORE;
import static com.shatteredpixel.shatteredpixeldungeon.windows.LevelChecker.SSS_SCORE;
import static com.shatteredpixel.shatteredpixeldungeon.windows.LevelChecker.SS_SCORE;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DragonGirlBlue;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.RemainsItem;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Badges {

	public static void validateDeathFromEnemyMagic() {
		Badge badge = Badge.DEATH_FROM_ENEMY_MAGIC;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}
	public static void validateDeathFromFriendlyMagic() {
		Badge badge = Badge.DEATH_FROM_FRIENDLY_MAGIC;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void validateDeathFromSacrifice() {
		Badge badge = Badge.DEATH_FROM_SACRIFICE;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void KILLMG() {
		displayBadge( Badge.KILL_MG );
    }

	public static void validateRatmogrify(){
		addGlobal(Badge.FOUND_RATMOGRIFY);
	}

	public static void validateItemsCrafted() {
		Badge badge = null;

		if (!local.contains( Badge.ITEMS_CRAFTED_1 ) && Statistics.itemsCrafted >= 5) {
			badge = Badge.ITEMS_CRAFTED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEMS_CRAFTED_2 ) && Statistics.itemsCrafted >= 10) {
			badge = Badge.ITEMS_CRAFTED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEMS_CRAFTED_3 ) && Statistics.itemsCrafted >= 15) {
			badge = Badge.ITEMS_CRAFTED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEMS_CRAFTED_4 ) && Statistics.itemsCrafted >= 20) {
			badge = Badge.ITEMS_CRAFTED_4;
			local.add(badge);
		}

		displayBadge( badge );
	}

	public static HashSet<Badge> global;
	private static HashSet<Badge> local = new HashSet<>();

	private static boolean saveNeeded = false;

	public static void reset() {
		local.clear();
		loadGlobal();
	}

	public static final String BADGES_FILE	= "badges.dat";
	private static final String BADGES		= "badges";

	private static final HashSet<String> removedBadges = new HashSet<>();
	static{
		//no recently removed badges
	}

	private static final HashMap<String, String> renamedBadges = new HashMap<>();
	static{
		//no recently renamed badges
	}

	public static HashSet<Badge> restore( Bundle bundle ) {
		HashSet<Badge> badges = new HashSet<>();
		if (bundle == null) return badges;

		String[] names = bundle.getStringArray( BADGES );
		if (names == null) return badges;

		for (int i=0; i < names.length; i++) {
			try {
				if (renamedBadges.containsKey(names[i])){
					names[i] = renamedBadges.get(names[i]);
				}
				if (!removedBadges.contains(names[i])){
					badges.add( Badge.valueOf( names[i] ) );
				}
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
			}
		}

		addReplacedBadges(badges);

		return badges;
	}

	public static void store( Bundle bundle, HashSet<Badge> badges ) {
		addReplacedBadges(badges);

		int count = 0;
		String[] names = new String[badges.size()];

		for (Badge badge:badges) {
			names[count++] = badge.toString();

		}
		bundle.put( BADGES, names );
	}

	public static void loadLocal( Bundle bundle ) {
		local = restore( bundle );
	}

	public static void saveLocal( Bundle bundle ) {
		store( bundle, local );
	}

	public static void loadGlobal() {
		if (global == null) {
			try {
				Bundle bundle = FileUtils.bundleFromFile( BADGES_FILE );
				global = restore( bundle );

			} catch (IOException e) {
				global = new HashSet<>();
			}
		}
	}

	public static void saveGlobal() {
		if (saveNeeded) {

			Bundle bundle = new Bundle();
			store( bundle, global );

			try {
				FileUtils.bundleToFile(BADGES_FILE, bundle);
				saveNeeded = false;
			} catch (IOException e) {
				ShatteredPixelDungeon.reportException(e);
			}
		}
	}

	public static int unlocked(boolean global){
		if (global) return Badges.global.size();
		else        return Badges.local.size();
	}

	public static void validateMonstersSlain() {
		Badge badge = null;

		if (!local.contains( Badge.MONSTERS_SLAIN_1 ) && Statistics.enemiesSlain >= 10) {
			badge = Badge.MONSTERS_SLAIN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_2 ) && Statistics.enemiesSlain >= 50) {
			badge = Badge.MONSTERS_SLAIN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_3 ) && Statistics.enemiesSlain >= 150) {
			badge = Badge.MONSTERS_SLAIN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_4 ) && Statistics.enemiesSlain >= 250) {
			badge = Badge.MONSTERS_SLAIN_4;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateHighScore( int score ){
		Badge badge = null;
		if (score >= B_SCORE * chalMultiplier * (amuletObtained ? 1 : 2)) {
			badge = Badge.HIGH_SCORE_1;
			local.add( badge );
		}
		if (score >= A_SCORE * chalMultiplier * (amuletObtained ? 1 : 2)) {
			badge = Badge.HIGH_SCORE_2;
			local.add( badge );
		}
		if (score >= SS_SCORE * chalMultiplier * (amuletObtained ? 1 : 3)) {
			badge = Badge.HIGH_SCORE_3;
			local.add( badge );
		}
		if (score >= SSS_SCORE * chalMultiplier/2 * (amuletObtained ? 1 : 4)) {
			badge = Badge.HIGH_SCORE_4;
			local.add( badge );
		}
		if (score >= SSSP_SCORE * chalMultiplier * (amuletObtained ? 1 : 5)) {
			badge = Badge.HIGH_SCORE_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateGoldCollected() {
		Badge badge = null;

		if (!local.contains( Badge.GOLD_COLLECTED_1 ) && Statistics.goldCollected >= 100) {
			badge = Badge.GOLD_COLLECTED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_2 ) && Statistics.goldCollected >= 500) {
			badge = Badge.GOLD_COLLECTED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_3 ) && Statistics.goldCollected >= 2500) {
			badge = Badge.GOLD_COLLECTED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_4 ) && Statistics.goldCollected >= 7500) {
			badge = Badge.GOLD_COLLECTED_4;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void valiReadBooks() {
		Badge badge = null;

		if (!local.contains( Badge.READ_BOOK_ONE ) && Statistics.readBooks >= 1) {
			badge = Badge.READ_BOOK_ONE;
			local.add( badge );
		}
		if (!local.contains( Badge.READ_BOOK_TWO ) && Statistics.readBooks >= 3) {
			badge = Badge.READ_BOOK_TWO;
			local.add( badge );
		}
		if (!local.contains( Badge.READ_BOOK_THREE ) && Statistics.readBooks >= 5) {
			badge = Badge.READ_BOOK_THREE;
			local.add( badge );
		}
		if (!local.contains( Badge.READ_BOOK_FOUR ) && Statistics.readBooks >= 7) {
			badge = Badge.READ_BOOK_FOUR;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void nyzvalidateGoldCollected() {
		Badge badge = null;

		if (!local.contains( Badge.NYZ_SHOP ) && Statistics.naiyaziCollected >= 7) {
			badge = Badge.NYZ_SHOP;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void GhostDageCollected() {
		Badge badge = null;

		if (!local.contains( Badge.GHOSTDAGE ) && Statistics.dageCollected == 1) {
			badge = Badge.GHOSTDAGE;
			local.add( badge );
		}
		if (!local.contains( Badge.DAGETO ) && Statistics.dageCollected >= 2) {
			badge = Badge.DAGETO;
			local.add( badge );
		}
		displayBadge( badge );
	}

	public static void validateLevelReached() {
		Badge badge = null;

		if (!local.contains( Badge.LEVEL_REACHED_1 ) && Dungeon.hero.lvl >= 6) {
			badge = Badge.LEVEL_REACHED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_2 ) && Dungeon.hero.lvl >= 12) {
			badge = Badge.LEVEL_REACHED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_3 ) && Dungeon.hero.lvl >= 18) {
			badge = Badge.LEVEL_REACHED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_4 ) && Dungeon.hero.lvl >= 24) {
			badge = Badge.LEVEL_REACHED_4;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateStrengthAttained() {
		Badge badge = null;

		if (!local.contains( Badge.STRENGTH_ATTAINED_1 ) && Dungeon.hero.STR >= 13) {
			badge = Badge.STRENGTH_ATTAINED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_2 ) && Dungeon.hero.STR >= 15) {
			badge = Badge.STRENGTH_ATTAINED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_3 ) && Dungeon.hero.STR >= 17) {
			badge = Badge.STRENGTH_ATTAINED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_4 ) && Dungeon.hero.STR >= 19) {
			badge = Badge.STRENGTH_ATTAINED_4;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateAncityProgress() {
		Badge badge = null;

		if (!local.contains( Badge.ANCITY_ONE ) && DragonGirlBlue.Quest.survey_research_points >= 1200) {
			badge = Badge.ANCITY_ONE;
			local.add( badge );
		}
		if (!local.contains( Badge.ANCITY_TWO ) && DragonGirlBlue.Quest.survey_research_points >= 2400) {
			badge = Badge.ANCITY_TWO;
			local.add( badge );
		}
		if (!local.contains( Badge.ANCITY_THREE ) && DragonGirlBlue.Quest.survey_research_points >= 4000) {
			badge = Badge.ANCITY_THREE;
			local.add( badge );
		}
		displayBadge( badge );
	}

	public static void validateFoodEaten() {
		Badge badge = null;

		if (!local.contains( Badge.FOOD_EATEN_1 ) && Statistics.foodEaten >= 10) {
			badge = Badge.FOOD_EATEN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_2 ) && Statistics.foodEaten >= 20) {
			badge = Badge.FOOD_EATEN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_3 ) && Statistics.foodEaten >= 30) {
			badge = Badge.FOOD_EATEN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_4 ) && Statistics.foodEaten >= 40) {
			badge = Badge.FOOD_EATEN_4;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validatePiranhasKilled() {
		Badge badge = null;

		if (!local.contains( Badge.PIRANHAS ) && Statistics.piranhasKilled >= 6) {
			badge = Badge.PIRANHAS;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateItemLevelAquired( Item item ) {

		// This method should be called:
		// 1) When an item is obtained (Item.collect)
		// 2) When an item is upgraded (ScrollOfUpgrade, ScrollOfWeaponUpgrade, ShortSword, WandOfMagicMissile)
		// 3) When an item is identified

		// Note that artifacts should never trigger this badge as they are alternatively upgraded
		if (!item.levelKnown || item instanceof Artifact) {
			return;
		}

		Badge badge = null;
		if (!local.contains( Badge.ITEM_LEVEL_1 ) && item.level() >= 3) {
			badge = Badge.ITEM_LEVEL_1;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_2 ) && item.level() >= 6) {
			badge = Badge.ITEM_LEVEL_2;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_3 ) && item.level() >= 9) {
			badge = Badge.ITEM_LEVEL_3;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_4 ) && item.level() >= 12) {
			badge = Badge.ITEM_LEVEL_4;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateAllBagsBought( Item bag ) {

		Badge badge = null;
		if (bag instanceof VelvetPouch) {
			badge = Badge.BAG_BOUGHT_SEED_POUCH;
		} else if (bag instanceof ScrollHolder) {
			badge = Badge.BAG_BOUGHT_SCROLL_HOLDER;
		} else if (bag instanceof PotionBandolier) {
			badge = Badge.BAG_BOUGHT_POTION_BANDOLIER;
		} else if (bag instanceof MagicalHolster) {
			badge = Badge.BAG_BOUGHT_WAND_HOLSTER;
		}

		if (badge != null) {

			local.add( badge );

			if (!local.contains( Badge.ALL_BAGS_BOUGHT ) &&
					local.contains( Badge.BAG_BOUGHT_SEED_POUCH ) &&
					local.contains( Badge.BAG_BOUGHT_SCROLL_HOLDER ) &&
					local.contains( Badge.BAG_BOUGHT_POTION_BANDOLIER ) &&
					local.contains( Badge.BAG_BOUGHT_WAND_HOLSTER )) {

				badge = Badge.ALL_BAGS_BOUGHT;
				local.add( badge );
				displayBadge( badge );
			}
		}
	}

	public static void validateItemsIdentified() {

		for (Catalog cat : Catalog.values()){
			if (cat.allSeen()){
				Badge b = Catalog.catalogBadges.get(cat);
				if (!global.contains(b)){
					displayBadge(b);
				}
			}
		}

		if (!global.contains( Badge.ALL_ITEMS_IDENTIFIED ) &&
				global.contains( Badge.ALL_WEAPONS_IDENTIFIED ) &&
				global.contains( Badge.ALL_ARMOR_IDENTIFIED ) &&
				global.contains( Badge.ALL_WANDS_IDENTIFIED ) &&
				global.contains( Badge.ALL_RINGS_IDENTIFIED ) &&
				global.contains( Badge.ALL_ARTIFACTS_IDENTIFIED ) &&
				global.contains( Badge.ALL_POTIONS_IDENTIFIED ) &&
				global.contains( Badge.ALL_SCROLLS_IDENTIFIED )) {

			displayBadge( Badge.ALL_ITEMS_IDENTIFIED );
		}
	}

	public static void validateDeathFromFire() {
		Badge badge = Badge.DEATH_FROM_FIRE;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void ENDDIED() {
//		Badge badge = Badge.ENDIED;
//		local.add( badge );
//		displayBadge( badge );
//
//		validateYASD();
	}

	public static void validateDeathFromPoison() {
		Badge badge = Badge.DEATH_FROM_POISON;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void validateDeathFromGas() {
		Badge badge = Badge.DEATH_FROM_GAS;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void validateDeathFromHunger() {
		Badge badge = Badge.DEATH_FROM_HUNGER;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void validateDeathFromFalling() {
		Badge badge = Badge.DEATH_FROM_FALLING;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void KILL_ROTHEART() {
		Badge badge = Badge.KILL_ROTHEART;
		local.add( badge );
		displayBadge( badge );

		validateGOODMAKE();
	}

	public static void GET_SC() {
		Badge badge = Badge.GET_SC;
		local.add( badge );
		displayBadge( badge );

		validateGOODMAKE();
	}

	public static void KILL_COLDELE() {
		Badge badge = Badge.KILL_COLDELE;
		local.add( badge );
		displayBadge( badge );

		validateGOODMAKE();
	}

	public static void HALOFIRE_DIED() {
		Badge badge = Badge.HALOFIRE_DIED;
		local.add( badge );
		displayBadge( badge );
	}

	public static void BRUTE_DIED() {
		Badge badge = Badge.BRUTE_BOT_DIED;
		local.add( badge );
		displayBadge( badge );
	}

//	public static void BOMB() {
//		Badge badge = Badge.BOMBBOW_DIED;
//		local.add( badge );
//		displayBadge( badge );
//	}

	private static void validateGOODMAKE() {
		if (global.contains( Badge.KILL_ROTHEART ) &&
				global.contains( Badge.GET_SC ) &&
				global.contains( Badge.KILL_COLDELE)) {

			PaswordBadges.Badge badge = PaswordBadges.Badge.GODD_MAKE;
			PaswordBadges.displayBadge( badge );
		}
	}



	private static void validateYASD() {
		if (global.contains( Badge.DEATH_FROM_FIRE ) &&
				global.contains( Badge.DEATH_FROM_POISON ) &&
				global.contains( Badge.DEATH_FROM_GAS ) &&
				global.contains( Badge.DEATH_FROM_HUNGER) &&
				global.contains( Badge.DEATH_FROM_FALLING) && global.contains( Badge.HALOFIRE_DIED) && global.contains( Badge.BRUTE_BOT_DIED) && global.contains( Badge.DEATH_FROM_FRIENDLY_MAGIC) && global.contains( Badge.DEATH_FROM_SACRIFICE)) {

			Badge badge = Badge.YASD;
			displayBadge( badge );
		}
	}

	public static void validateBossSlain() {
		Badge badge = null;
		switch (Dungeon.depth) {
			case 5:
				badge = Badge.BOSS_SLAIN_1;
				break;
			case 10:
				badge = Badge.BOSS_SLAIN_2;
				break;
			case 15:
				badge = Badge.BOSS_SLAIN_3;
				break;
			case 20:
				badge = Badge.BOSS_SLAIN_4;
				break;
		}

		if (badge != null) {
			local.add( badge );
			displayBadge( badge );

			if (badge == Badge.BOSS_SLAIN_1) {
				switch (Dungeon.hero.heroClass) {
					case WARRIOR:
						badge = Badge.BOSS_SLAIN_1_WARRIOR;
						break;
					case MAGE:
						badge = Badge.BOSS_SLAIN_1_MAGE;
						break;
					case ROGUE:
						badge = Badge.BOSS_SLAIN_1_ROGUE;
						break;
					case HUNTRESS:
						badge = Badge.BOSS_SLAIN_1_HUNTRESS;
						break;
				}
				local.add( badge );
				if (!global.contains( badge )) {
					global.add( badge );
					saveNeeded = true;
				}

				if (global.contains( Badge.BOSS_SLAIN_1_WARRIOR ) &&
						global.contains( Badge.BOSS_SLAIN_1_MAGE ) &&
						global.contains( Badge.BOSS_SLAIN_1_ROGUE ) &&
						global.contains( Badge.BOSS_SLAIN_1_HUNTRESS)) {

					badge = Badge.BOSS_SLAIN_1_ALL_CLASSES;
					if (!global.contains( badge )) {
						displayBadge( badge );
						global.add( badge );
						saveNeeded = true;
					}
				}
			} else
			if (badge == Badge.BOSS_SLAIN_3) {
				switch (Dungeon.hero.subClass) {
					case GLADIATOR:
						badge = Badge.BOSS_SLAIN_3_GLADIATOR;
						break;
					case BERSERKER:
						badge = Badge.BOSS_SLAIN_3_BERSERKER;
						break;
					case WARLOCK:
						badge = Badge.BOSS_SLAIN_3_WARLOCK;
						break;
					case BATTLEMAGE:
						badge = Badge.BOSS_SLAIN_3_BATTLEMAGE;
						break;
					case FREERUNNER:
						badge = Badge.BOSS_SLAIN_3_FREERUNNER;
						break;
					case ASSASSIN:
						badge = Badge.BOSS_SLAIN_3_ASSASSIN;
						break;
					case SNIPER:
						badge = Badge.BOSS_SLAIN_3_SNIPER;
						break;
					case WARDEN:
						badge = Badge.BOSS_SLAIN_3_WARDEN;
						break;
					case CHAMPION:
						badge = Badge.BOSS_SLAIN_3_CHAMPION;
						break;
					case MONK:
						badge = Badge.BOSS_SLAIN_3_MONK;
						break;
					default:
						return;
				}
				local.add( badge );
				if (!global.contains( badge )) {
					global.add( badge );
					saveNeeded = true;
				}

				if (global.contains( Badge.BOSS_SLAIN_3_GLADIATOR ) &&
						global.contains( Badge.BOSS_SLAIN_3_BERSERKER ) &&
						global.contains( Badge.BOSS_SLAIN_3_WARLOCK ) &&
						global.contains( Badge.BOSS_SLAIN_3_BATTLEMAGE ) &&
						global.contains( Badge.BOSS_SLAIN_3_FREERUNNER ) &&
						global.contains( Badge.BOSS_SLAIN_3_ASSASSIN ) &&
						global.contains( Badge.BOSS_SLAIN_3_SNIPER ) &&
						global.contains( Badge.BOSS_SLAIN_3_WARDEN ) &&
						global.contains( Badge.BOSS_SLAIN_3_CHAMPION ) &&
						global.contains( Badge.BOSS_SLAIN_3_MONK )) {

					badge = Badge.BOSS_SLAIN_3_ALL_SUBCLASSES;
					if (!global.contains( badge )) {
						displayBadge( badge );
						global.add( badge );
						saveNeeded = true;
					}
				}
			}
		}
	}

	public static void validateMastery() {

		Badge badge = null;
		switch (Dungeon.hero.heroClass) {
			case WARRIOR:
				badge = Badge.MASTERY_WARRIOR;
				break;
			case MAGE:
				badge = Badge.MASTERY_MAGE;
				break;
			case ROGUE:
				badge = Badge.MASTERY_ROGUE;
				break;
			case HUNTRESS:
				badge = Badge.MASTERY_HUNTRESS;
				break;
			case DUELIST:
				badge = Badge.MASTERY_DUELIST;
				break;
		}

		unlock(badge);
	}

	public static void unlock( Badge badge ){
		if (!isUnlocked(badge)){
			global.add( badge );
			saveNeeded = true;
		}
	}

	public static void validateMageUnlock(){
		if (Statistics.upgradesUsed >= 1 && !isUnlocked(Badge.UNLOCK_MAGE)){
			displayBadge( Badge.UNLOCK_MAGE );
		}
	}

	public static void validateRogueUnlock(){
		if (Statistics.sneakAttacks >= 10 && !isUnlocked(Badge.UNLOCK_ROGUE)){
			displayBadge( Badge.UNLOCK_ROGUE );
		}
	}

	public static void validateHuntressUnlock(){
		if (Statistics.thrownAssists >= 10 && !isUnlocked(Badge.UNLOCK_HUNTRESS)){
			displayBadge( Badge.UNLOCK_HUNTRESS );
		}
	}

	public static void validateMasteryCombo( int n ) {
		if (!local.contains( Badge.MASTERY_COMBO ) && n == 10) {
			Badge badge = Badge.MASTERY_COMBO;
			local.add( badge );
			displayBadge( badge );
		}
	}

    public static void KILL_DOG() {
		displayBadge( Badge.KILL_DOG );
    }

	public static void KILL_ST() {
		displayBadge( Badge.KILL_CLSISTER );
	}

	public static void KILL_FIRE() {
		displayBadge( Badge.KILL_FIRE_DRAGON );
	}


	public static void CITY_END() {
		displayBadge( Badge.HOLLOWCITY );
	}

	public static void WOC() {
		displayBadge( Badge.WOC_MONEY_GIRL);
	}

	public static void validateVictory() {

		Statistics.winGame = true;

		Badge badge = Badge.VICTORY;
		displayBadge( badge );

		switch (Dungeon.hero.heroClass) {
			case WARRIOR:
				badge = Badge.VICTORY_WARRIOR;
				break;
			case MAGE:
				badge = Badge.VICTORY_MAGE;
				break;
			case ROGUE:
				badge = Badge.VICTORY_ROGUE;
				break;
			case HUNTRESS:
				badge = Badge.VICTORY_HUNTRESS;
				break;
			case DUELIST:
				badge = Badge.VICTORY_DUELIST;
				break;
		}
		local.add( badge );
		if (!global.contains( badge )) {
			global.add( badge );
			saveNeeded = true;
		}

		if (global.contains( Badge.VICTORY_WARRIOR ) &&
				global.contains( Badge.VICTORY_MAGE ) &&
				global.contains( Badge.VICTORY_ROGUE ) &&
				global.contains( Badge.VICTORY_HUNTRESS )&&
				global.contains( Badge.VICTORY_DUELIST )) {

			badge = Badge.VICTORY_ALL_CLASSES;
			displayBadge( badge );
		}
	}

	public static void validateNoKilling() {
		if (!local.contains( Badge.NO_MONSTERS_SLAIN ) && Statistics.completedWithNoKilling) {
			Badge badge = Badge.NO_MONSTERS_SLAIN;
			local.add( badge );
			displayBadge( badge );
		}
	}

	public static void validateGrimWeapon() {
		if (!local.contains( Badge.GRIM_WEAPON )) {
			Badge badge = Badge.GRIM_WEAPON;
			local.add( badge );
			displayBadge( badge );
		}
	}

	public static void validateGamesPlayed() {
		Badge badge = null;
		if (Rankings.INSTANCE.totalNumber >= 10) {
			badge = Badge.GAMES_PLAYED_1;
		}
		if (Rankings.INSTANCE.totalNumber >= 50) {
			badge = Badge.GAMES_PLAYED_2;
		}
		if (Rankings.INSTANCE.totalNumber >= 75) {
			badge = Badge.GAMES_PLAYED_3;
		}
		if (Rankings.INSTANCE.totalNumber >= 100) {
			badge = Badge.GAMES_PLAYED_4;
		}

		displayBadge( badge );
	}

	//necessary in order to display the happy end badge in the surface scene
	public static void silentValidateHappyEnd() {
		if (!local.contains( Badge.HAPPY_END )){
			local.add( Badge.HAPPY_END );
		}
	}

	public static void validateHappyEnd() {
		displayBadge( Badge.HAPPY_END );
	}

	public static void silentValidateHDEX() {
		if (!local.contains( Badge.HIDEEN_BADAGEX)){
			local.add( Badge.HIDEEN_BADAGEX);
		}
	}

	public static void HDEX() {
		displayBadge( Badge.HIDEEN_BADAGEX);
	}


	public static void KILLSAPPLE() {
		displayBadge( Badge.KILL_APPLE);
	}





	public static void STORM() {
		displayBadge( Badge.STORM);
	}

	public static void KILLSDM720() {
		displayBadge( Badge.KILL_DM720 );
	}

	public static void BOSSTHREE() {
		displayBadge( Badge.BOSS_SLAIN_3 );
	}

	public static void CLEARWATER() {
		displayBadge( Badge.CLEAR_WATER );
	}

    public static void KILL_SMK() {
        displayBadge(Badge.KILL_SM);
    }

    public static void GOODRLPT() {
        displayBadge(Badge.RLPT);
    }

	public static void GOO() {
		displayBadge(Badge.BOSS_CHALLENGE_1);
	}

    public static void validateBossChallengeCompleted() {
        Badge badge = null;
        switch (Dungeon.depth) {
            case 10:
                badge = Badge.BOSS_CHALLENGE_2;
                break;
            case 15:
                badge = Badge.BOSS_CHALLENGE_3;
                break;
            case 20:
                badge = Badge.BOSS_CHALLENGE_4;
                break;
            case 25:
                badge = Badge.BOSS_CHALLENGE_5;
                break;
        }

        if (badge != null) {
            local.add(badge);
            displayBadge(badge);
        }
    }

    public static void validateChampion(int challenges) {
        if (challenges == 0) return;
        Badge badge = null;

		boolean isNoChampion = Statistics.bossRushMode || Statistics.RandMode;

        if (challenges >= 1 && !(Dungeon.isDLC(Conducts.Conduct.DEV)) || !isNoChampion && challenges >= 1) {
            badge = Badge.CHAMPION_1X;
        }
        if (challenges >= 3 && !(Dungeon.isDLC(Conducts.Conduct.DEV)) || !isNoChampion && challenges >= 3) {
            addGlobal(badge);
			badge = Badge.CHAMPION_2X;
		}
		if (challenges >= 6 && !(Dungeon.isDLC(Conducts.Conduct.DEV))|| !isNoChampion && challenges >= 6){
			addGlobal(badge);
			badge = Badge.CHAMPION_3X;
		}
		if (challenges >= 8 && !(Dungeon.isDLC(Conducts.Conduct.DEV))||!isNoChampion && challenges >= 8){
			addGlobal(badge);
			badge = Badge.CHAMPION_4X;
		}
		if (challenges >= 10 && !(Dungeon.isDLC(Conducts.Conduct.DEV))||!isNoChampion && challenges >= 10){
			addGlobal(badge);
			badge = Badge.CHAMPION_5X;
		}
		local.add(badge);
		displayBadge( badge );
	}


	public enum BadgeType {
		HIDDEN, //internal badges used for data tracking
		LOCAL,  //unlocked on a per-run basis and added to overall player profile
		GLOBAL, //unlocked for the save profile only, usually over multiple runs
		JOURNAL //profile-based and also tied to the journal, which means they even unlock in seeded runs
	}

	public enum Badge {
		MASTERY_WARRIOR,
		MASTERY_MAGE,
		MASTERY_ROGUE,
		MASTERY_HUNTRESS,
		MASTERY_DUELIST,
		FOUND_RATMOGRIFY,

		//bronze
		UNLOCK_MAGE                 ( 1 ),
		UNLOCK_ROGUE                ( 2 ),
		UNLOCK_HUNTRESS             ( 3 ),
		MONSTERS_SLAIN_1            ( 4 ),
		MONSTERS_SLAIN_2            ( 5 ),
		GOLD_COLLECTED_1            ( 6 ),
		GOLD_COLLECTED_2            ( 7 ),
		ITEM_LEVEL_1                ( 8 ),
		LEVEL_REACHED_1             ( 9 ),
		STRENGTH_ATTAINED_1         ( 10 ),
		FOOD_EATEN_1                ( 11 ),
		ITEMS_CRAFTED_1             ( 12 ),
		BOSS_SLAIN_1                ( 13 ),
		DEATH_FROM_FIRE             ( 14 ),
		DEATH_FROM_POISON           ( 15 ),
		DEATH_FROM_GAS              ( 16 ),
		DEATH_FROM_HUNGER           ( 17 ),
		DEATH_FROM_FALLING          ( 18 ),
		HIGH_SCORE_1 				( 19 ),
		KILL_ROTHEART         			( 20 ),
		GET_SC        			( 21 ),
		KILL_COLDELE        			( 22 ),

		HALOFIRE_DIED					( 23 ),

		READ_BOOK_ONE				( 24 ),

		BRUTE_BOT_DIED				( 25 ),

		ANCITY_ONE					(27),
		UNLOCK_DUELIST              ( 4 ),
		//UNLOCK_CLERIC             ( 5 ),
		MONSTERS_SLAIN_1            ( 6 ),
		MONSTERS_SLAIN_2            ( 7 ),
		GOLD_COLLECTED_1            ( 8 ),
		GOLD_COLLECTED_2            ( 9 ),
		ITEM_LEVEL_1                ( 10 ),
		LEVEL_REACHED_1             ( 11 ),
		STRENGTH_ATTAINED_1         ( 12 ),
		FOOD_EATEN_1                ( 13 ),
		ITEMS_CRAFTED_1             ( 14 ),
		BOSS_SLAIN_1                ( 15 ),
		CATALOG_ONE_EQUIPMENT       ( 16, BadgeType.JOURNAL ),
		DEATH_FROM_FIRE             ( 17 ),
		DEATH_FROM_POISON           ( 18 ),
		DEATH_FROM_GAS              ( 19 ),
		DEATH_FROM_HUNGER           ( 20 ),
		DEATH_FROM_FALLING          ( 21 ),
		RESEARCHER_1                ( 22, BadgeType.JOURNAL ),
		GAMES_PLAYED_1              ( 23, BadgeType.GLOBAL ),
		HIGH_SCORE_1                ( 24 ),

		//silver
		NO_MONSTERS_SLAIN           ( 32 ),
		GRIM_WEAPON                 ( 33 ),
		MONSTERS_SLAIN_3            ( 34 ),
		MONSTERS_SLAIN_4            ( 35 ),
		GOLD_COLLECTED_3            ( 36 ),
		GOLD_COLLECTED_4            ( 37 ),
		ITEM_LEVEL_2                ( 38 ),
		ITEM_LEVEL_3                ( 39 ),
		LEVEL_REACHED_2             ( 40 ),
		LEVEL_REACHED_3             ( 41 ),
		STRENGTH_ATTAINED_2         ( 42 ),
		STRENGTH_ATTAINED_3         ( 43 ),
		FOOD_EATEN_2                ( 44 ),
		FOOD_EATEN_3                ( 45 ),
		ITEMS_CRAFTED_2             ( 46 ),
		ITEMS_CRAFTED_3             ( 47 ),
		BOSS_SLAIN_2                ( 48 ),
		BOSS_SLAIN_3                ( 49 ),
		ALL_POTIONS_IDENTIFIED      ( 50 ),
		ALL_SCROLLS_IDENTIFIED      ( 51 ),
		ALL_POTIONS_IDENTIFIED      , //still exists internally for pre-2.5 saves
		ALL_SCROLLS_IDENTIFIED      , //still exists internally for pre-2.5 saves
		CATALOG_POTIONS_SCROLLS     ( 50 ),
		DEATH_FROM_ENEMY_MAGIC      ( 51 ),
		DEATH_FROM_FRIENDLY_MAGIC   ( 52 ),
		DEATH_FROM_SACRIFICE        ( 53 ),
		BOSS_SLAIN_1_WARRIOR,
		BOSS_SLAIN_1_MAGE,
		BOSS_SLAIN_1_ROGUE,
		BOSS_SLAIN_1_HUNTRESS,

		BOSS_SLAIN_1_DUELIST,
		BOSS_SLAIN_1_ALL_CLASSES    ( 53, true ),
		GAMES_PLAYED_1              ( 54, true ),

		HIGH_SCORE_2				( 55 ),

		READ_BOOK_TWO				( 56 ),

		HIDEEN_BADAGEX( 57 ),

		//伏法
		DEATH_FROM_FRIENDLY_MAGIC(58),

		//上好寄品
		DEATH_FROM_SACRIFICE(59),

		//死于敌方法术
		DEATH_FROM_ENEMY_MAGIC(60),

		ANCITY_TWO	(61),


		//gold
		PIRANHAS                    ( 64 ),
		//these names are a bit outdated, but it doesn't really matter.

		ALL_BAGS_BOUGHT             ( 65 ),
		MASTERY_COMBO               ( 66 ),
		ITEM_LEVEL_4                ( 67 ),
		LEVEL_REACHED_4             ( 68 ),
		STRENGTH_ATTAINED_4         ( 69 ),
		FOOD_EATEN_4                ( 70 ),
		ITEMS_CRAFTED_4            ( 71 ),
		BOSS_SLAIN_4                ( 72 ),
		ALL_WEAPONS_IDENTIFIED      ( 73 ),
		ALL_ARMOR_IDENTIFIED        ( 74 ),
		ALL_WANDS_IDENTIFIED        ( 75 ),
		ALL_RINGS_IDENTIFIED        ( 76 ),
		ALL_ARTIFACTS_IDENTIFIED    ( 77 ),
		VICTORY                     ( 78 ),
		YASD                        ( 79, true ),


		CLEAR_WATER					( 83 ),
		GHOSTDAGE					( 84 ),
//		ENDIED					( 85 ),

		HIGH_SCORE_3 				( 86 ),
		BOSS_SLAIN_1_ALL_CLASSES    ( 54, BadgeType.GLOBAL ),
		RESEARCHER_2                ( 55, BadgeType.JOURNAL ),
		GAMES_PLAYED_2              ( 56, BadgeType.GLOBAL ),
		HIGH_SCORE_2                ( 57 ),

		//gold
		PIRANHAS                    ( 64 ),
		GRIM_WEAPON                 ( 65 ),
		BAG_BOUGHT_VELVET_POUCH,
		BAG_BOUGHT_SCROLL_HOLDER,
		BAG_BOUGHT_POTION_BANDOLIER,
		BAG_BOUGHT_MAGICAL_HOLSTER,
		ALL_BAGS_BOUGHT             ( 66 ),
		MASTERY_COMBO               ( 67 ),
		MONSTERS_SLAIN_5            ( 68 ),
		GOLD_COLLECTED_5            ( 69 ),
		ITEM_LEVEL_4                ( 70 ),
		LEVEL_REACHED_4             ( 71 ),
		STRENGTH_ATTAINED_4         ( 72 ),
		STRENGTH_ATTAINED_5         ( 73 ),
		FOOD_EATEN_4                ( 74 ),
		FOOD_EATEN_5                ( 75 ),
		ITEMS_CRAFTED_4             ( 76 ),
		ITEMS_CRAFTED_5             ( 77 ),
		BOSS_SLAIN_4                ( 78 ),
		ALL_RINGS_IDENTIFIED        , //still exists internally for pre-2.5 saves
		ALL_ARTIFACTS_IDENTIFIED    , //still exists internally for pre-2.5 saves
		ALL_RARE_ENEMIES            ( 79, BadgeType.JOURNAL ),
		DEATH_FROM_GRIM_TRAP        ( 80 ), //also disintegration traps
		VICTORY                     ( 81 ),
		BOSS_CHALLENGE_1            ( 82 ),
		BOSS_CHALLENGE_2            ( 83 ),
		RESEARCHER_3                ( 84, BadgeType.JOURNAL ),
		GAMES_PLAYED_3              ( 85, BadgeType.GLOBAL ),
		HIGH_SCORE_3                ( 86 ),

		//platinum
		ITEM_LEVEL_5                ( 96 ),
		LEVEL_REACHED_5             ( 97 ),
		HAPPY_END                   ( 98 ),
		HAPPY_END_REMAINS           ( 99 ),
		RODNEY                      ( 100, BadgeType.JOURNAL ),
		ALL_WEAPONS_IDENTIFIED      , //still exists internally for pre-2.5 saves
		ALL_ARMOR_IDENTIFIED        , //still exists internally for pre-2.5 saves
		ALL_WANDS_IDENTIFIED        , //still exists internally for pre-2.5 saves
		ALL_ITEMS_IDENTIFIED        , //still exists internally for pre-2.5 saves
		VICTORY_WARRIOR,
		VICTORY_MAGE,
		VICTORY_ROGUE,
		VICTORY_HUNTRESS,
		VICTORY_DUELIST,
		VICTORY_ALL_CLASSES         ( 101, BadgeType.GLOBAL ),
		DEATH_FROM_ALL              ( 102, BadgeType.GLOBAL ),
		BOSS_SLAIN_3_GLADIATOR,
		BOSS_SLAIN_3_BERSERKER,
		BOSS_SLAIN_3_WARLOCK,
		BOSS_SLAIN_3_BATTLEMAGE,
		BOSS_SLAIN_3_FREERUNNER,
		BOSS_SLAIN_3_ASSASSIN,
		BOSS_SLAIN_3_SNIPER,
		BOSS_SLAIN_3_WARDEN,
		BOSS_SLAIN_3_CHAMPION,
		BOSS_SLAIN_3_MONK,
		BOSS_SLAIN_3_ALL_SUBCLASSES ( 80, true ),
		GAMES_PLAYED_2              ( 81, true ),

		READ_BOOK_THREE				( 87 ),


		HOLLOWCITY					(  ),
		MASTER						( ),
		RED_DRAMATICUL				( ),
		GOD_PLEASE					( ),

		NOW_ANTATTCK				(),
		WOC_MONEY_GIRL				(93),


		//platinum
		HAPPY_END                   ( 96 ),
		ALL_ITEMS_IDENTIFIED        ( 97, true ),
		VICTORY_WARRIOR,
		VICTORY_MAGE,
		VICTORY_ROGUE,
		VICTORY_HUNTRESS,
		VICTORY_DUELIST,
		VICTORY_ALL_CLASSES         ( 98, true ),
		GAMES_PLAYED_3              ( 99, true ),
		CHAMPION_1X                  ( 100 ),
		KILL_APPLE(101),
		KILL_DM720				(102),
		RLPT				(103),

		HIGH_SCORE_4 				( 104 ),

		READ_BOOK_FOUR				( 105 ),

		//diamond
		GAMES_PLAYED_4              ( 112, true ),
		CHAMPION_2X                  ( 113 ),
		CHAMPION_3X                  ( 114 ),
		CHAMPION_4X                  ( 115 ),
		CHAMPION_5X                  ( 116 ),
		NYZ_SHOP                    ( 117 ),
        DAGETO						(118),
        KILL_SM(119),
        //rudy
		BOSS_SLAIN_3_ALL_SUBCLASSES ( 103, BadgeType.GLOBAL ),
		BOSS_CHALLENGE_3            ( 104 ),
		BOSS_CHALLENGE_4            ( 105 ),
		RESEARCHER_4                ( 106, BadgeType.JOURNAL ),
		GAMES_PLAYED_4              ( 107, BadgeType.GLOBAL ),
		HIGH_SCORE_4                ( 108 ),
		CHAMPION_1                  ( 109 ),

		//diamond
		BOSS_CHALLENGE_5            ( 120 ),
		RESEARCHER_5                ( 121, BadgeType.JOURNAL ),
		GAMES_PLAYED_5              ( 122, BadgeType.GLOBAL ),
		HIGH_SCORE_5                ( 123 ),
		CHAMPION_2                  ( 124 ),
		CHAMPION_3                  ( 125 );


        STORM(132),

        KILL_MG(133),

        HIGH_SCORE_5(137),

		KILL_DOG(138),

		KILL_CLSISTER(140),

		KILL_FIRE_DRAGON(141),

        BOSS_CHALLENGE_1(152),
        BOSS_CHALLENGE_2(153),
        BOSS_CHALLENGE_3(154),
        BOSS_CHALLENGE_4(155),
        BOSS_CHALLENGE_5(156),

		ANCITY_THREE	(157),

		BAG_BOUGHT_SEED_POUCH,
		BAG_BOUGHT_SCROLL_HOLDER,
		BAG_BOUGHT_POTION_BANDOLIER,
		BAG_BOUGHT_WAND_HOLSTER;


        public boolean meta;

        public int image;

        public String title() {
            return Messages.get(this, name() + ".title");
        }

        public String desc(){
			return Messages.get(this, name()+".desc");
		}

		Badge( int image ) {
			this( image, false );
		}

		Badge( int image, boolean meta ) {
		public int image;
		public BadgeType type;

		Badge(){
			this(-1, BadgeType.HIDDEN);
		}

		Badge( int image ) {
			this( image, BadgeType.LOCAL );
		}

		Badge( int image, BadgeType type ) {
			this.image = image;
			this.type = type;
		}

		public String title(){
			return Messages.get(this, name()+".title");
		}

		public String desc(){
			return Messages.get(this, name()+".desc");
		}
	}

	private static void displayBadge( Badge badge ) {

		if (badge == null) {
			return;
		}

		if (global.contains( badge )) {

			if (!badge.meta) {
				GLog.h( Messages.get(Badges.class, "endorsed", badge.title()) );
			}

		} else {

			global.add( badge );
			saveNeeded = true;

			if (badge.meta) {
				GLog.h( Messages.get(Badges.class, "new_super", badge.title()) );
			} else {
				GLog.h( Messages.get(Badges.class, "new", badge.title()) );
			}
			PixelScene.showBadge( badge );
		}
	}

	public static boolean isUnlocked( Badge badge ) {
		return badge != null && global.contains(badge);
	}

	public static HashSet<Badge> allUnlocked(){
		loadGlobal();
		return new HashSet<>(global);
	}

	public static void disown( Badge badge ) {
		loadGlobal();
		global.remove( badge );
		saveNeeded = true;
	}

	public static void addGlobal( Badge badge ){
		if (!global.contains(badge)){
			global.add( badge );
			saveNeeded = true;
		}
	}

	public static List<Badge> filterReplacedBadges( boolean global ) {

		ArrayList<Badge> badges = new ArrayList<>(global ? Badges.global : Badges.local);

		Iterator<Badge> iterator = badges.iterator();
		while (iterator.hasNext()) {
			Badge badge = iterator.next();
			if ((!global && badge.meta) || badge.image == -1) {
				iterator.remove();
			}
		}

		Collections.sort(badges);

		return filterReplacedBadges(badges);

	}

	//several badges all tie into catalog completion
	public static void validateCatalogBadges(){

		int totalSeen = 0;
		int totalThings = 0;

		for (Catalog cat : Catalog.values()){
			totalSeen += cat.totalSeen();
			totalThings += cat.totalItems();
		}

		for (Bestiary cat : Bestiary.values()){
			totalSeen += cat.totalSeen();
			totalThings += cat.totalEntities();
		}

		for (Document doc : Document.values()){
			if (!doc.isLoreDoc()) {
				for (String page : doc.pageNames()){
					if (doc.isPageFound(page)) totalSeen++;
					totalThings++;
				}
			}
		}

		//overall unlock badges
		Badge badge = null;
		if (totalSeen >= 40) {
			badge = Badge.RESEARCHER_1;
		}
		if (totalSeen >= 80) {
			unlock(badge);
			badge = Badge.RESEARCHER_2;
		}
		if (totalSeen >= 160) {
			unlock(badge);
			badge = Badge.RESEARCHER_3;
		}
		if (totalSeen >= 320) {
			unlock(badge);
			badge = Badge.RESEARCHER_4;
		}
		if (totalSeen == totalThings) {
			unlock(badge);
			badge = Badge.RESEARCHER_5;
		}
		displayBadge( badge );

		//specific task badges

		boolean qualified = true;
		for (Catalog cat : Catalog.equipmentCatalogs) {
			if (cat != Catalog.ENCHANTMENTS && cat != Catalog.GLYPHS) {
				if (cat.totalSeen() == 0) {
					qualified = false;
					break;
				}
			}
		}
		if (qualified) {
			displayBadge(Badge.CATALOG_ONE_EQUIPMENT);
		}

		//doesn't actually use catalogs, but triggers at the same time effectively
		if (!local.contains(Badge.CATALOG_POTIONS_SCROLLS)
				&& Potion.allKnown() && Scroll.allKnown()
				&& Dungeon.hero != null && Dungeon.hero.isAlive()){
			local.add(Badge.CATALOG_POTIONS_SCROLLS);
			displayBadge(Badge.CATALOG_POTIONS_SCROLLS);
		}

		if (Bestiary.RARE.totalEntities() == Bestiary.RARE.totalSeen()){
			displayBadge(Badge.ALL_RARE_ENEMIES);
		}

		if (Document.HALLS_KING.isPageRead(Document.KING_ATTRITION)){
			displayBadge(Badge.RODNEY);
		}

	}

	public static List<Badge> filterHigherIncrementalBadges(List<Badges.Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveWorst( badges, tierReplace );
		}

		Collections.sort( badges );

		return badges;
	}

	private static void leaveWorst( Collection<Badge> list, Badge...badges ) {
		for (int i=0; i < badges.length; i++) {
			if (list.contains( badges[i])) {
				for (int j=i+1; j < badges.length; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}

	public static Collection<Badge> addReplacedBadges(Collection<Badges.Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			addLower( badges, tierReplace );
		}

		for (Badge[] metaReplace : metaBadgeReplacements){
			addLower( badges, metaReplace );
		}

		return badges;
	}

	private static LinkedHashMap<HeroClass, Badge> firstBossClassBadges = new LinkedHashMap<>();
	static {
		firstBossClassBadges.put(HeroClass.WARRIOR, Badge.BOSS_SLAIN_1_WARRIOR);
		firstBossClassBadges.put(HeroClass.MAGE, Badge.BOSS_SLAIN_1_MAGE);
		firstBossClassBadges.put(HeroClass.ROGUE, Badge.BOSS_SLAIN_1_ROGUE);
		firstBossClassBadges.put(HeroClass.HUNTRESS, Badge.BOSS_SLAIN_1_HUNTRESS);
		firstBossClassBadges.put(HeroClass.DUELIST, Badge.BOSS_SLAIN_1_DUELIST);
	}

	private static LinkedHashMap<HeroClass, Badge> victoryClassBadges = new LinkedHashMap<>();
	static {
		victoryClassBadges.put(HeroClass.WARRIOR, Badge.VICTORY_WARRIOR);
		victoryClassBadges.put(HeroClass.MAGE, Badge.VICTORY_MAGE);
		victoryClassBadges.put(HeroClass.ROGUE, Badge.VICTORY_ROGUE);
		victoryClassBadges.put(HeroClass.HUNTRESS, Badge.VICTORY_HUNTRESS);
		victoryClassBadges.put(HeroClass.DUELIST, Badge.VICTORY_DUELIST);
	}

	private static LinkedHashMap<HeroSubClass, Badge> thirdBossSubclassBadges = new LinkedHashMap<>();
	static {
		thirdBossSubclassBadges.put(HeroSubClass.BERSERKER, Badge.BOSS_SLAIN_3_BERSERKER);
		thirdBossSubclassBadges.put(HeroSubClass.GLADIATOR, Badge.BOSS_SLAIN_3_GLADIATOR);
		thirdBossSubclassBadges.put(HeroSubClass.BATTLEMAGE, Badge.BOSS_SLAIN_3_BATTLEMAGE);
		thirdBossSubclassBadges.put(HeroSubClass.WARLOCK, Badge.BOSS_SLAIN_3_WARLOCK);
		thirdBossSubclassBadges.put(HeroSubClass.ASSASSIN, Badge.BOSS_SLAIN_3_ASSASSIN);
		thirdBossSubclassBadges.put(HeroSubClass.FREERUNNER, Badge.BOSS_SLAIN_3_FREERUNNER);
		thirdBossSubclassBadges.put(HeroSubClass.SNIPER, Badge.BOSS_SLAIN_3_SNIPER);
		thirdBossSubclassBadges.put(HeroSubClass.WARDEN, Badge.BOSS_SLAIN_3_WARDEN);

		thirdBossSubclassBadges.put(HeroSubClass.CHAMPION, Badge.BOSS_SLAIN_3_CHAMPION);
		thirdBossSubclassBadges.put(HeroSubClass.MONK, Badge.BOSS_SLAIN_3_MONK);
	}
	
	public static void validateBossSlain() {
		Badge badge = null;
		switch (Dungeon.depth) {
		case 5:
			badge = Badge.BOSS_SLAIN_1;
			break;
		case 10:
			badge = Badge.BOSS_SLAIN_2;
			break;
		case 15:
			badge = Badge.BOSS_SLAIN_3;
			break;
		case 20:
			badge = Badge.BOSS_SLAIN_4;
			break;
		}
		
		if (badge != null) {
			local.add( badge );
			displayBadge( badge );
			
			if (badge == Badge.BOSS_SLAIN_1) {
				badge = firstBossClassBadges.get(Dungeon.hero.heroClass);
				if (badge == null) return;
				local.add( badge );
				unlock(badge);

				boolean allUnlocked = true;
				for (Badge b : firstBossClassBadges.values()){
					if (!isUnlocked(b)){
						allUnlocked = false;
						break;
					}
				}
				if (allUnlocked) {
					
					badge = Badge.BOSS_SLAIN_1_ALL_CLASSES;
					if (!isUnlocked( badge )) {
						displayBadge( badge );
					}
				}
			} else if (badge == Badge.BOSS_SLAIN_3) {

				badge = thirdBossSubclassBadges.get(Dungeon.hero.subClass);
				if (badge == null) return;
				local.add( badge );
				unlock(badge);

				boolean allUnlocked = true;
				for (Badge b : thirdBossSubclassBadges.values()){
					if (!isUnlocked(b)){
						allUnlocked = false;
						break;
					}
				}
				if (allUnlocked) {
					badge = Badge.BOSS_SLAIN_3_ALL_SUBCLASSES;
					if (!isUnlocked( badge )) {
						displayBadge( badge );
					}
				}
			}

			if (Statistics.qualifiedForBossRemainsBadge && Dungeon.hero.belongings.getItem(RemainsItem.class) != null){
				badge = Badge.BOSS_SLAIN_REMAINS;
				local.add( badge );
				displayBadge( badge );
			}

		}
	}

	public static void validateBossChallengeCompleted(){
		Badge badge = null;
		switch (Dungeon.depth) {
			case 5:
				badge = Badge.BOSS_CHALLENGE_1;
				break;
			case 10:
				badge = Badge.BOSS_CHALLENGE_2;
				break;
			case 15:
				badge = Badge.BOSS_CHALLENGE_3;
				break;
			case 20:
				badge = Badge.BOSS_CHALLENGE_4;
				break;
			case 25:
				badge = Badge.BOSS_CHALLENGE_5;
				break;
		}

		if (badge != null) {
			local.add(badge);
			displayBadge(badge);
		}
	}
	
	public static void validateMastery() {
		
		Badge badge = null;
		switch (Dungeon.hero.heroClass) {
			case WARRIOR:
				badge = Badge.MASTERY_WARRIOR;
				break;
			case MAGE:
				badge = Badge.MASTERY_MAGE;
				break;
			case ROGUE:
				badge = Badge.MASTERY_ROGUE;
				break;
			case HUNTRESS:
				badge = Badge.MASTERY_HUNTRESS;
				break;
			case DUELIST:
				badge = Badge.MASTERY_DUELIST;
				break;
		}
		
		unlock(badge);
	}

	public static void validateRatmogrify(){
		unlock(Badge.FOUND_RATMOGRIFY);
	}
	
	public static void validateMageUnlock(){
		if (Statistics.upgradesUsed >= 1 && !isUnlocked(Badge.UNLOCK_MAGE)){
			displayBadge( Badge.UNLOCK_MAGE );
		}
	}
	
	public static void validateRogueUnlock(){
		if (Statistics.sneakAttacks >= 10 && !isUnlocked(Badge.UNLOCK_ROGUE)){
			displayBadge( Badge.UNLOCK_ROGUE );
		}
	}
	
	public static void validateHuntressUnlock(){
		if (Statistics.thrownAttacks >= 10 && !isUnlocked(Badge.UNLOCK_HUNTRESS)){
			displayBadge( Badge.UNLOCK_HUNTRESS );
		}
	}

	public static void validateDuelistUnlock(){
		if (!isUnlocked(Badge.UNLOCK_DUELIST) && Dungeon.hero != null
				&& Dungeon.hero.belongings.weapon instanceof MeleeWeapon
				&& ((MeleeWeapon) Dungeon.hero.belongings.weapon).tier >= 2
				&& ((MeleeWeapon) Dungeon.hero.belongings.weapon).STRReq() <= Dungeon.hero.STR()){

			if (Dungeon.hero.belongings.weapon.isIdentified() &&
					((MeleeWeapon) Dungeon.hero.belongings.weapon).STRReq() <= Dungeon.hero.STR()) {
				displayBadge(Badge.UNLOCK_DUELIST);

			} else if (!Dungeon.hero.belongings.weapon.isIdentified() &&
					((MeleeWeapon) Dungeon.hero.belongings.weapon).STRReq(0) <= Dungeon.hero.STR()){
				displayBadge(Badge.UNLOCK_DUELIST);
			}
		}
	}
	
	public static void validateMasteryCombo( int n ) {
		if (!local.contains( Badge.MASTERY_COMBO ) && n == 10) {
			Badge badge = Badge.MASTERY_COMBO;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateVictory() {

		Badge badge = Badge.VICTORY;
		local.add( badge );
		displayBadge( badge );

		badge = victoryClassBadges.get(Dungeon.hero.heroClass);
		if (badge == null) return;
		local.add( badge );
		unlock(badge);

		boolean allUnlocked = true;
		for (Badge b : victoryClassBadges.values()){
			if (!isUnlocked(b)){
				allUnlocked = false;
				break;
			}
		}
		if (allUnlocked){
			badge = Badge.VICTORY_ALL_CLASSES;
			displayBadge( badge );
		}
	}

	public static void validateNoKilling() {
		if (!local.contains( Badge.NO_MONSTERS_SLAIN ) && Statistics.completedWithNoKilling) {
			Badge badge = Badge.NO_MONSTERS_SLAIN;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateGrimWeapon() {
		if (!local.contains( Badge.GRIM_WEAPON )) {
			Badge badge = Badge.GRIM_WEAPON;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateGamesPlayed() {
		Badge badge = null;
		if (Rankings.INSTANCE.totalNumber >= 10 || Rankings.INSTANCE.wonNumber >= 1) {
			badge = Badge.GAMES_PLAYED_1;
		}
		if (Rankings.INSTANCE.totalNumber >= 25 || Rankings.INSTANCE.wonNumber >= 3) {
			unlock(badge);
			badge = Badge.GAMES_PLAYED_2;
		}
		if (Rankings.INSTANCE.totalNumber >= 50 || Rankings.INSTANCE.wonNumber >= 5) {
			unlock(badge);
			badge = Badge.GAMES_PLAYED_3;
		}
		if (Rankings.INSTANCE.totalNumber >= 200 || Rankings.INSTANCE.wonNumber >= 10) {
			unlock(badge);
			badge = Badge.GAMES_PLAYED_4;
		}
		if (Rankings.INSTANCE.totalNumber >= 1000 || Rankings.INSTANCE.wonNumber >= 25) {
			unlock(badge);
			badge = Badge.GAMES_PLAYED_5;
		}
		
		displayBadge( badge );
	}

	public static void validateHighScore( int score ){
		Badge badge = null;
		if (score >= 5000) {
			badge = Badge.HIGH_SCORE_1;
			local.add( badge );
		}
		if (score >= 25_000) {
			unlock(badge);
			badge = Badge.HIGH_SCORE_2;
			local.add( badge );
		}
		if (score >= 100_000) {
			unlock(badge);
			badge = Badge.HIGH_SCORE_3;
			local.add( badge );
		}
		if (score >= 250_000) {
			unlock(badge);
			badge = Badge.HIGH_SCORE_4;
			local.add( badge );
		}
		if (score >= 1_000_000) {
			unlock(badge);
			badge = Badge.HIGH_SCORE_5;
			local.add( badge );
		}

		displayBadge( badge );
	}
	
	public static void validateHappyEnd() {
		local.add( Badge.HAPPY_END );
		displayBadge( Badge.HAPPY_END );

		if( Dungeon.hero.belongings.getItem(RemainsItem.class) != null ){
			local.add( Badge.HAPPY_END_REMAINS );
			displayBadge( Badge.HAPPY_END_REMAINS );
		}
	}

	public static void validateChampion( int challenges ) {
		if (challenges == 0) return;
		Badge badge = null;
		if (challenges >= 1) {
			badge = Badge.CHAMPION_1;
		}
		if (challenges >= 3){
			unlock(badge);
			badge = Badge.CHAMPION_2;
		}
		if (challenges >= 6){
			unlock(badge);
			badge = Badge.CHAMPION_3;
		}
		local.add(badge);
		displayBadge( badge );
	}
	
	private static void displayBadge( Badge badge ) {

		if (badge == null || (badge.type != BadgeType.JOURNAL && !Dungeon.customSeedText.isEmpty())) {
			return;
		}
		
		if (isUnlocked( badge )) {
			
			if (badge.type == BadgeType.LOCAL) {
				GLog.h( Messages.get(Badges.class, "endorsed", badge.title()) );
				GLog.newLine();
			}
			
		} else {
			
			unlock(badge);
			
			GLog.h( Messages.get(Badges.class, "new", badge.title() + " (" + badge.desc() + ")") );
			GLog.newLine();
			PixelScene.showBadge( badge );
		}
	}
	
	public static boolean isUnlocked( Badge badge ) {
		return global.contains( badge );
	}
	
	public static HashSet<Badge> allUnlocked(){
		loadGlobal();
		return new HashSet<>(global);
	}
	
	public static void disown( Badge badge ) {
		loadGlobal();
		global.remove( badge );
		saveNeeded = true;
	}
	
	public static void unlock( Badge badge ){
		if (!isUnlocked(badge) && (badge.type == BadgeType.JOURNAL || Dungeon.customSeedText.isEmpty())){
			global.add( badge );
			saveNeeded = true;
		}
	}

	public static List<Badge> filterReplacedBadges( boolean global ) {

		ArrayList<Badge> badges = new ArrayList<>(global ? Badges.global : Badges.local);

		Iterator<Badge> iterator = badges.iterator();
		while (iterator.hasNext()) {
			Badge badge = iterator.next();
			if ((!global && badge.type != BadgeType.LOCAL) || badge.type == BadgeType.HIDDEN) {
				iterator.remove();
			}
		}

		Collections.sort(badges);

		return filterReplacedBadges(badges);

	}

	//only show the highest unlocked and the lowest locked
	private static final Badge[][] tierBadgeReplacements = new Badge[][]{
			{Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4, Badge.MONSTERS_SLAIN_5},
			{Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4, Badge.GOLD_COLLECTED_5},
			{Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4, Badge.ITEM_LEVEL_5},
			{Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4, Badge.LEVEL_REACHED_5},
			{Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4, Badge.STRENGTH_ATTAINED_5},
			{Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4, Badge.FOOD_EATEN_5},
			{Badge.ITEMS_CRAFTED_1, Badge.ITEMS_CRAFTED_2, Badge.ITEMS_CRAFTED_3, Badge.ITEMS_CRAFTED_4, Badge.ITEMS_CRAFTED_5},
			{Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4},
			{Badge.RESEARCHER_1, Badge.RESEARCHER_2, Badge.RESEARCHER_3, Badge.RESEARCHER_4, Badge.RESEARCHER_5},
			{Badge.HIGH_SCORE_1, Badge.HIGH_SCORE_2, Badge.HIGH_SCORE_3, Badge.HIGH_SCORE_4, Badge.HIGH_SCORE_5},
			{Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4, Badge.GAMES_PLAYED_5},
			{Badge.CHAMPION_1, Badge.CHAMPION_2, Badge.CHAMPION_3}
	};

	//don't show the later badge if the earlier one isn't unlocked
	private static final Badge[][] prerequisiteBadges = new Badge[][]{
			{Badge.BOSS_SLAIN_1, Badge.BOSS_CHALLENGE_1},
			{Badge.BOSS_SLAIN_2, Badge.BOSS_CHALLENGE_2},
			{Badge.BOSS_SLAIN_3, Badge.BOSS_CHALLENGE_3},
			{Badge.BOSS_SLAIN_4, Badge.BOSS_CHALLENGE_4},
			{Badge.VICTORY,      Badge.BOSS_CHALLENGE_5},
	};

	//If the summary badge is unlocked, don't show the component badges
	private static final Badge[][] summaryBadgeReplacements = new Badge[][]{
			{Badge.DEATH_FROM_FIRE, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_GAS, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_HUNGER, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_POISON, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_FALLING, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_ENEMY_MAGIC, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_FRIENDLY_MAGIC, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_SACRIFICE, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_GRIM_TRAP, Badge.DEATH_FROM_ALL},

			{Badge.ALL_WEAPONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_ARMOR_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_WANDS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_RINGS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_ARTIFACTS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_POTIONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_SCROLLS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED}
	};
	
	public static List<Badge> filterReplacedBadges( List<Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveBest( badges, tierReplace );
		}

		for (Badge[] metaReplace : summaryBadgeReplacements){
			leaveBest( badges, metaReplace );
		}
		
		return badges;
	}
	
	private static void leaveBest( Collection<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}

	public static List<Badge> filterBadgesWithoutPrerequisites(List<Badges.Badge> badges ) {

		for (Badge[] prereqReplace : prerequisiteBadges){
			leaveWorst( badges, prereqReplace );
		}

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveWorst( badges, tierReplace );
		}

		Collections.sort( badges );

		return badges;
	}

	private static void leaveWorst( Collection<Badge> list, Badge...badges ) {
		for (int i=0; i < badges.length; i++) {
			if (list.contains( badges[i])) {
				for (int j=i+1; j < badges.length; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}

	public static Collection<Badge> addReplacedBadges(Collection<Badges.Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			addLower( badges, tierReplace );
		}

		for (Badge[] metaReplace : summaryBadgeReplacements){
			addLower( badges, metaReplace );
		}

		return badges;
	}

	private static void addLower( Collection<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.add( badges[j] );
				}
				break;
			}
		}
	}

	//used for badges with completion progress that would otherwise be hard to track
	public static String showCompletionProgress( Badge badge ){
		if (isUnlocked(badge)) return null;

		String result = "\n";

		if (badge == Badge.BOSS_SLAIN_1_ALL_CLASSES){
			for (HeroClass cls : HeroClass.values()){
				result += "\n";
				if (isUnlocked(firstBossClassBadges.get(cls)))  result += "_" + Messages.titleCase(cls.title()) + "_";
				else                                            result += Messages.titleCase(cls.title());
			}

			return result;

		} else if (badge == Badge.VICTORY_ALL_CLASSES) {

			for (HeroClass cls : HeroClass.values()){
				result += "\n";
				if (isUnlocked(victoryClassBadges.get(cls)))    result += "_" + Messages.titleCase(cls.title()) + "_";
				else                                            result += Messages.titleCase(cls.title());
			}

			return result;

		} else if (badge == Badge.BOSS_SLAIN_3_ALL_SUBCLASSES){

			for (HeroSubClass cls : HeroSubClass.values()){
				if (cls == HeroSubClass.NONE) continue;
				result += "\n";
				if (isUnlocked(thirdBossSubclassBadges.get(cls)))   result += "_" + Messages.titleCase(cls.title()) + "_";
				else                                                result += Messages.titleCase(cls.title()) ;
			}

			return result;
		}

		return null;
	}

	//don't show the later badge if the earlier one isn't unlocked
	private static final Badge[][] prerequisiteBadges = new Badge[][]{
			{Badge.BOSS_SLAIN_1, Badge.BOSS_CHALLENGE_1},
			{Badge.BOSS_SLAIN_2, Badge.BOSS_CHALLENGE_2},
			{Badge.BOSS_SLAIN_3, Badge.BOSS_CHALLENGE_3},
			{Badge.BOSS_SLAIN_4, Badge.BOSS_CHALLENGE_4},
			{Badge.VICTORY,      Badge.BOSS_CHALLENGE_5},
	};

	public static List<Badge> filterBadgesWithoutPrerequisites(List<Badges.Badge> badges ) {

		for (Badge[] prereqReplace : prerequisiteBadges){
			leaveWorst( badges, prereqReplace );
		}

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveWorst( badges, tierReplace );
		}

		Collections.sort( badges );

		return badges;
	}

	private static void addLower( Collection<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.add( badges[j] );
				}
				break;
			}
		}
	}
}
