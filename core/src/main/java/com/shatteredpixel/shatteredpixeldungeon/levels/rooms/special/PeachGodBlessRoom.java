package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.PeachGodState;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;

public class PeachGodBlessRoom extends SpecialRoom {

    @Override
    public int minWidth() {
        return 11;
    }

    @Override
    public int minHeight() {
        return 11;
    }

    @Override
    public int maxWidth() {
        return 11;
    }

    @Override
    public int maxHeight() {
        return 11;
    }

    @Override
    public boolean canPlaceTrap(Point p){
        return false;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY_SP);

        int centerX = left + width() / 2;
        int centerY = top + height() / 2;

        //绘制横线不可通过的地方
        Painter.drawHorizontalLine(level, new Point(centerX - 1, centerY - 3), 1, SIGN);

        Painter.drawHorizontalLine(level, new Point(centerX - 2, centerY - 2), 2, SIGN);
        Painter.drawHorizontalLine(level, new Point(centerX - 2, centerY - 1), 2, SIGN);
        Painter.drawHorizontalLine(level, new Point(centerX - 2, centerY ), 2, SIGN);
        Painter.drawHorizontalLine(level, new Point(centerX - 2, centerY + 1), 1, SIGN);

        //绘制竖线不可通过的地方
        Painter.drawVerticalLine(level, new Point(centerX + 1, centerY - 3), 4, SIGN);
        Painter.drawVerticalLine(level, new Point(centerX + 2, centerY - 1), 2, SIGN);

        //Painter.drawHorizontalLine(level, new Point(centerX - 1, centerY - 4), 2, SIGN);

        for (Room.Door door : this.connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }

        SpringTilemap vis = new SpringTilemap();
        Point c = center();
        vis.pos(c.x - 4, c.y - 4);
        level.customTiles.add(vis);

        Mob n = new PeachGodState();
        n.pos = (top + 6) * level.width() + left + 5;
        level.mobs.add(n);


    }

    public static class SpringTilemap extends CustomTilemap {

        {
            texture = Assets.Environment.ALTAR_SPRING;
            tileW = tileH = 9;

        }

        final int TEX_WIDTH = 144;

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(mapSimpleImage(0, 0, TEX_WIDTH), 9);
            return v;
        }

        @Override
        public String name(int tileX, int tileY) {
            return Messages.get(this, "name");
        }

        @Override
        public String desc(int tileX, int tileY) {
            return Messages.get(this, "desc");
        }
    }

}
