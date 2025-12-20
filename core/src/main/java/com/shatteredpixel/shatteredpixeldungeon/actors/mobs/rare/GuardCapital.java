package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Guard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GuardCapitalSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GuardCapital extends Mob {
    {
        spriteClass = GuardCapitalSprite.class;

        HP = HT = 50;
        defenseSkill = 12;

        EXP = 7;

        isAnimal = true;
        maxLvl = 14;

        loot = Random.Float() > 0.5f ? Generator.Category.WEAPON : Generator.Category.ARMOR;
        lootChance = 1f;
    }

    private int knockbackCooldown = 0;

    @Override
    protected boolean act() {
        // 每回合减少冷却时间
        if (knockbackCooldown > 0) {
            knockbackCooldown--;
        }
        return super.act();
    }

    @Override
    protected boolean getCloser(int target) {
        boolean guardNearby = false;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof Guard) {
                if (Dungeon.level.distance(mob.pos, pos) <= viewDistance) {
                    guardNearby = true;
                    break;
                }
            }
        }

        if (state == HUNTING) {
            if (guardNearby) {
                return false;
            }
            return enemySeen && getFurther(target);
        } else {
            return super.getCloser(target);
        }
    }

    @Override
    protected boolean canAttack(Char enemy) {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof Guard) {
                if (Dungeon.level.distance(mob.pos, pos) <= viewDistance) {
                    Ballistica trajectory = new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE);
                    return trajectory.collisionPos == enemy.pos;
                }
            }
        }
        return !Dungeon.level.adjacent( pos, enemy.pos )
                && (super.canAttack(enemy) || new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos);
    }

    @Override
    public int damageRoll() {
        int distance = enemy != null ? Dungeon.level.distance(pos, enemy.pos) : 0;
        if (distance > 1) {
            return Random.NormalIntRange(6, 12);
        }
        return Random.NormalIntRange(6, 16);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (!Dungeon.level.adjacent(pos, enemy.pos) && knockbackCooldown <= 0 &&
                canKnockBack(enemy)) {

            int direction = enemy.pos - pos;
            Ballistica trajectory = new Ballistica(enemy.pos, enemy.pos + direction, Ballistica.PROJECTILE);

            WandOfBlastWave.throwChar(enemy, trajectory, 3, true, false, this);

            knockbackCooldown = 20;
        }
        return super.attackProc(enemy, damage);
    }

    private boolean canKnockBack(Char target) {
        if (target == null) return false;

        int direction = target.pos - pos;
        Ballistica trajectory = new Ballistica(target.pos, target.pos + direction, Ballistica.PROJECTILE);

        for (int i = 1; i <= Math.min(3, trajectory.dist); i++) {
            int nextPos = trajectory.path.get(i);

            if (!Dungeon.level.passable[nextPos] ||
                    Dungeon.level.map[nextPos] == Terrain.CHASM) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 7);
    }

    private static final String KNOCLOCK_COOLDOWN = "knockback_cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(KNOCLOCK_COOLDOWN, knockbackCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        knockbackCooldown = bundle.getInt(KNOCLOCK_COOLDOWN);
    }

}