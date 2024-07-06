package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneral;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TimeReset extends Weapon.Enchantment {

    private static ItemSprite.Glowing BLUEISH = new ItemSprite.Glowing(Window.BLUE_COLOR);

    private ArrayList<Mob> mobs = new ArrayList<>();
    private ArrayList<Integer> mobpos = new ArrayList<>();

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage ) {
        int level = Math.max( 0, weapon.buffedLvl() );

        Buff.affect(defender, ScaryBuff.class).set((100), 1);
        ScaryBuff existingX = defender.buff(ScaryBuff.class);
        if (existingX != null) {
            existingX.damgeScary(Random.Int((int) (2*procChanceMultiplier(attacker)), (int) (6*procChanceMultiplier(attacker))*level == 0 ? 1:level));
        }

        if(mobs.isEmpty() && !mobpos.isEmpty()){
            mobpos.clear();
        }
        if(!mobs.isEmpty() && mobpos.isEmpty()){
            mobs.clear();
        }

        if(mobs.isEmpty() && mobpos.isEmpty()){
            for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if(Dungeon.level.heroFOV[mob.pos]){
                    mobs.add(mob);
                    mobpos.add(mob.pos);
                }
            }
        } else {
            for(int m1 = mobs.size()-1; m1 > 0; m1--){
                if(mobs.get(m1) != null && mobpos.get(m1) != null){
                    if(Actor.findChar(mobpos.get(m1)) == null){
                        appear(mobs.get(m1), mobpos.get(m1));
                        Buff.affect( mobs.get(m1), ScaryBuff.class ).set( (100), 1 );
                        ScaryBuff existing = mobs.get(m1).buff(ScaryBuff.class);
                        if (existing != null){
                            existing.damgeScary((int) (Random.Int(3,5)*(procChanceMultiplier(attacker)/2f)));
                        }
                    }
                }
            }
            mobs.clear();
            mobpos.clear();
        }

        if(attacker instanceof Hero){
            if(((Hero) attacker).belongings.armor.glyph != null){
                comboProc(this, ((Hero) attacker).belongings.armor.glyph, attacker, defender, damage);
            }
        }

        return damage;

    }

    public void appear( Char ch, int pos ) {
        if (ch.invisible == 0) {

            try {
                ch.sprite.interruptMotion();
                ch.move( pos );
                ch.sprite.place( pos );
                ch.sprite.alpha( 0 );
                int target = ch.pos;
                int oppositeAdjacent = target + (target - pos);
                Ballistica trajectory = new Ballistica(target, oppositeAdjacent, Ballistica.MAGIC_BOLT);
                WandOfBlastWave.throwChar(ch, trajectory, 2, false, false, this);

                ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
            } catch (Exception e) {
                //
            }

        }

    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUEISH;
    }


    public static class MobsWither extends DwarfGeneral.Wither {

        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        public static final float DURATION	= 30f;
        private float damageInc = 0;
        @Override
        public boolean act() {
            if (target.isAlive()) {

                if(hero.belongings.weapon != null){
                    Weapon altWeapon = (Weapon) hero.belongings.weapon;
                    if (altWeapon.enchantment instanceof TimeReset){
                        damageInc = Random.Int(2,3)*altWeapon.enchantment.procChanceMultiplier(hero);
                    }
                }


                target.damage((int)damageInc+1, this);
                damageInc -= (int)damageInc;

                spend(1f);
                if (--level <= 0) {
                    detach();
                }
                if (target == hero && !target.isAlive()){
                    GLog.n(Messages.get(DwarfGeneral.Wither.class, "on_kill"));
                }


            } else {
                detach();
            }

            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.POISON;
        }

        public static final String DAMAGE = "damage_inc";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DAMAGE, damageInc);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            damageInc = bundle.getFloat(DAMAGE);
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 0f, 0f);
        }

    }
}
