package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.INFINITE_ACCURACY;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.KusumiMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

//晨星
//四阶，力量需求16
//初始4-20，成长1-5
//这把武器的攻击会轮流给予敌人2+0.3*等级（向上取整）回合的虚弱、易伤、晕眩、失明。
//寒光四射的单手钉头锤，也许是因此得名。
//武技：苍茫天星，消耗2充能，对目标单位施加晨星的所有效果，随后对目标造成140%必中伤害，并使目标身上的所有负面效果延长3回合。
// 目标身上的每一个负面效果都会使本次伤害额外+5%。
public class MorningStar extends MeleeWeapon {
    {
        image = ItemSpriteSheet.MORNING_STAR;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
    }

    @Override
    public int max(int lvl) { return 20 + lvl * 5; }

    @Override
    public int min(int lvl) { return 4 + lvl; }
    // buff回合数
    public int theDuration(){
        return (int) Math.ceil(2+0.3f*buffedLvl());
    }
    // 表示当前加哪个buff,不序列化
    private int nowBuff = 0;
    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        MorningStarBuffsAdder(defender, nowBuff);
        nowBuff++;
        return super.proc(attacker, defender, damage);
    }

    // 将晨星施加的buff提取为private方法
    private void MorningStarBuffsAdder(Char defender, int nowBuff) {
        switch(nowBuff%4){
            case 0: {
                // 虚弱
                Buff.affect(defender, Weakness.class, theDuration());
                break;
            }
            case 1:{
                // 易伤
                Buff.affect(defender, Vulnerable.class, theDuration());
                break;
            }
            case 2:{
                // 眩晕
                Buff.affect(defender, Vertigo.class, theDuration());
                break;
            }
            case 3:{
                // 失明
                Buff.affect(defender, Blindness.class, theDuration());
                break;
            }
        }
    }

    // ========== 武技：苍茫天星 ==========
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

                // 施加buff
                for(int i=0;i<4;i++)
                {
                    MorningStarBuffsAdder(enemy, i);
                }

                // 攻击与攻击倍率应用
                if (hero.attack(enemy, damageMultiplier(enemy), 0f, INFINITE_ACCURACY) && !enemy.isAlive()) {
                    // 武技击杀的天赋联动效果
                    onAbilityKill(hero, enemy);
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

    private float damageMultiplier(Char defender){
        float multiplier = 1.4f;
        for (Buff buff : defender.buffs()){
            if (buff.type != Buff.buffType.NEGATIVE) continue;
            multiplier += 0.05f;
            if (buff instanceof FlavourBuff){
                Buff.affect(defender, ((FlavourBuff) buff).getClass(), 3f);
            } else if (buff instanceof Poison){
                ((Poison) buff).extend(3f);
            } else if (buff instanceof Burning){
                ((Burning) buff).reignite(defender, 3f);
            }
        }
        return multiplier;
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
