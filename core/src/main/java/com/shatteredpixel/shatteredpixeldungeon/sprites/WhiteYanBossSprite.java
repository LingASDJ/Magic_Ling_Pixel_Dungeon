package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.peach.WhiteYan;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class WhiteYanBossSprite extends MobSprite {

    private Animation cast;

    public WhiteYanBossSprite() {
        texture( Assets.Sprites.WHITEYAN );

        TextureFilm textureFilm = new TextureFilm(this.texture, 24, 24);

        idle = new MovieClip.Animation(9, true);
        idle.frames(textureFilm, 0,1,2,3,4,5);

        attack = new Animation( 20, false );
        attack.frames( textureFilm, 98, 99, 100, 101, 102 );

        cast = attack.clone();

        run = new MovieClip.Animation(10, true);
        run.frames(textureFilm, 105,106,107,108,109);

        die = new MovieClip.Animation(10, false);
        die.frames(textureFilm, 0);

        play(this.idle);
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            MagicMissile.boltFromChar( parent,
                    MagicMissile.SHAMAN_PURPLE,
                    this,
                    cell, () -> ((WhiteYan)ch).onZapComplete());
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
            turnTo( ch.pos , cell );
            play( cast );
        } else {
            super.attack( cell );
        }
    }

}
