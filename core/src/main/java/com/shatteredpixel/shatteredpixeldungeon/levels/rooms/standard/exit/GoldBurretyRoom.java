package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.PinkFox;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.watabou.utils.Point;

public class GoldBurretyRoom extends SpecialRoom {
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
    public void paint(Level level) {
        Point center = new Point((left + right) / 2, (top + bottom) / 2);

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY_SP );

        int pos = level.pointToCell(center());
        PinkFox npc18 = new PinkFox();
        npc18.pos = pos;
        level.mobs.add(npc18);

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
        }

    }

}
