package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public class HermitCrab extends Crab {

    {
        spriteClass = HermitCrabSprite.class;

        HP = HT = 15;
        defenseSkill = 2;
        baseSpeed = 0.95f;

        EXP = 0;
        maxLvl = 10;

    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(1, 3);
    }

    @Override
    public String info(){
        String desc = super.description();

        desc += "\n\n" + Messages.get(GoldMob.class,"infos");

        return desc;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        HermitCrabNoShell hermitCrabNoShell = new HermitCrabNoShell();
        hermitCrabNoShell.pos = pos;
        Buff.affect(hermitCrabNoShell, Terror.class, 6);
        Buff.affect(hermitCrabNoShell, Haste.class, 6);
        GameScene.add(hermitCrabNoShell);
    }
}
