package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.CerbusSleep;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class CerbusSleepRoom extends SpecialRoom {

    public int width = 7;
    public int height = 7;

    @Override
    public int minWidth() {
        return width;
    }
    @Override
    public int minHeight() {
        return height;
    }
    @Override
    public int maxWidth() {
        return width;
    }
    @Override
    public int maxHeight() {
        return height;
    }

    @Override
    public void paint(Level level) {
        Point c = center();
        Painter.fill( level, this, Terrain.WALL );
        Painter.drawCircle(level, c, 1, EMPTY_SP);
        Painter.drawCircle(level, c, 2, WATER);

        int pos = level.pointToCell(center());

        CerbusSleep cerbusSleep = new CerbusSleep();
        cerbusSleep.pos = pos;
        level.mobs.add(cerbusSleep);
        Door door = entrance();
        if (door.x == left || door.x == right) {
            if (door.y == c.y) c.y += Random.Int(2) == 0 ? -1 : +1;
            Point p = Painter.drawInside( level, this, door, Math.abs( door.x - c.x ) - 2, Terrain.EMPTY_SP );
            for (; p.y != c.y; p.y += p.y < c.y ? +1 : -1) {
                Painter.set( level, p, WATER );
            }
        } else {
            if (door.x == c.x) c.x += Random.Int(2) == 0 ? -1 : +1;
            Point p = Painter.drawInside( level, this, door, Math.abs( door.y - c.y ) - 2, Terrain.EMPTY_SP );
            for (; p.x != c.x; p.x += p.x < c.x ? +1 : -1) {
                Painter.set( level, p, WATER );
            }
        }

        Painter.set( level, c, Terrain.PEDESTAL );

        door.set( Door.Type.REGULAR );
    }

}
