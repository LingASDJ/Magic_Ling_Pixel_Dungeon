package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockHeadSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WarlockHead extends Mob implements Callback {

    private static final float TIME_TO_ZAP	= 1f;

    {
        spriteClass = WarlockHeadSprite.class;

        HP = HT = 85;
        defenseSkill = 18;

        EXP = 11;
        maxLvl = 21;

        loot = new PotionOfExperience();
        lootChance =1f;

        properties.add(Property.UNDEAD);
    }

    public int attackProc( Char enemy, int damage ) {
        Buff.affect(this, Barrier.class).setShield( Random.NormalIntRange(10,20));
        return super.attackProc(enemy, damage);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 18, 23 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 28;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 10);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack(enemy)
                || new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )
                || new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos != enemy.pos) {

            return super.doAttack( enemy );

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
    }

    protected void zap() {
        spend(TIME_TO_ZAP);

        Invisibility.dispel(this);
        Char enemy = this.enemy;
        if (hit(this, enemy, true)) {
            int dmg = Random.NormalIntRange(12, 18);
            dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
            enemy.damage(dmg, new Warlock.DarkBolt());

            // 2/3概率降级
            if (Random.Float() < 0.67f) {
                Buff.prolong(enemy, Degrade.class, 40f);
                Buff.affect(enemy, Weakness.class, 5f);
                Sample.INSTANCE.play(Assets.Sounds.DEGRADE);
            }

            // 1/3概率流血
            if (Random.Float() < 0.33f) {
                Buff.affect(enemy, Bleeding.class).set(Random.IntRange(5, 8));
                Buff.affect(enemy, Weakness.class, 5f);
            }

            // 1/3概率幻惑
            if (Random.Float() < 0.33f) {
                Buff.prolong(enemy, Vertigo.class, 30f);
                Buff.affect(enemy, Weakness.class, 5f);
            }

            // 1/5概率失明
            if (Random.Float() < 0.2f) {
                Buff.prolong(enemy, Blindness.class, 12f);
                Buff.affect(enemy, Weakness.class, 5f);
            }

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Badges.validateDeathFromEnemyMagic();
                Dungeon.fail(this);
                GLog.n(Messages.get(this, "bolt_kill"));
            }
        } else {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void call() {
        next();
    }

}

