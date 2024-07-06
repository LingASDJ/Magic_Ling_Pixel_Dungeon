package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;

import java.util.ArrayList;

public class DoubleBeeRoom extends SpecialRoom {
    @Override
    public int minWidth() {
        return 9;
    }

    @Override
    public int minHeight() {
        return 9;
    }

    @Override
    public int maxWidth() {
        return 9;
    }

    @Override
    public int maxHeight() {
        return 9;
    }

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}
    @Override
    public void paint(Level level) {
        Point center = new Point((left + right) / 2, (top + bottom) / 2);

        Painter.fill(level, this, WALL);
        Painter.fill(level, this, 1, WATER);
        Painter.fill(level, this, 2, CHASM);
        Painter.fill(level, this, 3, EMPTY_SP);
        Painter.set(level,center, Terrain.BARRICADE);

        ArrayList<Item> itemss = new ArrayList<>();
        itemss.add(Generator.randomMissile());
        for (int i=0; i < 1; i++) {
            for (Item item : itemss) {
                int Lpos;
                do {
                    Lpos = level.pointToCell(random());
                } while (level.map[Lpos] != WATER || level.heaps.get(Lpos) != null);
                Heap h = level.drop(item, Lpos);
                h.type = Heap.Type.HEAP;
            }
        }
        level.addItemToSpawn( new PotionOfLevitation());
        ArrayList<Item> mis = new ArrayList<>();
        mis.add(Generator.random(Generator.Category.SEED));
        for (int i=0; i < 2; i++) {
            for (Item item : mis) {
                int Lpos;
                do {
                    Lpos = level.pointToCell(random(1));
                } while (level.map[Lpos] != EMPTY_SP || level.heaps.get(Lpos) != null);
                Heap h = level.drop(item, Lpos);
                h.type = Heap.Type.HEAP;
            }
        }

        ArrayList<Item> misx = new ArrayList<>();
        misx.add(new Blandfruit());
        for (int i=0; i < 1; i++) {
            for (Item item : misx) {
                int Lpos;
                do {
                    Lpos = level.pointToCell(random(1));
                } while (level.map[Lpos] != EMPTY_SP || level.heaps.get(Lpos) != null);
                Heap h = level.drop(item, Lpos);
                h.type = Heap.Type.HEAP;
            }
        }

        ArrayList<Item> misxs = new ArrayList<>();
        misxs.add(new Honeypot.ShatteredPot());
        for (int i=0; i < 1; i++) {
            for (Item item : misxs) {
                int Lpos;
                do {
                    Lpos = level.pointToCell(random(1));
                } while (level.map[Lpos] != EMPTY_SP || level.heaps.get(Lpos) != null);
                Heap h = level.drop(item, Lpos);
                h.type = Heap.Type.HEAP;

                Bee bee = new Bee();
                bee.spawn( Dungeon.depth );
                bee.HP = bee.HT;
                bee.pos = Lpos;
                level.mobs.add( bee );
                bee.state = bee.SLEEPING;
                bee.setPotInfo(Lpos, null);
            }
        }

        ArrayList<Item> misxsx = new ArrayList<>();
        misxsx.add(new Honeypot.ShatteredPot());
        for (int i=0; i < 1; i++) {
            for (Item item : misxsx) {
                int Lpos;
                do {
                    Lpos = level.pointToCell(random(1));
                } while (level.map[Lpos] != WATER || level.heaps.get(Lpos) != null);
                Heap h = level.drop(item, Lpos);
                h.type = Heap.Type.HEAP;

                Bee bee = new Bee();
                bee.spawn( Dungeon.depth );
                bee.HP = bee.HT;
                bee.pos = Lpos;
                bee.state = bee.SLEEPING;
                level.mobs.add( bee );
                bee.setPotInfo(Lpos, null);
            }
        }

        for (Door door : connected.values()) {
            door.set(Door.Type.HIDDEN);
        }

    }
}
