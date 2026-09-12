package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

//断生者
//四阶，力量需求17
//初始6-24，成长2-6
//每次命中都会让敌人一分为二：本体与分身各继承当前生命值与生命上限的一半。
//武技：腰斩，消耗2充能，对目标造成160%必中伤害，对与目标相连的所有敌人造成120%必中伤害，如果腰斩击杀了1个单位，获得1充能且本次攻击不消耗回合。每次释放至多获得1次充能。

public class LifeCutter extends MeleeWeapon{
    {
        image = ItemSpriteSheet.NO_LIVE;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
    }

    @Override
    public int STRReq(int lvl){
        int req = STRReq(tier, lvl)+1;
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    @Override
    public int max(int lvl) { return 24 + lvl * 6; }

    @Override
    public int min(int lvl) { return 6 + lvl * 2; }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 命中即标记目标：真正的分裂在伤害结算（Char.damage）后进行
        if (defender instanceof Mob
                && defender.alignment == Char.Alignment.ENEMY
                && !Char.hasProp(defender, Char.Property.BOSS)
                && !Char.hasProp(defender, Char.Property.MINIBOSS)){
            Buff.affect(defender, SplitMark.class);
        }
        return super.proc(attacker, defender, damage);
    }

    // 伤害结算（Char.damage）后调用：目标仍存活才分裂，本体与分身各继承一半血量和血量上限
    public static void trySplit(Char defender, Object src){
        if (!defender.isAlive()
                || !(defender instanceof Mob)
                || defender.alignment != Char.Alignment.ENEMY
                || Char.hasProp(defender, Char.Property.BOSS)
                || Char.hasProp(defender, Char.Property.MINIBOSS)
                || defender.HP < 2){
            return;
        }

        int cell = freeCellNear(defender);
        if (cell == -1){
            return;
        }

        try {
            int halfHP = defender.HP / 2;
            int halfHT = defender.HT / 2;
            defender.HP = halfHP;
            defender.HT = halfHT;

            Mob copy = Reflection.newInstance(((Mob) defender).getClass());
            copy.HP = halfHP;
            copy.HT = halfHT;
            copy.isEndLess = ((Mob) defender).isEndLess;

            GameScene.add(copy);
            ScrollOfTeleportation.appear(copy, cell);
            if (src instanceof Char){
                copy.aggro((Char) src);
            }
        } catch (Exception ignored) {
            // 遇到无法复制的特殊敌人时，静默跳过分裂
        }
    }

    // 在敌人周围找一个可站立的空格，找不到返回 -1
    private static int freeCellNear(Char ch){
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int n : PathFinder.NEIGHBOURS8) {
            int cell = ch.pos + n;
            if (Dungeon.level.passable[cell]
                    && Actor.findChar(cell) == null
                    && (!Char.hasProp(ch, Char.Property.LARGE) || Dungeon.level.openSpace[cell])) {
                candidates.add(cell);
            }
        }
        return candidates.size() > 0 ? Random.element(candidates) : -1;
    }

    // 一次性分裂标记：挂在目标身上，伤害结算后消费
    public static class SplitMark extends Buff {
    }

    // ========== 武技：腰斩 ==========
    // 每次使用武技消耗的充能点数（由决斗者的 Charger buff 提供）
    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 2;
    }

    // 选址文本与选址功能启用
    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    // 仿照MerchantSword的5g
    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        // 选址为空，则选取失败
        if (target == null) {
            return;
        }

        // 选址位置的目标若为空、为魅惑者、不在视野范围内，则选取失败
        Char enemy = Actor.findChar(target);
        if (enemy == null
                || enemy == hero
                || hero.isCharmedBy(enemy)
                || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        hero.belongings.abilityWeapon = this;
        if (!hero.canAttack(enemy)){
            GLog.w(Messages.get(this, "ability_target_range"));
            hero.belongings.abilityWeapon = null;
            return;
        }
        hero.belongings.abilityWeapon = null;

        // 对选择的目标进行攻击
        hero.sprite.attack(enemy.pos, new Callback() {
            @Override
            public void call() {
                // 充能回复标记（击杀了至少一个敌人后会使得该标记置为真并在后续回复充能）
                final boolean[] gainCharge = {false};

                // 扣充能：beforeAbilityUsed 会按 baseChargeUse 的返回值扣掉对应充能
                beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);

                // 攻击，攻击的耗时不在攻击方法本身中做处理
                hero.attack(enemy, 1.6f);

                final Char target = enemy;
                Actor.add(new Actor() {
                    {
                        actPriority = VFX_PRIO;
                    }

                    @Override
                    protected boolean act() {
                        if (!target.isAlive()) {
                            gainCharge[0] = true;
                        }
                        Actor.remove(this);
                        return true;
                    }
                });

                // 回复充能
                if (gainCharge[0]) Buff.affect( hero, MeleeWeapon.Charger.class ).gainCharge(1);
                // 武技后处理
                afterAbilityUsed(hero);
            }
        });
    }

    // 武技描述相关
    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc");
        } else {
            return Messages.get(this, "typical_ability_desc");
        }
    }
}