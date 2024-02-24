package com.shatteredpixel.shatteredpixeldungeon.levels.nosync;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.crivusfruitslevel2;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CRYSTAL_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.LOCKED_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.PEDESTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ClearElemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.CrivusStarFruits;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.CrivusStarFruitsLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class ForestHardBossLevel extends Level {
    private static final int WIDTH = 33;
    private static final int HEIGHT = 32;
    public String tilesTex() {
        return Assets.Environment.TILES_SEWERS;
    }

    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    {
        color1 = 0x801500;
        color2 = 0xa68521;
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        int pos = 82; //random cell adjacent to the entrance.
        int cell;
        do {
            cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }


    private static final int getBossDoor = 214;
    private static final int LDBossDoor = 247;

    private static final int HOME = 82;

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);
        //GLog.p(String.valueOf(hero.pos));
        boolean isTrue = ch.pos == LDBossDoor && ch == hero && Dungeon.level.distance(ch.pos, entrance) >= 2;

        for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (boss instanceof CrivusStarFruits) {
                if (!Statistics.crivusfruitslevel2 && boss.HP<=160) {
                    crivusfruitslevel2 = true;
                    GLog.n(Messages.get(CrivusStarFruits.class, "anargy"));
                    GameScene.flash(0x808c8c8c);
                    //doYogLasers()

                    changeMap(boss_CHASM_Map);

                    for (Heap heap : level.heaps.valueList()) {
                        for (Item item : heap.items) {
                            if (!(item instanceof PotionOfPurity.PotionOfPurityLing)) {
                                item.doPickUp(hero, hero.pos);
                                heap.destroy();
                            } else {
                                heap.destroy();
                            }
                        }
                    }


                    for (int i : TPos) {
                        Heap s = drop(new Bomb(), i);
                        s.type = Heap.Type.SKELETON;
                        s.sprite.view(s);
                    }

                    Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
                    boss.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
                    ScrollOfTeleportation.teleportToLocation(hero, 248);
                    boss.HP = boss.HT = 160;
                    for (Buff b : boss.buffs()){
                        b.detach();
                    }
                    for (int i : ForestHardBossLasherTWOPos) {
                        ClearElemental csp = new ClearElemental();
                        csp.HT = csp.HP = 30;
                        csp.pos = i;
                        GameScene.add(csp);
                        csp.state = csp.WANDERING;
                        Buff.affect(csp, CrivusFruits.CFBarrior.class).setShield(30);
                    }
                    for (int i : MobPos) {
                        CrivusStarFruitsLasher csp = new CrivusStarFruitsLasher();
                        csp.pos = i;
                        csp.state = csp.WANDERING;
                        GameScene.add(csp);
                    }
                    //crivusfruitslevel2 = true;
                }
            }
        }

        if(Statistics.CrivusbossTeleporter>16 && !Statistics.crivusfruitslevel3 && crivusfruitslevel2) {
            for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (boss instanceof CrivusStarFruits) {
                    int pos;
                    switch (Random.Int(9)) {
                        case 0:
                        default:
                            pos = 346;
                            break;
                        case 1:
                            pos = 709;
                            break;
                        case 2:
                            pos = 339;
                            break;
                        case 3:
                            pos = 353;
                            break;
                        case 4:
                            pos = 721;
                            break;
                        case 5:
                            pos = 697;
                            break;
                        case 6:
                            pos = 596;
                            break;
                        case 7:
                            pos = 624;
                            break;
                        case 8:
                            pos = 577;
                            break;
                    }
                    ScrollOfTeleportation.teleportToLocation(boss, pos);
                    CellEmitter.get(boss.pos).burst(Speck.factory(Speck.LIGHT), 10);
                    Statistics.CrivusbossTeleporter = 0;
                }
            }
        } else {
            Statistics.CrivusbossTeleporter++;
        }

        //如果有生物来到BossDoor的下一个坐标，且生物是玩家，那么触发seal().
        if (map[getBossDoor] == WATER && isTrue ) {
            seal();
        }
    }

    public static int[] ForestBossLasherPos = new int[]{
            474,482,647,639,568,586,738,746,383,375,511,643
    };

    public static int[] ChestPos = new int[]{
            137,141,73,205
    };

    @Override
    public void seal() {
        super.seal();

        for (int i : ForestBossLasherPos) {
            CrivusStarFruitsLasher csp = new CrivusStarFruitsLasher();
            csp.pos = i;
            GameScene.add(csp);
        }

        for (int i : TombPos) {
            Heap s = drop(new PotionOfPurity.PotionOfPurityLing().identify(), i);
            s.type = Heap.Type.REMAINS;
            s.sprite.view(s);
        }

        set( getBossDoor, WALL );
        GameScene.updateMap( getBossDoor );

        set( HOME, Terrain.EMPTY );
        GameScene.updateMap( HOME );
        Dungeon.observe();

        CrivusStarFruits boss = new CrivusStarFruits();
        boss.state = boss.WANDERING;
        boss.pos = 577;
        GameScene.add(boss);
    }

    @Override
    public void unseal() {
        super.unseal();

        drop( ( Generator.randomUsingDefaults( Generator.Category.POTION ) ), 935 );

        drop( ( Generator.randomUsingDefaults( Generator.Category.SCROLL ) ), 945 );


        set( getBossDoor, Terrain.EMPTY );
        GameScene.updateMap( getBossDoor );

        set( HOME, Terrain.ENTRANCE );
        GameScene.updateMap( HOME );

        RatKing king = new RatKing();
        king.pos = 139;
        GameScene.add(king);
        for (int i : ChestPos) {
            //drop(new Gold( Random.IntRange( 10, 25 )),i).type = Heap.Type.CHEST;
            Heap droppedGold = Dungeon.level.drop( new Gold( Random.IntRange( 50, 125 )),i);
            droppedGold.type = Heap.Type.CHEST;
            //必须追加一个view处理才能让金币的类型申请给地牢view处理后变成箱子样子
            droppedGold.sprite.view( droppedGold );
        }

        Heap droppedB = Dungeon.level.drop( Generator.randomUsingDefaults( Generator.Category.RING), 156 );
        droppedB.type = Heap.Type.CRYSTAL_CHEST;
        droppedB.sprite.view( droppedB );

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                Music.INSTANCE.fadeOut(5f, new Callback() {
                    @Override
                    public void call() {
                        Music.INSTANCE.play(Assets.BGM_1, true);
                    }
                });
            }
        });

//        for (int i : UpdateRead) {
//            set( i, Terrain.EMPTY_SP );
//            GameScene.updateMap( i );
//        }
//
//        switch(Random.NormalIntRange(1,6)){
//            case 1:case 2:case 3:


//
//                drop( new CrystalKey(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH) ? 2 : 5 ), WIDTH*7+29 );
//
//                Heap droppedA = Dungeon.level.drop( Generator.randomUsingDefaults( Generator.Category.ARMOR),
//                        WIDTH*7+28 );
//                droppedA.type = Heap.Type.CRYSTAL_CHEST;
//                droppedA.sprite.view( droppedA );
//                break;
//            case 4: case 5: case 6:
//                for (int i : RatKingRoomBSpawn) {
//                    Heap droppedGold = Dungeon.level.drop( new Gold( Random.IntRange( 10, 25 )),i);
//                    droppedGold.type = Heap.Type.CHEST;
//                    droppedGold.sprite.view( droppedGold );
//                }
//                RatKing king2 = new RatKing();
//                king2.pos = WIDTH*7+28;
//                GameScene.add(king2);
//
//                drop( new CrystalKey(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH) ? 2 : 5 ), WIDTH*7+4 );
//

//        }

        Dungeon.observe();


    }



    private static final int S = CHASM;
    private static final int L = WALL;
    private static final int D = EMPTY_SP;
    private static final int W = EMPTY;
    private static final int X = LOCKED_DOOR;
    private static final int Z = EXIT;
    private static final int Q = ENTRANCE;
    private static final int C = CRYSTAL_DOOR;
    private static final int N = PEDESTAL;
    private static final int M = WATER;

    private static final int K = DOOR;

    private static final int J = EMPTY;

    private static final int[] bossMap = {
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,L,L,L,L,L,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,L,L,L,L,L,L,L,L,L,L,S,S,L,J,J,J,L,S,S,L,L,L,L,L,L,L,L,L,L,S,S,
            S,L,L,M,J,J,J,J,J,J,J,L,L,L,J,J,Q,J,J,L,L,L,J,J,J,J,J,J,J,M,L,L,S,
            S,L,S,M,M,J,J,J,J,J,J,J,L,J,J,J,J,J,J,J,L,J,J,J,J,J,J,J,M,M,S,L,S,
            S,L,S,S,M,J,J,J,J,J,J,J,C,J,J,J,J,J,J,J,C,J,J,J,J,J,J,J,M,S,S,L,S,
            S,L,S,S,M,J,J,J,J,J,J,J,L,J,J,L,M,L,J,J,L,J,J,J,J,J,J,J,M,S,S,L,S,
            S,L,S,S,M,M,J,J,J,J,J,J,L,L,J,L,M,L,J,L,L,J,J,J,J,J,J,M,M,S,S,L,S,
            S,L,S,S,M,J,J,J,L,L,L,L,L,L,L,L,K,L,L,L,L,L,L,L,L,J,J,J,M,S,S,L,S,
            L,L,S,L,L,L,L,L,L,W,W,W,W,W,W,W,D,W,W,W,W,W,W,W,L,L,L,L,L,L,S,L,S,
            L,L,L,L,M,M,M,M,M,S,S,S,S,S,S,D,D,D,S,S,S,S,S,S,M,M,M,M,M,L,L,L,L,
            L,L,D,M,M,S,S,S,S,D,D,D,D,D,D,D,W,D,D,D,D,D,D,D,S,S,S,S,M,M,D,L,L,
            L,D,M,S,S,S,D,D,D,D,S,S,D,L,S,D,D,D,S,L,D,S,S,D,D,D,D,S,S,M,M,D,L,
            L,M,S,S,D,D,D,S,D,S,S,S,S,L,S,S,D,S,S,L,S,S,S,S,D,S,D,D,D,S,S,M,L,
            L,M,S,D,D,D,W,S,D,D,S,S,S,L,D,D,D,D,D,L,S,S,S,D,D,S,W,D,D,D,S,M,L,
            L,M,W,S,D,S,W,W,W,D,S,S,D,D,D,L,D,L,D,D,D,S,S,D,W,W,W,S,D,S,W,M,L,
            L,M,S,S,D,W,W,W,D,D,D,D,D,L,L,L,D,L,L,L,D,D,D,D,D,W,W,W,D,S,S,M,L,
            L,S,D,D,D,D,W,W,W,D,S,S,D,D,L,D,D,D,L,D,D,S,S,D,W,S,W,D,D,D,D,S,L,
            L,D,D,W,W,D,S,D,D,D,S,S,S,D,D,D,N,D,D,D,S,S,S,D,D,D,S,D,W,W,D,D,L,
            L,S,D,W,D,D,W,S,W,D,S,S,D,D,L,D,D,D,L,D,D,S,S,D,W,W,W,D,D,W,D,S,L,
            L,S,W,W,D,W,W,W,D,D,D,D,D,L,L,L,D,L,L,L,D,D,D,D,D,W,W,W,D,W,W,S,L,
            L,S,W,W,D,W,W,W,W,D,S,L,D,L,L,L,D,L,L,L,D,L,S,D,W,W,W,W,D,W,W,S,L,
            L,S,W,D,D,D,D,W,D,D,L,L,D,L,D,D,D,D,D,L,D,L,L,D,D,W,D,D,D,D,W,S,L,
            L,S,W,W,D,W,D,W,D,W,L,D,D,D,D,L,D,L,D,D,D,D,L,W,D,W,D,W,D,W,W,S,L,
            L,S,W,W,S,W,D,D,D,W,L,W,D,W,W,S,D,S,W,W,D,W,L,W,D,D,D,W,S,W,W,S,L,
            L,S,W,W,S,W,W,W,W,W,L,W,S,W,W,S,D,S,W,W,S,W,L,W,W,W,W,W,S,W,W,S,L,
            L,L,W,W,S,W,W,W,W,W,S,W,W,W,W,S,D,S,W,W,W,W,S,W,W,W,W,W,S,W,W,L,L,
            S,L,L,W,S,W,W,W,L,L,L,L,L,L,L,L,X,L,L,L,L,L,L,L,L,W,W,W,S,W,L,L,S,
            S,S,L,L,L,L,L,L,L,J,J,J,J,J,L,J,J,J,L,J,J,J,J,J,L,L,L,L,L,L,L,S,S,
            S,S,S,S,S,S,S,S,L,J,J,J,J,J,X,J,J,J,X,J,J,J,J,J,L,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,L,J,J,J,J,J,L,J,Z,J,L,J,J,J,J,J,L,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,L,J,J,L,L,L,L,J,J,J,L,L,L,L,J,J,L,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,L,L,L,L,S,S,L,L,L,L,L,S,S,L,L,L,L,S,S,S,S,S,S,S,S,
    };

    public static final int[] boss_CHASM_Map = {
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,L,L,L,L,L,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,L,L,L,L,L,L,L,L,L,L,S,S,L,J,J,J,L,S,S,L,L,L,L,L,L,L,L,L,L,S,S,
            S,L,L,M,J,J,J,J,J,J,J,L,L,L,J,J,Q,J,J,L,L,L,J,J,J,J,J,J,J,M,L,L,S,
            S,L,S,M,M,J,J,J,J,J,J,J,L,J,J,J,J,J,J,J,L,J,J,J,J,J,J,J,M,M,S,L,S,
            S,L,S,S,M,J,J,J,J,J,J,J,C,J,J,J,J,J,J,J,C,J,J,J,J,J,J,J,M,S,S,L,S,
            S,L,S,S,M,J,J,J,J,J,J,J,L,J,J,L,M,L,J,J,L,J,J,J,J,J,J,J,M,S,S,L,S,
            S,L,S,S,M,M,J,J,J,J,J,J,L,L,J,L,L,L,J,L,L,J,J,J,J,J,J,M,M,S,S,L,S,
            S,L,S,S,M,J,J,J,L,L,L,L,L,L,L,L,K,L,L,L,L,L,L,L,L,J,J,J,M,S,S,L,S,
            L,L,S,L,L,L,L,L,L,W,W,W,W,W,W,W,D,W,W,W,W,W,W,W,L,L,L,L,L,L,S,L,S,
            L,L,L,L,M,M,M,M,M,S,S,S,S,S,S,D,D,D,S,S,S,S,S,S,M,M,M,M,M,L,L,L,L,
            L,L,D,M,M,S,S,S,S,D,D,D,D,D,D,D,W,D,D,D,D,D,D,D,S,S,S,S,M,M,D,L,L,
            L,D,M,S,S,S,D,D,D,D,S,S,D,L,S,D,D,D,S,L,D,S,S,D,D,D,D,S,S,M,M,D,L,
            L,M,S,S,D,D,D,S,D,S,S,S,S,L,S,S,D,S,S,L,S,S,S,S,D,S,D,D,D,S,S,M,L,
            L,M,S,D,D,D,W,S,D,D,S,S,S,L,D,D,D,D,D,L,S,S,S,D,D,S,W,D,D,D,S,M,L,
            L,M,W,S,D,S,W,W,W,D,S,S,D,D,D,L,D,L,D,D,D,S,S,D,W,W,W,S,D,S,W,M,L,
            L,M,S,S,D,W,W,W,D,D,D,D,D,L,L,L,D,L,L,L,D,D,D,D,D,W,W,W,D,S,S,M,L,
            L,S,D,D,D,D,W,W,W,D,S,S,D,D,L,D,D,D,L,D,D,S,S,D,W,S,W,D,D,D,D,S,L,
            L,D,D,S,S,D,S,D,D,D,S,S,S,D,D,D,N,D,D,D,S,S,S,D,D,D,S,D,S,S,D,D,L,
            L,S,D,S,D,D,S,S,S,D,S,S,D,D,L,D,D,D,L,D,D,S,S,D,S,S,S,D,D,S,D,S,L,
            L,S,S,S,D,S,S,S,D,D,D,D,D,L,L,L,D,L,L,L,D,D,D,D,D,S,S,S,D,S,S,S,L,
            L,S,S,S,D,S,S,S,S,D,S,L,D,L,L,L,D,L,L,L,D,L,S,D,S,S,S,S,D,S,S,S,L,
            L,S,S,D,D,D,D,S,D,D,L,L,D,L,D,D,D,D,D,L,D,L,L,D,D,S,D,D,D,D,S,S,L,
            L,S,S,S,D,S,D,S,D,S,L,D,D,D,D,L,D,L,D,D,D,D,L,S,D,S,D,S,D,S,S,S,L,
            L,S,W,W,S,W,D,D,D,W,L,W,D,W,W,S,D,S,W,W,D,W,L,W,D,D,D,W,S,W,W,S,L,
            L,S,W,W,S,W,W,W,W,W,L,W,S,W,W,S,D,S,W,W,S,W,L,W,W,W,W,W,S,W,W,S,L,
            L,L,W,W,S,W,W,W,W,W,S,W,W,W,W,S,D,S,W,W,W,W,S,W,W,W,W,W,S,W,W,L,L,
            S,L,L,W,S,W,W,W,L,L,L,L,L,L,L,L,X,L,L,L,L,L,L,L,L,W,W,W,S,W,L,L,S,
            S,S,L,L,L,L,L,L,L,J,J,J,J,J,L,J,J,J,L,J,J,J,J,J,L,L,L,L,L,L,L,S,S,
            S,S,S,S,S,S,S,S,L,J,J,J,J,J,X,J,J,J,X,J,J,J,J,J,L,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,L,J,J,J,J,J,L,J,Z,J,L,J,J,J,J,J,L,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,L,J,J,L,L,L,L,J,J,J,L,L,L,L,J,J,L,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,L,L,L,L,S,S,L,L,L,L,L,S,S,L,L,L,L,S,S,S,S,S,S,S,S,
    };

    public static int[] TombPos = new int[]{
            535,619,562,592
    };

    public static int[] TPos = new int[]{
            535,592
    };

    public static int[] MobPos = new int[]{
         383,375,639,648,570,584
    };


    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = bossMap.clone();

        int entranceCell = 82;
        int exitCell = 973;

        LevelTransition enter = new LevelTransition(this, entranceCell, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        return true;
    }

    public static int[] ForestHardBossLasherTWOPos = new int[]{
           507,515
    };

    @Override
    protected void createMobs() {

    }

    @Override
    protected void createItems() {

    }
}
