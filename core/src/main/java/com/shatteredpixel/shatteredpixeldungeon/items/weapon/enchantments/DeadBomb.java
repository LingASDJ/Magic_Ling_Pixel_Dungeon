package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class DeadBomb extends Weapon.Enchantment {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        //审判附魔
        if(defender.buff(DeadBomb.TargetDead.class) != null && defender.TrueDied && defender.isAlive() && defender.HP <= damage) {
            Buff.detach(defender, DeadBomb.TargetDead.class);
            boolean kill = true;
            for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos] && kill && (!defender.properties.contains(Char.Property.BOSS) || !defender.properties.contains(Char.Property.MINIBOSS))) {
                    int dmg = (int) (defender.HT * ((40 + (hero.belongings.weapon != null ? hero.belongings.weapon.level() : 2 * 5) / 100.0)));
                    int maxDamage = (int) (defender.HT * (135) / 100.0);
                    dmg = Math.round(dmg * AscensionChallenge.statModifier(mob));
                    dmg = mob.defenseProc(mob,dmg);
                    dmg -= mob.drRoll();
                    damage = Math.min(damage, maxDamage);
                    mob.damage(dmg,this);
                }
            }
        } else if(Random.Int(100)<= 36 + weapon.level() && (!defender.properties.contains(Char.Property.BOSS) || !defender.properties.contains(Char.Property.MINIBOSS)) ) {
            for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                if (mob.buff(DeadBomb.TargetDead.class) == null) {
                    Buff.affect(defender, TargetDead.class);
                }
            }
        }
        return damage;
    }

    public String desc() {
        int weapon;
        if(hero != null && hero.belongings.weapon != null){
            weapon = hero.belongings.weapon.level() * 5;
        } else {
            weapon = 0;
        }
        int maxDamage = (40 + 95);
        int damage = (40 + weapon);
        damage = Math.min(damage, maxDamage);
        return Messages.get(this, "desc",damage);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xDC143C, 1f);
    }

    public static class TargetDead extends Buff {

        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        @Override
        public boolean attachTo(Char target) {
            if (super.attachTo(target)){
                target.TrueDied = true;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void detach() {
            super.detach();
            target.TrueDied = false;
            target.die(true);
        }

        @Override
        public int icon() {
            return BuffIndicator.CORRUPT;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xDC143C);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }

    }

}
