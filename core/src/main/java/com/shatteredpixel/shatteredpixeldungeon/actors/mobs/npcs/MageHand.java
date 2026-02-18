package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.MageHandControlBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.MagicAbsorb;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfScale;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand.WandOfBlueFuck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand.WandOfHightHunderStorm;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MageHandSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MageHand extends DirectableAlly {

    public static class HandWareness extends FlavourBuff {
        public int distance = 2;
        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 123456789f;

        @Override
        public void detach() {
            super.detach();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }

    {
        spriteClass = MageHandSprite.class;
        flying = true;
        properties.add(Property.UNKNOWN);
        immunities.add(Blob.class);
        immunities.add(Buff.class);
        viewDistance = 10;
        WANDERING = new Wandering();
    }

    private class Wandering extends Mob.Wandering {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {

            if (enemyInFOV
                    && attacksAutomatically
                    && !movingToDefendPos
                    && (defendingPos == -1
                    || !Dungeon.level.heroFOV[defendingPos]
                    || canAttack(enemy))) {

                enemySeen = true;

                notice();
                alerted = true;
                state = HUNTING;
                target = enemy.pos;

            } else {

                enemySeen = false;

                int oldPos = pos;
                target = defendingPos != -1 ? defendingPos : hero.pos;

                // 检查目标位置是否有效
                if (target < 0 || target >= Dungeon.level.length()) {
                    // 如果目标位置无效，重置目标位置
                    target = hero.pos;
                    if (target < 0 || target >= Dungeon.level.length()) {
                        // 如果英雄位置也无效，就不移动
                        spend(TICK);
                        return true;
                    }
                }

                //always move towards the hero when wandering
                if (getCloser(target)) {
                    spend(1 / speed());
                    if (pos == defendingPos) movingToDefendPos = false;
                    return moveSprite(oldPos, pos);
                } else {
                    //if it can't move closer to defending pos, then give up and defend current position
                    if (movingToDefendPos) {
                        defendingPos = pos;
                        movingToDefendPos = false;
                    }
                    spend(TICK);
                }
            }
            return true;
        }

        @Override
        protected boolean noticeEnemy() {
            spend(TICK);
            return super.noticeEnemy();
        }
    }

    @Override
    public void aggro(Char ch) {
        enemy = ch;
        state = HUNTING;
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        // 先执行伤害逻辑
        super.damage(0, src, type);

        teleportNearHero();
    }

    // 尝试传送到英雄周围5x5区域
    private void teleportNearHero() {
        int heroPos = hero.pos;
        ArrayList<Integer> validPositions = new ArrayList<>();

        // 获取5x5区域内的有效位置
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                int newPos = heroPos + i + j * Dungeon.level.width();

                // 检查位置是否有效
                if (isHeroValidTeleportPosition(newPos)) {
                    validPositions.add(newPos);
                }
            }
        }

        // 如果找到有效位置，随机选择一个传送
        if (!validPositions.isEmpty()) {
            int targetPos = Random.element(validPositions);
            ScrollOfTeleportation.appear(this,targetPos);
        }

    }

    private boolean isHeroValidTeleportPosition(int pos) {
        return pos >= 0
                && pos < Dungeon.level.length()
                && !Dungeon.level.solid[pos]
                && !Dungeon.level.pit[pos]
                && Actor.findChar(pos) == null;
    }

    @Override
    public String description() {
        String desc = super.description();

        if(magesStaff != null){
            desc += "\n\n" + Messages.get(this, "desc_staff", magesStaff.name());
            desc += "\n" + Messages.get(this, "desc_colddown", 1);
        }

        if(equippedWand != null){
            desc += "\n\n" + Messages.get(this, "desc_equipped_wand", equippedWand.name());
            desc += "\n" + Messages.get(this, "desc_charges", equippedWand.curCharges, equippedWand.maxCharges);
            desc += "\n" + Messages.get(this, "desc_colddown", wandCooldown);
        }

        return desc;
    }

    // 在MageHand类中添加以下常量
    private static final float BASE_CHARGE_DELAY = 10f;
    private static final float SCALING_CHARGE_ADDITION = 40f;
    private static final float NORMAL_SCALE_FACTOR = 0.875f;

    // 修改充能相关方法
    @Override
    public void spend(float time) {
        super.spend(time);

        if (equippedWand != null) {
            chargeWand(equippedWand);
        }
        if (magesStaff != null) {
            chargeWand(magesStaff.wand);
        }
    }

    private void chargeWand(Wand equippedWand) {
        if (equippedWand == null || equippedWand.curCharges >= equippedWand.maxCharges ) {
            return;
        }

        // 计算缺失的充能数
        int missingCharges = equippedWand.maxCharges - equippedWand.curCharges;
        missingCharges = Math.max(0, missingCharges);

        // 计算充能所需回合数
        float turnsToCharge = BASE_CHARGE_DELAY
                + (SCALING_CHARGE_ADDITION * (float) Math.pow(NORMAL_SCALE_FACTOR, missingCharges));

        // 计算本回合的充能量
        float chargeAmount = 1f / turnsToCharge;

        // 应用充能加成（如果有）
        chargeAmount *= getChargeMultiplier();

        // 累积部分充能
        equippedWand.partialCharge += chargeAmount;

        // 处理完整的充能
        while (equippedWand.partialCharge >= 1f && equippedWand.curCharges < equippedWand.maxCharges) {
            equippedWand.partialCharge -= 1f;
            equippedWand.curCharges++;
        }

        // 如果法杖已充满，重置部分充能值
        if (equippedWand.curCharges >= equippedWand.maxCharges) {
            equippedWand.partialCharge = 0f;
        }
    }

    private float getChargeMultiplier() {
        float multiplier = 1f;

        // 检查是否有充能相关的buff
        for (Recharging bonus : buffs(Recharging.class)) {
            if (bonus != null && bonus.remainder() > 0f) {
                multiplier += 0.25f * bonus.remainder();
            }
        }

        return multiplier;
    }

    public Wand equippedWand = null;

    public MagesStaff magesStaff = null;

    private int wandCooldown = 0;

    public MageHand(){
        super();
        HP = HT = 1;
        if (equippedWand != null) {
            equippedWand.partialCharge = 0f;
        }
    }

    public void equipWand(Wand wand) {
        this.equippedWand = wand;
        wand.partialCharge = 0f;
        wand.charge(this);
        GLog.i(Messages.get(this, "wand_equipped", wand.name()));
    }

    public void unequipWand() {
        if (equippedWand != null) {
            GLog.i(Messages.get(this, "wand_unequipped", equippedWand.name()));
        }
        this.equippedWand = null;
    }

    public void equipMageStaff(MagesStaff magesStaff) {
        this.magesStaff = magesStaff;
        GLog.i(Messages.get(this, "wand_equipped", magesStaff.name()));
    }

    public void unequipMageStaff() {
        if (magesStaff != null) {
            GLog.i(Messages.get(this, "wand_unequipped", magesStaff.name()));
        }
        this.magesStaff = null;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        if (hero != null) {
            // 如果装备了法师之杖，优先检查近战攻击
            if (magesStaff != null) {
                // 检查是否可以近战攻击
                if (Dungeon.level.adjacent(pos, enemy.pos)) {
                    return true;
                }
                // 如果不能近战攻击，再检查法杖攻击
                Wand mwand = magesStaff.wand;
                if (mwand != null && wandCooldown == 0) {
                    if (mwand.curCharges > 0) {
                        Ballistica attack = new Ballistica(pos, enemy.pos, 10);
                        if (!isHeroInAttackPath(attack)) {
                            return attack.collisionPos == enemy.pos;
                        }
                    }
                }
            }
            // 如果没有装备法师之杖，检查普通法杖
            else if (equippedWand != null && wandCooldown == 0) {
                if (equippedWand.curCharges > 0) {
                    Ballistica attack = new Ballistica(pos, enemy.pos, 10);
                    if (!isHeroInAttackPath(attack)) {
                        return attack.collisionPos == enemy.pos;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected boolean doAttack(Char enemy) {
        if (hero != null) {
            // 如果装备了法师之杖，优先进行近战攻击
            if (magesStaff != null) {
                Wand mwand = magesStaff.wand;
                // 如果法杖有充能且不在冷却中，优先远程攻击
                if (Dungeon.level.adjacent(pos, enemy.pos)) {
                    return super.doAttack(enemy);
                }
                if (mwand != null && wandCooldown == 0 && mwand.curCharges > 0) {
                    Ballistica attack = new Ballistica(pos, enemy.pos, 10);
                    if (isHeroInAttackPath(attack)) {
                        return tryMoveToBetterPosition(enemy);
                    }
                    if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                        sprite.zap(enemy.pos);
                        return false;
                    } else {
                        zap();
                        return true;
                    }
                }
            }
            // 如果没有装备法师之杖，使用普通法杖攻击
            else if (equippedWand != null && wandCooldown == 0) {
                if (equippedWand.curCharges > 0) {
                    Ballistica attack = new Ballistica(pos, enemy.pos, 10);
                    if (isHeroInAttackPath(attack)) {
                        return tryMoveToBetterPosition(enemy);
                    }
                    if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                        sprite.zap(enemy.pos);
                        return false;
                    } else {
                        zap();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isHeroInAttackPath(Ballistica attack) {
        for (int i = 0; i < attack.path.size(); i++) {
            int p = attack.path.get(i);
            if (p == hero.pos) {
                return true;
            }
        }
        return false;
    }

    public boolean hasWand() {
        return equippedWand != null;
    }

    public boolean hasMageStaff() {
        return magesStaff != null;
    }

    public Wand getEquippedWand() {
        return equippedWand;
    }

    public MagesStaff getEquippedMageStaff() {
        return magesStaff;
    }

    public Wand getEquippedMageStaffWand() {
        return magesStaff.wand;
    }

    @Override
    protected boolean act() {
        if (wandCooldown > 0) {
            wandCooldown--;
        }

        boolean hasEnemy = false;
        for (Char ch : Actor.chars()) {
            if (ch != null && ch.alignment == Alignment.ENEMY && fieldOfView != null
                    && fieldOfView.length > ch.pos && fieldOfView[ch.pos]) {
                hasEnemy = true;
                break;
            }
        }

        if (!hasEnemy) {
            for (Char ch : Actor.chars()) {
                if (ch instanceof WandOfWarding.Ward) {
                    if(((WandOfWarding.Ward) ch).handSummon){
                        ch.die(null);
                    }
                }
            }
            clearDefensingPos();
        }

        MageHandControlBuff buff = hero.buff(MageHandControlBuff.class);
        if (buff == null) {
            Buff.affect(hero, MageHandControlBuff.class);
        }

        MageHand.HandWareness mageHandWareness = hero.buff(MageHand.HandWareness.class);
        if(mageHandWareness == null){
            Buff.affect(hero, MageHand.HandWareness.class);
        }

        if(equippedWand != null || magesStaff != null){
            invisible = 0;
        } else {
            invisible = 1;
        }

        return super.act();
    }

    public void gainCharge(float charge) {
        if (equippedWand != null && equippedWand.curCharges < equippedWand.maxCharges) {
            equippedWand.partialCharge += charge;
            while (equippedWand.partialCharge >= 1f && equippedWand.curCharges < equippedWand.maxCharges) {
                equippedWand.partialCharge -= 1f;
                equippedWand.curCharges++;
            }
            if (equippedWand.curCharges >= equippedWand.maxCharges) {
                equippedWand.partialCharge = 0f;
                equippedWand.curCharges = equippedWand.maxCharges;
            }
        }
    }


    public void onZapComplete() {
        zap();
        next();
    }


    private void zap() {
        if (magesStaff != null && enemy != null) {
            if (magesStaff.wand.curCharges > 0) {
                zapWithMagesStaff();
            }
        } else if (equippedWand != null && enemy != null) {
            if (equippedWand.curCharges > 0) {
                zapWithEquippedWand();
            }
        }
    }

    private void zapWithMagesStaff() {
        Wand mwand = magesStaff.wand;
        executeWandEffect(mwand);
        mwand.curCharges -= Math.max(1, mwand.chargesPerCast());
        finalizeWandUse();
    }

    private void zapWithEquippedWand() {
        executeWandEffect(equippedWand);
        equippedWand.curCharges -= Math.max(1, equippedWand.chargesPerCast());
        finalizeWandUse();
    }

    private void executeWandEffect(Wand wand) {
        if (wand instanceof WandOfWarding && enemy != null) {
            // 获取敌人周围半径2的所有位置
            ArrayList<Integer> validPositions = new ArrayList<>();
            int radius = 3;

            // 遍历敌人周围8个方向的所有点
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    // 跳过中心点和距离不是2的点
                    if (dx == 0 && dy == 0) continue;
                    if (Math.abs(dx) + Math.abs(dy) != radius) continue;

                    int cell = enemy.pos + dx + dy * Dungeon.level.width();

                    // 检查位置是否有效
                    if (cell >= 0 && cell < Dungeon.level.length() &&
                            Dungeon.level.passable[cell] &&
                            Actor.findChar(cell) == null) {

                        // 检查是否是封闭位置
                        boolean solidFound = false;
                        boolean passableFound = false;

                        // 检查周围8个方向
                        for (int i : PathFinder.NEIGHBOURS8) {
                            int neighbourPos = cell + i;
                            if (neighbourPos >= 0 && neighbourPos < Dungeon.level.length()) {
                                if (Dungeon.level.solid[neighbourPos]) {
                                    solidFound = true;
                                } else if (!Dungeon.level.solid[neighbourPos] &&
                                        Actor.findChar(neighbourPos) == null) {
                                    passableFound = true;
                                }
                            }
                        }

                        // 只有当位置不是封闭的时才添加到有效位置列表
                        if (solidFound && passableFound) {
                            validPositions.add(cell);
                        }
                    }
                }
            }

            if (!validPositions.isEmpty()) {
                // 随机选择一个位置
                int targetPos = Random.element(validPositions);

                // 创建法术轨迹
                Ballistica attack = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET);

                // 检查是否需要创建新的哨位
                Char existingWard = Actor.findChar(targetPos);
                if (existingWard instanceof WandOfWarding.Ward) {
                    // 如果已存在哨位，直接施法
                    if (wand.cursed) {
                        CursedWand.cursedZap(wand, this, attack, this::next);
                    } else {
                        wand.onZap(attack);
                    }
                } else {
                    // 如果不存在哨位，创建新的并设置handSummon
                    WandOfWarding.Ward ward = new WandOfWarding.Ward();
                    ward.pos = targetPos;
                    ward.wandLevel = wand.level();
                    ward.handSummon = true;  // 设置handSummon为true
                    GameScene.add(ward, 1f);
                    Dungeon.level.occupyCell(ward);
                    ward.sprite.emitter().burst(MagicMissile.WardParticle.UP, ward.tier);
                }
            }
        } else if (wand.cursed) {
            CursedWand.cursedZap(
                    wand == equippedWand ? wand : null,
                    this,
                    new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET),
                    this::next
            );
        } else if (isSpecialWand(wand)) {
            wand.onAIZap(new Ballistica(pos, enemy.pos, wand.collisionProperties));
        } else {
            wand.onZap(new Ballistica(pos, enemy.pos, wand.collisionProperties));
        }
    }



    private boolean isSpecialWand(Wand wand) {
        return wand instanceof WandOfFireblast ||
                wand instanceof WandOfBlueFuck ||
                wand instanceof WandOfScale ||
                wand instanceof WandOfRegrowth || wand instanceof WandOfHightHunderStorm;
    }

    private void finalizeWandUse() {
        wandCooldown = 3;
        spend(1f);
    }


    @Override
    public int attackSkill(Char target) {
        int acc = hero.lvl + 9;
        if (equippedWand != null) {
            acc += equippedWand.level();
        }
        return acc;
    }

    @Override
    public int damageRoll() {
        if (magesStaff != null) {
            return magesStaff.damageRoll(this);
        } else if (equippedWand != null) {
            return equippedWand.buffedLvl();
        }
        return Random.NormalIntRange(1, 3);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        if (magesStaff != null) {
            damage = magesStaff.proc(this, enemy, damage);
            Buff.prolong(hero, StoneOfAggression.Aggression.class, 1f);
            if(hero.hasTalent(Talent.MAGIC_ABSORB)){
                MagicAbsorb buff = hero.buff(MagicAbsorb.class);
                if(buff != null){
                    buff.downAbsord(hero.pointsInTalent(Talent.MAGIC_ABSORB));
                }
            }
        } else if (equippedWand != null) {
            damage += equippedWand.level();
        }
        return damage;
    }

    public void sayAppeared(){
        yell(Messages.get(this, "appear"));
    }

    private static final String WAND =        "wand";
    private static final String MAGE_STAFF = "mage_staff";
    private static final String PRIORITY_ATTACK = "priority_attack";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);

        if (equippedWand != null)  bundle.put( WAND,equippedWand );
        if(magesStaff != null) bundle.put( MAGE_STAFF, magesStaff );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);

        if (bundle.contains(WAND))
            equippedWand = (Wand) bundle.get( WAND );
        if (bundle.contains(MAGE_STAFF))
            magesStaff = (MagesStaff) bundle.get( MAGE_STAFF );
    }

    public static class HandShield extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        private int level = 0;
        private int interval = 1;


        @Override
        public boolean act() {
            super.act();
            if(--interval <= 0){
                detach();
            }
            return true;
        }

        public void set( int value, int ints) {
            if (level <= value) {
                level = value;
                interval = ints;
                spend(ints - cooldown() - 1);
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
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

        //These two methods allow for multiple instances of barkskin to stack in terms of duration
        // but only the stronger bonus is applied

        public static int currentLevel(Char ch ){
            int level = 0;
            for (HandShield b : ch.buffs(HandShield.class)){
                level = Math.max(level, b.level);
            }
            return level;
        }
    }

    // 尝试移动到更好的位置以避免误伤英雄并攻击敌人
    private boolean tryMoveToBetterPosition(Char enemy) {
        // 寻找可以攻击敌人且不经过英雄的位置
        ArrayList<Integer> candidates = new ArrayList<>();

        // 检查周围8个方向，并添加边界检查
        int[] neighborOffsets = new int[]{
                -1, 1,
                -Dungeon.level.width(), Dungeon.level.width(),
                -Dungeon.level.width() - 1, -Dungeon.level.width() + 1,
                Dungeon.level.width() - 1, Dungeon.level.width() + 1
        };

        // 计算每个候选位置的评分
        ArrayList<Integer> bestCandidates = new ArrayList<>();
        float bestScore = Float.NEGATIVE_INFINITY;

        for (int offset : neighborOffsets) {
            int newPos = pos + offset;

            // 添加边界检查，确保 newPos 在有效范围内
            if (newPos < 0 || newPos >= Dungeon.level.length()) {
                continue;
            }

            // 检查位置是否可通行且没有其他角色
            if (Dungeon.level.passable[newPos] && Actor.findChar(newPos) == null) {
                Ballistica attack = new Ballistica(newPos, enemy.pos, MagicMissile.WARD);
                if (!isHeroInAttackPath(attack) && attack.collisionPos == enemy.pos) {
                    // 计算这个位置的评分
                    float score = evaluatePosition(newPos, enemy);

                    // 如果评分更高，则清空之前的最佳候选
                    if (score > bestScore) {
                        bestScore = score;
                        bestCandidates.clear();
                        bestCandidates.add(newPos);
                    }
                    // 如果评分相同，则添加到候选列表
                    else if (score == bestScore) {
                        bestCandidates.add(newPos);
                    }
                }
            }
        }

        // 如果找到了合适的位置，移动过去
        if (!bestCandidates.isEmpty()) {
            int newPos = Random.element(bestCandidates);
            move(newPos);
            spend(1 / speed());
            return true;
        }

        // 如果找不到合适的位置，就移动到英雄附近但不挡住英雄
        ArrayList<Integer> heroNeighbors = new ArrayList<>();
        int heroPos = hero.pos;

        // 同样添加边界检查
        for (int offset : neighborOffsets) {
            int newPos = heroPos + offset;

            // 添加边界检查，确保 newPos 在有效范围内
            if (newPos < 0 || newPos >= Dungeon.level.length()) {
                continue;
            }

            if (Dungeon.level.passable[newPos] && Actor.findChar(newPos) == null && newPos != pos) {
                heroNeighbors.add(newPos);
            }
        }

        if (!heroNeighbors.isEmpty()) {
            int newPos = Random.element(heroNeighbors);
            move(newPos);
            spend(1 / speed());
            return true;
        }

        return false;
    }

    // 评估位置的优劣
    private float evaluatePosition(int position, Char enemy) {
        float score = 0;

        // 距离英雄的距离（不要太远也不要太近）
        float distanceToHero = Dungeon.level.distance(position, hero.pos);
        if (distanceToHero < 3) {
            score += 10; // 离英雄近一点好
        } else if (distanceToHero > 6) {
            score -= 5; // 离英雄太远不好
        }

        // 距离敌人的距离
        float distanceToEnemy = Dungeon.level.distance(position, enemy.pos);
        score -= distanceToEnemy; // 距离敌人越近越好

        // 检查这个位置是否能攻击到多个敌人
        int enemiesInRange = 0;
        for (Char ch : Actor.chars()) {
            if (ch.alignment == Alignment.ENEMY && fieldOfView[ch.pos]) {
                Ballistica attack = new Ballistica(position, ch.pos, MagicMissile.WARD);
                if (!isHeroInAttackPath(attack) && attack.collisionPos == ch.pos) {
                    enemiesInRange++;
                }
            }
        }
        score += enemiesInRange * 5; // 能攻击到多个敌人加分

        // 检查这个位置是否安全（不容易被攻击）
        int enemiesThreateningPosition = 0;
        for (Char ch : Actor.chars()) {
            if (ch.alignment == Alignment.ENEMY && fieldOfView[ch.pos]) {
                Ballistica attack = new Ballistica(ch.pos, position, Ballistica.PROJECTILE);
                if (attack.collisionPos == position) {
                    enemiesThreateningPosition++;
                }
            }
        }
        score -= enemiesThreateningPosition * 3; // 容易被攻击的位置减分

        return score;
    }

    @Override
    public void die( Object cause ) {

        super.die(cause);
        if(magesStaff != null){
            Dungeon.level.drop(magesStaff, hero.pos).sprite.drop();
        }
        if(equippedWand != null){
            Dungeon.level.drop(equippedWand, hero.pos).sprite.drop();
        }
        Buff.detach(hero,MageHandControlBuff.class);
    }

    private static class WndMageHand extends Window {

        private static final int BTN_SIZE  = 32;
        private static final float GAP     = 5;
        private static final float BTN_GAP = 12;
        private static final int WIDTH     = 116;

        private ItemButton btnWand;
        private StyledButton btnAction1;
        public MageHand.MageHandControl magesStaffcontrol;
        WndMageHand(final MageHand hand){
            magesStaffcontrol = Dungeon.hero.belongings.getItem(MageHand.MageHandControl.class);
            IconTitle titlebar = new IconTitle();
            titlebar.icon( new ItemSprite(ItemSpriteSheet.MAGES_STAFF) );
            titlebar.label( Messages.get(this, "title") );
            titlebar.setRect( 0, 0, WIDTH, 0 );
            add( titlebar );

            RenderedTextBlock message =
                    PixelScene.renderTextBlock(Messages.get(this, "desc"), 6);
            message.maxWidth( WIDTH );
            message.setPos(0, titlebar.bottom() + GAP);
            add( message );

            // 法杖按钮居中显示
            btnWand = new ItemButton(){
                @Override
                protected void onClick() {
                    if (hand.hasMageStaff()){
                        MagesStaff currentStaff = hand.getEquippedMageStaff();
                        item(new WndBag.Placeholder(ItemSpriteSheet.WAND_HOLDER));
                        if (!currentStaff.doPickUp(hero)){
                            Dungeon.level.drop(currentStaff, hero.pos);
                        }
                        hand.unequipMageStaff();
                    } else if (hand.hasWand()){
                        Wand currentWand = hand.getEquippedWand();
                        item(new WndBag.Placeholder(ItemSpriteSheet.WAND_HOLDER));
                        if (!currentWand.doPickUp(hero)){
                            Dungeon.level.drop(currentWand, hero.pos);
                        }
                        hand.unequipWand();
                    } else {
                        GameScene.selectItem(new WndBag.ItemSelector() {
                            @Override
                            public String textPrompt() {
                                return Messages.get(WndMageHand.class, "wand_prompt");
                            }

                            @Override
                            public boolean itemSelectable(Item item) {
                                return item instanceof Wand || item instanceof MagesStaff;
                            }

                            @Override
                            public Class<?extends Bag> preferredBag(){
                                return MagicalHolster.class;
                            }

                            @Override
                            public void onSelect(Item item) {
                                if (!(item instanceof Wand) && !(item instanceof MagesStaff)) {
                                    // 窗口取消时不做任何操作

                                } else if (item.isEquipped(hero)) {
                                    GLog.w( Messages.get(WndMageHand.class, "cant_equip") );
                                    hide();
                                } else if (!item.isIdentified()) {
                                    GLog.w( Messages.get(WndMageHand.class, "cant_unidentified"));
                                    hide();
                                } else {
                                    if(item instanceof MagesStaff){
                                        Wand w = ((MagesStaff) item).wand;
                                        hand.equipMageStaff((MagesStaff) item);
                                        item(hand.getEquippedMageStaff());
                                        item.detach(hero.belongings.backpack);
                                    } else {
                                        hand.equipWand((Wand) item);
                                        item(hand.getEquippedWand());
                                        item.detach(hero.belongings.backpack);
                                    }
                                }
                            }
                        });
                    }
                }

                @Override
                protected boolean onLongClick() {
                    if (item() != null && item().name() != null){
                        GameScene.show(new WndInfoItem(item()));
                        return true;
                    }
                    return false;
                }
            };

            // 计算法杖按钮的居中位置
            float wandX = 25;
            btnWand.setRect( wandX, message.top() + message.height() + GAP, BTN_SIZE, BTN_SIZE );

            // 左侧按钮
            btnAction1 = new StyledButton(Chrome.Type.TOAST,Messages.get(this,"attack"),7) {
                @Override
                protected void onClick() {
                    if(magesStaffcontrol != null){
                        magesStaffcontrol.execute(Dungeon.hero, MageHandControl.AC_TARGET_ENEMY);
                    }
                    hide();
                }
            };
            btnAction1.textBelowIcon = true;
            btnAction1.icon(new BuffIcon(BuffIndicator.CHALLENGE,true));
            btnAction1.setRect(btnWand.right()+GAP, message.top() + message.height() + GAP, BTN_SIZE, BTN_SIZE);

            if(hand.hasMageStaff()){
                btnWand.item(hand.getEquippedMageStaff());
            } else if (hand.hasWand()) {
                btnWand.item(hand.getEquippedWand());
            } else {
                btnWand.item(new WndBag.Placeholder(ItemSpriteSheet.WAND_HOLDER));
            }

            add( btnWand );
            add( btnAction1 );
            //add( btnAction2 );

            resize(WIDTH, (int)(btnWand.bottom() + 2));
        }
    }

    public static class MageHandControl extends Item {
        public static final String AC_HAND = "HAND";
        public static final String AC_DIRECT = "DIRECT";
        public static final String AC_SUMMON_HAND = "SUMMON_HAND";
        public static final String AC_TARGET_ENEMY = "TARGET_ENEMY";

        public MageHand mageHand = null;

        {
            unique = true;
            image = ItemSpriteSheet.WAND_HAND_CONTROL;
        }

        @Override
        public String defaultAction(){
            if(mageHand!=null){
                return AC_DIRECT;
            } else {
                return AC_SUMMON_HAND;
            }
        }

        @Override
        public ItemSprite.Glowing glowing() {

            return mageHand != null ? null : new ItemSprite.Glowing(0x880000, 1f) ;
        }

        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public String status() {
            if(mageHand != null){
                if (mageHand.magesStaff != null && mageHand.magesStaff.wand != null) {
                    return mageHand.magesStaff.wand.curCharges + "/" + mageHand.magesStaff.wand.maxCharges;
                } else if (mageHand.equippedWand != null) {
                    return mageHand.equippedWand.curCharges + "/" + mageHand.equippedWand.maxCharges;
                }
            }
            return "";
        }



        @Override
        public boolean isIdentified() {
            return true;
        }

        @Override
        public ArrayList<String> actions(Hero hero) {
            ArrayList<String> actions = super.actions(hero);
            actions.add(AC_HAND);
            actions.add(AC_DIRECT);
            actions.add(AC_SUMMON_HAND);
            actions.add(AC_TARGET_ENEMY);
            return actions;
        }

        @Override
        public void execute(Hero hero, String action) {
            super.execute(hero, action);

            switch (action) {
                case AC_HAND:
                    if(mageHand != null){
                        GameScene.show(new WndMageHand(mageHand));
                    }
                    break;
                case AC_DIRECT:
                    if(mageHand != null){
                        GameScene.selectCell(handDirector);
                    }
                    break;
                case AC_SUMMON_HAND:
                    boolean hasMageHand = false;
                    ArrayList<Integer> spawnPoints = new ArrayList<>();
                    for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                        int p = hero.pos + PathFinder.NEIGHBOURS8[i];
                        if (Actor.findChar(p) == null
                                && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])
                                && !(PathFinder.distance[p] == Integer.MAX_VALUE)) {
                            spawnPoints.add(p);
                        }
                    }

                    if(mageHand == null){

                        if (spawnPoints.size() > 0) {
                            MageHand mageHand = new MageHand();
                            mageHand.pos = Random.element(spawnPoints);
                            GameScene.add(mageHand, 1f);
                            Dungeon.level.occupyCell(mageHand);

                            CellEmitter.get(mageHand.pos).start(ShaftParticle.FACTORY, 0.3f, 4);
                            CellEmitter.get(mageHand.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

                            hero.spend(1f);
                            hero.busy();
                            hero.sprite.operate(hero.pos);

                            if (mageHand.equippedWand != null) {
                                mageHand.equipWand(mageHand.equippedWand);
                                mageHand.yell(Messages.get(MageHand.class, "appear"));
                                Sample.INSTANCE.play(Assets.Sounds.MASTERY);
                                mageHand.sayAppeared();
                            }

                            Invisibility.dispel(hero);
                            Talent.onArtifactUsed(hero);
                            updateQuickslot();
                        }
                    } else {
                        GLog.w(Messages.get(MageHand.class, "already_exists"));
                    }
                    break;
                case AC_TARGET_ENEMY:
                    if(mageHand != null){
                            GameScene.selectCell(enemySelector);
                    }
                    break;
            }
        }

        public CellSelector.Listener handDirector = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell == null) return;

                boolean isVisible = Dungeon.level.heroFOV[cell];

                if (isVisible) {
                    boolean isInBounds = cell < Dungeon.level.length();
                    Char enemy = Actor.findChar(cell);

                    if ((Dungeon.level.map[cell] == Terrain.CHASM || Dungeon.level.passable[cell]) && isInBounds && enemy == null) {
                        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                            if (mob instanceof MageHand) {
                                ScrollOfTeleportation.appear(mob, cell);
                            }
                        }
                        curUser.spend(Actor.TICK);
                        curUser.busy();
                        curUser.sprite.operate(curUser.pos);
                        Sample.INSTANCE.play(Assets.Sounds.READ);
                        Emitter e = curUser.sprite.centerEmitter();
                        e.pos(e.x - 2, e.y - 6, 4, 4);
                        e.start(Speck.factory(Speck.STAR), 0.05f, 20);
                    } else {
                        GLog.w(Messages.get(MageHand.class, "invalid_target"));
                    }
                } else {
                    GLog.w(Messages.get(MageHand.class, "out_of_range"));
                }
            }

            @Override
            public String prompt() {
                return "\"" + Messages.get(DriedRose.GhostHero.class, "direct_prompt") + "\"";
            }
        };

        public CellSelector.Listener enemySelector = new CellSelector.Listener(){

            @Override
            public void onSelect(Integer cell) {
                if (cell == null) return;

                Sample.INSTANCE.play( Assets.Sounds.GHOST );

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob instanceof MageHand) {
                        ((MageHand) mob).directTocell(cell);
                    }
                }

            }

            @Override
            public String prompt() {
                return  "\"" + Messages.get(DriedRose.GhostHero.class, "direct_prompt") + "\"";
            }
        };
    }

}