package com.shatteredpixel.shatteredpixeldungeon.levels.minilevels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.AncityPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class AncityMiniLevel extends RegularLevel {

    @Override
    protected Painter painter() {
        return new AncityPainter()
                .setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 5)
                .setGrass(feeling == Feeling.GRASS ? 0.80f : 0.20f, 4)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 3;
        //4 to 6, average 5
        return 1+ Random.chances(new float[]{1, 3, 1});
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 2;
        //1 to 2, average 1.8
        return 1+Random.chances(new float[]{1, 4});
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
