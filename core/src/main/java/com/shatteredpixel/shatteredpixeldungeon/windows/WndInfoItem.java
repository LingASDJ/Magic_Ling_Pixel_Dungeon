/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndInfoItem extends Window {
	private static WndInfoItem INSTANCE;

	public WndInfoItem(Heap heap) {
		WndInfoItem wndInfoItem = INSTANCE;
		if (wndInfoItem != null) {
			wndInfoItem.hide();
		}
		INSTANCE = this;
		if (heap.type == Heap.Type.HEAP) {
			fillFields(heap.peek());
		} else {
			fillFields(heap);
		}
	}

	public WndInfoItem(Item item) {
		WndInfoItem wndInfoItem = INSTANCE;
		if (wndInfoItem != null) {
			wndInfoItem.hide();
		}
		INSTANCE = this;
		fillFields(item);
	}

	private void fillFields(Heap heap) {
		IconTitle iconTitle = new IconTitle(heap);
		iconTitle.color(16777028);
		layoutFields(null, iconTitle, PixelScene.renderTextBlock(heap.info(), 6));
	}

	private void fillFields(Item item) {
		int color;
		if ((!(item instanceof EquipableItem) || ((EquipableItem) item).customName.equals("")) && (!(item instanceof Wand) || ((Wand) item).customName.equals(""))) {
			color = Window.SKYBULE_COLOR;
		} else {
			color = Window.CYELLOW;
		}
		IconTitle iconTitle = new IconTitle(item);
		iconTitle.color(color);
		layoutFields(item, iconTitle, PixelScene.renderTextBlock(item.info(), 6));
	}

	boolean isMatch(String customName) {
		String[] matchArray = {
				// 水果 Fruit
				"Apple", "Banana", "Orange", "Grape", "Watermelon", "Strawberry", "Mango", "Peach", "Pineapple", "Pear",

				// 动物 Animal
				"Bear", "Beaver", "Bee", "Bird", "Butterfly", "Cat", "Chicken", "Cow", "Crocodile",
				"Deer", "Dog", "Dolphin", "Duck", "Eagle", "Elephant", "Fish", "Fox", "Frog", "Giraffe",
				"Goat", "Gorilla", "Hamster", "Hippopotamus", "Horse", "Jaguar", "Kangaroo", "Koala", "Leopard",
				"Lion", "Lobster", "Monkey", "Mosquito", "Mouse", "Octopus", "Owl", "Panda", "Panther", "Parrot",
				"Pelican", "Penguin", "Pig", "Pigeon", "Pike", "Rabbit", "Rat", "Rhinoceros", "Rooster", "Seagull",
				"Shark", "Sheep", "Snail", "Snake", "Spider", "Squirrel", "Swan", "Tiger", "Turtle", "Walrus",
				"Whale", "Wolf", "Zebra",

				// 花卉 Flower
				"Rose", "Lily", "Daisy", "Sunflower", "Tulip", "Orchid", "Carnation", "Hyacinth", "Peony", "Chrysanthemum",

				// 运动器材 Sport Equipment
				"Basketball", "Football", "Soccer", "Tennis", "Table Tennis", "Badminton", "Swimming", "Running", "Cycling", "Hiking",

				// 学科 Subject
				"Mathematics", "Physics", "Chemistry", "Biology", "History", "Geography", "English", "Chinese", "Art", "Music",

				// 家具 Furniture
				"Chair", "Table", "Bed", "Wardrobe", "Sofa", "Armchair", "Bookshelf", "Coffee Table", "Dining Table", "Desk",

				// 电器 Appliance
				"Television", "Refrigerator", "Washing Machine", "Air Conditioner", "Microwave Oven", "Vacuum Cleaner", "Water Heater", "Hair Dryer", "Electric Fan", "Lamp",

				// 自然地理 Nature
				"Forest", "Mountain", "River", "Lake", "Beach", "Desert", "Jungle", "Grassland", "Island", "Volcano",

				// 职业 Occupation
				"Doctor", "Nurse", "Teacher", "Engineer", "Lawyer", "Writer", "Actor", "Singer", "Chef", "Scientist",

				// 学习用品 Stationery
				"Pencil", "Pen", "Eraser", "Ruler", "Scissors", "Tape", "Glue", "Calculator", "Notebook", "Highlighter"
		};

		//忽略首尾空格 忽略大小写判定
		customName = customName.trim().toLowerCase();
		for (String str : matchArray) {
			if (str.toLowerCase().equals(customName)) {
				return true;
			}
		}
		return false;
	}

	private void layoutFields(Item item, IconTitle iconTitle, RenderedTextBlock renderedTextBlock) {
		int i = 120;
		renderedTextBlock.maxWidth(120);
		while (PixelScene.landscape() && renderedTextBlock.height() > 100.0f && i < 220) {
			i += 20;
			renderedTextBlock.maxWidth(i);
		}
		if (!Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1) || !(this instanceof WndUseItem) || (!(item instanceof EquipableItem) && !(item instanceof Wand))) {
			iconTitle.setRect(0.0f, 0.0f, (float) i, 0.0f);
		} else {
			iconTitle.setRect(0.0f, 0.0f, (float) (i - 16), 0.0f);
		}
		add(iconTitle);
		renderedTextBlock.setPos(iconTitle.left(), iconTitle.bottom() + 2.0f);
		add(renderedTextBlock);
		resize(i, (int) (renderedTextBlock.bottom() + 2.0f));
	}

	@Override
	public void hide() {
		super.hide();
		if (INSTANCE == this) {
			INSTANCE = null;
		}
	}
}