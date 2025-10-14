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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.BookBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.HerbBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.KingBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.LingBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PropBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.PointF;
import com.watabou.utils.Signal;

import java.util.ArrayList;

public class InventoryPane extends Component {
	// 常量定义
	private static final int EQUIPPED_SLOTS_COUNT = 5;
	private static final float DISABLED_ALPHA = 0.3f;
	private static final float ENABLED_ALPHA = 1f;
	private static final int ENERGY_COLOR = 0x44CCFF;
	public static final int WIDTH = 187;
	public static final int HEIGHT = 102;
	private static final int SLOT_WIDTH = 17;
	private static final int SLOT_HEIGHT = 23;
	private static final int SLOTS_PER_ROW = 10; // 每行显示的格子数量
	private static final int SCROLL_PANE_HEIGHT = 72; // 滚动区域的高度

	private NinePatch bg;
	private NinePatch bg2; //2 backgrounds to reduce transparency

	//used to prevent clicks through the BG normally, or to cancel selectors if they're enabled
	private PointerArea blocker;

	private Signal.Listener<KeyEvent> keyBlocker;

	private static InventoryPane instance;

	private ArrayList<InventorySlot> equipped;
	// 改为使用滚动窗格
	private ScrollPane bagScrollPane;
	private Component bagContainer;
	private ArrayList<InventorySlot> bagItems;

	private Image gold;
	private BitmapText goldTxt;
	private Image energy;
	private BitmapText energyTxt;
	private RenderedTextBlock promptTxt;

	private ArrayList<BagButton> bags;

	// 分页相关变量
	private int currentPage = 0;
	private static final int BAGS_PER_PAGE = 4; // 每页显示的背包数量
	private PageCycleButton pageCycleBtn; // 循环翻页按钮
	private BitmapText pageIndicator; // 页码指示器

	private WndBag.ItemSelector selector;

	public static Bag lastBag;

	private boolean lastEnabled = true;

	private static Image crossB;
	private static Image crossM;

	private static boolean targeting = false;
	private static InventorySlot targetingSlot = null;
	public static Char lastTarget = null;

	public InventoryPane(){
		super();
		instance = this;
	}

	@Override
	public synchronized void destroy() {
		KeyEvent.removeKeyListener(keyBlocker);
		super.destroy();
		cleanup();
	}
	private void cleanup() {
		if (instance == this) {
			instance = null;
			crossB = null;
			crossM = null;
			targetingSlot = null;
			lastTarget = null;
		}
		if (equipped != null) {
			equipped.clear();
			equipped = null;
		}
		if (bagItems != null) {
			bagItems.clear();
			bagItems = null;
		}
		if (bags != null) {
			bags.clear();
			bags = null;
		}
	}

	@Override
	protected void createChildren() {
		try {
			createBackgrounds();
			createBlocker();
			createEquippedSlots();
			createResourceDisplays();
			createBagContainerAndScrollPane();
			createBagButtons();
			createPageElements();
			createTargetingCrosshairs();
			lastEnabled = true;
			updateInventory();
			width = WIDTH;
			height = HEIGHT;
		} catch (Exception e) {
			resetToDefaultState();
		}
	}
	private void createBackgrounds() {
		bg = Chrome.get(Chrome.Type.TOAST_TR);
		add(bg);

		bg2 = Chrome.get(Chrome.Type.TOAST_TR);
		add(bg2);
	}
	private void createBlocker() {
		blocker = new PointerArea(0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height){
			@Override
			protected void onClick(PointerEvent event) {
				if (selector != null && !bg.overlapsScreenPoint((int)event.current.x, (int)event.current.y)){
					GameScene.centerNextWndOnInvPane();
					selector.onSelect(null);
					selector = null;
					updateInventory();
				}
			}
		};
		blocker.target = bg;
		add(blocker);

		keyBlocker = new Signal.Listener<KeyEvent>(){
			@Override
			public boolean onSignal(KeyEvent keyEvent) {
				if (keyEvent.pressed && isSelecting() && InventoryPane.this.visible
						&& !isBagActionKey(keyEvent)){
					GameScene.centerNextWndOnInvPane();
					selector.onSelect(null);
					selector = null;
					updateInventory();
					return true;
				}
				return false;
			}
		};
	}
	private boolean isBagActionKey(KeyEvent keyEvent) {
		GameAction action = KeyBindings.getActionForKey(keyEvent);
		return action == SPDAction.BAG_1 || action == SPDAction.BAG_2 ||
				action == SPDAction.BAG_3 || action == SPDAction.BAG_4 ||
				action == SPDAction.BAG_5;
	}
	private void createEquippedSlots() {
		equipped = new ArrayList<>(EQUIPPED_SLOTS_COUNT);
		for (int i = 0; i < EQUIPPED_SLOTS_COUNT; i++){
			InventorySlot btn = new InventoryPaneSlot(null);
			equipped.add(btn);
			add(btn);
		}
	}
	private void createResourceDisplays() {
		gold = Icons.get(Icons.COIN_SML);
		add(gold);
		goldTxt = new BitmapText(PixelScene.pixelFont);
		goldTxt.hardlight(Window.TITLE_COLOR);
		add(goldTxt);

		energy = Icons.get(Icons.ENERGY_SML);
		add(energy);
		energyTxt = new BitmapText(PixelScene.pixelFont);
		energyTxt.hardlight(ENERGY_COLOR);
		add(energyTxt);

		promptTxt = PixelScene.renderTextBlock(6);
		promptTxt.hardlight(Window.TITLE_COLOR);
		add(promptTxt);
	}
	private void createBagContainerAndScrollPane() {
		// 创建背包物品容器
		bagContainer = new Component();
		bagItems = new ArrayList<>();
		// 创建滚动窗格，将背包容器放入其中
		bagScrollPane = new ScrollPane(bagContainer);
		add(bagScrollPane);
	}
	private void createBagButtons() {
		bags = new ArrayList<>(BAGS_PER_PAGE);
		for (int i = 0; i < BAGS_PER_PAGE; i++){
			BagButton btn = new BagButton(null, i+1);
			bags.add(btn);
			add(btn);
		}
	}
	/**
	 * 创建页面元素的方法
	 * 用于初始化并添加页面切换按钮和页面指示器
	 */
	private void createPageElements() {
		// 创建并添加页面循环按钮
		pageCycleBtn = new PageCycleButton();
		add(pageCycleBtn);
		// 创建并添加页面指示器文本
		pageIndicator = new BitmapText(PixelScene.pixelFont);
		// 设置文本颜色为标题颜色
		pageIndicator.hardlight(Window.TITLE_COLOR);
		add(pageIndicator);
	}
	private void createTargetingCrosshairs() {
		crossB = Icons.TARGET.get();
		crossB.visible = false;
		add(crossB);

		crossM = new Image();
		crossM.copy(crossB);
	}

	@Override
	protected void layout() {
		width = WIDTH;
		height = HEIGHT;

		layoutBackgrounds();
		layoutEquippedSlots();
		layoutPromptsAndResources();
		layoutBagButtons();
		layoutPageElements();
		layoutBagScrollPane();

		super.layout();
	}

	private void layoutBackgrounds() {
		bg.x = bg2.x = x;
		bg.y = bg2.y = y;
		bg.size(width, height);
		bg2.size(width, height);
	}

	private void layoutEquippedSlots() {
		float left = x + 4;
		for (InventorySlot slot : equipped){
			slot.setRect(left, y + 4, SLOT_WIDTH, SLOT_HEIGHT);
			left = slot.right() + 1;
		}
	}

	private void layoutPromptsAndResources() {
		float equipEnd = x + 4 + (EQUIPPED_SLOTS_COUNT * (SLOT_WIDTH + 1) - 1);
		promptTxt.maxWidth((int)(width - (equipEnd - x) - bg.marginRight()));
		if (promptTxt.height() > 10){
			promptTxt.setPos(equipEnd, y + 2 + (12 - promptTxt.height()) / 2);
		} else {
			promptTxt.setPos(equipEnd, y + 4 + (10 - promptTxt.height()) / 2);
		}

		goldTxt.x = equipEnd;
		goldTxt.y = y + 5.5f - 1;
		PixelScene.align(goldTxt);
		gold.x = goldTxt.x + goldTxt.width() + 1;
		gold.y = goldTxt.y - 1;

		energyTxt.x = gold.x + gold.width() + 2;
		energyTxt.y = y + 5.5f - 1;
		PixelScene.align(energyTxt);
		energy.x = energyTxt.x + energyTxt.width() + 1;
		energy.y = energyTxt.y - 1;
	}

	private void layoutBagButtons() {
		float equipEnd = x + 4 + (EQUIPPED_SLOTS_COUNT * (SLOT_WIDTH + 1) - 1);
		float left = equipEnd + 1;
		for (BagButton bag : bags){
			bag.setRect(left, y + 13, SLOT_WIDTH, 14);
			left = bag.right() + 1;
		}
	}

	private void layoutPageElements() {
		float lastBagRight = bags.isEmpty() ? x + 4 + (EQUIPPED_SLOTS_COUNT * (SLOT_WIDTH + 1) - 1) :
				bags.get(bags.size() - 1).right();
		pageCycleBtn.setRect(lastBagRight + 1, y + 13, SLOT_WIDTH, 14);
		pageIndicator.x = lastBagRight + 3.5f;
		pageIndicator.y = y + 5f;
	}

	private void layoutBagScrollPane() {
		// 设置滚动窗格的位置和大小
		bagScrollPane.setRect(x + 4, y + 4 + SLOT_HEIGHT + 1,
				WIDTH - 8, SCROLL_PANE_HEIGHT);
		// 布局背包容器中的格子
		layoutBagSlotsInContainer();
	}

	private void layoutBagSlotsInContainer() {
		if (bagItems.isEmpty()) return;

		float left = 0;
		float top = 0;
		int slotsPerRow = SLOTS_PER_ROW;

		// 计算需要的行数
		int totalSlots = bagItems.size();
		int rows = (int) Math.ceil((double) totalSlots / slotsPerRow);

		// 设置容器的大小
		bagContainer.setSize(WIDTH - 8, rows * (SLOT_HEIGHT + 1));

		for (int i = 0; i < totalSlots; i++) {
			InventorySlot slot = bagItems.get(i);
			slot.visible = true;
			slot.setRect(left, top, SLOT_WIDTH, SLOT_HEIGHT);
			left += SLOT_WIDTH + 1;
			if ((i + 1) % slotsPerRow == 0) {
				left = 0;
				top += SLOT_HEIGHT + 1;
			}
		}
	}

	public void alpha( float value ){
		bg.alpha( value );
		bg2.alpha( value );

		for (InventorySlot slot : equipped){
			slot.alpha( value );
		}
		for (InventorySlot slot : bagItems){
			slot.alpha( value );
		}

		gold.alpha(value);
		goldTxt.alpha(value);
		energy.alpha(value);
		energyTxt.alpha(value);

		for (BagButton bag : bags){
			bag.alpha( value );
		}
	}

	public static void refresh(){
		if (instance != null) instance.updateInventory();
	}

	public void updateInventory() {
		if (isUpdating) return;
		isUpdating = true;
		try {
			setupSelectorState();
			updateEquippedItems();
			updateBagItems();
			updatePrompts();
			updateBagButtonsAndPagination();
			updateElementStates();
			layout();
		} catch (Exception e) {
			// 只重置状态，不触发更新
			lastBag = Dungeon.hero.belongings.backpack;
			currentPage = 0;
			selector = null;
		} finally {
			isUpdating = false;
		}
	}

	// 添加状态标志
	private boolean isUpdating = false;

	//设置选择器状态
	private void setupSelectorState() {
		if (selector == null) {
			blocker.target = bg;
			KeyEvent.removeKeyListener(keyBlocker);
		} else {
			blocker.target = blocker;
			KeyEvent.addKeyListener(keyBlocker);
		}
	}

	// 更新装备槽位中的物品
	private void updateEquippedItems() {
		Belongings stuff = Dungeon.hero.belongings;

		if (lastBag == null || !stuff.getBags().contains(lastBag)) {
			lastBag = stuff.backpack;
		}

		equipped.get(0).item(stuff.weapon == null ? new WndBag.Placeholder(ItemSpriteSheet.WEAPON_HOLDER) : stuff.weapon);
		equipped.get(1).item(stuff.armor == null ? new WndBag.Placeholder(ItemSpriteSheet.ARMOR_HOLDER) : stuff.armor);
		equipped.get(2).item(stuff.artifact == null ? new WndBag.Placeholder(ItemSpriteSheet.ARTIFACT_HOLDER) : stuff.artifact);
		equipped.get(3).item(stuff.misc == null ? new WndBag.Placeholder(ItemSpriteSheet.SOMETHING) : stuff.misc);
		equipped.get(4).item(stuff.ring == null ? new WndBag.Placeholder(ItemSpriteSheet.RING_HOLDER) : stuff.ring);
	}

	//更新背包中的物品
	private void updateBagItems() {
		// 清空现有格子
		for (InventorySlot slot : bagItems) {
			slot.destroy();
		}
		bagItems.clear();
		bagContainer.clear();

		bagScrollPane.scrollTo(0, 0);

		if (lastBag == null) {
			return;
		}

		Belongings stuff = Dungeon.hero.belongings;
		ArrayList<Item> items = (ArrayList<Item>) lastBag.items.clone();

		if (lastBag == stuff.backpack && stuff.secondWep != null) {
			items.add(0, stuff.secondWep);
		}

		// 动态创建格子
		int slotsToCreate = Math.min(lastBag.capacity(), items.size());
		for (int i = 0; i < slotsToCreate; i++) {
			InventorySlot slot = new InventoryPaneSlot(items.get(i));
			bagItems.add(slot);
			bagContainer.add(slot);
		}

		// 如果背包容量大于当前物品数量，创建空格子
		if (lastBag.capacity() > items.size()) {
			for (int i = items.size(); i < lastBag.capacity(); i++) {
				InventorySlot slot = new InventoryPaneSlot(null);
				bagItems.add(slot);
				bagContainer.add(slot);
			}
		}
	}

	//更新界面提示
	private void updatePrompts() {
		if (selector == null) {
			promptTxt.visible = false;
			updateGoldDisplay();
			updateEnergyDisplay();
		} else {
			promptTxt.text(selector.textPrompt());
			promptTxt.visible = true;
			goldTxt.visible = gold.visible = false;
			energyTxt.visible = energy.visible = false;
		}
	}

	private void updateGoldDisplay() {
		goldTxt.text(Integer.toString(Dungeon.gold));
		goldTxt.measure();
		goldTxt.visible = gold.visible = true;
	}

	private void updateEnergyDisplay() {
		energyTxt.text(Integer.toString(Dungeon.energy));
		energyTxt.measure();
		energyTxt.visible = energy.visible = Dungeon.energy > 0;
	}

	//更新背包按钮和分页状态
	private void updateBagButtonsAndPagination() {
		ArrayList<Bag> inventBags = Dungeon.hero.belongings.getBags();
		int totalPages = getTotalPages();

		validateCurrentPage(totalPages);
		updateVisibleBags(inventBags);
		updatePageButton(totalPages);
	}

	private int getTotalPages() {
		ArrayList<Bag> inventBags = Dungeon.hero.belongings.getBags();
		return Math.max(1, (int) Math.ceil((double) inventBags.size() / BAGS_PER_PAGE));
	}

	private void validateCurrentPage(int totalPages) {
		if (currentPage >= totalPages && totalPages > 0) {
			currentPage = totalPages - 1;
		} else if (totalPages == 0) {
			currentPage = 0;
		}
	}

	private void updateVisibleBags(ArrayList<Bag> inventBags) {
		int startIndex = currentPage * BAGS_PER_PAGE;
		int endIndex = Math.min(startIndex + BAGS_PER_PAGE, inventBags.size());

		for (int i = 0; i < bags.size(); i++) {
			BagButton button = bags.get(i);
			int bagIndex = startIndex + i;
			if (bagIndex < endIndex) {
				button.bag(inventBags.get(bagIndex));
				button.visible = true;
			} else {
				button.bag(null);
				button.visible = false;
			}
		}
	}

	//更新所有元素的启用状态
	private void updateElementStates() {
		updateEquippedSlotsEnabledState();
		updateBagSlotsEnabledState();
		updateBagButtonsEnabledState();
		updateResourceDisplayEnabledState();
	}

	private void updateEquippedSlotsEnabledState() {
		for (InventorySlot slot : equipped) {
			slot.enable(isEquippedSlotEnabled(slot));
		}
	}

	private void updateBagSlotsEnabledState() {
		for (InventorySlot slot : bagItems) {
			slot.enable(isSlotEnabled(slot));
		}
	}

	private void updateBagButtonsEnabledState() {
		for (BagButton button : bags) {
			button.enable(lastEnabled);
		}
		pageCycleBtn.enable(lastEnabled);
	}

	private void updateResourceDisplayEnabledState() {
		float alpha = lastEnabled ? ENABLED_ALPHA : DISABLED_ALPHA;
		goldTxt.alpha(alpha);
		gold.alpha(alpha);
		energyTxt.alpha(alpha);
		energy.alpha(alpha);
	}

	private boolean isSlotEnabled(InventorySlot slot) {
		if (!lastEnabled) return false;
		if (slot.item() == null) return false;
		boolean lostInvent = Dungeon.hero.belongings.lostInventory();
		if (lostInvent && !slot.item().keptThroughLostInventory()) return false;
		return selector == null || selector.itemSelectable(slot.item());
	}

	private boolean isEquippedSlotEnabled(InventorySlot slot) {
		if (!lastEnabled) return false;
		if (slot.item() instanceof WndBag.Placeholder) return false;
		boolean lostInvent = Dungeon.hero.belongings.lostInventory();
		if (lostInvent && !slot.item().keptThroughLostInventory()) return false;
		return selector == null || selector.itemSelectable(slot.item());
	}

	private void updatePageButton(int totalPages) {
		boolean showPageButton = totalPages > 1;
		pageCycleBtn.visible = showPageButton;
		pageIndicator.visible = showPageButton;

		if (showPageButton) {
			pageCycleBtn.enable(lastEnabled);
			pageCycleBtn.alpha(lastEnabled ? ENABLED_ALPHA : DISABLED_ALPHA);
			pageIndicator.text((currentPage + 1) + "/" + totalPages);
			pageIndicator.measure();
			pageIndicator.alpha(lastEnabled ? ENABLED_ALPHA : DISABLED_ALPHA);
		} else {
			pageIndicator.text("");
		}
	}

	public void setSelector(WndBag.ItemSelector selector){
		this.selector = selector;
		if (selector.preferredBag() == Belongings.Backpack.class){
			lastBag = Dungeon.hero.belongings.backpack;
		} else if (selector.preferredBag() != null) {
			Bag preferred = Dungeon.hero.belongings.getItem(selector.preferredBag());
			if (preferred != null)  lastBag = preferred;
				//if a specific preferred bag isn't present, then the relevant items will be in backpack
			else                    lastBag = Dungeon.hero.belongings.backpack;
		}
		updateInventory();
	}

	public boolean isSelecting(){
		return selector != null;
	}

	public static void clearTargetingSlot(){
		targetingSlot = null;
	}

	public static void useTargeting(){
		if (instance != null &&
				instance.visible &&
				lastTarget != null &&
				targetingSlot != null &&
				Actor.chars().contains( lastTarget ) &&
				lastTarget.isAlive() &&
				lastTarget.alignment != Char.Alignment.ALLY &&
				Dungeon.level.heroFOV[lastTarget.pos]) {

			targeting = true;
			CharSprite sprite = lastTarget.sprite;

			if (sprite.parent != null) {
				sprite.parent.addToFront(crossM);
				crossM.point(sprite.center(crossM));
			}

			crossB.point(targetingSlot.sprite.center(crossB));
			crossB.visible = true;

		} else {

			lastTarget = null;
			targeting = false;

		}
	}

	public static void cancelTargeting(){
		if (targeting){
			crossB.visible = false;
			crossM.remove();
			targeting = false;
		}
	}

	@Override
	public synchronized void update() {
		super.update();
		updateEnabledState();
	}

	private void updateEnabledState() {
		boolean newEnabledState = Dungeon.hero.ready || !Dungeon.hero.isAlive();
		if (lastEnabled != newEnabledState) {
			lastEnabled = newEnabledState;
			updateAllElementsEnabledState();
		}
	}

	private void updateAllElementsEnabledState() {
		updateEquippedSlotsEnabledState();
		updateBagSlotsEnabledState();
		updateBagButtonsEnabledState();
		updateResourceDisplayEnabledState();
		updatePageButtonEnabledState();
	}

	private void updatePageButtonEnabledState() {
		int totalPages = getTotalPages();
		updatePageButton(totalPages);
	}

	//切换到下一页，如果是最后一页则回到第一页
	public void nextPage() {
		ArrayList<Bag> inventBags = Dungeon.hero.belongings.getBags();
		int totalPages = (int) Math.ceil((double) inventBags.size() / BAGS_PER_PAGE);

		if (currentPage < totalPages - 1) {
			currentPage++;
		} else {
			currentPage = 0; // 循环回到第一页
		}
		updateInventory();
	}

	public void resetToFirstPage() {
		currentPage = 0;
		updateInventory();
	}

	private void resetToDefaultState() {
		lastBag = Dungeon.hero.belongings.backpack;
		currentPage = 0;
		selector = null;
	}

	private Image bagIcon(Bag bag ) {
		if (bag instanceof VelvetPouch) {
			return Icons.get( Icons.SEED_POUCH );
		} else if (bag instanceof ScrollHolder) {
			return Icons.get( Icons.SCROLL_HOLDER );
		} else if (bag instanceof MagicalHolster) {
			return Icons.get( Icons.WAND_HOLSTER );
		} else if (bag instanceof KingBag) {
			return Icons.get( Icons.B_BACKPACK );
		} else if (bag instanceof PotionBandolier) {
			return Icons.get( Icons.POTION_BANDOLIER );
		} else if (bag instanceof HerbBag) {
			return Icons.get( Icons.F_BACKPACK );
		} else if (bag instanceof LingBag) {
			return new Image("Ling.png", 0, 0, 16, 16);
		} else if (bag instanceof PropBag) {
			return Icons.get(Icons.PROPBAG);
		} else if (bag instanceof BookBag) {
			return Icons.get(Icons.BOOKBAG);
		} else {
			return Icons.get( Icons.BACKPACK );
		}
	}

	private class InventoryPaneSlot extends InventorySlot {

		private InventoryPaneSlot( Item item ){
			super(item);
		}

		@Override
		protected void onClick() {
			if (lastBag != item && !lastBag.contains(item) && !item.isEquipped(Dungeon.hero)){
				updateInventory();
				return;
			}

			if (targeting){
				if (targetingSlot == this){
					int cell = QuickSlotButton.autoAim(lastTarget, item());

					if (cell != -1){
						GameScene.handleCell(cell);
					} else {
						//couldn't auto-aim, just target the position and hope for the best.
						GameScene.handleCell( lastTarget.pos );
					}
					return;
				} else {
					cancelTargeting();
				}
			}

			//any windows opened as a consequence of this button should be centered on the inventory
			GameScene.centerNextWndOnInvPane();
			if (selector != null) {
				WndBag.ItemSelector activating = selector;
				selector = null;
				activating.onSelect( item );
				updateInventory();
			} else {
				targetingSlot = this;
				GameScene.show(new WndUseItem( null, item ));
			}
		}

		@Override
		protected boolean onLongClick() {
			if (selector == null && item.defaultAction() != null) {
				QuickSlotButton.set( item );
				return true;
			} else if (selector != null) {
				GameScene.centerNextWndOnInvPane();
				GameScene.show(new WndInfoItem(item));
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onMiddleClick() {
			if (lastBag != item && !lastBag.contains(item) && !item.isEquipped(Dungeon.hero)){
				updateInventory();
				return;
			}

			if (!Dungeon.hero.isAlive() || !Dungeon.hero.ready){
				return;
			}

			if (targeting){
				if (targetingSlot == this){
					onClick();
				}
				return;
			}

			if (selector == null && item.defaultAction() != null){
				item.execute(Dungeon.hero);
				if (item != null && item.usesTargeting) {
					targetingSlot = this;
					InventoryPane.useTargeting();
				}
			} else {
				onClick();
			}
		}

		@Override
		protected void onRightClick() {
			if (lastBag != item && !lastBag.contains(item) && !item.isEquipped(Dungeon.hero)){
				updateInventory();
				return;
			}

			if (!Dungeon.hero.isAlive() || !Dungeon.hero.ready){
				return;
			}

			if (targeting){
				//do nothing
				return;
			}

			if (selector == null){
				targetingSlot = this;
				RightClickMenu r = new RightClickMenu(item);
				parent.addToFront(r);
				r.camera = camera();
				PointF mousePos = PointerEvent.currentHoverPos();
				mousePos = camera.screenToCamera((int)mousePos.x, (int)mousePos.y);
				r.setPos(mousePos.x-3, mousePos.y-3);
			} else {
				//do nothing
			}
		}
	}

	private class BagButton extends IconButton {

		private static final int ACTIVE       = 0x9953564D;
		private static final int INACTIVE  = 0x9942443D;

		private ColorBlock bgTop;
		private ColorBlock bgBottom;

		private Bag bag;
		private final int index;

		public BagButton( Bag bag, int index ){
			super( bagIcon(bag) );
			this.bag = bag;
			this.index = index;
			visible = active = bag != null;
		}

		public void bag( Bag bag ){
			this.bag = bag;
			icon(bagIcon(bag));
			visible = active = bag != null;

			if (lastBag == bag){
				bgTop.texture(TextureCache.createSolid(ACTIVE));
				bgBottom.texture(TextureCache.createSolid(ACTIVE));
			} else {
				bgTop.texture(TextureCache.createSolid(INACTIVE));
				bgBottom.texture(TextureCache.createSolid(INACTIVE));
			}
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			bgTop = new ColorBlock(1, 1, ACTIVE);
			add(bgTop);

			bgBottom = new ColorBlock(1, 1, ACTIVE);
			add(bgBottom);
		}

		@Override
		protected void layout() {
			super.layout();

			bgTop.size(width-2, 1);
			bgTop.y = y;
			bgTop.x = x+1;

			bgBottom.size(width, height-1);
			bgBottom.y = y+1;
			bgBottom.x = x;
		}

		public void alpha( float value ){
			bgTop.alpha(value);
			bgBottom.alpha(value);
			icon.alpha(value);
		}

		@Override
		protected void onClick() {
			super.onClick();
			GameScene.cancel();
			lastBag = bag;
			refresh();
		}

		@Override
		public GameAction keyAction() {
			switch (index){
				case 1:
					return SPDAction.BAG_1;
				case 2:
					return SPDAction.BAG_2;
				case 3:
					return SPDAction.BAG_3;
				case 4:
					return SPDAction.BAG_4;
				case 5:
					return SPDAction.BAG_5;
				default:
					return null;
			}
		}

		@Override
		public GameAction secondaryTooltipAction() {
			return SPDAction.INVENTORY_SELECTOR;
		}

		@Override
		protected String hoverText() {
			if (bag != null) {
				return Messages.titleCase(bag.name());
			} else {
				return null;
			}
		}
	}

	//循环翻页按钮类
	private class PageCycleButton extends IconButton {

		private static final int ACTIVE = 0x9953564D;
		private static final int INACTIVE = 0x9942443D;

		private ColorBlock bgTop;
		private ColorBlock bgBottom;
		private final String tooltipText;

		public PageCycleButton() {
			super(Icons.get(Icons.CHANGES)); // 使用循环箭头图标
			this.tooltipText = "翻页";
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			bgTop = new ColorBlock(1, 1, ACTIVE);
			add(bgTop);

			bgBottom = new ColorBlock(1, 1, ACTIVE);
			add(bgBottom);
		}

		@Override
		protected void layout() {
			super.layout();

			bgTop.size(width-2, 1);
			bgTop.y = y;
			bgTop.x = x+1;

			bgBottom.size(width, height-1);
			bgBottom.y = y+1;
			bgBottom.x = x;
		}

		public void alpha(float value) {
			bgTop.alpha(value);
			bgBottom.alpha(value);
			icon.alpha(value);
		}

		@Override
		protected void onClick() {
			nextPage();
		}

		@Override
		protected String hoverText() {
			return tooltipText;
		}
	}

}
