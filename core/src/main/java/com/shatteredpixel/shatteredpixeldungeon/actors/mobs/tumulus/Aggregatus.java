package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.extra.ScrollOfSoul;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AggregatusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Aggregatus extends Mob implements Callback, Hero.Doom {

    {
        HP = HT = 25;
        defenseSkill = 35;
        EXP = 12;
        maxLvl = 21;

        spriteClass = AggregatusSprite.class;

        properties.add(Property.NECRO);
        properties.add(Property.TUMULUS);
        properties.add(Property.UNDEAD);
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int drRoll() {
        return 0;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(9, 18);
    }


    private static final int SHRIEK_MIN_DAMAGE = 12;
    private static final int SHRIEK_MAX_DAMAGE = 21;
    private static final int SHRIEK_PARALYSIS_DURATION = 5;
    private static final int SHRIEK_SLOW_DURATION = 5;
    private static final int SHRIEK_RANGE = 8;

    @Override
    protected boolean canAttack(Char enemy) {

        if (super.canAttack(enemy)) {
            return true;
        }

        boolean shriekAvailable = false;
        if (enemy.isAlive() && enemySeen) {
            int dist = Dungeon.level.distance(pos, enemy.pos);
            if (dist <= SHRIEK_RANGE && !Dungeon.level.adjacent(pos, enemy.pos)
                    && !isTargetDisabled(enemy)) {
                Ballistica shriek = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);
                if (shriek.collisionPos == enemy.pos) {
                    shriekAvailable = Random.Float() < 0.5f;
                }
            }
        }


        int dist = Dungeon.level.distance(pos, enemy.pos);
        if (dist <= SHRIEK_RANGE && !Dungeon.level.adjacent(pos, enemy.pos)) {
            Ballistica shriek = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);
            return shriek.collisionPos == enemy.pos && shriekAvailable;
        }

        return false;
    }

    @Override
    protected boolean doAttack(Char enemy) {

        if (Dungeon.level.adjacent(pos, enemy.pos)) {
            return super.doAttack(enemy);
        }

        if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
            sprite.zap(enemy.pos);
            return false;
        } else {
            shriek();
            return true;
        }
    }

    private static final float TIME_TO_ZAP = 1f;

    protected void shriek() {
        spend(TIME_TO_ZAP);
        Invisibility.dispel(this);
        Char enemy = this.enemy;

        if (hit(this, enemy, false)) {
            int damage = Random.NormalIntRange(SHRIEK_MIN_DAMAGE, SHRIEK_MAX_DAMAGE);
            enemy.damage(damage, new SoulShriek(), DamageType.REAL);
            Buff.affect(enemy, Paralysis.class, SHRIEK_PARALYSIS_DURATION);

            if (Dungeon.level.heroFOV[enemy.pos]) {
                CellEmitter.center(enemy.pos).burst(Speck.factory(Speck.SCREAM), 5);
                Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
            }
        } else {
            GLog.w( Messages.get(this, "shriek_dodge",enemy.name()) );
            Buff.affect(enemy, Slow.class, SHRIEK_SLOW_DURATION);
            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
        }
    }

    public static class SoulShriek {}

    public void onZapComplete() {
        shriek();
        next();
    }

    @Override
    public void call() {
        next();
    }

    private boolean isTargetDisabled(Char target) {
        if (target == null) return false;
        return target.buff(Paralysis.class) != null || target.buff(Slow.class) != null;
    }

    // ========== 怨毒机制 ==========

    private static final int VENOMOUS_POISON_DURATION = 14;
    private static final int VENOMOUS_WEAKNESS_DURATION = 17;

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy != null && enemy.isAlive()) {
            Buff.affect(enemy, Poison.class).set(VENOMOUS_POISON_DURATION);
            Buff.affect(enemy, Weakness.class, VENOMOUS_WEAKNESS_DURATION);
        }
        return super.attackProc(enemy, damage);
    }

    @Override
    public void rollToDropLoot() {
        super.rollToDropLoot();
        ScrollOfSoul soul = new ScrollOfSoul();
        Dungeon.level.drop(soul, pos).sprite.drop();
    }

    @Override
    public void onDeath() {
        Dungeon.fail( getClass() );
        GLog.n( Messages.get(this, "shriek_hit",Dungeon.hero.name()) );
    }

}