package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GameRules;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.Typhon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.DeadFireFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stone;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeadDogCerberusSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class DeadDogCerberus extends Boss {

    /**
     * 恶狗扑食技能参数
     */
    private int   lastEnemyPos = -1;
    private float leapCooldown =  0;
    private int   leapPos      = -1;

    /**
     * 狩猎准备技能参数
     */
    public int HunterReady = 0;
    public boolean Hunter;

    public boolean deadAlive = false;

    /**
     * 连环撕咬技能参数
     */
    public int ComboAttackCooldown;
    public boolean ComboAttackThis = true;

    //阶段推进
    private int phase = 0;

    private int AltPhase = 0;

    /**
     *  免伤用参数
     */

    public int magicDefence = 100;
    public int physicDefence = 100;

    /**
     *  听声辨位
     */
    public boolean attackHero = false;
    public int skillTime = 15;

    {
        initProperty();
        initBaseStatus(20, 60, 20, 26, 1000, 0, 0);
        initStatus(100);

        spriteClass = DeadDogCerberusSprite.class;

        ComboAttackThis = false;
        ComboAttackCooldown = 28;

        viewDistance = 31;

        HUNTING = new Hunting();

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.ACIDIC);

        immunities.add(SoulDead.class);
        immunities.add(Burning.class);
    }

    @Override
    public void notice() {
        //super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
            GameScene.flash(0x33ff0000);
            Dungeon.level.playBossMusic();
            GameScene.bossReady();
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    /**
     * Phase小于2时，Boss不可能死亡
     */
    @Override
    public boolean isAlive() {
        return phase < 2 || HP > 0;
    }

    /** 鏖战准备：没有目标时，每回合回复3生命，无上限。*/
    @Override
    public void move( int step ) {
        if(state == WANDERING){
            HP += Math.min(3, HT - HP);
        }
        super.move( step );
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {

        //Boss阶段推进
        if (phase == 0 && HP < 700) {
            phase++;
            HP = 700;
            Buff.affect(this,  RoseShiled.class, 15f);
            immunities.add(HalomethaneBurning.class);
            Buff.detach( this, HalomethaneBurning.class );
        } else if (phase == 1 && HP < 390) {
            phase++;
            HP = 390;
            immunities.add(Freezing.class);
            immunities.add(Frost.class);
            Buff.affect(this,  RoseShiled.class, 35f);
            Buff.affect(this, SuperAttack.class,SuperAttack.DURATION);
        }

        if (AltPhase == 0 && HP<500){
            AltPhase++;
            HP = 500;
            Buff.affect(this, SuperAttack.class,SuperAttack.DURATION);
        }

        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 3;
            lock.addTime(dmg*multiple);
        }

        //TODO 减伤实现
        /*
         * 固定被动·战场适应·坚韧不倒：受到伤害时，获得（伤害值）%同类型伤害减免，最多增加至50%；
         * 受到其他类型伤害后减少原减伤类型（伤害数值）%，最低减少至0%。
         * 受到大于自身生命值最大值20%的伤害时，将伤害降低至该数值。
         */

        Class<?> srcClass = src.getClass();
        HashSet<Class> resists = new HashSet<>(RingOfElements.RESISTS);
        boolean flag = false;
        for (Class c : resists){
            if (c.isAssignableFrom(srcClass)){
                flag=true;
                break;
            }
        }

        if (flag) {
            float rate = ((float) magicDefence / 100);
            dmg *= rate;
        } else if (!(src instanceof Buff)){
            float rate = ((float) physicDefence / 100);
            dmg *= rate;
        }

        dmg = Math.min(dmg,200);

        if(phase == 0){
            dmg = Math.min(dmg, 150);
        } else {
            dmg = Math.min(dmg, 150/phase);
        }

        super.damage(dmg, src, type);

        if (flag) {
            magicDefence -= dmg;
            physicDefence += dmg;
        } else if (!(src instanceof Buff))
            /* Buff伤害暂时定义为真伤吧，另外没写到元素戒指里的伤害并且伤害来源不是buff的伤害都按照物理处理了
               哦对，我把烈阳加到元素里去了，虽然讲道理现在代码的写法，敌方烈阳不会索到玩家
         */ {
            magicDefence += dmg;
            physicDefence -= dmg;
        }

        calibrate();
    }

    public void calibrate(){
        if(magicDefence > 100) magicDefence = 100;
        if(magicDefence < 50 ) magicDefence = 50;
        if(physicDefence > 100) physicDefence = 100;
        if(physicDefence < 50 ) physicDefence = 50;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 25, 60 );
    }

    /** 二阶段：磨牙吮血 */
    public void ReFlashTootehBlood() {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof BleedCrystal && Dungeon.level.distance(pos, mob.pos) <= 7) {
                sprite.parent.add(new Chains(sprite.center(),
                mob.sprite.destinationCenter(),
                Effects.Type.RED_CHAIN,
                () -> {
                    Actor.add(new Pushing(mob, mob.pos, pos,
                            () -> pullEnemy(mob, pos)));
                    Statistics.bossScores[5] -= 100;
                    next();
                }));
            }
        }
    }

    /** 常驻： */
    public void GetCryStal() {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof BleedCrystal && Dungeon.level.distance(pos, mob.pos) <= 8) {
                sprite.parent.add(new Chains(sprite.center(),
                        mob.sprite.destinationCenter(),
                        Effects.Type.RED_CHAIN,
                        () -> {
                            Actor.add(new Pushing(mob, mob.pos, pos,
                                    () -> pullEnemy(mob, pos)));
                            Statistics.bossScores[5] -= 100;
                            next();
                        }));
            }
        }
    }

    private void pullEnemy( Char enemy, int pullPos ){
        enemy.pos = pullPos;
        enemy.sprite.place(pullPos);
        enemy.die(null);
        Buff.affect(this, NoArmorDamage_BleedingNomalAttack.class, 8f);

        Buff.affect(this, Barrier.class).setShield(4);
        if(phase>=2){
            Buff.affect(this, Adrenaline.class, 3f);
        }
        Buff.affect(this, Barrier.class).setShield(4);
    }

    @Override
    public float attackDelay() {
        float delay = super.attackDelay();
        //提升100%攻速
        if(buff(SuperAttack.class) != null){
            delay /= 2;
        }
        return delay;
    }

    private boolean isAttack = false;
    @Override
    public void onAttackComplete() {
        super.onAttackComplete();

        if(ComboAttackThis){
            GameScene.scene.add(new Delayer(2f){
                @Override
                protected void onComplete() {
                    if(!isAttack){
                        ComboAttack();
                    }
                }
            });

        }
    }

    public void ComboAttack(){
        if(enemy != null && !isAttack){
            int initialDmg = damageRoll();
            initialDmg = Math.round(initialDmg * AscensionChallenge.statModifier(DeadDogCerberus.this));
            initialDmg = defenseProc(enemy, initialDmg);
            isAttack = true;
            int[] damagePercentages = {55, 40, 35, 20};

            for (int i = 0; i < (phase == 2 ? 5 : 4); i++) {
                int percentage = (i < damagePercentages.length) ? damagePercentages[i] : (int) (damageRoll() * 0.05f);
                // 如果超过数组长度，伤害设为5%
                int dmg = Math.round(initialDmg * percentage / 100.0f);
                enemy.damage(dmg, new Stone());
                Buff.affect(enemy, Bleeding.class).set(i + 2);
            }

            ComboAttackThis = false;
            ComboAttackCooldown = 15;
            isAttack = true;
        }
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if(enemy !=null) {
            //提升200%攻击力
            if (buff(SuperAttack.class) != null) {
                damage *= 4;
            }

            if (buff(SoulDead.class) != null) {
                damage *= 4;
                if (enemy == hero) {
                    for (Buff buff : hero.buffs()) {
                        if (buff instanceof ScaryBuff) {
                            ((ScaryBuff) buff).damgeScary(5);
                        } else {
                            Buff.affect(enemy, ScaryBuff.class).set((100), 5);
                        }
                    }
                }
            }

            //【二阶段】被动技能：非人狩猎
            if (phase == 1 && enemy != hero) {
                damage *= 2;
                if (enemy.HP < damage) {
                    BleedCrystal bleedCrystal = new BleedCrystal();
                    bleedCrystal.pos = enemy.pos;
                    GameScene.add(bleedCrystal);
                    Buff.affect(bleedCrystal, HaloDeadBite.class, 100f);
                    Dungeon.level.occupyCell(bleedCrystal);
                    CellEmitter.get(bleedCrystal.pos).burst(Speck.factory(Speck.EVOKE), 4);
                }
            }

            //连环撕咬 四连寄
            if (ComboAttackCooldown <= 0 && isAttack) {
                damage = 0;
                yell(Messages.get(this, "combo_attack_hit"));
                isAttack = false;
            }

            //凝血结晶只在无冷却Buff + 敌人是英雄状态下触发 此为固定技能
            if (buff(DeadDogCerberus.BleedingSummonCoolown.class) == null) {
                ArrayList<Integer> candidates = new ArrayList<>();

                int[] neighbours = {pos + 2, pos - 2, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
                for (int n : neighbours) {
                    if (!Dungeon.level.solid[n]
                            && Actor.findChar(n) == null
                            && (Dungeon.level.passable[n] || Dungeon.level.avoid[n])
                            && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[n])) {
                        candidates.add(n);
                    }
                }

                if (!candidates.isEmpty()) {
                    if (enemy == hero) {
                        Buff.affect(this, BleedingSummonCoolown.class, BleedingSummonCoolown.DURATION);
                        BleedCrystal bleedCrystal = new BleedCrystal();
                        bleedCrystal.pos = Random.element(candidates);
                        GameScene.add(bleedCrystal);
                        Dungeon.level.occupyCell(bleedCrystal);
                        CellEmitter.get(bleedCrystal.pos).burst(Speck.factory(Speck.EVOKE), 4);
                    }
                }

            }

            if (buff(CerberusBless.class) != null || buff(NoArmorDamage_BleedingNomalAttack.class) != null) {
                damage -= enemy.drRoll() / 2;
            }

            //普攻流血 1/10
            if (buff(NoArmorDamage_BleedingNomalAttack.class) != null) {
                Buff.affect(enemy, Bleeding.class).set(damage / 4f);
            }
            ScrollOfTeleportation.teleportToLocation(this, 0);
        }
        return damage;
    }


    @Override
    protected boolean act() {
        if (state == WANDERING){
            leapPos = -1;
        }

        if(enemy != null){
            if(level.distance(pos,enemy.pos)<1 && enemy != hero){
                enemy.die(null);
            } else {
                if(!deadAlive){
                    int oppositeAdjacent = hero.pos + (hero.pos - pos);
                    Ballistica trajectory = new Ballistica(hero.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
                    WandOfBlastWave.throwChar(hero, trajectory, 100, true, true, getClass());
                    deadAlive = true;
                }
            }
        }

        if(enemy == hero){
            attackHero = true;
        }else if(enemy == null){
            attackHero = false;
        }

        if(skillTime >0) skillTime --;

        //三阶段
        if(phase == 2){
            Buff.affect(hero, SoulDead.class,SoulDead.DURATION);
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                Buff.affect(mob, SoulDead.class,SoulDead.DURATION);
            }
            //听声辩位
            if (!attackHero){
                if((hero.curAction instanceof HeroAction.Move ) && skillTime == 0) {

                    skillTime = 15;

                    enemy = hero;
                    target = hero.pos;
                    enemySeen = true;
                    notice();

                    Buff.affect(this, Haste.class, 6f);
                    spend(1f);
                    yell(Messages.get(this, "dog_see_you", hero.name()));
                    if (hero.buff(Invisibility.class) != null) {
                        hero.buff(Invisibility.class).detach();
                    }
                }
            }
        }

        //全局阶段
        if(Random.Int(100) < 10 * phase){
            for (int i : PathFinder.NEIGHBOURS8) {
                if (!Dungeon.level.solid[pos + i]) {
                    CellEmitter.get(pos + i).burst(ElmoParticle.FACTORY, 5);
                    GameScene.add(Blob.seed(pos + i, 12, DeadHaloFire.class));
                }
            }
        }

        //连击手动冷却
        if(!ComboAttackThis){
            ComboAttackCooldown--;
            if(ComboAttackCooldown <= 0){
                ComboAttackThis = true;
            }
        }

        //一阶段：磨牙
        AiState lastState = state;
        boolean result = super.act();
        if (paralysed <= 0) {
            leapCooldown --;
            if(HunterReady <= 0){
                HunterReady = 15;
                Hunter = false;
                Buff.affect(this, CerberusBless.class, CerberusBless.DURATION);
            } else if(HunterReady == 1){
                Hunter = true;
                HunterReady--;
                ((DeadDogCerberusSprite) sprite).setTooteh(pos);
                GLog.n(Messages.get(this,"dog_skills_one"));
                next();
            } else {
                HunterReady--;
            }
        }
        //恶狗扑食
        if (!(lastState == WANDERING && state == HUNTING)) {
            if (enemy != null) {
                lastEnemyPos = enemy.pos;
            } else {
                lastEnemyPos = hero.pos;
            }
        }

        //二阶段：磨牙吮血
        if(phase >= 1 && buff(BloodTeethCooldown.class) == null){
            ReFlashTootehBlood();
            Buff.affect(this, BloodTeethCooldown.class,BloodTeethCooldown.DURATION);
        } else if(buff(BloodTeethCooldown.class) == null) {
            Buff.affect(this, BloodTeethCooldown.class,BloodTeethCooldown.DURATION);
            GetCryStal();
        }

        //二阶段：云开雾散
        if(phase>=1 && buff(ClearBlobs.class) == null){
            PathFinder.buildDistanceMap( lastEnemyPos, BArray.not( Dungeon.level.solid, null ), 9 );
            ArrayList<Class> affectedBlobs;
            affectedBlobs = new ArrayList<>(new BlobImmunity().immunities());
            ArrayList<Blob> blobs = new ArrayList<>();
            for (Class c : affectedBlobs){
                Blob b = Dungeon.level.blobs.get(c);
                if (b != null && b.volume > 0){
                    blobs.add(b);
                }
            }

            for (int i=0; i < Dungeon.level.length(); i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE) {

                    for (Blob blob : blobs) {
                        blob.clear(i);
                    }

                    if (Dungeon.level.heroFOV[i]) {
                        CellEmitter.get( i ).burst( Speck.factory( Speck.DISCOVER ), 2 );
                    }

                }
            }
            Buff.affect(this, ClearBlobs.class,ClearBlobs.DURATION);
        }

        return result;
    }

    private static final String LAST_ENEMY_POS = "last_enemy_pos";
    private static final String LEAP_POS = "leap_pos";
    private static final String LEAP_CD = "leap_cd";

    private static final String HUNTER_COOLDOWN  = "hunter_cooldown";
    private static final String HUNTER_BOOLEAN   = "hunter_boolean";

    private static final String COMBO_ATTACK_COOLDOWN = "combo_attack_cooldown";
    private static final String COMBO_ATTACK_BOOLEAN  = "combo_attack_boolean";

    private static final String PHASE = "phase";
    private static final String ALT_PHASE = "alt_phase";
    private static final String SKILLTIME = "skilltime";
    private static final String ATTACKHERO = "attackhero";
    private static final String MAGICDEFENSE = "magicdefense";
    private static final String PHYSICDEFENSE = "physicdefense";

    private static final String DEADALIVE = "deadalive";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LAST_ENEMY_POS, lastEnemyPos);
        bundle.put(LEAP_POS, leapPos);
        bundle.put(LEAP_CD, leapCooldown);

        bundle.put(DEADALIVE, deadAlive);

        bundle.put(HUNTER_COOLDOWN,HunterReady);
        bundle.put(HUNTER_BOOLEAN, Hunter);

        bundle.put(COMBO_ATTACK_COOLDOWN, ComboAttackCooldown);
        bundle.put(COMBO_ATTACK_BOOLEAN, ComboAttackThis);

        bundle.put(PHASE, phase);
        bundle.put(ALT_PHASE, AltPhase);

        bundle.put(SKILLTIME,skillTime);
        bundle.put(ATTACKHERO,attackHero);
        bundle.put(MAGICDEFENSE,magicDefence);
        bundle.put(PHYSICDEFENSE,physicDefence);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        deadAlive = bundle.getBoolean(DEADALIVE);

        skillTime = bundle.getInt(SKILLTIME);
        attackHero = bundle.getBoolean(ATTACKHERO);
        magicDefence = bundle.getInt(MAGICDEFENSE);
        physicDefence = bundle.getInt(PHYSICDEFENSE);

        lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
        leapPos = bundle.getInt(LEAP_POS);
        leapCooldown = bundle.getFloat(LEAP_CD);

        HunterReady = bundle.getInt(HUNTER_COOLDOWN);
        Hunter = bundle.getBoolean(HUNTER_BOOLEAN);

        ComboAttackCooldown = bundle.getInt(COMBO_ATTACK_COOLDOWN);
        ComboAttackThis = bundle.getBoolean(COMBO_ATTACK_BOOLEAN);

        phase = bundle.getInt(PHASE);

        AltPhase = bundle.getInt(ALT_PHASE);

        if(phase == 1){
            immunities.add(HalomethaneBurning.class);
        } else if(phase == 2){
            immunities.add(Freezing.class);
            immunities.add(Frost.class);
        }

        if (state != SLEEPING) BossHealthBar.assignBoss(this);
    }

    @Override
    public int drRoll() {

        FireSuperDr fireSuperDr = buff(FireSuperDr.class);
        int NormalDr = Random.NormalIntRange(0, 15);
        if(fireSuperDr != null){
            NormalDr *= 3;
        }

        return NormalDr;
    }

    /**
     * 恶狗扑食技能需要重写Hunting
     */
    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            if (leapPos != -1){

                leapCooldown = 15;

                if (rooted){
                    leapPos = -1;
                    return true;
                }

                Ballistica b = new Ballistica(pos, leapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                leapPos = b.collisionPos;

                final Char leapVictim = Actor.findChar(leapPos);
                final int endPos;

                //ensure there is somewhere to land after leaping
                if (leapVictim != null){
                    int bouncepos = -1;
                    //attempt to bounce in free passable space
                    for (int i : PathFinder.NEIGHBOURS8){
                        if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, leapPos+i) < Dungeon.level.trueDistance(pos, bouncepos))
                                && Actor.findChar(leapPos+i) == null && Dungeon.level.passable[leapPos+i]){
                            bouncepos = leapPos+i;
                        }
                    }
                    //try again, allowing a bounce into any non-solid terrain
                    if (bouncepos == -1){
                        for (int i : PathFinder.NEIGHBOURS8){
                            if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, leapPos+i) < Dungeon.level.trueDistance(pos, bouncepos))
                                    && Actor.findChar(leapPos+i) == null && !Dungeon.level.solid[leapPos+i]){
                                bouncepos = leapPos+i;
                            }
                        }
                    }
                    //if no valid position, cancel the leap
                    if (bouncepos == -1) {
                        leapPos = -1;
                        return true;
                    } else {
                        endPos = bouncepos;
                    }
                } else {
                    endPos = leapPos;
                }

                //do leap
                sprite.visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos] || Dungeon.level.heroFOV[endPos];
                sprite.jump(pos, leapPos, new Callback() {
                    @Override
                    public void call() {
                        if(enemy != null){
                            if (leapVictim != null && alignment != leapVictim.alignment){
                                if (hit( DeadDogCerberus.this, leapVictim, Char.INFINITE_ACCURACY, false)) {
                                    int dmg = damageRoll();
                                    dmg *= Random.NormalIntRange(2,3);
                                    dmg = Math.round(dmg * AscensionChallenge.statModifier(DeadDogCerberus.this));
                                    dmg = defenseProc(enemy,dmg);
                                    dmg -= enemy.drRoll();
                                    enemy.damage(dmg, new Stone());
                                    Buff.affect(leapVictim, Bleeding.class).set(0.75f * damageRoll());
                                    leapVictim.sprite.flash();
                                    Sample.INSTANCE.play(Assets.Sounds.HIT);

                                    Statistics.bossScores[5] -= 200;

                                    int targetingPos = enemy.pos;

                                    if(phase ==  2){
                                        for (int i : PathFinder.CIRCLE8) {
                                            if (!Dungeon.level.solid[targetingPos + i]) {
                                                CellEmitter.get(targetingPos + i).burst(ElmoParticle.FACTORY, 5);
                                                GameScene.add(Blob.seed(targetingPos + i, 12, DeadHaloFire.class));
                                            }
                                        }
                                    } else {
                                        for (int i : PathFinder.NEIGHBOURS8) {
                                            if (!Dungeon.level.solid[targetingPos + i]) {
                                                CellEmitter.get(targetingPos + i).burst(ElmoParticle.FACTORY, 5);
                                                GameScene.add(Blob.seed(targetingPos + i, 12, DeadHaloFire.class));
                                            }
                                        }
                                    }
                                } else {
                                    leapVictim.sprite.showStatus( CharSprite.NEUTRAL, leapVictim.defenseVerb() );
                                    Sample.INSTANCE.play(Assets.Sounds.MISS);
                                    int targetingPos = enemy.pos;
                                    if(phase ==  2){
                                        for (int i : PathFinder.CIRCLE8) {
                                            if (!Dungeon.level.solid[targetingPos + i]) {
                                                CellEmitter.get(targetingPos + i).burst(ElmoParticle.FACTORY, 5);
                                                GameScene.add(Blob.seed(targetingPos + i, 12, DeadHaloFire.class));
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (endPos != leapPos){
                            Actor.add(new Pushing( DeadDogCerberus.this, leapPos, endPos));
                        }

                        pos = endPos;
                        leapPos = -1;
                        sprite.idle();
                        Dungeon.level.occupyCell( DeadDogCerberus.this);
                        next();
                    }
                });
                return false;
            }

            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

                return doAttack( enemy );

            } else {

                if (enemyInFOV) {
                    target = enemy.pos;
                } else if (enemy == null) {
                    state = WANDERING;
                    target = Dungeon.level.randomDestination(  DeadDogCerberus.this );
                    return true;
                }

                if (leapCooldown <= 0 && enemyInFOV && !rooted
                        && Dungeon.level.distance(pos, enemy.pos) >= 3) {

                    int targetPos = enemy.pos;
                    if (lastEnemyPos != enemy.pos){
                        int closestIdx = 0;
                        for (int i = 1; i < PathFinder.CIRCLE8.length; i++){
                            if (Dungeon.level.trueDistance(lastEnemyPos, enemy.pos+PathFinder.CIRCLE8[i])
                                    < Dungeon.level.trueDistance(lastEnemyPos, enemy.pos+PathFinder.CIRCLE8[closestIdx])){
                                closestIdx = i;
                            }
                        }
                        targetPos = enemy.pos + PathFinder.CIRCLE8[(closestIdx+4)%8];
                    }

                    Ballistica b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    //try aiming directly at hero if aiming near them doesn't work
                    if (b.collisionPos != targetPos && targetPos != enemy.pos){
                        targetPos = enemy.pos;
                        b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    }
                    if (b.collisionPos == targetPos){
                        //get ready to leap
                        leapPos = targetPos;
                        //don't want to overly punish players with slow move or attack speed
                        spend(GameMath.gate(attackDelay(), (int)Math.ceil(enemy.cooldown()), 3*attackDelay()));
                        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]){
                            GLog.w(Messages.get( DeadDogCerberus.this, "leap"));
                            sprite.parent.addToBack(new TargetedCell(leapPos, 0xFF0000));
                            ((DeadDogCerberusSprite)sprite).FlyAttack( leapPos );
                            hero.interrupt();
                        }
                        return true;
                    }
                }

                int oldPos = pos;
                if (target != -1 && getCloser( target )) {

                    spend( 1 / speed() );
                    return moveSprite( oldPos,  pos );

                } else {
                    spend( TICK );
                    if (!enemyInFOV) {
                        sprite.showLost();
                        state = WANDERING;
                        target = Dungeon.level.randomDestination(  DeadDogCerberus.this );
                    }
                    return true;
                }
            }
        }

    }

    /**
     * 狗子在这里面获得3倍防御加成
     */
    public static class DeadHaloFire extends Blob {

        @Override
        protected void evolve() {

            boolean[] flamable = Dungeon.level.flamable;
            int cell;
            int fire;

            Freezing freeze = (Freezing)Dungeon.level.blobs.get( Freezing.class );

            HalomethaneFire haloclear = (HalomethaneFire)Dungeon.level.blobs.get( HalomethaneFire.class );

            boolean observe = false;

            for (int i = area.left-1; i <= area.right; i++) {
                for (int j = area.top-1; j <= area.bottom; j++) {
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0) {

                        if (freeze != null && freeze.volume > 0 && freeze.cur[cell] > 0){
                            freeze.clear(cell);
                            off[cell] = cur[cell] = 0;
                            continue;
                        } else if (haloclear != null && haloclear.volume > 0 && haloclear.cur[cell] > 0){
                            haloclear.clear(cell);
                            off[cell] = cur[cell] = 0;
                            continue;
                        }

                        burn( cell );

                        fire = cur[cell] - 1;
                        if (fire <= 0 && flamable[cell]) {

                            Dungeon.level.destroy( cell );

                            observe = true;
                            GameScene.updateMap( cell );

                        }

                    } else if (freeze == null || freeze.volume <= 0 || freeze.cur[cell] <= 0 ||
                            haloclear == null || haloclear.volume <= 0 || haloclear.cur[cell] <= 0) {

                        if (flamable[cell]
                                && (cur[cell-1] > 0
                                || cur[cell+1] > 0
                                || cur[cell-Dungeon.level.width()] > 0
                                || cur[cell+Dungeon.level.width()] > 0)) {
                            fire = 4;
                            burn( cell );
                            area.union(i, j);
                        } else {
                            fire = 0;
                        }

                    } else {
                        fire = 0;
                    }

                    volume += (off[cell] = fire);
                }
            }

            if (observe) {
                Dungeon.observe();
            }
        }



        public static void burn( int pos ) {
            Char ch = Actor.findChar( pos );
            if (ch != null && !ch.isImmune(Fire.class)) {
                if(ch instanceof DeadDogCerberus){
                    Buff.affect( ch, FireSuperDr.class).set((2), 1);
                    ch.HP += Math.min(2 * ((DeadDogCerberus) ch).phase, ch.HT - ch.HP);
                    if(ch.HP != ch.HT){
                        ch.sprite.showStatusWithIcon(CharSprite.POSITIVE, "5", FloatingText.HEALING);
                    }
                } else {
                    ch.damage( 5,new DeadDogCerberus() );
                }
            }

            Heap heap = Dungeon.level.heaps.get( pos );
            if (heap != null) {
                heap.burn();
            }

            Plant plant = Dungeon.level.plants.get( pos );
            if (plant != null){
                plant.wither();
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use( emitter );
            emitter.pour(DeadFireFlameParticle.FACTORY, 0.05f );
        }

        @Override
        public String tileDesc() {
            return Messages.get(this, "desc");
        }
    }

    /**
     * 近战攻击获得50%穿甲【TODO】与50%精准
     */
    public static class CerberusBless extends FlavourBuff {

        public static final float DURATION	= 10f;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.INVERT_MARK;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xFF8C00);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }

    public static class BleedingSummonCoolown extends FlavourBuff {

        public static final float DURATION	= 10f;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xF08080);
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }

    public static class HaloDeadBite extends FlavourBuff {

        public static final float DURATION	= 10f;

        {
            type = buffType.POSITIVE;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.ORAGNECOLOR);
        }

        @Override
        public int icon() {
            return BuffIndicator.CORRUPT;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }

    public static class BloodTeethCooldown extends FlavourBuff {
        public static final float DURATION	= 15f;
        {
            type = buffType.POSITIVE;
        }
    }

    public static class ClearBlobs extends FlavourBuff {
        public static final float DURATION	= 15f;
        {
            type = buffType.POSITIVE;
        }
    }

    public static class SuperAttack extends FlavourBuff {
        public static final float DURATION	= 20f;
        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.CHALLENGE;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.R_COLOR);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    public static class SoulDead extends FlavourBuff {
        public static final float DURATION	= 100f;
        {
            type = buffType.POSITIVE;
        }
        @Override
        public int icon() {
            return BuffIndicator.CORRUPT;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.CBLACK);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }




    public static class NoArmorDamage_BleedingNomalAttack extends FlavourBuff {
        public static final float DURATION	= 8f;
        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.INVERT_MARK;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.WATA_COLOR);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    /** 狗子基准命中率 */
    @Override
    public int attackSkill( Char target ) {
        if(ComboAttackThis && phase<2){
            return 20;
        }
        return 48;
    }


    @Override
    public void die( Object cause ) {
        super.die( cause );
        Dungeon.level.unseal();
        if(Statistics.bossRushMode){
            GetBossLoot(pos);
        }
        Badges.KILL_DOG();
        GameScene.bossSlain();

        GameRules.PropsScore();

        Statistics.bossScores[5] += 6000;

        Buff.detach(hero, SoulDead.class);

        Statistics.defalult_deaddog = true;

        Typhon typhonn = new Typhon();
        typhonn.pos = 354;
        GameScene.add(typhonn);
    }
}