package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruitsLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Ripple;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

// 克里弗斯之果 5层
public class ForestBossLevel extends Level {

  @Override
  protected void createMobs() {}

  // 二选一
  @Override
  protected void createItems() {
    drop((Generator.randomUsingDefaults(Generator.Category.POTION)), this.width * 28 + 10);

    drop((Generator.randomUsingDefaults(Generator.Category.SCROLL)), this.width * 28 + 22);
  }

  public static final int WIDTH = 32;
  private static final int HEIGHT = 32;

  private static final short W = Terrain.WALL;
  private static final short L = Terrain.CHASM;
  private static final short M = Terrain.EMPTY;
  private static final short H = Terrain.EMPTY_SP;
  private static final short T = Terrain.STATUE;
  private static final short X = Terrain.DOOR;
  private static final short D = Terrain.PEDESTAL;

  private static final short B = Terrain.WALL_DECO;
  private static final short C = Terrain.WATER;
  private static final short S = Terrain.LOCKED_DOOR;
  private static final short Q = Terrain.EXIT;
  private static final short A = Terrain.CRYSTAL_DOOR;
  private static final short I = Terrain.ENTRANCE;

  private static final int getBossDoor = WIDTH * 9 + 16;
  private static final int LDBossDoor = WIDTH * 10 + 16;

  private static final int HOME = WIDTH + 16;

  // 首先诞生的8个触手 一阶段
  public static int[] ForestBossLasherPos =
      new int[] {
        WIDTH * 17 + 11,
        WIDTH * 15 + 13,
        WIDTH * 23 + 13,
        WIDTH * 21 + 11,
        WIDTH * 15 + 19,
        WIDTH * 17 + 21,
        WIDTH * 21 + 21,
        WIDTH * 23 + 19,
      };

  // 2阶段4个主触手
  public static int[] ForestBossLasherTWOPos =
      new int[] {
        WIDTH * 15 + 13, WIDTH * 15 + 19, WIDTH * 23 + 13, WIDTH * 23 + 19,
      };

  // 铺路
  public static int[] UpdateRead =
      new int[] {
        WIDTH * 11 + 12,
        WIDTH * 11 + 10,
        WIDTH * 11 + 11,
        WIDTH * 11 + 9,
        WIDTH * 11 + 20,
        WIDTH * 11 + 21,
        WIDTH * 11 + 22,
      };

  // 鼠王的A号房间
  public static int[] RatKingRoom =
      new int[] {
        WIDTH * 13 + 6, WIDTH * 9 + 6, WIDTH * 5 + 6,
        WIDTH * 8 + 6, WIDTH * 4 + 6, WIDTH + 6,
        WIDTH * 12 + 6, WIDTH * 7 + 6, WIDTH * 3 + 6,
        WIDTH * 10 + 6, WIDTH * 6 + 6, WIDTH * 2 + 6,
      };

  // 鼠王的B号房间
  public static int[] BRatKingRoom =
      new int[] {
        WIDTH + 25,
        WIDTH * 5 + 25,
        WIDTH * 10 + 25,
        WIDTH * 2 + 25,
        WIDTH * 6 + 25,
        WIDTH * 3 + 25,
        WIDTH * 7 + 25,
        WIDTH * 4 + 25,
        WIDTH * 8 + 25,
        WIDTH * 12 + 25,
        WIDTH * 9 + 25,
      };

  // 鼠王和它的宝藏A号区域
  public static int[] RatKingRoomASpawn =
      new int[] {
        WIDTH * 12 + 5,
        WIDTH * 12 + 1,
        WIDTH * 12 + 2,
        WIDTH * 10 + 5,
        WIDTH * 11 + 1,
        WIDTH * 12 + 3,
        WIDTH * 9 + 5,
        WIDTH * 10 + 1,
        WIDTH * 12 + 4,
        WIDTH * 8 + 5,
        WIDTH * 9 + 1,
        WIDTH * 7 + 5,
        WIDTH * 8 + 1,
        WIDTH * 6 + 5,
        WIDTH * 7 + 1,
        WIDTH * 5 + 5,
        WIDTH * 6 + 1,
        WIDTH * 4 + 5,
        WIDTH * 5 + 1,
        WIDTH * 3 + 5,
        WIDTH * 4 + 1,
        WIDTH * 2 + 5,
        WIDTH * 3 + 1,
        WIDTH + 5,
        WIDTH * 2 + 1,
        WIDTH + 1,
        WIDTH + 2,
        WIDTH + 3,
        WIDTH + 4
      };

  // 鼠王和它的宝藏B号区域
  public static int[] RatKingRoomBSpawn =
      new int[] {
        WIDTH + 26,
        WIDTH * 2 + 26,
        WIDTH * 3 + 26,
        WIDTH * 4 + 26,
        WIDTH * 5 + 26,
        WIDTH * 6 + 26,
        WIDTH * 7 + 26,
        WIDTH * 8 + 26,
        WIDTH * 9 + 26,
        WIDTH * 10 + 26,
        WIDTH * 12 + 26,
        WIDTH + 30,
        WIDTH * 2 + 30,
        WIDTH * 3 + 30,
        WIDTH * 4 + 30,
        WIDTH * 5 + 30,
        WIDTH * 6 + 30,
        WIDTH * 7 + 30,
        WIDTH * 8 + 30,
        WIDTH * 9 + 30,
        WIDTH * 10 + 30,
        WIDTH * 12 + 30,
        WIDTH * 11 + 30,
        WIDTH * 12 + 27,
        WIDTH * 12 + 28,
        WIDTH * 12 + 29,
        WIDTH + 27,
        WIDTH + 28,
        WIDTH + 29,
      };

  @Override
  public void occupyCell(Char ch) {

    super.occupyCell(ch);

    // 如果有生物来到BossDoor的下一个坐标，且生物是玩家，那么触发seal().
    if (map[getBossDoor] == Terrain.DOOR && ch.pos == LDBossDoor && ch == Dungeon.hero) {
      seal();
    }
  }

  @Override
  public void seal() {
    super.seal();

    for (int i : ForestBossLasherPos) {
      CrivusFruitsLasher csp = new CrivusFruitsLasher();
      csp.pos = i;
      GameScene.add(csp);
    }

    CrivusFruits boss = new CrivusFruits();
    boss.state = boss.WANDERING;
    boss.pos = WIDTH * 19 + 16;
    GameScene.add(boss);
    set(getBossDoor, Terrain.WALL);
    GameScene.updateMap(getBossDoor);
    set(HOME, Terrain.EMPTY);
    GameScene.updateMap(HOME);
    Dungeon.observe();

    drop(new PotionOfPurity(), WIDTH * 11 + 15);
    drop(new PotionOfPurity(), WIDTH * 15 + 16);
    drop(new PotionOfPurity(), WIDTH * 11 + 17);
  }

  @Override
  public void unseal() {
    super.unseal();

    set(getBossDoor, Terrain.EMPTY);
    GameScene.updateMap(getBossDoor);

    set(HOME, Terrain.ENTRANCE);
    GameScene.updateMap(HOME);

    for (int i : UpdateRead) {
      set(i, Terrain.EMPTY_SP);
      GameScene.updateMap(i);
    }

    switch (Random.NormalIntRange(1, 6)) {
      case 1:
      case 2:
      case 3:
        for (int i : RatKingRoomASpawn) {
          drop(new Gold(Random.IntRange(10, 25)), i);
        }
        RatKing king = new RatKing();
        king.pos = WIDTH * 7 + 3;
        GameScene.add(king);
        break;
      case 4:
      case 5:
      case 6:
        for (int i : RatKingRoomBSpawn) {
          drop(new Gold(Random.IntRange(10, 25)), i);
        }
        RatKing king2 = new RatKing();
        king2.pos = WIDTH * 7 + 28;
        GameScene.add(king2);
        break;
    }

    Dungeon.observe();
  }

  // 地图结构
  private static final int[] WorldRoomShort = {
    W, W, W, W, W, W, W, B, B, B, B, W, W, W, W, W, W, W, W, W, W, W, B, B, B, B, W, W, W, W, W, W,
    W, M, M, M, M, M, M, C, C, C, C, L, L, W, T, M, I, M, T, W, L, L, C, C, C, C, M, M, M, M, M, W,
    W, M, M, M, M, M, M, C, C, C, L, L, L, W, T, M, M, M, T, W, L, L, L, C, C, C, M, M, M, M, M, W,
    W, M, M, M, M, M, M, C, C, L, L, L, L, W, T, M, H, M, T, W, L, L, L, L, C, C, M, M, M, M, M, W,
    W, M, M, M, M, M, M, C, C, L, L, L, L, W, T, M, M, M, T, W, L, L, L, L, C, C, M, M, M, M, M, W,
    W, M, M, M, M, M, M, C, C, L, L, L, L, W, T, M, H, M, T, W, L, L, L, L, C, C, M, M, M, M, M, W,
    W, M, M, M, M, M, M, C, C, L, L, L, L, W, T, M, M, M, T, W, L, L, L, L, C, C, M, M, M, M, M, W,
    W, M, M, M, M, M, M, C, C, L, L, L, L, W, T, M, H, M, T, W, L, L, L, L, C, C, M, M, M, M, M, W,
    W, M, M, M, M, M, M, C, C, L, L, L, L, W, T, M, M, M, T, W, L, L, L, L, C, C, M, M, M, M, M, W,
    W, M, M, M, M, M, M, C, C, L, L, L, L, W, W, W, X, W, W, W, L, L, L, L, C, C, M, M, M, M, M, W,
    W, M, M, M, M, M, M, C, C, L, L, L, L, W, M, B, H, B, M, W, L, L, L, L, C, C, M, M, M, M, M, W,
    W, M, M, M, M, M, M, C, C, C, L, L, L, A, M, C, H, C, M, A, L, L, L, C, C, C, M, M, M, M, M, W,
    W, M, M, M, M, M, M, C, C, C, C, L, L, W, M, C, H, C, M, W, L, L, C, C, C, C, M, M, M, M, M, W,
    W, B, B, B, B, B, B, W, W, W, W, B, W, B, M, C, H, C, M, B, W, B, W, W, W, B, B, B, B, B, B, W,
    W, C, C, C, C, C, C, W, W, W, M, C, M, C, M, C, H, C, M, C, M, C, W, W, W, C, C, C, C, C, C, W,
    W, L, L, L, W, W, W, B, M, M, M, C, M, H, M, C, H, C, M, H, M, C, M, M, B, W, W, L, L, L, L, W,
    W, L, L, W, W, M, M, C, M, M, M, C, C, C, C, C, H, C, C, C, M, C, M, M, C, M, W, W, L, L, L, W,
    W, W, W, W, B, M, M, C, M, M, M, H, C, C, H, H, H, H, H, C, M, H, M, M, C, M, M, B, W, W, W, W,
    W, W, C, C, C, C, C, C, C, C, C, C, C, C, H, C, C, C, H, C, C, C, C, C, C, C, C, C, C, C, W, W,
    W, H, H, H, H, H, H, H, H, H, H, H, H, H, H, C, D, C, H, H, H, H, H, H, H, H, H, H, H, H, H, W,
    W, C, C, C, C, C, C, C, C, C, C, C, C, C, H, C, C, C, H, C, C, C, C, C, C, C, C, C, C, C, M, W,
    W, M, M, M, M, M, M, M, M, M, M, H, M, C, H, H, H, H, H, C, M, H, M, M, M, M, M, M, M, M, M, W,
    W, M, M, M, M, M, M, M, M, M, M, M, M, C, C, C, H, C, C, C, M, M, M, M, M, M, M, M, M, M, M, W,
    W, M, M, H, M, M, M, M, M, M, M, M, M, H, M, C, H, C, M, H, M, M, M, M, M, M, M, M, M, M, M, W,
    W, M, M, M, M, M, M, H, M, M, M, M, M, M, M, C, H, C, M, M, M, M, M, M, M, M, M, H, M, M, M, W,
    W, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W, S, W, W, W, W, W, M, H, M, M, M, M, M, M, M, W,
    W, M, M, M, M, M, M, M, M, M, W, W, M, M, W, L, M, L, W, M, M, W, W, M, M, M, M, M, M, M, M, W,
    W, M, M, M, H, M, M, M, W, W, W, M, M, M, W, L, M, L, W, M, M, M, W, W, M, M, M, M, M, M, M, W,
    W, M, M, M, M, M, M, M, W, M, M, M, M, M, S, M, M, M, S, M, M, M, M, W, W, M, M, M, M, M, M, W,
    W, M, M, M, M, M, M, M, W, M, M, M, M, M, W, L, M, L, W, M, M, M, M, M, W, M, M, H, M, M, M, W,
    W, W, M, M, M, M, M, M, W, M, M, M, M, M, W, L, Q, L, W, M, M, M, M, M, W, M, M, M, M, M, W, W,
    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
  };

  private static class Sink extends Emitter {

    private int pos;
    private float rippleDelay = 0;

    private static final Emitter.Factory factory =
        new Factory() {

          @Override
          public void emit(Emitter emitter, int index, float x, float y) {
            WaterParticle p = (WaterParticle) emitter.recycle(WaterParticle.class);
            p.reset(x, y);
          }
        };

    public Sink(int pos) {
      super();

      this.pos = pos;

      PointF p = DungeonTilemap.tileCenterToWorld(pos);
      pos(p.x - 2, p.y + 3, 4, 0);

      pour(factory, 0.1f);
    }

    @Override
    public void update() {
      if (visible == (pos < level.heroFOV.length && level.heroFOV[pos])) {

        super.update();

        if (!isFrozen() && (rippleDelay -= Game.elapsed) <= 0) {
          Ripple ripple = GameScene.ripple(pos + level.width());
          if (ripple != null) {
            ripple.y -= DungeonTilemap.SIZE / 2f;
            rippleDelay = Random.Float(0.1f, 0.9f);
          }
        }
      }
    }
  }

  public static final class WaterParticle extends PixelParticle {

    public WaterParticle() {
      super();

      acc.y = 50;
      am = 0.5f;

      color(ColorMath.random(0xb6ccc2, 0x3b6653));
      size(2);
    }

    public void reset(float x, float y) {
      revive();

      this.x = x;
      this.y = y;

      speed.set(Random.Float(-1, +1), 0);

      left = lifespan = 0.5f;
    }
  }

  public static void addSewerVisuals(Level level, Group group) {
    for (int i = 0; i < level.length(); i++) {
      if (level.map[i] == Terrain.WALL_DECO) {
        group.add(new ForestBossLevel.Sink(i));
      }
    }
  }

  // 构建地图
  protected boolean build() {
    setSize(WIDTH, HEIGHT);
    map = WorldRoomShort.clone();

    this.entrance = WIDTH + 16;
    this.exit = WIDTH * 30 + 16;

    return true;
  }

  // 加入粒子效果
  @Override
  public Group addVisuals() {
    super.addVisuals();
    addSewerVisuals(this, visuals);
    return visuals;
  }

  public String tilesTex() {
    return Assets.Environment.TILES_SEWERS;
  }

  public String waterTex() {
    return Assets.Environment.WATER_SEWERS;
  }
}
