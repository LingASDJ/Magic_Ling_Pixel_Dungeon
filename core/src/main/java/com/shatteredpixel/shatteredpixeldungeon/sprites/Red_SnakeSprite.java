package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.utils.MobsUtilsRoom;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Red_SnakeSprite extends MobSprite {

    public Red_SnakeSprite() {
        super();

        texture( Assets.Sprites.RED_SNAKE);

        TextureFilm frames = new TextureFilm( texture, 12, 11 );

        //many frames here as we want the rising/falling to be slow but the tongue to be fast
        idle = new MovieClip.Animation( 10, true );
        idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 1, 1);

        run = new MovieClip.Animation( 8, true );
        run.frames( frames, 4, 5, 6, 7 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 8, 9, 10, 9, 0);

        die = new MovieClip.Animation( 10, false );
        die.frames( frames, 11, 12, 13 );

        play(idle);
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( attack );

        MagicMissile.boltFromChar( parent,
                MagicMissile.RAINBOW,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((MobsUtilsRoom.RedSnake)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

}
