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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.Guidebook;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class EquipableItem extends Item {
	public static final String AC_EQUIP = "EQUIP";
	public static final String AC_UNEQUIP = "UNEQUIP";
	public String customName = "";
	public int customNoteID = -1;
	public String name() {
		return this.customName.equals("") ? super.name() : this.customName;
	}
	private static final String CUSTOM_NOTE_ID = "custom_note_id";
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(CUSTOM_NOTE_ID))    customNoteID = bundle.getInt(CUSTOM_NOTE_ID);
		if (bundle.contains("customName")) {
			this.customName = bundle.getString("customName");
		}
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (customNoteID != -1)     bundle.put(CUSTOM_NOTE_ID, customNoteID);
		if (!this.customName.equals("")) {
			bundle.put("customName", this.customName);
		}
	}

	{
		bones = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
		return actions;
	}

	@Override
	public boolean doPickUp(Hero hero, int pos) {
		if (super.doPickUp(hero, pos)){
			if (!isIdentified() && !Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_IDING)){
				GLog.p(Messages.get(Guidebook.class, "hint"));
				GameScene.flashForDocument(Document.ADVENTURERS_GUIDE, Document.GUIDE_IDING);
			}
			return true;
		} else {
			return false;
		}
	}

	protected static int slotOfUnequipped = -1;

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_EQUIP)) {

			// 在装备前记录该物品占用的全部快捷栏槽位
			// 多技能物品可能同时绑定多个槽位，因此不只使用 getSlot
			ArrayList<Integer> slots =
					Dungeon.quickslot.getSlots(this);

			// 同时保存每个槽位绑定的技能。
			String[] savedActions = new String[slots.size()];

			for (int i = 0; i < slots.size(); i++) {
				savedActions[i] =
						Dungeon.quickslot.getSlotAction(slots.get(i));
			}

			// 用于新装备继承被替换装备的快捷栏位置
			// 只有当前装备原本不在快捷栏时才会用到
			slotOfUnequipped = -1;

			doEquip(hero);

			if (!slots.isEmpty()) {

				// 装备后重新关联全部原槽位
				for (int i = 0; i < slots.size(); i++) {
					Dungeon.quickslot.setSlotWithAction(
							slots.get(i),
							this,
							savedActions[i]
					);
				}

				updateQuickslot();

			} else if (slotOfUnequipped != -1
					&& defaultAction() != null) {

				// 如果当前装备原本没有快捷栏位置,但被替换下来的装备有快捷栏位置,则让新装备继承那一个槽位
				Dungeon.quickslot.setSlot(slotOfUnequipped, this);
				updateQuickslot();
			}
		} else if (action.equals( AC_UNEQUIP )) {
			doUnequip( hero, true );
		}
	}

	@Override
	public void doDrop( Hero hero ) {
		if (!isEquipped( hero ) || doUnequip( hero, false, false )) {
			super.doDrop( hero );
		}
	}

	@Override
	public void cast( final Hero user, int dst ) {

		if (isEquipped( user )) {
			if (quantity == 1 && !this.doUnequip( user, false, false )) {
				return;
			}
		}

		super.cast( user, dst );
	}

	public static void equipCursed( Hero hero ) {
		hero.sprite.emitter().burst( ShadowParticle.CURSE, 6 );
		Sample.INSTANCE.play( Assets.Sounds.CURSED );
	}

	protected float timeToEquip( Hero hero ) {
		return 1f;
	}

	public abstract boolean doEquip( Hero hero );

	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {

		if (cursed
				&& hero.buff(MagicImmune.class) == null
				&& (!hero.belongings.lostInventory() || keptThroughLostInventory())) {
			GLog.w(Messages.get(EquipableItem.class, "unequip_cursed"));
			return false;
		}

		if (single) {
			hero.spendAndNext( timeToEquip( hero ) );
		} else {
			hero.spend( timeToEquip( hero ) );
		}

		slotOfUnequipped = Dungeon.quickslot.getSlot(this);

		//temporarily keep this item so it can be collected
		boolean wasKept = keptThoughLostInvent;
		keptThoughLostInvent = true;
		if (!collect || !collect( hero.belongings.backpack )) {
			onDetach();
			Dungeon.quickslot.clearItem(this);
			updateQuickslot();
			if (collect) Dungeon.level.drop( this, hero.pos ).sprite.drop();
		}
		keptThoughLostInvent = wasKept;

		return true;
	}

	final public boolean doUnequip( Hero hero, boolean collect ) {
		return doUnequip( hero, collect, true );
	}

	/**
	 * 无副作用地判断该装备当前是否允许卸下
	 * 与 doUnequip 的拦截条件保持一致
	 */
	public boolean canUnequip( Hero hero ) {
		return !(cursed
				&& hero.buff(MagicImmune.class) == null
				&& (!hero.belongings.lostInventory() || keptThroughLostInventory()));
	}

	public void activate( Char ch ){}
}
