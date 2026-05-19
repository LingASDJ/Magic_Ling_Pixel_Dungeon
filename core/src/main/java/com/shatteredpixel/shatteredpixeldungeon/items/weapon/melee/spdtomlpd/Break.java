package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RoundShield;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Break extends MeleeWeapon {

    {
        image = ItemSpriteSheet.GREATSHIELD;
        tier = 5;
    }

    @Override
    public int min(int lvl) {
        return 6 + lvl;
    }

    @Override
    public int max(int lvl) {
        return 28 + lvl * 6;
    }


    @Override
    public int damageRoll(Char owner) {
        int dmg = super.damageRoll(owner);
        if (owner instanceof Hero) {
            Hero hero = (Hero)owner;
            Char enemy = hero.enemy();
            if (enemy != null && enemy.HP <= enemy.HT * 0.5f) {
                float damageMultiplier = 1.0f + (0.1f * level());
                int damage;
                damage = Math.round(dmg * damageMultiplier);

                hero.sprite.showStatus(0xFF0000, "+%d%%", Math.round(damageMultiplier * 100));

                return damage;
            }
        }
        return super.damageRoll(owner);
    }


    public String statsInfo(){
        float kdamageMultiplier = 100 + (10f * level());
        float sdamageMultiplier = 30f + (3f * level());
        if (isIdentified()){
            return Messages.get(this, "stats_desc", sdamageMultiplier,kdamageMultiplier);
        } else {
            return Messages.get(this, "typical_stats_desc", 30,100);
        }
    }


    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        RoundShield.guardAbility(hero, 10, this);
    }

    @Override
    public String upgradeAbilityStat(int level) {
        return Integer.toString(3 + level);
    }

}
