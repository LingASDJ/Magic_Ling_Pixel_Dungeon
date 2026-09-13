package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.KusumiMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.INFINITE_ACCURACY;

//门板
//五阶，力量需求18
//初始4-20，成长1-4
//初始护甲2-3，成长1-3
//这把武器的攻击会额外附带你最大生命值（20+2*等级）%的真实伤害。
//沉重的武器，你似乎可以把全身的力量都倾注入一击之上。
//武技：门截裂夫，消耗2充能，处决剩余生命值低于你当前生命值的非boss单位。对boss单位释放时，进行一次必中的攻击，最终伤害*2且伤害类型变为真实。
public class DoorPlank extends MeleeWeapon{
    {
        image = ItemSpriteSheet.DOOR_PLATE;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 5;
    }

    @Override
    public int max(int lvl) { return 20 + lvl * 4; }

    @Override
    public int min(int lvl) { return 4 + lvl; }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        defender.damage(Math.round((0.2f + 0.02f * buffedLvl()) * attacker.HT), attacker, Char.DamageType.REAL);
        return super.proc(attacker, defender, damage);
    }

    // ========== 武技：门截裂夫 ==========
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

        // 选址位置的目标若为空、为魅惑者、不在视野范围内、为中立单位（规避未激活宝箱怪避免直接秒了吞物品）、为我方单位、为NPC（以及NPC形态久住）、为无敌目标，则选取失败
        Char enemy = Actor.findChar(target);
        if (enemy == null
                || enemy == hero
                || hero.isCharmedBy(enemy)
                || !Dungeon.level.heroFOV[target]
                || enemy.alignment == Char.Alignment.NEUTRAL
                || enemy.alignment == hero.alignment
                || (enemy instanceof NPC || enemy instanceof KusumiMagicGirl)
                || enemy.isInvulnerable(getClass())
        ) {
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
            @Override public void call() {
                // 抠2充能，并设置 abilityWeapon
                beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);

                // 攻击与攻击倍率应用
                if ((Char.hasProp(enemy, Char.Property.BOSS) || Char.hasProp(enemy, Char.Property.MINIBOSS))
                        && hero.attack(enemy, 2f, 0f, INFINITE_ACCURACY, Char.DamageType.REAL)) {
                    // 武技击杀的天赋联动效果
                    if(!enemy.isAlive()) onAbilityKill(hero, enemy);
                }
                else if(!(Char.hasProp(enemy, Char.Property.BOSS) || Char.hasProp(enemy, Char.Property.MINIBOSS))
                        && enemy.isAlive()
                        && enemy.HP < hero.HP){
                    // 处决分支（参考 Char.java:665-675）
                    enemy.HP = 0;
                    if (!enemy.isAlive()) {
                        enemy.die(hero);
                    } else {
                        enemy.damage(-1, hero, Char.DamageType.REAL);
                        DeathMark.processFearTheReaper(enemy);
                    }
                    if (enemy.sprite != null) {
                        enemy.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "executed"));
                    }
                    if (!enemy.isAlive()) onAbilityKill(hero, enemy);
                }

                // 消耗回合数
                hero.spendAndNext(hero.attackDelay());

                // 令攻击者（使用武技者）破隐
                Invisibility.dispel();

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
