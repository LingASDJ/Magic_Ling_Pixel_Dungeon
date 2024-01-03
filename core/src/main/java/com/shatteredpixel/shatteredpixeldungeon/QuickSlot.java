package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;

public class QuickSlot {
	public static int SIZE = 12;

	public static int VSIZE = 9;
	public int quickslots;
	/**
	 * Slots contain objects which are also in a player's inventory. The one exception to this is when quantity is 0,
	 * which can happen for a stackable item that has been 'used up', these are referred to as placeholders.
	 */

	//note that the current max size is coded at 6, due to UI constraints, but it could be much much bigger with no issue.

	private Item[] slots = new Item[SIZE];


	//direct array interaction methods, everything should build from these methods.
	public void setSlot(int slot, Item item){
		clearItem(item); //we don't want to allow the same item in multiple slots.
		slots[slot] = item;
	}

	public void clearSlot(int slot){
		slots[slot] = null;
	}

	public void reset(){
		slots = new Item[SIZE];
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

	public void clearItem(Item item){
		if (contains(item)) {
			clearSlot(getSlot(item));
		}
	}

	public boolean contains(Item item){
		return getSlot(item) != -1;
	}

	public void replacePlaceholder(Item item) {
		for (int i = 0; i < SIZE; i++) {
			if (isPlaceholder(i) && item.isSimilar(getItem(i))) {
				setSlot(i, item);
			}
		}
	}

	public void convertToPlaceholder(Item item){

		if (contains(item)) {
			Item placeholder = item.virtual();
			if (placeholder == null) return;

			for (int i = 0; i < SIZE; i++) {
				if (getItem(i) == item) setSlot(i, placeholder);
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

	private final String PLACEHOLDERS = "placeholders";
	private final String PLACEMENTS = "placements";

	/**
	 * Placements array is used as order is preserved while bundling, but exact index is not, so if we
	 * bundle both the placeholders (which preserves their order) and an array telling us where the placeholders are,
	 * we can reconstruct them perfectly.
	 */

	public void storePlaceholders(Bundle bundle){
		ArrayList<Item> placeholders = new ArrayList<>(SIZE);
		boolean[] placements = new boolean[SIZE];

		for (int i = 0; i < SIZE; i++) {
			if (isPlaceholder(i)) {
				placeholders.add(getItem(i));
				placements[i] = true;
			}
		}
		bundle.put( PLACEHOLDERS, placeholders );
		bundle.put( PLACEMENTS, placements );
	}

	public void restorePlaceholders(Bundle bundle){
		Collection<Bundlable> placeholders = bundle.getCollection(PLACEHOLDERS);
		boolean[] placements = bundle.getBooleanArray( PLACEMENTS );

		int i = 0;
		for (Bundlable item : placeholders){
			while (!placements[i]){
				i++;
			}
			setSlot( i, (Item)item );
			i++;
		}

	}

}