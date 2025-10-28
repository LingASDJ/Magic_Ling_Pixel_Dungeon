package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.escape;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.allsearch.HelpTeleportPoint;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.CustomLuaRoom;
import com.watabou.utils.Point;

public class MainTowerRoom extends CustomLuaRoom {

    {
        width = 11;
        height = 11;
        map_lua_file = Assets.Map_Luas.FocuRoom_MapLua;
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
        Point cx = new Point(centerX, centerY);
        int exit =  (left + right) - cx.x + cx.y * level.width();
        Mob mob = new HelpTeleportPoint();
        mob.pos = exit;
        level.mobs.add(mob);
    }

}

