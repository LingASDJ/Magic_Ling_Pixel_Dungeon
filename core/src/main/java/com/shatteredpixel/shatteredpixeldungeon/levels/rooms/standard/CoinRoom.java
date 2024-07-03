package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.PEDESTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.watabou.utils.Point;

public class CoinRoom extends SpecialRoom {

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

    @Override
    public int minWidth() {
        return 17;
    }

    @Override
    public int minHeight() {
        return 17;
    }

    @Override
    public int maxWidth() {
        return 17;
    }

    @Override
    public int maxHeight() {
        return 17;
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );

        Painter.fillEllipse( level, this, 1 , EMPTY_DECO );

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
            if (door.x == left || door.x == right){
                Painter.drawInside(level, this, door, width()/2, WATER);
            } else {
                Painter.drawInside(level, this, door, height()/2, WATER);
            }
        }

        Painter.fillEllipse( level, this, 3, Terrain.CHASM);

        int centerX = left + width() / 2;
        int centerY = top + height() / 2;

        Painter.set(level, centerX, centerY, PEDESTAL);
        Painter.drawRectangle(level, new Point(centerX, centerY),3,3, WATER,false,0);
        Painter.drawRectangle(level, new Point(centerX, centerY),5,5, EMPTY_SP,true,Terrain.CHASM);

        Painter.drawCrossWithOuterFill(level, new Point(centerX, centerY),4, WATER,false,0);

        Painter.drawHorizontalLine(level, new Point(centerX-6, centerY),3,EMPTY_SP);

        Painter.drawHorizontalLine(level, new Point(centerX+3, centerY),3,EMPTY_SP);

        Painter.drawVerticalLine(level, new Point(centerX, centerY+3),3,EMPTY_SP);
        Painter.drawVerticalLine(level, new Point(centerX, centerY-6),3,EMPTY_SP);

        Point e = new Point(centerX, centerY);
        int LXDPos = (left + right) - e.x + e.y * level.width();
        level.drop( new IronKey( Dungeon.depth ),LXDPos).type = Heap.Type.CHEST;

    }
}


