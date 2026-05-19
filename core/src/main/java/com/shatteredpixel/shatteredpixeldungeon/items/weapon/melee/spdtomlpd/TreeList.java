package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class TreeList extends MeleeWeapon {

    private static final String AC_BARRIER = "BARRIER";

    {
        image = ItemSpriteSheet.TREELIST;
        tier = 2;
    }

    @Override
    public int STRReq(int lvl) {
        return super.STRReq(lvl) + 1;
    }


    public String defaultAction(){
        if(Dungeon.hero.buff(TreeBarrier.class)!=null ){
            TreeBarrier tb = Dungeon.hero.buff(TreeBarrier.class);
            if(tb.accumulatedShield != 0){
                defaultAction = AC_BARRIER;
            } else {
                defaultAction = null;
            }
        } else {
            defaultAction = null;
        }
        return defaultAction;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );

        if(Dungeon.hero.buff(TreeBarrier.class)!=null ){
            actions.add(AC_BARRIER);
        }

        return actions;
    }


    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals(AC_BARRIER)) {
                releaseShield(hero);
                curUser.sprite.operate( curUser.pos );
                curUser.spend( Actor.TICK );
                curUser.busy();
                Sample.INSTANCE.play( Assets.Sounds.READ );
                Emitter e = curUser.sprite.centerEmitter();
                e.pos(e.x-2, e.y-6, 4, 4);
                e.start(Speck.factory(Speck.BUBBLE), 0.05f, 20);
        }
    }

    @Override
    public boolean doEquip(Hero hero) {
        Buff.affect(hero, TreeBarrier.class);
        return super.doEquip(hero);
    }

    public String desc() {
        return Messages.get(this, "desc", 20+2*level(),2 + level()/5);
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single){
        Buff.detach(hero, TreeBarrier.class);
        return super.doUnequip(hero, collect, single);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if(attacker instanceof Hero){
            attacker.buff(TreeBarrier.class).accumulateOnAttack(this);
        }
        return super.proc(attacker, defender, damage);
    }

    public static class TreeBarrst extends ShieldBuff {

        {
            type = buffType.NEUTRAL;
            announced = true;
        }

        @Override
        public boolean act() {

            absorbDamage(1);

            spend( TICK );

            return true;
        }

        @Override
        public void fx(boolean on) {
            if (on) {
                target.sprite.add(CharSprite.State.GREENSHIELDED);
            } else if (target.buff(TreeBarrst.class) == null) {
                target.sprite.remove(CharSprite.State.GREENSHIELDED);
            }
        }

        @Override
        public int icon () {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.SKYBULE_COLOR);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(shielding());
        }


        @Override
        public String toString () {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", shielding());
        }
    }

    public static class TreeBarrier extends Buff {

        {
            type = buffType.POSITIVE;
        }

        public int accumulatedShield = 0;
        public float partialLostShield = 0f;
        public int maxShield;

        @Override
        public boolean act() {
            if (target instanceof Hero) {
                Item weapon = ((Hero) target).belongings.weapon();
                if (weapon != null) {
                    maxShield = 20 + 2 * weapon.level();
                } else {
                    maxShield = 20;
                }
            }

            spend(TICK);
            return true;
        }

        // 攻击时积攒护盾
        public void accumulateOnAttack(Weapon weaponLevel) {
            int gained = 2 + weaponLevel.level()/5;
            accumulatedShield = Math.min(accumulatedShield + gained, maxShield);
        }

        public int getAccumulatedShield() {
            return accumulatedShield;
        }

        @Override
        public int icon() {
            return BuffIndicator.TERR_LIST;
        }


        @Override
        public String iconTextDisplay() {
            return Integer.toString(accumulatedShield);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", accumulatedShield, maxShield);
        }

        private static final String ACCUMULATED_SHIELD = "accumulated_shield";
        private static final String MAX_SHIELD = "max_shield";
        private static final String PARTIAL_LOST = "partial_lost";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(ACCUMULATED_SHIELD, accumulatedShield);
            bundle.put(MAX_SHIELD, maxShield);
            bundle.put(PARTIAL_LOST, partialLostShield);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            accumulatedShield = bundle.getInt(ACCUMULATED_SHIELD);
            maxShield = bundle.getInt(MAX_SHIELD);
            partialLostShield = bundle.getFloat(PARTIAL_LOST);
        }
    }

    @Override
    public int min(int lvl) {
        return 3 + lvl;
    }

    @Override
    public int max(int lvl) {
        return 14 + lvl;
    }

    public void releaseShield(Char hero) {
        TreeBarrier barrier = hero.buff(TreeBarrier.class);
        if (barrier != null) {
            Buff.affect(hero, TreeBarrst.class).setShield( barrier.getAccumulatedShield());
            barrier.accumulatedShield = 0;
        }
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        beforeAbilityUsed(hero, null);
        //1 turn less as using the ability is instant
        Buff.prolong(hero, DefensiveStance.class, 3 + buffedLvl());
        hero.sprite.operate(hero.pos);
        hero.next();
        afterAbilityUsed(hero);
    }

    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", 4+buffedLvl());
        } else {
            return Messages.get(this, "typical_ability_desc", 4);
        }
    }

    @Override
    public String upgradeAbilityStat(int level) {
        return Integer.toString(4+level);
    }

    public String statsInfo(){
        if (isIdentified()){
            return Messages.get(this, "stats_desc", 3+buffedLvl());
        } else {
            return Messages.get(this, "typical_stats_desc", 3);
        }
    }

    @Override
    public int defenseFactor( Char owner ) {
        return DRMax();
    }

    public int DRMax(){
        return DRMax(buffedLvl());
    }

    public int DRMax(int lvl){
        return 3 + lvl;
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 2;
    }


    public static class DefensiveStance extends FlavourBuff {

        {
            announced = true;
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.DUEL_EVASIVE;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (4 - visualcooldown()) / 4);
        }
    }
}
