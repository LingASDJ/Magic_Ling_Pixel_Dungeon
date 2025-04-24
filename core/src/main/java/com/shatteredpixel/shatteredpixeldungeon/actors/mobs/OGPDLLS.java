//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.GoldMob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.OGPDLLSTT;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class OGPDLLS extends GoldMob {
    public OGPDLLS() {
        this.spriteClass = OGPDLLSTT.class;
        this.HT = 12;
        this.HP = 12;
        this.maxLvl = 15;
        this.EXP = 6;
    }

    public int attackProc(Char var1, int var2) {
        return super.attackProc(var1, var2) - 1;
    }

    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos] && hero.armorAbility instanceof Ratmogrify){
            alignment = Alignment.ALLY;
            if (state == SLEEPING) state = WANDERING;
        }
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 5, 8 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 18;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 1);
    }

    private static final String RAT_ALLY = "rat_ally";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        if (alignment == Alignment.ALLY) bundle.put(RAT_ALLY, true);
    }


    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(RAT_ALLY)) alignment = Alignment.ALLY;
    }
}
