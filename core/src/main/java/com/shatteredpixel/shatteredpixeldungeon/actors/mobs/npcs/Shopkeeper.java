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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessNoMoney;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayMoneyMore;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.thanks.DistressSignalNesting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRushTradeItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class Shopkeeper extends NPC {

	{
		spriteClass = ShopkeeperSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	public static int MAX_BUYBACK_HISTORY = 3;
	public ArrayList<Item> buybackItems = new ArrayList<>();

	private int turnsSinceHarmed = -1;
	private boolean seenBefore = false;
	@Override
	protected boolean act() {
		if(Statistics.endingbald &&  getClass() == Shopkeeper.class) {
			flee();
		}
		if (Dungeon.level.visited[pos]){
			Notes.add(Notes.Landmark.SHOP);
		}

		if (turnsSinceHarmed >= 0){
			turnsSinceHarmed ++;
		}

		if (!seenBefore && Dungeon.level.heroFOV[pos]) {
			if (Dungeon.hero.buff(AscensionChallenge.class) != null) {
				yell(Messages.get(this, "talk_ascent", Messages.titleCase(Dungeon.hero.name())));
			}
			seenBefore = true;
		}
		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return super.act();
	}

	@Override
	public void damage(int dmg, Object src, DamageType type) {
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}



	@Override
	public boolean add(Buff buff ) {
		return false;
	}

	public void flee() {
		destroy();

		Notes.remove(Notes.Landmark.SHOP);

		if (sprite != null) {
			sprite.killAndErase();
			CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		if(Statistics.endingbald){
			for (Heap heap: Dungeon.level.heaps.valueList()) {
				if (heap.type == Heap.Type.FOR_SALE) {
					if (ShatteredPixelDungeon.scene() instanceof GameScene) {
						CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
					}
					if (heap.size() == 1) {
						heap.destroy();
					} else {
						heap.items.remove(heap.size()-1);
						heap.type = Heap.Type.HEAP;
					}
				}
			}
		} else {
			for (Heap heap: Dungeon.level.heaps.valueList()) {
				if (heap.type == Heap.Type.FOR_SALE) {
					if (ShatteredPixelDungeon.scene() instanceof GameScene) {
						CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
					}
					heap.type = Heap.Type.HEAP;
				}
			}
		}


	}

	@Override
	public boolean reset() {
		return true;
	}

	//shopkeepers are greedy!
	public static int sellPrice(Item item){
		int price = item.value() * 5 * (Dungeon.depth / 5 + 1);

		if(Dungeon.depth>26){
			if(!(item instanceof Ring || item instanceof Wand || item instanceof Artifact)){
				price *= 0.8;
			}
		}

		if(hero.buff(MagicGirlSayMoneyMore.class) != null){
			if(item instanceof Ankh ||item instanceof Food || item instanceof PotionOfHealing){
				price *= 2.5;
			}
		}

		if (hero.buff(BlessNoMoney.class) != null) {
			price *= 0.6f;
		}

		if (Dungeon.hero.buff(AscensionChallenge.class) != null && Dungeon.shopOnLevel()){
			price *= 3f;
		}

		return price;
	}

	public static int sellIcePrice(Item item){
		int price = item.iceCoinValue();

		if (Badges.isUnlocked(Badges.Badge.NYZ_SHOP)){
			price *= 0.9f;
		}

		return price;
	}

	public static int sellRushPrice(Item item){
		return item.RushValue();
	}

	public static WndBag sell() {
		return GameScene.selectItem( itemSelector );
	}

	public static boolean canSell(Item item){
		if (item.value() <= 0)                                              return false;
		if (item.unique && !item.stackable)                                 return false;
		if (item instanceof Armor && ((Armor) item).checkSeal() != null)    return false;
		if (item.isEquipped(Dungeon.hero) && item.cursed)                   return false;
		return true;
	}

	private static WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(Shopkeeper.class, "sell");
		}

		@Override
		public boolean itemSelectable(Item item) {
			return Shopkeeper.canSell(item);
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBag parentWnd = sell();
				if(Statistics.bossRushMode){
					GameScene.show( new WndRushTradeItem( item, parentWnd ) );
				} else {
					GameScene.show( new WndTradeItem( item, parentWnd ) );
				}
			}
		}
	};

	@Override
	public boolean interact(Char c) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				// 检查是否有可升级的求救信号套组
				DistressSignalNesting nesting = Dungeon.hero.belongings.getItem(DistressSignalNesting.class);
				final boolean hasUpgrade = nesting != null && nesting.level() < 3;
				final int upgradePrice = hasUpgrade ? nesting.getUpgradeCost() : 0;

				// 选项总数：出售、交谈、[升级]、回购列表
				int optionCount = 2 + (hasUpgrade ? 1 : 0) + buybackItems.size();
				String[] options = new String[optionCount];
				int maxLen = PixelScene.landscape() ? 30 : 25;
				int i = 0;

				options[i++] = Messages.get(Shopkeeper.this, "sell");
				options[i++] = Messages.get(Shopkeeper.this, "talk");

				if (hasUpgrade) {
					String levelName;
					switch (nesting.level()) {
						case 0: levelName = Messages.get(Shopkeeper.this, "upgrade_name_0"); break;
						case 1: levelName = Messages.get(Shopkeeper.this, "upgrade_name_1"); break;
						default: levelName = Messages.get(Shopkeeper.this, "upgrade_name_2"); break;
					}
					options[i] = Messages.get(Shopkeeper.this, "upgrade_option", levelName, upgradePrice);
					if (options[i].length() > maxLen) options[i] = options[i].substring(0, maxLen-3) + "...";
					i++;
				}

				for (Item item : buybackItems) {
					options[i] = Messages.get(Heap.class, "for_sale", item.value(), Messages.titleCase(item.title()));
					if (options[i].length() > maxLen) options[i] = options[i].substring(0, maxLen-3) + "...";
					i++;
				}

				final DistressSignalNesting finalNesting = nesting;
				GameScene.show(new WndOptions(sprite(), Messages.titleCase(name()), description(), options) {
					@Override
					protected void onSelect(int index) {
						super.onSelect(index);
						if (index == 0) {
							sell();
						} else if (index == 1) {
							GameScene.show(new WndTitledMessage(sprite(), Messages.titleCase(name()), chatText()));
						} else if (hasUpgrade && index == 2) {
							// 弹出升级确认窗口
							String confirmMsg;
							switch (finalNesting.level()) {
								case 0: confirmMsg = Messages.get(Shopkeeper.this, "upgrade_msg_0"); break;
								case 1: confirmMsg = Messages.get(Shopkeeper.this, "upgrade_msg_1"); break;
								default: confirmMsg = Messages.get(Shopkeeper.this, "upgrade_msg_2"); break;
							}
							GameScene.show(new WndOptions(
									sprite(),
									Messages.titleCase(name()),
									confirmMsg,
									Messages.get(Shopkeeper.this, "upgrade_confirm"),
									Messages.get(Shopkeeper.this, "upgrade_cancel")
							) {
								@Override
								protected void onSelect(int index) {
									if (index == 0) {
										Dungeon.gold -= upgradePrice;
										Statistics.goldCollected -= upgradePrice;
										finalNesting.upgrade();
										GLog.p(Messages.get(Shopkeeper.this, "upgrade_done"));
									}
								}
							});
						} else {
							// 回购物品（索引需要偏移）
							int buybackIndex = index - (hasUpgrade ? 3 : 2);
							GLog.i(Messages.get(Shopkeeper.this, "buyback"));
							Item returned = buybackItems.remove(buybackIndex);
							Dungeon.gold -= returned.value();
							Statistics.goldCollected -= returned.value();
							if (!returned.doPickUp(Dungeon.hero)) {
								Dungeon.level.drop(returned, Dungeon.hero.pos);
							}
						}
					}

					@Override
					protected boolean enabled(int index) {
						if (hasUpgrade && index == 2) {
							return Dungeon.gold >= upgradePrice;
						} else if (index >= (hasUpgrade ? 3 : 2)) {
							int buybackIndex = index - (hasUpgrade ? 3 : 2);
							return Dungeon.gold >= buybackItems.get(buybackIndex).value();
						} else {
							return true; // 出售和交谈总是可用
						}
					}

					@Override
					protected boolean hasIcon(int index) {
						if (hasUpgrade && index == 2) return true;
						return index >= (hasUpgrade ? 3 : 2);
					}

					@Override
					protected Image getIcon(int index) {
						if (hasUpgrade && index == 2) {
							return new ItemSprite(finalNesting);
						}
						if (index >= (hasUpgrade ? 3 : 2)) {
							int buybackIndex = index - (hasUpgrade ? 3 : 2);
							return new ItemSprite(buybackItems.get(buybackIndex));
						}
						return null;
					}
				});
			}
		});
		return true;
	}

	public String chatText(){
		if (Dungeon.hero.buff(AscensionChallenge.class) != null){
			return Messages.get(this, "talk_ascent");
		}
		switch (Dungeon.depth){
			case 6: default:
				return Messages.get(this, "talk_prison_intro") + "\n\n" + Messages.get(this, "talk_prison_" + Dungeon.hero.heroClass.name());
			case 11:
				return Messages.get(this, "talk_caves");
			case 16:
				return Messages.get(this, "talk_city");
			case 20:
				return Messages.get(this, "talk_halls");
		}
	}

	public static String BUYBACK_ITEMS = "buyback_items";

	public static String TURNS_SINCE_HARMED = "turns_since_harmed";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(BUYBACK_ITEMS, buybackItems);
		bundle.put(TURNS_SINCE_HARMED, turnsSinceHarmed);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		buybackItems.clear();
		if (bundle.contains(BUYBACK_ITEMS)){
			for (Bundlable i : bundle.getCollection(BUYBACK_ITEMS)){
				buybackItems.add((Item) i);
			}
		}
		turnsSinceHarmed = bundle.contains(TURNS_SINCE_HARMED) ? bundle.getInt(TURNS_SINCE_HARMED) : -1;
	}
}
