package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.AdrenalineDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.BlindingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ChillingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.CleansingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.DisplacingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.HealingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.HolyDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.IncendiaryDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.PoisonDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.RotDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ShockingDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MissileFishRoom extends SpecialRoom {
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
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        return false;
    }

    private Item idToTippedDart(int id){
        switch (id){
            case 0: return new HolyDart();
            case 1: return new ChillingDart();
            case 2: return new AdrenalineDart();
            case 3: return new HealingDart();
            case 4: return new BlindingDart();
            case 5: return new ShockingDart();
            case 6: return new IncendiaryDart();
            case 7: return new DisplacingDart();
            case 8: return new ParalyticDart();
            case 9: return new CleansingDart();
            case 10: return new RotDart();
            case 11: default: return new PoisonDart();
        }
    }


    @Override
    public void paint(Level level) {
        Point center = new Point((left + right) / 2, (top + bottom) / 2);
        Painter.fill( level, this, WALL );
        Painter.fill( level, this,1, EMPTY_SP );
        Painter.fill( level, this,2, WATER );
        //Painter.drawRectangle(level, center, 7, 7, EMPTY_SP,false,CHASM);

        int numFish = 2;

        for (int i=0; i < numFish; i++) {
            Piranha piranha = Piranha.random();
            do {
                piranha.pos = level.pointToCell(random(3));
            } while (level.map[piranha.pos] != Terrain.WATER|| level.findMob( piranha.pos ) != null);
            level.mobs.add( piranha );
        }

        ArrayList<Item> itemss = new ArrayList<>();
        itemss.add(new Gold(Random.NormalIntRange(10* Dungeon.depth/5, 20*Dungeon.depth/5)));
        for (int i=0; i < 2; i++) {
            for (Item item : itemss) {
                int Lpos;
                do {
                    Lpos = level.pointToCell(random(3));
                } while (level.map[Lpos] != WATER || level.heaps.get(Lpos) != null);
                Heap h = level.drop(item, Lpos);
                h.type = Heap.Type.SKELETON;
            }
        }

        ArrayList<Item> mis = new ArrayList<>();
        mis.add(idToTippedDart(Random.NormalIntRange(1,11)));
        for (int i=0; i < Random.NormalIntRange(2,3); i++) {
            for (Item item : mis) {
                int Lpos;
                do {
                    Lpos = level.pointToCell(random(1));
                } while (level.map[Lpos] != EMPTY_SP || level.heaps.get(Lpos) != null);
                Heap h = level.drop(item, Lpos);
                h.type = Heap.Type.HEAP;
            }
        }

        for (Door door : connected.values()) {
            door.set(Door.Type.HIDDEN);
        }

    }

}
