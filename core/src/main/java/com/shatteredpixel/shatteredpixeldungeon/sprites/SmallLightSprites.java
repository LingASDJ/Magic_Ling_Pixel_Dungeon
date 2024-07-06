package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.SmallLight;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class SmallLightSprites extends MobSprite {

    public SmallLightSprites() {
        super();

        texture( Assets.Sprites.SMSLIGHT );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0,1,2 );

        run = new Animation( 8, true );
        run.frames( frames, 0,1,2 );

        die = new Animation( 8, false );
        die.frames( frames, 3,4,5,6 );

        zap = run.clone();

        play( idle );

    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.FROST,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((SmallLight)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

}
