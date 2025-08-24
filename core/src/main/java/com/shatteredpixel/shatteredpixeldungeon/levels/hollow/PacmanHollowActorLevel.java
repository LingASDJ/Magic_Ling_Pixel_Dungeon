package com.shatteredpixel.shatteredpixeldungeon.levels.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ALCHEMY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.BOOKSHELF;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_WELL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.HIGH_GRASS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.PEDESTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;
import static com.watabou.utils.Random.getRandomElement;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Anger;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Junko;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Smart;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Pink;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PacmanHollowActorLevel extends Level {

    {
        extraGlass = false;
        viewDistance = 25;
    }

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.BGM_YOU, true);
    }

    private static final int WIDTH = 19;
    private static final int HEIGHT = 29;

    private static final int R = WATER;
    private static final int K = BOOKSHELF;
    private static final int W = WALL;
    private static final int Z = STATUE_SP;
    private static final int X = STATUE;
    private static final int F = EMPTY_SP;
    private static final int E = EMPTY;
    private static final int G = EMPTY_WELL;
    private static final int Y = ALCHEMY;
    private static final int A = HIGH_GRASS;
    private static final int D = DOOR;
    private static final int M = WALL_DECO;
    private static final int S = SIGN;
    private static final int P = PEDESTAL;
    private static final int[] code_map = {
            W,W,M,W,W,M,M,W,W,W,W,W,M,M,W,W,M,W,W,
            W,X,E,A,E,R,R,R,X,W,X,R,R,R,E,A,E,X,W,
            W,E,E,A,E,E,R,R,E,W,E,R,R,E,E,A,E,E,W,
            W,E,W,W,A,W,X,X,A,W,A,X,X,W,A,W,W,E,W,
            W,A,W,Z,E,W,M,W,A,Z,A,W,M,W,E,Z,W,A,W,
            W,E,A,F,E,R,E,E,E,F,E,E,E,R,E,F,A,E,W,
            W,E,A,F,R,R,R,E,F,F,F,E,R,R,R,F,A,E,W,
            W,E,W,X,E,W,A,W,W,X,W,W,A,W,E,X,W,E,W,
            W,F,M,W,E,W,E,Z,W,W,W,Z,E,W,E,W,M,F,W,
            W,F,E,E,E,W,F,F,E,W,E,F,F,W,E,E,E,F,W,
            W,Y,E,E,E,W,M,A,A,W,A,A,M,W,E,E,E,Y,W,
            W,W,W,W,A,W,R,R,E,P,E,R,R,W,A,W,W,W,W,
            W,W,W,X,E,W,R,W,S,S,S,W,R,W,E,X,W,W,W,
            W,G,F,F,F,F,E,S,F,F,F,S,E,F,F,F,F,G,W,
            W,G,F,F,F,F,E,S,F,F,F,S,E,F,F,F,F,G,W,
            W,W,W,Z,E,W,E,W,S,S,S,W,E,W,E,Z,W,W,W,
            W,M,W,W,D,W,E,E,E,E,E,E,E,W,D,W,W,M,W,
            W,E,R,E,E,W,E,M,W,W,W,M,E,W,E,E,R,E,W,
            W,R,R,R,E,E,E,E,E,W,E,E,E,E,E,R,R,R,W,
            W,R,M,W,W,D,K,K,R,E,R,K,K,D,W,W,M,R,W,
            W,E,E,E,W,E,E,X,R,R,R,X,E,E,W,E,E,E,W,
            W,E,E,E,W,E,E,F,E,R,E,F,E,E,W,E,E,E,W,
            K,K,K,D,M,F,K,D,K,K,K,D,K,F,M,D,K,K,K,
            K,X,E,F,F,F,K,F,F,K,F,F,K,F,F,F,E,X,K,
            K,E,E,F,F,Y,W,E,E,W,E,E,W,Y,F,F,E,E,K,
            K,D,K,M,K,K,W,K,E,D,E,K,W,K,K,M,K,D,K,
            K,F,F,F,F,F,X,F,F,W,F,F,X,F,F,F,F,F,K,
            K,Z,F,F,F,F,F,F,Z,K,Z,F,F,F,F,F,F,Z,K,
            K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,
    };

    private final List<Integer> Random_Spawn_Pos = new ArrayList<>(Arrays.asList(40,496,510,54));

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();
        int randomElement = getRandomElement(Random_Spawn_Pos);
        LevelTransition ent = new LevelTransition(this, randomElement , LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(ent);

        CustomTilemap vis = new GhostMapBehind();
        vis.pos(0, 0);
        customTiles.add(vis);

        return true;
    }


    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);

        if (ch.pos == 248) {
            Heap heap = Dungeon.level.heaps.get(248);
            if (heap != null && !heap.isEmpty()) {
                // 有掉落物，不传送
                return;
            }
            ScrollOfTeleportation.appear(ch, 263);
            hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        } else if (ch.pos == 264) {
            Heap heap = Dungeon.level.heaps.get(264);
            if (heap != null && !heap.isEmpty()) {
                // 有掉落物，不传送
                return;
            }
            ScrollOfTeleportation.appear(ch, 249);
            hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        } else if (ch.pos == 267) {
            Heap heap = Dungeon.level.heaps.get(267);
            if (heap != null && !heap.isEmpty()) {
                // 有掉落物，不传送
                return;
            }
            ScrollOfTeleportation.appear(ch, 282);
            hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        } else if (ch.pos == 283) {
            Heap heap = Dungeon.level.heaps.get(283);
            if (heap != null && !heap.isEmpty()) {
                // 有掉落物，不传送
                return;
            }
            ScrollOfTeleportation.appear(ch, 268);
            hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }


    public static class GhostMapBehind extends CustomTilemap {

        {
            texture = Assets.Environment.GHOST_HOUSE;

            tileW = 19;
            tileH = 29;
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
        return Assets.Environment.TILES_GHOST;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_GHOST;
    }

    @Override
    protected void createMobs() {

        Ghost_Anger red = new Ghost_Anger();
        red.pos = 257;
        mobs.add(red);

        Ghost_Smart ghostOrange = new Ghost_Smart();
        ghostOrange.pos = 255;
        mobs.add(ghostOrange);
//
        Ghost_Pink ghostPink = new Ghost_Pink();
        ghostPink.pos = 274;
        mobs.add(ghostPink);
//
        Ghost_Junko ghostRed = new Ghost_Junko();
        ghostRed.pos = 276;
        mobs.add(ghostRed);
    }

    @Override
    protected void createItems() {

    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addGhostWindowsVisuals(this, visuals);
        return visuals;
    }

    public static void addGhostWindowsVisuals(Level level, Group group){
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add( new Lanter( i ) );
            }
        }
    }

    public static class Lanter extends Emitter {

        private int pos;

        public Lanter( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );

            add( new Halo( 12, 0xFAF6A7, 0.3f ).point( p.x, p.y + 1 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

}
