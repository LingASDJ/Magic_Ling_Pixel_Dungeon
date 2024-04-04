package com.shatteredpixel.shatteredpixeldungeon.levels.minilevels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.JunglePainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.CaveEnteanceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.CaveExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.RIPSwordRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Group;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DragonCaveLevel extends RegularLevel {
    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 6;
        //4 to 6, average 5
        return 4+ Random.chances(new float[]{1, 3, 1});
    }

    @Override
    protected void createItems() {
        super.createItems();
        addItemToSpawn(Generator.random(Generator.Category.FOOD));
        addItemToSpawn(Generator.random(Generator.Category.STONE));
        addItemToSpawn(Generator.random(Generator.Category.SCROLL));
        addItemToSpawn(Generator.random(Generator.Category.SCROLL));
        addItemToSpawn(Generator.random(Generator.Category.POTION));
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 2;
        //1 to 2, average 1.8
        return 1+Random.chances(new float[]{1, 4});
    }
    @Override
    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms = new ArrayList<>();

        initRooms.add( roomEntrance = new CaveEnteanceRoom());
        initRooms.add( roomExit     = new CaveExitRoom() );

        int standards = standardRooms(true);
        for (int i = 0; i < standards; i++) {
            StandardRoom s = StandardRoom.createRoom();
            //force to normal size
            s.setSizeCat(1, 1);
            initRooms.add(s);
        }

        int rooms;
        SpecialRoom x;
        rooms = Random.NormalIntRange(1, 2);
        for (int i = 0; i < rooms; i++){
            x = new RIPSwordRoom();
            initRooms.add(x);
        }

        return initRooms;
    }

    protected int nTraps() {
        return 40;
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        HallsLevel.addHallsVisuals( this, visuals );
        return visuals;
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{
                FrostTrap.class, StormTrap.class, WeakeningTrap.class, BlazingTrap.class,
        DisintegrationTrap.class};
    }

    @Override
    protected Painter painter() {
        return new JunglePainter()
                .setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 5)
                .setGrass(feeling == Feeling.GRASS ? 0.80f : 0.20f, 4)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    @Override
    protected float[] trapChances() {
        return new float[]{
               7, 2, 3, 6, 9};
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_FIRE;
    }


    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_ENTRANCE && Dungeon.branch == 1) {
             return false;
        } else {
            return super.activateTransition(hero,transition);
        }
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.STATUE:case Terrain.STATUE_SP:
                return Messages.get(DragonCaveLevel.class, "statue_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(DragonCaveLevel.class, "statue_desc");
            default:
                return super.tileDesc( tile );
        }
    }


    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

}