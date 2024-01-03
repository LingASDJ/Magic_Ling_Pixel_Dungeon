package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.HollowMimic;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.HollowPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
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
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class HollowLevel extends RegularLevel {

    {
        color1 = 0x6a723d;
        color2 = 0x88924c;
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE && depth == 26) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                    if (timeFreeze != null) timeFreeze.disarmPresses();
                    Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                    if (timeBubble != null) timeBubble.disarmPresses();
                    InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                    InterlevelScene.curTransition = new LevelTransition();
                    InterlevelScene.curTransition.destDepth = depth - 1;
                    InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
                    InterlevelScene.curTransition.destBranch = 4;
                    InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                    InterlevelScene.curTransition.centerCell  = -1;
                    Game.switchScene( InterlevelScene.class );
                }
            });
            return false;
        } else {
            return super.activateTransition(hero, transition);
        }
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 9;
        //8 to 9, average 8.33
        return 8+ Random.chances(new float[]{2, 1});
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 3;
        //2 to 3, average 2.5
        return 2 + Random.chances(new float[]{1, 1});
    }

    @Override
    protected Painter painter() {
        return new HollowPainter()
                .setWater(feeling == Level.Feeling.WATER ? 0.90f : 0.30f, 2)
                .setGrass(feeling == Level.Feeling.GRASS ? 0.80f : 0.20f, 0)
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
            case Terrain.CUSTOM_DECO:
                return Messages.get(HollowMimic.class, "cspx_name");
            case Terrain.WALL_DECO:
                return Messages.get(HollowMimic.class, "minames");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.WALL_DECO:
                return Messages.get(HollowMimic.class, "midescs");
            case Terrain.CUSTOM_DECO:
                return Messages.get(HollowMimic.class, "cspx_desc");
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
