/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.tipsgodungeon;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMBERS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.MINE_CRYSTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;
import static com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.ready;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GameRules;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nyz;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.BzmdrLand;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WaloKe;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.YetYog;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.BzmdrNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.SliceDream;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.BossRushBloodGold;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.DLCItem;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.RushMobScrollOfRandom;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SakaFishSketon;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHardNotification;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.List;

public class ZeroLevel extends Level {

    {
        color1 = 5459774;
        color2 = 12179041;
        viewDistance = SPDSettings.intro() ? 4 : 15;
    }

    private static final int S = CHASM;
    private static final int W = WALL;
    private static final int K = EMPTY;
    private static final int D = EMPTY_SP;
    private static final int M = WATER;
    private static final int P = MINE_CRYSTAL;
    private static final int T = EMBERS;

    private static final int B = DOOR;
    private static final int N = EXIT;
    private static final int X = ENTRANCE;

    private static final int[] codedMap = {
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,W,W,W,W,W,W,K,K,K,D,K,K,K,K,W,W,W,W,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,W,W,W,W,W,K,K,K,K,D,X,D,K,S,K,K,W,W,W,W,W,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,W,W,W,K,K,K,K,D,K,D,K,D,S,K,K,K,W,W,W,W,W,S,S,S,S,S,S,S,
            S,S,S,S,S,S,W,W,W,W,W,K,S,S,S,K,K,K,D,K,K,S,K,K,K,K,W,W,W,W,W,S,S,S,S,S,S,
            S,S,S,S,S,W,W,W,W,W,K,K,K,P,K,K,K,K,D,K,K,S,K,K,K,K,K,W,W,W,W,W,S,S,S,S,S,
            S,S,S,S,W,W,W,W,W,S,K,K,K,K,K,K,K,K,D,K,K,S,K,K,K,K,K,K,W,W,W,W,W,S,S,S,S,
            S,S,S,W,W,W,W,W,S,S,D,D,D,D,D,K,K,K,D,K,K,K,K,S,K,P,K,K,K,W,W,W,W,W,S,S,S,
            S,S,W,W,W,W,W,S,S,T,M,M,M,M,M,T,K,K,D,K,K,K,K,S,K,K,K,K,K,K,W,W,W,W,W,S,S,
            S,S,W,W,W,W,S,S,D,M,M,M,D,M,M,M,D,K,D,K,K,K,K,S,K,K,K,K,K,S,K,W,W,W,W,S,S,
            S,W,W,W,W,K,P,K,D,M,M,M,D,M,S,M,D,K,D,K,K,K,K,S,S,S,S,K,K,S,S,K,W,W,W,W,S,
            S,W,W,W,K,K,K,K,D,M,P,M,D,M,S,M,D,K,D,K,K,P,K,K,K,K,K,K,K,K,S,S,K,W,W,W,S,
            S,W,W,K,K,K,K,K,D,M,D,D,D,M,S,M,D,K,D,K,K,K,K,K,K,K,K,P,K,K,K,S,S,K,W,W,S,
            W,W,W,W,W,W,W,K,D,M,M,M,M,M,M,M,D,K,D,K,K,K,K,K,K,K,K,K,K,K,W,W,W,W,W,W,W,
            W,K,K,K,K,K,W,K,K,T,M,M,M,M,M,T,K,K,D,K,K,K,S,S,S,K,K,K,K,K,W,K,K,K,K,K,W,
            W,K,K,D,K,K,W,K,K,K,D,D,D,D,D,K,K,D,D,D,K,K,K,K,K,K,D,K,K,K,W,K,K,D,K,K,W,
            W,K,K,D,K,K,W,K,K,K,K,K,P,K,K,K,D,K,D,K,D,K,K,K,K,K,K,D,K,K,W,K,K,D,K,K,W,
            W,K,D,D,K,K,B,K,S,S,S,S,S,K,K,K,D,D,N,D,D,D,D,D,D,D,D,D,D,K,B,K,K,N,D,K,W,
            W,K,K,K,K,K,W,K,K,K,K,K,K,K,K,K,D,K,D,K,D,K,K,K,K,K,K,D,K,K,W,K,K,K,K,K,W,
            W,K,K,K,K,K,W,K,K,K,K,K,K,K,K,K,K,D,D,D,K,K,K,K,K,K,D,K,K,K,W,K,K,K,K,K,W,
            W,K,K,K,K,K,W,K,K,K,D,D,D,D,K,K,K,K,K,K,K,K,K,S,K,K,K,K,S,K,W,K,K,K,K,K,W,
            W,W,W,W,W,W,W,K,K,D,M,M,M,M,D,K,K,S,K,K,K,K,K,S,K,K,K,K,S,K,W,W,W,W,W,W,W,
            S,W,W,K,K,K,K,K,D,M,M,D,M,M,M,D,K,S,K,K,K,S,S,S,K,K,K,K,S,K,K,K,K,K,W,W,S,
            S,W,W,W,K,K,K,K,D,M,M,D,M,M,M,D,K,S,K,K,K,S,K,K,K,K,K,K,S,K,K,P,K,K,W,W,S,
            S,W,W,W,W,K,P,K,D,M,M,D,D,D,M,D,K,S,K,K,K,S,K,K,K,K,K,K,K,K,K,K,K,W,W,W,S,
            S,S,W,W,W,W,K,K,D,M,M,M,M,M,M,D,K,S,K,P,K,K,K,K,K,P,K,K,K,K,K,K,W,W,W,S,S,
            S,S,W,W,W,W,W,K,K,D,M,M,M,M,D,K,K,S,K,K,K,K,K,K,K,K,S,S,S,S,K,W,W,W,W,S,S,
            S,S,S,W,W,W,W,W,K,K,D,D,D,D,K,K,K,K,K,K,S,S,S,S,K,K,K,K,K,K,W,W,W,W,S,S,S,
            S,S,S,S,W,W,W,W,W,K,S,S,K,K,W,W,W,W,B,W,W,W,W,K,K,K,K,K,K,W,W,W,W,S,S,S,S,
            S,S,S,S,S,W,W,W,W,W,K,S,S,K,W,K,K,K,K,K,K,K,W,K,S,P,K,K,W,W,W,W,S,S,S,S,S,
            S,S,S,S,S,S,W,W,W,W,W,K,S,S,W,K,K,K,D,K,K,K,W,K,S,K,K,W,W,W,W,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,W,W,W,K,S,W,K,D,D,D,K,K,K,W,K,S,K,W,W,W,W,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,W,W,W,W,W,K,W,K,K,K,K,K,K,K,W,K,S,W,W,W,W,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,W,W,W,W,W,W,K,K,K,K,K,K,K,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
    };


    protected boolean build() {
        setSize(37, 37);

        int exitCell = (this.width * 18 + 18);
        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        int exitCellX = 699;
        LevelTransition exitX = new LevelTransition(this, exitCellX, LevelTransition.Type.DOUBLE_ENTRANCE);
        transitions.add(exitX);

        int enterCell = 129;
        LevelTransition enter = new LevelTransition(this, enterCell, LevelTransition.Type.SURFACE);
        transitions.add(enter);

        map = codedMap.clone();
        return true;
    }


    private Potion changePotion(Potion p) {
        if (p instanceof ExoticPotion) {
            return Reflection.newInstance(ExoticPotion.exoToReg.get(p.getClass()));
        } else {
            return Reflection.newInstance(ExoticPotion.regToExo.get(p.getClass()));
        }
    }


    protected void createItems() {

        if(Badges.isUnlocked(Badges.Badge.KILL_MORES) && Random.Float() >=0.7f || DeviceCompat.isDebug()){
            SliceDream sliceDream = new SliceDream();
            sliceDream.pos = 534;
            mobs.add(sliceDream);
        }

            drop( ( Generator.randomUsingDefaults( Generator.Category.POTION ) ), 646 );

            drop( ( Generator.randomUsingDefaults( Generator.Category.SCROLL ) ), 648 );

            drop( new RandomChest(), 720  ).type = Heap.Type.FOR_ICE;

            drop( ( Generator.randomUsingDefaults( Generator.Category.FOOD ) ), 722 );


            List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);

            if(passwordbadges.contains(PaswordBadges.Badge.HERO_CLRE)){
                drop( changePotion((Potion) Generator.randomUsingDefaults( Generator.Category.POTION )), 632 );
                drop( new Gold(325), 595 );
                drop( new BzmdrNewYears.BzmdrGift(), 668 );
            }

    }

    public Mob createMob() {
        return null;
    }

    public static int[] SALEPOS_ONE = new int[]{
        824,861
    };

    public static int[] SALEPOS_TWO = new int[]{
        975,976
    };

    public static int[] SALEPOS_THREE = new int[]{
           972,934
    };

    public static int[] SALEPOS_FOUR = new int[]{
            864,1008
    };

    public static int[] POSSALE = new int[]{
           1010,863,900,901
    };

    protected void createMobs() {
        WaloKe god1= new WaloKe();
        god1.pos = 1202;
        mobs.add(god1);

        BzmdrLand god2= new BzmdrLand();
        god2.pos = 669;
        mobs.add(god2);

        YetYog npc3 = new YetYog();
        npc3.pos = 493;
        mobs.add(npc3);

        Nyz npc4= new Nyz();
        npc4.pos = 936;
        mobs.add(npc4);
        for (int i : SALEPOS_ONE) {
            drop((Generator.random(Generator.Category.MISSILE)), i).type =
                    Heap.Type.FOR_SALE;
        }
        for (int i : SALEPOS_TWO) {
            drop((Generator.random(Generator.Category.POTION)), i).type =
                    Heap.Type.FOR_SALE;
        }
        for (int i : SALEPOS_THREE) {
            drop((Generator.random(Generator.Category.WEP_T2)), i).type =
                    Heap.Type.FOR_SALE;
        }
        for (int i : SALEPOS_FOUR) {
            drop((Generator.random(Generator.Category.SCROLL)), i).type =
                    Heap.Type.FOR_SALE;
        }
        for (int i : POSSALE) {
            drop((Generator.random(Generator.Category.SEED)), i).type =
                    Heap.Type.FOR_SALE;
        }
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
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show( new WndMessage( Messages.get(hero, "leave") ) );
                    }
                });
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

                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show( new WndHardNotification(new ItemSprite(ItemSpriteSheet.DLCBOOKS),
                                Messages.get(hero, "dlc_name"),
                                Messages.get(hero, "leave_more_dead"),
                                "OK",
                                0));
                    }
                });
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
        return Assets.Environment.RELOAD;
    }

    public String waterTex() {
        return Assets.Environment.WATER_PRISON;
    }


    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case MINE_CRYSTAL:
                return Messages.get(this, "time_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case MINE_CRYSTAL:
                return Messages.get(this, "time_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    private void tell(String text) {
        Game.runOnRenderThread(new Callback() {
                                   @Override
                                   public void call() {
                                       GameScene.show(new WndQuest(new Nyz(), text));
                                   }
                               }
        );
    }

    private void talkToHero(){
        if(!tipsgodungeon) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    tell(Messages.get(Hero.class, "acsx"));
                }
            });
            ready();
            tipsgodungeon = true;
        }
    }

    @Override
    public void playLevelMusic() {
        Music.playModeBGM(Assets.Music.TOWN, true);
    }
}
