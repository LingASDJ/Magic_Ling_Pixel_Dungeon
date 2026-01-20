package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class ShadowHunstmanSprites extends MobSprite {
    public ShadowHunstmanSprites() {
        texture("sprites/SRPD/ShadowHunstman.png");
        TextureFilm frames = new TextureFilm(this.texture, 22, 16);
        this.idle = new MovieClip.Animation(2, true);
        this.idle.frames(frames, new Object[]{0, 0, 0, 1, 0, 0, 1, 1});
        this.run = new MovieClip.Animation(12, true);
        this.run.frames(frames, new Object[]{2, 3, 4, 5, 6, 7});
        this.attack = new MovieClip.Animation(12, false);
        this.attack.frames(frames, new Object[]{8, 9, 10});
        this.die = new MovieClip.Animation(12, false);
        this.die.frames(frames, new Object[]{11, 12, 13});
        play(this.idle);
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cell, new ParalyticDart(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );

            play( attack );
            turnTo( ch.pos , cell );

        } else {

            super.attack( cell );

        }
    }
}
