package com.shatteredpixel.shatteredpixeldungeon.items.armor;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class LamellarArmor extends Armor {

    {
        image = ItemSpriteSheet.ARMOR_LAMELLAR;
    }

    public LamellarArmor() {
        super( 6 );
    }

//    @Override
//    public boolean doEquip( Hero hero ) {
//
//        detach(hero.belongings.backpack);
//
//        if (hero.belongings.armor == null || hero.belongings.armor.doUnequip( hero, true, false )) {
//
//            hero.belongings.armor = this;
//
//            cursedKnown = true;
//            if (cursed) {
//                equipCursed( hero );
//                GLog.n( Messages.get(Armor.class, "equip_cursed") );
//            }
//
//            ((HeroSprite)hero.sprite).updateArmor();
//            activate(hero);
//            Talent.onItemEquipped(hero, this);
//            hero.spendAndNext( time2equip( hero ) );
//            return true;
//
//        } else {
//
//            collect( hero.belongings.backpack );
//            return false;
//
//        }
//    }

}