package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Red;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.JunglePainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.LibraryRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CellBlockRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.FissureRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.GrassyGraveRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.MinefieldRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.WaterBridgeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GardenLevel extends RegularLevel {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }


    @Override
    public void playLevelMusic(){
        if (Ghost.Quest.active()){
            Music.playModeBGM(Assets.Music.BGM_SHOP, true);
        } else {
            Music.playModeBGM(Assets.Music.BGM_1A,true);
        }
    }

    @Override
    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms =    new ArrayList<>();
        initRooms.add ( roomEntrance = new EntranceRoom());
        initRooms.add ( roomExit =     new EmptyRoom());

        StandardRoom s;
        s = new FissureRoom();
        s.setSizeCat();
        initRooms.add(s);

        int rooms = Random.Int(2);
        for (int i = 0; i < rooms; i++){
            WaterBridgeRoom sd = new WaterBridgeRoom();
            initRooms.add(sd);
        }

        SpecialRoom sx;
        sx = new LibraryRoom();
        initRooms.add(sx);

        StandardRoom x;
        x = new GrassyGraveRoom();
        initRooms.add(x);

        int rooms2 = 2;
        for (int i = 1; i < rooms2; i++){
            s = new CellBlockRoom();
            initRooms.add(s);
        }

        int rooms3 = 4;
        for (int i = 1; i < rooms3; i++){
            s = new MinefieldRoom();
            initRooms.add(s);
        }

        return initRooms;
    }

    @Override
    protected Painter painter() {
        return new JunglePainter()
                .setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 5)
                .setGrass(feeling == Feeling.GRASS ? 6.80f : 0.20f, 4)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_GARDEN;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    protected Class<?>[] trapClasses() {
        return Dungeon.depth == 1 ?
                new Class<?>[]{ WornDartTrap.class } :
                new Class<?>[]{
                        ChillingTrap.class, ShockingTrap.class, ToxicTrap.class, WornDartTrap.class,
                        AlarmTrap.class, OozeTrap.class,
                        ConfusionTrap.class, FlockTrap.class, SummoningTrap.class, TeleportationTrap.class, GatewayTrap.class };
    }

    @Override
    protected float[] trapChances() {
        return Dungeon.depth == 1 ?
                new float[]{1} :
                new float[]{
                        4, 4, 4, 4,
                        2, 2,
                        1, 1, 1, 1, 1};
    }

    @Override
    protected void createMobs() {
        super.createMobs();
    }

    @Override
    protected void createItems() {
        addItemToSpawn( new Red() );
        super.createItems();
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        return false;
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(SewerLevel.class, "water_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.EMPTY_DECO:
                return Messages.get(SewerLevel.class, "empty_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(SewerLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }
}
