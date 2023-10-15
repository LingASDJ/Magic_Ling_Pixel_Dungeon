package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.PrisonPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HollowLevel extends RegularLevel {

    {
        color1 = 0x6a723d;
        color2 = 0x88924c;
    }

    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.PRISON_1, Assets.Music.PRISON_2, Assets.Music.PRISON_2},
                new float[]{1, 1, 0.5f},
                false);
    }

    @Override
    protected ArrayList<Room> initRooms() {
        return Wandmaker.Quest.spawnRoom(super.initRooms());
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 6;
        //5 to 6, average 5.5
        return 5+ Random.chances(new float[]{1, 1});
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 3;
        //1 to 3, average 2.0
        return 1+Random.chances(new float[]{1, 3, 1});
    }

    @Override
    protected Painter painter() {
        return new PrisonPainter()
                .setWater(feeling == Level.Feeling.WATER ? 0.90f : 0.30f, 4)
                .setGrass(feeling == Level.Feeling.GRASS ? 0.80f : 0.20f, 3)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_HOLLOW;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HOLLOW;
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{
                ChillingTrap.class, ShockingTrap.class, ToxicTrap.class, BurningTrap.class, PoisonDartTrap.class,
                AlarmTrap.class, OozeTrap.class, GrippingTrap.class,
                ConfusionTrap.class, FlockTrap.class, SummoningTrap.class, TeleportationTrap.class, GatewayTrap.class, GeyserTrap.class };
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
            case Terrain.WATER:
                return Messages.get(PrisonLevel.class, "water_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.EMPTY_DECO:
                return Messages.get(PrisonLevel.class, "empty_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(PrisonLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addPrisonVisuals(this, visuals);
        return visuals;
    }

    public static void addPrisonVisuals(Level level, Group group){
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add( new PumpLanter( i ) );
            }
        }
    }

    public static class PumpLanter extends Emitter {

        private int pos;

        public PumpLanter( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );
//            for (int i=0; i){
//
//            }
//            pos( p.x - 4, p.y + 2, 2, 0 );
//
//            pour( FlameParticle.FACTORY, 0.15f );

            add( new Halo( 12, 0xFFa500, 0.3f ).point( p.x, p.y + 1 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }
}
