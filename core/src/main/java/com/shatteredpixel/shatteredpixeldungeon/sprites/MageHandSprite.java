package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MageHand;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class MageHandSprite extends MobSprite {
    public MageHandSprite() {
        super();

        texture( Assets.Sprites.MAGEHAND );

        TextureFilm frames = new TextureFilm( texture, 24, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3,4,5);

        run = new Animation( 5, true );
        run.frames( frames, 0,1,2,3,4,5);

        attack = new Animation( 11, false );
        attack.frames( frames, 6,7,8,0 );

        die = new Animation( 11, false );
        die.frames( frames, 13,14,15,16,17);

        zap = new Animation(11,false);
        zap.frames(frames, 9,10,11,12);

        play( idle );
    }

    private int getWandMissileType(Wand wand) {
        if (wand != null) {
            return wand.getMissileType();
        }
        return MagicMissile.MAGIC_MISSILE;
    }

    public void zap( int cell ) {
        turnTo( ch.pos , cell );
        play( zap );

        MageHand mageHand = (MageHand)ch;
        Wand equippedWand = mageHand.getEquippedWand();

        int missileType = getWandMissileType(mageHand.equippedWand);

        if (equippedWand != null) {
            missileType = getWandMissileType(equippedWand);
        }

        MagicMissile.boltFromChar( parent,
                missileType,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        mageHand.onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }
}
