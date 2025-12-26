package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfScale;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand.WandOfBlueFuck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MageHandSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MageHand extends DirectableAlly {

    private ArrayList<Wand> wands = hero.belongings.getAllItems(Wand.class);

    private int chargeNeeded = 2;

    {
        spriteClass = MageHandSprite.class;
        flying = true;
        state = HUNTING;
        properties.add(Property.UNKNOWN);
        immunities.add(Blob.class);
        immunities.add(Buff.class);
        viewDistance =10;
        invisible = 1;
    }

    @Override
    public void aggro(Char ch) {
        enemy = ch;
        if (!movingToDefendPos && alignment != Alignment.ALLY && state != PASSIVE){
            state = HUNTING;
        }
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        // 先执行伤害逻辑
        super.damage(0, src, type);

        // 尝试传送到英雄周围5x5区域
        if (!teleportNearHero()) {
            // 如果失败，尝试传送到全图随机位置
            teleportToRandomLocation();
        }
    }

    // 尝试传送到英雄周围5x5区域
    private boolean teleportNearHero() {
        int heroPos = Dungeon.hero.pos;
        ArrayList<Integer> validPositions = new ArrayList<>();

        // 获取5x5区域内的有效位置
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                int newPos = heroPos + i + j * Dungeon.level.width();

                // 检查位置是否有效
                if (isValidTeleportPosition(newPos)) {
                    validPositions.add(newPos);
                }
            }
        }

        // 如果找到有效位置，随机选择一个传送
        if (!validPositions.isEmpty()) {
            int targetPos = Random.element(validPositions);
            teleportTo(targetPos);
            return true;
        }

        return false;
    }

    // 传送到全图随机位置
    private void teleportToRandomLocation() {
        ArrayList<Integer> validPositions = new ArrayList<>();

        // 遍历整个地图寻找有效位置
        for (int i = 0; i < Dungeon.level.length(); i++) {
            if (isValidTeleportPosition(i)) {
                validPositions.add(i);
            }
        }

        // 如果找到有效位置，随机选择一个传送
        if (!validPositions.isEmpty()) {
            int targetPos = Random.element(validPositions);
            teleportTo(targetPos);
        }

    }

    // 检查位置是否适合传送
    private boolean isValidTeleportPosition(int pos) {
        return pos >= 0
                && pos < Dungeon.level.length()
                && !Dungeon.level.solid[pos]
                && !Dungeon.level.pit[pos]
                && Actor.findChar(pos) == null;
    }

    // 执行传送
    private void teleportTo(int targetPos) {
        // 添加传送特效
        sprite.emitter().burst(Speck.factory(Speck.LIGHT), 6);

        // 更新位置
        pos = targetPos;

        // 更新精灵位置
        sprite.place(pos);

        // 添加到达特效
        sprite.emitter().burst(Speck.factory(Speck.LIGHT), 6);

        // 更新视野
        Dungeon.level.occupyCell(this);

        // 如果需要，可以添加音效
        Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
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
        // 在每回合结束时处理充能
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


    // 修改equipWand方法
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
        if (enemy == null) {
            enemy = chooseEnemy();
            if (enemy == null) {
                return false;
            }
        }
        if (magesStaff != null) {
            Wand mwand = magesStaff.wand;
            if (mwand != null && wandCooldown == 0) {
                if (mwand.curCharges > 0) {
                    Ballistica attack = new Ballistica(pos, enemy.pos, MagicMissile.WARD);
                    // 检查英雄是否在攻击路径上
                    if (isHeroInAttackPath(attack)) {
                        return false; // 英雄在路径上，不能直接攻击
                    }
                    return attack.collisionPos == enemy.pos;
                } else {
                    // 检查法师之手天赋
                    if (Dungeon.hero.hasTalent(Talent.MYSTICAL_CHARGE)) {
                        // 检查是否有其他可用法杖的充能
                        for (Wand w : wands) {
                            if (w.curCharges >= chargeNeeded) {
                                Ballistica attack = new Ballistica(pos, enemy.pos, MagicMissile.WARD);
                                if (isHeroInAttackPath(attack)) {
                                    return false;
                                }
                                return attack.collisionPos == enemy.pos;
                            } else {
                                return Dungeon.level.adjacent(pos, enemy.pos);
                            }
                        }
                    }
                }
            }
        } else if (equippedWand != null && wandCooldown == 0) {
            if (equippedWand.curCharges > 0) {
                Ballistica attack = new Ballistica(pos, enemy.pos, MagicMissile.WARD);
                if (isHeroInAttackPath(attack)) {
                    return false;
                }
                return attack.collisionPos == enemy.pos;
            } else {
                // 检查法师之手天赋
                if (Dungeon.hero.hasTalent(Talent.MYSTICAL_CHARGE)) {
                    // 检查是否有其他可用法杖的充能
                    for (Wand w : wands) {
                        if (w.curCharges >= chargeNeeded) {
                            Ballistica attack = new Ballistica(pos, enemy.pos, MagicMissile.WARD);
                            if (isHeroInAttackPath(attack)) {
                                return false;
                            }
                            return attack.collisionPos == enemy.pos;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    protected boolean doAttack(Char enemy) {
        if (magesStaff != null) {
            Wand mwand = magesStaff.wand;
            if (mwand != null && wandCooldown == 0) {
                if (mwand.curCharges > 0) {
                    Ballistica attack = new Ballistica(pos, enemy.pos, MagicMissile.WARD);
                    if (isHeroInAttackPath(attack)) {
                        // 英雄在路径上，尝试移动到更好的位置
                        return tryMoveToBetterPosition(enemy);
                    }
                    if (fieldOfView[pos] || fieldOfView[enemy.pos]) {
                        sprite.zap(enemy.pos);
                        return false;
                    } else {
                        zap();
                        return true;
                    }
                } else {
                    // 检查法师之手天赋
                    if (Dungeon.hero.hasTalent(Talent.MYSTICAL_CHARGE)) {
                        if (tryMysticalCharge()) {
                            Ballistica attack = new Ballistica(pos, enemy.pos, MagicMissile.WARD);
                            if (isHeroInAttackPath(attack)) {
                                return tryMoveToBetterPosition(enemy);
                            }
                            if (fieldOfView[pos] || fieldOfView[enemy.pos]) {
                                sprite.zap(enemy.pos);
                                return false;
                            } else {
                                zap();
                                return true;
                            }
                        }
                    }
                    // 没有充能时进行近战攻击
                    if (fieldOfView[pos] || fieldOfView[enemy.pos]) {
                        sprite.attack(enemy.pos);
                    }
                    return false;
                }
            }
        } else {
            if (equippedWand != null && wandCooldown == 0) {
                if (equippedWand.curCharges > 0) {
                    Ballistica attack = new Ballistica(pos, enemy.pos, MagicMissile.WARD);
                    if (isHeroInAttackPath(attack)) {
                        return tryMoveToBetterPosition(enemy);
                    }
                    if (fieldOfView[pos] || fieldOfView[enemy.pos]) {
                        sprite.zap(enemy.pos);
                        return false;
                    } else {
                        zap();
                        return true;
                    }
                } else {
                    // 检查法师之手天赋
                    if (Dungeon.hero.hasTalent(Talent.MYSTICAL_CHARGE)) {
                        for (Wand w : wands) {
                            if (w.curCharges >= chargeNeeded) {
                                Ballistica attack = new Ballistica(pos, enemy.pos, MagicMissile.WARD);
                                if (isHeroInAttackPath(attack)) {
                                    return tryMoveToBetterPosition(enemy);
                                }
                                if (fieldOfView[pos] || fieldOfView[enemy.pos]) {
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
            } else {
                return false;
            }
        }
        return false;
    }

    // 检查英雄是否在攻击路径上
    private boolean isHeroInAttackPath(Ballistica attack) {
        for (int i = 0; i < attack.path.size(); i++) {
            int p = attack.path.get(i);
            if (p == Dungeon.hero.pos) {
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
//        if(equippedWand != null){
//            invisible = 0;
//        } else {
//            invisible = 1;
//        }

        if (enemy == null || !enemy.isAlive()) {
            enemy = chooseEnemy();
        }

        return super.act();
    }

    // 添加快速充能方法
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
            } else if (tryMysticalCharge()) {
                return;
            }
        } else if (equippedWand != null && enemy != null) {
            if (equippedWand.curCharges > 0) {
                zapWithEquippedWand();
            } else if (tryMysticalCharge()) {
                return;
            }
        }
    }

    private boolean tryMysticalCharge() {
        if (!hero.hasTalent(Talent.MYSTICAL_CHARGE)) {
            return false;
        }

        // 获取背包中的所有法杖
        ArrayList<Wand> availableWands = new ArrayList<>();
        for (Item item : hero.belongings) {
            if (item instanceof Wand) {
                Wand wand = (Wand) item;
                // 排除老魔杖和当前装备的法杖
                if(magesStaff != null){
                    if (wand != magesStaff.wand && wand.curCharges >= chargeNeeded) {
                        availableWands.add(wand);
                    }
                }
                if(equippedWand != null){
                    if (wand != equippedWand && wand.curCharges >= chargeNeeded) {
                        availableWands.add(wand);
                    }
                }
            }
        }

        if (availableWands.isEmpty()) {
            return false;
        }

        // 找到等级最低的法杖
        Wand lowestLevelWand = findLowestLevelWand(availableWands);
        if (lowestLevelWand == null) {
            return false;
        }

        consumeWandCharges(lowestLevelWand, chargeNeeded);
        executeWandEffect(magesStaff != null ? magesStaff.wand : equippedWand);
        finalizeWandUse(magesStaff != null ? magesStaff.wand : equippedWand);
        return true;
    }

    private Wand findLowestLevelWand(ArrayList<Wand> wands) {
        Wand lowestLevelWand = null;
        for (Wand w : wands) {
            if (lowestLevelWand == null || w.level < lowestLevelWand.level) {
                lowestLevelWand = w;
            }
        }
        return lowestLevelWand;
    }

    private void consumeWandCharges(Wand wand, int chargeNeeded) {
        wand.curCharges -= chargeNeeded;

        // 计算内部充能返还
        float internalChargeReturn = 0f;
        switch (hero.pointsInTalent(Talent.MYSTICAL_CHARGE)) {
            case 1: internalChargeReturn = 0f; break;
            case 2: internalChargeReturn = 0.25f; break;
            case 3: internalChargeReturn = 0.5f; break;
        }

        // 使用法杖自身的altPartialCharge属性存储内部充能
        wand.altPartialCharge += internalChargeReturn;

        // 检查是否可以返还真实充能
        if (wand.altPartialCharge >= 1f) {
            int realChargesToAdd = (int) wand.altPartialCharge;
            wand.curCharges += realChargesToAdd;
            wand.altPartialCharge -= realChargesToAdd;
        }

        // 更新快捷栏显示
        Item.updateQuickslot();
    }

    private void zapWithMagesStaff() {
        Wand mwand = magesStaff.wand;
        executeWandEffect(mwand);
        mwand.curCharges -= Math.max(1, mwand.chargesPerCast());
        finalizeWandUse(mwand);
    }

    private void zapWithEquippedWand() {
        executeWandEffect(equippedWand);
        equippedWand.curCharges -= Math.max(1, equippedWand.chargesPerCast());
        finalizeWandUse(equippedWand);
    }

    private void executeWandEffect(Wand wand) {
        if (wand.cursed) {
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
                wand instanceof WandOfRegrowth;
    }

    private void finalizeWandUse(Wand wand) {
        wandCooldown = 3;
        spend(1f);
        //GLog.i(Messages.get(this, "wand_used", wand.name()));
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
        } else if (equippedWand != null) {
            damage += equippedWand.level();
        }
        return damage;
    }

    public void sayAppeared(){
        yell(Messages.get(this, "appear"));
        if (ShatteredPixelDungeon.scene() instanceof GameScene) {
            Sample.INSTANCE.play(Assets.Sounds.GHOST);
        }
    }

    private static final String WAND =        "wand";
    private static final String MAGE_STAFF = "mage_staff";

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

    @Override
    public Char chooseEnemy() {
        Char bestEnemy = null;
        float highestThreat = 0;

        // 获取视野内的所有敌人
        for (Char ch : Actor.chars()) {
            if (ch instanceof Mob && ch.alignment == Alignment.ENEMY && fieldOfView[ch.pos]) {
                Mob mob = (Mob) ch;
                // 计算威胁值
                float threat = calculateThreat(mob);

                // 如果威胁值更高，则选择这个敌人
                if (threat > highestThreat) {
                    highestThreat = threat;
                    bestEnemy = mob;
                }
            }
        }

        return bestEnemy;
    }

    // 计算敌人对英雄的威胁值
    private float calculateThreat(Mob enemy) {
        // 基础威胁值基于敌人的生命值和攻击力
        float threat = enemy.HP * 0.5f + enemy.damageRoll() * 2;

        // 距离英雄越近，威胁值越高
        float distance = Dungeon.level.distance(enemy.pos, Dungeon.hero.pos);
        threat += 100 / (distance + 1);

        // 检查敌人是否正在攻击英雄（通过检查敌人的目标是否是英雄）
        if (enemy.enemy() == Dungeon.hero) {
            threat *= 1.5f;
        }

        // 如果敌人有特殊状态（如精英敌人），威胁值更高
        if (enemy.buff(ChampionEnemy.class) != null) {
            threat *= 5f;
        }

        return threat;
    }

    // 尝试移动到更好的位置以避免误伤英雄并攻击敌人
    private boolean tryMoveToBetterPosition(Char enemy) {
        // 寻找可以攻击敌人且不经过英雄的位置
        ArrayList<Integer> candidates = new ArrayList<>();

        // 检查周围8个方向
        int[] neighbors = new int[]{
                pos - 1, pos + 1,
                pos - Dungeon.level.width(), pos + Dungeon.level.width(),
                pos - Dungeon.level.width() - 1, pos - Dungeon.level.width() + 1,
                pos + Dungeon.level.width() - 1, pos + Dungeon.level.width() + 1
        };

        // 计算每个候选位置的评分
        ArrayList<Integer> bestCandidates = new ArrayList<>();
        float bestScore = Float.NEGATIVE_INFINITY;

        for (int i : neighbors) {
            if (Dungeon.level.passable[i] && Actor.findChar(i) == null) {
                Ballistica attack = new Ballistica(i, enemy.pos, MagicMissile.WARD);
                if (!isHeroInAttackPath(attack) && attack.collisionPos == enemy.pos) {
                    // 计算这个位置的评分
                    float score = evaluatePosition(i, enemy);

                    // 如果评分更高，则清空之前的最佳候选
                    if (score > bestScore) {
                        bestScore = score;
                        bestCandidates.clear();
                        bestCandidates.add(i);
                    }
                    // 如果评分相同，则添加到候选列表
                    else if (score == bestScore) {
                        bestCandidates.add(i);
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
        int heroPos = Dungeon.hero.pos;
        int[] heroNeighborOffsets = new int[]{
                -1, +1,
                -Dungeon.level.width(), +Dungeon.level.width(),
                -Dungeon.level.width() - 1, -Dungeon.level.width() + 1,
                +Dungeon.level.width() - 1, +Dungeon.level.width() + 1
        };

        for (int offset : heroNeighborOffsets) {
            int i = heroPos + offset;
            if (Dungeon.level.passable[i] && Actor.findChar(i) == null && i != pos) {
                heroNeighbors.add(i);
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
        float distanceToHero = Dungeon.level.distance(position, Dungeon.hero.pos);
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


}
