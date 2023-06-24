package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.MagicFireParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class MagicFire extends Blob {

    @Override
    protected void evolve() {

        int cell;

        for (int i = area.left; i < area.right; i++){
            for (int j = area.top; j < area.bottom; j++){
                cell = i + j* Dungeon.level.width();
                if (cur[cell] > 0) {
                    enchant(cell);
                }
            }
        }
    }

    public static void enchant( int pos ) {
        if(Dungeon.level.heaps.get(pos) != null){
            for(Item item : Dungeon.level.heaps.get(pos).items){
                if(item instanceof Armor){
                    ((Armor) item).inscribe(Armor.Glyph.random());
                }
                if(item instanceof Weapon){
                    ((Weapon) item).enchant(Weapon.Enchantment.random());
                }
                GLog.p(Messages.get(MagicFire.class, "good")+item.name()+"\n");
            }
        }
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.pour( MagicFireParticle.FACTORY, 0.03f );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}