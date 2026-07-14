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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.shopOnLevel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShopGuardDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.props.LuckyGlove;
import com.shatteredpixel.shatteredpixeldungeon.items.thanks.DistressSignalNesting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MerchantSword;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireMagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class WndTradeItem extends WndInfoItem {

	private static final float GAP		= 2;
	private static final int BTN_HEIGHT	= 18;

	private WndBag owner;

	private boolean selling = false;

	private static float priceMulti;

	//selling
	public WndTradeItem( final Item item, WndBag owner ) {

		super(item);

		selling = true;

		this.owner = owner;

		priceMulti = 1f;

		float pos = height;
		Shopkeeper shop = null;
		for (Char ch : Actor.chars()){
			if (ch instanceof Shopkeeper){
				shop = (Shopkeeper) ch;
				break;
			}
		}
		final Shopkeeper finalShop = shop;
		if (item.quantity() == 1) {

			int price;
			if (item instanceof DistressSignalNesting) {
				price = ((DistressSignalNesting) item).shopValue();
			} else {
				int basePrice = Shopkeeper.sellPrice(item);
				price = (int) (basePrice * priceMulti);
			}

			RedButton btnSell = new RedButton( Messages.get(this, "sell", price)) {
				@Override
				protected void onClick() {
					sell( item,finalShop );
					hide();
				}
			};
			btnSell.setRect( 0, pos + GAP, width, BTN_HEIGHT );
			btnSell.icon(new ItemSprite(ItemSpriteSheet.GOLD));
			add( btnSell );

			pos = btnSell.bottom();

		} else {

			int priceAll;
			if (item instanceof DistressSignalNesting) {
				priceAll = ((DistressSignalNesting) item).shopValue();
			} else {
				priceAll = (int) (item.value() * priceMulti);
			}

			RedButton btnSell1 = new RedButton( Messages.get(this, "sell_1", priceAll / item.quantity()) ) {
				@Override
				protected void onClick() {
					sellOne( item );
					hide();
				}
			};
			btnSell1.setRect( 0, pos + GAP, width, BTN_HEIGHT );
			btnSell1.icon(new ItemSprite(ItemSpriteSheet.GOLD));
			add( btnSell1 );
			RedButton btnSellAll = new RedButton( Messages.get(this, "sell_all", priceAll ) ) {
				@Override
				protected void onClick() {
					sell( item,finalShop );
					hide();
				}
			};
			btnSellAll.setRect( 0, btnSell1.bottom() + 1, width, BTN_HEIGHT );
			btnSellAll.icon(new ItemSprite(ItemSpriteSheet.GOLD));
			add( btnSellAll );

			pos = btnSellAll.bottom();

		}

		resize( width, (int)pos );
	}

	//buying
	public WndTradeItem( final Heap heap ) {

		super(heap);

		selling = false;

		if(Dungeon.hero.belongings.weapon() instanceof MerchantSword){
			priceMulti = Math.max((1f - (0.10f + 0.05f * hero.belongings.weapon().buffedLvl())),0.1f);
		}else {
			priceMulti = 1f;
		}

		Item item = heap.peek();

		float pos = height;

		// ========== 修复：购买按钮显示价格同步固定1500逻辑 ==========
		int price;
		if (item instanceof DistressSignalNesting) {
			price = ((DistressSignalNesting) item).shopValue();
		} else {
			int basePrice = Shopkeeper.sellPrice(item);
			price = (int) (basePrice * priceMulti);
		}

		RedButton btnBuy = new RedButton( Messages.get(this, "buy", price) ) {
			@Override
			protected void onClick() {
				hide();
				buy( heap );
			}
		};
		btnBuy.setRect( 0, pos + GAP, width, BTN_HEIGHT );
		btnBuy.icon(new ItemSprite(ItemSpriteSheet.GOLD));
		btnBuy.enable( price <= Dungeon.gold );
		add( btnBuy );

		pos = btnBuy.bottom();

		RedButton btnStole = new RedButton( Statistics.fireGirlnoshopping && !Statistics.deadshoppingdied ?
				Messages.get(this,
						"oks"):Messages.get(this, "stole", price) ) {
			@Override
			protected void onClick() {
				hide();
				for (Mob mob : Dungeon.level.mobs) {
					if (mob instanceof Shopkeeper) {
						GameScene.show(new WndGoShop(this));
						break;
					}
				}
			}
		};


		btnStole.setRect( 0, pos + GAP, width, BTN_HEIGHT );
		btnStole.icon(Statistics.fireGirlnoshopping && !Statistics.deadshoppingdied ? new FireMagicGirlSprite() :
				new ShopGuardDead.ShopGuardianRedSprite());

		add( btnStole );

		if(shopOnLevel() && Dungeon.branch == 0){
			pos = btnStole.bottom();
			btnStole.visible=true;
		} else {
			pos = btnBuy.bottom()-2;
			btnStole.visible=false;
		}


		final MasterThievesArmband.Thievery thievery = Dungeon.hero.buff(MasterThievesArmband.Thievery.class);
		if (thievery != null && !thievery.isCursed() && thievery.chargesToUse(item) > 0) {
			final float chance = thievery.stealChance(item);
			final int chargesToUse = thievery.chargesToUse(item);
			RedButton btnSteal = new RedButton(Messages.get(this, "steal", Math.min(100, (int) (chance * 100)), chargesToUse), 6) {
				@Override
				protected void onClick() {
					if (thievery.steal(item)) {
						Hero hero = Dungeon.hero;
						Item item = heap.pickUp();
						hide();

						if (!item.doPickUp(hero)) {
							Dungeon.level.drop(item, heap.pos).sprite.drop();
						}
					} else {
						for (Mob mob : Dungeon.level.mobs) {
							if (mob instanceof Shopkeeper) {
								mob.yell(Messages.get(mob, "thief"));
								((Shopkeeper) mob).flee();
								break;
							}
						}
						hide();
					}
				}
			};
			btnSteal.setRect(0, pos + 1, width, BTN_HEIGHT);
			btnSteal.icon(new ItemSprite(ItemSpriteSheet.ARTIFACT_ARMBAND));
			add(btnSteal);

			pos = btnSteal.bottom();

		}

		resize(width, (int) pos);
	}

	@Override
	public void hide() {

		super.hide();

		if (owner != null) {
			owner.hide();
		}
		if (selling) Shopkeeper.sell();
	}

	public static void sell( Item item, Shopkeeper shop ) {

		Hero hero = Dungeon.hero;

		if (item.isEquipped( hero ) && !((EquipableItem)item).doUnequip( hero, false )) {
			return;
		}
		item.detachAll( hero.belongings.backpack );

		//selling items in the sell interface doesn't spend time
		hero.spend(-hero.cooldown());

		int sellPrice;
		if (item instanceof DistressSignalNesting) {
			sellPrice = ((DistressSignalNesting) item).shopValue();
		} else {
			sellPrice = (int) (item.value() * priceMulti);
		}
		new Gold(sellPrice).doPickUp( hero );

		if (shop != null){
			shop.buybackItems.add(item);
			while (shop.buybackItems.size() > Shopkeeper.MAX_BUYBACK_HISTORY){
				shop.buybackItems.remove(0);
			}
		}
	}

	public static void sellOne( Item item ) {
		sellOne( item, null );
	}

	public static void sellOne( Item item, Shopkeeper shop ) {

		if (item.quantity() <= 1) {
			sell( item, shop );
		} else {

			Hero hero = Dungeon.hero;

			item = item.detach( hero.belongings.backpack );

			//selling items in the sell interface doesn't spend time
			hero.spend(-hero.cooldown());

			int sellPrice;
			if (item instanceof DistressSignalNesting) {
				sellPrice = ((DistressSignalNesting) item).shopValue();
			} else {
				sellPrice = (int) (item.value() * priceMulti);
			}
			new Gold(sellPrice).doPickUp( hero );

			if (shop != null){
				shop.buybackItems.add(item);
				while (shop.buybackItems.size() > Shopkeeper.MAX_BUYBACK_HISTORY){
					shop.buybackItems.remove(0);
				}
			}
		}
	}

	private void buy( Heap heap ) {

		Item item = heap.pickUp();
		if (item == null) return;

		int price;
		if (item instanceof DistressSignalNesting) {
			price = ((DistressSignalNesting) item).shopValue();
		} else {
			int basePrice = Shopkeeper.sellPrice(item);
			price = (int) (basePrice * priceMulti);
		}

		if(hero.belongings.getItem(LuckyGlove.class)!=null && Random.Float()>0.85f) {
			GLog.n(Messages.get(LuckyGlove.class,"lucky"));
		}else{
			Dungeon.gold -= price;
		}
		Catalog.countUses(Gold.class, price);

		if (!item.doPickUp( Dungeon.hero )) {
			Dungeon.level.drop( item, heap.pos ).sprite.drop();
		}
	}
}