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

	/**
	 * 布局字段
	 * @param item 物品
	 * @param iconTitle 物品图标
	 * @param renderedTextBlock 物品信息
	 */
	private void layoutFields(Item item, IconTitle iconTitle, RenderedTextBlock renderedTextBlock) {
		int i = 120;
		renderedTextBlock.maxWidth(120);
		while (PixelScene.landscape() && renderedTextBlock.height() > 100.0f && i < 220) {
			i += 20;
			renderedTextBlock.maxWidth(i);
//			q:为什么这里要用while循环？
//			a：因为在横屏的时候，如果文字太多，会导致窗口的高度超过屏幕的高度，所以要不断的增加窗口的宽度，直到文字的高度小于窗口的高度
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