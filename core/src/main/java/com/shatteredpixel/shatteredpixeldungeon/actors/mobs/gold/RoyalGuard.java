package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class RoyalGuard extends Mob {
    {
        spriteClass = RoyalGuardSprite.class;

        HP = HT = 80;
        defenseSkill = 30;


        EXP = 10;
        maxLvl = 21;

        properties.add(Property.UNDEAD);

        loot = Generator.Category.WEP_T4;
        lootChance = 0.2f;
    }
    Weapon equipment;
    private static final String ROYAL_GUARD_WEAPON	= "royal_guard_weapon";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        if(equipment != null)
            bundle.put( ROYAL_GUARD_WEAPON, equipment );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        equipment = (Weapon)bundle.get( ROYAL_GUARD_WEAPON );
        if(equipment == null)
            equipment = (Weapon) Generator.randomUsingDefaults(Random.oneOf(Generator.Category.WEP_T4, Generator.Category.WEP_T5));
    }

    public RoyalGuard(){
        super();
        equipment = (Weapon) Generator.randomUsingDefaults(Random.oneOf(Generator.Category.WEP_T4, Generator.Category.WEP_T5));
    }

    @Override
    public String description() {
        String desc = super.description();
        desc += "\n\n";
        desc += Messages.get(this, "equipment", equipment.name() );
        desc += "\n\n" + Messages.get(GoldMob.class,"infos");
        return desc;
    }
    @Override
    public Item createLoot() {
        int Ug = Random.Int(0,100)<50?0:(Random.Int(0,50)<40?1:2);
        if(equipment != null && equipment instanceof MeleeWeapon)
            loot = equipment.upgrade(Ug);
        return super.createLoot();
    }

    @Override
    public int damageRoll() {
        if(equipment != null)
            return equipment.damageRoll(this);
        else
            return Random.Int(8,8);
    }

    @Override
    public int attackSkill( Char target ) {
        int accuracy = 30;
        if(equipment != null && equipment instanceof MeleeWeapon){
            accuracy *= equipment.accuracyFactor( this, target );
        }
        return accuracy;
    }

    @Override
    public float attackDelay() {
        return super.attackDelay() * equipment.delayFactor(this);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if(equipment != null && equipment instanceof MeleeWeapon)
            return super.canAttack(enemy) || equipment.canReach(this, enemy.pos);
        else
            return super.canAttack(enemy);
    }
    @Override
    public int drRoll() {
        return Random.NormalIntRange(1, 8) + equipment.defenseFactor(this);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        damage = equipment.proc( this, enemy, damage );
        if (!enemy.isAlive() && enemy == Dungeon.hero){
            Dungeon.fail(getClass());
            GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
        }
        return damage;
    }


}
