package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.EXSG;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Revealed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.custom.buffs.AbsoluteBlindness;
import com.shatteredpixel.shatteredpixeldungeon.custom.buffs.ZeroDefense;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLightningShiledX;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RogerSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Roger extends Boss {

    /** 战吼固定减伤：5 + X（X = 已探索楼层中未摧毁的死亡晶塔数）。 */
    public static int warCryDR() {
        return 5 + Statistics.spawnersTombTownAlive;
    }

    /**
     * 千军辟易：罗杰存活时，其敌人（非 ENEMY 阵营）的移动/攻击速度系数。
     * 英雄不是 Mob，不受影响。
     */
    public static float enemySlowFactor(Mob mob) {
        if (mob == null || mob.alignment == Char.Alignment.ENEMY) return 1f;
        if (!isRogerAlive()) return 1f;
        float slow = (50f + 10f * Statistics.spawnersTombTownAlive) / 100f;
        return Math.max(0.1f, 1f - slow);
    }

    public static boolean isRogerAlive() {
        if (Dungeon.level == null) return false;
        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof Roger && mob.isAlive()) return true;
        }
        return false;
    }

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

    @Override
    public void die(Object cause) {
        super.die(cause);
        Dungeon.level.unseal();
    }

    private int StopAG = 0;
    private boolean warCryTriggered = false;
    private int noMeleeTurns = 0;

    @Override
    protected boolean act() {
        //罗刹斩期间罗杰自身不再移动、不进行普通攻击，只维持斩击
        if (buff(LuoShaSlash.class) != null) {
            spend(TICK);
            return true;
        }

        if (state != SLEEPING) {
            //战吼：冷却结束立刻再次释放
            damage_reflection();
            AllEnemyPY();
            getAGP();

            //怒气：连续6回合没有获得怒气后，每回合-1
            if (StopAG >= 6) {
                getAG(-1);
            }
            StopAG++;

            //掠袭：连续3回合没有成功近战命中，下一次移动距离加倍
            noMeleeTurns++;
            if (noMeleeTurns >= 3 && !doubleMoveNext && !inDoubleMove) {
                doubleMoveNext = true;
            }
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

    //禁止复制药水
    public static final List<Class<? extends Potion>> BAN_POTIONS = new ArrayList<>();
    static {
        BAN_POTIONS.add(PotionOfStrength.class);
        BAN_POTIONS.add(PotionOfHealing.class);
        BAN_POTIONS.add(PotionOfExperience.class);
    }

    //正向药水列表（罗杰自身生效）
    public static final List<Class<? extends Potion>> POSITIVE_POTIONS = new ArrayList<>();
    static {
        POSITIVE_POTIONS.add(PotionOfMindVision.class);
        POSITIVE_POTIONS.add(PotionOfHaste.class);
        POSITIVE_POTIONS.add(PotionOfInvisibility.class);
        POSITIVE_POTIONS.add(PotionOfLevitation.class);
        POSITIVE_POTIONS.add(PotionOfPurity.class);
        POSITIVE_POTIONS.add(PotionOfLightningShiledX.class);
    }

    //负面药水列表（预警后投掷英雄）
    public static final List<Class<? extends Potion>> NEGATIVE_POTIONS = new ArrayList<>();
    static {
        NEGATIVE_POTIONS.add(PotionOfFrost.class);
        NEGATIVE_POTIONS.add(PotionOfLiquidFlame.class);
        NEGATIVE_POTIONS.add(PotionOfToxicGas.class);
        NEGATIVE_POTIONS.add(PotionOfParalyticGas.class);
        NEGATIVE_POTIONS.add(PotionOfLiquidFlameX.class);
    }

    public static final Map<Class<? extends Potion>, Class<? extends FlavourBuff>> POTION_IMMUNE_MAP = new HashMap<>();
    static {
        POTION_IMMUNE_MAP.put(PotionOfFrost.class, ImmuneFrost.class);
        POTION_IMMUNE_MAP.put(PotionOfLiquidFlame.class, ImmuneLiquidFlame.class);
        POTION_IMMUNE_MAP.put(PotionOfToxicGas.class, ImmuneToxicGas.class);
        POTION_IMMUNE_MAP.put(PotionOfParalyticGas.class, ImmuneParalyticGas.class);
        POTION_IMMUNE_MAP.put(PotionOfLiquidFlameX.class, ImmuneLiquidFlameX.class);
    }

    // ========== 全套免疫Buff ==========
    public static class ImmuneFrost extends FlavourBuff {
        {
            immunities.add(Frost.class);
            immunities.add(Chill.class);
        }
    }
    public static class ImmuneLiquidFlame extends FlavourBuff {
        {
            immunities.add(Burning.class);
            immunities.add(Fire.class);
        }
    }
    public static class ImmuneToxicGas extends FlavourBuff {
        {
            immunities.add(ToxicGas.class);
        }
    }
    public static class ImmuneParalyticGas extends FlavourBuff {
        {
            immunities.add(Paralysis.class);
        }
    }
    public static class ImmuneLiquidFlameX extends FlavourBuff {
        {
            immunities.add(HalomethaneBurning.class);
            immunities.add(HalomethaneFire.class);
        }
    }


    public static class ThrowPotionWarning extends FlavourBuff {
        public Class<? extends Potion> potionCls;
        private float scale = 1f;
        private int targetCell = -1;
        private boolean warned = false;

        private static final String POTION_CLS = "potion_cls";
        private static final String SCALE = "scale";
        private static final String TARGET_CELL = "target_cell";
        private static final String WARNED = "warned";

        public void setPotion(Class<? extends Potion> cls, float scale, int targetCell){
            this.potionCls = cls;
            this.scale = scale;
            this.targetCell = targetCell;
        }

        @Override
        public boolean act() {
            Roger roger = target instanceof Roger ? (Roger) target : null;
            if (roger == null || !roger.isAlive()) {
                detach();
                return true;
            }

            //第一回合预警，第二回合投掷
            if (!warned) {
                warned = true;
                spend(TICK);
                return true;
            }

            //复制品扔向罗杰当前的敌人（英雄），而不是英雄原本的投掷落点
            int cell = (roger.enemy != null && roger.enemy.isAlive())
                    ? roger.enemy.pos : -1;
            if (cell < 0 && Dungeon.hero != null && Dungeon.hero.isAlive()) {
                cell = Dungeon.hero.pos;
            }

            if (cell >= 0 && potionCls != null) {
                try {
                    final Potion pot = potionCls.newInstance();
                    final Roger owner = roger;
                    final int targetPos = cell;
                    final float s = scale;
                    roger.sprite.turnTo(roger.pos, targetPos);
                    ((MissileSprite) roger.sprite.parent.recycle(MissileSprite.class))
                            .reset(roger.pos, targetPos, pot, new Callback() {
                                @Override
                                public void call() {
                                    //复制品只产生 (50+5X)% 效果
                                    owner.shatterScaled(pot, targetPos, s);
                                    Sample.INSTANCE.play(Assets.Sounds.HIT);
                                }
                            });
                    Sample.INSTANCE.play(Assets.Sounds.ATK_SPIRITBOW);
                } catch (Exception ignored) {
                }
            }
            detach();
            return true;
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POTION_CLS, potionCls == null ? "" : potionCls.getName());
            bundle.put(SCALE, scale);
            bundle.put(TARGET_CELL, targetCell);
            bundle.put(WARNED, warned);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            try {
                potionCls = (Class<? extends Potion>) Class.forName(bundle.getString(POTION_CLS));
            } catch (Exception ignored) {
            }
            scale = bundle.contains(SCALE) ? bundle.getFloat(SCALE) : 1f;
            targetCell = bundle.getInt(TARGET_CELL);
            warned = bundle.getBoolean(WARNED);
        }
    }


    /**
     * 外部调用入口：英雄使用药水（drink = 饮用，thrown = 投掷）。
     */
    public void onHeroUsePotion(Potion usedPotion, boolean thrown, int targetCell){
        if (!isAlive() || state == SLEEPING) return;

        Class<? extends Potion> potCls = usedPotion.getClass();
        //禁止复制
        for (Class<?> ban : BAN_POTIONS){
            if (ban.isAssignableFrom(potCls)) return;
        }

        int X = Statistics.spawnersTombTownAlive;
        float scale = (50f + 5f * X) / 100f;
        boolean negative = isNegativePotion(potCls);
        boolean positive = isPositivePotion(potCls);
        if (!negative && !positive) return;

        if (thrown) {
            //复制“投掷”这一使用方式：预警一回合后把复制品扔向英雄本人
            ThrowPotionWarning warn = Buff.affect(this, ThrowPotionWarning.class);
            warn.setPotion(potCls, scale, -1);
        } else {
            //复制“饮用”这一使用方式：罗杰自己喝下复制品
            if (negative) {
                shatterScaled(usedPotion, pos, scale);
            } else {
                applyScaledPotionEffect(this, usedPotion, scale);
            }
        }

        //复制品会造成负面效果时，罗杰获得对应免疫5回合
        if (negative) {
            Class<? extends FlavourBuff> immune = POTION_IMMUNE_MAP.get(potCls);
            if (immune != null) {
                Buff.affect(this, immune, 5f);
            }
        }
    }

    private static boolean isNegativePotion(Class<?> cls) {
        for (Class<?> neg : NEGATIVE_POTIONS) {
            if (neg.isAssignableFrom(cls)) return true;
        }
        return false;
    }

    private static boolean isPositivePotion(Class<?> cls) {
        for (Class<?> pos : POSITIVE_POTIONS) {
            if (pos.isAssignableFrom(cls)) return true;
        }
        return false;
    }

    /**
     * 按比例缩放药水产生的区域效果：先记录场上同类 Blob 的总体积，
     * 再让药水正常炸开，最后把本次新增的体积乘以 scale。
     */
    private void shatterScaled(Potion potion, int cell, float scale) {
        Map<Class<? extends Blob>, Integer> before = new HashMap<>();
        for (Blob b : Dungeon.level.blobs.values()) {
            before.put(b.getClass(), b.volume);
        }

        potion.shatter(cell);

        for (Blob b : Dungeon.level.blobs.values()) {
            Integer old = before.get(b.getClass());
            if (old == null) {
                b.volume = Math.max(1, Math.round(b.volume * scale));
            } else if (b.volume > old) {
                b.volume = old + Math.max(1, Math.round((b.volume - old) * scale));
            }
        }
    }

    /**
     * 给目标施加按比例削弱的药水效果（正向药水 / 罗杰自己饮用复制品）。
     */
    private void applyScaledPotionEffect(Char target, Potion origin, float scale){
        if (origin instanceof PotionOfHaste){
            if(Dungeon.isChallenged(EXSG)) {
                Buff.affect(target, Cripple.class, 8f * scale);
            } else {
                Buff.prolong(target, Haste.class, Haste.DURATION * scale);
                SpellSprite.show(target, SpellSprite.HASTE, 1, 1, 0);
            }
        }else if (origin instanceof PotionOfPurity){
            Buff.prolong(target, BlobImmunity.class, BlobImmunity.DURATION  * scale);
        }else if (origin instanceof PotionOfMindVision){
            if(Dungeon.isChallenged(EXSG)) {
                Buff.affect(target, Blindness.class, 5f * scale);
            } else {
                Buff.affect(target, MindVision.class, MindVision.DURATION * scale );
            }
        }else if (origin instanceof PotionOfInvisibility){
            if (Dungeon.isChallenged(EXSG)) {
                Buff.detach(target, Invisibility.class);
                Buff.affect(target, Revealed.class, Revealed.DURATION * scale);
            } else {
                Buff.affect(target, Invisibility.class, Invisibility.DURATION * scale );
            }
        }else if (origin instanceof PotionOfLevitation){
            Buff.affect(target, Levitation.class, Levitation.DURATION * scale );
        }else if (origin instanceof PotionOfLightningShiledX){
            Buff.affect(target, ChampionHero.Light.class, ChampionHero.DURATION/2 * scale);
        }
    }


    private void damage_reflection(){
        if (state == SLEEPING) return;
        if(buff(DamageREFCD.class) == null){
            triggerWarCry();
        }
    }

    private void triggerWarCry(){
        if (!isAlive() || buff(DamageREFCD.class) != null) return;

        Buff.affect(this, DamageREFCD.class, 28f);
        Buff.affect(this, DamageREF.class, 8f);
        if (sprite != null) {
            sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
        }
        Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
    }

    /**
     * 净化诅咒
     */
    public void cleanseCursed(){
        Sample.INSTANCE.play( Assets.Sounds.GHOST );
        damage((int) (HT*0.05f),this,DamageType.REAL);
        Buff.affect(this, Paralysis.class,8f);
    }

    public static class DamageREFCD extends FlavourBuff{
        @Override
        public String desc() {
            return Messages.get(this, "desc", (int) visualcooldown());
        }
    }

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
            return Messages.get(this,"desc", warCryDR(), (int)visualcooldown());
        }
    }

    @Override
    public float attackDelay() {
        float delay = super.attackDelay();
        //嗜血：攻击处于裂伤或流血状态的目标时，攻击速度+50%
        if (enemy != null && (enemy.buff(BreakDamage.class) != null || enemy.buff(Bleeding.class) != null)) {
            delay /= 1.5f;
        }
        return delay;
    }

    /**
     * 怒气
     */
    private void getAG(int s){
        AngerPock angerPock = buff(AngerPock.class);
        if(angerPock != null){
            angerPock.level = Math.max(0, Math.min(100, angerPock.level + s));
            if(s > 0){
                StopAG = 0;
            }
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        int X = Statistics.spawnersTombTownAlive;
        float ratio = (float) damage / Math.max(1, enemy.HT);
        int duration = (int) Math.ceil((50f + 5f * X) * ratio);
        if (duration < 1) duration = 1;
        //裂伤：多次近战命中会叠加剩余时长
        Buff.affect(enemy, BreakDamage.class).stack(duration);
        getAG(8);
        noMeleeTurns = 0;
        return super.attackProc(enemy, damage);
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        //战吼：固定伤害减免（真实伤害无视）
        if(buff(DamageREF.class) != null && type != DamageType.REAL){
            dmg = Math.max(0, dmg - warCryDR());
        }
        //怒气：被攻击命中+4，法术攻击减半
        if (src instanceof Char && src != this) {
            getAG(6);
        }

        super.damage(dmg, src, type);
    }

    public static class BreakDamage extends Buff {
        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        public int level = 0;
        private int maxLevel = 0;

        @Override
        public boolean act() {
            if (target.isAlive()) {
                spend( TICK );
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

        /**
         * 叠加裂伤：多次近战命中会延长剩余时长（上限60回合）。
         */
        public void stack(int value) {
            if (value < 1) value = 1;
            level = Math.min(level + value, 200);
            maxLevel = Math.max(maxLevel, level);
        }

        /**
         * 兼容旧调用：语义与 {@link #stack(int)} 相同。
         */
        public void set(int value, int time) {
            stack(value);
        }

        /** 移动触发时的真实伤害：基础1点，剩余时长每6回合+1。 */
        public int moveDamage() {
            return 1 + level / 6;
        }

        @Override
        public int icon() {
            return BuffIndicator.BREAK_DMG;
        }

        @Override
        public float iconFadePercent() {
            return maxLevel <= 0 ? 1f : Math.max(0, (maxLevel - level) / (float) maxLevel);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(level);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", 1 + level / 6, level);
        }

        private static final String LEVEL       = "level";
        private static final String MAX_LEVEL   = "max_level";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( LEVEL, level );
            bundle.put( MAX_LEVEL, maxLevel );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            level = bundle.getInt( LEVEL );
            maxLevel = bundle.contains(MAX_LEVEL) ? bundle.getInt(MAX_LEVEL) : level;
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
        public String desc() {
            return Messages.get(this, "desc", 50 + 10 * Statistics.spawnersTombTownAlive, dispTurns(visualcooldown()));
        }

        private static final String LEVEL       = "level";
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
                //怒气满100时释放罗刹斩（同一时间只会有一个）
                if(level >= 100 && target instanceof Roger
                        && target.buff(LuoShaSlash.class) == null){
                    Buff.affect(target, LuoShaSlash.class);
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

        private static final String LEVEL       = "level";
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
        //战斗开始时立刻战吼
        if (!warCryTriggered) {
            warCryTriggered = true;
            triggerWarCry();
        }
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
            noMeleeTurns = 0;
            inDoubleMove = true;

            boolean moved1 = super.getCloser(target);
            boolean moved2 = false;

            if (moved1 && pos != target) {
                moved2 = super.getCloser(target);
            }

            inDoubleMove = false;
            return moved1;
        }
        return super.getCloser(target);
    }

    private static final String STOP_AGRH = "stop_agrh";
    private static final String DER = "der";
    private static final String DOUBLE_MOVE = "double_move";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STOP_AGRH, StopAG);
        bundle.put(DER, defenseFuckAttack);
        bundle.put(DOUBLE_MOVE, doubleMoveNext);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        StopAG = bundle.getInt(STOP_AGRH);
        defenseFuckAttack = bundle.getInt(DER);
        doubleMoveNext = bundle.getBoolean(DOUBLE_MOVE);
    }

    /**
     * 罗刹：11回合绝对失明，之后10次有预警的贯穿斩击。
     * 每次斩击贯穿整张地图的一条直线（横/竖/斜），命中英雄造成3倍基础伤害，
     * 非英雄单位即死；结束后清空怒气并陷入15回合虚弱与破防。
     */
    public static class LuoShaSlash extends Buff {

        private static final int TOTAL_SLASHES = 10;
        private static final int BLIND_TURNS = 11;
        //10次斩击使用10个互不相同的角度（0~162度，每18度一条线）
        private static final int[] ANGLES = {0, 18, 36, 54, 72, 90, 108, 126, 144, 162};

        private int slashesLeft = TOTAL_SLASHES;
        private boolean warning = false;
        private int lockedCell = -1;
        private int angle = 0;
        private int[] line = new int[0];
        private boolean started = false;
        //尚未使用的角度，确保10次斩击角度不重复
        private final ArrayList<Integer> remainingAngles = new ArrayList<>();

        {
            type = buffType.NEGATIVE;
            announced = true;
            for (int a : ANGLES) {
                remainingAngles.add(a);
            }
            Random.shuffle(remainingAngles);
        }

        @Override
        public boolean act() {
            spend(TICK);

            if (!started) {
                started = true;
                //失明与斩击同时开始；罗杰在此期间停止移动与普通攻击
                if (target instanceof Roger) {
                    if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
                        Buff.affect(Dungeon.hero, AbsoluteBlindness.class)
                                .addLeft(BLIND_TURNS - 1f);
                        BArray.setFalse(Dungeon.level.visited);
                        BArray.setFalse(Dungeon.level.mapped);
                        Dungeon.level.addVisuals();
                        Dungeon.observe();
                    }
                    Camera.main.shake(1.5f, 0.4f);
                }
                //第一回合先预警第一条线，下一回合斩出并预警下一条
                prepareNext();
                renderWarning();
                warning = true;
                return true;
            }

            if (warning) {
                fireSlash();
                warning = false;
                slashesLeft--;
                if (slashesLeft <= 0) {
                    detach();
                    return true;
                }
                prepareNext();
                renderWarning();
                warning = true;
            }

            return true;
        }

        private void prepareNext() {
            Char hero = Dungeon.hero;
            lockedCell = hero != null && hero.isAlive() ? hero.pos
                    : (target != null ? target.pos : -1);
            if (lockedCell < 0) {
                return;
            }

            //从尚未使用的角度里挑一个，保证10次斩击角度互不相同；
            //若该角度会把英雄所有可走的相邻格都封死，则换一个角度。
            for (int attempt = 0; attempt < ANGLES.length; attempt++) {
                if (remainingAngles.isEmpty()) {
                    for (int a : ANGLES) {
                        remainingAngles.add(a);
                    }
                }
                int idx = Random.Int(remainingAngles.size());
                int a = remainingAngles.remove(idx);

                int[] candidate = computeLine(lockedCell, a);
                if (hasEscape(lockedCell, candidate)) {
                    angle = a;
                    line = candidate;
                    return;
                }
                //不安全，放回去换个角度
                remainingAngles.add(a);
            }

            //极端情况（英雄被地形围死）：退回水平线
            angle = 0;
            line = computeLine(lockedCell, 0);
        }

        /**
         * 检查英雄周围是否至少有一个可走、未被斩击线覆盖的空格。
         * 保证玩家总能在预警回合移动到安全区域。
         */
        private boolean hasEscape(int heroCell, int[] slashLine) {
            HashSet<Integer> covered = new HashSet<>();
            for (int cell : slashLine) {
                covered.add(cell);
            }

            int w = Dungeon.level.width();
            int heroX = heroCell % w;
            int heroY = heroCell / w;

            for (int[] d : new int[][]{{-1,0},{1,0},{0,-1},{0,1},{-1,-1},{1,-1},{-1,1},{1,1}}) {
                int x = heroX + d[0];
                int y = heroY + d[1];
                if (x < 0 || x >= w || y < 0 || y >= Dungeon.level.height()) continue;

                int cell = y * w + x;
                if (!Dungeon.level.passable[cell] || Dungeon.level.solid[cell]) continue;
                if (Actor.findChar(cell) != null) continue;
                if (!covered.contains(cell)) return true;
            }
            return false;
        }

        private void renderWarning() {
            if (line.length == 0) return;
            if (target == null || target.sprite == null || target.sprite.parent == null) return;

            PointF from = lineEnd(lockedCell, angle, -1f);
            PointF to = lineEnd(lockedCell, angle, 1f);

            //粗线：多层平行光束叠加，预警更醒目
            for (int i = -2; i <= 2; i++) {
                addBeamLayer(from, to, i * 2.2f, i == 0 ? Window.CBLACK : Window.CYELLOW, 0.9f);
            }
            //沿线火花
            for (int cell : line) {
                CellEmitter.center(cell).burst(Speck.factory(Speck.LIGHT), 1);
            }

            Camera.main.shake(0.7f, 0.08f);
            Sample.INSTANCE.play(Assets.Sounds.SCAN, 0.8f);
        }

        private void fireSlash() {
            if (line.length == 0) return;
            if (target == null || target.sprite == null || target.sprite.parent == null) return;

            PointF from = lineEnd(lockedCell, angle, -1f);
            PointF to = lineEnd(lockedCell, angle, 1f);

            //斩击本体：更粗更亮的红色光束
            for (int i = -3; i <= 3; i++) {
                addBeamLayer(from, to, i * 2.2f, i == 0 ? 0xFFFFFF : 0xFF1010, 1.2f);
            }

            Camera.main.shake(2.2f, 0.35f);
            Sample.INSTANCE.play(Assets.Sounds.SCAN, 1.4f);

            for (int cell : line) {
                Char ch = Actor.findChar(cell);
                if (ch == null || ch == target || !ch.isAlive()) continue;

                if (ch == Dungeon.hero) {
                    ch.damage(target.damageRoll() * 3, LuoShaSlash.class, DamageType.PHYSICAL);
                    ch.sprite.flash();
                    CellEmitter.center(cell).burst(RainbowParticle.BURST, Random.IntRange(6, 10));
                    if (!ch.isAlive()) {
                        Dungeon.fail(getClass());
                    }
                } else {
                    //非英雄单位即死
                    ch.die(true);
                    CellEmitter.center(cell).burst(RainbowParticle.BURST, Random.IntRange(4, 8));
                }
            }
        }

        private void addBeamLayer(PointF from, PointF to, float offset, int color, float lifespan) {
            float dx = to.x - from.x;
            float dy = to.y - from.y;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            if (len <= 0f) return;

            float nx = -dy / len * offset;
            float ny = dx / len * offset;

            BeamCustom beam = new BeamCustom(
                    new PointF(from.x + nx, from.y + ny),
                    new PointF(to.x + nx, to.y + ny),
                    Effects.Type.DEATH_RAY).setLifespan(lifespan).setColor(color);
            target.sprite.parent.add(beam);
        }

        /** 斩击线在地图外的两个端点（世界坐标），用于绘制任意角度的贯穿光束。 */
        private PointF lineEnd(int cell, int angleDeg, float sign) {
            int w = Dungeon.level.width();
            int h = Dungeon.level.height();
            float cx = cell % w + 0.5f;
            float cy = cell / w + 0.5f;
            double rad = Math.toRadians(angleDeg);
            float len = (w + h) * 1.2f;

            return new PointF(
                    (cx + (float) Math.cos(rad) * len * sign) * DungeonTilemap.SIZE,
                    (cy + (float) Math.sin(rad) * len * sign) * DungeonTilemap.SIZE);
        }

        /** 计算穿过指定格子的贯穿直线，支持任意角度（0~162度）。 */
        private static int[] computeLine(int cell, int angleDeg) {
            int w = Dungeon.level.width();
            int h = Dungeon.level.height();
            float cx = cell % w + 0.5f;
            float cy = cell / w + 0.5f;
            double rad = Math.toRadians(angleDeg);
            float dx = (float) Math.cos(rad);
            float dy = (float) Math.sin(rad);

            HashSet<Integer> cells = new HashSet<>();
            int max = w + h;
            for (int t = -max; t <= max; t++) {
                int gx = Math.round(cx + dx * t);
                int gy = Math.round(cy + dy * t);
                if (gx >= 0 && gx < w && gy >= 0 && gy < h) {
                    cells.add(gy * w + gx);
                }
            }

            int[] result = new int[cells.size()];
            int i = 0;
            for (int c : cells) {
                result[i++] = c;
            }
            return result;
        }

        @Override
        public void detach() {
            super.detach();
            if (target == null) return;
            Buff.affect(target, Weakness.class, 15f);
            Buff.affect(target, ZeroDefense.class, 15f);
            AngerPock ag = target.buff(AngerPock.class);
            if (ag != null) {
                ag.level = 0;
            }
        }

        private static final String SLASHES = "slashes";
        private static final String WARNING = "warning";
        private static final String LOCKED = "locked";
        private static final String ANGLE = "angle";
        private static final String STARTED = "started";
        private static final String ANGLES_LEFT = "angles_left";

        @Override
        public void storeInBundle(Bundle b) {
            super.storeInBundle(b);
            b.put(SLASHES, slashesLeft);
            b.put(WARNING, warning);
            b.put(LOCKED, lockedCell);
            b.put(ANGLE, angle);
            b.put(STARTED, started);

            int[] left = new int[remainingAngles.size()];
            for (int i = 0; i < left.length; i++) {
                left[i] = remainingAngles.get(i);
            }
            b.put(ANGLES_LEFT, left);
        }

        @Override
        public void restoreFromBundle(Bundle b) {
            super.restoreFromBundle(b);
            slashesLeft = b.getInt(SLASHES);
            warning = b.getBoolean(WARNING);
            lockedCell = b.getInt(LOCKED);
            angle = b.getInt(ANGLE);
            started = b.getBoolean(STARTED);

            if (b.contains(ANGLES_LEFT)) {
                remainingAngles.clear();
                for (int a : b.getIntArray(ANGLES_LEFT)) {
                    remainingAngles.add(a);
                }
            }
            if (lockedCell >= 0) {
                line = computeLine(lockedCell, angle);
            }
        }
    }
}
