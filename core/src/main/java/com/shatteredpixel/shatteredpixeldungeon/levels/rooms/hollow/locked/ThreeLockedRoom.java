package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.locked;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.CustomLuaRoom;

public class ThreeLockedRoom extends CustomLuaRoom {
    {
        width = 9;
        height = 9;
        map_lua_file = Assets.Map_Luas.LockedThreeRoom_MapLua;
    }
}
