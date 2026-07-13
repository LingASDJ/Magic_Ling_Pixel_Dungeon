package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_WELL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.GRASS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;

public class RogerBossLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 8;
        extraGlass = false;
    }

    private static final int WIDTH = 19;
    private static final int HEIGHT = 19;

    private static final int S = CHASM;
    private static final int R = WATER;
    private static final int E = EMPTY_SP;
    private static final int A = ENTRANCE;
    private static final int B = EXIT;
    private static final int W = WALL;
    private static final int H = GRASS;
    private static final int P = STATUE;
    private static final int K = STATUE_SP;
    private static final int G = EMPTY_WELL;
    private static final int[] code_map = {
            S,S,S,S,S,S,S,S,E,E,E,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,E,B,E,W,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,E,E,E,W,S,S,S,S,S,S,S,
            S,S,S,S,S,W,W,W,E,E,E,W,W,W,S,S,S,S,S,
            S,S,S,S,W,W,E,E,E,E,E,E,E,W,W,S,S,S,S,
            S,S,S,W,W,E,K,E,H,E,R,E,K,E,W,W,S,S,S,
            S,S,S,W,P,E,E,H,R,R,R,E,E,E,P,W,S,S,S,
            S,S,S,W,E,E,E,E,R,E,E,H,E,E,E,W,S,S,S,
            S,S,S,W,E,E,H,E,E,E,E,H,H,E,E,W,S,S,S,
            S,S,S,W,P,H,H,E,E,E,E,E,E,E,P,W,S,S,S,
            S,S,S,W,E,E,R,R,E,E,E,E,E,E,E,W,S,S,S,
            S,S,S,W,E,E,E,R,E,E,H,H,R,E,E,W,S,S,S,
            S,S,S,W,P,H,E,E,R,E,H,R,R,E,P,W,S,S,S,
            S,S,S,W,W,E,K,E,E,E,E,R,K,E,W,W,S,S,S,
            S,S,S,S,W,W,E,E,E,E,E,E,E,W,W,S,S,S,S,
            S,S,S,S,S,W,W,W,E,A,E,W,W,W,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,E,W,W,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,W,W,W,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
    };

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                    if (timeFreeze != null) timeFreeze.disarmPresses();
                    Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                    if (timeBubble != null) timeBubble.disarmPresses();
                    InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                    InterlevelScene.curTransition = new LevelTransition();
                    InterlevelScene.curTransition.destDepth = depth - 1;
                    InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
                    InterlevelScene.curTransition.destBranch = 0;
                    InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                    InterlevelScene.curTransition.centerCell  = -1;
                    Game.switchScene( InterlevelScene.class );
                }
            });
            return false;
        } else if(transition.type == LevelTransition.Type.REGULAR_EXIT) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                    if (timeFreeze != null) timeFreeze.disarmPresses();
                    Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                    if (timeBubble != null) timeBubble.disarmPresses();
                    InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                    InterlevelScene.curTransition = new LevelTransition();
                    InterlevelScene.curTransition.destDepth = depth;
                    InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_ENTRANCE;
                    InterlevelScene.curTransition.destBranch = 1;
                    InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_ENTRANCE;
                    InterlevelScene.curTransition.centerCell  = -1;
                    Game.switchScene( InterlevelScene.class );
                }
            });
            return false;
        }
        return false;
    }

    //TODO 等待正式音乐到达
    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.TOMB_CACHE, true);
    }

    @Override
    protected boolean build() {
        feeling = Feeling.NONE;
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int enter = 28;
        LevelTransition entrance = new LevelTransition(this, enter, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(entrance);

        int entra = 294;
        LevelTransition exit = new LevelTransition(this, entra, LevelTransition.Type.REGULAR_ENTRANCE);
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

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.TOMB_HALL;

            tileW = 19;
            tileH = 19;
        }

        final int TEX_WIDTH = 19*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_TOMB;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_TOMB;
    }

    public static class townAbove extends CustomTilemap {

        {
            texture = Assets.Environment.TOMB_HALL;

            tileW = 19;
            tileH = 19;
        }

        final int TEX_WIDTH = 19*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

}
