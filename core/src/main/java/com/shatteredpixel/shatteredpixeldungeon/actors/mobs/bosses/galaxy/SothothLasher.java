package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SothothLasherSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class SothothLasher extends Mob implements Callback {

    private static final float TIME_TO_ZAP	= 8f;

    {
        spriteClass = SothothLasherSprite.class;

        HP = HT = 150;
        defenseSkill = 20;

        EXP = 9;
        maxLvl = 21;

        loot = Generator.Category.GOLD;
        lootChance = 0.6f;

        properties.add(Char.Property.UNDEAD);
    }

    {
        immunities.add(Terror.class);
        immunities.add(Vertigo.class);
        immunities.add(Burning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(Freezing.class);
        immunities.add(Frost.class);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if(Random.Int(10)>=5){
            Buff.affect(enemy, Ooze.class).set( Ooze.DURATION );
        }
        return super.attackProc(enemy, damage);
    }

    @Override
    public int damageRoll() {
        return Char.combatRoll( 12, 24 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 25;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(6, 10);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

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

    private void zap() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {

            int dmg = Random.NormalIntRange( 8, 24 );
            enemy.damage( dmg, new Eye.DeathGaze());

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                GLog.n(Messages.get(Char.class, "kill", name()));
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        Statistics.killYogMobsAnargy++;
    }

        @Override
    public void call() {
        next();
    }

}

