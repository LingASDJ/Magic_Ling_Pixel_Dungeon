package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sickle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class DeathRongBoat extends MeleeWeapon {

    {
        image = ItemSpriteSheet.DEATHRONG_BOAT;
        tier = 5;
        ACC = 1.25f;
    }

    public int min(int level) {
        return 5 + level;
    }


    public int max(int level) {
        return 30 + level * 6;
    }

    public String desc() {
        int damageReduction = Math.max(0, 25 - buffedLvl());
        return Messages.get(this, "desc", damageReduction);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (Dungeon.level.water[attacker.pos] || Dungeon.level.map[attacker.pos] == Terrain.SALT_WATER) {
            int targetPos = defender.pos;
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob.pos == targetPos) continue;
                if (Dungeon.level.water[mob.pos] || Dungeon.level.map[mob.pos] == Terrain.SALT_WATER) {
                    if(mob.alignment == Char.Alignment.ENEMY && mob != attacker){
                        int distance = Dungeon.level.distance(targetPos, mob.pos);
                        // 确保伤害递减率至少为0，避免增伤
                        float damageReduction = Math.max(0, (25 - buffedLvl()) / 100f);
                        // 计算最终伤害，确保不超过原始伤害
                        int finalDamage = (int) (damage * Math.max(0, 1 - damageReduction * distance));
                        // 确保最小伤害为1
                        finalDamage = Math.min(damage, Math.max(1, finalDamage));
                        mob.damage(finalDamage, attacker, Char.DamageType.PHYSICAL);
                    }
                }
            }
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //replaces damage with 30+4.5*lvl bleed, roughly 133% avg base dmg, 129% avg scaling
        int bleedAmt = augment.damageFactor(Math.round(30f + 4.5f*buffedLvl()));
        Sickle.harvestAbility(hero, target, 0f, bleedAmt, this);
    }

    @Override
    public String abilityInfo() {
        int bleedAmt = levelKnown ? Math.round(30f + 4.5f*buffedLvl()) : 30;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(bleedAmt));
        } else {
            return Messages.get(this, "typical_ability_desc", bleedAmt);
        }
    }

    @Override
    public String upgradeAbilityStat(int level) {
        return Integer.toString(augment.damageFactor(Math.round(30f + 4.5f*level)));
    }



}
