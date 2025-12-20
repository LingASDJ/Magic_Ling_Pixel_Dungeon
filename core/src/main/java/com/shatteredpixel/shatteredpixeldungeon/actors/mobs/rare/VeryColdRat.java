package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VeryColdRatSprite;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class VeryColdRat extends Mob {

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
        if(state != HUNTING){
            HP += stolenLife;
        }
        return super.act();
    }


    @Override
    public int defenseProc( Char enemy, int damage ) {

        PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                GameScene.add(Blob.seed(i, 20, Freezing.class));
            }
        }

        return super.defenseProc(enemy, damage);
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        FrostBomb bomb = new FrostBomb();
        Bomb.Fuse fuse = new Bomb.Fuse();
        fuse.bomb = bomb;
        bomb.fuse = fuse;
        Actor.add(fuse, Actor.now);
        Dungeon.level.drop(bomb, pos).sprite.drop();
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy != null) {
            Chill buff = enemy.buff(Chill.class);
            if(buff != null && buff.time>=8f){
                Buff.affect(enemy, Frost.class,buff.time);
                buff.detach();
            }
        }
        HP -= (int) (HT*0.05f);
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
}

