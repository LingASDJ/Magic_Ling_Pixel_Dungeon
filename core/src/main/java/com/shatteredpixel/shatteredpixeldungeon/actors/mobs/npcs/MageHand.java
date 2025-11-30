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
        immunities.add(Buff.class);
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
    private boolean isBlockingPath = false;
    private int blockCheckCooldown = 0;

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
    protected boolean canAttack( Char enemy ) {
        if(equippedWand!=null && wandCooldown == 0) {
            if(equippedWand.curCharges>0){
                return new Ballistica(pos, enemy.pos, MagicMissile.WARD).collisionPos == enemy.pos;
            } else {
                return false;
            }
        } else {
            return super.canAttack(enemy);
        }
    }

    public boolean hasWand() {
        return equippedWand != null;
    }

    public Wand getEquippedWand() {
        return equippedWand;
    }

    public boolean isWandReady() {
        return wandCooldown == 0;
    }

    private void updateWandStats() {
        if (equippedWand == null) return;
        defenseSkill = hero.lvl + 4 + equippedWand.level();
    }

    @Override
    protected boolean act() {
        // 每5回合检查一次是否阻挡路径
        if (blockCheckCooldown <= 0) {
            checkBlockingPath();
            blockCheckCooldown = 5;
        } else {
            blockCheckCooldown--;
        }
        manageCharging();
        // 如果正在阻挡路径，开始消散计时
        if (isBlockingPath) {
            disappear();
            return true;
        }

        // 如果处于重生冷却中
        if (respawnTimer > 0) {
            respawnTimer--;
            if (respawnTimer == 0) {
                respawn();
            }
            return true;
        }

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

    // 修改攻击逻辑，添加动画支持
    @Override
    protected boolean doAttack(Char enemy) {
        // 优先使用法杖攻击（如果法杖就绪且有敌人）
        if (equippedWand != null && wandCooldown == 0) {
            if (equippedWand.curCharges > 0) {
                // 有能量时使用法杖攻击
                if (fieldOfView[pos] || fieldOfView[enemy.pos]) {
                    // 可见时播放法杖动画
                    sprite.zap( enemy.pos );
                    return false; // 等待动画完成
                } else {
                    // 不可见时直接施法
                    zap();
                    return true;
                }
            } else {
                // 法杖没有能量，转换为近战攻击
                GLog.w(Messages.get(this, "wand_no_energy", equippedWand.name()));
                return super.doAttack(enemy);
            }
        }

        // 如果没有法杖或法杖在冷却中，进行近战攻击
        return super.doAttack(enemy);
    }


    public void onZapComplete() {
        zap();
        next();
    }
    

    // 同时修改useWand方法，添加能量检查
    private void zap() {
        if (equippedWand != null && enemy != null) {
            if (equippedWand.curCharges > 0) {
                equippedWand.onZap(new Ballistica(pos, enemy.pos, equippedWand.collisionProperties));
                wandCooldown = 3;
                equippedWand.curCharges--;
                spend(1f);
                GLog.i(Messages.get(this, "wand_used", equippedWand.name()));
            }
        }
    }


    private void checkBlockingPath() {
        isBlockingPath = false;

        // 检查周围8格内是否有敌人
        for (int i = 0; i < Dungeon.level.length(); i++) {
            if (Dungeon.level.distance(pos, i) <= 2) { // 2格范围内
                Char ch = Actor.findChar(i);
                if (ch != null && ch != this && ch.alignment == Char.Alignment.ENEMY) {
                    // 如果敌人无法移动到法师之手的位置，则认为被阻挡
                    if (!Dungeon.level.passable[i] || Dungeon.level.avoid[i]) {
                        isBlockingPath = true;
                        break;
                    }
                }
            }
        }
    }

    private void disappear() {
        yell(Messages.get(this, "disappear"));
        sayDisappear();

        // 保存当前法杖信息
        Wand savedWand = equippedWand;
        equippedWand = null;

        destroy();
        respawnTimer = 50; // 50回合后重生

        // 重生时恢复法杖
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                if (respawnTimer == 0) {
                    MageHand newHand = new MageHand();
                    newHand.equipWand(savedWand);
                }
            }
        });
    }

    private void respawn() {
        // 在英雄附近寻找空位重生
        int newPos = Dungeon.level.randomRespawnCell(this);
        if (newPos != -1) {
            MageHand newHand = new MageHand();
            newHand.pos = newPos;
            GameScene.add(newHand);
            newHand.sayAppeared();

            if (equippedWand != null) {
                newHand.equipWand(equippedWand);
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
    public float attackDelay() {
        float delay = 1f; // 默认1回合攻击延迟

        // 如果有法杖且是老魔杖（通过名称或其他特征判断）
        if (equippedWand != null && equippedWand.name().contains("Elder")) {
            delay = 1f; // 老魔杖近战攻击延迟为1回合
        }

        return delay;
    }

    @Override
    public int damageRoll() {
        if (equippedWand != null) {
            // 根据法杖类型决定近战伤害
            if (equippedWand.name().contains("Elder")) {
                // 老魔杖的近战伤害较高
                return Random.NormalIntRange(5 + equippedWand.level(), 10 + equippedWand.level() * 2);
            }
            // 其他法杖的近战伤害
            return Random.NormalIntRange(2 + equippedWand.level(), 5 + equippedWand.level());
        }
        return Random.NormalIntRange(1, 3); // 默认伤害
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        if (equippedWand != null) {
            // 法杖可能有的特殊近战效果
            damage += equippedWand.level(); // 简单增加法杖等级的伤害
        }
        return damage;
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        // 无敌状态，不受任何伤害
        // 但仍然记录被攻击的事件
        if (src instanceof Char) {
            GLog.i(Messages.get(this, "immune_damage"));
        }
    }

    @Override
    public boolean isImmune(Class effect) {
        // 对大多数负面效果免疫
        if (effect == Burning.class ||
                effect == CorrosiveGas.class ||
                effect == MagicImmune.class) {
            return true;
        }
        return super.isImmune(effect);
    }

    @Override
    public void destroy() {
        // 清理资源
        equippedWand = null;
        super.destroy();
    }

    public void sayAppeared(){
        yell(Messages.get(this, "appear"));
        if (ShatteredPixelDungeon.scene() instanceof GameScene) {
            Sample.INSTANCE.play(Assets.Sounds.GHOST);
        }
    }

    public void sayDisappear(){
        yell(Messages.get(this, "disappear"));
        Sample.INSTANCE.play(Assets.Sounds.GHOST);
    }

    @Override
    public void defendPos(int cell) {
        yell(Messages.get(this, "directed_position"));
        super.defendPos(cell);
    }

    @Override
    public void followHero() {
        yell(Messages.get(this, "directed_follow"));
        super.followHero();
    }

    @Override
    public void targetChar(Char ch) {
        yell(Messages.get(this, "directed_attack"));
        super.targetChar(ch);
    }

    private static final String WAND =        "wand";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        if (equippedWand != null)  bundle.put( WAND,equippedWand );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(WAND))
            equippedWand = (DamageWand) bundle.get( WAND );
    }
}
