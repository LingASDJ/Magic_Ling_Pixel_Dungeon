package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.DeathListener;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WhiteFlower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Wisp;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing.VirtualActor;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.tomb.PalePalaceBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EgretSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Egret extends Boss implements DeathListener {

    public static final int MAX_HP = 800;

    /** 主动2「纯白擢升之赐」设计暂缓：方法保留，但不自动释放。 */
    private static final boolean ASCEND_ENABLED = false;

    /** 技能动作锁：释放后短暂停手，避免移动动画立刻覆盖技能动画 */
    private int actionLock = 0;

    /** 施法状态：为 true 时白魔女不进行任何其他行为（由后续施法类技能使用） */
    protected boolean casting = false;

    /** 开场诞生阶段：在花中生成后暂时静止，等待回到王座 */
    private boolean introPending = false;
    private boolean introYellDone = false;

    /** 炽烈亡魂出场累计的永久防御加成 */
    private int drBonus = 0;

    private float grudgeChance = 0f;
    private boolean deathMarriageLock = false;
    private boolean resolvingDeaths = false;

    /** 三圣祝福之礼全破后的内部麻痹回合数（不挂游戏 Paralysis） */
    private int paralysisTurns = 0;

    /** 黎明颠倒之辉的施法状态 */
    private enum Cast {
        NONE, DAWN, MARRIAGE_ASCEND
    }

    private Cast castType = Cast.NONE;
    private int castTurns = 0;
    private int castTargetId = -1;
    private boolean castLinkedTriple = false;

    /** 三圣祝福之礼的球 */
    private final HashSet<Integer> holyBalls = new HashSet<>();
    private final boolean[] ballBroken = new boolean[3];
    private int ballsRemaining = 0;

    {
        initProperty();
        initBaseStatus(0, 0, 20, 15, 800, 0, 8);
        initStatus(30);
        spriteClass = EgretSprite.class;
        immunities.add(Bleeding.class);
        properties.add(Property.ICY);
    }

    @Override
    protected boolean act() {
        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()) {
            fieldOfView = new boolean[Dungeon.level.length()];
        }
        Dungeon.level.updateFieldOfView(this, fieldOfView);

        if (!isAlive()) {
            spend(TICK);
            return true;
        }

        if (introPending) {
            spend(TICK);
            return true;
        }

        // 读档/开场兜底：确保 Boss 血条与封门最终完成
        if (!BossHealthBar.isAssigned() && state != SLEEPING
                && Dungeon.hero != null && Dungeon.hero.sprite != null) {
            notice();
        }

        // 被麻痹时不会触发任何技能；麻痹动画持续循环到状态结束
        if (paralysed > 0 || paralysisTurns > 0) {
            if (sprite instanceof EgretSprite) {
                EgretSprite es = (EgretSprite) sprite;
                if (!es.isCastingParalysis()) {
                    es.castParalysis();
                }
            }
            if (paralysisTurns > 0) {
                paralysisTurns--;
            }
            spend(TICK);
            return true;
        }

        // 麻痹结束后恢复正常待机
        if (sprite instanceof EgretSprite && ((EgretSprite) sprite).isCastingParalysis()) {
            ((EgretSprite) sprite).idle();
        }

        // 施法成功/失败/圣光形态循环到下一次状态切换
        if (sprite instanceof EgretSprite && ((EgretSprite) sprite).isCastingResultAnim()) {
            ((EgretSprite) sprite).idle();
        }

        // 睡眠：等待敌人进入视野
        if (state == SLEEPING) {
            Char enemy = chooseEnemy();
            if (enemy != null && enemy.isAlive() && canSee(enemy)) {
                state = HUNTING;
                notice();
            }
            spend(TICK);
            return true;
        }

        // 施法时不进行其他行为
        if (casting) {
            advanceCasting();
            spend(TICK);
            return true;
        }

        // 技能释放后的短暂动作锁：先让技能动画播完
        if (actionLock > 0) {
            actionLock--;
            spend(TICK);
            return true;
        }

        // 主动1：身边 5x5 有敌人时发动
        if (buff(RepulseCooldown.class) == null && hasEnemyIn5x5()) {
            EnemyNoReach();
            spend(TICK);
            return true;
        }

        // 主动2：纯白擢升之赐（设计暂缓，保留方法但不自动释放）
        if (ASCEND_ENABLED && buff(AscendCooldown.class) == null) {
            Ascend();
            spend(TICK);
            return true;
        }

        // 主动3：黎明颠倒之辉
        if (buff(DawnCooldown.class) == null) {
            startDawn(false, false);
            spend(TICK);
            return true;
        }

        return movement();
    }

    @Override
    public int drRoll() {
        return super.drRoll() + drBonus;
    }

    /** 开场阶段：诞生于花中时静止，传送到王座后解除。 */
    public void setIntroPending(boolean pending) {
        introPending = pending;
    }

    /** 不会进行普通攻击。 */
    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            Camera.main.shake(1f, 3f);
            GameScene.bossReady();
        }

        if (!introYellDone) {
            introYellDone = true;
            yell(Messages.get(this, "yell_intro"));
        }
    }

    @Override
    public void die(Object cause) {
        yell(Messages.get(this, "yell_death"));

        resetCasting();
        clearHolyBalls();
        clearFlowers();

        super.die(cause);
        if (Dungeon.level instanceof PalePalaceBossLevel) {
            ((PalePalaceBossLevel) Dungeon.level).onEgretDefeated();
        }
        if (Dungeon.level != null) {
            Dungeon.level.unseal();
        }
    }

    @Override
    public boolean add(Buff buff) {
        boolean added = super.add(buff);
        if (added && buff instanceof Paralysis) {
            interruptCast();
            if (sprite instanceof EgretSprite) {
                ((EgretSprite) sprite).castParalysis();
            }
            yell(Messages.get(this, "yell_paralysis"));
        }
        return added;
    }

    /** 双保险：即使有外部逻辑调用攻击，也不会造成普攻。 */
    @Override
    public boolean attack(Char enemy) {
        return false;
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        if (dmg <= 0 || !isAlive()) {
            super.damage(dmg, src, type);
            return;
        }
        if (src == null) {
            src = this;
        }
        if (isInvulnerable(src.getClass())) {
            super.damage(dmg, src, type);
            return;
        }

        // 50% 韧性
        dmg = Math.max(1, Math.round(dmg * 0.5f));

        boolean willMarry = !casting && !deathMarriageLock && dmg >= 80;
        if (willMarry) {
            dmg = 80;
        }

        int before = HP;
        super.damage(dmg, src, type);
        int taken = Math.max(0, before - HP);

        // 参考 FireDragon：Boss 战持续锁楼层
        if (taken > 0 && Dungeon.hero != null) {
            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null) {
                if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
                    lock.addTime(taken);
                } else {
                    lock.addTime(taken * 1.5f);
                }
            }
        }

        if (willMarry && isAlive()) {
            triggerDeathMarriage();
        }

        if (taken > 0 && isAlive()) {
            // 被动4：百相质询之因
            grudgeChance += 0.10f;
            if (Random.Float() < grudgeChance) {
                spawnGrudgeWisp();
                grudgeChance = 0f;
            }

            // 麻痹状态下受到攻击：额外触发一次抵抗敌厄之拒（独立冷却）
            if (paralysed > 0 && buff(ParalysisRepulseCooldown.class) == null) {
                EnemyNoReachFromParalysis();
            }
        }

        if (isAlive()) {
            BossHealthBar.bleed(HP * 2 <= HT);
        }
    }

    /* ===== 被动1 / 被动5 / 被动6：死亡监听 ===== */

    @Override
    public void onCharDeath(Char dead) {
        if (dead == null || dead == this || !isAlive() || resolvingDeaths) {
            return;
        }

        resolvingDeaths = true;
        try {
            int total = 0;

            Hero hero = Dungeon.hero;
            if (hero != null && hero.isAlive() && dead.pos >= 0
                    && Dungeon.level.distance(dead.pos, hero.pos) <= 2) {
                int before = hero.HP;
                hero.damage(Random.NormalIntRange(4, 16), this, DamageType.MAGIC);
                total += Math.max(0, before - hero.HP);
            }

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob == this || !mob.isAlive() || dead.pos < 0) {
                    continue;
                }
                if (mob.alignment == Alignment.NEUTRAL) {
                    continue;
                }
                if (Dungeon.level.distance(dead.pos, mob.pos) > 2) {
                    continue;
                }
                int before = mob.HP;
                mob.damage(Random.NormalIntRange(4, 16), this, DamageType.MAGIC);
                total += Math.max(0, before - mob.HP);
            }

            // 被动1：灵肉桎梏之界
            if (total > 0) {
                HP = Math.min(HT, HP + total * 2);
            }

            // 被动5：千面殁亡之果
            if (HP >= HT) {
                gainShield(Math.max(1, Math.round(HT * 0.02f)));
            } else {
                HP = Math.min(HT, HP + Math.max(1, Math.round(HT * 0.01f)));
            }

            // 被动6：希声奔赴之花
            spawnFlowers(2 + Random.Int(4));

            if (sprite instanceof EgretSprite) {
                ((EgretSprite) sprite).bane();
            }
        } finally {
            resolvingDeaths = false;
        }
    }

    /* ===== 被动2：决绝告死之嫁 ===== */

    private void triggerDeathMarriage() {
        if (deathMarriageLock) {
            return;
        }
        deathMarriageLock = true;
        yell(Messages.get(this, "yell_marriage"));

        try {
            // 施法期间触发：瞬间提前完成当前施法
            if (casting) {
                if (castType == Cast.DAWN) {
                    finishDawnInstantly();
                } else {
                    resetCasting();
                    clearHolyBalls();
                }
            }

            // 直接使用游戏的 Invulnerability 状态，2 回合无敌
            Buff.affect(this, Invulnerability.class, 2f);
            if (sprite instanceof EgretSprite) {
                EgretSprite es = (EgretSprite) sprite;
                es.setOnNuptialComplete(() -> {
                    if (casting && castType == Cast.MARRIAGE_ASCEND) {
                        es.castStart();
                    } else {
                        es.idle();
                    }
                });
                es.nuptialLocked();
            }
            Camera.main.shake(1f, 3f);
            Sample.INSTANCE.play(Assets.Sounds.CHARMS);

            // 击退周围单位至 2 格外并造成 5~15 魔法伤害
            Hero hero = Dungeon.hero;
            if (hero != null && hero.isAlive()
                    && Dungeon.level.distance(pos, hero.pos) <= 2
                    && !hero.isInvulnerable(getClass())) {
                pushAway(hero, 2);
                if (hero.isAlive()) {
                    hero.damage(Random.NormalIntRange(5, 15), this, DamageType.MAGIC);
                }
            }

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob == this || !mob.isAlive()) {
                    continue;
                }
                if (mob.alignment == Alignment.NEUTRAL || mob.alignment == alignment) {
                    continue;
                }
                if (Dungeon.level.distance(pos, mob.pos) > 2) {
                    continue;
                }
                if (mob.isInvulnerable(getClass())) {
                    continue;
                }
                pushAway(mob, 2);
                if (mob.isAlive()) {
                    mob.damage(Random.NormalIntRange(5, 15), this, DamageType.MAGIC);
                }
            }

            // 开始一次额外的纯白擢升之赐，需要 2 回合施法
            casting = true;
            castType = Cast.MARRIAGE_ASCEND;
            castTurns = 2;
            castTargetId = -1;
            castLinkedTriple = countAlive(NecroWarrior.class) >= 2;
        } finally {
            deathMarriageLock = false;
        }
    }

    private void finishDawnInstantly() {
        int targetId = castTargetId;
        resetCasting();
        clearHolyBalls();

        Actor a = Actor.findById(targetId);
        if (a instanceof Char && ((Char) a).isAlive()) {
            drainLife((Char) a, 50);
        }
    }

    private int countAlive(Class<? extends Mob> type) {
        int count = 0;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (type.isInstance(mob) && mob.isAlive()) {
                count++;
            }
        }
        return count;
    }

    private boolean canSee(Char ch) {
        return ch != null
                && ch.pos >= 0
                && fieldOfView != null
                && ch.pos < fieldOfView.length
                && fieldOfView[ch.pos];
    }

    /**
     * 主动1冷却中总是尝试远离敌人；
     * 冷却就绪时靠近敌人，直到进入 5x5 触发抵抗敌厄之拒。
     */
    private boolean movement() {
        Char enemy = chooseEnemy();
        if (enemy == null || !enemy.isAlive() || enemy.pos < 0) {
            spend(TICK);
            return true;
        }

        int step = -1;
        // 只有敌人贴近时才移动：冷却中后退，其余情况原地等待。
        // 不会隔着老远主动靠近或传送。
        if (buff(RepulseCooldown.class) != null
                && Dungeon.level.distance(pos, enemy.pos) <= 2) {
            step = Dungeon.flee(this, enemy.pos, Dungeon.level.passable, fieldOfView, true);
        }

        // 用传送来更新位置和贴图，避免普通移动时白魔女贴图不刷新的问题
        if (step >= 0 && ScrollOfTeleportation.teleportToLocation(this, step)) {
            return true;
        }

        spend(TICK);
        return true;
    }

    /** 身边 5x5（切比雪夫距离 <= 2）内是否有敌人。 */
    private boolean hasEnemyIn5x5() {
        Hero hero = Dungeon.hero;
        if (isEnemyIn5x5(hero)) {
            return true;
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (isEnemyIn5x5(mob)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnemyIn5x5(Char ch) {
        return ch != null
                && ch != this
                && ch.isAlive()
                && ch.pos >= 0
                && ch.alignment != Alignment.NEUTRAL
                && ch.alignment != alignment
                && Dungeon.level.distance(pos, ch.pos) <= 2;
    }

    /* 抵抗敌厄之拒 */
    private void EnemyNoReach() {
        // 冷却做成 buff，且不显示图标（FlavourBuff 默认 icon 就是 NONE）
        Buff.affect(this, RepulseCooldown.class, RepulseCooldown.DURATION);
        actionLock = 1;
        yell(Messages.get(this, "yell_repulse"));

        if (sprite instanceof EgretSprite) {
            ((EgretSprite) sprite).playBaneDeadEnemy();
        }

        pushEnemiesAway(6);

        // 每次释放后立刻额外施展一次黎明颠倒之辉（无视冷却，且不改变其冷却）
        startDawn(true, true);
    }

    /** 麻痹状态下受击触发的额外抵抗敌厄之拒，使用独立冷却。 */
    private void EnemyNoReachFromParalysis() {
        Buff.affect(this, ParalysisRepulseCooldown.class, ParalysisRepulseCooldown.DURATION);
        yell(Messages.get(this, "yell_repulse"));

        if (sprite instanceof EgretSprite) {
            ((EgretSprite) sprite).playBaneDeadEnemy();
        }

        pushEnemiesAway(6);
        startDawn(true, true);
    }

    private void pushEnemiesAway(int power) {
        Hero hero = Dungeon.hero;
        if (isEnemyIn5x5(hero) && !hero.isInvulnerable(getClass())) {
            pushAway(hero, power);
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (!isEnemyIn5x5(mob)) {
                continue;
            }
            if (mob.isInvulnerable(getClass())) {
                continue;
            }
            pushAway(mob, power);
        }
    }

    private void pushAway(Char ch, int power) {
        int width = Dungeon.level.width();
        int dx = (ch.pos % width) - (pos % width);
        int dy = (ch.pos / width) - (pos / width);
        if (dx == 0 && dy == 0) {
            return;
        }

        // 先朝远离白魔女的方向走一格，再以该方向把目标推出去
        int cell = ch.pos + dx + dy * width;
        if (cell < 0 || cell >= Dungeon.level.length()) {
            cell = ch.pos;
        }

        Ballistica trajectory = new Ballistica(ch.pos, cell, Ballistica.MAGIC_BOLT);
        WandOfBlastWave.throwChar(ch, trajectory, power, false, false, this);
    }

    /* 主动2：纯白擢升之赐 */
    private void Ascend() {
        Buff.affect(this, AscendCooldown.class, AscendCooldown.DURATION);
        yell(Messages.get(this, "yell_ascend"));
        if (sprite instanceof EgretSprite) {
            ((EgretSprite) sprite).castSuccess();
        }

        NecroHunter hunter = findBest(NecroHunter.class);
        if (hunter == null) {
            NecroWarrior warrior = findBest(NecroWarrior.class);
            if (warrior != null) {
                evolveToHunter(warrior);
            }

            Wisp wisp = findAny(Wisp.class);
            if (wisp != null) {
                evolveWispToWarrior(wisp);
            } else {
                summonWarrior();
            }
        } else {
            evolveToWraith(hunter);
            summonWarrior();
        }
    }

    /** 词条接口：仅 STRONGER_BOSSES/精英挑战会填充，普通流程通常为 0。 */
    private int championCount(Mob mob) {
        return mob.buffs(ChampionEnemy.class).size();
    }

    /**
     * 优先选词条最多的单位；词条相同或都没有词条时，
     * 选当前生命比例最高的（状态最好）。
     */
    private <T extends Mob> T findBest(Class<T> type) {
        T best = null;
        int bestAffix = -1;
        float bestHealth = -1f;

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (!type.isInstance(mob) || !mob.isAlive()) {
                continue;
            }

            int affix = championCount(mob);
            float health = mob.HP / (float) mob.HT;

            if (affix > bestAffix
                    || (affix == bestAffix && health > bestHealth)) {
                bestAffix = affix;
                bestHealth = health;
                best = type.cast(mob);
            }
        }
        return best;
    }

    private <T extends Mob> T findAny(Class<T> type) {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (type.isInstance(mob) && mob.isAlive()) {
                return type.cast(mob);
            }
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void copyChampions(Mob from, Mob to) {
        for (ChampionEnemy ce : from.buffs(ChampionEnemy.class)) {
            Buff.affect(to, (Class) ce.getClass());
        }
    }

    private void removeMob(Mob mob) {
        if (mob.sprite != null) {
            mob.sprite.killAndErase();
        }
        Dungeon.level.mobs.remove(mob);
        Actor.remove(mob);
    }

    private void evolveToHunter(NecroWarrior old) {
        int cell = old.pos;
        NecroHunter hunter = new NecroHunter();
        hunter.pos = cell;
        hunter.state = hunter.HUNTING;
        copyChampions(old, hunter);
        removeMob(old);
        GameScene.add(hunter);
        Dungeon.level.occupyCell(hunter);
        evolutionEffect(cell, 0xBFF7F2, 10);
    }

    private void evolveWispToWarrior(Wisp old) {
        int cell = old.pos;
        NecroWarrior warrior = new NecroWarrior();
        warrior.pos = cell;
        warrior.state = warrior.HUNTING;
        copyChampions(old, warrior);
        removeMob(old);
        GameScene.add(warrior);
        Dungeon.level.occupyCell(warrior);
        evolutionEffect(cell, 0xBFF7F2, 10);
    }

    private void evolveToWraith(NecroHunter old) {
        int cell = old.pos;
        BlazingWraith wraith = new BlazingWraith();
        wraith.pos = cell;
        wraith.state = wraith.HUNTING;
        copyChampions(old, wraith);
        removeMob(old);
        GameScene.add(wraith);
        Dungeon.level.occupyCell(wraith);

        // 炽烈亡魂的转化更醒目
        evolutionEffect(cell, 0x37E3D0, 20);
        Camera.main.shake(0.6f, 2f);

        // 每当炽烈亡魂成功出现在场上，白鹭防御永久 +5
        drBonus += 5;
    }

    private void summonWarrior() {
        int cell = findSummonCell();
        if (cell < 0) {
            return;
        }

        NecroWarrior warrior = new NecroWarrior();
        warrior.pos = cell;
        warrior.state = warrior.HUNTING;
        GameScene.add(warrior);
        Dungeon.level.occupyCell(warrior);
        evolutionEffect(cell, 0x37E3D0, 8);
    }

    /** 进化/召唤共用的爆发效果。 */
    private void evolutionEffect(int cell, int color, int count) {
        CellEmitter.get(cell).burst(Speck.factory(Speck.LIGHT), count);
        CellEmitter.get(cell).burst(Speck.factory(Speck.STAR), Math.max(4, count / 2));
        Splash.at(cell, color, Math.max(4, count / 3));
        Camera.main.shake(0.35f, 1.2f);
        Sample.INSTANCE.play(Assets.Sounds.CHARMS);
    }

    private int findSummonCell() {
        for (int r = 1; r <= 6; r++) {
            ArrayList<Integer> cells = new ArrayList<>();
            for (int cell = 0; cell < Dungeon.level.length(); cell++) {
                if (cell == pos || Dungeon.level.distance(pos, cell) != r) {
                    continue;
                }
                if (Dungeon.level.solid[cell] || !Dungeon.level.passable[cell]) {
                    continue;
                }
                if (Actor.findChar(cell) != null) {
                    continue;
                }
                cells.add(cell);
            }
            if (!cells.isEmpty()) {
                return Random.element(cells);
            }
        }
        return -1;
    }

    /* ===== 主动3：黎明颠倒之辉 ===== */

    private boolean startDawn(boolean fromRepulse, boolean forceTriple) {
        Char target = chooseDawnTarget();

        if (target == null) {
            summonWarrior();
            if (!fromRepulse) {
                Buff.affect(this, DawnCooldown.class, DawnCooldown.DURATION);
            }
            return false;
        }

        if (!fromRepulse) {
            yell(Messages.get(this, "yell_dawn"));
        }

        if (forceTriple || target == Dungeon.hero) {
            spawnTripleBlessing();
        }

        casting = true;
        castType = Cast.DAWN;
        castTurns = 2;
        castTargetId = target.id();
        castLinkedTriple = forceTriple || target == Dungeon.hero;

        if (sprite instanceof EgretSprite) {
            ((EgretSprite) sprite).castStart();
        }
        if (!fromRepulse) {
            Buff.affect(this, DawnCooldown.class, DawnCooldown.DURATION);
        }
        return true;
    }

    private Char chooseDawnTarget() {
        Char best = null;
        float bestPercent = -1f;

        Hero hero = Dungeon.hero;
        if (hero != null && hero.isAlive() && hero.HP >= 50
                && !hero.isInvulnerable(getClass())) {
            best = hero;
            bestPercent = hero.HP / (float) hero.HT;
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob == this || !mob.isAlive() || mob.pos < 0) {
                continue;
            }
            if (mob.alignment == Alignment.NEUTRAL || mob.HP < 50) {
                continue;
            }
            if (mob.isInvulnerable(getClass())) {
                continue;
            }
            float percent = mob.HP / (float) mob.HT;
            if (percent > bestPercent) {
                bestPercent = percent;
                best = mob;
            }
        }
        return best;
    }

    private void advanceCasting() {
        if (!casting) {
            return;
        }
        castTurns--;
        spawnFlowers(2 + Random.Int(4));
        if (castTurns <= 0) {
            completeCast();
        }
    }

    private void completeCast() {
        Cast finished = castType;
        int targetId = castTargetId;
        boolean linkedTriple = castLinkedTriple;

        resetCasting();
        clearHolyBalls();

        if (finished == Cast.DAWN) {
            Actor a = Actor.findById(targetId);
            if (a instanceof Char && ((Char) a).isAlive()) {
                drainLife((Char) a, 50);
            }
        } else if (finished == Cast.MARRIAGE_ASCEND) {
            Ascend();
            if (linkedTriple) {
                spawnTripleBlessing();
            }
        }

        HP = Math.min(HT, HP + Math.max(1, Math.round(HT * 0.03f)));
        if (sprite instanceof EgretSprite) {
            ((EgretSprite) sprite).castSuccess();
        }
    }

    private void resetCasting() {
        casting = false;
        castType = Cast.NONE;
        castTurns = 0;
        castTargetId = -1;
        castLinkedTriple = false;
    }

    private void interruptCast() {
        if (!casting) {
            return;
        }
        resetCasting();
        clearHolyBalls();
        if (sprite instanceof EgretSprite) {
            ((EgretSprite) sprite).castFail();
        }
        damage(Math.max(1, Math.round(HT * 0.03f)), this, DamageType.REAL);
    }

    private void drainLife(Char target, int amount) {
        int before = target.HP;
        target.damage(amount, this, DamageType.REAL);
        int dealt = Math.max(0, before - target.HP);
        if (dealt > 0) {
            HP = Math.min(HT, HP + dealt);
        }
    }

    public void gainShield(int amount) {
        if (amount <= 0) {
            return;
        }
        Barrier barrier = Buff.affect(this, Barrier.class);
        barrier.incShield(amount);
    }

    private void spawnFlowers(int count) {
        // 每次触发固定生成 2~5 朵，不限制场上总数
        for (int i = 0; i < count; i++) {
            int cell = randomFlowerCell();
            if (cell >= 0) {
                WhiteFlower.plant(cell);
            }
        }
    }

    private int randomFlowerCell() {
        int length = Dungeon.level.length();
        for (int i = 0; i < 24; i++) {
            int cell = Random.Int(length);
            if (!Dungeon.level.solid[cell] && Dungeon.level.passable[cell]) {
                return cell;
            }
        }
        return -1;
    }

    private void clearFlowers() {
        WhiteFlower flowers = (WhiteFlower) Dungeon.level.blobs.get(WhiteFlower.class);
        if (flowers != null) {
            flowers.fullyClear();
            Dungeon.level.blobs.remove(WhiteFlower.class);
            Actor.remove(flowers);
        }
    }

    private void spawnGrudgeWisp() {
        int cell = findSummonCell();
        if (cell < 0) {
            return;
        }
        Wraith wraith = Wraith.spawnAt(cell);
        if (wraith != null) {
            wraith.state = wraith.HUNTING;
        }
    }

    /* ===== 被动3：三圣祝福之礼 ===== */

    private void spawnTripleBlessing() {
        clearHolyBalls();
        ballsRemaining = 0;
        for (int i = 0; i < ballBroken.length; i++) {
            ballBroken[i] = false;
        }

        int[] cells = {HOLY_BALL_TOP, HOLY_BALL_LEFT, HOLY_BALL_RIGHT};
        int[] colors = {HolyBall.RED, HolyBall.BLUE, HolyBall.GREEN};

        // 绝命头目：坐标固定，颜色随机抽取
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            for (int i = colors.length - 1; i > 0; i--) {
                int j = Random.Int(i + 1);
                int tmp = colors[i];
                colors[i] = colors[j];
                colors[j] = tmp;
            }
        }

        for (int i = 0; i < cells.length; i++) {
            int cell = nearestFreeCell(cells[i]);
            if (cell < 0) {
                continue;
            }

            HolyBall ball = new HolyBall().init(colors[i], this);
            ball.pos = cell;
            GameScene.add(ball);
            Dungeon.level.occupyCell(ball);
            holyBalls.add(ball.id());
            ballsRemaining++;
        }

        if (sprite instanceof EgretSprite) {
            ((EgretSprite) sprite).nuptial();
        }
    }

    /** 三圣球的固定坐标：上、左、右。 */
    private static final int HOLY_BALL_TOP = 387;
    private static final int HOLY_BALL_LEFT = 607;
    private static final int HOLY_BALL_RIGHT = 617;

    /** 供圣光球显示当前施法剩余回合。 */
    public int castTurns() {
        return castTurns;
    }

    public void onHolyBallBroken(int colorType) {
        if (colorType < 0 || colorType >= ballBroken.length || ballBroken[colorType]) {
            return;
        }
        ballBroken[colorType] = true;
        ballsRemaining--;

        // 每打破一个球都会延长当前施法 1 回合
        if (casting) {
            castTurns += 1;
        }

        if (ballsRemaining <= 0) {
            // 三个球均被对应方式打破：5% 真实伤害 + 打断所有技能 + 内部麻痹 3 回合
            damage(Math.max(1, Math.round(HT * 0.05f)), this, DamageType.REAL);
            interruptCast();
            paralysisTurns = 3;
            yell(Messages.get(this, "yell_triple_break"));
            if (sprite instanceof EgretSprite) {
                ((EgretSprite) sprite).castParalysis();
            }
        }
    }

    private void clearHolyBalls() {
        for (int id : holyBalls) {
            Actor a = Actor.findById(id);
            if (a instanceof HolyBall) {
                ((HolyBall) a).die(true);
            }
        }
        holyBalls.clear();
        ballsRemaining = 0;
    }

    private int cellAt(int x, int y) {
        if (x < 0 || y < 0 || x >= Dungeon.level.width() || y >= Dungeon.level.height()) {
            return -1;
        }
        return x + y * Dungeon.level.width();
    }

    private int nearestFreeCell(int cell) {
        if (isFreeCell(cell)) {
            return cell;
        }
        for (int r = 1; r <= 6; r++) {
            ArrayList<Integer> ring = new ArrayList<>();
            for (int c = 0; c < Dungeon.level.length(); c++) {
                if (Dungeon.level.distance(cell, c) != r) {
                    continue;
                }
                if (isFreeCell(c)) {
                    ring.add(c);
                }
            }
            if (!ring.isEmpty()) {
                return Random.element(ring);
            }
        }
        return -1;
    }

    private boolean isFreeCell(int cell) {
        if (cell < 0 || cell >= Dungeon.level.length()) {
            return false;
        }
        if (Dungeon.level.solid[cell] || !Dungeon.level.passable[cell]) {
            return false;
        }
        return Actor.findChar(cell) == null;
    }

    private static final String DR_BONUS = "dr_bonus";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DR_BONUS, drBonus);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        drBonus = bundle.getInt(DR_BONUS);
    }

    /** 抵抗敌厄之拒的冷却：只计时，不显示 icon。 */
    public static class RepulseCooldown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 12f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    /** 纯白擢升之赐的冷却：只计时，不显示 icon。 */
    public static class AscendCooldown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 8f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    /** 黎明颠倒之辉的冷却：只计时，不显示 icon。 */
    public static class DawnCooldown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 12f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    /** 麻痹反击独立冷却：只计时，不显示 icon。 */
    public static class ParalysisRepulseCooldown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 12f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
