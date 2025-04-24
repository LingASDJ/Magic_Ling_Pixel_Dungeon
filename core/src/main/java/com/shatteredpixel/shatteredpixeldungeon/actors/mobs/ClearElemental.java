package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ClearElementalSprites;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ClearElemental extends Mob {

    public void onZapComplete() {
        zap();
        next();
    }

    public boolean add(Buff buff) {
        if (super.add(buff)) {
            if (state == PASSIVE && buff.type == Buff.buffType.NEGATIVE) {
                state = HUNTING;
            }
            return true;
        }
        return false;
    }

    @Override
    public void damage( int dmg, Object src ) {

        if (state == PASSIVE) {
            state = HUNTING;
        }

        super.damage( dmg, src );
    }

    private int combo = 0;
    {
        spriteClass = ClearElementalSprites.class;
        EXP = 8;
        HP = HT = 16;
        defenseSkill = 2;
        if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
            immunities.add( CrivusFruits.DiedBlobs.class );
        }
        maxLvl = 7;
        state = Badges.isUnlocked(Badges.Badge.KILL_FIRE_DRAGON) ? PASSIVE : HUNTING;
        flying =true;
    }

    {
        immunities.add( CorrosiveGas.class );
        immunities.add( CrivusFruits.DiedBlobs.class );
    }
    @Override
    public int attackSkill( Char target ) {
        return 7;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
    }

    private void zap() {
        spend(3f);
        combo++;
        int effect = Random.Int(4)+combo;

        if (effect > 2) {

            if (effect >= 4 && enemy.buff(Burning.class) == null) {
                GameScene.add(Blob.seed(enemy.pos, 45, ConfusionGas.class));
            }
        }
    }

    @Override
    protected boolean getCloser( int target ) {
        combo = 0; //if he's moving, he isn't attacking, reset combo.
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
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
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        combo = bundle.getInt( COMBO );
    }
}
