package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//弑君
//二阶，力量需求12
//初始2-15，成长1-3，伏击修正50%
//嬗变这把武器会使得它的阶级增加1。（初始+1-5，成长+0-1，力量需求+2）
//永远都会回到应有的人手中。

public class KillKing extends MeleeWeapon {

    private static final int BASE_TIER = 2;
    private static final int MAX_TIER = 6;
    private static final String TRANSMUTED = "transmuted";

    {
        image = ItemSpriteSheet.SKIN_5;

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
        return Messages.get(this, "stats_desc",
                augment.damageFactor(ambushMin),
                augment.damageFactor(max()));
    }

    // ==================== 嬗变：阶级 +1 ====================

    public boolean canTransmuteUpgrade() {
        return tier < MAX_TIER;
    }

    public boolean transmuteUpgrade() {
        if (!canTransmuteUpgrade()) return false;
        transmuted++;
        tier = Math.min(MAX_TIER, tier + 1);
        return true;
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
}
