package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Albino;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CausticSlime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ClearElemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Salamander;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.MagicalFireRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
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
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class GooRoom extends SpecialRoom {

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
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.WATER );
        Painter.fill( level, this, 2, Terrain.EMPTY_SP );


        Point c = center();
        int cx = c.x;
        int cy = c.y;

        for (Door door : connected.values()) {
            door.set( Door.Type.CRYSTAL );
        }

        level.addItemToSpawn( new CrystalKey( Dungeon.depth ) );

        level.addItemToSpawn( new CrystalKey( Dungeon.depth ) );
        level.addItemToSpawn( new PotionOfMindVision());

        for(Point p : getPoints()) {
            int cell = level.pointToCell(p);
            if (level.map[cell] == Terrain.TRAP){
                Blob.seed(level.pointToCell(p), 1, MagicalFireRoom.EternalFire.class, level);
            }
        }

        setupGooNest(level);

        int pillarW = (width()-8)/2;
        int pillarH = (height()-8)/2;

        Painter.fill(level, left+3, top+3, pillarW+1, pillarH+1, Terrain.WALL);
        Painter.fill(level, left+3, bottom-3-pillarH, pillarW+1, pillarH+1, Terrain.WALL);
        Painter.fill(level, right-3-pillarW, top+3, pillarW+1, pillarH+1, Terrain.WALL);
        Painter.fill(level, right-3-pillarW, bottom-3-pillarH, pillarW+1, pillarH+1, Terrain.WALL);

        Painter.drawCircle(level, c, 7, Terrain.WATER);
        Painter.drawCircle(level, c, 5, Terrain.EMPTY);
        Painter.drawCircle(level, c, 3, Terrain.WATER);
        Painter.drawCircle(level, c, 2, Terrain.EMPTY_DECO);
        Painter.drawCircle(level, c, 0, Terrain.WATER);

        Painter.set(level, cx, cy - 4, Terrain.STATUE);

        Painter.set(level, cx+4, cy,Terrain.STATUE);
        Painter.set(level, cx-4, cy, Terrain.STATUE);

        Painter.set(level, cx, cy + 4, Terrain.STATUE);

        Painter.set(level, cx, cy - 7, Terrain.WALL);

        Painter.set(level, cx+7, cy,Terrain.WALL);

        Painter.set(level, cx-7, cy, Terrain.WALL);

        Painter.set(level, cx, cy + 7, Terrain.WALL);

        switch (Random.Int(4)){
            case 0:
            Painter.set(level, cx, cy - 7, Terrain.CRYSTAL_DOOR);
            break;
            case 1:
            Painter.set(level, cx+7, cy,Terrain.CRYSTAL_DOOR);
            break;
            case 2:
            Painter.set(level, cx-7, cy, Terrain.CRYSTAL_DOOR);
            break;
            case 3:
            Painter.set(level, cx, cy + 7, Terrain.CRYSTAL_DOOR);
            break;
        }

        int entrancePos = cx + cy * level.width();

//        GooMob statue = new GooMob();
//        statue.pos = cx + cy * level.width();
//        statue.state = statue.PASSIVE;
//        level.mobs.add( statue );

        level.transitions.add(new LevelTransition(level,
                entrancePos,
                LevelTransition.Type.BRANCH_EXIT,
                Dungeon.depth,
                Dungeon.branch + 2,
                LevelTransition.Type.BRANCH_ENTRANCE));
        Painter.set(level, entrancePos, Terrain.EXIT);



        Albino statue2 = new Albino();
        statue2.HT = statue2.HP = statue2.HT*2;
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

        CausticSlime statue5 = new CausticSlime();
        statue5.properties.add(Char.Property.IMMOVABLE);
        statue5.HT = statue5.HP = statue5.HT * 2;
        statue5.pos = (cx) + (cy+5) * level.width();
        level.mobs.add( statue5 );
    }

    protected void setupGooNest( Level level ){
        GooRoom.GooNest nest = new GooRoom.GooNest();
        nest.setRect(left + width()/2 - 2, top + height()/2 - 2, 4 + width()%2, 4 + height()%2);

        level.customTiles.add(nest);
    }

    public static class GooNest extends CustomTilemap {

        {
            texture = Assets.Environment.SEWER_BOSS;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            int[] data = new int[tileW*tileH];
            for (int x = 0; x < tileW; x++){
                for (int y = 0; y < tileH; y++){

                    //corners
                    if ((x == 0 || x == tileW-1) && (y == 0 || y == tileH-1)){
                        data[x + tileW*y] = -1;

                        //adjacent to corners
                    } else if ((x == 1 && y == 0) || (x == 0 && y == 1)){
                        data[x + tileW*y] = 0;

                    } else if ((x == tileW-2 && y == 0) || (x == tileW-1 && y == 1)){
                        data[x + tileW*y] = 1;

                    } else if ((x == 1 && y == tileH-1) || (x == 0 && y == tileH-2)){
                        data[x + tileW*y] = 2;

                    } else if ((x == tileW-2 && y == tileH-1) || (x == tileW-1 && y == tileH-2)) {
                        data[x + tileW*y] = 3;

                        //sides
                    } else if (x == 0){
                        data[x + tileW*y] = 4;

                    } else if (y == 0){
                        data[x + tileW*y] = 5;

                    } else if (x == tileW-1){
                        data[x + tileW*y] = 6;

                    } else if (y == tileH-1){
                        data[x + tileW*y] = 7;

                        //inside
                    } else {
                        data[x + tileW*y] = 8;
                    }

                }
            }
            v.map( data, tileW );
            return v;
        }

        @Override
        public Image image(int tileX, int tileY) {
            return null;
        }
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