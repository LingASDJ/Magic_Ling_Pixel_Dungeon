package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CUSTOM_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.TombFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class TombHallExitLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 8;
    }

    private static final int WIDTH = 17;
    private static final int HEIGHT = 24;

    private static final int S = CHASM;

    private static final int G = SIGN;

    private static final int E = EMPTY_SP;
    private static final int R = CUSTOM_DECO;
    private static final int D = DOOR;

    private static final int[] code_map = {
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,G,G,S,S,S,S,S,G,G,S,S,S,S,
            S,S,S,S,G,G,S,S,G,S,S,G,G,S,S,S,S,
            S,S,S,S,G,G,S,G,G,G,S,G,G,S,S,S,S,
            S,S,S,S,G,G,G,G,G,G,G,G,G,S,S,S,S,
            S,S,S,S,G,G,G,G,G,G,G,G,G,S,S,S,S,
            S,S,S,S,G,G,G,G,G,G,G,G,G,S,S,S,S,
            S,S,S,S,G,G,G,G,G,G,G,G,G,S,S,S,S,
            S,S,S,S,G,G,G,G,G,G,G,G,G,S,S,S,S,
            S,S,S,S,G,G,G,G,G,G,G,G,G,S,S,S,S,
            S,S,S,S,G,G,G,G,G,G,G,G,G,S,S,S,S,
            S,S,S,S,G,G,G,G,G,G,G,G,G,S,S,S,S,
            S,S,S,S,G,G,G,G,D,G,G,G,G,S,S,S,S,
            S,S,S,R,G,G,G,E,E,E,G,G,G,R,S,S,S,
            S,S,S,G,G,G,G,E,E,E,G,G,G,G,S,S,S,
            S,S,S,G,G,R,G,E,E,E,G,R,G,G,S,S,S,
            S,S,S,S,S,G,G,E,E,E,G,G,S,S,S,S,S,
            S,S,S,S,S,S,G,E,E,E,G,S,S,S,S,S,S,
            S,S,S,S,S,G,G,E,E,E,G,G,S,S,S,S,S,
            S,S,S,S,S,G,G,E,E,E,G,G,S,S,S,S,S,
            S,S,S,S,S,G,G,E,E,E,G,G,S,S,S,S,S,
            S,S,S,S,S,G,G,E,E,E,G,G,S,S,S,S,S,
            S,S,G,G,G,G,E,E,E,E,E,G,G,G,G,S,S,
            G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,
    };

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.TOMB_CACHE, true);
    }

    @Override
    protected boolean build() {
        feeling = Feeling.NONE;
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int enter = 212;
        LevelTransition entrance = new LevelTransition(this, enter, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(entrance);

        for(int cellId = 381; cellId <= 383; cellId++){
            LevelTransition exit = new LevelTransition(this, cellId, LevelTransition.Type.REGULAR_ENTRANCE);
            transitions.add(exit);
        }

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);

        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);

        return true;
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addVisuals(this, visuals);
        return visuals;
    }

    public static void addVisuals(Level level, Group group){
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == CUSTOM_DECO) {
                group.add( new LanterFire( i ) );
            }
        }
    }

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
                    InterlevelScene.curTransition.destDepth = depth;
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
                    InterlevelScene.curTransition.destDepth = depth + 1;
                    InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_ENTRANCE;
                    InterlevelScene.curTransition.destBranch = 0;
                    InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_ENTRANCE;
                    InterlevelScene.curTransition.centerCell  = -1;
                    Game.switchScene( InterlevelScene.class );
                }
            });
            return false;
        }
        return false;
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
            texture = Assets.Environment.TOMB_ENTR;

            tileW = 17;
            tileH = 24;
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

    public static class townAbove extends CustomTilemap {

        {
            texture = Assets.Environment.TOMB_ENTR;

            tileW = 17;
            tileH = 24;
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



    public static class LanterFire extends Emitter {

        private int pos;

        public LanterFire( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );
            pos( p.x - 1, p.y + 2, 2, 0 );

            pour(TombFlameParticle.FACTORY, 0.15f );

            add( new Halo( 12, 0xccf8ff, 0.2f ).point( p.x, p.y + 1 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

}


