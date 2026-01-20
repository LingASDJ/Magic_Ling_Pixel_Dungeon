package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VeryColdRatSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class VeryColdRat extends Mob {

    private boolean trueDied = false;
    private int deathCount = 0;

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
    protected boolean act() {
        int stolenLife = (int) Math.min(HT - HP, HT*0.05f);
        if(state != HUNTING && !trueDied){
            HP += stolenLife;
        }
        if(deathCount != 0){
            sprite.showStatus(CharSprite.NEGATIVE, String.valueOf(deathCount));
            deathCount--;
        }
        if(deathCount == 0 && HP == 0 && trueDied){
            die(true);
            FrostBomb bomb = new FrostBomb();
            Bomb.Fuse fuse = new Bomb.Fuse();
            fuse.bomb = bomb;
            bomb.fuse = fuse;
            Actor.add(fuse, Actor.now);
            Dungeon.level.drop(bomb, pos).sprite.drop();
        }
        return super.act();
    }

    @Override
    public boolean isAlive() {
       if(trueDied){
           return super.isAlive();
       } else if(deathCount == 0 && HP == 0) {
           HP = 0;
           deathCount = 4;
           state = PASSIVE;
           trueDied = true;
           if (Dungeon.hero.lvl > maxLvl + 2) {
               FrostBomb bomb = new FrostBomb();
               Dungeon.level.drop(bomb, enemy != null ? enemy.pos : Dungeon.hero.pos).sprite.drop();
           }
           GLog.n(Messages.get(this, "bomb"));
           return true;
       }
       return true;
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

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("deathCount", deathCount);
        bundle.put("trueDied", trueDied);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        deathCount = bundle.getInt("deathCount");
        trueDied = bundle.getBoolean("trueDied");
    }

}

