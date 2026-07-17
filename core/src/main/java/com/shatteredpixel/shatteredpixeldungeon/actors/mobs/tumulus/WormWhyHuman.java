package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WormWhyHumanSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class WormWhyHuman extends Mob {

    private int drRoll = 42;
    private int totalDr;

    {
        spriteClass = WormWhyHumanSprite.class;
        HP = HT = 40;
        defenseSkill = 0;

        EXP = 8;

        maxLvl = 17;
        properties.add(Property.TUMULUS);
        immunities.addAll(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic.RESISTS);
    }

    @Override
    public void move(int step) {
        super.move(step);
        if(Dungeon.level.map[pos] == Terrain.WATER || Dungeon.level.map[pos] == Terrain.SALT_WATER){
            if(drRoll > 0){
                int s = Random.NormalIntRange(1,7);
                drRoll -= s;
                totalDr += s;
            }
        }
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 9, 19 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 8;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, Math.max(drRoll,0));
    }

    @Override
    public String description() {
        return Messages.get(this,"desc",drRoll,totalDr);
    }

    private static final String DR = "drRoll";
    private static final String TR = "trRoll";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DR,drRoll);
        bundle.put(TR,totalDr);
    }


    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        drRoll = bundle.getInt(DR);
        totalDr = bundle.getInt(TR);
    }
}
