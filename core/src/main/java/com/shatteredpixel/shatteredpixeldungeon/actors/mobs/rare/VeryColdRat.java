package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VeryColdRatSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class VeryColdRat extends Mob {
    protected boolean hasRaged = false;

    {
        spriteClass = VeryColdRatSprite.class;

        HP = HT = 70;

        defenseSkill = 5;

        EXP = 8;

        baseSpeed = 1.5f;

        maxLvl = 16;

        loot = new FrozenCarpaccio();
        lootChance = 1f;

        properties.add(Property.ICY);
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return super.isInvulnerable(effect) || buff(DeadBombTime.class) != null;
    }

    @Override
    public synchronized boolean isAlive() {
        if (super.isAlive()){
            return true;
        } else {
            if (!hasRaged){
                triggerEnrage();
            }
            return !buffs(DeadBombTime.class).isEmpty();
        }
    }

    protected void triggerEnrage(){
        Buff.affect(this, DeadBombTime.class).setShield(5);
        sprite.showStatusWithIcon( CharSprite.NEGATIVE, "5", FloatingText.SHIELDING );
        if (Dungeon.level.heroFOV[pos]) {
            SpellSprite.show( this, SpellSprite.BERSERK);
        }
        spend( TICK );
        hasRaged = true;
    }


    @Override
    public int defenseProc( Char enemy, int damage ) {

        PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 1 );
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                GameScene.add(Blob.seed(i, 20, Freezing.class));
            }
        }

        return super.defenseProc(enemy, damage);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if(Random.Float()<=0.25f){
            Buff.affect(enemy, Frost.class,6f);
        }
        damage((int) (HT*0.05f),this, DamageType.REAL);
        return super.attackProc(enemy, damage);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 25 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 3);
    }

    public static class DeadBombTime extends ShieldBuff {

        {
            type = buffType.POSITIVE;
        }

        @Override
        public boolean act() {

            if(target instanceof VeryColdRat){
                ((VeryColdRat) target).state = ((VeryColdRat) target).PASSIVE;
            }

            if (target.HP > 0){
                detach();
                return true;
            }

            absorbDamage( 1 );

            if (shielding() <= 0){
                target.die(null);
            }

            spend( TICK );

            return true;
        }

        @Override
        public void detach() {
            super.detach();
            FrostBomb bomb = new FrostBomb();
            Bomb.Fuse fuse = new Bomb.Fuse();
            fuse.bomb = bomb;
            bomb.fuse = fuse;
            Actor.add(fuse, Actor.now);
            Dungeon.level.drop(bomb, target.pos).sprite.drop();
        }

        @Override
        public int icon () {
            return BuffIndicator.FROST;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.BLUE_COLOR);
        }

        @Override
        public String desc () {
            return Messages.get(this, "desc", shielding());
        }

    }

    private static final String HAS_RAGED = "has_raged";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(HAS_RAGED, hasRaged);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        hasRaged = bundle.getBoolean(HAS_RAGED);
    }

}

