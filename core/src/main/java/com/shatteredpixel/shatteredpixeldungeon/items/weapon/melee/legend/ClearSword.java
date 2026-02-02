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

import java.util.ArrayList;

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
        if (target == null || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        hero.belongings.abilityWeapon = this;
        Char xenemy = Actor.findChar(target);
        if (!hero.canAttack(xenemy)){
            GLog.w(Messages.get(this, "ability_bad_position"));
            hero.belongings.abilityWeapon = null;
            return;
        }

        try {
            int magicDamage = (int) (1.25f * Random.NormalIntRange(min(), max()));
            ArrayList<Char> targets = new ArrayList<>();

            int mapLength = Dungeon.level.length();

            for (int i : PathFinder.CIRCLE7) {
                int pos = target + i;
                if (pos >= 0 && pos < mapLength && Dungeon.level.heroFOV[pos]) {
                    Char enemy = Actor.findChar(pos);
                    if (enemy != null
                            && enemy != hero
                            && enemy.alignment == Char.Alignment.ENEMY
                            && !hero.isCharmedBy(enemy)) {
                        targets.add(enemy);
                    }
                }
            }

            if (targets.isEmpty()) {
                GLog.w(Messages.get(this, "ability_no_target"));
                return;
            }

            hero.sprite.zap(target);
            MagicMissile.boltFromChar(
                    hero.sprite.parent,
                    MagicMissile.BLOOD_CONE,
                    hero.sprite,
                    xenemy.pos,
                    new Callback() {
                        @Override
                        public void call() {
                            Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC);
                            for (Char enemy : targets) {
                                enemy.damage(magicDamage, Char.DamageType.MAGIC);
                                Buff.prolong(enemy, Vulnerable.class, 9f);
                                Buff.prolong(enemy, Weakness.class, 9f);
                                enemy.sprite.showStatus(CharSprite.NEGATIVE, Integer.toString(magicDamage));
                            }
                            updateQuickslot();
                            beforeAbilityUsed(hero, xenemy);
                        }
                    });

        } finally {
            hero.belongings.abilityWeapon = null;
        }
    }
}
