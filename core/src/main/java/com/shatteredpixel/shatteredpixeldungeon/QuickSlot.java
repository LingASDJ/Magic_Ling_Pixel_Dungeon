package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;

public class QuickSlot {
	public static int SIZE = 12;

	public static int VSIZE = 10;
	public int quickslots;
	/**
	 * Slots contain objects which are also in a player's inventory. The one exception to this is when quantity is 0,
	 * which can happen for a stackable item that has been 'used up', these are referred to as placeholders.
	 */

	//note that the current max size is coded at 6, due to UI constraints, but it could be much much bigger with no issue.

	private Item[] slots = new Item[SIZE];
	// 每个快捷栏槽位当前选中的技能 action ID
	// 空值表示该槽位还没有指定技能，使用时需要让玩家选择
	private String[] slotActions = new String[SIZE];

	// 保存玩家为某个快捷栏槽位选择的技能
	// 记录 action ID，不记录显示文字；显示名称由 Item.actionName() 生成
	public void setSlotAction(int slot, String action) {
		slotActions[slot] = action;
	}
	// 读取某个快捷栏槽位保存的技能
	// 返回 null 表示没有记录技能，通常需要回退到“使用时选择”或补选窗口
	public String getSlotAction(int slot) {
		return slotActions[slot];
	}
	// 清空快捷栏槽位
	public void clearSlot(int slot) {
		slots[slot] = null;
		slotActions[slot] = null;
	}


	//direct array interaction methods, everything should build from these methods.
	public void setSlot(int slot, Item item){
		clearItem(item); //we don't want to allow the same item in multiple slots.
		setSlotInternal(slot, item, null);
	}

	/**
	 * 与 setSlot() 不同，这个方法不会调用 clearItem()，
	 * 因此同一个物品可以同时占用多个快捷栏槽位
	 * 只由“放入时选择”的多技能窗口调用
	 * 这样玩家可以让同一个物品在不同槽位绑定不同技能
	 */
	public void setSlotWithAction(int slot, Item item, String action) {
		setSlotInternal(slot, item, action);
	}

	public void reset(){
		slots = new Item[SIZE];slotActions = new String[SIZE];
	}

	public Item getItem(int slot){
		return slots[slot];
	}

	//utility methods, for easier use of the internal array.
	public int getSlot(Item item) {
		for (int i = 0; i < SIZE; i++) {
			if (getItem(i) == item) {
				return i;
			}
		}
		return -1;
	}

	public Boolean isPlaceholder(int slot){
		return getItem(slot) != null && getItem(slot).quantity() == 0;
	}

	public Boolean isNonePlaceholder(int slot){
		return getItem(slot) != null && getItem(slot).quantity() > 0;
	}

	/**
	 * 从快捷栏中清除这个物品占用的所有槽位
	 * 一个物品现在可能同时出现在多个快捷栏中了
	 * 物品被丢弃、移除或离开背包时，清除全部引用，避免快捷栏留下失效槽位
	 */
	public void clearItem(Item item) {
		for (int i = 0; i < SIZE; i++) {
			if (slots[i] == item) {
				clearSlot(i);
			}
		}
	}

	public boolean contains(Item item){
		return getSlot(item) != -1;
	}
	// 底层快捷栏写入方法，主要用于恢复存档和占位符替换，因为这些场景需要保留原有技能。
	public void replacePlaceholder(Item item) {
		for (int i = 0; i < SIZE; i++) {
			if (isPlaceholder(i) && item.quickslotSimilar(getItem(i))) {
				// 先保存旧技能，再替换物品。
				String oldAction = slotActions[i];
				setSlotInternal(i, item, oldAction);
			}
		}
	}
	/**
	 * 返回同一个物品占用的所有快捷栏槽位。
	 * 用于兼容原本只支持单槽位的老代码。
	 * 多个槽位主要用于“放入时选择”模式：
	 * 同一个物品可以放到不同快捷栏，并绑定不同技能。
	 */
	public ArrayList<Integer> getSlots(Item item) {
		ArrayList<Integer> result = new ArrayList<>();

		for (int i = 0; i < SIZE; i++) {
			if (slots[i] == item) {
				result.add(i);
			}
		}

		return result;
	}

	// 将实际物品转换成虚拟占位符。
	// 当物品数量变成 0 后，快捷栏仍需要记住原来的位置和技能。
	public void convertToPlaceholder(Item item){

		if (contains(item)) {
			Item placeholder = item.virtual();
			if (placeholder == null) return;

			for (int i = 0; i < SIZE; i++) {
				// 保存该槽位原来的技能，然后用占位符替换物品
				if (getItem(i) == item) {
					String oldAction = slotActions[i];
					setSlotInternal(i, placeholder, oldAction);
				}
			}
		}
	}

	public Item randomNonePlaceholder(){

		ArrayList<Item> result = new ArrayList<>();
		for (int i = 0; i < SIZE; i ++) {
			if (getItem(i) != null && !isPlaceholder(i)) {
				result.add(getItem(i));
			}
		}
		return Random.element(result);
	}
	private void setSlotInternal(int slot, Item item, String action) {
		slots[slot] = item;
		slotActions[slot] = action;
	}

	private final String PLACEHOLDERS = "placeholders";
	private final String PLACEMENTS = "placements";
	private static final String SLOT_ACTIONS = "slot_actions";

	/**
	 * Placements array is used as order is preserved while bundling, but exact index is not, so if we
	 * bundle both the placeholders (which preserves their order) and an array telling us where the placeholders are,
	 * we can reconstruct them perfectly.
	 */
	// 保存快捷栏占位符、占位符位置和每个槽位的技能
	// slotActions 中的 null 不能直接保存为 JSON null，所以这里统一转换成空字符串，读取时再还原成 null
	public void storePlaceholders(Bundle bundle){
		ArrayList<Item> placeholders = new ArrayList<>(SIZE);
		boolean[] placements = new boolean[SIZE];
		String[] savedActions = new String[SIZE];

		for (int i = 0; i < SIZE; i++) {
			// 记录当前哪些槽位是占位符
			if (isPlaceholder(i)) {
				placeholders.add(getItem(i));
				placements[i] = true;
			}
			// 没有技能的槽位保存为空字符串
			savedActions[i] = slotActions[i] == null ? "" : slotActions[i];
		}
		bundle.put( PLACEHOLDERS, placeholders );
		bundle.put( PLACEMENTS, placements );
		bundle.put(SLOT_ACTIONS, savedActions);
	}
	// 恢复快捷栏占位符和每个槽位的技能
	public void restorePlaceholders(Bundle bundle){
		Collection<Bundlable> placeholders = bundle.getCollection(PLACEHOLDERS);
		boolean[] placements = bundle.getBooleanArray( PLACEMENTS );

		// 兼容旧存档或异常数据
		if (placements == null) {
			placements = new boolean[SIZE];}

		String[] actions = bundle.contains(SLOT_ACTIONS)
				? bundle.getStringArray(SLOT_ACTIONS)
				: new String[SIZE];

		// 旧存档、字段缺失、读取失败时都使用空数组
		if (actions == null) {
			actions = new String[SIZE];
		}

		//先恢复整个技能数组，而不是只在占位符循环里恢复
		slotActions = new String[SIZE];

		for (int i = 0; i < SIZE; i++) {
			String action = i < actions.length ? actions[i] : "";

			if (action == null
					|| action.isEmpty()
					|| action.equals("null")) {
				slotActions[i] = null;
			} else {
				slotActions[i] = action;
			}
		}
		// 恢复占位符
		int i = 0;
		for (Bundlable item : placeholders){
			while (!placements[i]){
				i++;
			}
			setSlotInternal(i, (Item) item, slotActions[i]);
			i++;
		}

	}
	//防止清理已保存的快捷栏技能选择（1）
	//用新物品替换槽位内容，但保留该槽位已经选择的技能
	public void setSlotPreservingAction(int slot, Item item) {
		setSlotInternal(slot, item, slotActions[slot]);
	}

}