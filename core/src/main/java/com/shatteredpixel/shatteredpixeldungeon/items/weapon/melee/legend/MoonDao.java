package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class MoonDao extends MeleeWeapon {
    public MoonDao() {
        image = ItemSpriteSheet.MOONDAILY;
        tier = 3;
        ACC = 1.54F;
        DLY = 0.3F;
    }

    public String statsInfo(){
    return (Messages.get(this,"stats_info"));
}
    @Override
    public int proc(Char attacker, Char defender, int damage) {

        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                Buff.affect(defender, Chill.class, 2+level()/4f);
            }
        }
        damage= (new Blocking()).proc(this, attacker, defender, damage);

        return super.proc(attacker, defender, damage);
    }


    public int min(int level) {
        return 3 + level;
    }

    @Override
    public int iceCoinValue() {
        if (Badges.isUnlocked(Badges.Badge.NYZ_SHOP)){
            return (int) ((175 + tier*25) * 0.9f);
        }
        return 175 + tier*25;
    }

    public int max(int level) {
        return 10 + level * 3;
    }
    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }
    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (hero.HP / (float)hero.HT >= 0.8f){
            GLog.w(Messages.get(this, "ability_cant_use"));
            return;
        }

        if (target == null) {
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

        hero.sprite.attack(enemy.pos, new Callback() {
            @Override
            public void call() {
                beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);
                if (hero.attack(enemy, 1.40f, 0, Char.INFINITE_ACCURACY)){
                    Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH);
                    if (!enemy.isAlive()){
                        onAbilityKill(hero, enemy);
                    }
                }

                Buff.affect(hero, Barrier.class).setShield(10);

                Invisibility.dispel();
                hero.spendAndNext(hero.attackDelay());
                afterAbilityUsed(hero);
            }
        });
    }

}
