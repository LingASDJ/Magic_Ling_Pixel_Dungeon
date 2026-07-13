package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecroPioneerSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class NecroPioneer extends Mob {

    {
        HT = 55;
        HP = HT;
        defenseSkill = 22;
        EXP = 12;
        maxLvl = 25;
        spriteClass = NecroPioneerSprite.class;
        alignment = Alignment.ENEMY;
        properties.add(Property.UNDEAD);
    }

    @Override
    public int attackSkill(Char target) {
        return 22;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(10, 26);
    }

    @Override
    public float speed() {
        return 2f;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage); // 近战攻击

        if (enemy != null && enemy.isAlive() && enemy == this.enemy) {
            // 1. 计算远离方向
            int x = pos % level.width();
            int y = pos / level.width();
            int ex = enemy.pos % level.width();
            int ey = enemy.pos / level.width();

            int stepX = Integer.compare(x, ex);
            int stepY = Integer.compare(y, ey);

            int newPos = pos;
            for (int i = 0; i < 2; i++) {
                int nextPos = newPos + stepX + stepY * level.width();
                if (level.passable[nextPos] && Actor.findChar(nextPos) == null) {
                    newPos = nextPos;
                } else {
                    break;
                }
            }

            // 2. 传送位移（带特效）
            if (newPos != pos) {
                ScrollOfTeleportation.appear(this, newPos);
                level.updateFieldOfView(this, fieldOfView);
            }

            // 3. 投掷飞刀（播放投掷动画 + 投射物飞行）
            if (sprite != null) {
                sprite.zap(enemy.pos); // 播放投掷动作
                final BoneKnife knife = new BoneKnife();
                // （可选）可根据怪物等级设置飞刀等级，这里保持0
                final int throwDamage = Math.max(0, damage / 2); // 50% 近战伤害
                final Char attacker = this;
                final Char defender = enemy;

                // 发射投射物
                ((MissileSprite) ((NecroPioneerSprite) sprite).parent.recycle(MissileSprite.class))
                        .reset(sprite, enemy.pos, knife, new Callback() {
                            @Override
                            public void call() {
                                // 飞刀命中效果（流血、残废，并消耗耐久）
                                knife.proc(attacker, defender, throwDamage);
                            }
                        });
            }
        }
        return damage;
    }

    @Override
    public Item createLoot() {
        return new BoneKnife(); // 死亡掉落一把全新的飞刀
    }

    @Override
    public float lootChance() {
        return 1f;
    }

    // ---------- 削骨飞刀（内部类） ----------
    public static class BoneKnife extends MissileWeapon {


        int MAX;
        int MIN;


        {
            image = 0;          // 请替换为实际资源 ID
            hitSound = Assets.Sounds.HIT_SLASH;
            hitSoundPitch = 1.2f;
            tier = 2;

            MIN = 4;            // 基础伤害（但在投掷中我们传入自定义伤害）
            MAX = 8;

            durability = 5;
        }

        @Override
        public int damageRoll(Char owner) {
            // 这个方法的返回值仅在 MissileWeapon 的 proc 中被用于基础伤害
            // 但我们传入自定义伤害，所以该方法不会影响投掷伤害
            return Random.NormalIntRange(MIN, MAX);
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            // 1. 施加流血与残废（受等级加成）
            int bleedAmt = Random.NormalIntRange(8, 20);
            int crippleTurns = 4;
            int level = level();
            bleedAmt += Random.Int(level * 6 + 1);
            crippleTurns += level / 2;

            Buff.affect(defender, Bleeding.class).set(bleedAmt);
            Buff.affect(defender, Cripple.class, crippleTurns);

            // 2. 消耗耐久
            if (durability > 0) {
                durability--;
                if (durability == 0 && attacker instanceof Hero) {
                    detach(((Hero) attacker).belongings.backpack);
                    GLog.w(Messages.get(BoneKnife.class, "shattered"));
                }
            }

            // 3. 调用父类，返回传入的伤害（不额外增加）
            return super.proc(attacker, defender, damage);
        }
    }
}