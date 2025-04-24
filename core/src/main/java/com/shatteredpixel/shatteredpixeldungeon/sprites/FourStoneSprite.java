package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class FourStoneSprite extends MobSprite {

    public FourStoneSprite() {

        texture( Assets.Sprites.FSOE );

        TextureFilm ren = new TextureFilm(this.texture, 16, 16);

        idle = new MovieClip.Animation(3, true);
        idle.frames(ren, 1);

        run = new MovieClip.Animation(10, true);
        run.frames(ren, 1);

        die = new MovieClip.Animation(10, false);
        die.frames(ren, 1);

        play(this.idle);
    }

    public static class FrostFourStoneSprite extends MobSprite {

        public FrostFourStoneSprite() {
            texture( Assets.Sprites.FSOE );

            TextureFilm ren = new TextureFilm(this.texture, 16, 16);

            idle = new MovieClip.Animation(3, true);
            idle.frames(ren, 2);

            run = new MovieClip.Animation(10, true);
            run.frames(ren, 2);

            die = new MovieClip.Animation(10, false);
            die.frames(ren, 2);

            play(this.idle);
        }

    }

    public static class PoisonFourStoneSprite extends MobSprite {

        public PoisonFourStoneSprite() {
            texture( Assets.Sprites.FSOE );

            TextureFilm ren = new TextureFilm(this.texture, 16, 16);

            idle = new MovieClip.Animation(3, true);
            idle.frames(ren, 3);

            run = new MovieClip.Animation(10, true);
            run.frames(ren, 3);

            die = new MovieClip.Animation(10, false);
            die.frames(ren, 3);

            play(this.idle);
        }

    }

    public static class MagicFourStoneSprite extends MobSprite {

        public MagicFourStoneSprite() {
            texture( Assets.Sprites.FSOE );

            TextureFilm ren = new TextureFilm(this.texture, 16, 16);

            idle = new MovieClip.Animation(3, true);
            idle.frames(ren, 0);

            run = new MovieClip.Animation(10, true);
            run.frames(ren, 0);

            die = new MovieClip.Animation(10, false);
            die.frames(ren, 0);

            play(this.idle);
        }

    }



}
