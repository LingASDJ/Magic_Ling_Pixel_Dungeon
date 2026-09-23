package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMBERS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.BackGoKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.watabou.noosa.Game;

public class SnowChasmLevel extends Level {

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if(transition.type == LevelTransition.Type.BRANCH_ENTRANCE){
            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = depth;
            InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_EXIT;
            InterlevelScene.curTransition.destBranch = 7;
            InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_EXIT;
            InterlevelScene.curTransition.centerCell = -1;
            Game.switchScene(InterlevelScene.class);
        }
        return false;
    }

    private static final int W = WALL;
    private static final int O = Terrain.DOOR;
    private static final int R = Terrain.MINE_CRYSTAL;
    private static final int E = EMPTY;
    private static final int M = EMPTY_SP;
    private static final int S = WATER;
    private static final int D = EMBERS;
    private static final int T = EXIT;

    //portals. (from, to).

    private static final int WIDTH = 17;
    private static final int HEIGHT = 17;

    private static final int[] code_map = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,R,E,E,E,W,E,E,E,E,E,W,R,E,E,E,W,
            W,E,R,E,E,W,M,M,M,M,M,W,E,E,E,E,W,
            W,E,E,R,M,W,S,S,M,E,E,W,M,E,E,E,W,
            W,E,E,M,R,W,S,S,M,E,E,W,E,M,E,R,W,
            W,W,W,W,O,W,S,S,R,E,E,W,O,W,W,W,W,
            W,E,M,S,S,S,S,S,D,E,E,E,E,E,M,E,W,
            W,E,M,S,S,S,S,D,D,D,E,E,E,E,M,E,W,
            W,E,M,M,M,R,D,D,T,D,D,R,M,M,M,E,W,
            W,E,M,E,E,E,E,D,D,D,S,S,S,S,M,E,W,
            W,E,M,E,E,E,E,E,D,S,S,S,S,S,M,E,W,
            W,W,W,W,O,W,E,E,R,S,S,W,O,W,W,W,W,
            W,R,E,M,E,W,E,E,M,S,S,W,R,M,E,E,W,
            W,E,E,E,M,W,E,E,M,S,S,W,M,R,E,E,W,
            W,E,E,E,E,W,M,M,M,M,M,W,E,E,R,E,W,
            W,E,E,E,R,W,E,E,E,E,E,W,E,E,E,R,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
    };

    {
        color1 = 5459774;
        color2 = 12179041;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 246;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.BRANCH_EXIT);
        transitions.add(enter);

        int exits = 144;

        LevelTransition exit = new LevelTransition(this, exits, LevelTransition.Type.BRANCH_ENTRANCE);
        transitions.add(exit);

        return true;
    }

    @Override
    protected void createItems() {
        drop( new BackGoKey(), 42  );
    }

    @Override
    public Mob createMob() {
        return null;
    }

    @Override
    protected void createMobs() {
    }


    public int randomRespawnCell() {
        return this.entrance - width();
    }


    public Actor respawner() {
        return null;
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_COLD;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CAVES;
    }

}
