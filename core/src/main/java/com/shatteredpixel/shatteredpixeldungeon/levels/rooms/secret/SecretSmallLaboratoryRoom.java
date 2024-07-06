package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter.fill;

import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class SecretSmallLaboratoryRoom extends SecretRoom {

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

    @Override
    public boolean canConnect(Point p) {
        //refuses connections next to corners
        return super.canConnect(p) && ((p.x > left+1 && p.x < right-1) || (p.y > top+1 && p.y < bottom-1));
    }

    @Override
    public Rect resize(int w, int h) {
        super.resize(w, h);
        return this;
    }

    @Override
    public int minWidth() {
        return 7;
    }

    @Override
    public int minHeight() {
        return 7;
    }

    @Override
    public int maxWidth() {
        return 7;
    }

    @Override
    public int maxHeight() {
        return 7;
    }

    @Override
    public void paint(Level level) {

        int centerX = left + width() / 2;
        int centerY = top + height() / 2;

        fill(level, this, Terrain.WALL);
        fill(level, this,1, Terrain.WATER);
        Painter.drawRectangle(level, new Point(centerX, centerY), width()-2, height()-2, EMPTY_SP,true,STATUE_SP);

        Painter.set(level,centerX,centerY,Terrain.ALCHEMY);

        entrance().set( Door.Type.HIDDEN );

        Point e = new Point(centerX, centerY-1);
        Point x = new Point(centerX, centerY+1);
        Point j = new Point(centerX-1, centerY);
        Point s = new Point(centerX+1, centerY);

        ArrayList<Integer> places = new ArrayList<>();

        places.add((left + right) - e.x + e.y * level.width());
        places.add((left + right) - x.x + x.y * level.width());

        int pos = Random.element(places);
        level.drop( new EnergyCrystal().random(), pos );

        int book = (left + right) - j.x + j.y * level.width();
        int srol = (left + right) - s.x + s.y * level.width();

        level.drop(prize(), book);
        level.drop(prize(), srol);
    }



    private static Item prize( ) {
        return Generator.random( Random.oneOf(
                Generator.Category.POTION,
                Generator.Category.STONE
        ) );
    }
}
