package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//弑君
//二阶，力量需求12
//初始2-15，成长1-3，伏击修正50%
//嬗变这把武器会使得它的阶级增加1。（初始+1-5，成长+0-1，力量需求+2）
//永远都会回到应有的人手中。
//武技：硝烟处刑，消耗2充能，进入隐身，隐身至多持续3回合，并强化下1次攻击，造成的最终伤害提升至150%。

public class KillKing extends MeleeWeapon {

    private static final int BASE_TIER = 2;
    private static final int MAX_TIER = 6;
    private static final String TRANSMUTED = "transmuted";

    {
        image = ItemSpriteSheet.KILL_KING;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 2;
    }

    /** 已嬗变次数，每嬗变一次阶级 +1 */
    private int transmuted = 0;

    // ==================== 伏击修正 50% ====================
    @Override
    public int damageRoll(Char owner) {
        Char enemy = owner instanceof Hero ? ((Hero) owner).enemy()
                : owner instanceof Mob ? ((Mob) owner).enemy()
                : null;

        if (enemy instanceof Mob && ambushed((Mob) enemy, owner)) {
            // 伏击修正：伤害区间变为 [最小伤害 + (最大伤害-最小伤害)*0.5, 最大伤害]
            int diff = max() - min();
            if (owner instanceof Hero) {
                Hero hero = (Hero) owner;
                int damage = augment.damageFactor(Hero.heroDamageIntRange(
                        min() + Math.round(diff * 0.5f),
                        max()));
                int exStr = hero.STR() - STRReq();
                if (exStr > 0) {
                    damage += Hero.heroDamageIntRange(0, exStr);
                }
                return damage;
            } else {
                // 非Hero拥有者（怪物/NPC等）同样生效
                return augment.damageFactor(Random.NormalIntRange(
                        min() + Math.round(diff * 0.5f),
                        max()));
            }
        }
        return super.damageRoll(owner);
    }

    /** 判断目标是否处于被偷袭（伏击）状态 */
    private boolean ambushed(Mob target, Char attacker) {
        if (attacker instanceof Hero) {
            return target.surprisedBy((Hero) attacker);
        }
        // 非Hero拥有者：surprisedBy 只对英雄生效，这里按原版逻辑用视野判定
        return (attacker.invisible > 0
                || target.fieldOfView == null
                || target.fieldOfView.length != Dungeon.level.length()
                || !target.fieldOfView[attacker.pos])
                && attacker.canSurpriseAttack();
    }

    @Override
    public String statsInfo() {
        // 实时显示当前伏击修正后的伤害区间
        int diff = max() - min();
        int ambushMin = min() + Math.round(diff * 0.5f);

        if(isIdentified()){
            return Messages.get(this, "stats_desc",
                    augment.damageFactor(ambushMin),
                    augment.damageFactor(max()));
        } else {
            return Messages.get(this, "stats_desc",
                   9,
                 15);
        }
    }

    // ==================== 嬗变：阶级 +1 ====================
    public boolean canTransmuteUpgrade() {
        return tier < MAX_TIER;
    }

    public void transmuteUpgrade() {
        if (!canTransmuteUpgrade()) return;
        transmuted++;
        tier = Math.min(MAX_TIER, tier + 1);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TRANSMUTED, transmuted);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        transmuted = bundle.getInt(TRANSMUTED);
        // tier 字段不参与存档，读档后按嬗变次数重新算回阶级
        tier = Math.min(MAX_TIER, BASE_TIER + transmuted);
    }

    // ==================== 决斗者武技：硝烟处刑 ====================
    @Override
    public String targetingPrompt() {
        return null; //不需要选目标，以自身为中心释放
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        beforeAbilityUsed(hero, null);
        AttackIndicator.target(null);

        Buff.prolong(hero, InvisibilityDLing.class,3f);
        Buff.prolong(hero, SmokeExecutionBuff.class, 3f);

        hero.spendAndNext(hero.attackDelay());
        afterAbilityUsed(hero);
    }

    public static class InvisibilityDLing extends Invisibility {
        @Override
        public void detach() {
            super.detach();
            Buff.detach(target,SmokeExecutionBuff.class);
        }
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        return 2;
    }

    public static class SmokeExecutionBuff extends FlavourBuff {

        private boolean usedDamageBoost = false;

        // 攻击伤害修正：仅第一次攻击生效，最终伤害 ×1.5
        public float damageMultiplier() {
            if (!usedDamageBoost) {
                usedDamageBoost = true;
                detach();
                return 1.5f;
            }
            return 1f;
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put("used", usedDamageBoost);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            usedDamageBoost = bundle.getBoolean("used");
        }
    }
}
