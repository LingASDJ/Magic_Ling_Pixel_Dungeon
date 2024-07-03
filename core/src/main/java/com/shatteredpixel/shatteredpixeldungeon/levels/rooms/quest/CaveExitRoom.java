package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;

public class CaveExitRoom extends ExitRoom {

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
        Painter.fill( level, this , Terrain.WALL_DECO );
        Painter.fill( level, this, 1, Terrain.WATER );

        int topPos = (top + 5) * level.width() + left + 4;
        Painter.set( level, topPos, Terrain.EXIT );

        level.transitions.add(new LevelTransition(level,
                topPos,
                LevelTransition.Type.BRANCH_EXIT,
                Dungeon.depth,
                Dungeon.branch+1,
                LevelTransition.Type.BRANCH_ENTRANCE));
    }
}

