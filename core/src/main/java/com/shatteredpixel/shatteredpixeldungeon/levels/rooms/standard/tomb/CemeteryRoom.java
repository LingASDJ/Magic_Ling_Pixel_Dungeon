package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.tomb;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.GRASS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;

public class CemeteryRoom extends StandardRoom {

    @Override
    public int minWidth() { return 9; }
    @Override
    public int minHeight() {
        return 9; }
    @Override
    public int maxWidth() { return 9; }
    @Override
    public int maxHeight() {return 9; }

    @Override
    public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
        return false;
    }

    @Override
    public void paint(Level level) {
        int centerX = left + width() / 2;
        int centerY = top + height() / 2;


        Painter.fill(level,this,1, EMPTY);


        Painter.drawRectangle(level, new Point(centerX, centerY),3,3, GRASS,true, WATER);
        Painter.set(level, centerX, centerY, EMPTY);
        Painter.drawRectangle(level, new Point(centerX, centerY),5,5, EMPTY,false, WATER);
        for (Room.Door door : connected.values()) {
            door.set( Room.Door.Type.REGULAR );
        }
    }
}
