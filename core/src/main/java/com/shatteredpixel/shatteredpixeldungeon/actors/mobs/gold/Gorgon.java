package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Gorgon extends GoldMob {
    {
        spriteClass = GorgonSprite.class;

        HP = HT = 120;
        defenseSkill = 22;


        EXP = 13;
        maxLvl = 27;

        properties.add(Property.DEMONIC);

        loot = new PotionOfCleansing();
        lootChance = 0.25f;
    }
    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        Buff.affect(enemy, Petrification.class).set( Petrification.DURATION );
        return damage;
    }


    public int damageRoll() {
        return Random.NormalIntRange( 55, 95 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 30;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(1, 8);
    }

    @Override
    public void die( Object cause ) {
        if(Random.Int(0,100)<=25) Dungeon.level.drop(new Gold().quantity(Random.Int(400,600)),pos);
        super.die( cause );
    }

    @Override
    public float attackDelay() {
        return super.attackDelay() * 0.75f;
    }
    @Override
    protected boolean act() {
        return super.act();
    }


    public static class Petrification extends Buff{
        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        private static final String PETRIFICATION_LV = "Petrification_lv";

        public float slowRate(){
            return 1f-(0.01f*Lv);
        }

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( PETRIFICATION_LV, Lv );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            Lv = bundle.getInt(PETRIFICATION_LV);
        }

        @Override
        public int icon() {
            return BuffIndicator.TERROR;
        }

        public static final int DURATION = 2;
        private int Lv;

        @Override
        public String iconTextDisplay() {
            return Lv +"/100";
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", this.Lv);
        }

        public void set(int Lv){
            if(this.Lv > 0)
                this.Lv += Lv;
            else{
                this.Lv = Lv;
            }
        }

        @Override
        public boolean act() {

            if (Lv >= 100){
                target.die(null);
                Dungeon.fail( getClass() );
                GLog.n( Messages.capitalize(Messages.get(Gorgon.Petrification.class, "ondeath")) );
            }

            spend( TICK );

            return true;
        }
    }
}
