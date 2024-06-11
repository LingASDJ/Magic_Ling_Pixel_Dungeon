package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.dragon.PirahaKing;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.watabou.utils.Point;

public class BigFishBossRoom extends SpecialRoom {

    @Override
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        return false;
    }

    @Override
    public int minWidth() {
        return 27;
    }

    @Override
    public int minHeight() {
        return 27;
    }

    @Override
    public int maxWidth() {
        return 27;
    }

    @Override
    public int maxHeight() {
        return 27;
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

        Painter.fillEllipse( level, this, 3, WATER);

        int centerX = left + width() / 2;
        int centerY = top + height() / 2;

        Painter.set(level, centerX, centerY, WATER);
        Painter.drawRectangle(level, new Point(centerX, centerY),3,3, WATER,true, WATER);
        Painter.drawRectangle(level, new Point(centerX, centerY),5,5, EMPTY_SP,true, STATUE_SP);

        Painter.drawRectangle(level, new Point(centerX, centerY),6,6, WATER,false,0);
        Painter.drawRectangle(level, new Point(centerX, centerY),10,11, EMPTY_SP,true, STATUE);

        Painter.drawCrossWithOuterFill(level, new Point(centerX, centerY),4, WATER,false,0);

        Painter.drawHorizontalLine(level, new Point(centerX-10, centerY),7,EMPTY_SP);

        Painter.drawHorizontalLine(level, new Point(centerX+3, centerY),7,EMPTY_SP);

        Painter.drawVerticalLine(level, new Point(centerX, centerY+3),7,EMPTY_SP);
        Painter.drawVerticalLine(level, new Point(centerX, centerY-10),7,EMPTY_SP);

        Point e = new Point(centerX, centerY);
        int LXDPos = (left + right) - e.x + e.y * level.width();

        PirahaKing n = new PirahaKing();
        n.pos = LXDPos;
        level.mobs.add(n);

    }
}



