package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.watabou.utils.Point;

public class AncientMysteryRoom extends EntranceRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 7);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 7);
    }

    @Override
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        //StandardRoom.canMerge
        int cell = l.pointToCell(pointInside(p, 1));
        return (Terrain.flags[l.map[cell]] & Terrain.SOLID) == 0;
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        int entrance;
        do {
            entrance = level.pointToCell(random(3));
        } while (level.findMob(entrance) != null || level.map[entrance] == Terrain.WALL);
        Painter.set( level, entrance, Terrain.ENTRANCE );

        level.transitions.add(new LevelTransition(level,
                entrance,
                LevelTransition.Type.BRANCH_ENTRANCE,
                Dungeon.depth,
                Dungeon.branch+2,
                LevelTransition.Type.BRANCH_EXIT));
    }
}

