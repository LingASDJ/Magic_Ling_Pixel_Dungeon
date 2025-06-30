package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.CerbusSleep;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class CerbusSleepRoom extends SpecialRoom {

    @Override
    public int minWidth() { return 7; }
    public int minHeight() { return 7; }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.CHASM );

        int pos = level.pointToCell(center());

        CerbusSleep cerbusSleep = new CerbusSleep();
        cerbusSleep.pos = pos;
        level.mobs.add(cerbusSleep);


        Point c = center();
        Door door = entrance();
        if (door.x == left || door.x == right) {
            if (door.y == c.y) c.y += Random.Int(2) == 0 ? -1 : +1;
            Point p = Painter.drawInside( level, this, door, Math.abs( door.x - c.x ) - 2, Terrain.EMPTY_SP );
            for (; p.y != c.y; p.y += p.y < c.y ? +1 : -1) {
                Painter.set( level, p, Terrain.EMPTY_SP );
            }
        } else {
            if (door.x == c.x) c.x += Random.Int(2) == 0 ? -1 : +1;
            Point p = Painter.drawInside( level, this, door, Math.abs( door.y - c.y ) - 2, Terrain.EMPTY_SP );
            for (; p.x != c.x; p.x += p.x < c.x ? +1 : -1) {
                Painter.set( level, p, Terrain.EMPTY_SP );
            }
        }

        //we add four statues to give some cover from ranged enemies
        Point statue = new Point(c);
        statue.x -= 2;
        if (statue.x > left) Painter.set( level, statue, Terrain.WALL_DECO );
        statue.x += 2; statue.y -= 2;
        if (statue.y > top) Painter.set( level, statue,  Terrain.WALL_DECO );
        statue.y += 2; statue.x += 2;
        if (statue.x < right) Painter.set( level, statue, Terrain.WALL_DECO );
        statue.x -= 2; statue.y += 2;
        if (statue.y < bottom) Painter.set( level, statue,  Terrain.WALL_DECO );

        Painter.fill( level, c.x - 1, c.y - 1, 3, 3, Terrain.EMBERS );
        Painter.set( level, c, Terrain.EMPTY_SP );

        door.set( Door.Type.REGULAR );
    }

}
