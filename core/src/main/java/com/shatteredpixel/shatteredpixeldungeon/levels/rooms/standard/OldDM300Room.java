package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM201;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OldDM300;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class OldDM300Room extends StandardRoom {

    {
        //noMobs = true;
    }

    @Override
    public int minWidth() { return 39; }
    @Override
    public int minHeight() {
        return 39; }
    @Override
    public int maxWidth() { return 39; }
    @Override
    public int maxHeight() { return 39; }
    @Override
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        return false;
    }

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMBERS );
        Painter.fill( level, this, 2, Terrain.WATER );

        Point c = center();
        int cx = c.x;
        int cy = c.y;

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
        }

        Painter.drawCircle(level, c, 17, Terrain.HIGH_GRASS);
        Painter.drawCircle(level, c, 16, Terrain.TRAP);
        Painter.drawCircle(level, c, 15, Terrain.HIGH_GRASS);
        Painter.drawCircle(level, c, 14, Terrain.TRAP);
        Painter.drawCircle(level, c, 13, Terrain.EMPTY);
        Painter.drawCircle(level, c, 7, Terrain.EMPTY);
        Painter.drawCircle(level, c, 5, Terrain.WATER);
        Painter.drawCircle(level, c, 2, Terrain.EMPTY);

        for(Point p : getPoints()) {
            int cell = level.pointToCell(p);
            if (level.map[cell] == Terrain.TRAP){
                Trap trap = Reflection.newInstance(trapClasses()[Random.chances( trapChances() )]).reveal();
                level.setTrap( trap, cell );
            }
        }

        OldDM300 statue = new OldDM300();
        statue.pos = cx + cy * level.width();
        level.mobs.add( statue );

        DM201 statue2 = new DM201();
        statue2.pos = (cx-5) + cy * level.width();
        statue2.properties.add(Char.Property.IMMOVABLE);
        level.mobs.add( statue2 );

        DM201 statue3 = new DM201();
        statue3.properties.add(Char.Property.IMMOVABLE);
        statue3.pos = (cx+5) + cy * level.width();
        level.mobs.add( statue3 );

        DM201 statue4 = new DM201();
        statue4.properties.add(Char.Property.IMMOVABLE);
        statue4.pos = (cx) + (cy-5) * level.width();
        level.mobs.add( statue4 );

        DM201 statue5 = new DM201();
        statue5.properties.add(Char.Property.IMMOVABLE);
        statue5.pos = (cx) + (cy+5) * level.width();
        level.mobs.add( statue5 );

    }

    protected Class<? extends Trap>[] trapClasses() {
        return new Class[]{
                FrostTrap.class, StormTrap.class,BlazingTrap.class,
                RockfallTrap.class,CursingTrap.class };
    }


    protected float[] trapChances() {
        return new float[]{
                1, 2, 3, 2, 3,
        };
    }
}
