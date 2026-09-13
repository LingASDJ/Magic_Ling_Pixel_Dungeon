package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.KusumiMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.INFINITE_ACCURACY;

//钉入矛
//五阶，力量需求18
//初始4-30，成长1-5，攻击距离2
//若目标敌人的3*3范围内有墙地块，则攻击它造成的伤害提升50%。
//能够狠狠的把敌人钉在墙里。
//武技：刻骨连钉，消耗1充能，对+1攻击距离以内的1个目标造成一次必中伤害，随后再使用副手武器对其造成一次必中伤害。
// 副手武器造成伤害时也可以触发钉入矛的特效。
// 如果没有副手武器，则使用钉入矛连续攻击两次。
public class NailingSpear extends MeleeWeapon{
    {
        image = ItemSpriteSheet.DING;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        RCH  = 2;

        tier = 5;
    }

    @Override
    public int max(int lvl) { return 30 + lvl * 5; }

    @Override
    public int min(int lvl) { return 4 + lvl; }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 目标 3*3 范围内有墙地块时，伤害 +50% 四舍五入
        if (nearWall(defender)) {
            damage = Math.round(damage * 1.5f);
        }
        return super.proc(attacker, defender, damage);
    }

    // 目标 3*3 范围内是否存在墙地块
    private boolean nearWall(Char defender) {
        for (int i : PathFinder.NEIGHBOURS9) {
            int pos = defender.pos + i;
            if (Dungeon.level.insideMap(pos)) {
                int terrain = Dungeon.level.map[pos];
                if (terrain == Terrain.WALL || terrain == Terrain.WALL_DECO) {
                    return true;
                }
            }
        }
        return false;
    }

    // ========== 武技：刻骨连钉 ==========

    // 武技瞄准期间临时生效的额外攻击距离（不序列化）
    private transient int aimReachBonus = 0;

    // 修改武器的攻击距离，因为在后续中令5g的瞄准范围取决于武器的攻击距离是较为稳妥的
    @Override
    public int reachFactor(Char owner){
        return super.reachFactor(owner) + aimReachBonus;
    }

    // 每次使用武技消耗的充能点数（由决斗者的 Charger buff 提供）
    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 1;
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

        // 选址位置的目标若为空、为魅惑者、不在视野范围内、为我方单位、为NPC（以及NPC形态久住）、为无敌单位，则选取失败
        Char enemy = Actor.findChar(target);
        if (enemy == null
                || enemy == hero
                || hero.isCharmedBy(enemy)
                || !Dungeon.level.heroFOV[target]
                || enemy.alignment == hero.alignment
                || (enemy instanceof NPC || enemy instanceof KusumiMagicGirl)
                || enemy.isInvulnerable(getClass())
        ) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        hero.belongings.abilityWeapon = this;
        aimReachBonus = 1;
        boolean inRange = hero.canAttack(enemy);
        aimReachBonus = 0;
        if (!inRange){
            GLog.w(Messages.get(this, "ability_target_range"));
            hero.belongings.abilityWeapon = null;
            return;
        }
        hero.belongings.abilityWeapon = null;

        // 对选择的目标进行攻击
        hero.sprite.attack(enemy.pos, new Callback() {
            @Override public void call() {
                // 抠1充能，并设置 abilityWeapon
                beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);

                // 第一次攻击，来自钉入矛：攻击与攻击倍率应用
                hero.attack(enemy, 1f, 0f, INFINITE_ACCURACY);

                // 第二击：副手武器；没有副手就用这把矛再打一次
                if (enemy.isAlive()){
                    KindOfWeapon off = hero.belongings.secondWep();
                    if (off != null) {
                        hero.belongings.abilityWeapon = off;
                        float mult = (!(off instanceof NailingSpear) && nearWall(enemy)) ? 1.5f : 1f;
                        hero.attack(enemy, mult, 0f, INFINITE_ACCURACY);
                        hero.belongings.abilityWeapon = NailingSpear.this;
                    }
                    else hero.attack(enemy, 1f, 0f, INFINITE_ACCURACY);
                }

                // 武技击杀效果结算
                if (!enemy.isAlive()) onAbilityKill(hero, enemy);

                // 消耗回合数
                hero.spendAndNext(hero.attackDelay());

                // 令攻击者（使用武技者）破隐
                Invisibility.dispel();

                // 武技后处理
                afterAbilityUsed(hero);
            }
        });
    }
}
