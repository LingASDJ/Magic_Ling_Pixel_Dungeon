package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
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
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MageHand extends DirectableAlly {

    {
        spriteClass = MageHandSprite.class;
        flying = true;
        state = HUNTING;
        properties.add(Property.UNKNOWN);
        immunities.add(Blob.class);
        viewDistance =10;
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
    private boolean teleportToRandomLocation() {
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
            return true;
        }

        return false;
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
            chargeWand();
        }
    }

    private void chargeWand() {
        if (equippedWand == null || equippedWand.curCharges >= equippedWand.maxCharges) {
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
        // 重置部分充能值
        wand.partialCharge = 0f;
        // 立即开始充能
        wand.charge(this);
        GLog.i(Messages.get(this, "wand_equipped", wand.name()));
    }

    public void unequipWand() {
        if (equippedWand != null) {
            GLog.i(Messages.get(this, "wand_unequipped", equippedWand.name()));
            equippedWand.stopCharging();
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
        if (magesStaff != null) {
            // 装备法师之杖时，检查是否在攻击范围内
            return Dungeon.level.adjacent(pos, enemy.pos);
        } else if (equippedWand != null && wandCooldown == 0) {
            if (equippedWand.curCharges > 0) {
                return new Ballistica(pos, enemy.pos, MagicMissile.WARD).collisionPos == enemy.pos;
            } else {
                return false;
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

    @Override
    protected boolean act() {
        if (wandCooldown > 0) {
            wandCooldown--;
        }
        if(equippedWand != null){
            invisible = 0;
        } else {
            invisible = 1;
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

    protected boolean doAttack(Char enemy) {
        // 只有装备了法杖且不在冷却中才能攻击
        // 如果装备了法师之杖，使用近战攻击
        if (magesStaff != null) {
            if (fieldOfView[pos] || fieldOfView[enemy.pos]) {
                sprite.attack(enemy.pos);
                return false;
            } else {
                return true;
            }
        } else {
            if (equippedWand != null && wandCooldown == 0) {
                if (equippedWand.curCharges > 0) {
                    // 有能量时使用法杖攻击
                    if (fieldOfView[pos] || fieldOfView[enemy.pos]) {
                        // 可见时播放法杖动画
                        sprite.zap(enemy.pos);
                        return false; // 等待动画完成
                    } else {
                        // 不可见时直接施法
                        zap();
                        return true;
                    }
                } else {
                    // 法杖没有能量，不进行攻击
                    GLog.w(Messages.get(this, "wand_no_energy", equippedWand.name()));
                    return false;
                }
            } else {
                return false;
            }
        }
    }


    public void onZapComplete() {
        zap();
        next();
    }
    

    // 同时修改useWand方法，添加能量检查
    private void zap() {
        if (equippedWand != null && enemy != null) {
            if (equippedWand.curCharges > 0) {
                if(equippedWand instanceof WandOfFireblast || equippedWand instanceof WandOfBlueFuck || equippedWand instanceof WandOfScale || equippedWand instanceof WandOfRegrowth){
                    equippedWand.onAIZap(new Ballistica(pos, enemy.pos, equippedWand.collisionProperties));
                } else {
                    equippedWand.onZap(new Ballistica(pos, enemy.pos, equippedWand.collisionProperties));
                }
                wandCooldown = 3;
                equippedWand.curCharges--;
                spend(1f);
                GLog.i(Messages.get(this, "wand_used", equippedWand.name()));

            }
        }
    }

    @Override
    public int attackSkill(Char target) {
        int acc = hero.lvl + 9;
        if (equippedWand != null) {
            acc += equippedWand.level();
        }
        return acc;
    }

    // 修改damageRoll方法
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

    @Override
    public boolean isImmune(Class effect) {
        if (effect == Burning.class ||
                effect == CorrosiveGas.class ||
                effect == MagicImmune.class) {
            return true;
        }
        return super.isImmune(effect);
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
}
