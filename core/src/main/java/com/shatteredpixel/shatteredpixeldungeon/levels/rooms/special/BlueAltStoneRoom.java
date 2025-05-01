package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

public class BlueAltStoneRoom extends SpecialRoom{

    private final int width = 5;

    @Override
    public int minWidth() {
        return width;
    }
    @Override
    public int minHeight() {
        return width;
    }
    @Override
    public int maxWidth() {
        return width;
    }
    @Override
    public int maxHeight() {
        return width;
    }

    @Override
    public void paint(Level level) {

        Painter.fill(level,this, 0, WALL);
        Painter.fill(level,this, 1, EMPTY);

        entrance().set(Door.Type.HIDDEN);

        int centerX = left + width() / 2;
        int centerY = top + height() /2;
        Point xpos = new Point(centerX, centerY);
        int RPos = left + right - xpos.x + xpos.y * level.width();

        SkeletonFishRoom.BlueAltStoneDoor ncd = new SkeletonFishRoom.BlueAltStoneDoor();
        ncd.pos = RPos;
        level.mobs.add(ncd);
    }
}
