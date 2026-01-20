package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BleedCrystalSprite;
import com.watabou.utils.PathFinder;

public class BleedCrystal extends Mob {

    {
        spriteClass = BleedCrystalSprite.class;
        properties.add(Property.IMMOVABLE);
        HP = HT = 45;
        properties.add(Property.MINIBOSS);
        state = PASSIVE;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return 0;
    }

    @Override
    protected boolean act() {
        if(buff(DeadDogCerberus.HaloDeadBite.class)!=null){
            for (int i : PathFinder.NEIGHBOURS9) {
                GameScene.add( Blob.seed( pos + i, 1, DeadDogCerberus.DeadHaloFire.class ) );
            }
            die(true);
        }
        return  super.act();
    }

}
