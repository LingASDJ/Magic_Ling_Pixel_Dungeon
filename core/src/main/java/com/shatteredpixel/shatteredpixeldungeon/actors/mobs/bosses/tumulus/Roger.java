package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.EXSG;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BallisticaReal;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing.VirtualActor;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ScanningBeam;
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
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //处理负面药水投掷倒计时Buff
        ThrowPotionWarning throwBuff = buff(ThrowPotionWarning.class);
        if (throwBuff != null){
            throwBuff.processThrow(this);
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

    public void castRasetsu() {
        if(enemy != null){
        }
    }

    /**
     * 斩击后的0防御
     */
    public static class NoDr extends FlavourBuff{};

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
        private static final String POTION_CLS = "potion_cls";

        public void setPotion(Class<? extends Potion> cls){
            potionCls = cls;
        }

        public void processThrow(Roger roger){
            spend(TICK);
            if (roger.enemy != null){
                final int fromCell = roger.pos;
                final int targetCell = roger.enemy.pos;
                try {
                    final Potion pot = potionCls.newInstance();
                    roger.sprite.turnTo(fromCell, targetCell);
                    ((MissileSprite) roger.sprite.parent.recycle(MissileSprite.class))
                            .reset(fromCell, targetCell, pot, new Callback() {
                                @Override
                                public void call() {
                                    // 飞行结束，药水爆炸
                                    pot.shatter(targetCell);
                                    // 添加5回合免疫Buff
                                    Class<? extends FlavourBuff> immune = POTION_IMMUNE_MAP.get(potionCls);
                                    if (immune != null) {
                                        Buff.affect(roger, immune, 5f);
                                    }
                                    Sample.INSTANCE.play(Assets.Sounds.HIT);
                                }
                            });
                    Sample.INSTANCE.play(Assets.Sounds.ATK_SPIRITBOW);
                } catch (Exception ignored) {

                }
                detach();
            }
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POTION_CLS, potionCls.getName());
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            try {
                potionCls = (Class<? extends Potion>) Class.forName(bundle.getString(POTION_CLS));
            }catch (Exception ignored){}
        }
    }


    /**
     * 外部调用入口：Hero使用药水后触发此方法
     */
    public void onHeroUsePotion(Potion usedPotion){
        Class<? extends Potion> potCls = usedPotion.getClass();
        //禁止复制
        for (Class<?> ban : BAN_POTIONS){
            if (ban.isAssignableFrom(potCls)) return;
        }

        int X = Statistics.spawnersTombTownAlive;
        float baseScale = (50f + 5f * X) / 100f;

        //正向药水：自身生效
        for (Class<?> pos : POSITIVE_POTIONS){
            if (pos.isAssignableFrom(potCls)){
                applyScaledPotionEffect(this, usedPotion, baseScale);
                return;
            }
        }
        //负面药水：预警一回合后投掷
        for (Class<?> neg : NEGATIVE_POTIONS){
            if (neg.isAssignableFrom(potCls)){
                ThrowPotionWarning warn = Buff.affect(this, ThrowPotionWarning.class, 5f);
                warn.setPotion(potCls);
                return;
            }
        }
    }

    /**
     * 给目标施加按比例削弱的药水效果
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
            Buff.affect(target, Invisibility.class, Invisibility.DURATION * scale );
        }else if (origin instanceof PotionOfLevitation){
            Buff.affect(target, Levitation.class, Levitation.DURATION * scale );
        }else if (origin instanceof PotionOfLightningShiledX){
            Buff.affect(target, ChampionHero.Light.class, ChampionHero.DURATION/2 * scale);
        }
    }


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
                if(level >= 100){
                    if(target instanceof Roger){
                        if(((Roger) target).enemy != null){
                            Buff.affect(target, LuoShaSlash.class).setTarget(((Roger) target).enemy.pos);
                        } else {
                            Buff.affect(target, LuoShaSlash.class).setTarget(180);
                        }
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

    public static class LuoShaSlash extends Buff implements ScanningBeam.OnCollide {
        // Buff总剩余回合：固定8轮攻击
        private int totalTurnLeft = 8;
        // true = 当前回合绘制预警标记
        private boolean warningFrame = true;
        // 当前这一组斩击锁定的玩家格子（一旦生成就固定，不跟随移动）
        private int lockedHeroCell = -1;
        // 当前回合3条斩击角度
        private final List<Float> currentAngles = new ArrayList<>();

        // 可选角度池
        private static final float[] BASE_ANGLES = {0f, 45f, 90f, 135f, 180f, 225f, 270f, 315f};
        private static final int WARNING_COLOR = 0xFF2222;
        private static final float BEAM_RANGE = 15f;

        public LuoShaSlash setTarget(int heroCell) {
            // 第一次挂上buff直接锁定第一组的目标并生成角度
            lockedHeroCell = heroCell;
            regenerateSafeAngleSet();
            return this;
        }

        @Override
        public void storeInBundle(Bundle b) {
            super.storeInBundle(b);
            b.put("totalTurn", totalTurnLeft);
            b.put("warningFrame", warningFrame);
            b.put("lockedHeroCell", lockedHeroCell);
            float[] angles = new float[currentAngles.size()];
            for (int i = 0; i < angles.length; i++) angles[i] = currentAngles.get(i);
            b.put("angles", angles);
        }

        @Override
        public void restoreFromBundle(Bundle b) {
            super.restoreFromBundle(b);
            totalTurnLeft = b.getInt("totalTurn");
            warningFrame = b.getBoolean("warningFrame");
            lockedHeroCell = b.getInt("lockedHeroCell");
            currentAngles.clear();
            float[] arr = b.getFloatArray("angles");
            for (float a : arr) currentAngles.add(a);
        }

        @Override
        public boolean act() {
            spend(TICK);

            if (warningFrame) {
                // 绘制预警，使用【锁定的旧坐标】，不读取新hero位置
                renderWarningBeams();
                warningFrame = false;
            } else {
                // 发射激光，依然使用同一个锁定坐标！
                fireRealSlashBeams();
                totalTurnLeft--;

                if (totalTurnLeft <= 0) {
                    diactivate();
                    detach();
                    return true;
                }
                // ===== 准备下一轮攻击：重新锁定【此刻新的玩家位置】+生成新角度 =====
                lockedHeroCell = hero.pos;
                regenerateSafeAngleSet();
                warningFrame = true;
            }
            return true;
        }

        @Override
        public void detach() {
            super.detach();
            Buff.affect(target, Weakness.class, 15f);
            Buff.affect(target, NoDr.class, 15f);
            AngerPock s = target.buff(AngerPock.class);
            if(s != null){
                s.level = 0;
            }
        }

        /**
         * 根据【lockedHeroCell锁定点】生成安全角度组
         */
        private void regenerateSafeAngleSet() {
            int w = Dungeon.level.width();
            int hx = lockedHeroCell % w;
            int hy = lockedHeroCell / w;
            boolean validSet = false;

            while (!validSet) {
                currentAngles.clear();
                List<Float> pool = new ArrayList<>();
                for (float a : BASE_ANGLES) pool.add(a);
                Random.shuffle(pool);
                currentAngles.add(pool.get(0));
                currentAngles.add(pool.get(1));
                currentAngles.add(pool.get(2));

                int safeCount = 0;
                int[][] dir8 = {{-1,0},{1,0},{0,-1},{0,1},{-1,-1},{1,-1},{-1,1},{1,1}};
                for(int[] d : dir8){
                    int nx = hx + d[0];
                    int ny = hy + d[1];
                    boolean blocked = checkCellBlockedByAnyBeam(hx, hy, nx, ny, currentAngles);
                    if (!blocked) safeCount++;
                }
                if(safeCount >= 1){
                    validSet = true;
                }
            }
        }

        private boolean checkCellBlockedByAnyBeam(int sx, int sy, int tx, int ty, List<Float> angles){
            for(float ang : angles){
                double rad = Math.toRadians(ang);
                double dx = tx - sx;
                double dy = ty - sy;
                double beamDx = Math.cos(rad);
                double beamDy = Math.sin(rad);
                double cross = dx * beamDy - dy * beamDx;
                if(Math.abs(cross) < 0.35f){
                    return true;
                }
            }
            return false;
        }

        /**
         * 预警标记：全程使用锁定的旧坐标，不会跟随玩家移动
         */
        protected void renderWarningBeams() {
            int w = Dungeon.level.width();
            int h = Dungeon.level.height();
            int hx = lockedHeroCell % w;
            int hy = lockedHeroCell / w;

            for (float angle : currentAngles) {
                double rad = Math.toRadians(angle);
                float dirX = (float) Math.cos(rad);
                float dirY = (float) Math.sin(rad);

                // 光束起点（地图外侧）格子坐标
                float spawnX = (float) (hx + 0.5 - dirX * BEAM_RANGE);
                float spawnY = (float) (hy + 0.5 - dirY * BEAM_RANGE);
                // 光束终点 = 锁定玩家格子中心
                float endX = hx;
                float endY = hy;

                PointF startWorld = new PointF(spawnX, spawnY)
                        .offset(0.5f, 0.5f)
                        .scale(DungeonTilemap.SIZE);

                PointF endWorld = new PointF(endX, endY)
                        .offset(0.5f, 0.5f)
                        .scale(DungeonTilemap.SIZE);

                BeamCustom beam = new BeamCustom(startWorld, endWorld, Effects.Type.DEATH_RAY)
                        .setLifespan(0.7f)
                        .setColor(0xD471FF);

                target.sprite.parent.add(beam);
            }
        }

        /**
         * 发射激光：和预警使用**完全同一个锁定坐标**
         */
        protected void fireRealSlashBeams() {
            int w = Dungeon.level.width();
            int hx = lockedHeroCell % w;
            int hy = lockedHeroCell / w;

            ScanningBeam.setCollide(this);
            for(float angle : currentAngles){
                double rad = Math.toRadians(angle);
                float dirX = (float)Math.cos(rad);
                float dirY = (float)Math.sin(rad);

                float spawnX = (float)(hx + 0.5 - dirX * BEAM_RANGE);
                float spawnY = (float)(hy + 0.5 - dirY * BEAM_RANGE);
                float speedX = dirX * 11f;
                float speedY = dirY * 11f;

                target.sprite.parent.add(new ScanningBeam(Effects.Type.BLUE_RAY, BallisticaReal.STOP_TARGET,
                        new ScanningBeam.BeamData()
                                .setPosition(spawnX, spawnY, angle, 16f)
                                .setSpeed(speedX, speedY, 0f)
                                .setTime(0.2f, 1.2f, 0.4f)
                ).setDiameter(2.8f));
            }

            VirtualActor.delay(1.5f, ()->{
                Camera.main.shake(1.5f, 0.25f);
            });
            Camera.main.shake(1.2f, 80f);
        }

        @Override
        public int onHitProc(Char ch) {
            if(ch instanceof Roger) return 0;
            ch.damage(target.damageRoll()*3, LuoShaSlash.class,DamageType.PHYSICAL);
            ch.sprite.centerEmitter().burst(RainbowParticle.BURST, Random.IntRange(6,11));
            ch.sprite.flash();

            if(ch == hero){
                Sample.INSTANCE.play(Assets.Sounds.SCAN, Random.Float(1.0f,1.4f));
                if(!ch.isAlive()) Dungeon.fail(getClass());
            } else {
                //非英雄单位即刻死亡喵
                ch.die(true);
            }
            return 1;
        }

        @Override
        public int cellProc(int i) {
            if (i < 0 || i >= Dungeon.level.length()){
                return 0;
            }
            if(Dungeon.level.flamable[i]){
                Dungeon.level.destroy(i);
                GameScene.updateMap(i);
            }
            return 0;
        }
    }
}