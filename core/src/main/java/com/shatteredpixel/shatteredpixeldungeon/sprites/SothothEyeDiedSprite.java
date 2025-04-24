package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.SothothEyeDied;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class SothothEyeDiedSprite extends MobSprite {

    private MovieClip.Animation changesBody;

    private MovieClip.Animation wwgy;

    public SothothEyeDiedSprite() {
        super();

        texture( Assets.Sprites.SOTS );

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new MovieClip.Animation( 7, false );
        idle.frames( frames, 36,37,38,39,40,41,42,43,44,45,46,47);

        run = new Animation( 10, true );
        run.frames( frames, 36,37,38,39,40,41,42,43,44,45,46,47);

        attack = new Animation( 15, false );
        attack.frames( frames, 54,55,56,57,58,59,60,61,62,63,64,65 );

        changesBody = new MovieClip.Animation( 7, false );
        changesBody.frames( frames, 144,145,146,147,148,149,150,151,152,153,154,155 );

        die = new MovieClip.Animation( 7, false );
        die.frames( frames, 179,180,181,182,183,184,185,186,187,188,189,190 );

        zap = new MovieClip.Animation( 10, false );
        zap.frames( frames, 107,108,109,110,111,112,113,114,115,116,117,118 );

        wwgy = new MovieClip.Animation( 10, true );
        wwgy.frames( frames, 198,199,200,201,202,203 );

        play( idle );
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( attack );

        MagicMissile.boltFromChar( parent,
                MagicMissile.FROSTFIRE,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((SothothEyeDied)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    public void setWWW( ){
        play( wwgy);
    }


}

