package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.HIGH_GRASS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.SkyDead;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;

public class SkyShadowBossRoom extends StandardRoom {

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
    public boolean canPlaceWater(Point p) {
        return false;
    }
    @Override
    public void paint(Level level) {
        Painter.fill( level, this, WALL );

        Painter.fillEllipse( level, this, 1 , EMPTY);

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
            if (door.x == left || door.x == right){
                Painter.drawInside(level, this, door, width()/2, WATER);
            } else {
                Painter.drawInside(level, this, door, height()/2,  HIGH_GRASS);
            }
        }

        int centerX = left + width() / 2;
        int centerY = top + height() / 2;

        Point e = new Point(centerX, centerY);

        Painter.drawCircle(level, e, 1, Terrain.EMBERS);
        Painter.drawCircle(level, e, 2, Terrain.HIGH_GRASS);
        Painter.set(level, e, Terrain.WATER);

        int LXDPos = (left + right) - e.x + e.y * level.width();

        SkyDead n = new SkyDead();
        n.pos = LXDPos;
        level.mobs.add(n);
    }


    @Override
    public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
        return false;
    }

}


