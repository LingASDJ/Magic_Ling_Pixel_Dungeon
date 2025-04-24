package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DARKNESS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class LanFireStats extends Buff implements Hero.Doom {

    @Override
    public String heroMessage() {
        return "";
    }

    private static final String LEVEL = "level";
    private static final String PARTIALDAMAGE = "partialDamage";

    private static final float STEP = 100.0f;

    private float level;
    private float partialDamage;

    @Override
    public boolean act() {
        if (Dungeon.depth <= 5) {
            spend(STEP);
            return true;
        } else if (Dungeon.level.locked || this.target.buff(LighS.class) != null) {
            spend(STEP);
            return true;
        } else {
            Hero hero = (Hero) this.target;
            if (!this.target.isAlive()) {
                diactivate();
                return true;
            }
            if (hero.lanterfire > 0 ) {
                hero.damageLantern(1+Challenges.activeChallenges()/3 + (Dungeon.isChallenged(DARKNESS) ? 2 : 0));
                spend((int) (59f-(float) (Dungeon.depth/5+Challenges.activeChallenges())));
            } else {
                spend(STEP);
            }
            return true;
        }
    }

    @Override
    public String desc() {
        String result;
        int damage = 1+Challenges.activeChallenges()/3 + (Dungeon.isChallenged(DARKNESS) ? 2 : 0);
        int lanter = (int) (55f-(float) (Dungeon.depth/5+Challenges.activeChallenges()));
        if (hero.lanterfire >= 90 && hero.lanterfire <= 100) {
            result =  Messages.get(Nyctophobia.class, "desc");
        } else if (hero.lanterfire >= 80 && hero.lanterfire <= 89) {
            result = Messages.get(Nyctophobia.class, "desc2");
        } else if (hero.lanterfire >= 60 && hero.lanterfire <= 79) {
            result = Messages.get(Nyctophobia.class, "desc3");
        } else if (hero.lanterfire >= 35 && hero.lanterfire <= 59) {
            result = Messages.get(Nyctophobia.class, "desc4");
        } else if (hero.lanterfire >= 1 && hero.lanterfire <= 34) {
            result = Messages.get(Nyctophobia.class, "desc5");
        } else {
            result = Messages.get(Nyctophobia.class, "desc6");
        }
        result += "\n\n" + Messages.get(Nyctophobia.class,"damage",damage)
                + Messages.get(Nyctophobia.class,"lanter",lanter) ;
        return result;
    }


    @Override
    public int icon() {
        if (hero.lanterfire >= 90 && hero.lanterfire <= 100) {
            return BuffIndicator.LANTERFIRE_ONE;
        } else if (hero.lanterfire >= 80 && hero.lanterfire <= 89) {
            return BuffIndicator.LANTERFIRE_TWO;
        } else if (hero.lanterfire >= 60 && hero.lanterfire <= 79) {
            return BuffIndicator.LANTERFIRE_THREE;
        } else if (hero.lanterfire >= 35 && hero.lanterfire <= 59) {
            return BuffIndicator.LANTERFIRE_FOUR;
        } else if (hero.lanterfire >= 1 && hero.lanterfire <= 34) {
            return BuffIndicator.LANTERFIRE_FIVE;
        } else {
            return BuffIndicator.LANTERFIRE_SIX;
        }
    }

    @Override
    public void onDeath() {
        GLog.n( Messages.get(Nyctophobia.class, "ondeath"));
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.level = bundle.getFloat(LEVEL);
        this.partialDamage = bundle.getFloat(PARTIALDAMAGE);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, this.level);
        bundle.put(PARTIALDAMAGE, this.partialDamage);
    }

    @Override
    public String name() {
        if (hero.lanterfire >= 90 && hero.lanterfire <= 100) {
            return Messages.get(Nyctophobia.class, "name");
        } else if (hero.lanterfire >= 80 && hero.lanterfire <= 89) {
            return Messages.get(Nyctophobia.class, "name2");
        } else if (hero.lanterfire >= 60 && hero.lanterfire <= 79) {
            return Messages.get(Nyctophobia.class, "name3");
        } else if (hero.lanterfire >= 35 && hero.lanterfire <= 59) {
            return Messages.get(Nyctophobia.class, "name4");
        } else if (hero.lanterfire >= 1 && hero.lanterfire <= 34) {
            return Messages.get(Nyctophobia.class, "name5");
        } else {
            return Messages.get(Nyctophobia.class, "name6");
        }
    }
}

