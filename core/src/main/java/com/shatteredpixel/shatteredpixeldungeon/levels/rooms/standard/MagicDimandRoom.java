package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.DimandBook;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;

public class MagicDimandRoom extends StandardRoom{


    @Override
    public int minWidth() {
        return 8;
    }

    @Override
    public int minHeight() {
        return 8;
    }


    public void paint( Level level ) {

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
        }

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY_SP);

        ChestMarker vis = new ChestMarker();
        Point c = center();
        vis.pos(c.x - 1, c.y - 1);
        int pos = level.pointToCell(center());

        level.drop( new DimandBook(),pos );

        level.customTiles.add(vis);

        Painter.fill(level, c.x-1, c.y-1, 3, 3, Terrain.EMPTY_SP);

//        level.addItemToSpawn(new CeremonialCandle());
//        level.addItemToSpawn(new CeremonialCandle());
//        level.addItemToSpawn(new CeremonialCandle());
//        level.addItemToSpawn(new CeremonialCandle());

        //CeremonialCandle.ritualPos = c.x + (level.width() * c.y);
    }

    public static class ChestMarker extends CustomTilemap {

        {
            texture = Assets.Environment.PRISON_MAGIC;
            tileW = tileH = 4;

        }

        final int TEX_WIDTH = 64;

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(mapSimpleImage(0, 0, TEX_WIDTH), 4);
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
