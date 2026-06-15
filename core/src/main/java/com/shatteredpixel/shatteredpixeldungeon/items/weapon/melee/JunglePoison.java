//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class JunglePoison extends MeleeWeapon {

    {
        image = ItemSpriteSheet.JUNGLE_SWORD;
        tier = 3;
        DLY = 0.5f;
    }

    @Override
    public int min(int lvl) {
        return 6 + lvl;
    }

    @Override
    public int max(int lvl) {
        return 12 + lvl * 2;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Buff.affect(defender, Poison.class).extend(2*level()+3);

        Poison enemypoison = defender.buff(Poison.class);
        if (enemypoison != null) {
            if(enemypoison.GetPoisonLevel() >= (float) defender.HT /3){
                Buff.affect( defender, Roots.class, enemypoison.GetPoisonLevel() );
                Buff.affect( defender, Blindness.class, enemypoison.GetPoisonLevel() );
            }
        }

        return super.proc(attacker, defender, damage);
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        return 4;
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        ArrayList<Char> targets = new ArrayList<>();
        hero.belongings.abilityWeapon = this;

        for (Char ch : Actor.chars()){
            if (ch.alignment == Char.Alignment.ENEMY
                    && !hero.isCharmedBy(ch)
                    && Dungeon.level.heroFOV[ch.pos]){
                targets.add(ch);
            }
        }
        hero.belongings.abilityWeapon = null;

        if (targets.isEmpty()) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        throwSound();
        hero.sprite.attack(hero.pos, new Callback() {
            @Override
            public void call() {
                beforeAbilityUsed(hero, null);

                int duration = level() * 2;
                int poisonStacks = 10+level()*5;

                // 对所有敌人施加控制+中毒
                for (Char ch : targets) {
                    if(ch.alignment == Char.Alignment.ENEMY){

                        hero.sprite.parent.add(new Chains(
                                hero.sprite.center(),
                                ch.sprite.destinationCenter(),
                                Effects.Type.GLASSCHAIN,
                                new Callback() {
                                    public void call() {
                                        Buff.affect(ch, Roots.class, duration);
                                        Buff.affect(ch, Weakness.class, duration);
                                        Buff.affect(ch, Poison.class).set(poisonStacks);
                                        hero.spendAndNext(hero.attackDelay());
                                    }
                                }));
                    }
                }

                Invisibility.dispel();
                afterAbilityUsed(hero);
            }
        });
    }

    @Override
    public String abilityInfo() {
        if (levelKnown){
            int dur = level()*2;
            int poison = 2 + level()/2;
            return Messages.get(this, "typical_ability_desc", dur, poison, dur);
        } else {
            return Messages.get(this, "ability_desc",0,2,0);
        }
    }

}
