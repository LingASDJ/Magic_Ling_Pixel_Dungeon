package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulLossTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Aggregatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroAcolyte;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroArcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroCavalry;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroPioneer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroScout;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroWarlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Wisp;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LichKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * 巫妖王。
 *
 * <p>数值：生命 550，攻击 10~35，防御 2~6，精准 30，闪避 12。
 * <p>机制：
 * <ul>
 *     <li>魔刃：除特别标注的伤害外，巫妖造成的一切伤害均为法术伤害。</li>
 *     <li>夺魂：巫妖在场时，其他单位每损失 20 点生命就会被施加 1 层失魂。</li>
 *     <li>剥削：目标每有 1 层失魂，巫妖对其伤害 +15%。</li>
 *     <li>滋养：场上所有失魂层数之和，每层给予巫妖 0~4 点物防与 4 点闪避。</li>
 *     <li>轰杀：蓄力 4 回合后射出解离射线，造成 200% 真实伤害；蓄力期间获得
 *     0~75 点物防与 75% 法术减伤，冷却 5 回合。</li>
 *     <li>苦难：巫妖在场时，携带失魂的单位身上的持续时间型负面状态不会自然结束（可被净化）。</li>
 * </ul>
 */
public class LichKing extends Boss {

    //战斗数值
    private static final float ATTACK_MIN = 10f;
    private static final float ATTACK_MAX = 35f;
    private static final int ACCURACY = 30;
    private static final int EVASION = 12;
    private static final int HEALTH = 550;
    private static final int ARMOR_MIN = 2;
    private static final int ARMOR_MAX = 6;

    //轰杀
    private static final int PHASE_TWO_HP = 200;         //二阶段召唤阈值
    private static final int CHARGE_TURNS = 4;           //蓄力回合数
    private static final int BEAM_COOLDOWN_TURNS = 5;    //冷却回合数
    private static final float BEAM_DAMAGE_MULTIPLIER = 2f; //200%伤害
    private static final int CHARGE_ARMOR_BONUS = 75;    //蓄力期间0~75物防
    private static final float CHARGE_MAGIC_REDUCTION = 0.75f; //蓄力期间75%法术减伤

    //剥削 / 滋养
    private static final float SOUL_DAMAGE_BONUS = 0.15f; //每层失魂+15%伤害
    private static final int SOUL_EVASION_BONUS = 4;      //每层失魂+4闪避
    private static final int SOUL_ARMOR_BONUS = 4;        //每层失魂0~4物防

    private boolean warStart = false;
    private boolean phaseTwoSummoned = false;

    private boolean charging = false;
    private int chargeTurns = 0;
    private int beamCooldown = 0;
    private int beamTargetPos = -1;

    {
        initProperty();
        initBaseStatus(ATTACK_MIN, ATTACK_MAX, ACCURACY, EVASION, HEALTH, ARMOR_MIN, ARMOR_MAX);
        initStatus(0);
        spriteClass = LichKingSprite.class;
    }

    // ====================== 状态 ======================

    public boolean isCharging() {
        return charging;
    }

    /** 场上是否还有活着的巫妖（苦难、夺魂的判定条件）。 */
    public static boolean isLichAlive() {
        for (Char ch : Actor.chars()) {
            if (ch instanceof LichKing && ch.isAlive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean act() {
        //保证视野数据总是最新的：蓄力期间会跳过 super.act()，而选敌依赖 fieldOfView
        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()) {
            fieldOfView = new boolean[Dungeon.level.length()];
        }
        Dungeon.level.updateFieldOfView(this, fieldOfView);

        ensureSoulLossTracker();
        tryPhaseTwoSummon();

        if (beamCooldown > 0) {
            beamCooldown--;
        }

        //轰杀：蓄力中，停止一切其他行动
        if (charging) {
            chargeTurns--;
            if (chargeTurns <= 0) {
                charging = false;
                chargeTurns = 0;
                fireDisintegrationRay();
            }
            spend(TICK);
            return true;
        }

        //冷却结束且有一条清晰射线时，开始蓄力
        if (beamCooldown <= 0) {
            Char target = beamChargeTarget();
            if (target != null) {
                startCharging(target);
                spend(TICK);
                return true;
            }
        }

        return super.act();
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            Camera.main.shake(1f, 3f);
            GameScene.bossReady();
            yell(Messages.get(this, "notice", Dungeon.hero.name()));
            for (Char ch : Actor.chars()) {
                if (ch instanceof DriedRose.GhostHero) {
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }

        ensureSoulLossTracker();

        if (!warStart) {
            warStart = true;
            summonNecroMobs();
        }
    }

    @Override
    public void die(Object cause) {
        charging = false;
        chargeTurns = 0;
        if (sprite instanceof LichKingSprite) {
            ((LichKingSprite) sprite).endCharge();
        }
        //巫妖离场后夺魂不再生效
        for (Char ch : Actor.chars()) {
            SoulLossTracker tracker = ch.buff(SoulLossTracker.class);
            if (tracker != null) {
                tracker.detach();
            }
        }

        for (LostSoul buff : Dungeon.hero.buffs(LostSoul.class)) {
            buff.detach();
        }

        Dungeon.level.unseal();
        super.die(cause);
    }

    // ====================== 夺魂 ======================

    /**
     * 给巫妖以外的所有存活单位挂上“失魂记录器”，由它把每 20 点生命损失折算成 1 层失魂。
     */
    private void ensureSoulLossTracker() {
        for (Char ch : Actor.chars()) {
            if (ch == this || ch instanceof LichKing || !ch.isAlive()) continue;
            if (ch.buff(SoulLossTracker.class) == null) {
                Buff.affect(ch, SoulLossTracker.class);
            }
        }
    }

    // ====================== 滋养 / 剥削 / 魔刃 ======================

    private int totalSoulStacks() {
        int total = 0;
        for (Char ch : Actor.chars()) {
            if (ch == this || !ch.isAlive()) continue;
            total += ch.buffs(LostSoul.class).size();
        }
        return total;
    }

    /** 怪物信息里展示「滋养」的机制说明与当前实时加成。 */
    @Override
    public String description() {
        int stacks = totalSoulStacks();
        return super.description() + "\n\n" + Messages.get(this, "desc_nourish",
                stacks, stacks * SOUL_ARMOR_BONUS, stacks * SOUL_EVASION_BONUS);
    }

    @Override
    public int defenseSkill(Char enemy) {
        int base = super.defenseSkill(enemy);
        if (base <= 0) return base; //被偷袭/麻痹时不叠加闪避
        return base + totalSoulStacks() * SOUL_EVASION_BONUS;
    }

    @Override
    public int drRoll() {
        int dr = super.drRoll();
        //滋养：每层失魂提供 0~4 点物防（含 4）
        for (int i = 0; i < totalSoulStacks(); i++) {
            dr += Random.IntRange(0, SOUL_ARMOR_BONUS);
        }
        //轰杀蓄力：0~75 点物防（含 75）
        if (charging) {
            dr += Random.IntRange(0, CHARGE_ARMOR_BONUS);
        }
        return dr;
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        //轰杀蓄力：75% 法术伤害减免
        if (charging && dmg > 0 && type == DamageType.MAGIC) {
            dmg = Math.max(0, Math.round(dmg * (1f - CHARGE_MAGIC_REDUCTION)));
        }
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null){
            lock.addTime(dmg);
        }
        super.damage(dmg, src, type);
    }

    @Override
    public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti, DamageType type) {
        //剥削：目标每有 1 层失魂，伤害提高 15%
        if (enemy != null && enemy.isAlive()) {
            int stacks = enemy.buffs(LostSoul.class).size();
            if (stacks > 0) {
                dmgMulti *= (1f + SOUL_DAMAGE_BONUS * stacks);
            }
        }
        //魔刃：巫妖的普通攻击一律视为法术伤害
        return super.attack(enemy, dmgMulti, dmgBonus, accMulti, DamageType.MAGIC);
    }

    // ====================== 轰杀 ======================

    /** 只有当敌人处于射线上、且不在近身时才用射线，否则交给普通 AI 近战/追击。 */
    private Char beamChargeTarget() {
        if (state == SLEEPING || state == FLEEING || paralysed > 0) return null;

        Char target = chooseEnemy();
        if (target == null || target == this || !target.isAlive()) return null;
        if (target.invisible > 0 || !fieldOfView[target.pos]) return null;
        if (Dungeon.level.adjacent(pos, target.pos)) return null;

        Ballistica beam = new Ballistica(pos, target.pos, Ballistica.STOP_SOLID);
        if (beam.dist < 2 || beam.collisionPos == null || beam.collisionPos.intValue() != target.pos) {
            return null;
        }
        return target;
    }

    private void startCharging(Char target) {
        charging = true;
        chargeTurns = CHARGE_TURNS;
        beamTargetPos = target.pos;
        if (sprite instanceof LichKingSprite) {
            ((LichKingSprite) sprite).charge(target.pos);
        }
        yell(Messages.get(this, "charging"));
    }

    /**
     * 解离射线：贯穿整条直线，命中单位受到 200% 真实伤害（不受护甲/护盾影响）。
     * 这是“魔刃”中特别标注的例外，因此使用 REAL 而不是 MAGIC。
     */
    private void fireDisintegrationRay() {
        Char target = chooseEnemy();
        int aim = (target != null && target.isAlive()) ? target.pos : beamTargetPos;
        if (aim < 0) aim = pos;

        Ballistica beam = new Ballistica(pos, aim, Ballistica.STOP_SOLID);
        beamTargetPos = -1;

        if (sprite instanceof LichKingSprite) {
            LichKingSprite s = (LichKingSprite) sprite;
            s.endCharge();
            s.turnTo(pos, beam.collisionPos);
            s.zap(beam.collisionPos);
        }
        if (sprite != null && sprite.parent != null
                && (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[beam.collisionPos])) {
            sprite.parent.add(new Beam.DeathRayS(sprite.center(),
                    DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
        }
        Invisibility.dispel(this);

        for (int cell : beam.subPath(1, beam.dist)) {
            Char ch = Actor.findChar(cell);
            if (ch == null || ch == this || !ch.isAlive()) continue;
            if (ch.isInvulnerable(getClass())) continue;

            if (!hit(this, ch, true)) {
                if (ch.sprite != null && ch.sprite.visible) {
                    ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
                }
                continue;
            }

            int dmg = Math.round(damageRoll() * BEAM_DAMAGE_MULTIPLIER * AscensionChallenge.statModifier(this));
            int stacks = ch.buffs(LostSoul.class).size();
            if (stacks > 0) {
                dmg = Math.round(dmg * (1f + SOUL_DAMAGE_BONUS * stacks));
            }
            ch.damage(dmg, this, DamageType.REAL);

            if (ch.sprite != null && Dungeon.level.heroFOV[cell]) {
                ch.sprite.flash();
                CellEmitter.center(cell).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
            }
        }

        beamCooldown = BEAM_COOLDOWN_TURNS;
    }

    // ====================== 召唤 ======================

    /** 生命降至 200 时再召唤一整轮死灵。 */
    private void tryPhaseTwoSummon() {
        if (phaseTwoSummoned || !isAlive() || HP > PHASE_TWO_HP) return;

        phaseTwoSummoned = true;
        summonNecroMobs();
        BossHealthBar.bleed(true);
        yell(Messages.get(this, "phase_two"));

        if (sprite != null) {
            Emitter emitter = sprite.centerEmitter();
            if (emitter != null) {
                emitter.burst(Speck.factory(Speck.BLUE_LIGHT), 20);
            }
        }
        if (Dungeon.level.heroFOV[pos]) {
            Camera.main.shake(1f, 2f);
        }
    }

    private final int particles = 2 + (int) Random.Float(2f);

    /**
     * 召唤全部种类的死灵生物各 1 个：共有 9 种带 {@link Char.Property#NECRO} 的单位。
     * 召唤位置以巫妖为中心，从内向外逐环选取，让死灵围在巫妖身边。
     */
    private void summonNecroMobs() {
        List<Mob> mobs = new ArrayList<>();
        mobs.add(new NecroArcher());
        mobs.add(new NecroGuard());
        mobs.add(new Aggregatus());
        mobs.add(new NecroCavalry());
        mobs.add(new NecroPioneer());
        mobs.add(new NecroScout());
        mobs.add(new NecroWarlock());
        mobs.add(new Wisp());
        mobs.add(new NecroAcolyte());

        //先确定所有召唤点（围绕巫妖），再随机分配死灵种类
        List<Integer> cells = findSummonCellsAround(mobs.size());
        Random.shuffle(mobs);

        for (int i = 0; i < mobs.size() && i < cells.size(); i++) {
            Mob mob = mobs.get(i);
            int cell = cells.get(i);

            if (mob instanceof NecroGuard) {
                ((NecroGuard) mob).setLegionSpawn(true);
            } else if (mob instanceof NecroArcher) {
                ((NecroArcher) mob).setLegionSpawn(true);
            }

            mob.pos = cell;
            GameScene.add(mob);
            Dungeon.level.occupyCell(mob);
            if (mob.sprite != null) {
                mob.sprite.emitter().startDelayed(
                        Speck.factory(Speck.BLUE_LIGHT), 0.02f, particles, 0.05f);
            }
        }
    }

    /**
     * 以巫妖为中心，从半径 1 开始逐环收集可站立空位，最多取 count 个。
     * 同一环内随机打乱，避免每次都从同一侧开始占位。
     */
    private List<Integer> findSummonCellsAround(int count) {
        ArrayList<Integer> result = new ArrayList<>();
        if (count <= 0) {
            return result;
        }

        HashSet<Integer> chosen = new HashSet<>();
        int maxRadius = Dungeon.level.length();

        for (int radius = 1; radius <= maxRadius && result.size() < count; radius++) {
            ArrayList<Integer> ring = new ArrayList<>();
            for (int cell = 0; cell < Dungeon.level.length(); cell++) {
                if (cell == pos || Dungeon.level.distance(pos, cell) != radius) continue;
                if (Dungeon.level.solid[cell] || !Dungeon.level.passable[cell]) continue;
                if (Actor.findChar(cell) != null || chosen.contains(cell)) continue;
                ring.add(cell);
            }

            Collections.shuffle(ring);
            for (int cell : ring) {
                if (result.size() >= count) break;
                result.add(cell);
                chosen.add(cell);
            }
        }

        return result;
    }

    // ====================== 存档 ======================

    private static final String WAR_START = "war_start";
    private static final String PHASE_TWO = "phase_two";
    private static final String CHARGING = "charging";
    private static final String CHARGE_TURNS_KEY = "charge_turns";
    private static final String BEAM_COOLDOWN = "beam_cooldown";
    private static final String BEAM_TARGET = "beam_target";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(WAR_START, warStart);
        bundle.put(PHASE_TWO, phaseTwoSummoned);
        bundle.put(CHARGING, charging);
        bundle.put(CHARGE_TURNS_KEY, chargeTurns);
        bundle.put(BEAM_COOLDOWN, beamCooldown);
        bundle.put(BEAM_TARGET, beamTargetPos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        warStart = bundle.getBoolean(WAR_START);
        phaseTwoSummoned = bundle.getBoolean(PHASE_TWO);
        charging = bundle.getBoolean(CHARGING);
        chargeTurns = bundle.getInt(CHARGE_TURNS_KEY);
        beamCooldown = bundle.getInt(BEAM_COOLDOWN);
        beamTargetPos = bundle.contains(BEAM_TARGET) ? bundle.getInt(BEAM_TARGET) : -1;

        //旧存档或异常状态下，蓄力回合数不合法时直接结束蓄力
        if (charging && chargeTurns <= 0) {
            charging = false;
            chargeTurns = 0;
        }
    }
}
