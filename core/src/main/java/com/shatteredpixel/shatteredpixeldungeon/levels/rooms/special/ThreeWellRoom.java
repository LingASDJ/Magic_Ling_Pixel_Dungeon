package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.alter.AWaterOfAwareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.alter.AWaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.alter.AWaterOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.alter.AltWellWater;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfAwareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class ThreeWellRoom extends SpecialRoom {
    @Override
    public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
        return false;
    }

    @Override
    public int minWidth() {
        return Random.NormalIntRange(7,10);
    }

    @Override
    public int minHeight() {
        return Random.NormalIntRange(7,10);
    }

    @Override
    public int maxWidth() {
        return Random.NormalIntRange(7,10);
    }

    @Override
    public int maxHeight() {
        return Random.NormalIntRange(7,10);
    }
    private static final Class<?>[] IDENTIFY_WATERS =
            {AWaterOfAwareness.class};

    public Class<?extends AltWellWater> identifyWateroverrideWater = null;

    private static final Class<?>[] HEALS_WATERS =
            {AWaterOfHealth.class};

    public Class<?extends AltWellWater> healsWateroverrideWater = null;

    private static final Class<?>[] RANDOM_WATERS =
            {AWaterOfTransmutation.class};

    public Class<?extends AltWellWater> randomWateroverrideWater = null;

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        Point c = center();
        Painter.set( level, c.x, c.y, Terrain.ALTWELL );
        Painter.set( level, c.x-2, c.y, Terrain.ALTWELL );
        Painter.set( level, c.x+2, c.y, Terrain.ALTWELL );

        @SuppressWarnings("unchecked")
        Class<? extends AltWellWater> iwaterClass =
                identifyWateroverrideWater != null ?
                        identifyWateroverrideWater :
                        (Class<? extends AltWellWater>) Random.element(IDENTIFY_WATERS);
        AltWellWater.seed(c.x + level.width() * c.y, 1, iwaterClass, level);

        @SuppressWarnings("unchecked")
        Class<? extends AltWellWater> hwaterClass =
                healsWateroverrideWater != null ?
                        healsWateroverrideWater :
                        (Class<? extends AltWellWater>) Random.element(HEALS_WATERS);
        AltWellWater.seed(c.x-2 + level.width() * c.y, 1, hwaterClass, level);

        @SuppressWarnings("unchecked")
        Class<? extends AltWellWater> rwaterClass =
                randomWateroverrideWater != null ?
                        randomWateroverrideWater :
                        (Class<? extends AltWellWater>) Random.element(RANDOM_WATERS);
        AltWellWater.seed(c.x+2 + level.width() * c.y, 1, rwaterClass, level);

        entrance().set( Room.Door.Type.REGULAR );
    }
}


