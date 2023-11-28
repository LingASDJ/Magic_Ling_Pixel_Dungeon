package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.JunglePainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class AncientMysteryCityLevel extends RegularLevel {

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 6;
        //4 to 6, average 5
        return 4+ Random.chances(new float[]{1, 3, 1});
    }

    @Override
    protected Painter painter() {
        return new JunglePainter()
                .setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 6)
                .setGrass(feeling == Feeling.GRASS ? 0.65f : 0.15f, 3);
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_ANCIENT;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_ANCIENT;
    }

}