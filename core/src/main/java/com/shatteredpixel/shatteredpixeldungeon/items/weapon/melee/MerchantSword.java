package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MerchantSword extends MeleeWeapon{
    {
        image = ItemSpriteSheet.SHOP_SWORD;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 2;
    }
    @Override
    public int max(int lvl) {
        return  15 + lvl*3;
    }
    @Override
    public int min(int lvl) {
        return  2 + lvl;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc" , 10 + (buffedLvl() * 5));
    }


    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        beforeAbilityUsed(hero, null);
        //1 turn less as using the ability is instant
        Buff.prolong(hero, Scimitar.SwordDance.class, 4+buffedLvl());
        hero.sprite.operate(hero.pos);
        hero.next();
        afterAbilityUsed(hero);
    }

    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", 4+buffedLvl());
        } else {
            return Messages.get(this, "typical_ability_desc", 4);
        }
    }

    @Override
    public String upgradeAbilityStat(int level) {
        return Integer.toString(3+level);
    }

}
