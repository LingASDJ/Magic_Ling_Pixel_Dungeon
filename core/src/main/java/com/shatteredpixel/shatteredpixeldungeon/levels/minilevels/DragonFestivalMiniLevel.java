package com.shatteredpixel.shatteredpixeldungeon.levels.minilevels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.MiningLevelPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.BigFishBossRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.MineEntrance;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.MineLargeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.NukeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DragonFestivalMiniLevel extends CavesLevel {
    @Override
    protected void createItems() {
        Random.pushGenerator(Random.Long());
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            int cell = randomDropCell();
            if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
                map[cell] = Terrain.GRASS;
                losBlocking[cell] = false;
            }
            for (Item i : bonesItems) {
                drop(i, cell).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();

        int cell = randomDropCell();
        if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
            map[cell] = Terrain.GRASS;
            losBlocking[cell] = false;
        }
        drop( Generator.randomUsingDefaults(Generator.Category.FOOD), cell );
        if (Blacksmith.Quest.Type() == Blacksmith.Quest.GNOLL){
            //drop a second ration for the gnoll quest type, more mining required!
            cell = randomDropCell();
            if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
                map[cell] = Terrain.GRASS;
                losBlocking[cell] = false;
            }
            drop( Generator.randomUsingDefaults(Generator.Category.FOOD), cell );
        }

        if (Dungeon.isChallenged(Challenges.DARKNESS)){
            cell = randomDropCell();
            if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
                map[cell] = Terrain.GRASS;
                losBlocking[cell] = false;
            }
            drop( new Torch(), cell );
        }
    }
    @Override
    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms = new ArrayList<>();
        initRooms.add ( roomEntrance = new MineEntrance());

        //spawns 1 giant, 3 large, 6-8 small, and 1-2 secret cave rooms
        StandardRoom s;
        s = new NukeRoom();
        s.setSizeCat();
        initRooms.add(s);

        int rooms = 3;
        for (int i = 0; i < rooms; i++){
            s = new MineLargeRoom();
            s.setSizeCat();
            initRooms.add(s);
        }

        SpecialRoom s1;
        s1 = new BigFishBossRoom();
        initRooms.add(s1);

        return initRooms;
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_ENTRANCE) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions( new BlacksmithSprite(),
                            Messages.titleCase(Messages.get(Blacksmith.class, "name")),
                            Messages.get(Blacksmith.class, "exit_warn_none"),
                            Messages.get(Blacksmith.class, "exit_yes"),
                            Messages.get(Blacksmith.class, "exit_no")){
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
                                Blacksmith.Quest.complete();
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
    protected Painter painter() {
        return new MiningLevelPainter()
                .setGold(Random.NormalIntRange(42, 46))
                .setWater(0.4f, 0)
                .setGrass(0.6f, 1);
    }
    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_COLD_MINE;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CAVES;
    }

}