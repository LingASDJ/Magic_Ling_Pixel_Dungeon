package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class MayFlySprite extends MobSprite {
    public MayFlySprite(){
        super();

        texture(Assets.Sprites.MAYFLY);

        TextureFilm frames = new TextureFilm( texture, 20, 16 );
        idle = new Animation( 9, true );
        idle.frames( frames, 0,1,2,3);

        run = new Animation( 11, true );
        run.frames( frames, 4, 5, 6,7 );

        attack = new Animation( 11, false );
        attack.frames( frames, 8, 9,10);

        zap = attack.clone();

        die = new Animation( 11, false );
        die.frames( frames, 11, 12,13,14,15 );

        play(idle);
    }
    public void zap( int cell ) {

        super.zap( cell );

        MagicMissile.boltFromChar( parent,
                MagicMissile.BLOOD_CONE,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Mayfly)ch).onZapComplete();
                    }
                } );
//        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }
}
