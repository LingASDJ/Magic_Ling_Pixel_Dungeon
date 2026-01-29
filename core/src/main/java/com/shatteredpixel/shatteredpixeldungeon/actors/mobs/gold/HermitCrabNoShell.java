package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

public class HermitCrabNoShell extends Crab {
    @Override
    public String info(){
        String desc = super.description();

        desc += "\n\n" + Messages.get(GoldMob.class,"infos");

        return desc;
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        GoldMob.GetSearch();
    }

    {
        spriteClass = HermitCrabNoShellSprite.class;

        HT = 15;
        HP = 10;
        defenseSkill = 15;

        state = FLEEING;

        EXP = 4;
        maxLvl = 10;

        loot = new FrozenCarpaccio();
        lootChance = 0.127f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 2, 7 );
    }

    @Override
    public int drRoll() {
        return 0;
    }

}
