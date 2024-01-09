package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter.drawVerticalLine;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

public class RangeMobRoom extends SpecialRoom {
    @Override
    public void paint(Level level) {


        for (Door door : connected.values()) {
            door.set(Door.Type.HIDDEN);
        }

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1,Terrain.EMPTY);
        Painter.fill(level, this, 2,Terrain.WALL);
        Painter.fill(level, this, 3,Terrain.EMPTY);

        int centerX = left + width() / 2;
        int centerY = top + height() / 2;

        Painter.set(level, centerX+5,centerY+4, Terrain.DOOR);

        drawVerticalLine(level, new Point(centerX+4,centerY-4), 7, Terrain.WALL);
        drawVerticalLine(level, new Point(centerX+3,centerY-4), 5, Terrain.WALL);
    }

    @Override
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        return false;
    }

    @Override
    public Rect resize(int w, int h) {
        super.resize(w, h);
        return this;
    }

    @Override
    public int minWidth() {
        return 15;
    }

    @Override
    public int minHeight() {
        return 15;
    }

    @Override
    public int maxWidth() {
        return 15;
    }

    @Override
    public int maxHeight() {
        return 15;
    }

}
