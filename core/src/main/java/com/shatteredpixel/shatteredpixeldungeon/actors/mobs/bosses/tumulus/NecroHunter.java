package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecroHunterSprite;
import com.watabou.utils.Random;

/**
 * 死灵猎手：经由白魔女赐福后进化的死灵勇士，更快、更强、更致命。
 *
 * <p>生命为白鹭的 12%；每次攻击行为使目标受到其最大生命值 11% 的魔法伤害，
 * 并施加 2 回合残废。防御 0~6，精准 24，闪避 22。</p>
 */
public class NecroHunter extends Mob {

    public NecroHunter() {
        spriteClass = NecroHunterSprite.class;

        HP = HT = Math.max(1, Math.round(Egret.MAX_HP * 0.12f));
        defenseSkill = 22;
        baseSpeed = 1.5f;
        EXP = 0;
        maxLvl = 30;
        lootChance = 0f;

        properties.add(Property.UNDEAD);
        properties.add(Property.NECRO);
        properties.add(Property.TUMULUS);
    }

    @Override
    public int attackSkill(Char target) {
        return 24;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 2);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 6);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy != null && enemy.isAlive()) {
            int percent = Math.max(1, Math.round(enemy.HT * 0.11f));
            enemy.damage(percent, this, DamageType.MAGIC);
            Buff.prolong(enemy, Cripple.class, 2f);
        }
        return super.attackProc(enemy, damage);
    }

    @Override
    public void rollToDropLoot() {
        // 召唤物不掉落
    }
}
