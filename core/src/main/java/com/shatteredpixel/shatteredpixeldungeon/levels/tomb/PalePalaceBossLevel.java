package com.shatteredpixel.shatteredpixeldungeon.levels.tomb;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PalePalaceBossLevel extends Level {

    private static final int WIDTH = 25;
    private static final int HEIGHT = 30;

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 100;
        extraGlass = false;
    }

    private static final int S = CHASM;
    private static final int W = SIGN;
    private static final int P = EMPTY_SP;
    private static final int R = ENTRANCE;

    private static final int[] code_map = {
            S, S, S, S, S, S, S, S, S, S, S, W, W, W, S, S, S, S, S, S, S, S, S, S, S,
            S, S, S, S, S, S, S, S, W, W, W, W, W, W, W, W, W, S, S, S, S, S, S, S, S,
            S, S, S, S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S, S, S, S,
            S, S, S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S, S, S,
            S, S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S, S,
            S, S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S, S,
            S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S,
            S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S,
            S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S,
            S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S,
            S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S,
            S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S,
            S, W, W, W, W, W, W, W, W, W, W, W, R, W, W, W, W, W, W, W, W, W, W, W, S,
            W, W, W, W, W, W, W, W, W, P, P, W, P, W, P, P, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            S, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, S,
            S, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, S,
            S, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, S,
            S, S, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, S, S,
            S, S, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, S, S,
            S, S, S, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, S, S, S,
            S, S, S, S, W, W, W, P, P, P, P, P, P, P, P, P, P, P, W, W, W, S, S, S, S,
            S, S, S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S, S, S
    };

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
        Random.pushGenerator(Random.Long());
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            int pos;
            do {
                pos = randomRespawnCell(null);
            } while (pos == entrance());
            for (Item i : bonesItems) {
                drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
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
            texture = Assets.Environment.TOMB_PALACE;

            tileW = 25;
            tileH = 30;
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


    public static class townAbove extends CustomTilemap {

        {
            texture = Assets.Environment.TOMB_PALACE;

            tileW = 25;
            tileH = 30;
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

}
