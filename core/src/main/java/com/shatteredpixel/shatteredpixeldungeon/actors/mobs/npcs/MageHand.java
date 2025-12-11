package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.DamageWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MageHandSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class MageHand extends DirectableAlly {

    {
        spriteClass = MageHandSprite.class;
        flying = true;
        state = HUNTING;
        properties.add(Property.UNKNOWN);
        immunities.add(Blob.class);
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return true;
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
    private int wandCooldown = 0;
    private int respawnTimer = -1;

    public MageHand(){
        super();
        HP = HT = 1;
        if (equippedWand != null) {
            equippedWand.partialCharge = 0f;
        }
    }

    private void manageCharging() {
        chargeWand();
    }


    // 修改equipWand方法
    public void equipWand(Wand wand) {
        this.equippedWand = wand;
        // 重置部分充能值
        wand.partialCharge = 0f;
        updateWandStats();
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

    @Override
    protected boolean canAttack(Char enemy) {
        // 只有装备了法杖且不在冷却中才能攻击
        if (equippedWand != null && wandCooldown == 0) {
            // 检查法杖是否有能量
            if (equippedWand.curCharges > 0) {
                invisible = 0;
                return new Ballistica(pos, enemy.pos, MagicMissile.WARD).collisionPos == enemy.pos;
            } else {
                invisible = 1;
                return false;
            }
        } else {
            invisible = 1;
            return false;
        }
    }

    public boolean hasWand() {
        return equippedWand != null;
    }

    public Wand getEquippedWand() {
        return equippedWand;
    }

    private void updateWandStats() {
        if (equippedWand == null) return;
        defenseSkill = hero.lvl + 4 + equippedWand.level();
    }

    @Override
    protected boolean act() {
        // 更新法杖冷却
        if (wandCooldown > 0) {
            wandCooldown--;
        }

        // 如果没有法杖或法杖在冷却中，进行近战攻击
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


    public void onZapComplete() {
        zap();
        next();
    }
    

    // 同时修改useWand方法，添加能量检查
    private void zap() {
        if (equippedWand != null && enemy != null) {
            if (equippedWand.curCharges > 0) {
                if(equippedWand instanceof WandOfFireblast){
                    ((WandOfFireblast) equippedWand).onAIZap(new Ballistica(pos, enemy.pos, equippedWand.collisionProperties));
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

    @Override
    public int damageRoll() {
        if (equippedWand != null) {
            if (equippedWand.name().contains("Elder")) {
                return Random.NormalIntRange(5 + equippedWand.level(), 10 + equippedWand.level() * 2);
            }
            return Random.NormalIntRange(2 + equippedWand.level(), 5 + equippedWand.level());
        }
        return Random.NormalIntRange(1, 3);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        if (equippedWand != null) {
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
    private static final String ID_R =     "ID";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);

        if (equippedWand != null)  bundle.put( WAND,equippedWand );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);

        if (bundle.contains(WAND))
            equippedWand = (Wand) bundle.get( WAND );
    }
}
