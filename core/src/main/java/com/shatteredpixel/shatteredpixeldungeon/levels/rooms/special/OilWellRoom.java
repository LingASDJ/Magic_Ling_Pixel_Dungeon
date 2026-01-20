package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfOil;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

//USA:Where?
public class OilWellRoom extends SpecialRoom {

    private static final Class<?>[] WATERS =
            {WaterOfOil.class};

    @Override
    public int minWidth() {
        return 11;
    }

    @Override
    public int minHeight() {
        return 11;
    }

    @Override
    public int maxWidth() {
        return 11;
    }

    @Override
    public int maxHeight() {
        return 11;
    }

    public Class<?extends WellWater> overrideWater = null;

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        Point c = center();
        Painter.set( level, c.x, c.y, Terrain.WELL );

        OilWellMaker vis = new OilWellMaker();
        vis.pos(c.x-2, c.y-2);
        level.customTiles.add(vis);

        @SuppressWarnings("unchecked")
        Class<? extends WellWater> waterClass =
                overrideWater != null ?
                        overrideWater :
                        (Class<? extends WellWater>) Random.element( WATERS );


        WellWater.seed(c.x + level.width() * c.y, 1, waterClass, level);

        entrance().set( Door.Type.REGULAR );
    }


    public static class OilWellMaker extends CustomTilemap {

        {
            texture = Assets.Environment.OilWell;
            tileW = tileH = 5;

        }

        final int TEX_WIDTH = 80;

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(mapSimpleImage(0, 0, TEX_WIDTH), 5);
            return v;
        }

        @Override
        public String name(int tileX, int tileY) {
            return Messages.get(this, "name");
        }

        @Override
        public String desc(int tileX, int tileY) {
            return Messages.get(this, "desc");
        }
    }

}

