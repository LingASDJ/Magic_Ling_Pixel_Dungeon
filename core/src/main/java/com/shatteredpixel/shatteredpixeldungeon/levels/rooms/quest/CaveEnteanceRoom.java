package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.watabou.utils.Point;

public class CaveEnteanceRoom extends EntranceRoom {

    @Override
    public int minWidth() {
        return 7;
    }

    @Override
    public int minHeight() {
        return 7;
    }

    @Override
    public int maxWidth() {
        return 7;
    }

    @Override
    public int maxHeight() {
        return 7;
    }
    @Override
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        //StandardRoom.canMerge
        int cell = l.pointToCell(pointInside(p, 1));
        return (Terrain.flags[l.map[cell]] & Terrain.SOLID) == 0;
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL_DECO );
        Painter.fill( level, this, 1, Terrain.WATER );

        int topPos = (top + 5) * level.width() + left + 4;
        Painter.set( level, topPos, Terrain.ENTRANCE );

        level.transitions.add(new LevelTransition(level,
                topPos,
                LevelTransition.Type.BRANCH_ENTRANCE,
                Dungeon.depth,
                Dungeon.branch-1,
                LevelTransition.Type.BRANCH_EXIT));
    }
}



