package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfAwareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class ThreeWellRoom extends SpecialRoom {

    private static final Class<?>[] IDENTIFY_WATERS =
            {WaterOfAwareness.class};

    public Class<?extends WellWater> identifyWateroverrideWater = null;

    private static final Class<?>[] HEALS_WATERS =
            {WaterOfHealth.class};

    public Class<?extends WellWater> healsWateroverrideWater = null;

    private static final Class<?>[] RANDOM_WATERS =
            {WaterOfTransmutation.class};

    public Class<?extends WellWater> randomWateroverrideWater = null;

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        Point c = center();
        Painter.set( level, c.x, c.y, Terrain.WELL );
        Painter.set( level, c.x-2, c.y, Terrain.WELL );
        Painter.set( level, c.x+2, c.y, Terrain.WELL );

        @SuppressWarnings("unchecked")
        Class<? extends WellWater> iwaterClass =
                identifyWateroverrideWater != null ?
                        identifyWateroverrideWater :
                        (Class<? extends WellWater>) Random.element(IDENTIFY_WATERS);
        WellWater.seed(c.x + level.width() * c.y, 1, iwaterClass, level);

        @SuppressWarnings("unchecked")
        Class<? extends WellWater> hwaterClass =
                healsWateroverrideWater != null ?
                        healsWateroverrideWater :
                        (Class<? extends WellWater>) Random.element(HEALS_WATERS);
        WellWater.seed(c.x-2 + level.width() * c.y, 1, hwaterClass, level);

        @SuppressWarnings("unchecked")
        Class<? extends WellWater> rwaterClass =
                randomWateroverrideWater != null ?
                        randomWateroverrideWater :
                        (Class<? extends WellWater>) Random.element(RANDOM_WATERS);
        WellWater.seed(c.x+2 + level.width() * c.y, 1, rwaterClass, level);

        entrance().set( Room.Door.Type.REGULAR );
    }
}


