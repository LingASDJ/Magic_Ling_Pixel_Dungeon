package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.PEDESTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SECRET_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;

public class SkeletonFishRoom extends SpecialRoom {
    @Override
    public int minWidth() {
        return 13;
    }
    @Override
    public int minHeight() {
        return 13;
    }
    @Override
    public int maxWidth() {
        return 13;
    }
    @Override
    public int maxHeight() {
        return 13;
    }
	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}
    @Override
    public boolean canConnect(Point p) {
        if (!super.canConnect(p)){
            return false;
        }
        //only place doors in the center
        if (Math.abs(p.x - (right - (width()-1)/4f)) < 1f){
            return true;
        }
        return Math.abs(p.y - (bottom - (height() - 1) / 4f)) < 1f;
    }


    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }

    @Override
    public void paint(Level level) {
        Point center = new Point((left + right) / 2, (top + bottom) / 2);

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
            if (door.x == left || door.x == right){
                Painter.drawInside(level, this, door, width()/2, EMPTY);
            } else {
                Painter.drawInside(level, this, door, height()/2, EMPTY);
            }
        }

        int centerX = left + width() / 2;
        int centerY = top + height() / 2;

        Painter.fill(level, this, WALL);
        Painter.fill(level, this, 1, EMPTY);

        Painter.set(level, centerX - 5, centerY - 5, CHASM);
        Painter.set(level, centerX - 5, centerY - 4, CHASM);
        Painter.set(level, centerX - 4, centerY - 5, CHASM);

        Painter.set(level, centerX - 5, centerY, CHASM);

        Painter.drawVerticalLine(level, new Point(centerX - 4, centerY - 3), 3, CHASM);
        Painter.drawHorizontalLine(level, new Point(centerX - 3, centerY - 4), 3, CHASM);

        Painter.set(level, centerX, centerY - 5, CHASM);
        Painter.set(level, centerX + 1, centerY - 5, PEDESTAL);
        Painter.set(level, centerX + 2, centerY - 5, SECRET_DOOR);
        Painter.set(level, centerX + 2, centerY - 4, WALL);
        Painter.drawHorizontalLine(level, new Point(centerX + 3, centerY - 3), 3, WALL);
        Painter.set(level, centerX + 5, centerY - 2, PEDESTAL);
        Painter.set(level, centerX + 5, centerY - 1, CHASM);
        Painter.set(level, centerX + 4, centerY - 1, WALL);

        Painter.drawHorizontalLine(level, new Point(centerX + 1, centerY), 1, WALL);
        Painter.drawHorizontalLine(level, new Point(centerX + 1, centerY + 1), 3, WALL);
        Painter.drawHorizontalLine(level, new Point(centerX + 2, centerY + 2), 1, WALL);
        Painter.drawHorizontalLine(level, new Point(centerX + 2, centerY + 3), 0, WALL);

        Painter.set(level, centerX, centerY - 1, WALL);
        Painter.set(level, centerX, centerY + 3, WALL);

        Painter.set(level, centerX-1, centerY + 4, PEDESTAL);
        Painter.set(level, centerX-2, centerY + 4, CHASM);
        Painter.set(level, centerX-2, centerY + 3, WALL);

        Painter.set(level, centerX-3, centerY + 2, WALL_DECO);
        Painter.set(level, centerX-3, centerY + 3, CHASM);
        Painter.set(level, centerX-3, centerY + 4, CHASM);
        Painter.set(level, centerX-4, centerY + 2, SECRET_DOOR);
        Painter.set(level, centerX-4, centerY + 1, PEDESTAL);
        Painter.set(level, centerX-5, centerY + 2, CHASM);

        Painter.set(level, centerX-2, centerY + 1, STATUE);
        Painter.set(level, centerX-2, centerY, STATUE);

        Painter.set(level, centerX, centerY - 3, STATUE);
        Painter.set(level, centerX+1, centerY - 3, STATUE);

        Painter.set(level, centerX+3, centerY - 4, CHASM);
        Painter.set(level, centerX+4, centerY - 4, CHASM);

        Painter.drawHorizontalLine(level, new Point(centerX - 5, centerY + 5), 10, WATER);

        Painter.set(level, centerX, centerY + 5, WALL_DECO);
        Painter.set(level, centerX-1, centerY + 5, CHASM);
        Painter.set(level, centerX-2, centerY + 5, WALL_DECO);
    }
}
