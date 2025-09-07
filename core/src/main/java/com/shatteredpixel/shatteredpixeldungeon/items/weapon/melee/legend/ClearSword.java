package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class ClearSword extends MeleeWeapon implements Item.LengedsItem {
    {
        image = ItemSpriteSheet.CLEARPRO;
        tier = 5;
        ACC = 0.9f;
        DLY = 1.20F;
    }

    public int min(int level) {
        return 5 + level;
    }

    @Override
    public int iceCoinValue() {
        if (Badges.isUnlocked(Badges.Badge.NYZ_SHOP)){
            return (int) (850 * 0.9f);
        }
        return 850;
    }

    public int max(int level) {
        return 40 + level * 7;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        int level = buffedLvl();

        if (Random.Int(100) < (15 + 2 * level)) {
            Buff.affect(defender, Bleeding.class).set(damage * 0.1f);
        }

        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            float lifePercentage = hero.HP / (float) hero.HT;
            int dmmageMuilt = (int) (damage * (1 - lifePercentage) * 0.33f);
            damage += dmmageMuilt;
            if (!defender.properties().contains(Char.Property.BOSS)) {
                if(damage > defender.HP){
                    int healAmount = (int) (defender.HT * (2 + 0.5 * level()) / 100);
                    if(healAmount <= 0){
                        healAmount = 3;
                        attacker.sprite.showStatus(Window.Pink_COLOR, "+" + healAmount + " HP");
                    } else {
                        hero.HP = Math.min(hero.HT, hero.HP + healAmount);
                        attacker.sprite.showStatus(CharSprite.POSITIVE, "+" + healAmount + " HP");
                    }
                }
            }
        }



        return super.proc(attacker, defender, damage);
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 3;
    }
    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }
    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (target == null) {
            GLog.w(Messages.get(this, "no_target"));
            return;
        }

        Char enemy = Actor.findChar(target);
        if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        hero.belongings.abilityWeapon = this;
        if (!hero.canAttack(enemy)){
            GLog.w(Messages.get(this, "ability_bad_position"));
            hero.belongings.abilityWeapon = null;
            return;
        }
        hero.belongings.abilityWeapon = null;

        int magicDamage = (int) (Random.NormalIntRange(min(), max()) * Random.Float(0.75f, 1.5f));

        applyMagicEffect(hero, enemy, magicDamage);

        for (int n : PathFinder.CIRCLE7) {
            Char xenemy = Actor.findChar(target + n);

            if (xenemy != null &&
                    xenemy != hero &&
                    Dungeon.level.heroFOV[xenemy.pos] &&
                    xenemy.alignment == Char.Alignment.ENEMY &&
                    !hero.isCharmedBy(xenemy)) {

                applyMagicEffect(hero, xenemy, magicDamage);
            }
        }

        hero.spendAndNext(1f);
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC);
        updateQuickslot();
    }

    // 抽取出的公共方法，用于施放魔法效果
    private void applyMagicEffect(Hero hero, Char enemy, int magicDamage) {
        MagicMissile.boltFromChar(hero.sprite.parent,
                MagicMissile.SHADOW,
                hero.sprite,
                enemy.pos,
                new Callback() {
                    @Override
                    public void call() {
                        beforeAbilityUsed(hero, enemy);
                        enemy.damage(magicDamage, ClearSword.class);
                        Buff.prolong(enemy, Vulnerable.class, 20f);
                        Buff.prolong(enemy, Weakness.class, 20f);
                        hero.next();
                    }
                });
    }

}
