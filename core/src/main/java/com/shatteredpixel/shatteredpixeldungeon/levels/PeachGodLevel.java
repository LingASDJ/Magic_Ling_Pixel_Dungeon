package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;

public class PeachGodLevel extends Level{

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 16;
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_CITY_CS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CITY;
    }


    private static final int WIDTH = 31;
    private static final int HEIGHT = 47;

    private static final int W = WALL;
    private static final int D = EMPTY;
    private static final int X = SIGN;
    private static final int C = CHASM;
    private static final int M = EMPTY_SP;

    private static final int[] code_map = {
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,W,W,W,W,W,W,W,W,W,W,W,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,W,W,W,W,W,X,X,X,X,X,X,X,X,X,W,W,W,C,C,C,C,C,C,C,
            C,C,C,C,C,W,W,W,X,X,X,X,X,X,X,X,X,X,X,X,X,X,X,W,W,W,W,C,C,C,C,
            C,C,C,W,W,W,X,X,X,X,X,X,X,X,X,D,D,D,D,D,X,X,X,X,X,X,W,W,W,C,C,
            C,C,C,W,X,X,X,X,X,D,D,X,D,D,D,D,D,D,D,D,X,X,X,X,X,X,X,X,W,C,C,
            C,C,C,W,X,X,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,D,X,X,X,X,W,C,C,
            C,C,W,W,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,X,W,W,C,
            C,W,W,X,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,X,W,C,
            C,W,X,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,W,C,
            C,W,X,X,D,D,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,W,W,
            W,W,X,X,X,D,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,W,
            W,X,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,W,W,
            W,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,D,X,X,X,W,W,
            W,X,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,D,X,X,X,X,W,
            W,X,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,D,D,D,X,X,X,W,
            W,X,X,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,W,
            W,X,X,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,W,
            W,X,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,W,
            W,X,X,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,X,W,
            W,W,X,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,W,W,
            C,W,X,X,X,X,D,D,D,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,W,C,
            C,W,X,X,X,X,D,D,D,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,X,W,C,
            C,W,W,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,W,W,C,
            C,C,W,W,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,W,W,C,C,
            C,C,C,W,W,X,X,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,X,X,X,X,W,C,C,C,
            C,C,C,C,W,W,X,X,X,D,X,X,D,D,D,D,D,D,D,D,D,X,X,X,X,X,W,W,C,C,C,
            C,C,C,C,C,W,W,X,X,X,X,X,D,D,D,D,D,D,X,X,X,X,X,X,X,W,W,C,C,C,C,
            C,C,C,C,C,C,W,W,W,X,X,X,X,X,X,D,D,X,X,X,X,X,X,X,W,W,C,C,C,C,C,
            C,C,C,C,C,C,C,C,W,W,W,W,W,W,W,M,W,W,W,W,W,W,W,W,W,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,X,X,X,X,X,D,X,X,X,X,X,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,X,X,X,X,X,D,X,X,X,X,X,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,X,X,X,X,D,X,X,X,X,X,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,X,X,X,D,X,X,X,C,X,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,X,X,D,X,X,X,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,X,X,X,D,C,X,X,X,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,D,D,X,X,X,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,X,D,X,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,X,D,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,D,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,D,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,X,D,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,X,D,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,D,X,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,X,D,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,D,D,D,D,D,D,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,D,D,D,D,D,D,C,C,C,C,C,C,C,C,C,C,C,
    };

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 47 + WIDTH * 43;
        int exit = 0;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exits = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exits);

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);
        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);

        //map[exit] = Terrain.LOCKED_EXIT;

        return true;
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.PEACH_POX;

            tileW = 31;
            tileH = 47;
        }

        final int TEX_WIDTH = 31*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    public static class townAbove extends CustomTilemap {

        {
            texture = Assets.Environment.PEACH_PO;

            tileW = 31;
            tileH = 47;
        }

        final int TEX_WIDTH = 31*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    @Override
    protected void createMobs() {

    }

    @Override
    protected void createItems() {

    }
}
