package com.shatteredpixel.shatteredpixeldungeon.levels.tomb;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus.Egret;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing.VirtualActor;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.DeadLaser;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.WhiteFlowerCircle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PalePalaceBossLevel extends Level {

    private static final int WIDTH = 25;
    private static final int HEIGHT = 30;

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 100;
        extraGlass = false;
    }

    private static final int S = CHASM;
    private static final int W = SIGN;
    private static final int P = EMPTY_SP;
    private static final int R = EXIT	;

    private static final int[] code_map = {
            S, S, S, S, S, S, S, S, S, S, S, W, W, W, S, S, S, S, S, S, S, S, S, S, S,
            S, S, S, S, S, S, S, S, W, W, W, W, W, W, W, W, W, S, S, S, S, S, S, S, S,
            S, S, S, S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S, S, S, S,
            S, S, S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S, S, S,
            S, S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S, S,
            S, S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S, S,
            S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S,
            S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S,
            S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S,
            S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S,
            S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S,
            S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S,
            S, W, W, W, W, W, W, W, W, W, W, W, P, W, W, W, W, W, W, W, W, W, W, W, S,
            W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, R, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W,
            S, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, S,
            S, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, S,
            S, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, S,
            S, S, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, S, S,
            S, S, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, S, S,
            S, S, S, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, S, S, S,
            S, S, S, S, W, W, W, P, P, P, P, P, P, P, P, P, P, P, W, W, W, S, S, S, S,
            S, S, S, S, S, S, W, W, W, W, W, W, W, W, W, W, W, W, W, S, S, S, S, S, S
    };

    private static final int BOSS_SPAWN = 312;
    private static final int FLOWER_SPAWN = 512;
    /** palace_apse 顶部玫瑰花窗在游戏里的格子坐标 */
    private static final int LASER_SOURCE = 162;

    private int status = 0;
    private boolean introStarted = false;
    private boolean bossDefeated = false;
    private long introStartTime = 0L;
    private WhiteFlowerCircle introFlower = null;

    //TODO 等待正式音乐到达
    @Override
    public void playLevelMusic(){
        Music.playModeBGM("music/tomb/tomb2.mp3",true);
    }

    @Override
    public void playBossMusic() {
        Music.playModeBGM("music/tomb/egret.ogg", true);
    }

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);

        if (ch != Dungeon.hero) {
            return;
        }

        // 参考 RogerBossLevel：玩家进入楼层后向前走两步（离入口 2 格）触发锁定
        if (status == 0 && !bossDefeated
                && entrance() >= 0 && distance(Dungeon.hero.pos, entrance()) >= 3) {
            status = 1;
            seal();
            return;
        }

        // 开场特效被窗口调整/读档打断时，直接兜底生成白鹭
        if (introStarted && !hasEgret() && !bossDefeated
                && introStartTime > 0L && Game.realTime - introStartTime > 4000L) {
            Game.runOnRenderThread(this::spawnEgretFallback);
        }
    }

    @Override
    public void unseal() {
        super.unseal();

        Level.set(entrance(), ENTRANCE);
        GameScene.updateMap(entrance());
        Dungeon.observe();

        Level.set(exit(), EXIT);
        GameScene.updateMap(exit());
        Dungeon.observe();
    }

    @Override
    public void seal() {
        super.seal();

        if (!introStarted && !bossDefeated && !hasEgret()) {
            introStarted = true;
            introStartTime = Game.realTime;
            Game.runOnRenderThread(this::startIntro);

            // 参考 Roger：即使开场特效被窗口调整/读档中断，也必须保证白鹭最终登场
            VirtualActor.delay(6f, () -> Game.runOnRenderThread(this::spawnEgretFallback));
        }

        Level.set(entrance(), EMPTY_SP);
        GameScene.updateMap(entrance());
        Dungeon.observe();

        Level.set(exit(), EMPTY_SP);
        GameScene.updateMap(exit());
        Dungeon.observe();
    }

    private void startIntro() {
        if (Dungeon.level != this || bossDefeated || hasEgret()) {
            return;
        }

        introStartTime = Game.realTime;

        float cx = (FLOWER_SPAWN % width() + 0.5f) * DungeonTilemap.SIZE;
        float cy = (FLOWER_SPAWN / width() + 0.5f) * DungeonTilemap.SIZE;

        // 从 palace_apse 顶部的玫瑰花窗（162）向 537 落下一道持续天降光柱
        PointF from = new PointF(
                (LASER_SOURCE % width() + 0.5f) * DungeonTilemap.SIZE,
                (LASER_SOURCE / width() + 0.5f) * DungeonTilemap.SIZE);
        PointF to = new PointF(cx, cy);

        DeadLaser beam = new DeadLaser(cx, from.y, to.y);
        if (Dungeon.hero != null && Dungeon.hero.sprite != null && Dungeon.hero.sprite.parent != null) {
            Dungeon.hero.sprite.parent.add(beam);
        }

        GameScene.flash(0x28BFEFFF);
        Camera.main.shake(0.9f, 1.4f);
        Sample.INSTANCE.play(Assets.Sounds.RAY);

        // 光柱落地的同时花就开始绽放，避免特效链中断时看不到花
        bloomFlower(cx, cy);
    }

    private void bloomFlower(float cx, float cy) {
        if (Dungeon.level != this || bossDefeated || hasEgret()) {
            return;
        }

        if (introFlower != null) {
            introFlower.killAndErase();
            introFlower = null;
        }

        introFlower = new WhiteFlowerCircle(cx, cy, 48f);
        introFlower.setOnShatter(this::onFlowerShattered);
        if (Dungeon.hero != null && Dungeon.hero.sprite != null && Dungeon.hero.sprite.parent != null) {
            Dungeon.hero.sprite.parent.add(introFlower);
        }

        CellEmitter.get(FLOWER_SPAWN).burst(Speck.factory(Speck.LIGHT), 40);
        CellEmitter.get(FLOWER_SPAWN).burst(Speck.factory(Speck.STAR), 16);
        Splash.at(new PointF(cx, cy), 0x37E3D0, 24);
        Camera.main.shake(0.6f, 1f);
    }

    /** 花破碎的同一帧生成白鹭，之后由她自行尝试回到王座。 */
    private void onFlowerShattered() {
        introFlower = null;

        CellEmitter.get(FLOWER_SPAWN).burst(Speck.factory(Speck.LIGHT), 30);
        CellEmitter.get(FLOWER_SPAWN).burst(Speck.factory(Speck.STAR), 12);
        Sample.INSTANCE.play(Assets.Sounds.SHATTER);

        final Egret egret = spawnEgretAt(FLOWER_SPAWN);
        if (egret != null) {
            VirtualActor.delayVisual(1f, () -> moveEgretToThrone(egret));
        }
    }

    private Egret spawnEgretAt(int cell) {
        if (Dungeon.level != this || bossDefeated || hasEgret()) {
            return null;
        }

        Egret egret = new Egret();
        egret.pos = cell;
        egret.state = egret.HUNTING;
        egret.setIntroPending(true);
        GameScene.add(egret);
        Dungeon.level.occupyCell(egret);

        if (egret.sprite != null) {
            egret.sprite.visible = true;
            egret.sprite.alpha(0f);
            if (Dungeon.hero != null && Dungeon.hero.sprite != null && Dungeon.hero.sprite.parent != null) {
                Dungeon.hero.sprite.parent.add(new AlphaTweener(egret.sprite, 1f, 0.8f));
            } else {
                egret.sprite.alpha(1f);
            }
        }

        return egret;
    }

    private void spawnEgretFallback() {
        if (Dungeon.level != this || bossDefeated) {
            return;
        }

        // 若白鹭已经生成（例如还停在花的位置），直接把她送回王座
        Egret existing = findEgret();
        if (existing != null) {
            moveEgretToThrone(existing);
            return;
        }

        if (introFlower != null) {
            introFlower.killAndErase();
            introFlower = null;
        }

        Egret egret = spawnEgretAt(BOSS_SPAWN);
        if (egret != null) {
            egret.setIntroPending(false);
            if (egret.sprite != null) {
                egret.sprite.alpha(1f);
            }
            egret.notice();
            unlockHero();
        }
    }

    private void moveEgretToThrone(Egret egret) {
        if (egret == null || !egret.isAlive() || Dungeon.level != this) {
            return;
        }

        egret.setIntroPending(false);

        // 场景尚未创建精灵时不能走 teleport（appear 会直接访问 sprite）
        if (egret.sprite == null) {
            egret.pos = BOSS_SPAWN;
            GameScene.add(egret);
            Dungeon.level.occupyCell(egret);
            if (egret.sprite == null) {
                return;
            }
        }

        if (!ScrollOfTeleportation.teleportToLocation(egret, BOSS_SPAWN)) {
            egret.pos = BOSS_SPAWN;
            egret.sprite.place(BOSS_SPAWN);
            Dungeon.level.occupyCell(egret);
        }

        egret.sprite.alpha(1f);
        egret.notice();
        unlockHero();
    }

    /** 白鹭到达王座后立刻解除出场期间的玩家回合锁定。 */
    private void unlockHero() {
        if (Dungeon.hero == null || !Dungeon.hero.isAlive()) {
            return;
        }

        Dungeon.hero.ready();

        // Actor 线程可能在同帧再次把英雄置忙，补几次确保输入真正恢复
        VirtualActor.delayVisual(0.2f, this::unlockHeroOnce);
        VirtualActor.delayVisual(0.6f, this::unlockHeroOnce);
        VirtualActor.delayVisual(1.0f, this::unlockHeroOnce);
    }

    private void unlockHeroOnce() {
        if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
            Dungeon.hero.ready();
        }
    }

    private Egret findEgret() {
        if (Dungeon.level == null) {
            return null;
        }
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof Egret && mob.isAlive()) {
                return (Egret) mob;
            }
        }
        return null;
    }

    private boolean hasEgret() {
        return findEgret() != null;
    }

    public void onEgretDefeated() {
        bossDefeated = true;
    }

    private static final String INTRO_KEY = "egret_intro";
    private static final String DEFEATED_KEY = "egret_defeated";
    private static final String STATUS_KEY = "egret_status";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(INTRO_KEY, introStarted);
        bundle.put(DEFEATED_KEY, bossDefeated);
        bundle.put(STATUS_KEY, status);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        introStarted = bundle.getBoolean(INTRO_KEY);
        bossDefeated = bundle.getBoolean(DEFEATED_KEY);
        status = bundle.getInt(STATUS_KEY);

        if (introStarted && !bossDefeated && !hasEgret()) {
            Game.runOnRenderThread(this::spawnEgretFallback);
        }
    }

    @Override
    protected boolean build() {
        feeling = Feeling.NONE;
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int enter = 712;
        LevelTransition entrance = new LevelTransition(this, enter, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(entrance);

        int entra = 462;
        LevelTransition exit = new LevelTransition(this, entra, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);

        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);

        return true;
    }

    @Override
    protected void createMobs() {
        // 白魔女不再常驻：由 seal() 开场动画结束后在 BOSS_SPAWN 生成
    }

    @Override
    protected void createItems() {
        Random.pushGenerator(Random.Long());
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            int pos;
            do {
                pos = randomRespawnCell(null);
            } while (pos == entrance());
            for (Item i : bonesItems) {
                drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_PLACE;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_PLACE;
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.TOMB_PALACE;

            tileW = 25;
            tileH = 30;
        }

        final int TEX_WIDTH = 25*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }


    public static class townAbove extends CustomTilemap {

        {
            texture = Assets.Environment.TOMB_PALACE;

            tileW = 25;
            tileH = 30;
        }

        final int TEX_WIDTH = 25*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }


    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if(Dungeon.depth == 20 && Statistics.Tomb_Reach && transition.type == LevelTransition.Type.REGULAR_ENTRANCE){
            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = 20;
            InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
            InterlevelScene.curTransition.destBranch = 1;
            InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
            InterlevelScene.curTransition.centerCell = -1;
            Game.switchScene(InterlevelScene.class);
            return false;
        } else {
            return super.activateTransition(hero,transition);
        }
    }

}
