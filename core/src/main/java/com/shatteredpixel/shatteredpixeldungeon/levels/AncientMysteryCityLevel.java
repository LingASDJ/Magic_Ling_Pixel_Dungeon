package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DragonGirlBlue;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.JunglePainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.BridgeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.PerimeterRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.WalkwayRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.AncientMysteryEnteanceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.AncientMysteryExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretWellRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.LibraryRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.TrapsRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.PlantsRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.SewerPipeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DragonGirlBlueSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AncientMysteryCityLevel extends RegularLevel {

    @Override
    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms = new ArrayList<>();
        initRooms.add ( roomEntrance = new AncientMysteryEnteanceRoom());
        initRooms.add ( roomExit = new AncientMysteryExitRoom());

        //spawns 1 giant, 3 large, 6-8 small, and 1-2 secret cave rooms
        StandardRoom s;
        s = new PlantsRoom();
        s.setSizeCat();
        initRooms.add(s);

        int rooms = 2;
        for (int i = 0; i < rooms; i++){
            s = new SewerPipeRoom();
            s.setSizeCat();
            initRooms.add(s);
        }

        SpecialRoom x;
        rooms = Random.NormalIntRange(1, 2);
        for (int i = 0; i < rooms; i++){
            x = new TrapsRoom();
            initRooms.add(x);
        }

        int rooms2 = 2;
        for (int i = 1; i < rooms2; i++){
            x = new LibraryRoom();
            initRooms.add(x);
        }

        ConnectionRoom xs;
        rooms = Random.NormalIntRange(2, 4);
        for (int i = 0; i < rooms; i++){
            xs = new BridgeRoom();
            initRooms.add(xs);
        }
        for (int i = 1; i < rooms; i++){
            xs = new PerimeterRoom();
            initRooms.add(xs);
        }
        for (int i = 2; i < rooms; i++){
            xs = new WalkwayRoom();
            initRooms.add(xs);
        }

        rooms = 2;
        for (int i = 0; i < rooms; i++){
            initRooms.add(new SecretWellRoom());
        }

        return initRooms;
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_ENTRANCE) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions( new DragonGirlBlueSprite(),
                            Messages.titleCase(Messages.get(DragonGirlBlue.class, "name")),
                            Messages.get(DragonGirlBlue.class, "exit_warn_none"),
                            Messages.get(DragonGirlBlue.class, "exit_yes"),
                            Messages.get(DragonGirlBlue.class, "exit_no")){
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){
                                GameScene.show(new WndOptions( new DragonGirlBlueSprite(),
                                        Messages.titleCase(Messages.get(DragonGirlBlue.class, "name")),
                                        Messages.get(DragonGirlBlue.class, "exit_warn_none"),
                                        Messages.get(DragonGirlBlue.class, "exit_yes_2"),
                                        Messages.get(DragonGirlBlue.class, "exit_no_2")){
                                    @Override
                                    protected void onSelect(int index) {
                                        if (index == 0){
                                            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                                            if (timeFreeze != null) timeFreeze.disarmPresses();
                                            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                                            if (timeBubble != null) timeBubble.disarmPresses();
                                            InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                                            InterlevelScene.curTransition = new LevelTransition();
                                            InterlevelScene.curTransition.destDepth = depth;
                                            InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_EXIT;
                                            InterlevelScene.curTransition.destBranch = 0;
                                            InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_EXIT;
                                            InterlevelScene.curTransition.centerCell  = -1;
                                            Game.switchScene( InterlevelScene.class );
                                            Dungeon.anCityQuest2Progress = true;
                                        }
                                    }
                                } );
                            } else if(Dungeon.branch!=1){
                                TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                                if (timeFreeze != null) timeFreeze.disarmPresses();
                                Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                                if (timeBubble != null) timeBubble.disarmPresses();
                                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                                InterlevelScene.curTransition = new LevelTransition();
                                InterlevelScene.curTransition.destDepth = depth;
                                InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_EXIT;
                                InterlevelScene.curTransition.destBranch = Dungeon.branch-1;
                                InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_EXIT;
                                InterlevelScene.curTransition.centerCell  = -1;
                                Game.switchScene( InterlevelScene.class );
                            }
                        }
                    } );
                }
            });
            return false;
        } else {
            return super.activateTransition(hero, transition);
        }
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{
                FrostTrap.class, StormTrap.class, CorrosionTrap.class, BlazingTrap.class, DisintegrationTrap.class,
                RockfallTrap.class, FlashingTrap.class, GuardianTrap.class, WeakeningTrap.class,
                DisarmingTrap.class, SummoningTrap.class, WarpingTrap.class, CursingTrap.class, DistortionTrap.class, GatewayTrap.class, GeyserTrap.class };
    }

    @Override
    protected Painter painter() {
        return new JunglePainter()
                .setWater(0.9f, 9)
                .setGrass(0.9f, 9)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_ANCIENT;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_ANCIENT;
    }

}