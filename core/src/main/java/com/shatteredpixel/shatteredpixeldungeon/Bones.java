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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfNukeCole;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.WaterSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Bones {

	private static final String BONES_FILE	= "bones.dat";
	
	private static final String LEVEL	= "level";
	private static final String BRANCH	= "branch";
	private static final String ITEM	= "item";
	private static final String HERO_CLASS	= "hero_class";

	private static final String NAME = "hero_name";
	private static final String KILLED = "enemies_slain";
	private static final String HEALING = "available_healing";
	private static String name;
	private static int killed;
	private static int healing;


	private static int depth = -1;
	private static int branch = -1;

	private static Item item;
	private static HeroClass heroClass;

	public static void leave() {

		//remains will usually drop on the floor the hero died on
		// but are capped at 5 floors above the lowest depth reached (even when ascending)
		depth = Math.max(Dungeon.depth, Statistics.deepestFloor-5);

		branch = Dungeon.branch;

		//daily runs do not interact with remains
		if (Dungeon.daily) {
			depth = branch = -1;
			return;
		}

		item = pickItem(Dungeon.hero);
		heroClass = Dungeon.hero.heroClass;

		Bundle bundle = new Bundle();
		bundle.put( LEVEL, depth );
		bundle.put( BRANCH, branch );
		bundle.put( ITEM, item );
		bundle.put( HERO_CLASS, heroClass );

		//TODO 墓志铭测试
		setEpitaphProps(bundle);

		try {
			FileUtils.bundleToFile( BONES_FILE, bundle );
		} catch (IOException e) {
			ShatteredPixelDungeon.reportException(e);
		}
	}

	private static void setEpitaphProps(Bundle bundle) {
		if (Dungeon.hero.name().equals(Dungeon.hero.className())) {
			name = "";
		} else {
			name = Dungeon.hero.name();
		}
		killed = Statistics.enemiesSlain;
		healing = 0;
		Waterskin waterskin = Dungeon.hero.belongings.getItem(Waterskin.class);
		//死亡时携带核子可乐
		if(Dungeon.hero.belongings.getItem(ElixirOfNukeCole.class) != null){
			healing = 4;
		//死亡时携带水灵药剂
		}else if(Dungeon.hero.belongings.getItem(WaterSoul.class) != null){
			healing = 3;
		} else if (waterskin != null && waterskin.isFull()) {
			healing = 2;
		} else if (Dungeon.hero.belongings.getItem(PotionOfHealing.class) != null) {
			healing = 1;
		}
		bundle.put(NAME, name);
		bundle.put(KILLED, killed);
		bundle.put(HEALING, healing);
	}

	private static void clearEpitaphProps() {
		name = null;
		killed = 0;
		healing = 0;
	}

	private static void loadEpitaphProps(Bundle bundle) {
		if (bundle.contains(NAME)) {
			name = bundle.getString(NAME);
			healing = bundle.getInt(HEALING);
			killed = bundle.getInt(KILLED);
			return;
		}
		clearEpitaphProps();
	}


	private static Item pickItem(Hero hero){
		Item item = null;

		//seeded runs don't leave items
		//This is to prevent using specific seeds to transport items to regular runs
		if (!Dungeon.customSeedText.isEmpty()){
			return null;
		}

		if (Random.Int(3) != 0) {
			switch (Random.Int(7)) {
				case 0:
					item = hero.belongings.weapon;
					//if the hero has two weapons (champion), pick the stronger one
					if (hero.belongings.secondWep != null &&
							(item == null || hero.belongings.secondWep.trueLevel() > item.trueLevel())){
						item = hero.belongings.secondWep;
						break;
					}
					break;
				case 1:
					item = hero.belongings.armor;
					break;
				case 2:
					item = hero.belongings.artifact;
					break;
				case 3:
					item = hero.belongings.misc;
					break;
				case 4:
					item = hero.belongings.ring;
					break;
				case 5: case 6:
					item = Dungeon.quickslot.randomNonePlaceholder();
					break;
			}
			if (item == null || !item.bones) {
				return pickItem(hero);
			}
		} else {

			Iterator<Item> iterator = hero.belongings.backpack.iterator();
			Item curItem;
			ArrayList<Item> items = new ArrayList<>();
			while (iterator.hasNext()){
				curItem = iterator.next();
				if (curItem.bones) {
					items.add(curItem);
				}
			}

			//if there are few items, there is an increasingly high chance of leaving nothing
			if (Random.Int(3) < items.size()) {
				item = Random.element(items);
				if (item.stackable){
					item.quantity(Random.NormalIntRange(1, (item.quantity() + 1) / 2));
					if (item.quantity() > 3){
						item.quantity(3);
					}
				}
			} else {
				item = null;
			}
		}
		
		return item;
	}

	public static ArrayList<Item> get() {
		//daily runs do not interact with remains
		if (Dungeon.daily){
			return null;
		}

		if (depth == -1) {

			try {
				Bundle bundle = FileUtils.bundleFromFile(BONES_FILE);

				depth = bundle.getInt( LEVEL );
				branch = bundle.getInt( BRANCH );
				if (depth > 0) {
					if (bundle.contains(ITEM)) {
						item = (Item) bundle.get(ITEM);
					} else {
						item = null;
					}
					if (bundle.contains(HERO_CLASS)){
						heroClass = bundle.getEnum(HERO_CLASS, HeroClass.class);
					} else {
						heroClass = null;
					}
					loadEpitaphProps(bundle);
				}

				return get();

			} catch (IOException e) {
				return null;
			}

		} else {
			if (lootAtCurLevel()) {

				Bundle emptyBones = new Bundle();
				emptyBones.put(LEVEL, 0);
				try {
					FileUtils.bundleToFile( BONES_FILE, emptyBones );
				} catch (IOException e) {
					ShatteredPixelDungeon.reportException(e);
				}
				depth = 0;

				//challenged or seeded runs don't get items from prior runs
				if (Dungeon.challenges != 0 || !Dungeon.customSeedText.isEmpty()){
					item = null;
				}

				//Enforces artifact uniqueness
				if (item instanceof Artifact){
					if (Generator.removeArtifact(((Artifact)item).getClass())) {
						
						//generates a new artifact of the same type, always +0
						Artifact artifact = Reflection.newInstance(((Artifact)item).getClass());
						
						if (artifact != null){
							artifact.cursed = true;
							artifact.cursedKnown = true;
						}

						item = artifact;
						
					} else {
						item = new Gold(item.value());
					}
				}

				if (item != null) {
					if (item.isUpgradable() && !(item instanceof MissileWeapon)) {
						item.cursed = true;
						item.cursedKnown = true;
					}

					if (item.isUpgradable()) {
						//caps at +3
						if (item.level() > 3) {
							item.degrade(item.level() - 3);
						}
						//thrown weapons are always IDed, otherwise set unknown
						item.levelKnown = item instanceof MissileWeapon;
					}

					item.reset();
				}

				ArrayList<Item> result = new ArrayList<>();

//				if (heroClass != null) {
//					result.add(RemainsItem.get(heroClass));
//					if (Dungeon.bossLevel()){
//						Statistics.qualifiedForBossRemainsBadge = true;
//					}
//				}

				if (item != null) {
					result.add(item);
				}

				return result.isEmpty() ? null : result;
			} else {
				return null;
			}
		}
	}


	public static String generateHeroEpitaph() {
		// 初始检查，确保必要条件被满足
		if (depth != 0 || name == null || heroClass == null) {
			return null;
		}

		// 根据英雄是否有名字，选择合适的墓志铭开头
		String epitaphStart = name.isEmpty() ?
				Messages.get(Bones.class, "here_lies_nameless", heroClass.title()) :
				Messages.get(Bones.class, "here_lies_named", name, heroClass.title());

		epitaphStart += "\n\n"; // 添加段落分隔

		String epitaphBody; // 墓志铭的主体部分
		// 根据英雄的深度和杀敌数，生成墓志铭的主体内容
		if (killed == 0) {
			epitaphBody = Messages.get(Bones.class, "pacifist");
		} else if (depth < 4) {
			epitaphBody = Messages.get(Bones.class, "rats", killed);
		} else if (depth < 7) {
			epitaphBody = Messages.get(Bones.class, "crabs", killed);
		} else if (depth < 9) {
			epitaphBody = Messages.get(Bones.class, "undead", killed);
		} else if (depth < 11) {
			epitaphBody = Messages.get(Bones.class, "thieves", killed);
		} else if (depth < 14) {
			epitaphBody = Messages.get(Bones.class, "bats", killed);
		} else if (depth < 17) {
			epitaphBody = Messages.get(Bones.class, "brutes", killed);
		} else if (depth < 19) {
			epitaphBody = Messages.get(Bones.class, "monks", killed);
		} else if (depth < 21) {
			epitaphBody = Messages.get(Bones.class, "golems", killed);
		} else {
			epitaphBody = depth < 25 ?
					Messages.get(Bones.class, "demons", killed) :
					Messages.get(Bones.class, "yog", killed);
		}

		// 添加关于治疗的备注
		if (healing == 1) {
			epitaphBody += "\n\n" + Messages.get(Bones.class, "forgot_waterskin");
		} else if (healing == 2) {
			epitaphBody += "\n\n" + Messages.get(Bones.class, "forgot_potion");
		} else if (healing == 3) {
			epitaphBody += "\n\n" + Messages.get(Bones.class, "forgot_watersoul");
		} else if (healing == 4) {
			epitaphBody += "\n\n" + Messages.get(Bones.class, "forgot_nukecole");
		}

		// 结尾部分，表示安息
		String epitaphEnd = "\n\n" + Messages.get(Bones.class, "rest_in_peace");

		// 清除墓志铭相关的属性
		clearEpitaphProps();

		// 返回完整的墓志铭
		return epitaphStart + epitaphBody + epitaphEnd;
	}


	private static boolean lootAtCurLevel(){
		if (branch == Dungeon.branch) {
			if (branch == 0) {
				//always match depth exactly for main path
				return depth == Dungeon.depth;
			} else if (branch == 1) {
				//just match the region for quest sub-floors
				return depth/5 == Dungeon.depth/5;
			}
		}
		return false;
	}
}
