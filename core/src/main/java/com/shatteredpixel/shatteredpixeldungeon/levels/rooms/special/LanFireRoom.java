package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.LanFire;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;

public class LanFireRoom extends SpecialRoom {

    public int minWidth() {
        return 5;
    }

    public int minHeight() {
        return 5;
    }

    public void paint(Level level) {
        Painter.fill(level, this, 4);
        Painter.fill(level, this, 1, 14);

        placeLanFire(level);

        for (Room.Door door : this.connected.values()) {
            door.set(Door.Type.REGULAR);
        }
        Door entrance = entrance();
        entrance.set( Door.Type.REGULAR );

    }

    protected void placeLanFire(Level level) {
        int pos = level.pointToCell(center());
        Mob lanFireNPC = new LanFire();
        lanFireNPC.pos = pos;
        level.mobs.add(lanFireNPC);
    }
}


