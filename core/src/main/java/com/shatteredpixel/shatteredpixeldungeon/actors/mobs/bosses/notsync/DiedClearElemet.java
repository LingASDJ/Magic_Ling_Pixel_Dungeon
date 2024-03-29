package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireDragon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DiedClearElementalSprites;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class DiedClearElemet extends Mob {




    //MainNotUsed
    public void onZapComplete() {
        zap();
        next();
    }

    public int combo = 0;
    {
        spriteClass = DiedClearElementalSprites.class;
        EXP = 8;
        HP = HT = Random.NormalIntRange(20,45);
        defenseSkill = 2;
        maxLvl = -1;
        immunities.add(Burning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(FrostBurning.class);
        immunities.add(Chill.class);
        immunities.add(Vertigo.class);
        immunities.add(ToxicGas.class);
        flying =true;
    }

    @Override
    public int attackSkill( Char target ) {
        return 7;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if(Dungeon.level.distance(pos,target)>3)
            return false;
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
    }

    private void zap() {
        spend(5f);
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
            zap();
            next();
        }

        private void zap() {
            spend(5f);
            combo++;
            int effect = Random.Int(4)+combo;

            if (effect > 2) {

                if (effect >= 4 && enemy.buff(Burning.class) == null) {
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
            zap();
            next();
        }

        private void zap() {
            spend(5f);
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
            zap();
            next();
        }

        private void zap() {
            spend(5f);
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
            zap();
            next();
        }

        private void zap() {
            spend(5f);
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
            zap();
            next();
        }

        private void zap() {
            spend(5f);
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

