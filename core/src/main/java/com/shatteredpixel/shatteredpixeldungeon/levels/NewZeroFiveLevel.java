package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.tipsgodungeon;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ALCHEMY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;
import static com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.ready;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GameRules;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nyz;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.BossRushBloodGold;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.DLCItem;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.RushMobScrollOfRandom;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SakaFishSketon;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHardNotification;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;

public class NewZeroFiveLevel extends Level {

    {
        color1 = 5459774;
        color2 = 12179041;
        viewDistance = 100;
    }

    private static final int S = SIGN;
    private static final int W = WALL;
    private static final int E = EMPTY_SP;
    private static final int D = DOOR;
    private static final int X = EXIT;
    private static final int Y = ENTRANCE;
    private static final int A = ALCHEMY;
    private static final int T = WATER;
    private static final int J = CHASM;

    private static final int[] codedMap = {
            W,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,S,S,Y,S,S,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,S,E,E,E,S,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,E,E,E,E,E,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,E,E,E,E,E,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,E,E,E,E,E,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,E,E,E,E,E,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,E,E,E,E,E,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,E,W,W,W,W,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,D,W,W,W,W,W,W,W,W,W,W,W,W,
            W,E,E,E,E,E,W,E,E,E,E,E,E,E,E,S,S,S,W,W,W,W,W,W,W,
            W,E,E,E,E,E,W,S,S,S,S,E,E,E,E,E,E,E,W,S,E,S,S,W,W,
            W,E,E,E,E,E,D,E,E,E,E,E,E,E,E,E,E,E,D,E,E,E,E,W,W,
            W,E,E,E,E,E,W,E,E,S,S,E,E,E,S,S,E,E,W,S,S,S,S,W,W,
            W,E,E,E,E,E,W,E,E,S,S,E,E,E,S,S,E,E,W,W,W,W,W,W,W,
            W,W,W,E,W,W,W,E,E,E,E,E,E,E,E,E,E,E,S,S,W,W,W,W,W,
            W,W,W,D,W,W,W,E,E,S,S,E,E,E,S,S,E,E,S,Y,S,W,W,W,W,
            W,E,E,E,E,E,W,E,E,S,S,E,E,E,S,S,E,E,E,S,S,W,W,W,W,
            W,E,E,E,E,E,D,E,E,E,E,E,E,E,E,E,E,E,W,W,S,S,W,W,W,
            W,E,E,E,E,E,W,E,E,E,E,E,E,E,E,E,E,E,W,S,S,S,S,S,W,
            W,W,E,E,E,W,W,W,W,W,W,E,E,E,W,W,W,W,W,S,S,S,S,S,W,
            W,W,W,W,W,W,S,S,S,S,W,E,E,E,W,S,S,S,S,E,E,E,S,S,S,
            W,W,W,W,W,S,S,S,S,S,W,E,E,E,W,E,E,E,E,E,E,E,E,S,S,
            W,W,W,W,W,S,S,S,S,S,W,W,E,W,W,E,E,E,E,E,E,E,E,S,S,
            W,W,W,W,S,S,E,E,E,S,S,W,D,W,S,E,E,E,E,E,E,E,E,E,S,
            W,W,W,S,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,
            W,W,W,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,
            W,W,W,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,W,W,W,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,A,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,E,E,E,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,W,W,W,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,S,S,
            W,W,W,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,T,T,S,S,
            W,W,W,S,S,S,S,S,S,E,E,S,S,E,E,E,S,T,T,T,T,T,T,S,S,
            W,W,W,S,S,S,S,S,S,S,S,S,S,E,E,E,S,T,T,T,T,T,T,S,S,
            W,W,W,S,S,S,S,S,S,S,S,E,S,E,E,E,S,T,T,T,T,T,S,S,S,
            W,W,W,S,S,S,S,S,S,S,S,X,E,E,E,E,S,S,S,S,S,S,S,S,S,
            W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
    };

    private static final int WIDTH = 25;
    private static final int HEIGHT = 43;


    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        int exitCell = 62;
        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        int exitCellX = 419;
        LevelTransition exitX = new LevelTransition(this, exitCellX, LevelTransition.Type.DOUBLE_ENTRANCE);
        transitions.add(exitX);

        int enterCell = 961;
        LevelTransition enter = new LevelTransition(this, enterCell, LevelTransition.Type.SURFACE);
        transitions.add(enter);

        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);

        map = codedMap.clone();
        return true;
    }

    public static class townAbove extends CustomTilemap {

        {
            texture = Assets.Environment.ZERO_BACK;

            tileW = WIDTH;
            tileH = HEIGHT;
        }

        final int TEX_WIDTH = WIDTH*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    protected void createItems() {

    }

    public Mob createMob() {
        return null;
    }



    protected void createMobs() {

    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_ENTRANCE) {
            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = depth;
            InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_EXIT;
            InterlevelScene.curTransition.destBranch = 1;
            InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_EXIT;
            InterlevelScene.curTransition.centerCell = -1;
            Game.switchScene(InterlevelScene.class);
            return false;
        } else if (transition.type == LevelTransition.Type.SURFACE){

            if (hero.belongings.getItem( Amulet.class ) == null) {
                Game.runOnRenderThread(() -> GameScene.show( new WndMessage( Messages.get(hero, "leave") ) ));
                return false;
            } else {
                Statistics.ascended = true;
                Badges.silentValidateHappyEnd();
                Dungeon.win( Amulet.class );
                Dungeon.deleteGame( GamesInProgress.curSlot, true );
                Game.switchScene( SurfaceScene.class );
                if (hero.belongings.getItem(SakaFishSketon.class) != null) {
                    PaswordBadges.REHOMESKY();
                }
                return true;
            }
        } else if (transition.type == LevelTransition.Type.DOUBLE_ENTRANCE) {

            if (hero.belongings.getItem( DLCItem.class ) == null) {

                Game.runOnRenderThread(() -> GameScene.show( new WndHardNotification(new ItemSprite(ItemSpriteSheet.DLCBOOKS),
                        Messages.get(hero, "dlc_name"),
                        Messages.get(hero, "leave_more_dead"),
                        "OK",
                        0)));
                return false;
            } else if(hero.belongings.getItem( BossRushBloodGold.class ) != null && Statistics.deepestFloor == 0) {
                GameRules.BossRush();
                return false;
            } else if(hero.belongings.getItem( RushMobScrollOfRandom.class ) != null && Statistics.deepestFloor == 0) {
                GameRules.RandMode();
                return false;
            } else {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndMessage(Messages.get(hero, "leave_more_dead")));
                    }
                });
                return false;
            }

        } else if (transition.type == LevelTransition.Type.REGULAR_EXIT) {
            if(!tipsgodungeon) {
                talkToHero();
            } else {
                return super.activateTransition(hero, transition);
            }
            return false;
        } else {
            return super.activateTransition(hero, transition);
        }
    }

    public int randomRespawnCell() {
        return this.entrance - width();
    }

    public String tilesTex() {
        return Assets.Environment.TILES_ZERO_SPRING;
    }

    public String waterTex() {
        return Assets.Environment.WATER_ZERO;
    }

    private void tell(String text) {
        Game.runOnRenderThread(() -> GameScene.show(new WndQuest(new Nyz(), text))
        );
    }

    private void talkToHero(){
        if(!tipsgodungeon) {
            Game.runOnRenderThread(() -> tell(Messages.get(Hero.class, "acsx")));
            ready();
            tipsgodungeon = true;
        }
    }

    @Override
    public void playLevelMusic() {
        Music.playModeBGM(Assets.Music.TOWN, true);
    }
}
