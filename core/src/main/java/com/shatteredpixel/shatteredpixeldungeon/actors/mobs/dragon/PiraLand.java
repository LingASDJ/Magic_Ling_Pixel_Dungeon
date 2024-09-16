package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.dragon;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PiraLandSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class PiraLand extends Mob {

    {
        spriteClass = PiraLandSprite.class;

        HP = HT = 35;
        defenseSkill = 15;

        EXP = 8;
        maxLvl = 21;

        state = SLEEPING;
        loot = DarkGold.class;
        lootChance = 0.6f;
    }

    public boolean supercharged = false;

    @Override
    public void move(int step) {
        super.move(step);
        supercharged = Dungeon.level.water[pos];
    }

    private static final String SUPERCHARGED = "supercharged";


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SUPERCHARGED, supercharged);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        supercharged = bundle.getBoolean(SUPERCHARGED);
    }

    @Override
    public float speed() {
        return super.speed() * (supercharged ? 1.1f : 1);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 12, 18 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 6);
    }
}
