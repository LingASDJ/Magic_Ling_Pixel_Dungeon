package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crossbow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class ForestBow extends MeleeWeapon implements Item.LengedsItem {
    {
        image = ItemSpriteSheet.FORESTCROSSBOW;
        tier = 3;
        ACC = 1.0f;
        DLY = 0.75F;
    }

    @Override
    public String desc() {
        String s = super.desc();
        if(!SPDSettings.isItemUnlock(getClass().getSimpleName())){
            s += "\n\n" + Messages.get(this, "no_unlock");
        }
        return s;
    }

    public int min(int level) {
        return 2 + level;
    }

    @Override
    public int iceCoinValue() {
        return 85;
    }

    public int max(int level) {
        return (int) (15 + level * 2.5);
    }



    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)){
            if (hero.buff(ChargedShot.class) != null &&
                    !(hero.belongings.weapon() instanceof Crossbow)
                    && !(hero.belongings.secondWep() instanceof Crossbow)){
                //clear charged shot if no crossbow is equipped
                hero.buff(ChargedShot.class).detach();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public float accuracyFactor(Char owner, Char target) {
        if (owner.buff(ChargedShot.class) != null){
            Actor.add(new Actor() {
                { actPriority = VFX_PRIO; }
                @Override
                protected boolean act() {
                    if (owner instanceof Hero && !target.isAlive()){
                        onAbilityKill((Hero)owner, target);
                    }
                    Actor.remove(this);
                    return true;
                }
            });
            return Float.POSITIVE_INFINITY;
        } else {
            return super.accuracyFactor(owner, target);
        }
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        int dmg = super.proc(attacker, defender, damage);

        //stronger elastic effect
        if (attacker.buff(ChargedShot.class) != null && !(curItem instanceof Dart)){
            //trace a ballistica to our target (which will also extend past them
            Ballistica trajectory = new Ballistica(attacker.pos, defender.pos, Ballistica.STOP_TARGET);
            //trim it to just be the part that goes past them
            trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
            //knock them back along that ballistica
            WandOfBlastWave.throwChar(defender,
                    trajectory,
                    3,
                    true,
                    true,
                    this);
            attacker.buff(ChargedShot.class).detach();
        }
        return dmg;
    }
    
    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (hero.buff(ChargedShot.class) != null){
            GLog.w(Messages.get(this, "ability_cant_use"));
            return;
        }

        beforeAbilityUsed(hero, null);
        Buff.affect(hero, ChargedShot.class);
        hero.sprite.operate(hero.pos);
        hero.next();
        afterAbilityUsed(hero);
    }

    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", 3+buffedLvl(), 3+buffedLvl());
        } else {
            return Messages.get(this, "ability_desc", 3, 3);
        }
    }

    @Override
    public String upgradeAbilityStat(int level) {
        return Integer.toString(3 + level);
    }

    public static class ChargedShot extends Buff{

        {
            announced = true;
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.DUEL_XBOW;
        }

    }
}
