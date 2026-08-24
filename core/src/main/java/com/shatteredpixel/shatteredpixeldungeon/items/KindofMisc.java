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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.props.BrokenRing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;


public abstract class KindofMisc extends EquipableItem {

	@Override
	public boolean doEquip(final Hero hero) {

		boolean equipFull = false;
		if ( this instanceof Artifact
				&& hero.belongings.artifact != null
				&& hero.belongings.misc != null){

			//see if we can re-arrange items first
			if (hero.belongings.misc instanceof Ring && hero.belongings.ring == null){
				hero.belongings.ring = (Ring) hero.belongings.misc;
				hero.belongings.misc = null;
				// ===== 修复1：检查伴生物 =====
			} else if (BrokenRing.isBind(hero.belongings.misc) && hero.belongings.ring == null){
				// 封印物品不能被移动
				equipFull = true;
				// ==========================================
			} else {
				equipFull = true;
			}
		} else if (this instanceof Ring
				&& hero.belongings.misc != null
				&& hero.belongings.ring != null){

			//see if we can re-arrange items first
			if (hero.belongings.misc instanceof Artifact && hero.belongings.artifact == null){
				hero.belongings.artifact = (Artifact) hero.belongings.misc;
				hero.belongings.misc = null;
				// ===== 修复2：检查伴生物 =====
			} else if (BrokenRing.isBind(hero.belongings.misc) && hero.belongings.artifact == null){
				// 封印物品不能被移动
				equipFull = true;
				// ==========================================
			} else {
				equipFull = true;
			}
		}

		if (equipFull) {

			final KindofMisc[] miscs = new KindofMisc[3];
			miscs[0] = hero.belongings.artifact;
			miscs[1] = hero.belongings.misc;
			miscs[2] = hero.belongings.ring;

			final boolean[] enabled = new boolean[3];
			enabled[0] = miscs[0] != null;
			enabled[1] = miscs[1] != null;
			enabled[2] = miscs[2] != null;

			//force swapping with the same type of item if 2x of that type is already present
			if (this instanceof Ring && hero.belongings.misc instanceof Ring){
				enabled[0] = false; //disable artifact
				// ===== 修复3：伴生物不是 Ring/Artifact =====
			} else if (this instanceof Ring && BrokenRing.isBind(hero.belongings.misc)){
				enabled[0] = false;
				enabled[1] = false; // 封印物品不能被替换
				// ================================================
			} else if (this instanceof Artifact && hero.belongings.misc instanceof Artifact){
				enabled[2] = false; //disable ring
				// ===== 修复4：伴生物不是 Artifact =====
			} else if (this instanceof Artifact && BrokenRing.isBind(hero.belongings.misc)){
				enabled[2] = false;
				enabled[1] = false; // 封印物品不能被替换
				// =================================================
			}

			GameScene.show(
					new WndOptions(new ItemSprite(this),
							Messages.get(KindofMisc.class, "unequip_title"),
							Messages.get(KindofMisc.class, "unequip_message"),
							miscs[0] == null ? "---" : Messages.titleCase(miscs[0].title()),
							miscs[1] == null ? "---" : Messages.titleCase(miscs[1].title()),
							miscs[2] == null ? "---" : Messages.titleCase(miscs[2].title())) {

						@Override
						protected void onSelect(int index) {

							KindofMisc equipped = miscs[index];
							//we directly remove the item because we want to have inventory capacity
							// to unequip the equipped one, but don't want to trigger any other
							// item detaching logic
							int slot = Dungeon.quickslot.getSlot(KindofMisc.this);
							slotOfUnequipped = -1;
							Dungeon.hero.belongings.backpack.items.remove(KindofMisc.this);
							if (equipped.doUnequip(hero, true, false)) {
								//swap out equip in misc slot if needed
								if (index == 0 && KindofMisc.this instanceof Ring){
									// ===== 修复5：核心崩溃点 =====
									if (BrokenRing.isBind(hero.belongings.misc)) {
										// 封印物品不能被移动，直接放入背包
										hero.belongings.misc = null;
									} else {
										hero.belongings.artifact = (Artifact)hero.belongings.misc;
										hero.belongings.misc = null;
									}
									// =============================
								} else if (index == 2 && KindofMisc.this instanceof Artifact){
									// ===== 修复6：核心崩溃点 =====
									if (BrokenRing.isBind(hero.belongings.misc)) {
										// 封印物品不能被移动，直接放入背包
										hero.belongings.misc = null;
									} else {
										hero.belongings.ring = (Ring) hero.belongings.misc;
										hero.belongings.misc = null;
									}
									// =============================
								}
								Dungeon.hero.belongings.backpack.items.add(KindofMisc.this);
								doEquip(hero);
							} else {
								Dungeon.hero.belongings.backpack.items.add(KindofMisc.this);
							}
							if (slot != -1) {
								Dungeon.quickslot.setSlot(slot, KindofMisc.this);
							} else if (slotOfUnequipped != -1 && defaultAction() != null){
								Dungeon.quickslot.setSlot(slotOfUnequipped, KindofMisc.this);
							}
							updateQuickslot();
						}

						@Override
						protected boolean enabled(int index) {
							return enabled[index];
						}
					});

			return false;

		} else {

			if (this instanceof Artifact){
				if (hero.belongings.artifact == null)   hero.belongings.artifact = (Artifact) this;
				else                                    hero.belongings.misc = (Artifact) this;
			} else if (this instanceof Ring){
				if (hero.belongings.ring == null)   hero.belongings.ring = (Ring) this;
				else                                hero.belongings.misc = (Ring) this;
			} else if (hero.belongings.misc == null){
				// 通用装备（非神器/非戒指，如破碎之环伴生物）：放入通配栏位
				hero.belongings.misc = this;
			} else {
				// 通配栏位已满，通用装备无法装备
				return false;
			}

			detach( hero.belongings.backpack );

			Talent.onItemEquipped(hero, this);
			activate( hero );

			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(this, "equip_cursed", this) );
			}

			hero.spendAndNext( timeToEquip(hero) );
			return true;

		}

	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){

			if (hero.belongings.artifact == this) {
				hero.belongings.artifact = null;
			} else if (hero.belongings.misc == this) {
				hero.belongings.misc = null;
			} else if (hero.belongings.ring == this){
				hero.belongings.ring = null;
			}

			return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean isEquipped( Hero hero ) {
		return hero != null && (hero.belongings.artifact() == this
				|| hero.belongings.misc() == this
				|| hero.belongings.ring() == this);
	}

}
