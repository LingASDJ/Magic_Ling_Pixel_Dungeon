package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.CavesPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.tomb.DeadTowerRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.CorpseDustTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.DeadDoorTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.DeadSoulTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.InjectSoulTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.LegionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.MobSpawnTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class LostTombLevel extends RegularLevel {

    @Override
    protected void createItems() {
        //RedDragon.Quest.spawn(this);
        super.createItems();
    }

    {
        color1 = 0x002E1A;
        color2 = 0x285D1A;
        extraGlass = false;
    }


    //TODO 等待音乐到达
    @Override
    public void playLevelMusic() {
        Music.playModeBGM(Assets.Music.TOMB_CACHE, true);
    }

    @Override
    protected void createMobs() {
//        if(Dungeon.depth == 14 && Dungeon.branch == 0 && Statistics.gdzHelpDungeon == 3){
//            Gudazi npc20 = new Gudazi();
//            npc20.pos = entrance()-1;
//            mobs.add(npc20);
//        }
//
//        if(Dungeon.depth == 11 && Statistics.RandMode){
//            drop(new TengusMask(), entrance()-1);
//        }

        super.createMobs();
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 7;
        //6 to 7, average 6.333
        return 6+ Random.chances(new float[]{2, 1});
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 3;
        //2 to 3, average 2.2
        return 2+Random.chances(new float[]{4, 1});
    }

    @Override
    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms = super.initRooms();

        initRooms.add(new DeadTowerRoom());

        return initRooms;
    }

    @Override
    protected Painter painter() {
        return new CavesPainter()
                .setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 6)
                .setGrass(feeling == Feeling.GRASS ? 0.65f : 0.15f, 3)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        return super.activateTransition(hero, transition);
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_TOMB;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_TOMB;
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{
                InjectSoulTrap.class, InjectSoulTrap.class, MobSpawnTrap.class,
                DeadDoorTrap.class, CorpseDustTrap.class,
                LegionTrap.class, LegionTrap.class,  CorpseDustTrap.class,
                MobSpawnTrap.class,MobSpawnTrap.class, DisarmingTrap.class, PitfallTrap.class, DeadSoulTrap.class, DeadSoulTrap.class };
    }

    @Override
    protected float[] trapChances() {
        return new float[]{
                4, 4, 4, 4, 4,
                2, 2, 2,
                1, 1, 1, 1, 1, 1 };
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(LostTombLevel.class, "statue_name");
            case Terrain.GRASS:
                return Messages.get(LostTombLevel.class, "grass_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(LostTombLevel.class, "high_grass_name");
            case Terrain.WATER:
                return Messages.get(LostTombLevel.class, "water_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc( int tile ) {
        switch (tile) {
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(LostTombLevel.class, "statue_desc");
            case Terrain.ENTRANCE:
            case Terrain.ENTRANCE_SP:
                return Messages.get(LostTombLevel.class, "entrance_desc");
            case Terrain.EXIT:
                return Messages.get(LostTombLevel.class, "exit_desc");
            case Terrain.HIGH_GRASS:
                return Messages.get(LostTombLevel.class, "high_grass_desc");
            case Terrain.WALL_DECO:
                return Messages.get(LostTombLevel.class, "wall_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(LostTombLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }

//    @Override
//    public Group addVisuals() {
//        super.addVisuals();
//        addCavesVisuals( this, visuals );
//        return visuals;
//    }
//
//    public static void addCavesVisuals( Level level, Group group ) {
//        addCavesVisuals(level, group, false);
//    }
}
