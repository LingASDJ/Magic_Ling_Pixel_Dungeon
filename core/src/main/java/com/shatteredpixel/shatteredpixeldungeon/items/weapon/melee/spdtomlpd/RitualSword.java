package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RitualSword extends MeleeWeapon {

    {
        image = ItemSpriteSheet.RITUAL_SWORD_M;
        tier = 2;
        if (!hasGoodEnchant()){
            enchantment = null;
            cursed = false;
        };
    }

    @Override
    public int image() {
        if (!hasGoodEnchant()){
            enchantment = null;
            cursed = false;
        };
        return ItemSpriteSheet.RITUAL_SWORD_M;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {

        return super.proc( attacker, defender, damage );
    }

    @Override
    public int min(int lvl) {
        return 3 + lvl;
    }

    @Override
    public int max(int lvl) {
        return  15 + lvl*3;
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        if (hero.buff(TragicCode.CleaveTracker.class) != null){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(4+lvl) damage, roughly +50% base dmg, +50% scaling
        int dmgBoost = augment.damageFactor(4 + buffedLvl());
        TragicCode.cleaveAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 4 + buffedLvl() : 4;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }

    public String upgradeAbilityStat(int level){
        int dmgBoost = 4 + level;
        return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
    }

}
