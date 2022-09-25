package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GoldIron extends Artifact {

    {
        image = ItemSpriteSheet.Gold_Iron;

        levelCap = 5;

        charge = 5+level();
        partialCharge = 0;
        chargeCap = 5+level();
    }

    public static final String AC_STONETOGOLD = "stonegold";
    public static final String AC_ONEDINGYI = "onedingyi";
    public static final String AC_CONDONTXA = "condontxa";


    //keeps track of generated sandbags.
    public int sandBags = 0;

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && !cursed && (charge > 0 || activeBuff != null)) {
            actions.add(AC_STONETOGOLD);
            actions.add(AC_ONEDINGYI);
            actions.add(AC_CONDONTXA);
        }
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute(hero, action);

        if (action.equals(AC_STONETOGOLD)){

        }  else if (action.equals(AC_ONEDINGYI)) {

        } else if (action.equals(AC_CONDONTXA)) {

        }
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        if (activeBuff != null)
            activeBuff.attachTo(ch);
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)){
            if (activeBuff != null){
                activeBuff.detach();
                activeBuff = null;
            }
            return true;
        } else
            return false;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new GoldIron.hourglassRecharge();
    }

    @Override
    public void charge(Hero target, float amount) {
        if (charge < chargeCap){
            partialCharge += 0.25f*amount;
            if (partialCharge >= 1){
                partialCharge--;
                charge++;
                updateQuickslot();
            }
        }
    }

    @Override
    public Item upgrade() {
        chargeCap+= 1;

        //for artifact transmutation.
        while (level()+1 > sandBags)
            sandBags ++;

        return super.upgrade();
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped( Dungeon.hero )){
            if (!cursed) {
                if (level() < levelCap )
                    desc += "\n\n" + Messages.get(this, "desc_hint");

            } else
                desc += "\n\n" + Messages.get(this, "desc_cursed");
        }
        return desc;
    }


    private static final String SANDBAGS =  "sandbags";
    private static final String BUFF =      "buff";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( SANDBAGS, sandBags );

        if (activeBuff != null)
            bundle.put( BUFF , activeBuff );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        sandBags = bundle.getInt( SANDBAGS );

        //these buffs belong to hourglass, need to handle unbundling within the hourglass class.
        if (bundle.contains( BUFF )){
            Bundle buffBundle = bundle.getBundle( BUFF );

            if (buffBundle.contains( GoldIron.timeFreeze.PRESSES ))
                activeBuff = new GoldIron.timeFreeze();
            else
                activeBuff = new GoldIron.timeStasis();

            activeBuff.restoreFromBundle(buffBundle);
        }
    }

    public class hourglassRecharge extends ArtifactBuff {
        @Override
        public boolean act() {

            LockedFloor lock = target.buff(LockedFloor.class);
            if (charge < chargeCap && !cursed && (lock == null || lock.regenOn())) {
                //90 turns to charge at full, 60 turns to charge at 0/10
                float chargeGain = 1 / (90f - (chargeCap - charge)*3f);
                chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
                partialCharge += chargeGain;

                if (partialCharge >= 1) {
                    partialCharge --;
                    charge ++;

                    if (charge == chargeCap){
                        partialCharge = 0;
                    }
                }
            } else if (cursed && Random.Int(10) == 0)
                ((Hero) target).spend( TICK );

            updateQuickslot();

            spend( TICK );

            return true;
        }
    }

    public class timeStasis extends ArtifactBuff {

        {
            type = buffType.POSITIVE;
            actPriority = BUFF_PRIO-3; //acts after all other buffs, so they are prevented
        }

        @Override
        public boolean attachTo(Char target) {

            if (super.attachTo(target)) {

                Invisibility.dispel();

                int usedCharge = Math.min(charge, 2);
                //buffs always act last, so the stasis buff should end a turn early.
                spend(5*usedCharge);

                //shouldn't punish the player for going into stasis frequently
                Hunger hunger = Buff.affect(target, Hunger.class);
                if (hunger != null && !hunger.isStarving()) {
                    hunger.satisfy(5 * usedCharge);
                }

                charge -= usedCharge;

                target.invisible++;
                target.paralysed++;
                target.next();

                updateQuickslot();

                if (Dungeon.hero != null) {
                    Dungeon.observe();
                }

                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean act() {
            detach();
            return true;
        }

        @Override
        public void detach() {
            if (target.invisible > 0) target.invisible--;
            if (target.paralysed > 0) target.paralysed--;
            super.detach();
            activeBuff = null;
            Dungeon.observe();
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add( CharSprite.State.INVISIBLE );
            else if (target.invisible == 0) target.sprite.remove( CharSprite.State.INVISIBLE );
        }
    }

    public class timeFreeze extends ArtifactBuff {

        {
            type = buffType.POSITIVE;
        }

        float turnsToCost = 0f;

        ArrayList<Integer> presses = new ArrayList<>();

        public void processTime(float time){
            turnsToCost -= time;

            //use 1/1,000 to account for rounding errors
            while (turnsToCost < -0.001f){
                turnsToCost += 2f;
                charge --;
            }

            updateQuickslot();

            if (charge < 0){
                charge = 0;
                detach();
            }

        }

        public void setDelayedPress(int cell){
            if (!presses.contains(cell))
                presses.add(cell);
        }

        public void triggerPresses(){
            for (int cell : presses)
                Dungeon.level.pressCell(cell);

            presses = new ArrayList<>();
        }

        public void disarmPressedTraps(){
            for (int cell : presses){
                Trap t = Dungeon.level.traps.get(cell);
                if (t != null && t.disarmedByActivation) t.disarm();
            }

            presses = new ArrayList<>();
        }

        @Override
        public void detach(){
            updateQuickslot();
            super.detach();
            activeBuff = null;
            triggerPresses();
            target.next();
        }

        @Override
        public void fx(boolean on) {
            Emitter.freezeEmitters = on;
            if (on){
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob.sprite != null) mob.sprite.add(CharSprite.State.PARALYSED);
                }
            } else {
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob.paralysed <= 0) mob.sprite.remove(CharSprite.State.PARALYSED);
                }
            }
        }

        private static final String PRESSES = "presses";
        private static final String TURNSTOCOST = "turnsToCost";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);

            int[] values = new int[presses.size()];
            for (int i = 0; i < values.length; i ++)
                values[i] = presses.get(i);
            bundle.put( PRESSES , values );

            bundle.put( TURNSTOCOST , turnsToCost);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);

            int[] values = bundle.getIntArray( PRESSES );
            for (int value : values)
                presses.add(value);

            turnsToCost = bundle.getFloat( TURNSTOCOST );
        }
    }

    public static class sandBag extends Item {

        {
            image = ItemSpriteSheet.SANDBAG;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            GoldIron hourglass = hero.belongings.getItem( GoldIron.class );
            if (hourglass != null && !hourglass.cursed) {
                hourglass.upgrade();
                Sample.INSTANCE.play( Assets.Sounds.DEWDROP );
                if (hourglass.level() == hourglass.levelCap)
                    GLog.p( Messages.get(this, "maxlevel") );
                else
                    GLog.i( Messages.get(this, "levelup") );
                GameScene.pickUp(this, pos);
                hero.spendAndNext(TIME_TO_PICK_UP);
                return true;
            } else {
                GLog.w( Messages.get(this, "no_hourglass") );
                return false;
            }
        }

        @Override
        public int value() {
            return 20;
        }

        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }
    }


}

