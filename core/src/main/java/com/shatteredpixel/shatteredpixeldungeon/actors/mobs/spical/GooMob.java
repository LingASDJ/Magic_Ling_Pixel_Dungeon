package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GooMob extends Mob {

    {
        if (Dungeon.isDLC(Conducts.Conduct.BOSSRUSH)) {
            HP = HT = 180;
        } else {
            HP = HT = Dungeon.isChallenged(Challenges.STRONGER_BOSSES) ? 120 : 100;
        }

        EXP = 10;
        defenseSkill = 8;
        spriteClass = GooSprite.class;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.ACIDIC);
    }

    private int pumpedUp = 0;
    private int healInc = 1;

    @Override
    public int damageRoll() {
        int min = 1;
        int max = (HP*2 <= HT) ? 12 : 8;
        if (pumpedUp > 0) {
            pumpedUp = 0;
            return Random.NormalIntRange( min*3, max*3 );
        } else {
            return Random.NormalIntRange( min, max );
        }
    }

    @Override
    public int attackSkill( Char target ) {
        int attack = 10;
        if (HP*2 <= HT) attack = 15;
        if (pumpedUp > 0) attack *= 2;
        return attack;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return (int)(super.defenseSkill(enemy) * ((HP*2 <= HT)? 1.5 : 1));
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    @Override
    public boolean act() {

        if (Dungeon.level.water[pos] && HP < HT) {
            HP += healInc;

            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null) lock.removeTime(healInc*2);

            if (Dungeon.level.heroFOV[pos] ){
                sprite.emitter().burst( Speck.factory( Speck.HEALING ), healInc );
            }
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES) && healInc < 3) {
                healInc++;
            }
            if (HP*2 > HT) {
                BossHealthBar.bleed(false);
                ((GooSprite)sprite).spray(false);
                HP = Math.min(HP, HT);
            }
        } else {
            healInc = 1;
        }

        return super.act();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (pumpedUp > 0){
            //we check both from and to in this case as projectile logic isn't always symmetrical.
            //this helps trim out BS edge-cases
            return Dungeon.level.distance(enemy.pos, pos) <= 2
                    && new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos
                    && new Ballistica( enemy.pos, pos, Ballistica.PROJECTILE).collisionPos == pos;
        } else {
            return super.canAttack(enemy);
        }
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int( 3 ) == 0) {
            Buff.affect( enemy, Ooze.class ).set( Ooze.DURATION );
            enemy.sprite.burst( 0x000000, 5 );
        }

        if (pumpedUp > 0) {
            Camera.main.shake( 3, 0.2f );
        }

        return damage;
    }

    @Override
    public void updateSpriteState() {
        super.updateSpriteState();

        if (pumpedUp > 0){
            ((GooSprite)sprite).pumpUp( pumpedUp );
        }
    }

    @Override
    protected boolean doAttack( Char enemy ) {
        if (pumpedUp == 1) {
            pumpedUp++;
            ((GooSprite)sprite).pumpUp( pumpedUp );

            spend( attackDelay() );

            return true;
        } else if (pumpedUp >= 2 || Random.Int( (HP*2 <= HT) ? 2 : 5 ) > 0) {

            boolean visible = Dungeon.level.heroFOV[pos];

            if (visible) {
                if (pumpedUp >= 2) {
                    ((GooSprite) sprite).pumpAttack();
                } else {
                    sprite.attack(enemy.pos);
                }
            } else {
                if (pumpedUp >= 2){
                    ((GooSprite)sprite).triggerEmitters();
                }
                attack( enemy );
                spend( attackDelay() );
            }

            return !visible;

        } else {

            pumpedUp++;
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
                pumpedUp++;
            }

            ((GooSprite)sprite).pumpUp( pumpedUp );

            if (Dungeon.level.heroFOV[pos]) {
                sprite.showStatus( CharSprite.NEGATIVE, Messages.get(Goo.class, "!!!") );
                GLog.n( Messages.get(Goo.class, "pumpup") );
            }

            spend( attackDelay() );

            return true;
        }
    }

    @Override
    public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
        boolean result = super.attack( enemy, dmgMulti, dmgBonus, accMulti );
        pumpedUp = 0;
        return result;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (pumpedUp != 0) {
            pumpedUp = 0;
            sprite.idle();
        }
        return super.getCloser( target );
    }

    @Override
    public void damage(int dmg, Object src) {
        boolean bleeding = (HP*2 <= HT);
        super.damage(dmg, src);
        if ((HP*2 <= HT) && !bleeding){
            sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Goo.class, "enraged"));
            ((GooSprite)sprite).spray(true);
            yell(Messages.get(Goo.class, "gluuurp"));
        }
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*2);
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
    }

    private final String PUMPEDUP = "pumpedup";
    private final String HEALINC = "healinc";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );

        bundle.put( PUMPEDUP , pumpedUp );
        bundle.put( HEALINC, healInc );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        pumpedUp = bundle.getInt( PUMPEDUP );
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);

        //if check is for pre-0.9.3 saves
        healInc = bundle.getInt(HEALINC);

    }

}
