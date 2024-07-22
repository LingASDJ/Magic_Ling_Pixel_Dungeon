package com.shatteredpixel.shatteredpixeldungeon.levels.spical;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;

public class PeachRoadLevel extends Level {

    private static final int WIDTH = 17;
    private static final int HEIGHT = 23;

    private static final int W = CHASM;
    private static final int G = SIGN;
    private static final int X = ENTRANCE;
    private static final int V = WALL;
    private static final int E = EMPTY_SP;
    private static final int H = EXIT;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_CITY_CS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CITY;
    }

    private static final int[] code_map = {
            W,W,G,G,G,G,G,G,G,G,G,G,W,W,W,W,W,
            W,W,G,G,G,G,G,G,G,G,G,G,G,W,W,W,W,
            W,W,G,G,G,G,G,G,G,G,G,G,G,G,W,W,W,
            W,W,G,G,G,G,G,G,G,G,G,G,G,G,W,W,W,
            W,W,G,G,G,G,G,G,G,G,G,G,G,G,G,W,W,
            W,G,G,G,G,G,G,X,G,G,G,G,G,G,G,W,W,
            W,G,G,G,G,G,E,E,G,G,G,G,G,G,G,G,W,
            W,G,G,G,E,G,E,E,G,G,G,G,G,G,G,G,W,
            W,G,G,G,E,E,E,E,E,E,G,G,G,G,G,G,W,
            W,G,G,E,E,E,E,E,E,G,E,G,E,G,G,G,W,
            W,W,G,G,G,E,E,E,E,E,E,E,V,V,G,G,W,
            W,W,G,G,G,E,E,E,E,E,E,E,E,E,G,G,G,
            W,W,G,G,G,V,V,E,E,E,E,V,V,E,G,G,G,
            W,W,G,G,V,V,G,G,E,E,V,E,E,G,G,G,G,
            W,W,W,W,G,G,G,V,V,E,E,V,V,V,V,V,G,
            W,W,W,W,G,V,V,V,E,E,E,E,G,G,E,E,G,
            W,W,W,W,W,G,G,G,G,E,E,E,E,E,E,G,G,
            W,W,W,W,G,G,G,G,E,E,E,V,V,V,V,V,W,
            W,W,W,G,G,G,V,V,V,E,E,G,G,G,E,G,W,
            W,W,W,G,G,G,G,G,E,E,E,G,G,E,E,G,G,
            W,W,W,G,G,G,G,G,E,E,E,E,G,E,E,G,G,
            W,W,W,G,G,G,G,E,E,E,H,E,E,E,G,W,W,
            W,W,W,G,G,G,E,E,E,E,E,G,G,G,G,W,W,
    };

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 367;
        int exit = 92;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exits = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exits);

        CustomTilemap vis = new PatchImage();
        vis.pos(0, 0);
        customTiles.add(vis);

        //map[exit] = Terrain.LOCKED_EXIT;

        return true;
    }

    public static class PatchImage extends CustomTilemap {

        {
            texture = Assets.Environment.PEACH_PATCH;

            tileW = 17;
            tileH = 23;
        }

        final int TEX_WIDTH = 17*16;

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
