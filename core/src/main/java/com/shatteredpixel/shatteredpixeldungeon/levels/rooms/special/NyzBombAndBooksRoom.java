//
// Decompiled by Jadx - 756ms
//
package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nyz;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.RegrowthBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.GrassKingBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class NyzBombAndBooksRoom extends SpecialRoom {
    private ArrayList<Item> itemsToSpawn;

    public NyzBombAndBooksRoom() {
    }

    public int minWidth() {
        return Math.max(4, (int) (Math.sqrt(itemCount()) + 3.0d));
    }

    public int minHeight() {
        return Math.max(4, (int) (Math.sqrt(itemCount()) + 3.0d));
    }

    public int itemCount() {
        if (this.itemsToSpawn == null) {
            this.itemsToSpawn = generateItems();
        }
        return this.itemsToSpawn.size();
    }

    public void paint(Level level) {
        Painter.fill(level, this, 4);
        Painter.fill(level, this, 1, 14);
        placeShopkeeper(level);
        placeItems(level);
        for (Room.Door door : this.connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }
    }

    protected void placeShopkeeper(Level level) {
        int pos = level.pointToCell(center());
        Mob nxhy = new Nyz();
        nxhy.pos = pos;
        level.mobs.add(nxhy);
    }

    protected void placeItems(Level level) {
        if (this.itemsToSpawn == null) {
            this.itemsToSpawn = generateItems();
        }
        Point itemPlacement = new Point(entrance());
        if (itemPlacement.y == this.top) {
            itemPlacement.y++;
        } else if (itemPlacement.y == this.bottom) {
            itemPlacement.y--;
        } else if (itemPlacement.x == this.left) {
            itemPlacement.x++;
        } else {
            itemPlacement.x--;
        }
        Iterator<Item> it = this.itemsToSpawn.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (itemPlacement.x == this.left + 1 && itemPlacement.y != this.top + 1) {
                itemPlacement.y--;
            } else if (itemPlacement.y == this.top + 1 && itemPlacement.x != this.right - 1) {
                itemPlacement.x++;
            } else if (itemPlacement.x != this.right - 1 || itemPlacement.y == this.bottom - 1) {
                itemPlacement.x--;
            } else {
                itemPlacement.y++;
            }
            int cell = level.pointToCell(itemPlacement);
            if (level.heaps.get(cell) != null) {
                while (true) {
                    cell = level.pointToCell(random());
                    if (level.heaps.get(cell) == null && level.findMob(cell) == null) {
                        break;
                    }
                }
            }
            level.drop( item, cell ).type = Heap.Type.FOR_SALE;
        }
    }

    protected static ArrayList<Item> generateItems() {
        Item rare;
        ArrayList<Item> itemsToSpawn2 = new ArrayList<>();
        Item w = Generator.random(Generator.wepTiers[3]);
        //itemsToSpawn2.add(Generator.random(Generator.misTiers[4]).quantity(2).identify());
        itemsToSpawn2.add(new LeatherArmor().identify());
        w.cursed = false;
        w.level(Random.Int(2,3));
        w.identify();
        itemsToSpawn2.add(w);
        itemsToSpawn2.add (new Flashbang().quantity(1));
        itemsToSpawn2.add (new Flashbang().quantity(1));
        itemsToSpawn2.add (new Noisemaker().quantity(1));
        itemsToSpawn2.add (new RegrowthBomb().quantity(1));
        itemsToSpawn2.add (new HolyBomb().quantity(1));
        itemsToSpawn2.add (new Firebomb().quantity(1));
        itemsToSpawn2.add (new FrostBomb().quantity(1));
        //itemsToSpawn2.add(new MerchantsBeacon());
        itemsToSpawn2.add(new GrassKingBooks().quantity(1));
        itemsToSpawn2.add(new ScrollOfTransmutation());
        //itemsToSpawn2.add(new DriedRose());
        itemsToSpawn2.add(new ScrollOfMagicMapping());
        //itemsToSpawn2.add(new BookBag());
        int Int = Random.Int(4);
        if (Int == 0) {
            itemsToSpawn2.add(new Bomb());
        } else if (Int == 1 || Int == 2) {
            itemsToSpawn2.add(new Bomb.DoubleBomb());
        } else if (Int == 3) {
            itemsToSpawn2.add(new Honeypot());
        }
        itemsToSpawn2.add(new StoneOfAugmentation());
        int Int2 = Random.Int(10);
        if (Int2 == 0) {
            rare = Generator.random(Generator.Category.WAND);
            rare.level(0);
        } else if (Int2 == 1) {
            rare = Generator.random(Generator.Category.SEED);
            rare.level(0);
        } else {
            rare = Generator.random(Generator.Category.POTION);
        }
        rare.cursed = false;
        rare.cursedKnown = true;
        itemsToSpawn2.add(rare);
        Random.shuffle(itemsToSpawn2);
        return itemsToSpawn2;
    }


}
