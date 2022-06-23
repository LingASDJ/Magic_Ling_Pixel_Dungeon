package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collection;

public class DeadSoul extends Buff {

    private ArrayList<Item> items = new ArrayList<>();

    public void stick(Item projectile){
        for (Item LostBackpack : items){
            if (LostBackpack.isSimilar(projectile)){
                LostBackpack.merge(projectile);
                return;
            }
        }
        items.add(projectile);
    }

    @Override
    public void detach() {
        for (Item item : items)
            Dungeon.level.drop( item, target.pos).sprite.drop();
        super.detach();
    }

    private static final String ITEMS = "items";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put( ITEMS , items );
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        items = new ArrayList<>((Collection<MissileWeapon>) ((Collection<?>) bundle.getCollection(ITEMS)));
        super.restoreFromBundle( bundle );
    }

    @Override
    public int icon() {
        return BuffIndicator.ANKH;
    }

    @Override
    public void tintIcon(Image icon) {
            icon.hardlight(0x808080);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        String desc = Messages.get(this, "desc");
        for (Item i : items){
            desc += "\n" + i.toString();
        }
        return desc;
    }

}

