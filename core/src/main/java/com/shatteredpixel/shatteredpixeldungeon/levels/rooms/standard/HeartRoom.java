package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PinkGhost;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.CustomLuaRoom;

public class HeartRoom extends CustomLuaRoom {

    {
        width = 17;
        height = 17;
        // *通过Lua 脚本渲染房间
        map_lua_file = Assets.Map_Luas.PinkGhostRoom_MapLua;
    }

    @Override
    public int maxConnections(int direction) {
        return 1;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        int MiddlePos = (top + 8) * level.width() + left + 8;

        Mob n = new PinkGhost();
        n.pos = MiddlePos;
        level.mobs.add(n);
    }
}