package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

public class GnollTwilight extends Gnoll {

    @Override
    public String info(){
        String desc = super.description();

        desc += "\n\n" + Messages.get(GoldMob.class,"infos");

        return desc;
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        if(Statistics.RandomQuest == 2){
            if(Statistics.GoldMobDead>=15){
                Statistics.goldRefogreCount++;
                Statistics.GoldMobDead = 0;
            }

        }
    }

    public int fleeingTime;
    {
        spriteClass = GnollTwilightSprite.class;
        fleeingTime = 0;

        HP = HT = 40;
        defenseSkill = 5;
        baseSpeed = 1f;
        viewDistance = 8;

        EXP = 5;

        maxLvl = 15;

        state = WANDERING;

        //at half quantity, see createLoot()
        loot = Generator.Category.MISSILE;
        lootChance = 1f;

        properties.add(Property.MINIBOSS);
    }

    @Override
    public int attackSkill( Char target ) {
        return 16;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return (super.canAttack(enemy) || new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        int effect = Random.Int(4);
        if ((effect == 1) && (enemy.buff(Roots.class) == null)){
            Buff.affect( enemy, Roots.class, 5);}
        if ((effect == 2) && (enemy.buff(Blindness.class) == null)){Buff.affect( enemy, Blindness.class, 5);}
        if ((effect == 3) && (enemy.buff(Levitation.class) == null)){Buff.affect( enemy, Levitation.class, 5);}
        if ((effect == 0) && (enemy.buff(Weakness.class) == null)){Buff.affect( enemy, Weakness.class, 5);}
        Buff.affect(this, Terror.class, 30); Buff.affect(this, Haste.class, 30);
        return damage;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public Item createLoot() {
        MissileWeapon drop = (MissileWeapon)super.createLoot();
        //half quantity, rounded up
        drop.quantity((drop.quantity()+1)/2);
        return drop;
    }

}
