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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class BrokenSeal extends Item {

	public static final String AC_AFFIX = "AFFIX";

	//only to be used from the quickslot, for tutorial purposes mostly.
	public static final String AC_INFO = "INFO_WINDOW";

	{
		image = ItemSpriteSheet.SEAL;

		cursedKnown = levelKnown = true;
		unique = true;
		bones = false;

		defaultAction = AC_INFO;
	}

	private Armor.Glyph glyph;

	public Armor.Glyph getGlyph(){
		if (!Dungeon.hero.hasTalent(Talent.RUNIC_TRANSFERENCE))
			setGlyph(null);
		return glyph;
	}

	public void setGlyph( Armor.Glyph glyph ){
		this.glyph = glyph;
		if (this.glyph!=null){
			this.glyph.onSeal=true;
		}
	}
	public boolean hasCurseGlyph(){
		return glyph != null && glyph.curse();
	}
	public void inscribe() {

		Class<? extends Armor.Glyph> oldGlyphClass = glyph != null ? glyph.getClass() : null;
		Armor.Glyph gl = Armor.Glyph.random( oldGlyphClass );
		inscribe( gl );
	}

	public void inscribe( Armor.Glyph glyph ) {
		if (glyph == null || !glyph.curse()) curseInfusionBonus = false;
		setGlyph(glyph);
		updateQuickslot();
	}
	@Override
	public void getCurse(boolean extraEffect) {
		Class<? extends Armor.Glyph> oldGlyphClass = glyph != null ? glyph.getClass() : null;
		Armor.Glyph gl = Armor.Glyph.randomCurse( oldGlyphClass );
		setGlyph(gl);
	}


	public int maxShield( int armTier, int armLvl ){
		return armTier + armLvl ;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return glyph != null ? glyph.glowing() : null;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions =  super.actions(hero);
		actions.add(AC_AFFIX);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_AFFIX)){
			curItem = this;
			GameScene.selectItem(armorSelector);
		} else if (action.equals(AC_INFO)) {
			GameScene.show(new WndUseItem(null, this));
		}
	}

	@Override
	//scroll of upgrade can be used directly once, same as upgrading armor the seal is affixed to then removing it.
	public boolean isUpgradable() {
		return level() == 0;
	}

	protected static WndBag.ItemSelector armorSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return  Messages.get(BrokenSeal.class, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item instanceof Armor;
		}

		@Override
		public void onSelect( Item item ) {
			BrokenSeal seal = (BrokenSeal) curItem;
			if (item instanceof Armor) {
				Armor armor = (Armor)item;
				if (!armor.levelKnown){
					GLog.w(Messages.get(BrokenSeal.class, "unknown_armor"));

				} else if (armor.cursed && (seal.getGlyph() == null || !seal.getGlyph().curse())){
					GLog.w(Messages.get(BrokenSeal.class, "cursed_armor"));

				} else {
					GLog.p(Messages.get(BrokenSeal.class, "affix"));
					Dungeon.hero.sprite.operate(Dungeon.hero.pos);
					Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
					armor.affixSeal((BrokenSeal)curItem);
					curItem.detach(Dungeon.hero.belongings.backpack);
				}
			}
		}
	};
	@Override
	public String name() {
		return glyph != null ? glyph.name( super.name() ) : super.name();
	}
	@Override
	public String info() {
		String info = desc();
		if (glyph!=null ) {
			info+="\n\n" +  Messages.capitalize(Messages.get(Armor.class, "inscribed", glyph.name()));
			info += " " + glyph.desc();
		}
		return info;
	}
	private static final String GLYPH = "glyph";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(GLYPH, glyph);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		glyph = (Armor.Glyph)bundle.get(GLYPH);
	}

	public static class WarriorShield extends ShieldBuff {

		private Armor armor;
		private float partialShield;

		@Override
		public synchronized boolean act() {
			if (shielding() < maxShield()) {
				partialShield += 1/30f;
			}

			while (partialShield >= 1){
				incShield();
				partialShield--;
			}

			if (shielding() <= 0 && maxShield() <= 0){
				detach();
			}

			spend(TICK);
			return true;
		}

		public synchronized void supercharge(int maxShield){
			if (maxShield > shielding()){
				setShield(maxShield);
			}
		}

		public synchronized void setArmor(Armor arm){
			armor = arm;
		}

		public synchronized int maxShield() {
			//metamorphed iron will logic

			if (armor != null && armor.isEquipped((Hero)target) && armor.checkSeal() != null) {
				return armor.checkSeal().maxShield(armor.tier, armor.level());
			} else {
				return 0;
			}
		}

		@Override
		//logic edited slightly as buff should not detach
		public int absorbDamage(int dmg) {
			if (shielding() <= 0) return dmg;

			if (shielding() >= dmg){
				decShield(dmg);
				dmg = 0;
			} else {
				dmg -= shielding();
				decShield(shielding());
			}
			return dmg;
		}
	}
}
