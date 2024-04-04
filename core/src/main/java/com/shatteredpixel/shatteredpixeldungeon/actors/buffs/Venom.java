package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Venom extends Buff implements Hero.Doom {

    private float damage = 2;
    protected float left;

    private static final String DAMAGE	= "damage";
    private static final String LEFT	= "left";

    {
        type = buffType.NEGATIVE;
        announced = true;
    }
    private static final String SOURCE	= "source";
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( DAMAGE, damage );
        bundle.put( LEFT, left );
        bundle.put( SOURCE, source);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        damage = bundle.getFloat( DAMAGE );
        left = bundle.getFloat( LEFT );
        source = bundle.getClass( SOURCE );
    }
    private Class source;
    public void set(float duration, int damage, Class source) {
        this.left = Math.max(duration, left);
        if (this.damage < damage) this.damage = damage;
        this.source = source;
    }

    @Override
    public int icon() {
        return BuffIndicator.POISON;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0.2f, 0.2f, 0.6f);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String heroMessage() {
        return Messages.get(this, "heromsg");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(left), (int)damage);
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
            target.damage((int)damage, new CrivusFruits.DiedBlobs());
            if (damage < (Dungeon.depth/4f)+1) {
                damage++;
            } else {
                damage += 1f;
            }

            if(damage>10){
                Buff.affect(target, Blindness.class, 5f);
            }

            if(damage>27){
                Buff.affect(target, Bleeding.class).set(4f);
            }

            spend( TICK );
            if ((left -= TICK) <= 0) {
                detach();
            }
        } else {
            detach();
        }

        return true;
    }

    @Override
    public void onDeath() {
        Dungeon.fail( getClass() );
        GLog.n(Messages.get(this, "ondeath"));
    }

}
