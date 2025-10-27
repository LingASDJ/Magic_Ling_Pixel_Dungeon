package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.escape;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;

public class MagicCircleRoom extends ExitRoom {

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

    private static final int[] pre_map = {
            1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1,
            1, 1, 49, 49, 5, 5, 5, 49, 49, 1, 1,
            1, 49, 50, 25, 74, 5, 74, 25, 50, 49, 1,
            1, 49, 25, 25, 25, 5, 25, 25, 25, 49, 1,
            1, 5, 74, 25, 5, 5, 5, 25, 74, 5, 1,
            5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
            1, 5, 25, 25, 5, 5, 5, 25, 25, 5, 1,
            1, 49, 25, 25, 25, 5, 25, 25, 25, 49, 1,
            1, 49, 49, 25, 25, 5, 25, 25, 49, 49, 1,
            1, 1, 81, 49, 5, 5, 5, 49, 81, 1, 1,
            1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1
    };

    private int codeToTerrain(int code){
        switch (code){
            case 50:
                return Terrain.WALL_DECO;
            case 74:
                return Terrain.STATUE_SP;
            case 5:
                return Terrain.EMPTY_SP;
            case 49:
                return Terrain.WALL;
            case 25:
                return Terrain.CHASM;
            default:
                return EMPTY;
        }
    }

    private void set(Level level, int x, int y, int value) {
        level.map[x + y * level.width()] = value;
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level,this, 0, WALL);

        MagicCircleMaker vis = new MagicCircleMaker();
        Point c = center();
        vis.pos(c.x - 1, c.y - 1);
        level.customTiles.add(vis);

        for (int i = left + 1; i <= right-1; i++) {
            for (int j = top + 1; j <= bottom-1; j++) {
                int dx = i - (left + 1);
                int dy = j - (top + 1);
                int index = dy * (minWidth()-2) + dx;
                if (index >= 0 && index < pre_map.length) {
                    set(level, i, j, codeToTerrain(pre_map[index]));
                }
            }

        }

        int centerX = left + width() / 2;
        int centerY = top + height() / 2;
        Point cx = new Point(centerX, centerY);

        int exit =  (left + right) - cx.x + cx.y * level.width();
        Painter.set( level, exit, Terrain.EXIT );
        level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
    }

    public static class MagicCircleMaker extends CustomTilemap {

        {
            texture = Assets.Environment.Magic_Marker;
            tileW = 3;
            tileH = 4;
        }

        final int TEX_WIDTH = 48;

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(mapSimpleImage(0, 0, TEX_WIDTH), 3);
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

