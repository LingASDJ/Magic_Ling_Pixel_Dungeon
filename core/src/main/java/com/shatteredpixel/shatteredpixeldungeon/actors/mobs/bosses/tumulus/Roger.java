package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.custom.buffs.AbsoluteBlindness;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RogerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Roger extends Boss {

    public static int damageREF = 5 + Statistics.spawnersTombTownAlive;

    {
        initProperty();
        initBaseStatus(15, 30, 30, 15, 400, 0, 10);
        initStatus(30);
        spriteClass = RogerSprite.class;
    }

    @Override
    public int damageRoll() {
        int s = super.damageRoll();
        AngerPock angerPock = buff(AngerPock.class);
        if (angerPock != null && angerPock.level > 0) {
            int percent = angerPock.level / 2;
            s = Math.round(s * (1f + percent / 100f));
        }
        return s;
    }

    // ========== 罗刹斩状态 ==========
    private boolean usingRasetsu = false;
    private int rasetsuSlashCount = 0;
    private int rasetsuCooldown = 0;
    private static final int RASETSU_COOLDOWN = 20;
    private ArrayList<Integer> rasetsuQueue = new ArrayList<>();

    // 8方向偏移（横竖 + 对角线，对应图片红线）
    private int[] getSlashDirs() {
        int w = Dungeon.level.width();
        return new int[]{ -w, -w+1, 1, w+1, w, w-1, -1, -w-1 };
    }

    private static final String[] DIR_NAMES = {
            "↑", "↗", "→", "↘", "↓", "↙", "←", "↖"
    };

    private int StopAG;

    @Override
    protected boolean act() {
        if(state != SLEEPING){
            damage_reflection();
            AllEnemyPY();
            getAGP();
            if(StopAG>=6){
                getAG(-1);
                StopAG = 0;
            }
            StopAG++;
        }

        // 罗刹斩冷却
        if (rasetsuCooldown > 0) rasetsuCooldown--;

        // 正在执行罗刹斩
        if (usingRasetsu) {
            executeRasetsuSlash();
            spend(TICK);
            return true;
        }

        return super.act();
    }

    private void AllEnemyPY(){
        if(buff(AllEnemyPY.class) == null){
            Buff.affect(this, AllEnemyPY.class).set(100, 1);
        }
    }

    private void getAGP(){
        if(buff(AngerPock.class)==null){
            Buff.affect(this,AngerPock.class).set(0, 1);
        }
    }

    // ========== 罗刹斩：释放 ==========

    /**
     * 释放罗刹斩
     * 1. 玩家11回合绝对失明
     * 2. 10次有预警的贯穿斩击（横竖/斜线贯穿屏幕）
     * 3. 3倍基础伤害，非英雄即死
     */
    public void castRasetsu() {
        if (usingRasetsu || rasetsuCooldown > 0) return;

        usingRasetsu = true;
        rasetsuSlashCount = 0;
        rasetsuCooldown = RASETSU_COOLDOWN;

        // 绝对失明11回合
        Buff.affect(Dungeon.hero, AbsoluteBlindness.class).addLeft(11);
        Dungeon.observe();

        // 生成10次随机斩击方向
        rasetsuQueue.clear();
        Random.shuffle(rasetsuQueue);
        for (int i = 0; i < 10; i++) {
            rasetsuQueue.add(Random.Int(8));
        }

        // 立即预警第一次
        warnRasetsuSlash();
    }

    /**
     * 预警下一次斩击
     */
    private void warnRasetsuSlash() {
        if (rasetsuSlashCount >= rasetsuQueue.size()) {
            endRasetsu();
            return;
        }

        int dirIdx = rasetsuQueue.get(rasetsuSlashCount);

        // 显示方向预警
        if (sprite != null) {
            sprite.showStatus(CharSprite.NEGATIVE,
                    Messages.get(this, "slash_warn", DIR_NAMES[dirIdx]));
        }

        // 红色预警线
        showWarningLine(dirIdx);
    }

    /**
     * 显示红色预警线（参考图片）
     */
    private void showWarningLine(int dirIdx) {
        if (sprite == null || sprite.parent == null) return;

        int[] dirs = getSlashDirs();
        ArrayList<Integer> path = getSlashPath(dirs[dirIdx]);

        for (int cell : path) {
            if (Dungeon.level.insideMap(cell)) {
                sprite.parent.addToBack(new ColorTargetedCell(cell, Window.ORAGNECOLOR));
            }
        }
    }

    /**
     * 执行斩击
     */
    private void executeRasetsuSlash() {
        if (rasetsuSlashCount >= rasetsuQueue.size()) {
            endRasetsu();
            return;
        }

        int[] dirs = getSlashDirs();
        int dirIdx = rasetsuQueue.get(rasetsuSlashCount);
        int dir = dirs[dirIdx];

        ArrayList<Integer> path = getSlashPath(dir);

        // 斩击音效
        Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH, 1.0f);

        // 贯穿光束效果
        showSlashBeam(path);

        // 伤害判定
        for (int cell : path) {
            if (!Dungeon.level.insideMap(cell)) continue;

            Char ch = Actor.findChar(cell);
            if (ch != null && ch != this) {
                applyRasetsuDamage(ch);
            }

            // 破坏草丛
            if (Dungeon.level.map[cell] == Terrain.HIGH_GRASS
                    || Dungeon.level.map[cell] == Terrain.FURROWED_GRASS) {
                Dungeon.level.destroy(cell);
                GameScene.updateMap(cell);
            }
        }

        rasetsuSlashCount++;

        // 完成前一次后立刻预警下一次
        if (rasetsuSlashCount < rasetsuQueue.size()) {
            warnRasetsuSlash();
        } else {
            endRasetsu();
        }
    }

    /**
     * 显示斩击光束（贯穿屏幕）
     */
    private void showSlashBeam(ArrayList<Integer> path) {
        if (sprite == null || sprite.parent == null || path.isEmpty()) return;

        int start = path.get(0);
        int end = path.get(path.size() - 1);

        // 转换为像素坐标 (假设每格16像素)
        PointF s = new PointF(
                (start % Dungeon.level.width()) * 16 + 8,
                ((float) start / Dungeon.level.width()) * 16 + 8
        );
        PointF e = new PointF(
                (end % Dungeon.level.width()) * 16 + 8,
                ((float) end / Dungeon.level.width()) * 16 + 8
        );

        // 使用 DeathRay（红色光束）作为斩击视觉
        sprite.parent.add(new Beam.LightRay(s, e));

        // 路径爆炸粒子
        for (int cell : path) {
            if (Random.Float() < 0.3f) {
                CellEmitter.get(cell).burst(RainbowParticle.BURST, 6);
            }
        }
    }

    /**
     * 罗刹斩伤害
     */
    private void applyRasetsuDamage(Char ch) {
        if (ch instanceof Hero) {
            // 英雄：3倍基础真实伤害
            int dmg = damageRoll() * 3;
            ch.HP -= dmg;
            ch.sprite.showStatus(CharSprite.NEGATIVE, "%d", dmg);
            ch.sprite.flash();

            Buff.affect(ch, Cripple.class, 3f);
            CellEmitter.get(ch.pos).burst(BlastParticle.FACTORY, 5);

            if (ch.HP <= 0) {
                ch.die(this);
            }
        } else if (ch instanceof Mob) {
            // 非英雄即死
            GLog.w(Messages.get(this, "instant_kill", ch.name()));
            CellEmitter.get(ch.pos).burst(ShadowParticle.CURSE, 10);
            ch.die(this);
        }
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Dungeon.level.unseal();
    }

    /**
     * 获取贯穿路径
     */
    private ArrayList<Integer> getSlashPath(int dir) {
        ArrayList<Integer> path = new ArrayList<>();
        int w = Dungeon.level.width();

        // 正向
        int curr = pos;
        for (int i = 0; i < Math.max(w, Dungeon.level.height()); i++) {
            curr += dir;
            if (!Dungeon.level.insideMap(curr)) break;
            // 防止跨行错误
            int prevCol = (curr - dir) % w;
            int currCol = curr % w;
            if (Math.abs(currCol - prevCol) > 1 && Math.abs(currCol - prevCol) < w - 1) break;
            path.add(curr);
        }

        // 反向（插入头部）
        curr = pos;
        for (int i = 0; i < Math.max(w, Dungeon.level.height()); i++) {
            curr -= dir;
            if (!Dungeon.level.insideMap(curr)) break;
            int prevCol = (curr + dir) % w;
            int currCol = curr % w;
            if (Math.abs(currCol - prevCol) > 1 && Math.abs(currCol - prevCol) < w - 1) break;
            path.add(0, curr);
        }

        return path;
    }

    /**
     * 结束罗刹斩
     */
    private void endRasetsu() {
        usingRasetsu = false;
        rasetsuSlashCount = 0;
        rasetsuQueue.clear();

        if (Dungeon.level.heroFOV[pos]) {
            GLog.w(Messages.get(this, "rasetsu_end"));
        }

        Buff.affect(this, Weakness.class, 15f);
        Buff.affect(this, NoDr.class,15f);

        AngerPock angerPock = buff(AngerPock.class);
        if(angerPock != null){
            angerPock.level = 0;
        }
    }

    /**
     * 斩击后的0防御
     */
    public static class NoDr extends FlavourBuff{};

    // ========== 原有代码保留 ==========

    private void damage_reflection(){
        if(buff(DamageREFCD.class) == null){
            Buff.affect(this,DamageREFCD.class,28f);
            Buff.affect(this,DamageREF.class,8f);
            sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
        }
    }

    /**
     * 净化诅咒
     */
    public void cleanseCursed(){
        Sample.INSTANCE.play( Assets.Sounds.GHOST );
        damage((int) (HT*0.05f),this,DamageType.REAL);
        Buff.affect(this, Paralysis.class,8f);
    }

    public static class DamageREFCD extends FlavourBuff{}

    public static class DamageREF extends FlavourBuff{
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xA1887F);
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public String desc() {
            return Messages.get(this,"desc",damageREF,(int)visualcooldown());
        }
    }

    @Override
    public float attackDelay() {
        if(enemy != null){
            return enemy.buff(BreakDamage.class) != null ? 0.5f : super.attackDelay();
        } else {
            return super.attackDelay();
        }
    }


    /**
     * 怒气
     * @param s
     */
    private void getAG(int s){
        AngerPock angerPock = buff(AngerPock.class);
        if(angerPock != null){
            angerPock.level += s;
            if(s > 0){
                StopAG = 0;
            }
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        int X = Statistics.spawnersTombTownAlive;
        float ratio = (float) damage / enemy.HT;
        float rawTurns = (50f + 5f * X) * ratio;
        int duration = (int) Math.ceil(rawTurns);
        if (duration < 1) duration = 1;
        Buff.affect(enemy, BreakDamage.class).set(duration, 1);
        getAG(6);
        return super.attackProc(enemy, damage);
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return super.isInvulnerable(effect) || usingRasetsu;
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        if(buff(DamageREF.class) != null){
            dmg -= damageREF;
        }
        if(type != DamageType.MAGIC){
            getAG(4);
        } else {
            getAG(2);
        }

        super.damage(dmg, src, type);
    }

    public static class BreakDamage extends Buff {
        {
            type = buffType.POSITIVE;
        }

        public int level = 0;
        private int interval = 1;

        @Override
        public boolean act() {
            if (target.isAlive()) {
                spend( interval );
                if (--level <= 0) {
                    detach();
                }
            } else {
                detach();
            }
            return true;
        }

        public int level() {
            return level;
        }

        public void set( int value, int time ) {
            if (level <= value) {
                level = value;
                interval = time;
                spend(time - cooldown() - 1);
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.BREAK_DMG;
        }

        @Override
        public float iconFadePercent() {
            if (target instanceof Hero){
                float max = ((Hero) target).lvl;
                return Math.max(0, (max-level)/max);
            }
            return 0;
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(level);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
        }

        private static final String LEVEL	    = "level";
        private static final String INTERVAL    = "interval";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( INTERVAL, interval );
            bundle.put( LEVEL, level );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            interval = bundle.getInt( INTERVAL );
            level = bundle.getInt( LEVEL );
        }
    }

    public static class AllEnemyPY extends Buff {
        {
            type = buffType.POSITIVE;
        }

        public int level = 0;
        private int interval = 1;

        @Override
        public boolean act() {
            if (target.isAlive()) {
                spend( interval );
                if (level < 0) {
                    detach();
                }
            } else {
                detach();
            }
            return true;
        }

        public int level() {
            return level;
        }

        public void set( int value, int time ) {
            if (level <= value) {
                level = value;
                interval = time;
                spend(time - cooldown() - 1);
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.ALL_ENEMYPY;
        }

        @Override
        public float iconFadePercent() {
            if (target instanceof Hero){
                float max = ((Hero) target).lvl;
                return Math.max(0, (max-level)/max);
            }
            return 0;
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(level);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", 50 + 10 * Statistics.spawnersTombTownAlive, dispTurns(visualcooldown()));
        }

        private static final String LEVEL	    = "level";
        private static final String INTERVAL    = "interval";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( INTERVAL, interval );
            bundle.put( LEVEL, level );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            interval = bundle.getInt( INTERVAL );
            level = bundle.getInt( LEVEL );
        }
    }

    public static class AngerPock extends Buff {
        {
            type = buffType.POSITIVE;
        }

        public int level = 0;
        private int interval = 1;

        @Override
        public boolean act() {
            if (target.isAlive()) {
                spend( interval );
                if (level < 0) {
                    detach();
                }
                if(level >= 100){
                    if(target instanceof Roger){
                        ((Roger) target).castRasetsu();
                    }
                }
            } else {
                detach();
            }
            return true;
        }

        public int level() {
            return level;
        }

        public void set( int value, int time ) {
            if (level <= value) {
                level = value;
                interval = time;
                spend(time - cooldown() - 1);
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.AG_POCK;
        }

        private static final float DURATION = 100f;
        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - level) / DURATION);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(level);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", level, level/2);
        }

        private static final String LEVEL	    = "level";
        private static final String INTERVAL    = "interval";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( INTERVAL, interval );
            bundle.put( LEVEL, level );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            interval = bundle.getInt( INTERVAL );
            level = bundle.getInt( LEVEL );
        }
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            Camera.main.shake(1f,3f);
            GameScene.bossReady();
            yell(Messages.get(this, "notice"));
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    /**
     * 防御
     * @return
     */
    @Override
    public int drRoll() {
        int dr =  super.drRoll();
        if(buff(NoDr.class)!=null){
            dr = 0;
        }
        return dr;
    }

    public int defenseFuckAttack;

    public boolean doubleMoveNext = false;
    public boolean inDoubleMove = false;

    @Override
    public float speed() {
        float s = super.speed();
        if (inDoubleMove) {
            s *= 2;
        }
        return s;
    }

    @Override
    protected boolean getCloser(int target) {
        if (doubleMoveNext && !rooted && !inDoubleMove && target != pos) {
            doubleMoveNext = false;
            inDoubleMove = true;

            boolean moved1 = super.getCloser(target);
            boolean moved2 = false;

            if (moved1 && pos != target) {
                moved2 = super.getCloser(target);
            }

            inDoubleMove = false;
            return moved1 || moved2;
        }
        return super.getCloser(target);
    }

    private static final String RASETSU_USING = "rasetsu_using";
    private static final String RASETSU_COUNT = "rasetsu_count";
    private static final String RASETSU_CD = "rasetsu_cd";
    private static final String RASETSU_QUEUE = "rasetsu_queue";

    private static final String STOP_AGRH = "stop_agrh";
    private static final String DER = "der";
    private static final String DOUBLE_MOVE = "double_move";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(RASETSU_USING, usingRasetsu);
        bundle.put(RASETSU_COUNT, rasetsuSlashCount);
        bundle.put(RASETSU_CD, rasetsuCooldown);
        int[] queue = new int[rasetsuQueue.size()];
        for (int i = 0; i < rasetsuQueue.size(); i++) queue[i] = rasetsuQueue.get(i);
        bundle.put(RASETSU_QUEUE, queue);
        bundle.put(STOP_AGRH, StopAG);
        bundle.put(DER, defenseFuckAttack);
        bundle.put(DOUBLE_MOVE, doubleMoveNext);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        usingRasetsu = bundle.getBoolean(RASETSU_USING);
        rasetsuSlashCount = bundle.getInt(RASETSU_COUNT);
        rasetsuCooldown = bundle.getInt(RASETSU_CD);
        int[] queue = bundle.getIntArray(RASETSU_QUEUE);
        if (queue != null) {
            rasetsuQueue.clear();
            for (int v : queue) rasetsuQueue.add(v);
        }
        StopAG = bundle.getInt(STOP_AGRH);
        defenseFuckAttack = bundle.getInt(DER);
        doubleMoveNext = bundle.getBoolean(DOUBLE_MOVE);
    }
}