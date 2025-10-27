package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.SliceAlter;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Point;

public class SliceDeadRoom extends CustomLuaRoom {

    {
        width = 17;
        height = 17;
        map_lua_file = Assets.Map_Luas.SliceDeadRoom_MapLua;
    }

    @Override
    public int maxConnections(int direction) {
        return 1;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);
        int centerX = left + width() / 2;
        int centerY = top + height() / 2;
        Point pos = new Point(centerX, centerY);
        int centerPos = left + right - pos.x + pos.y * level.width();

        SliceAlter sliceAlter = new SliceAlter();
        sliceAlter.pos = centerPos;
        level.mobs.add(sliceAlter);
    }

}
