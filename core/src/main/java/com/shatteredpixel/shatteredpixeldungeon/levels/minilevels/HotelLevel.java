package com.shatteredpixel.shatteredpixeldungeon.levels.minilevels;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.BOOKSHELF;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CRYSTAL_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CUSTOM_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.ATRI;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.BlueCJ;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Bzmdr;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoonCat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.QinYueWolf;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WhiteLing;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Random;

public class HotelLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 100;
    }

    private static final int WIDTH = 14;
    private static final int HEIGHT = 20;

    private static final int W = WALL;
    private static final int D = EMPTY;
    private static final int C = DOOR;
    private static final int X = EXIT;
    private static final int S = STATUE;
    private static final int G = CRYSTAL_DOOR;
    private static final int U = CUSTOM_DECO;
    private static final int I = BOOKSHELF;

    private static final int B = SIGN;

    private static final int R = WATER;




    private static final int[] code_map = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,D,D,D,D,D,W,D,D,D,D,D,D,W,
            W,D,D,D,D,D,C,D,D,S,X,S,D,W,
            W,D,D,D,D,D,W,D,D,D,D,D,D,W,
            W,W,W,W,W,W,W,D,D,D,D,D,D,W,
            W,D,D,D,D,D,W,D,D,I,I,I,B,W,
            W,R,R,D,D,D,C,D,D,D,D,D,B,W,
            W,R,R,D,D,D,W,D,D,I,I,D,B,W,
            W,W,W,W,W,W,W,D,D,D,D,D,B,W,
            W,D,D,D,D,D,W,D,D,D,D,D,B,W,
            W,D,D,D,D,D,C,D,D,D,D,D,B,W,
            W,D,D,D,D,D,W,D,D,U,U,U,U,W,
            W,W,W,W,W,W,W,D,D,U,W,W,W,W,
            W,D,D,D,D,D,W,D,D,U,D,D,S,W,
            W,D,D,D,D,D,C,D,D,U,D,D,S,W,
            W,D,D,D,D,D,W,D,D,U,D,D,D,W,
            W,W,W,W,W,W,W,D,D,U,D,D,S,W,
            W,D,D,D,D,D,W,D,D,W,D,D,S,W,
            W,D,D,D,D,D,C,D,D,G,D,D,D,W,
            U,U,U,U,U,U,W,U,U,W,U,U,U,U,
    };

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_EXIT) {
            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = depth;
            InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_ENTRANCE;
            InterlevelScene.curTransition.destBranch = 0;
            InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_ENTRANCE;
            InterlevelScene.curTransition.centerCell = -1;
            Game.switchScene(InterlevelScene.class);
        }
        return false;
    }

    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 38;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.BRANCH_EXIT);
        transitions.add(enter);

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
            texture = Assets.Environment.HOTEL_POX;

            tileW = 14;
            tileH = 20;
        }

        final int TEX_WIDTH = 14*16;

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
            texture = Assets.Environment.HOTEL_PO;

            tileW = 14;
            tileH = 20;
        }

        final int TEX_WIDTH = 14*16;

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

        if(Random.Float()<0.1f){
            WhiteLing n = new WhiteLing();
            n.pos = (this.width * 9 + 10);
            mobs.add(n);
        }

        if(!Dungeon.isChallenged(CS)) {
            MoonCat n1 = new MoonCat();
            n1.pos = 143;
            mobs.add(n1);

            ATRI n3 = new ATRI();
            n3.pos = 87;
            mobs.add(n3);

            QinYueWolf n5 = new QinYueWolf();
            n5.pos = 255;
            mobs.add(n5);

            BlueCJ n7 = new BlueCJ();
            n7.pos = 31;
            mobs.add(n7);

            if (!Statistics.onlyBzmdr) {
                Bzmdr n9 = new Bzmdr();
                n9.pos = 199;
                mobs.add(n9);
            }
        }

    }

    @Override
    protected void createItems() {

    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_CITY_CS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CAVES;
    }


}
