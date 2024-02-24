package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DragonGirlBlue;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ThreeLeafBugSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ThreeLeafBug extends Mob {

    private int attackChange;
    private static final String ATTACKCHANGE = "attackchange";

    @Override
    public float speed() {
        return super.speed() * (attackChange >= 7 ? 3.5f : 1+(attackChange/5f));
    }


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ATTACKCHANGE, attackChange);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        attackChange = bundle.getInt(ATTACKCHANGE);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(6, 10);
    }

    {
       HP = HT = 40;
       spriteClass = ThreeLeafBugSprite.class;
       defenseSkill = 8;
        EXP = 0;
        maxLvl = 32;
       HUNTING = new Hunting();
    }

    @Override
    public String description() {
        String desc = super.description();

        desc += "\n\n_" + Messages.get(ThreeLeafBug.class, "true",attackChange,damageRoll());


        return desc;
    }

    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV;
            if (!enemyInFOV && attackChange>0) {
               attackChange--;
            }
            return super.act( enemyInFOV, justAlerted );
        }

    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
        boolean result = super.attack( enemy, dmgMulti, dmgBonus, accMulti );
        if (attackChange<7) {
            attackChange++;
        }
        return result;
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        DragonGirlBlue.Quest.survey_research_points += 110;
        Badges.validateAncityProgress();
    }

    @Override
    public int damageRoll() {
        int dmg = 18;
        if(attackChange!=0){
            //伤害最高为18
            //基础伤害 x (攻击次数/10 + 基础伤害/10)
            // 18 x (5/10 + 1.8)
            dmg = Math.round(dmg * (attackChange/10f + dmg/10f));
        }
        return dmg;
    }

}
