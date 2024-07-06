package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;

public class GardenExitRoom extends ExitRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 5);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 5);
    }

    public void paint(Level level) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        for (Room.Door door : connected.values()) {
            door.set( Room.Door.Type.REGULAR );
        }

        int exit = level.pointToCell(random( 2 ));
        Painter.set( level, exit, Terrain.EMPTY );
        level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
    }

}
