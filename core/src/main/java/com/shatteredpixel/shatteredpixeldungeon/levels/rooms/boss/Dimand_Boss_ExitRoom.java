package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.boss;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;
import static com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter.fill;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;
import com.watabou.utils.Point;

public class Dimand_Boss_ExitRoom extends ExitRoom {

    @Override
    public int minWidth() {
        return 9;
    }

    @Override
    public int minHeight() {
        return 9;
    }

    @Override
    public int maxWidth() {
        return 9;
    }

    @Override
    public int maxHeight() {
        return 9;
    }

    @Override
    public void paint(Level level) {
        fill(level, this, Terrain.WALL);
        fill(level, this,1, EMPTY_DECO);
        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
        }
        Point center = new Point((left + right) / 2, (top + bottom) / 2);
        int centerX = left + width() / 2;
        int centerY = top + height() / 2;



        Painter.drawCircle(level, center, 1, CHASM);
        Painter.drawCircle(level, center, 3, WATER);
        //以中心点的3x3矩形 为裂缝
        Painter.drawRectangle(level, new Point(centerX, centerY), 3, 3, WATER,true,EMPTY_DECO);
        Painter.set(level, centerX, centerY, Terrain.PEDESTAL);


        Painter.drawCrossWithOuterFill(level, new Point(centerX, centerY),4, CHASM,false,0);

        int topPos = (top + 4) * level.width() + left + 4;

        level.transitions.add(new LevelTransition(level,
                topPos,
                LevelTransition.Type.BRANCH_EXIT,
                Dungeon.depth,
                Dungeon.branch-1,
                LevelTransition.Type.BRANCH_ENTRANCE));
    }
}

