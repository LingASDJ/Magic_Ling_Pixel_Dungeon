package com.shatteredpixel.shatteredpixeldungeon.levels.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.Morphs;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.TowerGods;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.TowerMachine;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.TowerMind;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.TowerTime;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;

public class MorpheusBossLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 16;
    }

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.HOLLOW_CITY_HARD, true);
    }

    @Override
    public void playBossMusic(){
        Music.playModeBGM(Assets.Music.MORP_BOSS,true);
    }

    private static final int WIDTH = 25;
    private static final int HEIGHT = 25;

    private static final int S = CHASM;
    private static final int G = WALL;
    private static final int E = EMPTY_SP;

    private static final int[] code_map = {
            S,S,S,S,S,S,S,S,G,G,G,G,G,G,G,G,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,G,G,G,G,E,E,E,E,E,E,G,G,G,G,S,S,S,S,S,S,
            S,S,S,S,G,G,E,E,E,E,E,E,E,E,E,E,E,E,G,G,S,S,S,S,S,
            S,S,S,S,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,G,S,S,S,S,
            S,S,G,G,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,G,G,S,S,
            S,S,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,G,S,
            S,S,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,S,
            S,G,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,S,
            S,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,G,
            G,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,
            G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,
            G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,
            G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,
            G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,
            G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,
            G,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,
            S,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,G,
            S,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,S,
            S,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,S,
            S,G,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,G,S,
            S,S,G,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,G,S,S,
            S,S,S,G,G,G,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,G,S,S,S,
            S,S,S,S,S,G,G,G,E,E,E,E,E,E,E,E,E,E,G,G,G,G,S,S,S,
            S,S,S,S,S,S,S,G,G,G,E,E,E,E,E,G,G,G,G,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,G,G,G,G,G,G,G,S,S,S,S,S,S,S,S,S,
    };

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 412;
        int exit = 0;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exits = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exits);

        CustomTilemap vis = new GalaxyBackGround();
        vis.pos(0, 0);
        customTiles.add(vis);

        return true;
    }

    @Override
    protected void createMobs() {
        Morphs morphs = new Morphs();
        morphs.pos = 312;
        mobs.add(morphs);
    }


    @Override
    public void seal() {
        super.seal();
        TowerGods towerGods = new TowerGods();
        towerGods.pos = 304;
        GameScene.add(towerGods);

        TowerTime towerTime = new TowerTime();
        towerTime.pos = 512;
        GameScene.add(towerTime);

        TowerMachine towerMachine = new TowerMachine();
        towerMachine.pos = 112;
        GameScene.add(towerMachine);

        TowerMind towerMind = new TowerMind();
        towerMind.pos = 320;
        GameScene.add(towerMind);
    }

    public static class GalaxyBackGround extends CustomTilemap {

        {
            texture = Assets.Environment.GALAXY_BACKGROUND;

            tileW = 25;
            tileH = 25;
        }

        final int TEX_WIDTH = 25*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    @Override
    protected void createItems() {

    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_MORGALAXY;
    }

    @Override
    public String waterTex() {
        return Assets.Interfaces.BLACK_RECT;
    }
}
