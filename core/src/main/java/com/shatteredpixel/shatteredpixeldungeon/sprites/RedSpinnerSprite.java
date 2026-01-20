package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class RedSpinnerSprite extends MobSprite {

    public RedSpinnerSprite() {
        super();

        perspectiveRaise = 0f;

        texture( Assets.Sprites.RED_SPIDER );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 10, true );
        idle.frames( frames, 0, 0, 0, 0, 0, 1, 0, 1 );

        run = new MovieClip.Animation( 15, true );
        run.frames( frames, 0, 2, 0, 3 );

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 0, 4, 5, 0 );

        zap = attack.clone();

        die = new MovieClip.Animation( 12, false );
        die.frames( frames, 6, 7, 8, 9 );

        play( idle );
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        if (parent != null) {
            parent.sendToBack(this);
            if (aura != null){
                parent.sendToBack(aura);
            }
        }
        renderShadow = false;
    }

    public void zap( int cell ) {

        super.zap( cell );

        MagicMissile.boltFromChar( parent,
                MagicMissile.MAGIC_MISSILE,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Spinner)ch).shootWeb();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.MISS );
    }

    @Override
    public void onComplete( MovieClip.Animation anim ) {
        if (anim == zap) {
            play( run );
        }
        super.onComplete( anim );
    }

    @Override
    public int blood() {
        return 0xFFBFE5B8;
    }
}

