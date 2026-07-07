package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;

public class WitchRoadLevel extends Level {

    private static final int WIDTH = 13;
    private static final int HEIGHT = 35;

    //TODO 等待正式音乐到达
    @Override
    public void playLevelMusic(){
        Music.playModeBGM("music/road.mp3", true);
    }

    private static final int[] codedMap = {
            25, 20, 20, 20, 5, 5, 18, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 5, 5, 5, 20, 20, 20, 25,
            25, 20, 20, 20, 5, 5, 17, 5, 5, 20, 20, 20, 25
    };

    private int codeToTerrain(int code){
        switch (code){
            default:
                return Terrain.EMPTY;
            case 65:
                return Terrain.WALL;
            case 5: case 11:
                return Terrain.EMPTY_SP;
            case 2:
                return Terrain.EMPTY_DECO;
            case 21:
                return Terrain.PEDESTAL;
            case 25:
                return Terrain.CHASM;
            case 66:
                return Terrain.WALL_DECO;
            case 17:
                return Terrain.ENTRANCE;
            case 18:
                return Terrain.LOCKED_EXIT;
            case 98:
                return Terrain.STATUE;
            case 99:
                return Terrain.STATUE_SP;
            case 20:
                return Terrain.SIGN;
        }
    }

    private static final int EXIT = 19;
    private static final int ENTRANCE = 409;

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        for(int i= 0; i< HEIGHT * WIDTH; ++i){
            map[i] = codeToTerrain(codedMap[i]);
        }

        LevelTransition entrance = new LevelTransition(this, ENTRANCE, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(entrance);

        LevelTransition exit = new LevelTransition(this, EXIT, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);


        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);

        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);

        return true;
    }

    @Override
    protected void createMobs() {

    }

    @Override
    protected void createItems() {

    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_PLACE;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_PLACE;
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.TOMB_ROAD;

            tileW = 13;
            tileH = 35;
        }

        final int TEX_WIDTH = 13*16;

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
            texture = Assets.Environment.TOMB_ROAD;

            tileW = 13;
            tileH = 35;
        }

        final int TEX_WIDTH = 13*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

}
