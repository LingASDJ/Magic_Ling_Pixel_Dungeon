package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Mace;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ReedPipe extends MeleeWeapon {

    {
        image = ItemSpriteSheet.SHEPHERD_FLUTE;
        tier = 3;
        RCH = 10;
    }

    @Override
    public int min(int lvl) {
        return 2 + lvl;
    }

    @Override
    public int max(int lvl) {
        return 9 + lvl * 2;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        int distance = attacker.distance(defender);
        float damageMultiplier = Math.max(0.1f, 1.0f - (distance * 0.1f));
        int actualDamage = Math.round(damage * damageMultiplier);
        if (distance > 1) {
            attacker.sprite.showStatus(0xFF8800, "-%d%%", (distance-1) * 10);
        }
        defender.damage(actualDamage,new DM100.LightningBolt());
        return super.proc(attacker, defender, 0);
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(4+1.5*lvl) damage, roughly +55% base dmg, +60% scaling
        int dmgBoost = augment.damageFactor(5 + Math.round(1.5f*buffedLvl()));
        Mace.heavyBlowAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 5 + Math.round(1.5f*buffedLvl()) : 5;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }

    public String upgradeAbilityStat(int level){
        int dmgBoost = 5 + Math.round(1.5f*level);
        return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
    }

}
