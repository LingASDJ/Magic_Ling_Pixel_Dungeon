/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.Guidebook;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

//TODO static方法会导致存档相互影响，此Buff已经废弃！但留在这里作为教训!
public class PureSoul extends Buff implements Hero.Doom {

    private final float STEP	= 10000f;
    private int interval = 1;

    public void set( int value, int time ) {
        //decide whether to override, preferring high value + low interval
        if (Math.sqrt(interval)*level <= Math.sqrt(time)*value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    public final float HUNGRY	= 300f;
    public final float STARVING	= 450f;
    public static final float LANTERHERO	= 100f;
    private float level;
    public float partialDamage;

    private static final String LEVEL			= "level";
    private static final String PARTIALDAMAGE 	= "partialDamage";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( LEVEL, level );
        bundle.put( PARTIALDAMAGE, partialDamage );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        level = bundle.getFloat( LEVEL );
        partialDamage = bundle.getFloat(PARTIALDAMAGE);
    }

    @Override
    public boolean act() {

        if (Dungeon.level.locked
                || target.buff(WellFed.class) != null
                || target.buff(ScrollOfChallenge.ChallengeArena.class) != null|| Dungeon.depth == 0){
            spend(STEP);
            return true;
        }

        if (target.isAlive() && target instanceof Hero) {

            Hero hero = (Hero)target;

            if (isStarving()) {

                partialDamage += STEP * target.HT/1000f;

                if (partialDamage > 1){
                    target.damage( (int)partialDamage, this);
                    partialDamage -= (int)partialDamage;
                }

            } else {

                float newLevel = level + LANTERHERO;
                if (newLevel >= STARVING) {

                    GLog.n( Messages.get(this, "onstarving") );
                    hero.resting = false;
                    hero.damage( 1, this );

                    hero.interrupt();

                } else if (newLevel >= HUNGRY && level < HUNGRY) {

                    GLog.w( Messages.get(this, "onhungry") );

                    if (!Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_FOOD)){
                        GLog.p(Messages.get(Guidebook.class, "hint"));
                        GameScene.flashForDocument(Document.GUIDE_FOOD);
                    }

                }
                level = newLevel;

            }

            spend( target.buff( Shadows.class ) == null ? STEP : STEP * 1.5f );

        } else {

            diactivate();

        }

        return true;
    }

    public void satisfy( float energy ) {

        Artifact.ArtifactBuff buff = target.buff( HornOfPlenty.hornRecharge.class );
        if (buff != null && buff.isCursed()){
            energy *= 0.67f;
            GLog.n( Messages.get(this, "cursedhorn") );
        }

        affectHunger( energy, false );
    }

    public void affectHunger(float energy, boolean overrideLimits ) {

        if (energy < 0 && target.buff(WellFed.class) != null){
            target.buff(WellFed.class).left += energy;
            BuffIndicator.refreshHero();
            return;
        }

        level += 0;
        if (level < 0 && !overrideLimits) {
            level = 0;
        } else if (level > STARVING) {
            float excess = level - STARVING;
            level = STARVING;
            partialDamage += excess * (target.HT/1000f);
        }

        BuffIndicator.refreshHero();
    }

    public boolean isStarving() {
        return level >= STARVING;
    }

    public int hunger() {
        return (int)Math.ceil(level);
    }

    @Override
    public int icon() {
        if (level <= 10f) {
            return BuffIndicator.LIGHT;
        } else if (level < STARVING) {
            return BuffIndicator.HUNGER;
        } else {
            return BuffIndicator.STARVATION;
        }
    }

    @Override
    public String toString() {
            return Messages.get(this, "lantern");
    }

    @Override
    public String desc() {
        String result;
        result = Messages.get(this, "desc_intro_lanter", Math.abs(level-LANTERHERO), dispTurns(visualcooldown()));

        result += Messages.get(this, "desc");

        return result;
    }

    @Override
    public void onDeath() {
        Dungeon.fail( getClass() );
        GLog.n( Messages.get(this, "ondeath") );
    }
}
