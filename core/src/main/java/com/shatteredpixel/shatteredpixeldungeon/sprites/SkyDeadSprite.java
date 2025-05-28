package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.SkyDead;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class SkyDeadSprite extends MobSprite {

    private Animation cast;

    public SkyDeadSprite() {
        super();

        texture( Assets.Sprites.SKY_DEAD );

        TextureFilm frames = new TextureFilm( texture, 22, 24 );

        idle = new MovieClip.Animation( 2, true );
        idle.frames( frames, 0, 1, 2 );

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 3,4,5,6 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 7,8,9,10,0);

        die = new MovieClip.Animation( 10, false );
        die.frames( frames, 11,12,13,14 );

        cast = attack.clone();

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {
            if (!((SkyDead) ch).firstBurning){
                MagicMissile.boltFromChar( parent,
                        MagicMissile.SHAMAN_RED,
                        this,
                        cell,
                        () -> ((SkyDead)ch).onZapComplete());
            } else if(((SkyDead) ch).firstBurning) {
                MagicMissile.boltFromChar( parent,
                        MagicMissile.ELMO,
                        this,
                        cell,
                        () -> ((SkyDead)ch).onZapComplete());
            }
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
            turnTo( ch.pos , cell );
            play( cast );

        } else {
            super.attack( cell );
        }
    }

}
