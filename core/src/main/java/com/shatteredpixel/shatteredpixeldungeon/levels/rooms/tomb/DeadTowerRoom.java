package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.tomb;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.CustomLuaRoom;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeadTowerSprite;
import com.watabou.utils.Point;

public class DeadTowerRoom extends CustomLuaRoom.FullLuaCustomRoom {

    {
        width = 17;
        height = 17;
        map_lua_file = Assets.Map_Luas.Tomb_DeathTower_MapLua;
    }

    public static class DeadTower extends Mob {
        {
            spriteClass = DeadTowerSprite.class;
            HT = HP = 150;
            properties.add(Property.IMMOVABLE);
            properties.add(Property.TUMULUS);
            properties.add(Property.INORGANIC);
        }

        @Override
        protected boolean act() {
            alerted = false;
            state = PASSIVE;
            return super.act();
        }
    }

    protected void placeDeathSpire( Level level ) {
        int deadPos = (top + 8) * level.width() + left + 8;
        Mob n = new DeadTower();
        n.pos = deadPos;
        level.mobs.add(n);
    }

    @Override
    public void paint(Level level) {
        super.paint(level);
        placeDeathSpire(level);
    }

    @Override
    public boolean canConnect(Point p) {
        int midX = left + 8;
        int midY = top + 8;
        if (p.x == midX && p.y == top) return true;
        if (p.x == midX && p.y == bottom) return true;
        if (p.x == left && p.y == midY) return true;
        if (p.x == right && p.y == midY) return true;
        return false;
    }

}
