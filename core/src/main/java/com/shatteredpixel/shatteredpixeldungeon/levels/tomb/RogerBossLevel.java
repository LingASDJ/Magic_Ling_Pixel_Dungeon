package com.shatteredpixel.shatteredpixeldungeon.levels.tomb;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_WELL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.GRASS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus.Roger;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing.VirtualActor;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RogerBossLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 8;
        extraGlass = false;
    }

    private static final int WIDTH = 19;
    private static final int HEIGHT = 19;

    private static final int S = CHASM;
    private static final int R = WATER;
    private static final int E = EMPTY_SP;
    private static final int A = ENTRANCE;
    private static final int B = EXIT;
    private static final int W = WALL;
    private static final int H = GRASS;
    private static final int P = STATUE;
    private static final int K = STATUE_SP;
    private static final int G = EMPTY_WELL;
    private static final int[] code_map = {
            S,S,S,S,S,S,S,S,E,E,E,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,E,B,E,W,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,E,E,E,W,S,S,S,S,S,S,S,
            S,S,S,S,S,W,W,W,E,E,E,W,W,W,S,S,S,S,S,
            S,S,S,S,W,W,E,E,E,E,E,E,E,W,W,S,S,S,S,
            S,S,S,W,W,E,K,E,H,E,R,E,K,E,W,W,S,S,S,
            S,S,S,W,P,E,E,H,R,R,R,E,E,E,P,W,S,S,S,
            S,S,S,W,E,E,E,E,R,E,E,H,E,E,E,W,S,S,S,
            S,S,S,W,E,E,H,E,E,E,E,H,H,E,E,W,S,S,S,
            S,S,S,W,P,H,H,E,E,E,E,E,E,E,P,W,S,S,S,
            S,S,S,W,E,E,R,R,E,E,E,E,E,E,E,W,S,S,S,
            S,S,S,W,E,E,E,R,E,E,H,H,R,E,E,W,S,S,S,
            S,S,S,W,P,H,E,E,R,E,H,R,R,E,P,W,S,S,S,
            S,S,S,W,W,E,K,E,E,E,E,R,K,E,W,W,S,S,S,
            S,S,S,S,W,W,E,E,E,E,E,E,E,W,W,S,S,S,S,
            S,S,S,S,S,W,W,W,E,A,E,W,W,W,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,E,W,W,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,W,W,W,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
    };

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {
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
                    InterlevelScene.curTransition.destBranch = 0;
                    InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                    InterlevelScene.curTransition.centerCell  = -1;
                    Game.switchScene( InterlevelScene.class );
                }
            });
            return false;
        } else if(transition.type == LevelTransition.Type.REGULAR_EXIT) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                    if (timeFreeze != null) timeFreeze.disarmPresses();
                    Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                    if (timeBubble != null) timeBubble.disarmPresses();
                    InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                    InterlevelScene.curTransition = new LevelTransition();
                    InterlevelScene.curTransition.destDepth = depth;
                    InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_ENTRANCE;
                    InterlevelScene.curTransition.destBranch = 1;
                    InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_ENTRANCE;
                    InterlevelScene.curTransition.centerCell  = -1;
                    Game.switchScene( InterlevelScene.class );
                }
            });
            return false;
        }
        return false;
    }

    //TODO 等待正式音乐到达
    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.TOMB_CACHE, true);
    }

    @Override
    public void playBossMusic() {
        Music.playModeBGM("music/tomb/royal.ogg", true);
    }

    @Override
    public void seal() {
        super.seal();

        Level.set(entrance(), STATUE);
        GameScene.updateMap(entrance());
        Dungeon.observe();

        Level.set(exit(), STATUE);
        GameScene.updateMap(exit());
        Dungeon.observe();

        //开场动画只播放一次；动画结束后才生成罗杰（未生成前不持久化）
        if (!introStarted && !hasRoger() && !rogerDefeated) {
            introStarted = true;
            //seal()可能由Actor线程中的移动触发，动画统一放到渲染线程启动
            Game.runOnRenderThread(this::startIntro);
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

    private int status = 0;
    private boolean introStarted = false;
    private boolean rogerDefeated = false;
    private long introStartTime = 0L;
    private MagicCircleGroup circleGroup = null;
    private Emitter circleEmitter = null;
    private Emitter summonEmitter = null;

    //地图上、下两排共10根柱子：6根STATUE + 4根STATUE_SP
    private static final int[] PILLARS = {
            118, 128, 175, 185, 232, 242,   //左右两侧
            101, 107, 253, 259              //上下两排
    };
    private static final int BOSS_SPAWN = 180;

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put("level_status", status);
        bundle.put("roger_intro", introStarted);
        bundle.put("roger_defeated", rogerDefeated);
    }

    /** 罗杰被击败后调用：禁止移动/读档时再次生成。 */
    public void onRogerDefeated() {
        rogerDefeated = true;
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        status = bundle.getInt("level_status");
        introStarted = bundle.getBoolean("roger_intro");
        rogerDefeated = bundle.getBoolean("roger_defeated");

        //若存档发生在开场动画途中（罗杰尚未生成），读档后重播开场动画。
        //场景可能还没创建，逐帧重试直到GameScene与英雄精灵就绪。
        if (introStarted && status > 0 && !hasRoger() && !rogerDefeated) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    if (Dungeon.level == null || !introStarted || hasRoger()) {
                        return;
                    }
                    if (Dungeon.hero == null || Dungeon.hero.sprite == null) {
                        Game.runOnRenderThread(this);
                        return;
                    }
                    startIntro();
                }
            });
        }
    }

    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell(ch);

        if (status == 0 && level.distance(hero.pos,294) >= 2) {
            status++;
            seal();
            CellEmitter.get(entrance).start(FlameParticle.FACTORY, 0.1f, 10);
            return;
        }

        //动画被窗口调整/场景重建打断时，玩家移动后重播（不依赖Actor线程）
        if (introStarted && !hasRoger() && !rogerDefeated
                && Game.realTime - introStartTime > 4000L) {
            Game.runOnRenderThread(() -> {
                if (introStarted && Dungeon.level != null && !hasRoger() && !rogerDefeated) {
                    startIntro();
                }
            });
        }
    }

    /**
     * 罗杰开场：10根柱子向八芒星顶点注入能量，地面由代码绘制八芒星法阵；
     * 召唤完成时法阵放大淡出，罗杰由汇聚的粒子凝聚成形。
     */
    private void startIntro() {
        introStartTime = Game.realTime;

        //重播前清理上一次可能残留的视觉
        if (circleGroup != null) {
            circleGroup.destroyNow();
            circleGroup = null;
        }
        if (summonEmitter != null) {
            summonEmitter.killAndErase();
            summonEmitter = null;
        }

        float cx = (BOSS_SPAWN % width() + 0.5f) * DungeonTilemap.SIZE;
        float cy = (BOSS_SPAWN / width() + 0.5f) * DungeonTilemap.SIZE;
        float radius = 40f;

        PointF[] vertices = new PointF[8];
        for (int i = 0; i < 8; i++) {
            double a = Math.toRadians(i * 45);
            vertices[i] = new PointF(cx + (float) Math.cos(a) * radius,
                    cy + (float) Math.sin(a) * radius);
        }

        // 10根柱子：粒子喷发，能量束射向八芒星顶点而非中心
        for (int i = 0; i < PILLARS.length; i++) {
            int pillar = PILLARS[i];
            CellEmitter.get(pillar).start(ElmoParticle.FACTORY, 0.05f, 30);
            addEnergyBeam(pillar, vertices[i % vertices.length]);
        }
        CellEmitter.get(BOSS_SPAWN).start(ElmoParticle.FACTORY, 0.03f, 24);

        buildMagicCircle(cx, cy, vertices);

        // 轻微暗屏，营造仪式感（不再使用闪电）
        GameScene.flash(0x55000000);
        Camera.main.shake(0.8f, 1.2f);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP, 1f);

        // 能量汇聚完成后进入召唤
        VirtualActor.delay(1.6f, this::beginSummon);
    }

    /**
     * 用代码绘制八芒星法阵：外圈、内圈、八芒星线条，并让粒子沿星形边流动。
     */
    private void buildMagicCircle(float cx, float cy, PointF[] vertices) {
        circleGroup = new MagicCircleGroup(cx, cy);

        // 八芒星 {8/3}
        for (int i = 0; i < 8; i++) {
            circleGroup.addLine(vertices[i], vertices[(i + 3) % 8]);
        }

        // 外圈与内圈
        for (int i = 0; i < 8; i++) {
            circleGroup.addLine(vertices[i], vertices[(i + 1) % 8]);

            PointF innerA = new PointF(cx + (vertices[i].x - cx) * 0.5f,
                    cy + (vertices[i].y - cy) * 0.5f);
            PointF innerB = new PointF(cx + (vertices[(i + 1) % 8].x - cx) * 0.5f,
                    cy + (vertices[(i + 1) % 8].y - cy) * 0.5f);
            circleGroup.addLine(innerA, innerB);
        }

        // 沿八芒星边流动的粒子（粒子使用世界坐标，Emitter留在原点）
        MagicCircleParticle.setCenter(cx, cy);
        circleEmitter = new Emitter();
        circleEmitter.start(MagicCircleParticle.FACTORY, 0.02f, 300);
        circleGroup.add(circleEmitter);

        if (hero != null && hero.sprite != null && hero.sprite.parent != null) {
            hero.sprite.parent.add(circleGroup);
        }
    }

    /**
     * 召唤最后阶段：法阵放大淡出，粒子从四周向中心汇聚成罗杰。
     */
    private void beginSummon() {
        if (circleGroup != null) {
            circleGroup.startFadeOut();
        }
        //停止法阵粒子发射，已存在的粒子会在寿命内自然淡出
        if (circleEmitter != null) {
            circleEmitter.killAndErase();
            circleEmitter = null;
        }

        float cx = (BOSS_SPAWN % width() + 0.5f) * DungeonTilemap.SIZE;
        float cy = (BOSS_SPAWN / width() + 0.5f) * DungeonTilemap.SIZE;

        SummonParticle.setCenter(cx, cy);
        summonEmitter = new Emitter();
        summonEmitter.start(SummonParticle.FACTORY, 0.01f, 120);
        if (hero != null && hero.sprite != null && hero.sprite.parent != null) {
            hero.sprite.parent.add(summonEmitter);
        }

        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP, 1f);

        // 法阵淡出、粒子汇聚的同时生成罗杰
        spawnRoger();

        VirtualActor.delay(1.2f, this::finishSummon);
    }

    private void finishSummon() {
        if (summonEmitter != null) {
            summonEmitter.killAndErase();
            summonEmitter = null;
        }
        if (circleGroup != null) {
            circleGroup.destroyNow();
            circleGroup = null;
        }
    }

    private void addEnergyBeam(int from, PointF to) {
        int w = width();
        PointF a = new PointF(from % w + 0.5f, from / w + 0.5f).scale(DungeonTilemap.SIZE);

        BeamCustom beam = new BeamCustom(a, to, Effects.Type.DEATH_RAY)
                .setLifespan(2.0f)
                .setColor(0x22EE66);

        if (hero != null && hero.sprite != null && hero.sprite.parent != null) {
            hero.sprite.parent.add(beam);
        }
    }

    /**
     * 罗杰淡入：粒子汇聚期间从透明平滑变为完全可见。
     */
    public static class RogerFader extends Visual {

        private static final float DURATION = 1.2f;

        private final Roger roger;
        private float life = 0f;

        public RogerFader(Roger roger) {
            super(0, 0, 0, 0);
            this.roger = roger;
        }

        @Override
        public void update() {
            super.update();

            life += Game.elapsed;
            float a = Math.min(1f, life / DURATION);
            if (roger.sprite != null) {
                roger.sprite.alpha(a);
            }

            if (life >= DURATION) {
                if (roger.sprite != null) {
                    roger.sprite.alpha(1f);
                }
                killAndErase();
            }
        }
    }

    /**
     * 八芒星法阵：线条由发光方块组成，召唤结束时整体放大并淡出。
     */
    public static class MagicCircleGroup extends Group {

        private static final float FADE_TIME = 1.2f;

        private final float cx;
        private final float cy;
        private final ArrayList<ColorBlock> blocks = new ArrayList<>();
        private final ArrayList<float[]> rel = new ArrayList<>();

        private boolean fading = false;
        private float fadeTime = 0f;

        public MagicCircleGroup(float cx, float cy) {
            this.cx = cx;
            this.cy = cy;
        }

        public void addLine(PointF a, PointF b) {
            float dx = b.x - a.x;
            float dy = b.y - a.y;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            int steps = Math.max(1, (int) (len / 2f));

            for (int i = 0; i <= steps; i++) {
                float t = i / (float) steps;
                ColorBlock block = new ColorBlock(2, 2, 0xCC66FF99);
                float x = a.x + dx * t - 1f;
                float y = a.y + dy * t - 1f;
                block.x = x;
                block.y = y;
                add(block);
                blocks.add(block);
                rel.add(new float[]{x - cx, y - cy});
            }
        }

        public void startFadeOut() {
            fading = true;
        }

        public void destroyNow() {
            destroy();
            if (parent != null) {
                parent.remove(this);
            }
        }

        @Override
        public void update() {
            super.update();
            if (!fading) return;

            fadeTime += Game.elapsed;
            float p = Math.min(1f, fadeTime / FADE_TIME);
            float scale = 1f + p * 1.4f;
            float alpha = 1f - p;

            for (int i = 0; i < blocks.size(); i++) {
                ColorBlock block = blocks.get(i);
                float[] r = rel.get(i);
                block.x = cx + r[0] * scale;
                block.y = cy + r[1] * scale;
                block.am = alpha;
            }

            if (p >= 1f) {
                destroyNow();
            }
        }
    }

    /**
     * 八芒星法阵粒子：沿星形边缓慢流动。
     */
    public static class MagicCircleParticle extends PixelParticle.Shrinking {

        public static final Emitter.Factory FACTORY = new Emitter.Factory() {
            @Override
            public void emit(Emitter emitter, int index, float x, float y) {
                ((MagicCircleParticle) emitter.recycle(MagicCircleParticle.class)).reset(index);
            }

            @Override
            public boolean lightMode() {
                return true;
            }
        };

        public MagicCircleParticle() {
            super();
            color(0x66FF99);
            lifespan = 0.8f;
        }

        private static float centerX = 0f;
        private static float centerY = 0f;

        public static void setCenter(float x, float y) {
            centerX = x;
            centerY = y;
        }

        public void reset(int index) {
            revive();

            // 沿八芒星 {8/3} 的边随机位置生成，并沿边流动
            int edge = index % 8;
            int next = (edge + 3) % 8;
            float ax = centerX + (float) Math.cos(Math.toRadians(edge * 45)) * 40f;
            float ay = centerY + (float) Math.sin(Math.toRadians(edge * 45)) * 40f;
            float bx = centerX + (float) Math.cos(Math.toRadians(next * 45)) * 40f;
            float by = centerY + (float) Math.sin(Math.toRadians(next * 45)) * 40f;

            float t = Random.Float();
            x = ax + (bx - ax) * t;
            y = ay + (by - ay) * t;

            float len = (float) Math.sqrt((bx - ax) * (bx - ax) + (by - ay) * (by - ay));
            speed.set((bx - ax) / len * 18f, (by - ay) / len * 18f);

            left = lifespan;
            size = 2.5f;
            am = 1f;
        }

        @Override
        public void update() {
            super.update();
            //随剩余寿命平滑淡出
            am = Math.max(0f, left / lifespan);
        }
    }

    /**
     * 召唤粒子：从四周向法阵中心汇聚，罗杰由这些粒子凝聚而成。
     */
    public static class SummonParticle extends PixelParticle.Shrinking {

        public static final Emitter.Factory FACTORY = new Emitter.Factory() {
            @Override
            public void emit(Emitter emitter, int index, float x, float y) {
                ((SummonParticle) emitter.recycle(SummonParticle.class)).reset();
            }

            @Override
            public boolean lightMode() {
                return true;
            }
        };

        public SummonParticle() {
            super();
            color(0x66FF99);
            lifespan = 0.9f;
        }

        private static float centerX = 0f;
        private static float centerY = 0f;

        public static void setCenter(float x, float y) {
            centerX = x;
            centerY = y;
        }

        public void reset() {
            revive();

            float angle = Random.Float((float) (Math.PI * 2));
            float radius = Random.Float(50f, 85f);
            float ox = (float) Math.cos(angle) * radius;
            float oy = (float) Math.sin(angle) * radius;

            x = centerX + ox;
            y = centerY + oy;

            // 0.9秒内汇聚到中心
            speed.set(-ox / lifespan, -oy / lifespan);

            left = lifespan;
            size = 3f;
            am = 1f;
        }

        @Override
        public void update() {
            super.update();
            //随剩余寿命平滑淡出
            am = Math.max(0f, left / lifespan);
        }
    }

    private void spawnRoger() {
        //关卡尚未挂载时不能生成（读档早期）
        if (Dungeon.level == null) return;
        if (hasRoger()) return;

        Roger roger = new Roger();
        roger.pos = BOSS_SPAWN;
        roger.state = roger.WANDERING;
        Buff.affect(roger, Frost.class,1f);

        GameScene.add(roger);

        //从不可见平滑淡入（与法阵淡出、粒子汇聚同步）
        if (roger.sprite != null) {
            roger.sprite.visible = true;
            roger.sprite.alpha(0f);
            if (hero != null && hero.sprite != null && hero.sprite.parent != null) {
                hero.sprite.parent.add(new RogerFader(roger));
            } else {
                roger.sprite.alpha(1f);
            }
        }

        //场景已就绪时才立即登场；读档早期由 Roger.act() 兜底
        if (hero != null && hero.sprite != null) {
            roger.notice();
        } else {
            //读档早期场景尚未创建：先挂到Actor时间轴，等场景恢复后正常行动
            Actor.addDelayed(roger, 0.1f);
        }
    }

    /** 让罗杰在法阵淡出时显形。 */
    private void revealRoger() {
        for (Char ch : Actor.chars()) {
            if (ch instanceof Roger && ch.sprite != null) {
                ch.sprite.visible = true;
                ch.sprite.flash();
            }
        }
    }

    private boolean hasRoger() {
        for (Char ch : Actor.chars()) {
            if (ch instanceof Roger) return true;
        }
        return false;
    }

    @Override
    protected boolean build() {
        feeling = Feeling.NONE;
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int enter = 28;
        LevelTransition entrance = new LevelTransition(this, enter, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(entrance);

        int entra = 294;
        LevelTransition exit = new LevelTransition(this, entra, LevelTransition.Type.REGULAR_ENTRANCE);
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

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.TOMB_HALL;

            tileW = 19;
            tileH = 19;
        }

        final int TEX_WIDTH = 19*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_TOMB;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_TOMB;
    }

    public static class townAbove extends CustomTilemap {

        {
            texture = Assets.Environment.TOMB_HALL;

            tileW = 19;
            tileH = 19;
        }

        final int TEX_WIDTH = 19*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

}
