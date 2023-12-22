package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMBERS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.FURROWED_GRASS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DragonGirlBlue;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;

public class DreamcatcherRoom extends SpecialRoom {

    @Override
    public int minWidth() {
        return 13;
    }

    @Override
    public int minHeight() {
        return 13;
    }

    @Override
    public int maxWidth() {
        return 13;
    }

    @Override
    public int maxHeight() {
        return 13;
    }

    @Override
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        return false;
    }

    @Override
    public void paint(Level level) {
        Point center = new Point((left + right) / 2, (top + bottom) / 2);
        int DragonGirlBluePos = (top + 6) * level.width() + left + 6;

        DreamcatcherMaker vis = new DreamcatcherMaker();
        Point c = center();
        vis.pos(c.x - 2, c.y - 2);
        level.customTiles.add(vis);

        Mob n = new DragonGirlBlue();
        n.pos = DragonGirlBluePos;
        level.mobs.add(n);

        Painter.fill( level, this,0, Terrain.WALL );
        drawDreamcatcher(center,level);
    }

    public static class DreamcatcherMaker extends CustomTilemap {

        {
            texture = Assets.Environment.Dreamcatcher;
            tileW = tileH = 5;

        }

        final int TEX_WIDTH = 80;

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(mapSimpleImage(0, 0, TEX_WIDTH), 5);
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

    private void drawDreamcatcher(Point center,Level level) {
        drawWeb(center,level);
        drawCenter(level);
    }



    private void drawWeb(Point center,Level level) {
        Painter.drawCircle(level, center, 6, WALL_DECO);
        Painter.drawCircle(level, center, 5, FURROWED_GRASS);
        Painter.drawCircle(level, center, 4, WATER);
        Painter.drawCircle(level, center, 3, EMPTY_SP);
        Painter.drawCircle(level, center, 2, EMPTY_SP);
        Painter.drawCircle(level, center, 1, EMBERS);
    }

    private void drawCenter(Level level) {
        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
            if (door.x == left || door.x == right){
                Painter.drawInside(level, this, door, width()/2, FURROWED_GRASS);
            } else {
                Painter.drawInside(level, this, door, height()/2, FURROWED_GRASS);
            }
        }
    }
}
