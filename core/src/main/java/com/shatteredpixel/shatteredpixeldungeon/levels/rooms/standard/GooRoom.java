package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ClearElemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Guard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Salamander;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.GooMob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.MagicalFireRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.watabou.utils.Point;

public class GooRoom extends StandardRoom {

    {
        //noMobs = true;
    }

    @Override
    public int minWidth() { return 21; }
    @Override
    public int minHeight() {
        return 21;
    }
    @Override
    public int maxWidth() { return 21; }
    @Override
    public int maxHeight() { return 21; }
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

        for(Point p : getPoints()) {
            int cell = level.pointToCell(p);
            if (level.map[cell] == Terrain.TRAP){
                Blob.seed(level.pointToCell(p), 1, MagicalFireRoom.EternalFire.class, level);
            }
        }

        Painter.drawCircle(level, c, 7, Terrain.WATER);
        Painter.drawCircle(level, c, 5, Terrain.EMPTY_SP);
        Painter.drawCircle(level, c, 2, Terrain.WATER);

        GooMob statue = new GooMob();
        statue.pos = cx + cy * level.width();
        level.mobs.add( statue );

        Guard statue2 = new Guard();
        statue2.HT = statue2.HP = statue2.HT * 3;
        statue2.pos = (cx-5) + cy * level.width();
        statue2.properties.add(Char.Property.IMMOVABLE);
        level.mobs.add( statue2 );

        Salamander statue3 = new Salamander();
        statue3.HT = statue3.HP = statue3.HT * 2;
        statue3.properties.add(Char.Property.IMMOVABLE);
        statue3.pos = (cx+5) + cy * level.width();
        level.mobs.add( statue3 );

        ClearElemental statue4 = new ClearElemental();
        statue4.HT = statue4.HP = statue4.HT * 3;
        statue4.properties.add(Char.Property.IMMOVABLE);
        statue4.pos = (cx) + (cy-5) * level.width();
        level.mobs.add( statue4 );

        DM100 statue5 = new DM100();
        statue5.properties.add(Char.Property.IMMOVABLE);
        statue5.HT = statue5.HP = statue5.HT * 2;
        statue5.pos = (cx) + (cy+5) * level.width();
        level.mobs.add( statue5 );
    }

    protected Class<? extends Trap>[] trapClasses() {
        return new Class[]{
                FrostTrap.class, StormTrap.class, CorrosionTrap.class, BlazingTrap.class, DisintegrationTrap.class,
                RockfallTrap.class, FlashingTrap.class, GuardianTrap.class, WeakeningTrap.class,
                DisarmingTrap.class, SummoningTrap.class, WarpingTrap.class, CursingTrap.class, PitfallTrap.class, DistortionTrap.class };
    }


    protected float[] trapChances() {
        return new float[]{
                4, 4, 4, 4, 4,
                2, 2, 2, 2,
                1, 1, 1, 1, 1, 1 };
    }
}