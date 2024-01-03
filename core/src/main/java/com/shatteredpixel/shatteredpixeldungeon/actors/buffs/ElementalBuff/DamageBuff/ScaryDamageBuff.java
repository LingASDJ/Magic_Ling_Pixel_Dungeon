package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.DamageBuff;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalBaseBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.Immunities.ScaryImmunitiesBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ScaryDamageBuff extends ElementalBaseBuff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    public static void beckonEnemies(){
        if (hero.buff(ScaryDamageBuff.class) != null){
            for (Mob m : Dungeon.level.mobs){
                if (m.alignment == Char.Alignment.ENEMY && m.distance(hero) > 8) {
                    m.beckon(hero.pos);
                }
            }
        }
    }

    {
        immunities.add( ScaryBuff.class );
    }

    public static final float DURATION	= 50f;
    private float damageInc = 0;
    @Override
    public boolean act() {
        if (target.isAlive()) {


            if (--level <= 0) {
                detach();
                Buff.affect(target, ScaryImmunitiesBuff.class, ScaryImmunitiesBuff.DURATION);
            }

            damageInc = Random.Int(1,5);
            target.damage((int)damageInc, this);
            damageInc -= (int)damageInc;
            beckonEnemies();
            spend(interval);

            if (target == hero && !target.isAlive()){
                GLog.n(Messages.get(this, "on_kill"));
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

}

