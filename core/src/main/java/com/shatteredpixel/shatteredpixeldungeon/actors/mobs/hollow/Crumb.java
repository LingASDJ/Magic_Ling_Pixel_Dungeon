package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.IconFloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrumbSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Crumb extends Mob {

    {
        spriteClass = CrumbSprite.class;

        //baseSpeed = 1.5f;
        HP = HT = 50;

        defenseSkill = 14;

        //loot = Candy.class;

        lootChance = 0.5f;

        EXP = 20;

        maxLvl = 36;
        properties.add(Char.Property.HOLLOW);
        properties.add(Property.IMMOVABLE);

        WANDERING = new Wandering();
        FLEEING = new Fleeing();
    }

    public Item food;
    private static final String FOOD = "item";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( FOOD, food );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        food = (Item) bundle.get( FOOD );
    }

    @Override
    public float speed() {
        if (food != null) return (5*super.speed())/6;
        else return super.speed();
    }


    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );

        //45%概率偷吃 steal
        if (alignment == Alignment.ENEMY && food == null
                && enemy instanceof Hero && steal( (Hero)enemy )) {
            state = FLEEING;
        }

        if(enemy!=null && enemy == hero) {
            for (Buff buff : hero.buffs()) {
                if (buff instanceof Hunger) {
                    //额外饥饿 至少2点 至多5点 30%概率
                    if(Random.Float()<=0.3f){
                        ((Hunger) buff).damgeExtraHungry(Random.NormalIntRange(2,5));
                    }
                }
            }
        }

        return damage;
    }

    @Override
    public float attackDelay() {
        return super.attackDelay()*0.75f;
    }

    protected boolean steal( Hero hero ) {

        Item toSteal = hero.belongings.getItem(Food.class);

        if (toSteal != null && !toSteal.unique && Random.Float()<=0.45f) {

            GLog.w( Messages.get(Crumb.class, "stole_food", toSteal.name()) );
            if (!toSteal.stackable) {
                Dungeon.quickslot.convertToPlaceholder(toSteal);
            }
            Item.updateQuickslot();
            food = toSteal.detach( hero.belongings.backpack );
            if (food instanceof Honeypot){
                food = ((Honeypot)food).shatter(this, this.pos);
            } else if (food instanceof Honeypot.ShatteredPot) {
                ((Honeypot.ShatteredPot)food).pickupPot(this);
            }
            //Buff.affect(this, Haste.class,20f);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String description() {
        String desc = super.description();

        if (food != null) {
            desc += Messages.get(this, "carries", food.name() );
        }

        return desc;
    }

    @Override
    public int damageRoll() {
        return Char.combatRoll( 27, 32 );
    }

    @Override
    public void rollToDropLoot() {
        if (food != null) {
            Dungeon.level.drop( food, pos ).sprite.drop();
            //updates position
            if (food instanceof Honeypot.ShatteredPot) ((Honeypot.ShatteredPot)food).dropPot( this, pos );
            food = null;
        }
        int value = Random.NormalIntRange(-30,20);
        Buff.affect(hero, Hunger.class).satisfy(value);
        hero.sprite.showStatusWithIcon(CharSprite.NEGATIVE, String.valueOf(value), IconFloatingText.HUNGRY_EXTRA_HEAL);
        super.rollToDropLoot();
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }


    private class Wandering extends Mob.Wandering {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            super.act(enemyInFOV, justAlerted);

            //if an enemy is just noticed and the thief posses an item, run, don't fight.
            if (state == HUNTING && food != null){
                state = FLEEING;
            }

            return true;
        }
    }

    public class Fleeing extends Mob.Fleeing {
        @Override
        protected void escaped() {
            if (food != null
                    && !Dungeon.level.heroFOV[pos]
                    && Dungeon.level.distance(Dungeon.hero.pos, pos) >= 9) {

                int count = 32;
                int newPos;
                do {
                    newPos = Dungeon.level.randomRespawnCell( Crumb.this );
                    if (count-- <= 0) {
                        break;
                    }
                } while (newPos == -1 || Dungeon.level.heroFOV[newPos] || Dungeon.level.distance(newPos, pos) < (count/3));

                if (newPos != -1) {

                    pos = newPos;
                    sprite.place( pos );
                    sprite.visible = Dungeon.level.heroFOV[pos];
                    if (Dungeon.level.heroFOV[pos]) CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);

                }

                if (food != null) GLog.n( Messages.get(Crumb.class, "escapes", food.name()));
                food = null;
                state = WANDERING;

                destroy();
                sprite.killAndErase();

            } else {
                state = WANDERING;
            }
        }
    }



}
