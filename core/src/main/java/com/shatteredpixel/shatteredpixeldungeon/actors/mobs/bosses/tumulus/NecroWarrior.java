package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecroWarriorSprite;
import com.watabou.utils.Random;

/**
 * 死灵勇士：从一众死灵中选拔出的佼佼者，拥有接受白魔女擢升赐福的潜质。
 *
 * <p>生命为白鹭的 8%；每次攻击行为使目标受到其当前生命值 11% 的物理伤害。
 * 防御 0~4，精准 21，闪避 19，属性：亡灵、死灵。</p>
 */
public class NecroWarrior extends Mob {

    public NecroWarrior() {
        spriteClass = NecroWarriorSprite.class;

        HP = HT = Math.max(1, Math.round(Egret.MAX_HP * 0.08f));
        defenseSkill = 19;
        baseSpeed = 1f;
        EXP = 0;
        maxLvl = 30;
        lootChance = 0f;

        properties.add(Property.UNDEAD);
        properties.add(Property.NECRO);
        properties.add(Property.TUMULUS);
    }

    @Override
    public int attackSkill(Char target) {
        return 21;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 2);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy != null && enemy.isAlive()) {
            int percent = Math.max(1, Math.round(enemy.HP * 0.11f));
            enemy.damage(percent, this, DamageType.PHYSICAL);
        }
        return super.attackProc(enemy, damage);
    }

    @Override
    public void rollToDropLoot() {
        // 召唤物不掉落
    }
}
