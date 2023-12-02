package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DragonGirlBlue;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AnomaloCarisSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class AnomaloCaris extends Mob {

    {
        HP = HT = 30;
        spriteClass = AnomaloCarisSprite.class;
        properties.add(Property.INORGANIC);
        EXP = 0;
        maxLvl = 32;
        properties.add(Property.LARGE);
    }

    @Override
    public String description() {
        String desc = super.description();
        if(first){
            desc += "\n\n_" + Messages.get(AnomaloCaris.class, "true");
        }

        return desc;
    }

    private boolean first=true;

    private static final String FIRST = "first";

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
        boolean result = super.attack( enemy, dmgMulti, dmgBonus, accMulti );
        if (first) {
            first=false;
        }
        return result;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    @Override
    public int damageRoll() {
        int dmg = Random.NormalIntRange( 14, 21 );
        if(first){
            dmg = dmg * 3;
        }
        return dmg;
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        DragonGirlBlue.Quest.survey_research_points += Math.min(1100, 110);
        Badges.validateAncityProgress();
    }


}
