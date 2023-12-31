package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class ApprenticeWitchSprite extends MobSprite {

    public ApprenticeWitchSprite() {
        super();

        texture( Assets.Sprites.APWHEEL );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0, 0, 0, 0, 0,1,2,3,3,3,3,3,2,1 );

        run = new Animation( 14, true );
        run.frames( frames, 14,15,16,17 );

        attack = new Animation( 14, false );
        attack.frames( frames, 4,5,6,7,8,9 );

        die = new Animation( 16, false );
        die.frames( frames, 10, 11,12, 13 );
        zap = attack.clone();
        play( idle );
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                3,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Elemental)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

}
