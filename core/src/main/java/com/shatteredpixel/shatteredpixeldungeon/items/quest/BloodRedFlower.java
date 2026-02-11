package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class BloodRedFlower extends Item {
    private static final String AC_ACTIVE = "active";
    private static final String AC_BLOOD = "blood";

    public int Charge;

    {
        image = ItemSpriteSheet.FLOWERS;
        cursed = false;
        defaultAction = AC_BLOOD;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_ACTIVE);
        actions.add(AC_BLOOD);
        return actions;
    }


    @Override
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );
        if(action.equals(AC_ACTIVE)){
            hero.HP += Math.min(hero.HT-hero.HP,Charge);
            GLog.p(Messages.get(BloodRedFlower.class,"heal"));
            hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 5 );
            Charge -= Charge;
            curUser.spend( Actor.TICK );
            curUser.busy();
            curUser.sprite.operate( curUser.pos );
        } else if(action.equals(AC_BLOOD)){
            if(hero.HP == 1) {
                GLog.n(Messages.get(BloodRedFlower.class,"blood_not"));
            } else if(Charge < hero.HT/2){
                int count = hero.HP/2;
                Charge += Math.min(count,hero.HT/2);
                hero.damage(count,this, Char.DamageType.REAL);
                GLog.n(Messages.get(BloodRedFlower.class,"blood"));
                curUser.spend( Actor.TICK );
                curUser.busy();
                curUser.sprite.operate( curUser.pos );
            } else {
                GLog.n(Messages.get(BloodRedFlower.class,"blood_no"));
            }
        }
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc",Charge, Dungeon.hero.HT/2);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(Window.GDX_COLOR, 3f);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private static final String CHARGE = "charge";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put(CHARGE, Charge );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        Charge	= bundle.getInt(CHARGE);
    }

}
