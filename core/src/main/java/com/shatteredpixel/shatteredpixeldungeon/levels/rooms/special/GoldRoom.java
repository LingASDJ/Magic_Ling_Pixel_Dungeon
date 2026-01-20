package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class GoldRoom extends SpecialRoom {
    @Override
    public int minWidth() {
        return 5;
    }

    @Override
    public int minHeight() {
        return 5;
    }

    @Override
    public int maxWidth() {
        return 5;
    }

    @Override
    public int maxHeight() {
        return 5;
    }

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

    @Override
    public void paint(Level level) {

        Painter.fill( level, this, Terrain.WALL );

        Painter.fillEllipse( level, this, 1 , Terrain.EMPTY_SP );

        int centerX = left + width() / 2;
        int centerY = top + height() / 2;
        Painter.set(level, centerX, centerY, Terrain.PEDESTAL);
        Point pos = new Point(centerX, centerY);
        Point Lpos = new Point(centerX-1, centerY);
        Point Rpos = new Point(centerX+1, centerY);

        int goldCenter = left + right - pos.x + pos.y * level.width();
        level.drop( new Gold().quantity(Random.Int(250* Dungeon.depth/5,351 * Dungeon.depth/5)), goldCenter );

        int l = left + right - Lpos.x + Lpos.y * level.width();
        int r = left + right - Rpos.x + Rpos.y * level.width();


            Item generator = Generator.random();
            if(generator instanceof Weapon){
                generator.level=Random.Int(0,2);
                generator.cursed = false;
            } else if(generator instanceof Wand){
                generator.level=Random.Int(2);
                generator.cursed = false;
                generator.upgrade();
            } else if(generator instanceof Armor){
                generator.cursed = false;
                generator.level=Random.Int(1,4);
            }

            Item generator2 = Generator.random();
            if(generator2 instanceof Weapon){
                generator.level=Random.Int(0,2);
                generator2.cursed = false;
            } else if(generator2 instanceof Wand){
                generator2.level=Random.Int(2);
                generator2.cursed = false;
                generator2.upgrade();
            } else if(generator2 instanceof Armor){
                generator2.cursed = false;
                generator2.level=Random.Int(1,4);
            }

            level.drop(generator, l ).type = Heap.Type.CHEST;
            level.drop(generator2, r ).type = Heap.Type.CHEST;

        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }
    }

}
