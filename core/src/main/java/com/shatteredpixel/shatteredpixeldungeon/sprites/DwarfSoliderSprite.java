package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Tomahawk;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class DwarfSoliderSprite extends MobSprite {

    protected int texOffset(){
        return 0;
    }
    private Animation cast;
    public DwarfSoliderSprite() {
        super();

        texture(Assets.Sprites.DXL);

        int c = texOffset();

        TextureFilm frames = new TextureFilm(texture, 20, 16);

        idle = new MovieClip.Animation(4, true);
        idle.frames(frames, c, c, c,1+c,1+c,1+c);

        run = new MovieClip.Animation(12, true);
        run.frames(frames, 2+c,3+c,4+c,5+c);

        attack = new MovieClip.Animation(12, false);
        attack.frames(frames, 6+c,7+c,8+c);

        die = new MovieClip.Animation(9, false);
        die.frames(frames, 9+c,9+c,9+c,10+c,10+c,10+c);

        cast = attack.clone();

        play(idle);
    }

    public static class DwarfFuzeSprite extends DwarfSoliderSprite {
        @Override
        protected int texOffset() {
            return 11;
        }
        private Animation cast;
        @Override
        public void attack( int cell ) {
            if (!Dungeon.level.adjacent(cell, ch.pos)) {

                ((MissileSprite)parent.recycle( MissileSprite.class )).
                        reset( this, cell, new Tomahawk(), new Callback() {
                            @Override
                            public void call() {
                                ch.onAttackComplete();
                            }
                        } );
                cast = attack.clone();
                play( cast );
                turnTo( ch.pos , cell );
            } else {
                super.attack( cell );
            }
        }
    }


}

