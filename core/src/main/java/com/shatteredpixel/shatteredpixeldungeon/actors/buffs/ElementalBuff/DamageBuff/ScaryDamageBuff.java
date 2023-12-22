package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.DamageBuff;

import static com.watabou.utils.Random.NormalFloat;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalFABuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.Immunities.ScaryImmunitiesBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class ScaryDamageBuff extends ElementalFABuff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    {
        immunities.add( ScaryBuff.class );
    }

    public static final float DURATION	= 20f;
    private float damageInc = 0;
    @Override
    public boolean act() {
        if (target.isAlive()) {

            damageInc = NormalFloat(damageInc / 2f, damageInc);
            int dmg = Math.round(damageInc);

            if (dmg > 0) {

                target.damage( dmg, this );
                if (target.sprite.visible) {
                    Splash.at( target.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
                            target.sprite.blood(), Math.min( 10 * dmg / target.HT, 10 ) );
                }

                if (target == Dungeon.hero && !target.isAlive()) {
                    Dungeon.fail( getClass() );
                    GLog.n( Messages.get(this, "ondeath") );
                }

                spend( TICK );
            } else {
                detach();
            }

        } else {

            detach();

        }
        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.SCARY;
    }

    @Override
    public void detach() {
        super.detach();
        Buff.affect(target, ScaryImmunitiesBuff.class, Random.Int(100,201));
    }

    public static final String STACKS = "enemy_stacks";
    public static final String DAMAGE = "damage_inc";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DAMAGE, damageInc);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        damageInc = bundle.getFloat(DAMAGE);
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 0f, 0f);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

}

