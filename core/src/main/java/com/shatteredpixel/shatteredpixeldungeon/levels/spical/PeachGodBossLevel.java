package com.shatteredpixel.shatteredpixeldungeon.levels.spical;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Tilemap;

public class PeachGodBossLevel extends Level {

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

    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell(ch);
        GLog.p(String.valueOf(hero.pos));
    }

    private static final int WIDTH = 21;
    private static final int HEIGHT = 24;

    private static final int E = EMPTY;
    private static final int R = SIGN;
    private static final int S = CHASM;
    private static final int V = ENTRANCE;

    private static final int[] code_map = {
            S,S,S,S,R,R,R,R,S,S,S,S,S,S,R,S,S,S,S,S,S,
            S,S,R,R,R,R,R,R,R,S,S,S,S,R,R,S,S,R,R,S,S,
            S,S,R,R,R,R,R,R,R,R,R,S,S,R,R,R,R,R,R,S,S,
            S,S,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,S,S,
            S,S,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,
            S,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,
            S,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,
            S,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,
            S,R,R,R,R,R,R,R,R,R,E,E,R,R,R,R,R,R,R,R,R,
            S,R,R,R,E,E,E,E,E,R,E,E,E,R,R,R,R,R,R,R,R,
            S,R,R,E,E,E,E,E,E,R,E,E,E,E,E,E,E,R,R,R,R,
            S,R,R,E,E,E,E,E,E,E,E,E,E,E,E,E,E,R,R,R,S,
            S,R,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,R,S,
            S,R,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,R,S,
            S,R,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,R,S,
            S,R,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,R,S,
            S,R,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,R,S,
            S,R,R,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,R,R,S,
            S,S,R,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,R,S,S,
            S,S,R,R,E,E,E,E,E,E,E,E,E,E,E,E,E,R,R,S,S,
            S,S,S,R,E,E,E,E,E,E,E,E,E,E,E,E,R,R,S,S,S,
            S,S,S,R,R,R,R,R,R,R,E,E,E,R,R,R,R,S,S,S,S,
            S,S,S,S,S,S,S,S,S,R,V,R,R,R,R,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,R,R,R,R,R,R,S,S,S,S,S,S,
    };

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 472;
        int exit = 0;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exits = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exits);

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);

        //map[exit] = Terrain.LOCKED_EXIT;

        return true;
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.PEACH_BOSS;

            tileW = WIDTH;
            tileH = HEIGHT;
        }

        final int TEX_WIDTH = WIDTH*16;

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
