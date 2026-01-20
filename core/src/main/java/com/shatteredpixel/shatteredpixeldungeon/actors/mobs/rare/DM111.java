package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM111Sprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class DM111 extends Mob implements Callback {

    private static final float TIME_TO_ZAP	= 2f;

    public int attackCooldown;

    {
        spriteClass = DM111Sprite.class;

        HP = HT = 35;
        defenseSkill = 8;

        EXP = 7;
        maxLvl = 13;

        baseSpeed = 0.5f;

        loot = new ScrollOfRecharging();
        lootChance = 0.25f;

        properties.add(Property.ELECTRIC);
        properties.add(Property.INORGANIC);
    }

    @Override
    public boolean act() {
        // 处理攻击冷却
        if (attackCooldown > 0) {
            attackCooldown--;
            spend(TICK);
            return true;
        }
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 4, 10 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 11;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 6);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        if (attackCooldown == 0) {
            return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
        } else {
            return super.canAttack(enemy);
        }
    }


    @Override
    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )
                || new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos != enemy.pos) {

            return super.doAttack( enemy );

        } else {

            spend( TIME_TO_ZAP );

            Invisibility.dispel(this);
            if (hit( this, enemy, true )) {
                int dmg = Random.NormalIntRange(5, 13);
                dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
                enemy.damage( dmg, new DM100.LightningBolt() );

                Buff.affect(enemy, Paralysis.class, 3f);

                attackCooldown = Random.Int(2,9);

                if (enemy.sprite.visible) {
                    enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
                    enemy.sprite.flash();
                }

                if (enemy == Dungeon.hero) {

                    PixelScene.shake( 2, 0.3f );

                    if (!enemy.isAlive()) {
                        Badges.validateDeathFromEnemyMagic();
                        Dungeon.fail( this );
                        GLog.n( Messages.get(this, "zap_kill") );
                    }
                }
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void call() {
        next();
    }


    private static final String ATTACK_COOLDOWN = "attack_cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ATTACK_COOLDOWN, attackCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        attackCooldown = bundle.getInt(ATTACK_COOLDOWN);
    }
}

