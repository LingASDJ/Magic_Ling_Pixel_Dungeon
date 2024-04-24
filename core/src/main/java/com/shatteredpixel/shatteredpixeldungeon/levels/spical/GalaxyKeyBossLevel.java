package com.shatteredpixel.shatteredpixeldungeon.levels.spical;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ALCHEMY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.BARRICADE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CRYSTAL_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.GALAXY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;

public class GalaxyKeyBossLevel extends Level {

    {
        viewDistance = 100;
    }

    private static final int WIDTH = 30;
    private static final int HEIGHT = 44;

    private static final int F = GALAXY;
    private static final int S = SIGN;
    private static final int X = BARRICADE;
    private static final int W = WATER;
    private static final int A = EMPTY_SP;
    private static final int B = EMPTY;
    private static final int O = EXIT;
    private static final int K = SIGN;
    private static final int D = CRYSTAL_DOOR;
    private static final int G = WALL;
    private static final int U = ALCHEMY;
    private static final int[] eye_map = {
            F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,F,S,S,S,S,S,F,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,S,S,B,B,B,S,S,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,S,B,B,B,B,B,S,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,S,B,B,O,B,B,S,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,S,B,B,B,B,B,S,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,S,K,B,B,B,X,S,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,F,S,K,B,X,S,F,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,F,F,S,B,S,F,F,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,F,F,S,B,S,F,F,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,S,S,S,S,S,S,B,S,F,F,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,S,B,A,B,B,B,B,S,S,S,S,S,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,S,K,K,K,K,B,B,B,B,A,B,S,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,F,S,B,X,K,K,K,K,S,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,F,S,B,S,F,F,F,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,F,S,B,S,F,F,F,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,F,S,B,S,F,F,F,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,S,S,S,S,G,D,G,S,S,S,S,S,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,S,S,S,B,B,B,B,B,B,B,B,B,B,S,S,S,F,F,F,F,F,F,F,
            F,F,F,F,F,S,S,S,B,B,B,B,A,B,B,B,B,B,B,A,B,B,S,S,S,F,F,F,F,F,
            F,F,F,F,S,S,B,B,B,B,F,F,F,F,F,F,F,F,F,F,B,B,B,B,S,S,F,F,F,F,
            F,F,F,F,S,B,B,B,B,F,F,F,F,F,F,F,F,F,F,F,F,B,B,B,B,S,F,F,F,F,
            F,F,F,S,S,B,B,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,A,B,B,S,S,F,F,F,
            F,F,F,S,B,B,A,F,F,F,F,W,F,F,F,F,F,F,F,U,F,F,F,B,B,B,S,F,F,F,
            F,F,S,S,B,B,F,F,W,W,F,F,F,F,F,F,F,F,F,W,W,W,F,F,B,B,S,S,F,F,
            F,F,S,B,B,B,F,F,W,W,F,F,F,F,F,F,F,F,F,W,W,W,F,F,B,A,B,S,F,F,
            F,F,S,B,B,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,B,B,S,F,F,
            F,F,S,B,B,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,B,B,S,F,F,
            F,F,S,B,B,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,B,B,S,F,F,
            F,F,S,B,B,B,F,U,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,B,B,S,F,F,
            F,F,S,B,A,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,A,B,B,S,F,F,
            F,F,S,B,B,B,F,F,F,F,F,W,W,F,F,F,F,F,F,F,F,F,F,F,B,B,B,S,F,F,
            F,F,S,B,B,B,F,F,F,F,W,W,W,F,F,F,F,F,F,F,F,F,F,F,B,B,B,S,F,F,
            F,F,S,K,B,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,B,X,S,F,F,
            F,F,F,S,B,B,A,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,B,B,S,F,F,F,
            F,F,F,S,K,B,B,B,F,F,F,U,F,F,F,W,F,W,W,F,F,F,B,A,B,X,S,F,F,F,
            F,F,F,F,S,B,B,B,B,F,F,F,F,F,F,F,F,W,W,F,F,B,B,B,B,S,F,F,F,F,
            F,F,F,F,S,K,B,B,B,B,F,F,F,F,F,F,F,F,F,F,B,B,B,B,X,S,F,F,F,F,
            F,F,F,F,F,S,K,K,B,B,B,B,B,B,A,B,B,B,B,B,B,B,X,K,S,F,F,F,F,F,
            F,F,F,F,F,F,F,S,K,K,B,B,B,B,B,B,B,B,B,B,X,K,S,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,S,K,K,K,K,K,K,K,K,K,K,S,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,
            F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,
    };

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = eye_map.clone();

        int entrance = 1;
        int exit = 0;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exits = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exits);

        CustomTilemap vis = new GalaxyBehind();
        vis.pos(0, 0);
        customTiles.add(vis);
        CustomTilemap via = new GalaxyAbove();
        via.pos(0, 0);
        customTiles.add(via);

        return true;
    }

    public static class GalaxyBehind extends CustomTilemap {

        {
            texture = Assets.Environment.GALAXY_BD;

            tileW = 30;
            tileH = 44;
        }

        final int TEX_WIDTH = 30*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    public static class GalaxyAbove extends CustomTilemap {

        {
            texture = Assets.Environment.GALAXY_AE;

            tileW = 30;
            tileH = 44;
        }

        final int TEX_WIDTH = 30*16;

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

    @Override
    public String tilesTex() {
        return Assets.Environment.GALAXY_TILED;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.GALAXY_WATER;
    }

}
