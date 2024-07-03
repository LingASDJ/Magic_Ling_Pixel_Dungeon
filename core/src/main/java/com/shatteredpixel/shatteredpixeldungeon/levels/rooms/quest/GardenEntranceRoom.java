package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.GuidePage;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class GardenEntranceRoom extends EntranceRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 5);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 5);
    }

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

    @Override
    public boolean canPlaceTrap(Point p) {
        if (Dungeon.depth == 1) {
            return false;
        } else {
            return super.canPlaceTrap(p);
        }
    }

    public void paint(Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        for (Room.Door door : connected.values()) {
            door.set( Room.Door.Type.REGULAR );
        }

        int entrance;
        do {
            entrance = level.pointToCell(random(2));
        } while (level.findMob(entrance) != null);
        Painter.set( level, entrance, Terrain.EMPTY );

        if (Dungeon.depth == 1){
            level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.SURFACE));
        } else {
            level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
        }

        //use a separate generator here so meta progression doesn't affect levelgen
        Random.pushGenerator();



        //places the third guidebook page on floor 2
        if (Dungeon.depth == 2 && !Document.ADVENTURERS_GUIDE.isPageFound(Document.GUIDE_SEARCHING)){
            int pos;
            do {
                //can't be on bottom row of tiles
                pos = level.pointToCell(new Point( Random.IntRange( left + 1, right - 1 ),
                        Random.IntRange( top + 1, bottom - 2 )));
            } while (pos == level.entrance() || level.findMob(level.entrance()) != null);
            GuidePage p = new GuidePage();
            p.page(Document.GUIDE_SEARCHING);
            level.drop( p, entrance );
        }

        Random.popGenerator();

    }

}
