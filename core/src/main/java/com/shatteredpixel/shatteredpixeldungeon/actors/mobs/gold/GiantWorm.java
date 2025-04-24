package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Random;

public class GiantWorm extends GoldMob {

    {
        spriteClass = GiantWormSprite.class;

        HP = HT = 12;
        defenseSkill = 1;

        EXP = 1;
        maxLvl = 5;
    }



    public int damageRoll() {
        return Random.NormalIntRange( 1, 5 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 8;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {

        damage = super.attackProc( enemy, damage );

        int healthSteal = Random.Int(1,3);
        HP += healthSteal;
        if(HP>HT) Buff.affect(this,Barrier.class).setShield(damage);
        HP = Math.min(HP,HT);
        this.sprite.emitter().burst(Speck.factory(Speck.HEALING), healthSteal);
        this.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", healthSteal);
        return damage;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
    }
}
