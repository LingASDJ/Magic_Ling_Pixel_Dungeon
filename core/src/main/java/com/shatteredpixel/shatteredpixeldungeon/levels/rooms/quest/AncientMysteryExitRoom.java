package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;
import com.watabou.utils.Point;

public class AncientMysteryExitRoom extends ExitRoom {

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
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        //StandardRoom.canMerge
        int cell = l.pointToCell(pointInside(p, 1));
        return (Terrain.flags[l.map[cell]] & Terrain.SOLID) == 0;
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL_DECO );
        Painter.fill( level, this, 1, Terrain.WATER );

        for (Door door : connected.values()) {
            door.set( Door.Type.HIDDEN );
        }

        Point c = center();

        if(Dungeon.branch != 3){
            Painter.fill( level, c.x-1, c.y-1, 3, 4, Terrain.WALL_DECO );
            Painter.fill( level, c.x-1, c.y+1, 3, 1, Terrain.WALL );
            Painter.set ( level,c.x, c.y+1, Terrain.EMPTY_SP);
            Painter.set ( level,c.x, c.y+2, Terrain.DOOR);
        }

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

