package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdMagicRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireDragon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DiedClearElementalSprites;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class DiedClearElemet extends Mob {

    @Override
    public String description() {
        String description = super.description();
        if (alignment == Alignment.ALLY){
            return description+"\n\n"+Messages.get(DiedClearElemet.class, "friend");
        } else {
            return description;
        }
    }

    private class Wandering extends Mob.Wandering {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            if ( enemyInFOV ) {

                enemySeen = true;

                notice();
                alerted = true;
                state = HUNTING;

                target = hero.pos;
            } else {

                enemySeen = false;

                int oldPos = pos;

                target = hero.pos;

                //always move towards the hero when wandering
                if (getCloser( target )) {
                    spend( 1 / speed() );
                    return moveSprite( oldPos, pos );
                } else {
                    spend( TICK );
                }
            }

            return true;
        }

    }

    private void Alt_Zap() {
        spend( 1f );

        if (hit( this, enemy, true )) {

            if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                Buff.prolong( enemy, Blindness.class, Degrade.DURATION/5 );
                Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
            }

            int dmg = Random.NormalIntRange( 10, 12 );
            enemy.damage( dmg, new ColdMagicRat.DarkBolt() );
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    //MainNotUsed
    public void onZapComplete() {
        zap();
        if(Dungeon.branch == 0){
            Alt_Zap();
        }
        next();
    }


    @Override
    public void damage(int dmg, Object src, DamageType type) {

        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null){
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))   lock.addTime(dmg);
            else                                                    lock.addTime(dmg*1.5f);
        }

        super.damage(dmg, src, type);
    }


    public int combo = 0;
    {
        spriteClass = DiedClearElementalSprites.class;
        EXP = 8;
        HP = HT = Random.NormalIntRange(20,45);
        defenseSkill = Dungeon.branch == 0 ? 12 : 2;
        maxLvl = -1;
        immunities.add(Burning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(FrostBurning.class);
        immunities.add(Chill.class);
        immunities.add(Vertigo.class);
        immunities.add(ToxicGas.class);
        flying =true;
        baseSpeed = Dungeon.branch == 0 ? 1.5f : 1f;

        if(Dungeon.branch == 0){
            WANDERING = new Wandering();
        }
    }

    @Override
    public int attackSkill( Char target ) {
        return Dungeon.branch == 0 ? 21 : 7;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if(Dungeon.level.distance(pos,target)>3)
            return false;
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
    }

    private void zap() {
        if(Dungeon.branch != 0){
            int dmg = Random.NormalIntRange( 4, 6 );
            enemy.damage( dmg, new ColdMagicRat.DarkBolt() );
        }
       spend(Dungeon.branch == 0 ? 1f : 5f);
        combo++;
        int effect = Random.Int(4)+combo;

        if (effect > 2) {

            if (effect >= 4 && enemy.buff(Burning.class) == null) {
                GameScene.add(Blob.seed(enemy.pos, 45, ConfusionGas.class));
            }
        }
    }

    @Override
    protected boolean getCloser(int target) {
        combo = 0;
        if (state == HUNTING) {
            if(Dungeon.level.distance(pos,target)>3)
                return super.getCloser( target );
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser(target);
        }
        //return false;
    }

    @Override
    public void aggro(Char ch) {
        //cannot be aggroed to something it can't see
        if (ch == null || fieldOfView == null || fieldOfView[ch.pos]) {
            super.aggro(ch);
        }
    }

    @Override
    public Item createLoot() {
        MissileWeapon drop = (MissileWeapon)super.createLoot();
        //half quantity, rounded up
        drop.quantity((drop.quantity()+1)/2);
        return drop;
    }

    private static final String COMBO = "combo";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(COMBO, combo);
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof FireDragon) {
                ((FireDragon) mob).summonedElementals--;
            }
        }
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        combo = bundle.getInt( COMBO );
    }

    //Mark Mob
    public static class ClearElemetalBlood extends DiedClearElemet{

        {
            spriteClass = DiedClearElementalSprites.Red.class;
        }

        public void onZapComplete() {
            super.onZapComplete();
            zap();
            next();
        }

        private void zap() {
            if(Dungeon.branch != 0){
                int dmg = Random.NormalIntRange( 4, 6 );
                enemy.damage( dmg, new ColdMagicRat.DarkBolt() );
            }
           spend(Dungeon.branch == 0 ? 1f : 5f);
            combo++;
            int effect = Random.Int(4)+combo;

            if (effect > 2) {

                if (effect >= 4 && enemy.buff(Bleeding.class) == null) {
                    Buff.affect( enemy, Bleeding.class ).set( effect );
                }
            }
        }

    }

    public static class ClearElemetalDark extends DiedClearElemet{

        {
            spriteClass = DiedClearElementalSprites.Dark.class;
        }

        public void onZapComplete() {
            super.onZapComplete();
            zap();
            next();
        }

        private void zap() {
            if(Dungeon.branch != 0){
                int dmg = Random.NormalIntRange( 4, 6 );
                enemy.damage( dmg, new ColdMagicRat.DarkBolt() );
            }
           spend(Dungeon.branch == 0 ? 1f : 5f);
            combo++;
            int effect = Random.Int(4)+combo;

            if (effect > 2) {

                if (effect >= 4 && enemy.buff(Burning.class) == null) {
                    Buff.affect(enemy, Cripple.class, 3f+effect);
                }
            }
        }

    }

    public static class ClearElemetalGreen extends DiedClearElemet{

        {
            spriteClass = DiedClearElementalSprites.Green.class;
        }

        public void onZapComplete() {
            super.onZapComplete();
            zap();
            next();
        }

        private void zap() {
            if(Dungeon.branch != 0){
                int dmg = Random.NormalIntRange( 4, 6 );
                enemy.damage( dmg, new ColdMagicRat.DarkBolt() );
            }
           spend(Dungeon.branch == 0 ? 1f : 5f);
            combo++;
            int effect = Random.Int(4)+combo;

            if (effect > 2) {

                if (effect >= 4 && enemy.buff(Burning.class) == null) {
                    GameScene.add(Blob.seed(enemy.pos, 45, ToxicGas.class));
                }
            }
        }

    }

    public static class ClearElemetalPure extends DiedClearElemet{

        {
            spriteClass = DiedClearElementalSprites.Pure.class;
        }

        public void onZapComplete() {
            super.onZapComplete();
            zap();
            next();
        }

        private void zap() {
            if(Dungeon.branch != 0){
                int dmg = Random.NormalIntRange( 4, 6 );
                enemy.damage( dmg, new ColdMagicRat.DarkBolt() );
            }
           spend(Dungeon.branch == 0 ? 1f : 5f);
            combo++;
            int effect = Random.Int(4)+combo;

            if (effect > 2) {

                if (effect >= 4 && enemy.buff(Burning.class) == null) {
                    Buff.affect(enemy, Hex.class, 3f+effect);
                }
            }
        }

    }

    public static class ClearElemetalGold extends DiedClearElemet{

        {
            spriteClass = DiedClearElementalSprites.Gold.class;
        }

        public void onZapComplete() {
            super.onZapComplete();
            zap();
            next();
        }

        private void zap() {
            if(Dungeon.branch != 0){
                int dmg = Random.NormalIntRange( 4, 6 );
                enemy.damage( dmg, new ColdMagicRat.DarkBolt() );
            }
           spend(Dungeon.branch == 0 ? 1f : 5f);
            combo++;
            int effect = Random.Int(4)+combo;

            if (effect > 2) {

                if (effect >= 4 && enemy.buff(Burning.class) == null) {
                    GameScene.add(Blob.seed(enemy.pos, 15, HalomethaneFire.class));
                }
            }
        }

    }


}

